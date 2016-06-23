package org.dangcat.boot.service;

import org.apache.log4j.Logger;

/**
 * ����߳�ִ�нӿڡ�
 */
public interface WatchRunnable extends Runnable {
    /**
     * �����Ӧʱ�䣬��λ���롣
     */
    long getLastResponseTime();

    /**
     * ִ����־��
     */
    Logger getLogger();

    /**
     * ��ʱʱ������λ���롣
     */
    long getTimeOutLength();

    /**
     * ��ʱ��ֹ�ӿڡ�
     */
    void terminate();
}
