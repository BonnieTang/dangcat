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
    void setValue(String name, Object value);
}
