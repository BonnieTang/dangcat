package org.dangcat.boot.service.impl;

/**
 * ����ִ�нӿڡ�
 * @author dangcat
 * 
 * @param <T>
 */
public interface QueueExecuteor<T>
{
    /**
     * �����߳�ִ�нӿڡ�
     */
    public void execute(T data);

}
