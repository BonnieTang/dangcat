package org.dangcat.boot.event.statistics;

import org.dangcat.boot.statistics.StatisticsData;

public class EventStatisticsData extends StatisticsData
{
    /** ���������� */
    public static final String HandleCount = "HandleCount";
    /** �����ʱ�� */
    public static final String HandleTimeCost = "HandleTimeCost";
    /** �������ʡ� */
    public static final String HandleVelocity = "HandleVelocity";

    public EventStatisticsData(String name)
    {
        super(name);
        this.addCount(HandleCount);
        this.addTime(HandleTimeCost);
        this.addVelocity(HandleVelocity, HandleCount, HandleTimeCost);
    }
}
