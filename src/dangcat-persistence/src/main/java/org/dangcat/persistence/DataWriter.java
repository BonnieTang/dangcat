package org.dangcat.persistence;

/**
 * ����д��ӿڡ�
 * @author dangcat
 * 
 */
public interface DataWriter extends DataAccess
{
    /**
     * д��ָ��λ�õ���λ���ݡ�
     * @param index ������
     * @param fieldName �ֶ�����
     * @return ��ֵ����
     */
    void setValue(int index, String fieldName, Object value);
}
