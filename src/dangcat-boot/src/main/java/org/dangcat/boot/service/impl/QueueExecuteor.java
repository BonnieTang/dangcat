package org.dangcat.boot.service.impl;

/**
 * ����ִ�нӿڡ�
 *
 * @param <T>
 * @author dangcat
 */
public interface QueueExecuteor<T> {
    /**
     * �����߳�ִ�нӿڡ�
     */
    void execute(T data);

}
