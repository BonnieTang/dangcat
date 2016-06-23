package org.dangcat.persistence.filter;

/**
 * ��������
 * @author dangcat
 * 
 */
interface Filter
{
    /**
     * У����ֵ�Ƿ���Ч��
     * @param values ��ֵ���顣
     * @param value �Ƚ϶���
     * @return �Ƿ���Ч��
     */
    boolean isValid(Object[] values, Object value);

    /**
     * ת����SQL���ʽ��
     * @param fieldName �ֶ�����
     * @param values ��ֵ���顣
     * @return ���ʽ��
     */
    String toSql(String fieldName, Object[] values);
}
