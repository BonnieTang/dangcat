package org.dangcat.net.process.service;

import org.dangcat.framework.exception.ServiceException;

/**
 * ���ݴ���ǰ���̷���ӿڡ�
 *
 * @param <T>
 * @author dangcat
 */
public interface AfterDataProcess<T> {
    /**
     * �������ݽӿڡ�
     *
     * @param data ���ݶ���
     * @throws ServiceException �����쳣��
     */
    void afterProcess(T data) throws ServiceException;
}
