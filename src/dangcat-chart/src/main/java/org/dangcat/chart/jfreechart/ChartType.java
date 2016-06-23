package org.dangcat.chart.jfreechart;

/**
 * ͳ��ͼ�Ρ�
 * @author dangcat
 * 
 */
public enum ChartType
{
    /** ����ͼ�� */
    Area(1),
    /** ��״ͼ�� */
    Bar(2),
    /** ��״��ջͼ�� */
    BarTime(7),
    /** �������ͼ�� */
    Combined(6),
    /** ��״ͼ�� */
    Line(3),
    /** ����λ��״ͼ�� */
    MultiLine(5),
    /** �ޡ� */
    None(0),
    /** ��״ͼ�� */
    Pie(4);

    /** ͳ��ͼ��ֵ�� */
    private final int value;

    /**
     * ����ͳ�����͡�
     * @param value ͳ��ͼ��ֵ��
     */
    ChartType(int value)
    {
        this.value = value;
    }

    /**
     * ȡ��ͳ��ͼ�Ρ�
     * @return ͳ��ͼ��ֵ��
     */
    public int getValue()
    {
        return this.value;
    }
}
