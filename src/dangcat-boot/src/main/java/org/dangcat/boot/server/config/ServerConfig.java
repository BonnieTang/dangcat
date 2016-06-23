package org.dangcat.boot.server.config;

import org.dangcat.boot.config.ServiceConfig;
import org.dangcat.commons.utils.Environment;

/**
 * ϵͳ������á�
 * @author dangcat
 * 
 */
public class ServerConfig extends ServiceConfig
{
    public static final String CronExpression = "CronExpression";
    public static final String DiskPath = "DiskPath";
    public static final String LogMaxKeepLength = "LogMaxKeepLength";
    public static final String MessageName = "MessageName";
    public static final String Type = "Type";
    private static final String CONFIG_NAME = "ServerConfig";
    private static ServerConfig instance = new ServerConfig();
    /** ������ڣ��룩�� */
    private String cronExpression = "0 0/5 * * * ?";
    /** ���·���� */
    private Integer diskPath = null;
    /** ״̬��־�����ʱ�䣨�죩�� */
    private Integer logMaxKeepLength = 7;
    /** ͬ����Ϣ���⡣ */
    private String messageName = null;
    /** ���������͡� */
    private Integer type = null;
    public ServerConfig()
    {
        super(CONFIG_NAME);

        this.addConfigValue(CronExpression, String.class, this.cronExpression);
        this.addConfigValue(Type, Integer.class, this.type);
        this.addConfigValue(LogMaxKeepLength, Integer.class, this.logMaxKeepLength);
        this.addConfigValue(DiskPath, String.class, this.diskPath);
        this.addConfigValue(MessageName, String.class, this.messageName);

        this.print();
    }

    /**
     * ��ȡ����ʵ��
     */
    public static ServerConfig getInstance() {
        return instance;
    }

    public String getCronExpression()
    {
        return super.getStringValue(CronExpression);
    }

    public String getDiskPath()
    {
        return super.getStringValue(DiskPath);
    }

    public Integer getLogMaxKeepLength()
    {
        return super.getIntValue(LogMaxKeepLength);
    }

    public String getMessageName()
    {
        return super.getStringValue(MessageName);
    }

    public Integer getType()
    {
        return super.getIntValue(Type);
    }

    @Override
    public boolean isEnabled()
    {
        return !Environment.isTestEnabled() && super.isEnabled();
    }
}
