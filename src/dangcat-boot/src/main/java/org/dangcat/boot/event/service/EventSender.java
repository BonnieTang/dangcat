package org.dangcat.boot.event.service;

import org.dangcat.framework.event.Event;

/**
 * ��Ϣ���ͽӿڡ�
 *
 * @author dangcat
 */
public interface EventSender {
    /**
     * ��ϢԴ���ơ�
     */
    String getName();

    /**
     * ������Ϣ��
     *
     * @param event ��Ϣ����
     */
    void send(Event event);

    /**
     * �������͡�
     */
    void start();

    /**
     * ֹͣ���͡�
     */
    void stop();
}
