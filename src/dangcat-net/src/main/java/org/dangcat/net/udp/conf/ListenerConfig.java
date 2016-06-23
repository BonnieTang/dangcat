package org.dangcat.net.udp.conf;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dangcat.boot.config.ServiceConfig;
import org.dangcat.boot.server.impl.ServerManager;
import org.dangcat.commons.utils.ValueUtils;

/**
 * �����˿����á�
 * @author dangcat
 * 
 */
public abstract class ListenerConfig extends ServiceConfig
{
    public static final String BindAddress = "BindAddress";
    public static final String BufferSize = "BufferSize";
    public static final String IdentifierEnabled = "IdentifierEnabled";
    public static final String MaxConcurrentSize = "MaxConcurrentSize";
    public static final String MaxQueueCapacity = "MaxQueueCapacity";
    public static final String Port = "Port";
    public static final String ReplyPort = "ReplyPort";
    public static final String TimeCostThreshold = "TimeCostThreshold";
    public static final String Timeout = "Timeout";
    public static final String TryTimes = "TryTimes";

    /** ����ش�С���ֽڣ��� */
    private int bufferSize = 90000;
    /** ���ñ�ʶ���١� */
    private boolean identifierEnabled = true;
    /** ��󲢷������� */
    private int maxConcurrentSize = 10;
    /** ������������ */
    private int maxQueueCapacity = 10000;
    /** �����¼����ĵ���ֵ�� */
    private long timeCostThreshold = 0;
    /** ������ʱ�����룩�� */
    private int timeout = 1000;
    /** ���Եȴ���Ӧ���Ĵ����� */
    private int tryTimes = 3;

    public ListenerConfig(String configName)
    {
        super(configName);

        this.addConfigValue(TimeCostThreshold, long.class, this.timeCostThreshold);
        this.addConfigValue(IdentifierEnabled, boolean.class, this.identifierEnabled);
        this.addConfigValue(BufferSize, int.class, this.bufferSize);
        this.addConfigValue(MaxConcurrentSize, int.class, this.maxConcurrentSize);
        this.addConfigValue(MaxQueueCapacity, int.class, this.maxQueueCapacity);
        this.addConfigValue(Timeout, int.class, this.timeout);
        this.addConfigValue(TryTimes, int.class, this.tryTimes);
        this.addConfigValue(Port, Integer.class, this.getDefaultPort());
        this.addConfigValue(ReplyPort, Integer.class, null);
        this.addConfigValue(BindAddress, String.class, null);
    }

    public InetAddress getBindAddress()
    {
        InetAddress inetAddress = null;
        try
        {
            String bindAddress = this.getStringValue(BindAddress);
            if (!ValueUtils.isEmpty(bindAddress))
                inetAddress = InetAddress.getByName(bindAddress);
            else
            {
                InetAddress serverAddress = ServerManager.getInstance().getServerAddress();
                if (!serverAddress.isLoopbackAddress())
                    inetAddress = serverAddress;
            }
        }
        catch (UnknownHostException e)
        {
        }
        return inetAddress;
    }

    public int getBufferSize()
    {
        return super.getIntValue(BufferSize);
    }

    protected abstract Integer getDefaultPort();

    public boolean getIdentifierEnabled()
    {
        return this.getBooleanValue(IdentifierEnabled);
    }

    public int getMaxConcurrentSize()
    {
        return this.getIntValue(MaxConcurrentSize);
    }

    public int getMaxQueueCapacity()
    {
        return this.getIntValue(MaxQueueCapacity);
    }

    public Integer getPort()
    {
        return this.getIntValue(Port);
    }

    public Integer getReplyPort()
    {
        return this.getIntValue(ReplyPort);
    }

    public long getTimeCostThreshold()
    {
        return this.getLongValue(TimeCostThreshold);
    }

    public int getTimeout()
    {
        return this.getIntValue(Timeout);
    }

    public int getTryTimes()
    {
        return this.getIntValue(TryTimes);
    }
}
