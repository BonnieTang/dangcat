package org.dangcat.persistence.filter;

/**
 * ���˱��ʽ��
 * @author dangcat
 * 
 */
public interface FilterExpress extends java.io.Serializable
{
    /**
     * �ж��������Ƿ�����Ҫ��
     * @param value ���ݶ���
     * @return ������Ϊtrue������Ϊfalse��
     */
    boolean isValid(Object value);
}
