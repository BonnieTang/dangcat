package org.dangcat.net.process.statistics;

import org.dangcat.boot.statistics.ProcessStatistics;

/**
 * ���ݴ���ͳ�ơ�
 * @author dangcat
 * 
 */
public class PacketProcessStatistics extends ProcessStatistics<PacketProcessStatisticsData>
{
    public PacketProcessStatistics(String name)
    {
        super(name);
    }

    @Override
    protected PacketProcessStatisticsData creatStatisticsData(String name)
    {
        return new PacketProcessStatisticsData(name);
    }

    public long increaseIgnore()
    {
        return this.increase(PacketProcessStatisticsData.Ignore);
    }

    /**
     * ��������
     */
    public long increaseParseError()
    {
        return this.increase(PacketProcessStatisticsData.ParseError);
    }

    /**
     * �������ݴ�С��
     */
    public long increaseReceive()
    {
        return this.increase(PacketProcessStatisticsData.Receive);
    }

    /**
     * У�����
     */
    public long increaseValidError()
    {
        return this.increase(PacketProcessStatisticsData.ValidError);
    }
}
