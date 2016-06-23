package org.dangcat.commons.resource;

import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

/**
 * ��Դ��ȡ�ӿڡ�
 *
 * @author dangcat
 */
public abstract class ResourceReader {
    public String format(String message, Object... params) {
        if (params != null && params.length > 0 && !ValueUtils.isEmpty(message))
            message = MessageFormat.format(message, params);
        return message;
    }

    /**
     * ��ȡ����
     *
     * @param locale ��������
     * @param key    �ؼ��֡�
     * @return ��Դ����
     */
    public abstract Object getObject(Locale locale, String key);

    /**
     * ��ȡ����
     *
     * @param key �ؼ��֡�
     * @return ��Դ����
     */
    public Object getObject(String key) {
        return this.getObject(Environment.getCurrentLocale(), key);
    }

    /**
     * ��ȡ�ı���
     *
     * @param locale ��������
     * @param key    �ؼ��֡�
     * @return ��Դ�ı���
     */
    public abstract String getText(Locale locale, String key, Object... params);

    /**
     * ��ȡ�ı���
     *
     * @param key �ؼ��֡�
     * @return ��Դ�ı���
     */
    public String getText(String key, Object... params) {
        return this.getText(Environment.getCurrentLocale(), key, params);
    }

    /**
     * ��ȡӳ���
     *
     * @param locale ��������
     * @param key    �ؼ��֡�
     * @return ��Դӳ���
     */
    public abstract Map<Integer, String> getValueMap(Locale locale, String key);

    /**
     * ��ȡӳ���
     *
     * @param key �ؼ��֡�
     * @return ��Դӳ���
     */
    public Map<Integer, String> getValueMap(String key) {
        return this.getValueMap(Environment.getCurrentLocale(), key);
    }
}
