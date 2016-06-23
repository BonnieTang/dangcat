package org.dangcat.framework.service;

/**
 * ������ơ�
 *
 * @author dangcat
 */
public interface ServiceControl {
    /**
     * �������ơ�
     */
    String getServiceName();

    /**
     * ����״̬��
     *
     * @return
     */
    ServiceStatus getServiceStatus();

    /**
     * �����Ƿ�������
     */
    boolean isEnabled();

    /**
     * ����������
     */
    void restart();

    /**
     * ��������
     */
    void start();

    /**
     * ֹͣ����
     */
    void stop();
}
