package org.dangcat.commons.formator;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * ��ֵ��ʽ����
 */
public class ValueFormator implements DataFormator {
    /**
     * Ĭ�����ָ�ʽ��ģ��
     */
    private static final String DEFAULT_NUMBER_FORMAT = "#.###";
    /**
     * ת��������
     */
    private final static int[] UNIT_TRANSCONSTS = new int[]{1, 1000, 1000, 1000};
    /**
     * ��ֵ��λ��
     */
    private final static String[] UNITS = new String[]{"", "K", "M", "G"};
    /**
     * ���ָ�ʽ��ģ��
     */
    private String format = null;
    /**
     * ���ָ�ʽģ�档
     */
    private NumberFormat numberFormat = null;

    /**
     * ȡ����ѵ�λ
     *
     * @param longValue ת��ֵ��
     * @return ��ѵ�λ��
     */
    public String calculatePerfectUnit(long longValue) {
        int[] transConsts = this.getTransConsts();
        String[] units = this.getUnits();
        String perfectUnit = units[0];
        double perfectValue = longValue * 1.0;
        for (int i = 1; i < units.length; i++) {
            if ((int) (perfectValue / transConsts[i]) == 0)
                break;
            perfectValue /= transConsts[i];
            perfectUnit = units[i];
        }
        return perfectUnit;
    }

    public double calculatePerfectValue(long longValue) {
        int[] transConsts = this.getTransConsts();
        double perfectValue = longValue * 1.0;
        for (int i = 1; i < transConsts.length; i++) {
            if ((int) (perfectValue / transConsts[i]) == 0)
                break;
            perfectValue /= transConsts[i];
        }
        return perfectValue;
    }

    /**
     * ����ָ����λ�µ�ת���ʡ�
     *
     * @param perfectUnit ָ����λ��
     * @return ת����
     */
    public double calculateTransRate(String perfectUnit) {
        int[] transConsts = this.getTransConsts();
        String[] units = this.getUnits();
        long transRate = 1;
        for (int i = 0; i < units.length; i++) {
            transRate *= transConsts[i];
            if (perfectUnit.equals(units[i]))
                break;
        }
        return 1.0 / transRate;
    }

    @Override
    public String format(Object data) {
        String text = null;
        if (data instanceof Long) {
            Long longValue = (Long) data;
            String perfectUnit = this.calculatePerfectUnit(longValue);
            double value = longValue * this.calculateTransRate(perfectUnit);
            text = this.getNumberFormat().format(value) + perfectUnit;
        } else if (data instanceof Double)
            text = this.getNumberFormat().format(data);
        return text;
    }

    protected String getDefaultFormat() {
        return DEFAULT_NUMBER_FORMAT;
    }

    public String getFormat() {
        return this.format == null ? this.getDefaultFormat() : this.format;
    }

    public void setFormat(String format) {
        this.format = format;
        this.numberFormat = null;
    }

    protected NumberFormat getNumberFormat() {
        if (this.numberFormat == null)
            this.numberFormat = new DecimalFormat(this.getFormat());
        return this.numberFormat;
    }

    public int[] getTransConsts() {
        return UNIT_TRANSCONSTS;
    }

    public String[] getUnits() {
        return UNITS;
    }
}
