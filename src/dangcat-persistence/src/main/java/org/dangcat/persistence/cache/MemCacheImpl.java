package org.dangcat.persistence.cache;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.DateUtils;
import org.dangcat.persistence.entity.EntityHelper;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.index.IndexManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * �ڴ����ݻ��档
 *
 * @author dangcat
 */
public class MemCacheImpl<T> implements MemCache<T> {
    protected static final Logger logger = Logger.getLogger(EntityCache.class);
    private Collection<T> accesssCollection = Collections.synchronizedCollection(new HashSet<T>());
    private Class<T> classType = null;
    private IndexManager<T> indexManager = new IndexManager<T>();
    private Integer interval = null;
    private long lastCleanTime = DateUtils.currentTimeMillis();
    private boolean notify = false;
    private String tableName = null;

    public MemCacheImpl(Class<T> classType, String tableName) {
        this.classType = classType;
        this.tableName = tableName;
    }

    /**
     * ��ӻ������ݡ�
     */
    public void add(T entity) {
        this.indexManager.add(entity);
    }

    private void addAccess(Collection<T> dataCollection) {
        if (dataCollection != null) {
            for (T data : dataCollection)
                this.addAccess(data);
        }
    }

    private void addAccess(T data) {
        if (data != null)
            this.accesssCollection.add(data);
    }

    @SuppressWarnings("unchecked")
    public void addEntities(Object... entities) {
        if (entities != null) {
            for (Object entity : entities) {
                if (this.isValidClassType(entity))
                    this.indexManager.add((T) entity);
            }
        }
    }

    /**
     * ���������
     *
     * @param indexName    ��������
     * @param isPrimaryKey �Ƿ�������������
     */
    public void appendIndex(String indexName, boolean isPrimaryKey) {
        this.indexManager.appendIndex(indexName, isPrimaryKey);
    }

    /**
     * ������档
     */
    public void clear(boolean force) {
        if (force || this.isTimeOut()) {
            this.accesssCollection = Collections.synchronizedCollection(new HashSet<T>());
            IndexManager<T> indexManager = new IndexManager<T>();
            for (String indexName : this.indexManager.getIndexNameSet()) {
                boolean isPrimaryKey = indexName.equalsIgnoreCase(this.indexManager.getPrimaryKeyIndex());
                indexManager.appendIndex(indexName, isPrimaryKey);
            }
            this.indexManager = indexManager;
        }
    }

    /**
     * ����ָ�����������ڴ�������ݡ�
     *
     * @param filterExpress ����������
     * @return ���ݼ��ϡ�
     */
    public Collection<T> find(FilterExpress filterExpress) {
        Collection<T> dataCollection = this.indexManager.find(filterExpress);
        this.addAccess(dataCollection);
        return dataCollection;
    }

    /**
     * ����ָ�����ֶ�ֵ�������ݡ�
     *
     * @param fieldNames �ֶ��������ֶ��Էֺż����
     * @param values     �ֶ���ֵ���������ֶζ�Ӧ��
     * @return �ҵ��ļ�¼�С�
     */
    public Collection<T> find(String[] fieldNames, Object... values) {
        Collection<T> dataCollection = this.indexManager.find(fieldNames, values);
        this.addAccess(dataCollection);
        return dataCollection;
    }

    public Class<T> getClassType() {
        return this.classType;
    }

    public Collection<T> getDataCollection() {
        return this.indexManager.getDataCollection();
    }

    /**
     * ������������������
     */
    public int getIndexSize() {
        return this.indexManager.getIndexNameSet().size();
    }

    public Integer getInterval() {
        return this.interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getTableName() {
        return this.tableName;
    }

    public boolean isNotify() {
        return this.notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    private boolean isTimeOut() {
        if (this.getInterval() == null)
            return false;
        return DateUtils.currentTimeMillis() - this.lastCleanTime > this.getInterval() * 1000;
    }

    private boolean isValidClassType(Object entity) {
        return entity != null && this.getClassType().equals(entity.getClass());
    }

    /**
     * ��������ֵ�ҵ���¼�С�
     *
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    public T locate(Object... params) {
        T data = this.indexManager.find(params);
        this.addAccess(data);
        return data;
    }

    /**
     * ���ݱ仯֪ͨ�޸�������
     *
     * @param entities ���޸ĵļ�¼����
     */
    @SuppressWarnings("unchecked")
    public void modifyEntities(Object... entities) {
        if (entities != null) {
            for (Object entity : entities) {
                if (this.isValidClassType(entity))
                    this.indexManager.update(null, (T) entity);
                else
                    this.removeEntityByPrimaryKeyValues(entity);
            }
        }
    }

    /**
     * ɾ��ָ�������Ļ������ݡ�
     */
    public Collection<T> remove(FilterExpress filterExpress) {
        Collection<T> dataCollection = null;
        if (filterExpress != null) {
            dataCollection = this.find(filterExpress);
            if (dataCollection != null) {
                for (T data : dataCollection)
                    this.remove(data);
            }
        }
        return dataCollection;
    }

    /**
     * ɾ���������ݡ�
     */
    public boolean remove(T data) {
        boolean result = this.indexManager.remove(data);
        this.removeAccess(data);
        return result;
    }

    private void removeAccess(T data) {
        if (data != null)
            this.accesssCollection.remove(data);
    }

    @SuppressWarnings("unchecked")
    public int removeEntities(Object... entities) {
        int count = 0;
        if (entities != null) {
            for (Object entity : entities) {
                if (this.isValidClassType(entity)) {
                    if (this.indexManager.remove((T) entity)) {
                        this.removeAccess((T) entity);
                        count++;
                    }
                } else
                    this.removeEntityByPrimaryKeyValues(entity);
            }
        }
        return count;
    }

    /**
     * ɾ��ָ�������Ļ������ݡ�
     */
    public T removeEntity(Object... primaryKeyValues) {
        T result = null;
        if (primaryKeyValues != null && primaryKeyValues.length > 0) {
            result = this.locate(primaryKeyValues);
            if (result != null)
                this.remove(result);
        }
        return result;
    }

    private void removeEntityByPrimaryKeyValues(Object entity) {
        if (entity != null && (entity.getClass().isAssignableFrom(this.getClassType()) || this.getClassType().isAssignableFrom(entity.getClass()))) {
            Object[] primaryKeyValues = EntityHelper.getEntityMetaData(entity).getPrimaryKeyValues(entity);
            if (primaryKeyValues != null && primaryKeyValues.length > 0)
                this.removeEntity(primaryKeyValues);
        }
    }

    /**
     * ����������������
     */
    public int size() {
        return this.indexManager.getDataCollection().size();
    }
}
