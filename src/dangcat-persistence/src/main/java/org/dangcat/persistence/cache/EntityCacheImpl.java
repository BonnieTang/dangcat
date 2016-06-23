package org.dangcat.persistence.cache;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.utils.DateUtils;
import org.dangcat.framework.service.ServiceHelper;
import org.dangcat.framework.service.impl.ServiceFactory;
import org.dangcat.persistence.entity.EntityLoader;
import org.dangcat.persistence.entity.EntityManager;
import org.dangcat.persistence.entity.EntityManagerFactory;
import org.dangcat.persistence.filter.FilterExpress;

import java.util.Collection;
import java.util.List;

/**
 * ʵ�建�档
 * @author dangcat
 * 
 */
public class EntityCacheImpl<T> extends MemCacheImpl<T> implements EntityCache<T>
{
    private String databaseName = null;
    private EntityLoader<T> entityLoader = null;
    private boolean isPreload = false;
    private Class<?> loaderClass = null;

    public EntityCacheImpl(Class<T> classType, String tableName)
    {
        super(classType, tableName);
    }

    private void addCache(Collection<T> dataCollection)
    {
        if (dataCollection != null)
        {
            for (T data : dataCollection)
                this.add(data);
        }
    }

    /**
     * ɾ��ָ�������Ļ������ݺ����ݿ����ݡ�
     */
    @Override
    public void delete(FilterExpress filterExpress)
    {
        if (filterExpress != null)
            this.getEntityManager().delete(this.getClassType(), filterExpress);
    }

    /**
     * ɾ���������ݺ����ݿ����ݡ�
     */
    @Override
    public void delete(T data)
    {
        if (data != null)
            this.getEntityManager().delete(data);
    }

    public String getDatabaseName()
    {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @SuppressWarnings("unchecked")
    private EntityLoader<T> getEntityLoader()
    {
        if (this.loaderClass != null && this.entityLoader == null)
        {
            this.entityLoader = (EntityLoader<T>) ReflectUtils.newInstance(this.loaderClass);
            if (this.entityLoader != null)
                ServiceHelper.inject(ServiceFactory.getInstance(), this.entityLoader);
        }
        return this.entityLoader;
    }

    private EntityManager getEntityManager()
    {
        return EntityManagerFactory.getInstance().open(this.getDatabaseName());
    }

    public Class<?> getLoaderClass()
    {
        return this.loaderClass;
    }

    public void setLoaderClass(Class<?> loaderClass) {
        this.loaderClass = loaderClass;
    }

    public boolean isPreload()
    {
        return this.isPreload;
    }

    public void setPreload(boolean isPreload) {
        this.isPreload = isPreload;
    }

    /**
     * ����ָ�������������ݿ�������ݺ���ӵ����档
     * @param filterExpress ����������
     * @return ���ݼ��ϡ�
     */
    @Override
    public Collection<T> load(FilterExpress filterExpress)
    {
        Collection<T> dataCollection = this.find(filterExpress);
        if (dataCollection == null)
        {
            EntityManager entityManager = this.getEntityManager();
            dataCollection = entityManager.load(this.getClassType(), filterExpress);
            if (dataCollection != null)
                this.addCache(dataCollection);
        }
        return dataCollection;
    }

    /**
     * ��������ֵ�ҵ���¼�С�
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    @Override
    public T load(Object... params)
    {
        T data = this.locate(params);
        if (data == null)
        {
            EntityManager entityManager = this.getEntityManager();
            data = entityManager.load(this.getClassType(), params);
            if (data != null)
                this.add(data);
        }
        return data;
    }

    /**
     * ����ָ�����ֶ�ֵ�������ݡ�
     * @param fieldNames �ֶ��������ֶ��Էֺż����
     * @param values �ֶ���ֵ���������ֶζ�Ӧ��
     * @return �ҵ��ļ�¼�С�
     */
    @Override
    public Collection<T> load(String[] fieldNames, Object... values)
    {
        Collection<T> dataCollection = this.find(fieldNames, values);
        if (dataCollection == null || dataCollection.size() == 0)
        {
            EntityManager entityManager = this.getEntityManager();
            dataCollection = entityManager.load(this.getClassType(), fieldNames, values);
            if (dataCollection != null)
                this.addCache(dataCollection);
        }
        return dataCollection;
    }

    public void loadData()
    {
        EntityManager entityManager = this.getEntityManager();
        long beginTime = DateUtils.currentTimeMillis();
        List<T> dataList = null;
        EntityLoader<T> entityLoader = this.getEntityLoader();
        if (entityLoader != null)
            dataList = entityLoader.load(entityManager);
        else
            dataList = entityManager.load(this.getClassType());
        if (dataList != null)
        {
            this.clear(true);
            this.addCache(dataList);
            logger.info("The cache " + this.getClassType() + " preload cost time " + (DateUtils.currentTimeMillis() - beginTime) + " ms and size is " + dataList.size());
        }
    }

    /**
     * ˢ���ڴ����ݡ�
     * @param data Ŀ�����ݡ�
     * @return ˢ�º����ݡ�
     */
    @Override
    public T refresh(T data)
    {
        T entity = null;
        if (data != null)
        {
            EntityManager entityManager = this.getEntityManager();
            entity = entityManager.refresh(data);
            if (entity == null)
                this.remove(data);
        }
        return entity;
    }

    /**
     * ˢ��ָ���������ڴ����ݡ�
     * @param primaryKeys Ŀ���������ݡ�
     * @return ˢ�º����ݡ�
     */
    @Override
    public T refreshEntity(Object... primaryKeys)
    {
        T entity = null;
        if (primaryKeys != null && primaryKeys.length > 0)
        {
            EntityManager entityManager = this.getEntityManager();
            entity = entityManager.load(this.getClassType(), primaryKeys);
            T data = this.locate(primaryKeys);
            if (data != null)
                this.remove(data);
            if (entity != null)
                this.add(entity);
        }
        return entity;
    }

    /**
     * ɾ��ָ�������Ļ������ݡ�
     */
    @Override
    public Collection<T> remove(FilterExpress filterExpress)
    {
        Collection<T> dataCollection = null;
        if (filterExpress != null)
        {
            dataCollection = this.find(filterExpress);
            if (dataCollection != null)
            {
                for (T data : dataCollection)
                    this.remove(data);
            }
        }
        return dataCollection;
    }

    /**
     * ��ӻ������ݡ�
     */
    @Override
    public void save(T data)
    {
        if (data != null)
            this.getEntityManager().save(data);
    }
}
