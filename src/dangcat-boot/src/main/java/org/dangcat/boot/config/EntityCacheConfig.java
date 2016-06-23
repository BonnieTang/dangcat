package org.dangcat.boot.config;

import org.dangcat.boot.ApplicationContext;

/**
 * �������á�
 *
 * @author dangcat
 */
public class EntityCacheConfig extends ServiceConfig {
    public static final String ConfigFile = "ConfigFile";
    public static final String CronExpression = "CronExpression";
    public static final String MessageName = "MessageName";
    private static final String CONFIG_NAME = "EntityCache";
    private static EntityCacheConfig instance = new EntityCacheConfig();
    /**
     * ���������ļ���
     */
    private String configFile = ApplicationContext.getInstance().getName() + ".cache.xml";
    /**
     * ������ڡ�
     */
    private String cronExpression = "0 0 0/2 * * ?";
    /**
     * ͬ����Ϣ���⡣
     */
    private String messageName = null;

    private EntityCacheConfig() {
        super(CONFIG_NAME);

        this.addConfigValue(ConfigFile, String.class, this.configFile);
        this.addConfigValue(CronExpression, String.class, this.cronExpression);
        this.addConfigValue(MessageName, String.class, this.messageName);

        this.print();
    }

    /**
     * ��ȡ����ʵ��
     */
    public static EntityCacheConfig getInstance() {
        return instance;
    }

    public String getConfigFile() {
        return super.getStringValue(ConfigFile);
    }

    public String getCronExpression() {
        return super.getStringValue(CronExpression);
    }

    public String getMessageName() {
        return super.getStringValue(MessageName);
    }
}
