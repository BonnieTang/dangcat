package org.dangcat.persistence;

/**
 * ��ֵ��ȡ����
 * @author dangcat
 * 
 */
public interface ValueReader
{
    /**
     * ��ȡָ���ֶε���ֵ��
     * @param <T> ��ֵ���͡�
     * @param name �ֶ����ơ�
     * @return ��ȡ����ֵ��
     */
    <T> T getValue(String name);
}
