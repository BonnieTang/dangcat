package org.dangcat.framework.service;

/**
 * �����ṩ�ߡ�
 * @author dangcat
 * 
 */
public interface ServiceProvider
{
    /**
     * �������ͻ�ȡ�������
     * @param classType �������͡�
     * @return �������
     */
    <T> T getService(Class<T> classType);
}
