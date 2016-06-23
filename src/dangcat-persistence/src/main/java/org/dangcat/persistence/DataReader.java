package org.dangcat.persistence;

import org.dangcat.persistence.filter.FilterExpress;

/**
 * ���ݶ�ȡ�ӿڡ�
 *
 * @author dangcat
 */
public interface DataReader extends DataAccess {
    /**
     * ��ȡ����������
     */
    FilterExpress getFilterExpress();

    /**
     * ���ù���������
     */
    void setFilterExpress(FilterExpress filterExpress);

    /**
     * ָ��λ�õ���λ���ݡ�
     *
     * @param index     ������
     * @param fieldName �ֶ�����
     * @return ��ֵ����
     */
    Object getValue(int index, String fieldName);

    /**
     * ˢ�����ݡ�
     */
    void refresh();
}
