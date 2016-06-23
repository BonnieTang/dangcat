package org.dangcat.business.exceptions;

import org.dangcat.persistence.validator.exception.DataValidateException;

/**
 * ҵ������쳣��
 *
 * @author dangcat
 */
public class BusinessException extends DataValidateException {
    /**
     * ������Ȩ�޲����޸ġ�
     */
    public static final Integer ADVANCED_MODIFY = 59;
    /**
     * ��������ʧ�ܡ�
     */
    public static final Integer CREATE_ERROR = 53;
    /**
     * ���ݲ����ڡ�
     */
    public static final Integer DATA_NOTEXISTS = 56;
    /**
     * û���ҵ����������Ľ����
     */
    public static final Integer DATA_NOTFOUND = 54;
    /**
     * �����ظ���
     */
    public static final Integer DATA_REPEAT = 55;
    /**
     * ����״̬����ȷ��
     */
    public static final Integer DATASTATE_INCORRECT = 60;
    /**
     * ɾ�����ݴ���
     */
    public static final Integer DELETE_ERROR = 50;
    /**
     * �ֶβ����ڡ�
     */
    public static final Integer FIELD_NOTEXISTS = 58;
    /**
     * ִ�д�����鿴��̨��־��
     */
    public static final Integer INVOKE_ERROR = 60;
    /**
     * �������ݴ���
     */
    public static final Integer LOAD_ERROR = 51;
    /**
     * û��ִ��Ȩ�ޡ�
     */
    public static final Integer PERMISSION_DENY = 57;
    /**
     * ��������ʧ�ܡ�
     */
    public static final Integer SAVE_ERROR = 52;
    private static final long serialVersionUID = 1L;

    public BusinessException(Class<?> classType, String fieldName, Integer messageId, Object... params) {
        super(classType, fieldName, messageId, params);
    }

    public BusinessException(Integer messageId, Object... params) {
        super(messageId, params);
    }
}
