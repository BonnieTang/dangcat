package org.dangcat.persistence.cache;

import java.util.Collection;

import org.dangcat.persistence.filter.FilterExpress;

/**
 * ʵ�建�档
 * @author dangcat
 * 
 * @param <T>
 */
public interface EntityCache<T> extends MemCache<T>
{
    /**
     * ɾ��ָ�������Ļ������ݺ����ݿ����ݡ�
     */
    public void delete(FilterExpress filterExpress);

    /**
     * ɾ���������ݡ�
     */
    public void delete(T data);

    /**
     * ����ָ���������������ϲ������ݡ�
     * @param filterExpress ����������
     * @return ���ݼ��ϡ�
     */
    public Collection<T> load(FilterExpress filterExpress);

    /**
     * ��������ֵ�ҵ���¼�С�
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    public T load(Object... params);

    /**
     * ����ָ�����ֶ�ֵ�������ݡ�
     * @param fieldNames �ֶ��������ֶ��Էֺż����
     * @param values �ֶ���ֵ���������ֶζ�Ӧ��
     * @return �ҵ��ļ�¼�С�
     */
    public Collection<T> load(String[] fieldNames, Object... values);

    /**
     * ���ػ������ݡ�
     */
    public void loadData();

    /**
     * ˢ���ڴ����ݡ�
     * @param data Ŀ�����ݡ�
     * @return ˢ�º����ݡ�
     */
    public T refresh(T data);

    /**
     * ˢ��ָ���������ڴ����ݡ�
     * @param primaryKeys Ŀ���������ݡ�
     * @return ˢ�º����ݡ�
     */
    public T refreshEntity(Object... primaryKeys);

    /**
     * ��ӻ������ݡ�
     */
    public void save(T data);
}
