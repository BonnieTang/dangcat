package org.dangcat.boot.statistics;

/**
 * ͳ�ƽӿڡ�
 * @author dangcat
 * 
 */
public interface StatisticsAble
{
    /**
     * ͳ����Ϣ�Ƿ���Ч��
     */
    boolean isValid();

    /**
     * ��ȡ������ʵʱ��Ϣ��
     */
    String readRealInfo();

    /**
     * ����ͳ����Ϣ��
     */
    void reset();
}
