package org.dangcat.boot.statistics;

/**
 * ͳ�ƻ�����
 * @author dangcat
 * 
 */
public class StatisticsData extends StatisticsBase
{
    public static final String Error = "Error";
    public static final String Success = "Success";

    public StatisticsData(String name)
    {
        super(name);
        this.addCount(Success);
        this.addCount(Error);
        this.addVelocity(Velocity, Success);
        this.addTime(TimeCost);
    }
}
