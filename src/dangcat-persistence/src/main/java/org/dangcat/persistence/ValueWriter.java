package org.dangcat.persistence;

/**
 * ��ֵд������
 * @author dangcat
 * 
 */
public interface ValueWriter
{
    /**
     * д��ָ���ֶε���ֵ��
     * @param <T> ��ֵ���͡�
     * @param name �ֶ����ơ�
     * @param value �ֶ���ֵ��
     */
    public void setValue(String name, Object value);
}
