package org.dangcat.business.validator.exceptions;

import org.dangcat.persistence.validator.exception.DataValidateException;

/**
 * �߼�У���쳣��
 * @author dangcat
 * 
 */
public class LogicValidatorException extends DataValidateException
{
    /** �ʼ����벻��ȷ�� */
    public static final Integer INVALIDATE_EMAIL = 101;
    /** wrong ipv4 format */
    public static final Integer INVALIDATE_IPV4 = 104;
    /** wrong ipv6 format */
    public static final Integer INVALIDATE_IPV6 = 105;
    /** �ֻ����벻��ȷ�� */
    public static final Integer INVALIDATE_MOBILE = 103;
    /** �˺����벻��ȷ�� */
    public static final Integer INVALIDATE_NO = 100;
    /** �绰���벻��ȷ�� */
    public static final Integer INVALIDATE_TEL = 102;
    private static final long serialVersionUID = 1L;

    public LogicValidatorException(Class<?> classType, String fieldName, Integer messageId, Object... params)
    {
        super(classType, fieldName, messageId, params);
    }
}
