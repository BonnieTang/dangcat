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
    public void addRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler);

    /**
     * ִ�ж��߳�����
     */
    public void execute(Runnable runnable);

    /**
     * ��ǰ����ִ�е���������
     */
    public int getActiveCount();

    /**
     * ��ǰ������������������
     */
    public int getBlockCount();

    /**
     * �Ƿ��Ѿ�ֹͣ��
     */
    public boolean isShutdown();

    /**
     * ɾ���ܾ�������������
     * @param rejectedExecutionHandler ��������
     */
    public void removeRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler);
}
