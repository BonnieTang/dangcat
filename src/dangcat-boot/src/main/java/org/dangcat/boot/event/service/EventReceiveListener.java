package org.dangcat.boot.event.service;

import org.dangcat.framework.event.Event;

/**
 * ��Ϣ��������
 * @author dangcat
 * 
 */
public interface EventReceiveListener
{
    /**
     * �������ݡ�
     * @param event �����¼���
     */
    public void onReceive(Event event);
}
