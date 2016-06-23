package org.dangcat.chart;

import java.util.Calendar;

public enum TimeType
{
    Day(Calendar.DAY_OF_YEAR), Hour(Calendar.HOUR_OF_DAY), Minute(Calendar.MINUTE), Month(Calendar.MONTH), Week(Calendar.WEEK_OF_MONTH), Year(Calendar.YEAR);

    private final int value;

    /**
     * ����ʱ�����͡�
     * @param value ��������ֵ��
     */
    TimeType(int value)
    {
        this.value = value;
    }

    /**
     * ȡ��ʱ�����͡�
     * @return ��������ֵ��
     */
    public int getValue()
    {
        return this.value;
    }
}
