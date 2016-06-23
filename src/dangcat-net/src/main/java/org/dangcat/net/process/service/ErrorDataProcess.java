package org.dangcat.net.process.service;

import org.dangcat.framework.exception.ServiceException;

/**
 * ���ݴ���ǰ���̷���ӿڡ�
 *
 * @param <T>
 * @author dangcat
 */
public interface ErrorDataProcess<T> {
    /**
     * �������ݽӿڡ�
     *
     * @param data             ���ݶ���
     * @param ServiceException �����쳣��
     */
    void onProcessError(T data, ServiceException exception);
}
