package org.dangcat.boot.event;

import org.dangcat.framework.event.Event;

/**
 * ���Ըı������ӿڡ�
 * @author dangcat
 * 
 */
public abstract class ChangeEventAdaptor
{
    /**
     * �仯���¼���
     * @param event �¼�����
     */
    public void afterChanged(Object sender, Event event)
    {

    }

    /**
     * �仯ǰ�¼���
     * @param event �¼�����
     */
    public void beforeChange(Object sender, Event event)
    {

    }
}
