package org.dangcat.boot.event.service;

import org.dangcat.framework.event.Event;

/**
 * ��Ϣ���ͽӿڡ�
 * @author dangcat
 * 
 */
public interface EventSender
{
    /**
     * ��ϢԴ���ơ�
     */
    public String getName();

    /**
     * ������Ϣ��
     * @param event ��Ϣ����
     */
    public void send(Event event);

    /**
     * �������͡�
     */
    public void start();

    /**
     * ֹͣ���͡�
     */
    public void stop();
}
