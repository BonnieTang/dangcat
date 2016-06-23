package org.dangcat.net.process.statistics;

import org.dangcat.boot.statistics.StatisticsData;

/**
 * ���ݴ���ͳ��
 *
 * @author dangcat
 */
public class PacketProcessStatisticsData extends StatisticsData {
    /**
     * ���ܲ�����Ե����ݡ�
     */
    public static final String Ignore = "Ignore";
    /**
     * ������������ݡ�
     */
    public static final String ParseError = "ParseError";
    /**
     * ���յ����ݡ�
     */
    public static final String Receive = "Receive";
    /**
     * ���յ����ʡ�
     */
    public static final String ReceiveVelocity = "ReceiveVelocity";
    /**
     * �����������ݡ�
     */
    public static final String ValidError = "ValidError";

    public PacketProcessStatisticsData(String name) {
        super(name);
        this.addCount(Receive);
        this.addVelocity(ReceiveVelocity, Receive);
        this.addCount(ParseError);
        this.addCount(ValidError);
        this.addCount(Ignore);
    }
}
