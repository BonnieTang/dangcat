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
    public boolean isValid();

    /**
     * ��ȡ������ʵʱ��Ϣ��
     */
    public String readRealInfo();

    /**
     * ����ͳ����Ϣ��
     */
    public void reset();
}
