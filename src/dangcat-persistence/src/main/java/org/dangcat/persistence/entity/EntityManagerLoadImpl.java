package org.dangcat.persistence.entity;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.DateUtils;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.model.DataState;
import org.dangcat.persistence.model.DataStatus;
import org.dangcat.persistence.model.Range;
import org.dangcat.persistence.orm.JdbcValueUtils;
import org.dangcat.persistence.orm.Session;
import org.dangcat.persistence.orm.SqlBuilder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ʵ�������������
 * @author dangcat
 * 
 */
class EntityManagerLoadImpl<T>
{
    private static final Logger logger = Logger.getLogger(EntityManager.class);
    /** ʵ��������� */
    private EntityManagerImpl entityManager = null;

    EntityManagerLoadImpl(EntityManagerImpl entityManager)
    {
        this.entityManager = entityManager;
    }

    private void calculateRowNum(List<T> entityList, Range range)
    {
        Integer startRow = null;
        if (range != null)
            startRow = range.getFrom();
        EntityCalculator.calculateRowNum(entityList, startRow);
    }

    private void calculateTotalSize(LoadEntityContext loadEntityContext, EntitySqlBuilder entitySqlBuilder) throws EntityException
    {
        Range range = loadEntityContext.getRange();
        // �����¼������
        if (range != null && range.isCalculateTotalSize())
        {
            int totalSize = 0;
            SqlBuilder sqlBuilder = entitySqlBuilder.buildTotalSizeStatement();
            if (sqlBuilder.length() > 0)
            {
                // ��ȡ�Ự����
                Session session = null;
                try
                {
                    session = this.entityManager.openSession();
                    ResultSet resultSet = session.executeQuery(sqlBuilder.toString());
                    while (resultSet.next())
                        totalSize = resultSet.getInt(Range.TOTALSIZE);
                    resultSet.close();
                }
                catch (Exception e)
                {
                    if (logger.isDebugEnabled())
                        logger.error(this, e);
                    throw new EntityException(sqlBuilder.toString(), e);
                }
                finally
                {
                    if (session != null)
                        session.release();
                }
            }
            range.setTotalSize(totalSize);
        }
    }

    /**
     * �ҵ�ָ��������ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param primaryKeys ����������
     * @return �ҵ���ʵ�����
     */
    protected T load(Class<T> entityClass, Object... primaryKeyValues) throws EntityException
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entityClass);

        if (entityMetaData == null)
            throw new EntityException(entityClass + " is not exists.");

        if (!entityMetaData.isPrimaryKeyValueValid(primaryKeyValues))
            throw new EntityException("The primarykey is invalid.");

        T entity = null;
        List<T> entityList = this.load(new LoadEntityContext(entityClass, primaryKeyValues));
        if (entityList != null && entityList.size() > 0)
            entity = entityList.get(0);
        return entity;
    }

    /**
     * �ҵ�ָ�����Ե�ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param primaryKeys ����������
     * @return �ҵ���ʵ�����
     */
    protected List<T> load(Class<T> entityClass, String[] fieldNames, Object[] values) throws EntityException
    {
        return this.load(new LoadEntityContext(entityClass, fieldNames, values));
    }

    /**
     * ����ָ����������Χ������Ҫ�����ʵ�����
     * @param <T> ʵ�����͡�
     * @param loadEntityContext ���������ġ�
     * @return ��ѯ�����
     */
    protected List<T> load(LoadEntityContext loadEntityContext) throws EntityException
    {
        loadEntityContext.setEntityManager(this.entityManager);
        loadEntityContext.initialize();
        List<T> entityList = null;
        SqlBuilder sqlBuilder = null;
        Session session = null;
        try
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(loadEntityContext.getEntityClass());
            if (entityMetaData == null || !EntityUtils.exists(entityMetaData, loadEntityContext))
                return entityList;

            EntitySqlBuilder entitySqlBuilder = new EntitySqlBuilder(entityMetaData, this.entityManager.getDatabaseName(), loadEntityContext);
            // �������ݡ�
            sqlBuilder = entitySqlBuilder.buildLoadStatement();
            if (sqlBuilder.length() > 0)
            {
                long beginTime = DateUtils.currentTimeMillis();
                if (logger.isDebugEnabled())
                    logger.debug("Begin load the entity " + loadEntityContext.getEntityClass().getName());
                // �����¼������
                this.calculateTotalSize(loadEntityContext, entitySqlBuilder);
                if (loadEntityContext.beforeLoad())
                {
                    // ��ȡ�Ự����
                    session = this.entityManager.openSession();
                    sqlBuilder = entitySqlBuilder.buildLoadStatement();
                    ResultSet resultSet = session.executeQuery(sqlBuilder.toString());

                    // �����λû�й�����Ҫ�Զ����ɡ�
                    if (entityMetaData.getTable().getColumns().size() == 0)
                        session.loadMetaData(entityMetaData.getTable(), resultSet);

                    // �������ݽ����
                    entityList = this.parse(entityMetaData, resultSet, loadEntityContext.getRange(), loadEntityContext.getEntity());
                    loadEntityContext.setEntityCollection(entityList);
                    this.calculateRowNum(entityList, loadEntityContext.getRange());
                    loadEntityContext.afterLoad();
                }
                if (logger.isDebugEnabled())
                    logger.debug("End load entity " + loadEntityContext.getEntityClass().getName() + " cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
            }
        }
        catch (EntityException e)
        {
            loadEntityContext.onLoadError(e);
            throw e;
        }
        catch (Exception e)
        {
            if (sqlBuilder != null)
                logger.error(sqlBuilder.toString());
            if (logger.isDebugEnabled())
                logger.error(this, e);
            throw new EntityException(this.toString(), e);
        }
        finally
        {
            if (session != null)
                session.release();
        }

        // ���������¼���
        if (entityList != null)
        {
            for (T entity : entityList)
                RelationManager.load(loadEntityContext, entity);
        }
        return entityList;
    }

    /**
     * �����ݿ�ˢ��ʵ��ʵ�����ݡ�
     * @param <T>
     * @param entity ʵ�����
     * @throws EntityException �����쳣��
     */
    protected T load(T entity) throws EntityException
    {
        T destEntity = null;
        if (entity != null)
        {
            List<T> entityList = this.load(new LoadEntityContext(entity));
            if (entityList != null && entityList.size() > 0)
                destEntity = entityList.get(0);
        }
        return destEntity;
    }

    /**
     * �������ݽ����
     * @param row �����ж���
     * @param resultSet ��ѯ�����
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    private List<T> parse(EntityMetaData entityMetaData, ResultSet resultSet, Range range, Object sourceEntity) throws SQLException
    {
        List<T> entityList = null;
        int position = 0;
        // ��ȡԪ���ݡ�
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        while (resultSet.next())
        {
            position++;
            if (range != null && range.getMode() != Range.BY_SQLSYNTAX)
            {
                if (position < range.getFrom())
                    continue;
                else if (position > range.getTo())
                    break;
            }

            try
            {
                if (entityList == null)
                    entityList = new ArrayList<T>();

                T entity = null;
                if (sourceEntity != null && entityList.size() == 0)
                    entity = (T) sourceEntity;
                else
                    entity = (T) entityMetaData.getEntityClass().newInstance();
                for (int i = 1; i <= columnCount; i++)
                {
                    String fieldName = resultSetMetaData.getColumnLabel(i);
                    EntityField entityField = entityMetaData.getEntityFieldByFieldName(fieldName);
                    if (entityField != null)
                    {
                        Object value = JdbcValueUtils.read(fieldName, resultSet, entityField.getClassType());
                        entityField.setValue(entity, value);
                    }
                }
                // ��������״̬��
                if (entity instanceof DataStatus)
                {
                    DataStatus dataStatus = (DataStatus) entity;
                    dataStatus.setDataState(DataState.Browse);
                }
                entityList.add(entity);
            }
            catch (InstantiationException e)
            {
            }
            catch (IllegalAccessException e)
            {
            }
        }
        return entityList;
    }
}
