package org.dangcat.boot.service;

import org.dangcat.framework.exception.ServiceException;

/**
 * ���ݴ������̷���ӿڡ�
 *
 * @param <T>
 * @author dangcat
 */
public interface DataProcessService<T> {
    /**
     * �������ݽӿڡ�
     *
     * @param data ���ݶ���
     * @throws ServiceException �����쳣��
     */
    void process(T data) throws ServiceException;
}
