package org.dangcat.commons.formator;

/**
 * ������ʽ����
 */
public class OctetsFormator extends ValueFormator {
    /**
     * ת��������
     */
    private final static int[] UNIT_TRANSCONSTS = new int[]{1, 1024, 1024, 1024, 1024, 1024};
    /**
     * traffic units
     */
    private final static String[] UNITS = new String[]{"Byte", "KB", "MB", "GB", "TB", "PB"};

    @Override
    public int[] getTransConsts() {
        return UNIT_TRANSCONSTS;
    }

    @Override
    public String[] getUnits() {
        return UNITS;
    }
}
