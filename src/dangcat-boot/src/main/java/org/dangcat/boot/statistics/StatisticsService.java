package org.dangcat.boot.statistics;

/**
 * ͳ�Ʒ���
 * @author dangcat
 * 
 */
public interface StatisticsService
{
    /**
     * ���ͳ�ƶ���
     * @param statisticsAble ͳ�ƶ���
     */
    public void addStatistics(StatisticsAble statisticsAble);

    /**
     * ɾ��ͳ�ƶ���
     * @param statisticsAble ͳ�ƶ���
     */
    public void removeStatistics(StatisticsAble statisticsAble);
}
