package org.dangcat.net.process.service;

import org.dangcat.framework.exception.ServiceException;

/**
 * ���ݴ���ǰ���̷���ӿڡ�
 * @author dangcat
 * 
 * @param <T>
 */
public interface BeforeDataProcess<T>
{
    /**
     * �������ݽӿڡ�
     * @param data ���ݶ���
     * @throws ServiceException �����쳣��
     */
    public void beforeProcess(T data) throws ServiceException;
}
