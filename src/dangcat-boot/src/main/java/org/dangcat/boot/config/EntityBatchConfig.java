package org.dangcat.boot.config;

import org.dangcat.commons.utils.Environment;

/**
 * ʵ�������������á�
 * @author dangcat
 * 
 */
public class EntityBatchConfig extends ServiceConfig
{
    public static final String BatchSize = "BatchSize";
    public static final String CronExpression = "CronExpression";
    private static final String CONFIG_NAME = "EntityBatch";
    private static final String MaxInterval = "MaxInterval";
    private static EntityBatchConfig instance = new EntityBatchConfig();
    /** ���������Ĵ�С�� */
    private int batchSize = 5000;
    /** ������ڡ� */
    private String cronExpression = "0/10 * * * * ?";
    /** ���ִ�����ڡ� */
    private int maxInterval = 30;
    private EntityBatchConfig()
    {
        super(CONFIG_NAME);

        this.addConfigValue(BatchSize, int.class, this.batchSize);
        this.addConfigValue(MaxInterval, int.class, this.maxInterval);
        this.addConfigValue(CronExpression, String.class, this.cronExpression);

        this.print();
    }

    /**
     * ��ȡ����ʵ��
     */
    public static EntityBatchConfig getInstance() {
        return instance;
    }

    public int getBatchSize()
    {
        return Environment.isTestEnabled() ? 0 : this.getIntValue(BatchSize);
    }

    public String getCronExpression()
    {
        return this.getStringValue(CronExpression);
    }

    public int getMaxInterval()
    {
        return Environment.isTestEnabled() ? 0 : this.getIntValue(MaxInterval);
    }
}
