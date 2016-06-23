package org.dangcat.boot.config;

/**
 * ͳ�����á�
 * @author dangcat
 * 
 */
public class StatisticsConfig extends ServiceConfig
{
    private static final String CONFIG_NAME = "Statistics";
    private static StatisticsConfig instance = new StatisticsConfig();
    public static final String LogInterval = "LogInterval";
    public static final String StatisticsInterval = "StatisticsInterval";

    /**
     * ��ȡ����ʵ��
     */
    public static StatisticsConfig getInstance()
    {
        return instance;
    }

    /** ���ͳ����Ϣ���ڣ��룩�� */
    private long logInterval = 60;
    /** ͳ�����ڣ��룩�� */
    private long statisticsInterval = 24 * 60 * 60;

    public StatisticsConfig()
    {
        super(CONFIG_NAME);

        this.addConfigValue(LogInterval, long.class, this.logInterval);
        this.addConfigValue(StatisticsInterval, long.class, this.statisticsInterval);

        this.print();
    }

    public long getLogInterval()
    {
        // ��С30�롣
        return Math.max(this.getLongValue(LogInterval), 30);
    }

    public long getStatisticsInterval()
    {
        // ��СһСʱ��
        return Math.max(this.getLongValue(StatisticsInterval), 60 * 60);
    }
}
