package org.dangcat.framework.service.impl;

import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.service.ServiceBase;
import org.dangcat.framework.service.ServiceControl;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.ServiceStatus;

/**
 * ������ƻ���
 *
 * @author dangcat
 */
public abstract class ServiceControlBase extends ServiceBase implements ServiceControl {
    private String name;

    /**
     * ����״̬��
     */
    private ServiceStatus serviceStatus = ServiceStatus.Stopped;

    /**
     * ����������
     *
     * @param parent ��������
     */
    public ServiceControlBase(ServiceProvider parent) {
        super(parent);
    }

    /**
     * ����������
     *
     * @param parent ��������
     * @param name   �������ơ�
     */
    public ServiceControlBase(ServiceProvider parent, String name) {
        super(parent);
        this.name = name;
    }

    /**
     * �������ơ�
     */
    public String getServiceName() {
        if (!ValueUtils.isEmpty(this.name))
            return this.name;
        return this.getClass().getSimpleName();
    }

    public void setServiceName(String name) {
        this.name = name;
    }

    /**
     * ����״̬��
     */
    @Override
    public ServiceStatus getServiceStatus() {
        return this.serviceStatus;
    }

    /**
     * ���÷���״̬��
     */
    protected void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    /**
     * �Ƿ��������С�
     */
    protected boolean isRunning() {
        return this.getServiceStatus().equals(ServiceStatus.Started);
    }

    @Override
    public void restart() {
        this.stop();
        this.start();
    }

    @Override
    public void start() {
        this.serviceStatus = ServiceStatus.Started;
        logger.info("The service " + this.getServiceName() + " started. ");
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.Stopped;
        logger.info("The service " + this.getServiceName() + " stopped. ");
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder(super.toString());
        info.append(Environment.LINETAB_SEPARATOR);
        info.append("ServiceName = " + this.getServiceName());
        info.append(Environment.LINETAB_SEPARATOR);
        info.append("isEnabled = " + this.isEnabled());
        return info.toString();
    }
}
