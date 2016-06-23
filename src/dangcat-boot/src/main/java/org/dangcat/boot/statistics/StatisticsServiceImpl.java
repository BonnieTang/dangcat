package org.dangcat.boot.statistics;

import org.dangcat.boot.config.StatisticsConfig;
import org.dangcat.boot.service.impl.ThreadService;
import org.dangcat.commons.timer.IntervalAlarmClock;
import org.dangcat.commons.utils.DateUtils;
import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.service.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * ͳ�Ʒ���
 * @author dangcat
 * 
 */
public class StatisticsServiceImpl extends ThreadService implements StatisticsService
{
    private static final String SERVICE_NAME = "STATISTICS";
    /** ͳ����ʼʱ�䡣 */
    private long beginTime = DateUtils.currentTimeMillis();
    /** ͳ�ƶ����б� */
    private List<StatisticsAble> statisticsList = new ArrayList<StatisticsAble>();

    /**
     * �������
     * @param parent ��������
     */
    public StatisticsServiceImpl(ServiceProvider parent)
    {
        super(parent, SERVICE_NAME);
    }

    /**
     * ���ͳ�ƶ���
     * @param statisticsAble ͳ�ƶ���
     */
    public void addStatistics(StatisticsAble statisticsAble)
    {
        if (statisticsAble != null && !this.statisticsList.contains(statisticsAble))
            this.statisticsList.add(statisticsAble);
    }

    @Override
    public void initialize()
    {
        super.initialize();

        this.setAlarmClock(new IntervalAlarmClock(this)
        {
            @Override
            public long getInterval()
            {
                return StatisticsConfig.getInstance().getLogInterval();
            }

            @Override
            public boolean isEnabled()
            {
                return StatisticsConfig.getInstance().isEnabled();
            }
        });
    }

    /**
     * ��ʱ���ͳ����Ϣ��
     */
    @Override
    protected void innerExecute()
    {
        if (this.statisticsList.size() > 0)
        {
            StringBuilder info = new StringBuilder();
            for (StatisticsAble statisticsAble : this.statisticsList)
            {
                if (statisticsAble.isValid())
                {
                    String message = statisticsAble.readRealInfo();
                    if (!ValueUtils.isEmpty(message))
                    {
                        info.append(Environment.LINE_SEPARATOR);
                        info.append(message);
                    }
                }
            }
            if (info.length() > 0)
                logger.info(info);
        }
        this.reset();
    }

    /**
     * ɾ��ͳ�ƶ���
     * @param statisticsAble ͳ�ƶ���
     */
    public void removeStatistics(StatisticsAble statisticsAble)
    {
        if (statisticsAble != null && this.statisticsList.contains(statisticsAble))
            this.statisticsList.remove(statisticsAble);
    }

    /**
     * ����ͳ��ֵ��
     */
    private void reset()
    {
        if (DateUtils.currentTimeMillis() - this.beginTime > StatisticsConfig.getInstance().getStatisticsInterval() * 1000)
        {
            for (StatisticsAble statisticsAble : this.statisticsList)
                statisticsAble.reset();
            this.beginTime = DateUtils.currentTimeMillis();
        }
    }
}
