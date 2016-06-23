package org.dangcat.persistence.cache;

import org.dangcat.persistence.filter.FilterExpress;

import java.util.Collection;

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
    void delete(FilterExpress filterExpress);

    /**
     * ɾ���������ݡ�
     */
    void delete(T data);

    /**
     * ����ָ���������������ϲ������ݡ�
     * @param filterExpress ����������
     * @return ���ݼ��ϡ�
     */
    Collection<T> load(FilterExpress filterExpress);

    /**
     * ��������ֵ�ҵ���¼�С�
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    T load(Object... params);

    /**
     * ����ָ�����ֶ�ֵ�������ݡ�
     * @param fieldNames �ֶ��������ֶ��Էֺż����
     * @param values �ֶ���ֵ���������ֶζ�Ӧ��
     * @return �ҵ��ļ�¼�С�
     */
    Collection<T> load(String[] fieldNames, Object... values);

    /**
     * ���ػ������ݡ�
     */
    void loadData();

    /**
     * ˢ���ڴ����ݡ�
     * @param data Ŀ�����ݡ�
     * @return ˢ�º����ݡ�
     */
    T refresh(T data);

    /**
     * ˢ��ָ���������ڴ����ݡ�
     * @param primaryKeys Ŀ���������ݡ�
     * @return ˢ�º����ݡ�
     */
    T refreshEntity(Object... primaryKeys);

    /**
     * ��ӻ������ݡ�
     */
    void save(T data);
}
