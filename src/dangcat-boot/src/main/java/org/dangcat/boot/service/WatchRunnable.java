package org.dangcat.boot.service;

import org.apache.log4j.Logger;

/**
 * ����߳�ִ�нӿڡ�
 * 
 */
public interface WatchRunnable extends Runnable
{
    /**
     * �����Ӧʱ�䣬��λ���롣
     */
    public long getLastResponseTime();

    /**
     * ִ����־��
     */
    public Logger getLogger();

    /**
     * ��ʱʱ������λ���롣
     */
    public long getTimeOutLength();

    /**
     * ��ʱ��ֹ�ӿڡ�
     */
    public void terminate();
}
