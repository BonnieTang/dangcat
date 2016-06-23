package org.dangcat.persistence;

import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Columns;

/**
 * ���ݷ��ʽӿڡ�
 * @author dangcat
 * 
 */
public interface DataAccess
{
    /**
     * ���ָ�����������λ��
     * @param fieldName �ֶ�����
     * @return ��λ����
     */
    Column addColumn(String fieldName);

    /**
     * Ҫ��ȡ����λ���ϡ�
     * @return
     */
    Columns getColumns();

    /**
     * ��ȡ�ֶα��⡣
     * @param fieldName �ֶ����ơ�
     * @return �ֶα��⡣
     */
    String getTitle(String fieldName);

    /**
     * ����������
     * @return
     */
    int size();
}
