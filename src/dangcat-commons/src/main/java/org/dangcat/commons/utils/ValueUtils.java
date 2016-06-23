package org.dangcat.commons.utils;

import org.dangcat.commons.formator.DateFormator;
import org.dangcat.commons.formator.DateType;

import java.lang.reflect.Array;
import java.util.Date;

/**
 * ֵת�����ߡ�
 */
public class ValueUtils {
    /**
     * �Ƚ���������Ĵ�С��
     *
     * @param from ��Դ����
     * @param to   Ŀ�����
     * @return �Ƚϴ�С��
     */
    public static int compare(Object from, Object to) {
        return ValueCompare.compare(from, to);
    }

    /**
     * �Ƿ��ǲ������͡�
     */
    public static boolean isBoolean(Class<?> classType) {
        if (classType == null)
            return false;
        return boolean.class.equals(classType) || Boolean.class.equals(classType);
    }

    /**
     * �ж���ֵ�Ƿ�Ϊ�ա�
     *
     * @param value ��ֵ����
     * @return �жϽ����
     */
    public static boolean isEmpty(Object value) {
        if (value == null)
            return true;
        if (value instanceof String) {
            String text = (String) value;
            return text.isEmpty() || text.trim().isEmpty();
        }
        if (value.getClass().isArray())
            return Array.getLength(value) == 0;
        return false;
    }

    /**
     * �Ƿ����������͡�
     */
    public static boolean isNumber(Class<?> classType) {
        if (classType == null)
            return false;
        return Number.class.isAssignableFrom(classType) || double.class.equals(classType) || short.class.equals(classType) || int.class.equals(classType) || float.class.equals(classType);
    }

    /**
     * �Ƿ����ִ����͡�
     */
    public static boolean isText(Class<?> classType) {
        if (classType == null)
            return false;
        return String.class.equals(classType) || char[].class.equals(classType) || Character[].class.equals(classType) || byte[].class.equals(classType) || Byte[].class.equals(classType);
    }

    /**
     * �жϸ������Ƿ�Ϊ0��
     *
     * @param value ����ֵ��
     * @return �жϽ����
     */
    public static boolean isZero(double value) {
        return value == 0.0 || Math.abs(value) < 0.00001;
    }

    /**
     * �����ַ������顣
     *
     * @param values �ַ������顣
     * @return ���Ӻ���ִ���
     */
    public static String join(String... values) {
        return join(values, ";");
    }

    /**
     * �����ַ������顣
     *
     * @param values   �ַ������顣
     * @param joinText �����ַ���
     * @return ���Ӻ���ִ���
     */
    public static String join(String[] values, String joinText) {
        String result = null;
        if (values != null && values.length > 0) {
            StringBuilder value = new StringBuilder();
            for (String text : values) {
                if (value.length() > 0)
                    value.append(joinText);
                value.append(text);
            }
            result = value.toString();
        }
        return result;
    }

    /**
     * ���ִ�����������
     *
     * @param text �ִ����֡�
     * @return ����ֵ��
     */
    public static Boolean parseBoolean(String text) {
        return parseBoolean(text, false);
    }

    /**
     * ���ִ�����������
     *
     * @param text         �ִ����֡�
     * @param defaultValue Ĭ��ֵ��
     * @return ����ֵ��
     */
    public static Boolean parseBoolean(String text, Boolean defaultValue) {
        return TextParser.parseBoolean(text, defaultValue);
    }

    /**
     * ���ִ��������ڡ�
     *
     * @param text �ִ����֡�
     * @return ����ֵ��
     */
    public static Date parseDate(String text) {
        return parseDate(text, null);
    }

    /**
     * ���ִ��������ڡ�
     *
     * @param text         �ִ����֡�
     * @param defaultValue Ĭ��ֵ��
     * @return ����ֵ��
     */
    public static Date parseDate(String text, Date defaultValue) {
        return DateUtils.parse(text, defaultValue);
    }

    /**
     * ���ִ�������������
     *
     * @param text �ִ����֡�
     * @return ����ֵ��
     */
    public static Double parseDouble(String text) {
        return parseDouble(text, null);
    }

    /**
     * ���ִ�������������
     *
     * @param text         �ִ����֡�
     * @param defaultValue Ĭ��ֵ��
     * @return ����ֵ��
     */
    public static Double parseDouble(String text, Double defaultValue) {
        return TextParser.parseDouble(text, defaultValue);
    }

    /**
     * ����ö�����͡�
     *
     * @param <T>
     * @param classType ö�����͡�
     * @param text      �ִ���
     * @return ������Ķ���
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseEnum(Class<?> classType, String text) {
        return (T) TextParser.parseEnum(classType, text);
    }

    /**
     * ��ö��ֵ����ö�����͡�
     *
     * @param <T>
     * @param classType ö�����͡�
     * @param value     ö��ֵ��
     * @return ������Ķ���
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseEnum(Class<T> classType, int value) {
        return (T) TextParser.parseEnum(classType, value);
    }

    /**
     * ���ִ�����������
     *
     * @param text �ִ����֡�
     * @return ����ֵ��
     */
    public static Integer parseInt(String text) {
        return parseInt(text, null);
    }

    /**
     * ���ִ�����������
     *
     * @param text         �ִ����֡�
     * @param defaultValue Ĭ��ֵ��
     * @return ����ֵ��
     */
    public static Integer parseInt(String text, Integer defaultValue) {
        return TextParser.parseInt(text, defaultValue);
    }

    /**
     * ���ִ�������������
     *
     * @param text �ִ����֡�
     * @return ����ֵ��
     */
    public static Long parseLong(String text) {
        return parseLong(text, null);
    }

    /**
     * ���ִ�������������
     *
     * @param text         �ִ����֡�
     * @param defaultValue Ĭ��ֵ��
     * @return ����ֵ��
     */
    public static Long parseLong(String text, Long defaultValue) {
        return TextParser.parseLong(text, defaultValue);
    }

    /**
     * ���ִ�������������
     *
     * @param text �ִ����֡�
     * @return ����ֵ��
     */
    public static Short parseShort(String text) {
        return parseShort(text, null);
    }

    /**
     * ���ִ�������������
     *
     * @param text         �ִ����֡�
     * @param defaultValue Ĭ��ֵ��
     * @return ����ֵ��
     */
    public static Short parseShort(String text, Short defaultValue) {
        return TextParser.parseShort(text, defaultValue);
    }

    /**
     * ����ֵת����ʵ��ֵ����
     */
    public static Object parseValue(Class<?> classType, String value) {
        return TextParser.parseValue(classType, value);
    }

    /**
     * ��ֵת�����ִ�ֵת��
     */
    public static String toString(Object value) {
        String text = null;
        try {
            if (value == null)
                return text;

            Class<?> classType = value.getClass();
            if (String.class.equals(classType))
                text = (String) value;
            else if (Character[].class.equals(classType) || char[].class.equals(classType))
                text = new String((char[]) value);
            else if (Date.class.isAssignableFrom(classType))
                text = DateFormator.getDateFormator(DateType.Full).format(value);
            else if (byte[].class.equals(classType))
                text = ParseUtils.toHex((byte[]) value);
            else
                text = value.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
