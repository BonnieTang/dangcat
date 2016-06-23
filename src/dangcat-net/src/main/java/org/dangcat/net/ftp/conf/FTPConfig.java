package org.dangcat.net.ftp.conf;

import org.dangcat.boot.config.ServiceConfig;

public class FTPConfig extends ServiceConfig
{
    private static final String CONFIG_NAME = "FTPConfig";
    private static FTPConfig instance = new FTPConfig();
    public static final String Interval = "Interval";

    /**
     * ��ȡ����ʵ��
     */
    public static FTPConfig getInstance()
    {
        return instance;
    }

    /**
     * ��ȡ���ڣ��룩��
     */
    private long interval = 30;

    public FTPConfig()
    {
        super(CONFIG_NAME);

        this.addConfigValue(Interval, long.class, this.interval);

        this.print();
    }

    public long getInterval()
    {
        return Math.max(this.getLongValue(Interval), 30);
    }
}
