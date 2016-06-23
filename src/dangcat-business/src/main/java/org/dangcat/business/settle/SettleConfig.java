package org.dangcat.business.settle;

import org.dangcat.boot.config.ServiceConfig;

/**
 * ͳ�����á�
 * @author dangcat
 * 
 */
public class SettleConfig extends ServiceConfig
{
    public static final String CronExpression = "CronExpression";
    private static final String CONFIG_NAME = "Settle";
    private static SettleConfig instance = new SettleConfig();
    /** ������ڣ��룩�� */
    private String cronExpression = "0 * * * * ?";

    public SettleConfig()
    {
        super(CONFIG_NAME);

        this.addConfigValue(CronExpression, String.class, this.cronExpression);

        this.print();
    }

    /**
     * ��ȡ����ʵ��
     */
    public static SettleConfig getInstance() {
        return instance;
    }

    public String getCronExpression()
    {
        return super.getStringValue(CronExpression);
    }
}
