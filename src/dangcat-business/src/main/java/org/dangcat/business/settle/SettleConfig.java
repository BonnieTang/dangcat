package org.dangcat.business.settle;

import org.dangcat.boot.config.ServiceConfig;

/**
 * ͳ�����á�
 * @author dangcat
 * 
 */
public class SettleConfig extends ServiceConfig
{
    private static final String CONFIG_NAME = "Settle";
    public static final String CronExpression = "CronExpression";
    private static SettleConfig instance = new SettleConfig();

    /**
     * ��ȡ����ʵ��
     */
    public static SettleConfig getInstance()
    {
        return instance;
    }

    /** ������ڣ��룩�� */
    private String cronExpression = "0 * * * * ?";

    public SettleConfig()
    {
        super(CONFIG_NAME);

        this.addConfigValue(CronExpression, String.class, this.cronExpression);

        this.print();
    }

    public String getCronExpression()
    {
        return super.getStringValue(CronExpression);
    }
}
