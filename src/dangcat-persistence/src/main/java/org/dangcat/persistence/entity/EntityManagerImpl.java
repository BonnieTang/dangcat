package org.dangcat.persistence.entity;

import org.apache.log4j.Logger;
import org.dangcat.framework.pool.SessionException;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.exception.TableException;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.model.Range;
import org.dangcat.persistence.orderby.OrderBy;
import org.dangcat.persistence.orm.Session;
import org.dangcat.persistence.orm.SessionFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * ʵ���������
 * @author dangcat
 * 
 */
public class EntityManagerImpl implements EntityManager
{
    protected static final Logger logger = Logger.getLogger(EntityManager.class);
    /** ���ݿ����� */
    private String databaseName = null;
    private ThreadLocal<Collection<SaveEntityContext>> saveEntityContextesThreadLocal = new ThreadLocal<Collection<SaveEntityContext>>();
    private ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>();

    public EntityManagerImpl()
    {
    }

    public EntityManagerImpl(String databaseName)
    {
        this.databaseName = databaseName;
    }

    protected void addSaveEntityContext(SaveEntityContext saveEntityContext)
    {
        Collection<SaveEntityContext> saveEntityContextes = this.saveEntityContextesThreadLocal.get();
        if (saveEntityContextes == null)
        {
            saveEntityContextes = new LinkedList<SaveEntityContext>();
            this.saveEntityContextesThreadLocal.set(saveEntityContextes);
        }
        saveEntityContextes.add(saveEntityContext);
    }

    @Override
    public Session beginTransaction()
    {
        Session session = this.sessionThreadLocal.get();
        if (session == null)
        {
            try
            {
                session = this.openSession();
                session.beginTransaction();
                this.sessionThreadLocal.set(session);
            }
            catch (Exception e)
            {
                logger.error(this, e);
            }
        }
        return session;
    }

    @Override
    public void commit()
    {
        try
        {
            Session session = this.sessionThreadLocal.get();
            if (session != null)
            {
                session.commit();
                Collection<SaveEntityContext> saveEntityContextes = this.saveEntityContextesThreadLocal.get();
                if (saveEntityContextes != null)
                {
                    for (SaveEntityContext saveEntityContext : saveEntityContextes)
                        saveEntityContext.afterCommit();
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(this, e);
        }
        finally
        {
            this.release();
        }
    }

    /**
     * ɾ��ָ������������ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    @Override
    public <T> int delete(Class<T> entityClass, FilterExpress filterExpress) throws EntityException
    {
        return this.getEntityManagerDeleteImpl().delete(new DeleteEntityContext(entityClass, filterExpress));
    }

    /**
     * ɾ��ָ��������ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param primaryKeyValues ����ֵ��
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    @Override
    public <T> int delete(Class<T> entityClass, Object... primaryKeyValues) throws EntityException
    {
        return this.getEntityManagerDeleteImpl().delete(entityClass, primaryKeyValues);
    }

    /**
     * ɾ��ָ�����Եĵ�ʵ�塣
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param fieldNames �ֶ����б�
     * @param values ����ֵ��
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    @Override
    public <T> int delete(Class<T> entityClass, String[] fieldNames, Object... values) throws EntityException
    {
        return this.getEntityManagerDeleteImpl().delete(entityClass, fieldNames, values);
    }

    /**
     * ����������ɾ��ָ����ʵ�����
     * @param <T> ʵ�����͡�
     * @param deleteEntityContext ɾ��ʵ�������ġ�
     * @return �Ƿ�ɾ����
     * @throws TableException
     */
    @Override
    public int delete(DeleteEntityContext deleteEntityContext) throws EntityException
    {
        return this.getEntityManagerDeleteImpl().delete(deleteEntityContext);
    }

    /**
     * ɾ��ָ����ʵ�����
     * @param <T> ʵ�����͡�
     * @param entites ɾ��ʵ�����
     * @return �Ƿ�ɾ����
     * @throws TableException
     */
    @Override
    public int delete(Object... entites) throws EntityException
    {
        return this.getEntityManagerDeleteImpl().delete(entites);
    }

    /**
     * ���ݿ����ơ�
     */
    protected String getDatabaseName()
    {
        return this.databaseName;
    }

    private EntityManagerDeleteImpl getEntityManagerDeleteImpl()
    {
        return new EntityManagerDeleteImpl(this);
    }

    protected EntityManagerSaveImpl getEntityManagerSaveImpl()
    {
        return new EntityManagerSaveImpl(this);
    }

    protected Session getSession()
    {
        return this.sessionThreadLocal.get();
    }

    /**
     * ����ָ�����͵�ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @return ��ѯ�����
     */
    @Override
    public <T> List<T> load(Class<T> entityClass) throws EntityException
    {
        return this.load(new LoadEntityContext(entityClass));
    }

    /**
     * ���չ�����������ָ�����͵�ʵ�塣
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     */
    @Override
    public <T> List<T> load(Class<T> entityClass, FilterExpress filterExpress) throws EntityException
    {
        return this.load(new LoadEntityContext(entityClass, filterExpress));
    }

    /**
     * ���չ�����������Χ����ָ�����͵�ʵ�塣
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     * @param range ���뷶Χ��
     * @return ��ѯ�����
     */
    public <T> List<T> load(Class<T> entityClass, FilterExpress filterExpress, Range range) throws EntityException
    {
        return this.load(new LoadEntityContext(entityClass, filterExpress, range));
    }

    /**
     * ���չ�����������Χ��������������ָ�����͵�ʵ�塣
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     * @param range ���뷶Χ��
     * @param orderBy ����������
     * @return ��ѯ�����
     */
    public <T> List<T> load(Class<T> entityClass, FilterExpress filterExpress, Range range, OrderBy orderBy) throws EntityException
    {
        return this.load(new LoadEntityContext(entityClass, filterExpress, range, orderBy));
    }

    /**
     * �ҵ�ָ��������ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param primaryKeys ����������
     * @return �ҵ���ʵ�����
     */
    @Override
    public <T> T load(Class<T> entityClass, Object... primaryKeyValues) throws EntityException
    {
        EntityManagerLoadImpl<T> entityManagerLoad = new EntityManagerLoadImpl<T>(this);
        return entityManagerLoad.load(entityClass, primaryKeyValues);
    }

    /**
     * �ҵ�ָ�����Ե�ʵ�����
     * @param <T> ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param fieldNames �ֶ����б�
     * @param values ����ֵ��
     * @return �ҵ���ʵ�����
     */
    @Override
    public <T> List<T> load(Class<T> entityClass, String[] fieldNames, Object... values) throws EntityException
    {
        EntityManagerLoadImpl<T> entityManagerLoad = new EntityManagerLoadImpl<T>(this);
        return entityManagerLoad.load(entityClass, fieldNames, values);
    }

    /**
     * ����ָ�����͵�ʵ�����
     * @param <T> ʵ�����͡�
     * @param loadEntityContext ����ʵ�������ġ�
     * @return ��ѯ�����
     */
    @Override
    public <T> List<T> load(LoadEntityContext loadEntityContext) throws EntityException
    {
        EntityManagerLoadImpl<T> entityManagerLoad = new EntityManagerLoadImpl<T>(this);
        return entityManagerLoad.load(loadEntityContext);
    }

    /**
     * �����Ự��
     * @return �Ự����
     * @throws SessionException ���ݿ��쳣��
     */
    protected Session openSession() throws SessionException
    {
        return SessionFactory.getInstance().openSession(this.getDatabaseName());
    }

    /**
     * �����ݿ�ˢ��ʵ��ʵ�����ݡ�
     * @param <T>
     * @param entity ʵ�����
     * @throws EntityException �����쳣��
     */
    public <T> T refresh(T entity) throws EntityException
    {
        EntityManagerLoadImpl<T> entityManagerLoad = new EntityManagerLoadImpl<T>(this);
        return entityManagerLoad.load(entity);
    }

    private void release()
    {
        Session session = this.sessionThreadLocal.get();
        if (session != null)
            session.release();
        this.sessionThreadLocal.remove();
        this.saveEntityContextesThreadLocal.remove();
    }

    @Override
    public void rollback()
    {
        try
        {
            Session session = this.sessionThreadLocal.get();
            if (session != null)
                session.rollback();
        }
        finally
        {
            this.release();
        }
    }

    /**
     * ����ָ��ʵ�����
     * @param <T> ʵ�����͡�
     * @param entities ����ʵ�����
     * @return ��������
     */
    @Override
    public void save(Object... entities) throws EntityException
    {
        this.save(new SaveEntityContext(), entities);
    }

    /**
     * ����ָ��ʵ�����
     * @param <T> ʵ�����͡�
     * @param saveEntityContext ����ʵ�������ġ�
     * @param entities ����ʵ�����
     * @return ��������
     */
    @Override
    public void save(SaveEntityContext saveEntityContext, Object... entities) throws EntityException
    {
        if (entities != null && entities.length > 0 && saveEntityContext != null)
        {
            saveEntityContext.setEntityManager(this);
            for (Object entity : entities)
                saveEntityContext.save(entity);
            if (saveEntityContext.size() > 0)
            {
                EntityManagerSaveImpl entityManagerSave = this.getEntityManagerSaveImpl();
                entityManagerSave.setSaveEntityContext(saveEntityContext);
                entityManagerSave.execute();
            }
        }
    }
}
