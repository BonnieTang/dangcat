package org.dangcat.persistence.filter;

/**
 * �������͡�
 * @author dangcat
 * 
 */
public enum FilterType
{
    /**
     * ��Χ��
     */
    between(1),
    /**
     * ���ڡ�
     */
    eq(2),
    /**
     * ���ڵ��ڡ�
     */
    ge(4),
    /**
     * ���ڡ�
     */
    gt(3),
    /**
     * ���ԡ�
     */
    ignore(0),
    /**
     * С�ڵ��ڡ�
     */
    le(6),
    /**
     * LIKE��
     */
    like(7),
    /**
     * С�ڡ�
     */
    lt(5),
    /**
     * �Զ����Ӿ䡣
     */
    sql(8);

    /**
     * ��������ֵ��
     */
    private final int value;

    /**
     * ����������͡�
     * @param value ��������ֵ��
     */
    FilterType(int value)
    {
        this.value = value;
    }

    /**
     * ȡ�ù������͡�
     * @return ��������ֵ��
     */
    public int getValue()
    {
        return this.value;
    }
}
