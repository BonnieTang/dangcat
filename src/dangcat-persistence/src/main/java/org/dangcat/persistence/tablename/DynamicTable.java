package org.dangcat.persistence.tablename;

/**
 * ��̬��
 * @author dangcat
 * 
 */
public interface DynamicTable extends Cloneable
{
    /**
     * �������ݾ������λ�á�
     * @param value ���ݡ�
     * @return ������
     */
    String getName(Object value);
}
