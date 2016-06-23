package org.dangcat.persistence.index;

import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.filter.*;

import java.util.*;

/**
 * ������������
 *
 * @param <T>
 * @author dangcat
 */
public class IndexManager<T> {
    private Collection<T> dataCollection = null;
    private Map<String, Indexer> indexerMap = Collections.synchronizedMap(new HashMap<String, Indexer>());
    private boolean isEmbedCache = false;
    private Indexer primaryIndexer = null;

    /**
     * ����������������
     *
     * @param dataList ���ݼ����б�
     */
    public IndexManager() {
        this.dataCollection = Collections.synchronizedCollection(new HashSet<T>());
        this.isEmbedCache = true;
    }

    /**
     * ����������������
     *
     * @param dataList ���ݼ����б�
     */
    public IndexManager(Collection<T> dataCollectoin) {
        this.dataCollection = Collections.synchronizedCollection(dataCollectoin);
    }

    /**
     * ������ݡ�
     *
     * @param data
     */
    public void add(T data) {
        if (data != null) {
            Map<String, Indexer> indexerMap = this.indexerMap;
            for (Indexer indexer : indexerMap.values())
                indexer.add(data);
            if (this.isEmbedCache)
                this.dataCollection.add(data);
        }
    }

    private void appendFilterExpress(Map<String, FilterComparable> indexFilterMap, FilterUnit filterUnit) {
        FilterComparable filterComparable = indexFilterMap.get(filterUnit.getFieldName());
        if (filterComparable == null) {
            filterComparable = new FilterComparable();
            indexFilterMap.put(filterUnit.getFieldName(), filterComparable);
        }
        filterComparable.add(filterUnit);
    }

    /**
     * ���������
     *
     * @param indexName ��������
     */
    public void appendIndex(String indexName) {
        this.appendIndex(indexName, false);
    }

    /**
     * ���������
     *
     * @param indexName    ��������
     * @param isPrimaryKey �Ƿ�������������
     */
    public void appendIndex(String indexName, boolean isPrimaryKey) {
        Map<String, Indexer> indexerMap = this.indexerMap;
        if (!ValueUtils.isEmpty(indexName) && !indexerMap.containsKey(indexName)) {
            Indexer indexer = new Indexer(indexName);
            indexerMap.put(indexName, indexer);
            if (isPrimaryKey)
                this.primaryIndexer = indexer;
            this.rebuild(indexName);
        }
    }

    /**
     * ���������
     */
    public void clear() {
        this.indexerMap = Collections.synchronizedMap(new HashMap<String, Indexer>());
        if (this.isEmbedCache)
            this.dataCollection = Collections.synchronizedCollection(new HashSet<T>());
    }

    private Map<String, FilterComparable> create(FilterExpress filterExpress) {
        Map<String, FilterComparable> indexFilterMap = new TreeMap<String, FilterComparable>(String.CASE_INSENSITIVE_ORDER);
        this.findIndexFilterMap(indexFilterMap, filterExpress);
        return indexFilterMap;
    }

    /**
     * ��ָ�����ֶ�������ֵ��������������
     *
     * @param fieldNames �ֶ�����
     * @param values     ��ֵ��
     * @return ����������
     */
    private FilterExpress createFilterExpress(String[] fieldNames, Object... values) {
        FilterExpress filterExpress = null;
        if (fieldNames.length == 1)
            filterExpress = new FilterUnit(fieldNames[0], FilterType.eq, values[0]);
        else {
            FilterGroup filterGroup = new FilterGroup();
            for (int i = 0; i < fieldNames.length; i++)
                filterGroup.add(new FilterUnit(fieldNames[i], FilterType.eq, values[i]));
            filterExpress = filterGroup;
        }
        return filterExpress;
    }

    /**
     * ����ָ���������������ϲ������ݡ�
     *
     * @param filterExpress ����������
     * @return ���ݼ��ϡ�
     */
    public Collection<T> find(FilterExpress filterExpress) {
        Collection<T> dataCollection = this.getDataCollection();
        if (dataCollection == null || dataCollection.size() == 0)
            return null;

        Map<String, FilterComparable> indexFilterMap = this.create(filterExpress);
        Indexer indexer = this.findIndexer(indexFilterMap);
        Collection<T> sourceCollection = new HashSet<T>();
        if (indexer != null)
            indexer.search(sourceCollection, indexFilterMap);
        else
            sourceCollection.addAll(dataCollection);
        Collection<T> findCollection = null;
        for (T data : sourceCollection) {
            if (filterExpress.isValid(data)) {
                if (findCollection == null)
                    findCollection = new HashSet<T>();
                findCollection.add(data);
            }
        }
        return findCollection;
    }

    /**
     * ��������ֵ�ҵ���¼�С�
     *
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    public T find(Object... params) {
        T data = null;
        if (this.primaryIndexer != null) {
            Collection<T> dataCollection = this.find(this.primaryIndexer.getIndexFieldNames(), params);
            if (dataCollection != null && dataCollection.size() > 0)
                data = dataCollection.iterator().next();
        }
        return data;
    }

    /**
     * ����ָ�����ֶ�ֵ�������ݡ�
     *
     * @param fieldNames �ֶ��������ֶ��Էֺż����
     * @param values     �ֶ���ֵ���������ֶζ�Ӧ��
     * @return �ҵ��ļ�¼�С�
     */
    public Collection<T> find(String[] fieldNames, Object... values) {
        Collection<T> findCollection = null;
        if (fieldNames.length > 0 && fieldNames.length == values.length) {
            FilterExpress filterExpress = this.createFilterExpress(fieldNames, values);
            if (filterExpress != null)
                findCollection = this.find(filterExpress);
        }
        return findCollection;
    }

    /**
     * ���Թ���ѡ�������á�
     */
    protected Indexer findIndexer(FilterExpress filterExpress) {
        Map<String, FilterComparable> indexFilterMap = this.create(filterExpress);
        return this.findIndexer(indexFilterMap);
    }

    private Indexer findIndexer(Map<String, FilterComparable> indexFilterMap) {
        Indexer findIndexer = null;
        int findhitRate = 0;
        Map<String, Indexer> indexerMap = this.indexerMap;
        for (Indexer indexer : indexerMap.values()) {
            int hitRate = indexer.checkHitRate(indexFilterMap.keySet());
            if (hitRate > findhitRate) {
                findIndexer = indexer;
                findhitRate = hitRate;
            }
        }
        return findIndexer;
    }

    private void findIndexFilterMap(Map<String, FilterComparable> indexFilterMap, FilterExpress filterExpress) {
        if (filterExpress instanceof FilterUnit)
            this.appendFilterExpress(indexFilterMap, (FilterUnit) filterExpress);
        else {
            FilterGroup filterGroup = (FilterGroup) filterExpress;
            if (FilterGroupType.and.equals(filterGroup.getGroupType()) || filterGroup.getFilterExpressList().size() == 1) {
                for (FilterExpress childFilterExpress : filterGroup.getFilterExpressList())
                    this.findIndexFilterMap(indexFilterMap, childFilterExpress);
            }
        }
    }

    public Collection<T> getDataCollection() {
        return this.dataCollection;
    }

    public Set<String> getIndexNameSet() {
        return this.indexerMap.keySet();
    }

    public String getPrimaryKeyIndex() {
        return this.primaryIndexer != null ? this.primaryIndexer.getName() : null;
    }

    /**
     * �ؽ�������
     */
    public void rebuild() {
        Map<String, Indexer> indexerMap = this.indexerMap;
        for (String indexName : indexerMap.keySet())
            this.rebuild(indexName);
    }

    /**
     * �ؽ�ָ����������
     */
    public void rebuild(String indexName) {
        Map<String, Indexer> indexerMap = this.indexerMap;
        Indexer indexer = indexerMap.get(indexName);
        if (indexer != null) {
            indexer.reset();
            Collection<T> dataCollection = this.getDataCollection();
            if (dataCollection != null) {
                synchronized (dataCollection) {
                    for (T data : dataCollection)
                        indexer.add(data);
                }
            }
        }
    }

    /**
     * ɾ��ָ���ļ�¼����
     *
     * @param data ��¼����
     */
    public boolean remove(T data) {
        boolean result = false;
        if (data != null) {
            Map<String, Indexer> indexerMap = this.indexerMap;
            for (Indexer indexer : indexerMap.values())
                result = indexer.remove(data);
            if (this.isEmbedCache)
                result = this.dataCollection.remove(data);
        }
        return result;
    }

    /**
     * ���ݱ仯֪ͨ�޸�������
     *
     * @param fieldName �ֶ�����
     * @param data      ���޸ĵļ�¼����
     */
    public void update(String fieldName, T data) {
        if (data != null) {
            Map<String, Indexer> indexerMap = this.indexerMap;
            for (Indexer indexer : indexerMap.values())
                indexer.update(data, fieldName);
            if (this.isEmbedCache)
                this.dataCollection.add(data);
        }
    }
}
