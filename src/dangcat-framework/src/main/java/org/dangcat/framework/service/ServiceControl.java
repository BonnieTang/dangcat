package org.dangcat.framework.service;

/**
 * ������ơ�
 * @author dangcat
 * 
 */
public interface ServiceControl
{
    /**
     * �������ơ�
     */
    public String getServiceName();

    /**
     * ����״̬��
     * @return
     */
    public ServiceStatus getServiceStatus();

    /**
     * �����Ƿ�������
     */
    public boolean isEnabled();

    /**
     * ����������
     */
    public void restart();

    /**
     * ��������
     */
    public void start();

    /**
     * ֹͣ����
     */
    public void stop();
}
