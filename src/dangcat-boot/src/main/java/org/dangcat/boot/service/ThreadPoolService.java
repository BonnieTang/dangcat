package org.dangcat.boot.service;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * �̳߳ط���
 * @author dangcat
 * 
 */
public interface ThreadPoolService
{
    /**
     * ��Ӿܾ�������������
     * @param rejectedExecutionHandler ��������
     */
    void addRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler);

    /**
     * ִ�ж��߳�����
     */
    void execute(Runnable runnable);

    /**
     * ��ǰ����ִ�е���������
     */
    int getActiveCount();

    /**
     * ��ǰ������������������
     */
    int getBlockCount();

    /**
     * �Ƿ��Ѿ�ֹͣ��
     */
    boolean isShutdown();

    /**
     * ɾ���ܾ�������������
     * @param rejectedExecutionHandler ��������
     */
    void removeRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler);
}
