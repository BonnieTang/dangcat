package org.dangcat.boot.statistics;

/**
 * ͳ�Ʒ���
 *
 * @author dangcat
 */
public interface StatisticsService {
    /**
     * ���ͳ�ƶ���
     *
     * @param statisticsAble ͳ�ƶ���
     */
    void addStatistics(StatisticsAble statisticsAble);

    /**
     * ɾ��ͳ�ƶ���
     *
     * @param statisticsAble ͳ�ƶ���
     */
    void removeStatistics(StatisticsAble statisticsAble);
}
