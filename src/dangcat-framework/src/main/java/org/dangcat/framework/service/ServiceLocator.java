package org.dangcat.framework.service;

import org.dangcat.framework.service.impl.ServiceInfo;

/**
 * ����Bean���ṩ�ߡ�
 * @author dangcat
 * 
 */
public interface ServiceLocator extends ServiceProvider
{
    /**
     * ���ݰ�����ȡ������Ϣ��
     * @param <T>
     * @param jndiName ������
     * @return ������Ϣ��
     */
    public ServiceInfo getServiceInfo(String jndiName);

    /**
     * ���ݰ�����ȡ����ʵ����
     * @param <T>
     * @param jndiName ������
     * @return ����ʵ����
     */
    public <T> T lookup(String jndiName);
}
