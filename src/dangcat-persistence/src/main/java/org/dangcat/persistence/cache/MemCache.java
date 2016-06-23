package org.dangcat.persistence.cache;

import java.util.Collection;

import org.dangcat.persistence.filter.FilterExpress;

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
    public void add(T entity);

    /**
     * ��ӻ������ݡ�
     */
    public void addEntities(Object... entities);

    /**
     * ������档
     */
    public void clear(boolean force);

    /**
     * ����ָ���������������ϲ������ݡ�
     * @param filterExpress ����������
     * @return ���ݼ��ϡ�
     */
    public Collection<T> find(FilterExpress filterExpress);

    /**
     * ����ָ�����ֶ�ֵ�������ݡ�
     * @param fieldNames �ֶ��������ֶ��Էֺż����
     * @param values �ֶ���ֵ���������ֶζ�Ӧ��
     * @return �ҵ��ļ�¼�С�
     */
    public Collection<T> find(String[] fieldNames, Object... values);

    /**
     * ��ȡ���л������ݡ�
     */
    public Collection<T> getDataCollection();

    /**
     * ������������������
     */
    public int getIndexSize();

    /**
     * ��������ֵ�ҵ���¼�С�
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    public T locate(Object... params);

    /**
     * ���ݱ仯֪ͨ�޸�������
     * @param entities ���޸ĵļ�¼����
     */
    public void modifyEntities(Object... entities);

    /**
     * ɾ��ָ�������Ļ������ݡ�
     */
    public Collection<T> remove(FilterExpress filterExpress);

    /**
     * ɾ���������ݡ�
     */
    public boolean remove(T data);

    /**
     * ɾ���������ݡ�
     */
    public int removeEntities(Object... entities);

    /**
     * ɾ��ָ�������Ļ������ݡ�
     */
    public T removeEntity(Object... primaryKeys);

    /**
     * ����������������
     */
    public int size();
}
