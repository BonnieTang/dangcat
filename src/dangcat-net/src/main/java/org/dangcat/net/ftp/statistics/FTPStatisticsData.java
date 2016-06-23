package org.dangcat.net.ftp.statistics;

import org.dangcat.boot.statistics.StatisticsData;

public class FTPStatisticsData extends StatisticsData
{
    /** �ļ���С�� */
    public static final String Size = "Size";
    /** �ļ���С���ʡ� */
    public static final String SizeVelocity = "SizeVelocity";

    public FTPStatisticsData(String name)
    {
        super(name);
        this.addCount(Size);
        this.addVelocity(SizeVelocity, Size, TimeCost);
    }
}
