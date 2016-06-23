package org.dangcat.persistence.entity;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.DateUtils;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.exception.TableException;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.orm.Session;
import org.dangcat.persistence.orm.SqlBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * ʵ��ɾ����������
 * @author dangcat
 * 
 */
class EntityManagerDeleteImpl extends EntityManagerTransaction
{
    private static final Logger logger = Logger.getLogger(EntityManager.class);

    protected EntityManagerDeleteImpl(EntityManagerImpl entityManager)
    {
        super(entityManager);
    }

    /**
     * ɾ��ָ��������ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param primaryKeyValues ����ֵ��
     * @return �Ƿ�ɾ����
     * @throws TableException
     */
    protected int delete(Class<?> entityClass, Object[] primaryKeyValues) throws EntityException
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entityClass);

        if (entityMetaData == null)
            throw new EntityException(entityClass + " is not exists.");

        if (!entityMetaData.isPrimaryKeyValueValid(primaryKeyValues))
            throw new EntityException("The primarykey is invalid.");

        List<String> primaryKeyList = new LinkedList<String>();
        for (EntityField entityField : entityMetaData.getPrimaryKeyFieldCollection())
            primaryKeyList.add(entityField.getFilterFieldName());
        return this.delete(entityClass, primaryKeyList.toArray(new String[0]), primaryKeyValues);
    }

    /**
     * ɾ��ָ������������ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param fieldNames �ֶ����ơ�
     * @param values ����ֵ��
     * @return �Ƿ�ɾ����
     * @throws TableException
     */
    protected int delete(Class<?> entityClass, String[] fieldNames, Object[] values) throws EntityException
    {
        if (fieldNames != null && values != null && fieldNames.length == values.length)
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entityClass);
            if (entityMetaData == null)
                return 0;

            FilterExpress filterExpress = entityMetaData.createFilterExpress(fieldNames, values);
            return this.delete(new DeleteEntityContext(entityClass, filterExpress));
        }
        return 0;
    }

    /**
     * ����������ɾ��ʵ�����
     * @param <T> ʵ�����͡�
     * @param deleteEntityContext ɾ�������Ķ���
     * @return ɾ������Ŀ��
     * @throws EntityException �����쳣��
     */
    protected int delete(DeleteEntityContext deleteEntityContext) throws EntityException
    {
        this.prepare(deleteEntityContext);
        Class<?> entityClass = deleteEntityContext.getEntityClass();
        int result = 0;
        SqlBuilder sqlBuilder = null;
        try
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entityClass);
            if (entityMetaData == null || !EntityUtils.exists(entityMetaData, deleteEntityContext))
                return 0;

            EntitySqlBuilder entitySqlBuilder = new EntitySqlBuilder(entityMetaData, this.getDatabaseName(), deleteEntityContext);
            // �������ݡ�
            sqlBuilder = entitySqlBuilder.buildDeleteStatement();
            if (sqlBuilder.length() > 0)
            {
                long beginTime = DateUtils.currentTimeMillis();
                if (logger.isDebugEnabled())
                    logger.debug("Begin delete the entity: " + entityClass.getClass().getName());

                if (deleteEntityContext.beforeDelete())
                {
                    // ��ȡ�Ự����
                    Session session = this.beginTransaction();
                    result = session.executeBatch(sqlBuilder.getBatchSqlList());
                    deleteEntityContext.afterDelete();
                    this.commit();
                }
                if (logger.isDebugEnabled())
                    logger.debug("End delete the entity " + entityClass.getClass().getName() + " cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
            }
        }
        catch (EntityException e)
        {
            this.rollback();
            deleteEntityContext.onDeleteError(e);

            throw e;
        }
        catch (Exception e)
        {
            throw new EntityException(sqlBuilder.toString(), e);
        }
        return result;
    }

    /**
     * ɾ��ָ����ʵ�����
     * @param <T> ʵ�����͡�
     * @param entity ʵ�����
     * @return �Ƿ�ɾ����
     * @throws TableException
     */
    protected int delete(Object... entities) throws EntityException
    {
        int result = 0;
        if (entities != null && entities.length > 0)
        {
            SaveEntityContext saveEntityContext = new SaveEntityContext();
            this.prepare(saveEntityContext);
            for (Object entity : entities)
                saveEntityContext.delete(entity);

            if (saveEntityContext.size() > 0)
            {
                EntityManagerSaveImpl entityManagerSave = this.entityManager.getEntityManagerSaveImpl();
                entityManagerSave.setSaveEntityContext(saveEntityContext);
                entityManagerSave.execute();
            }
            result = saveEntityContext.size();
        }
        return result;
    }
}
