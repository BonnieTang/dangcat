package org.dangcat.net.jms.conf;

import org.dangcat.boot.config.ServiceConfig;

/**
 * ͳ�����á�
 * 
 */
public class JMSConfig extends ServiceConfig
{
    private static final String CONFIG_NAME = "JMS";
    public static final String ConfigFile = "ConfigFile";
    private static JMSConfig instance = new JMSConfig();
    public static final String ListenProcessSize = "ListenProcessSize";

    /**
     * ��ȡ����ʵ��
     */
    public static JMSConfig getInstance()
    {
        return instance;
    }

    /** ActiveMQ�������ļ��� */
    private String configFile = "conf/jmsservice.activemq.xml";
    /** �������������߳����� */
    private int listenProcessSize = 20;

    public JMSConfig()
    {
        super(CONFIG_NAME);

        this.addConfigValue(ConfigFile, int.class, this.configFile);
        this.addConfigValue(ListenProcessSize, int.class, this.listenProcessSize);

        this.print();
    }

    public String getConfigFile()
    {
        return this.getStringValue(ConfigFile);
    }

    public int getListenProcessSize()
    {
        return Math.max(this.getIntValue(ListenProcessSize), 5);
    }
}
