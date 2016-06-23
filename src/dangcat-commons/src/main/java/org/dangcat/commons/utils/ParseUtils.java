package org.dangcat.commons.utils;

/**
 * ���Ľ������ߡ�
 */
public class ParseUtils {
    private static final String HEX_SPACE = " ";
    private static final String HEX_TEXT = "0123456789ABCDEF";

    /**
     * �ɶ������������������
     *
     * @param bytes      ���������顣
     * @param beginIndex ��ʵ����λ�á�
     * @param length     ���ȡ�
     * @return �������������
     */
    public static final int getInt(byte[] bytes, int beginIndex, int length) {
        return (int) getLong(bytes, beginIndex, length);
    }

    /**
     * �ɶ��������������������
     *
     * @param bytes      ���������顣
     * @param beginIndex ��ʵ����λ�á�
     * @param length     ���ȡ�
     * @return �������������
     */
    public static final long getLong(byte[] bytes, int beginIndex, int length) {
        long value = 0;
        for (int i = 0; i < length; i++)
            value |= (long) (bytes[beginIndex + i] & 0xff) << (8 * (length - 1 - i));
        return value;
    }

    /**
     * ���ַ����������������顣
     *
     * @param text �ַ�����
     * @return ����������顣
     */
    public static final byte[] parseHex(String text) {
        return parseHex(text, HEX_SPACE);
    }

    /**
     * ���ַ����������������顣
     *
     * @param text  �ַ�����
     * @param space �ָ��ַ���
     * @return ����������顣
     */
    public static final byte[] parseHex(String text, String space) {
        if (ValueUtils.isEmpty(text))
            return null;

        String hexText = text.trim().toUpperCase();
        String[] hexArray = null;
        if (space != null && hexText.indexOf(space) > -1)
            hexArray = hexText.split(space);
        else {
            hexArray = new String[hexText.length() / 2];
            for (int i = 0; i < hexArray.length; i++)
                hexArray[i] = hexText.substring(i * 2, (i + 1) * 2);
        }
        byte[] bytes = new byte[hexArray.length];
        int index = 0;
        for (String hex : hexArray) {
            bytes[index] |= (byte) ((HEX_TEXT.indexOf(hex.substring(0, 1)) & 0xf) << 4);
            bytes[index] |= (byte) (HEX_TEXT.indexOf(hex.substring(1, 2)) & 0xf);
            index++;
        }
        return bytes;
    }

    /**
     * ������ת����ָ�����ȵĶ��������顣
     *
     * @param value  ������ֵ��
     * @param length ���ȡ�
     * @return ת����Ķ��������顣
     */
    public static final byte[] toBytes(int value, int length) {
        return toBytes((long) value, length);
    }

    /**
     * �ɳ�����ת����ָ�����ȵĶ��������顣
     *
     * @param value  ������ֵ��
     * @param length ���ȡ�
     * @return ת����Ķ��������顣
     */
    public static byte[] toBytes(long value, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = (byte) ((value >>> (8 * (length - 1 - i))) & 0xff);
        return bytes;
    }

    /**
     * ת���ֽ�Ϊ16���Ʊ���ִ���
     *
     * @param value �ֽ����ݡ�
     * @return ת������ִ���
     */
    public static final String toHex(byte value) {
        StringBuffer info = new StringBuffer(2);
        toHex(info, value);
        return info.toString();
    }

    /**
     * ת���ֽ�Ϊ16���Ʊ���ִ���
     *
     * @param value �ֽ����ݡ�
     * @return ת������ִ���
     */
    public static final String toHex(byte[] bytes) {
        return toHex(bytes, 0);
    }

    /**
     * ת���ֽ�Ϊ16���Ʊ���ִ���
     *
     * @param bytes      �ֽ����ݡ�
     * @param changeLine ����������
     * @return ת������ִ���
     */
    public static final String toHex(byte[] bytes, int changeLine) {
        return toHex(bytes, changeLine, HEX_SPACE);
    }

    /**
     * ת���ֽ�Ϊ16���Ʊ���ִ���
     *
     * @param bytes      �ֽ����ݡ�
     * @param changeLine ����������
     * @param space      �ַ������
     * @return ת������ִ���
     */
    public static final String toHex(byte[] bytes, int changeLine, String space) {
        StringBuffer info = new StringBuffer(2);
        int count = 0;
        for (byte value : bytes) {
            if (count > 0) {
                if (changeLine > 0 && count > changeLine) {
                    count = 0;
                    info.append(Environment.LINE_SEPARATOR);
                } else if (space != null)
                    info.append(space);
            }
            toHex(info, value);
            count++;
        }
        return info.toString();
    }

    /**
     * ת���ֽ�Ϊ16���Ʊ���ִ���
     *
     * @param bytes �ֽ����ݡ�
     * @param space �ַ������
     * @return ת������ִ���
     */
    public static final String toHex(byte[] bytes, String space) {
        return toHex(bytes, 0, space);
    }

    /**
     * ת���ֽ�Ϊ16���Ʊ���ִ���
     *
     * @param value �ֽ����ݡ�
     * @return ת������ִ���
     */
    private static void toHex(StringBuffer info, byte value) {
        info.append(HEX_TEXT.charAt(0xf & value >>> 4));
        info.append(HEX_TEXT.charAt(value & 0xf));
    }
}
