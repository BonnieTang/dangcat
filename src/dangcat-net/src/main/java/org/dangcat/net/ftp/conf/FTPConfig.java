package org.dangcat.net.ftp.conf;

import org.dangcat.boot.config.ServiceConfig;

public class FTPConfig extends ServiceConfig
{
    public static final String Interval = "Interval";
    private static final String CONFIG_NAME = "FTPConfig";
    private static FTPConfig instance = new FTPConfig();
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

    /**
     * ��ȡ����ʵ��
     */
    public static FTPConfig getInstance() {
        return instance;
    }

    public long getInterval()
    {
        return Math.max(this.getLongValue(Interval), 30);
    }
}
