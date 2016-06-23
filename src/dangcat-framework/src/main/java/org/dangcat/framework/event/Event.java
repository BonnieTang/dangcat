package org.dangcat.framework.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dangcat.commons.utils.Environment;

/**
 * ��Ϣ����
 * @author dangcat
 * 
 */
public class Event implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    /** �Ƿ�ȡ���¼�·�ɡ� */
    private boolean cancel = false;
    /** �¼��Ѿ����� */
    private boolean handled = false;
    /** ��Ϣ�롣 */
    private String id;
    /** ������ */
    private Map<String, Object> params = new HashMap<String, Object>();
    /** �¼�ʱ�� */
    private Date timeStamp;

    public Event()
    {

    }

    public Event(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public Map<String, Object> getParams()
    {
        return params;
    }

    public Date getTimeStamp()
    {
        return timeStamp;
    }

    public boolean isCancel()
    {
        return cancel;
    }

    public boolean isHandled()
    {
        return handled;
    }

    public void setCancel(boolean cancel)
    {
        this.cancel = cancel;
    }

    public void setHandled(boolean handled)
    {
        this.handled = handled;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("ID: " + this.getId());
        info.append(", ");
        info.append("Cancel: " + this.isCancel());
        info.append(", ");
        info.append("Handled: " + this.isHandled());
        for (String paramName : this.getParams().keySet())
        {
            info.append(Environment.LINE_SEPARATOR);
            info.append("Param " + paramName + " = " + this.getParams().get(paramName));
        }
        return info.toString();
    }
}
