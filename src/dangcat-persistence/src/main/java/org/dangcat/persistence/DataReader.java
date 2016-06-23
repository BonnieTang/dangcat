package org.dangcat.persistence;

import org.dangcat.persistence.filter.FilterExpress;

/**
 * ���ݶ�ȡ�ӿڡ�
 * @author dangcat
 * 
 */
public interface DataReader extends DataAccess
{
    /**
     * ��ȡ����������
     */
    public FilterExpress getFilterExpress();

    /**
     * ָ��λ�õ���λ���ݡ�
     * @param index ������
     * @param fieldName �ֶ�����
     * @return ��ֵ����
     */
    public Object getValue(int index, String fieldName);

    /**
     * ˢ�����ݡ�
     */
    public void refresh();

    /**
     * ���ù���������
     */
    public void setFilterExpress(FilterExpress filterExpress);
}
