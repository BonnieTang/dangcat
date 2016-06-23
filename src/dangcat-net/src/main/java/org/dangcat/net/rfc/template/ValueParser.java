package org.dangcat.net.rfc.template;

import org.dangcat.net.rfc.exceptions.ProtocolParseException;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;

/**
 * ���ݽ����ӿڡ�
 * @author dangcat
 * 
 */
public interface ValueParser
{
    /**
     * �Ƚ�����ֵ�Ƿ���ͬ��
     * @param value Ŀ��ֵ��
     * @return
     */
    int compareTo(Object value);

    /**
     * ��ԭʼ�ֽ���������ɶ���
     * @param bytes �ֽ����顣
     * @param beginIndex ��ʼλ�á�
     * @param length ���ȡ�
     * @return ������Ķ���
     * @throws ProtocolParseException �����쳣��
     */
    void parse(byte[] bytes, int beginIndex, int length) throws ProtocolParseException;

    /**
     * ���ִ��������ԡ�
     * @param text �ֽ����顣
     */
    void parse(String text) throws ProtocolParseException;

    /**
     * ����ת����Э�����ݡ�
     * @return ת������ֽ����顣
     */
    byte[] toBytes();

    /**
     * У�������Ƿ�Ϸ���
     * @throws ProtocolValidateException У���쳣��
     */
    void validate() throws ProtocolValidateException;
}
