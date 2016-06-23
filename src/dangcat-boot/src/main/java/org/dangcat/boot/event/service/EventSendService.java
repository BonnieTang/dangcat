package org.dangcat.boot.event.service;

import org.dangcat.framework.event.Event;

/**
 * ��Ϣ���ͷ���
 *
 * @author dangcat
 */
public interface EventSendService {
    /**
     * ������Ϣ����
     *
     * @param event ��Ϣ����
     */
    void send(Event event);

    /**
     * ������Ϣ����
     *
     * @param name  ��ϢԴ���ơ�
     * @param event ��Ϣ����
     */
    void send(String name, Event event);
}
