package org.dangcat.boot.service;

import org.dangcat.framework.exception.ServiceException;

/**
 * ���ݴ������̷���ӿڡ�
 * @author dangcat
 * 
 * @param <T>
 */
public interface DataProcessService<T>
{
    /**
     * �������ݽӿڡ�
     * @param data ���ݶ���
     * @throws ServiceException �����쳣��
     */
    public void process(T data) throws ServiceException;
}
