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
    public Column addColumn(String fieldName);

    /**
     * Ҫ��ȡ����λ���ϡ�
     * @return
     */
    public Columns getColumns();

    /**
     * ��ȡ�ֶα��⡣
     * @param fieldName �ֶ����ơ�
     * @return �ֶα��⡣
     */
    public String getTitle(String fieldName);

    /**
     * ����������
     * @return
     */
    public int size();
}
