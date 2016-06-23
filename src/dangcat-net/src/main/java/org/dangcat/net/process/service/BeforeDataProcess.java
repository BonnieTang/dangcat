package org.dangcat.net.process.service;

import org.dangcat.framework.exception.ServiceException;

/**
 * ���ݴ���ǰ���̷���ӿڡ�
 *
 * @param <T>
 * @author dangcat
 */
public interface BeforeDataProcess<T> {
    /**
     * �������ݽӿڡ�
     *
     * @param data ���ݶ���
     * @throws ServiceException �����쳣��
     */
    void beforeProcess(T data) throws ServiceException;
}
