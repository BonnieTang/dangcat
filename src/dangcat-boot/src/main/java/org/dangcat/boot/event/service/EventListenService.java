package org.dangcat.boot.event.service;

import org.dangcat.framework.event.Event;

/**
 * ��Ϣ��������
 *
 * @author dangcat
 */
public interface EventListenService extends EventReceiveListener {
    /**
     * �����Ϣ��������
     *
     * @param eventListener ��Ϣ��������
     */
    void addEventListener(EventListener eventListener);

    /**
     * �����¼���Ϣ��
     *
     * @param event ��Ϣ����
     */
    void handleEvent(Event event);

    /**
     * ɾ����Ϣ��������
     *
     * @param eventListener ��Ϣ��������
     */
    void removeEventListener(EventListener eventListener);
}
