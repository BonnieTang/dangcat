package org.dangcat.boot.event.service;

import org.dangcat.framework.event.Event;

/**
 * ��Ϣ��������
 * @author dangcat
 * 
 */
public interface EventListenService extends EventReceiveListener
{
    /**
     * �����Ϣ��������
     * @param eventListener ��Ϣ��������
     */
    public void addEventListener(EventListener eventListener);

    /**
     * �����¼���Ϣ��
     * @param event ��Ϣ����
     */
    public void handleEvent(Event event);

    /**
     * ɾ����Ϣ��������
     * @param eventListener ��Ϣ��������
     */
    public void removeEventListener(EventListener eventListener);
}
