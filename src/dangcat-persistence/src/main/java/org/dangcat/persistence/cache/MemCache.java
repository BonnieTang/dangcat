package org.dangcat.persistence.cache;

import org.dangcat.persistence.filter.FilterExpress;

import java.util.Collection;

/**
 * �ڴ����ݻ��档
 * @author dangcat
 * 
 * @param <T>
 */
public interface MemCache<T>
{
    /**
     * ��ӻ������ݡ�
     */
    void add(T entity);

    /**
     * ��ӻ������ݡ�
     */
    void addEntities(Object... entities);

    /**
     * ������档
     */
    void clear(boolean force);

    /**
     * ����ָ���������������ϲ������ݡ�
     * @param filterExpress ����������
     * @return ���ݼ��ϡ�
     */
    Collection<T> find(FilterExpress filterExpress);

    /**
     * ����ָ�����ֶ�ֵ�������ݡ�
     * @param fieldNames �ֶ��������ֶ��Էֺż����
     * @param values �ֶ���ֵ���������ֶζ�Ӧ��
     * @return �ҵ��ļ�¼�С�
     */
    Collection<T> find(String[] fieldNames, Object... values);

    /**
     * ��ȡ���л������ݡ�
     */
    Collection<T> getDataCollection();

    /**
     * ������������������
     */
    int getIndexSize();

    /**
     * ��������ֵ�ҵ���¼�С�
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    T locate(Object... params);

    /**
     * ���ݱ仯֪ͨ�޸�������
     * @param entities ���޸ĵļ�¼����
     */
    void modifyEntities(Object... entities);

    /**
     * ɾ��ָ�������Ļ������ݡ�
     */
    Collection<T> remove(FilterExpress filterExpress);

    /**
     * ɾ���������ݡ�
     */
    boolean remove(T data);

    /**
     * ɾ���������ݡ�
     */
    int removeEntities(Object... entities);

    /**
     * ɾ��ָ�������Ļ������ݡ�
     */
    T removeEntity(Object... primaryKeys);

    /**
     * ����������������
     */
    int size();
}
