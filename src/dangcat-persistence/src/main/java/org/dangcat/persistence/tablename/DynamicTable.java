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
    public String getName(Object value);
}
