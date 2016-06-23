package org.dangcat.commons.formator;

/**
 * ʱ���ʽ����
 * 
 */
public class TimeLengthFormator extends ValueFormator
{
    /** ת�������� */
    private final static int[] UNIT_TRANSCONSTS = new int[] { 1, 1000, 60, 60 };
    /** ������λ�� */
    private final static String[] UNITS = new String[] { "ms", "Sec", "Min", "Hour" };

    @Override
    public int[] getTransConsts()
    {
        return UNIT_TRANSCONSTS;
    }

    @Override
    public String[] getUnits()
    {
        return UNITS;
    }
}
