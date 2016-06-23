package org.dangcat.commons.formator;

/**
 * �ٷ�����ʽ����
 * 
 */
public class PercentFormator extends ValueFormator
{
    /** Ĭ�����ָ�ʽ��ģ�� */
    private static final String DEFAULT_NUMBER_FORMAT = "0.00";
    /** ��С�ٷ��� */
    private static final double PERCENT_MINVALUE = 0.01;
    /** ����ֵ��� */
    private static final String PERCENT_NEGATIVE_ZERO = ">-0.01";
    /** ����ֵ��� */
    private static final String PERCENT_POSITIVE_ZERO = "<0.01";
    /** ת�������� */
    private final static int[] UNIT_TRANSCONSTS = new int[] { 1 };
    /** ������λ�� */
    private final static String[] UNITS = new String[] { "%" };

    /**
     * ��ʽ�����ݡ�
     * @param data ת��ֵ��
     * @return ת����ѵ�λ�ĸ�������
     */
    @Override
    public String format(Object data)
    {
        double doubleValue = 0.0;
        if (data instanceof Number)
            doubleValue = ((Number) data).doubleValue();
        String text = null;
        double value = Math.abs(doubleValue);
        if (value != 0.0 && value < PERCENT_MINVALUE)
        {
            if (doubleValue < 0.0)
                text = PERCENT_NEGATIVE_ZERO;
            else
                text = PERCENT_POSITIVE_ZERO;
        }
        else
            text = super.format(doubleValue);
        return text + UNITS[0];
    }

    @Override
    protected String getDefaultFormat()
    {
        return DEFAULT_NUMBER_FORMAT;
    }

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
