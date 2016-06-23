package org.dangcat.net.process.service;

import org.dangcat.framework.exception.ServiceException;

/**
 * ���ݴ���ǰ���̷���ӿڡ�
 * @author dangcat
 * 
 * @param <T>
 */
public interface ErrorDataProcess<T>
{
    /**
     * �������ݽӿڡ�
     * @param data ���ݶ���
     * @param ServiceException �����쳣��
     */
    public void onProcessError(T data, ServiceException exception);
}
