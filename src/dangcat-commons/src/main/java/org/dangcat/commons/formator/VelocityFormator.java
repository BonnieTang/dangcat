package org.dangcat.commons.formator;

/**
 * ���ʸ�ʽ����
 * 
 */
public class VelocityFormator extends ValueFormator
{
    /** ������λ�� */
    private final static String[] UNITS = new String[] { "/S", "K/S", "M/S", "G/S" };

    @Override
    public String[] getUnits()
    {
        return UNITS;
    }
}
