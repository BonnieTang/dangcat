package org.dangcat.boot.security.exceptions;

import org.dangcat.framework.exception.ServiceException;

public class SecurityLoginException extends ServiceException
{
    public static final String FIELDNAME_LICENSE = "license";
    public static final String FIELDNAME_NO = "no";
    public static final String FIELDNAME_PASSWORD = "password";

    /** ���ȵ�¼ϵͳ�� */
    public static final Integer INVALID_LOGIN = 405;
    /** ���벻��ȷ�� */
    public static final Integer INVALID_PASSWORD = 402;
    /** ��½��ɫ�����ڡ� */
    public static final Integer INVALID_ROLE = 403;
    /** licenseδ��Ȩ���ѹ��� */
    public static final Integer LICENSE_INVALIDATE = 408;
    /** �û�����Ϊ�ա� */
    public static final Integer NO_EMPTY = 401;
    /** �˻��ѹ��ڡ� */
    public static final Integer NO_EXPIRYTIME = 407;
    /** �˻������ڡ� */
    public static final Integer NO_NOT_EXISTS = 404;
    /** �˻���ͣ�á� */
    public static final Integer NO_USEABLE_FALSE = 406;
    /** ���벻��Ϊ�ա� */
    public static final Integer PASSWORD_EMPTY = 400;
    private static final long serialVersionUID = 1L;
    private String fieldName = null;

    public SecurityLoginException(Integer messageId, Object... params)
    {
        super(messageId, params);
    }

    public SecurityLoginException(String fieldName, Integer messageId, Object... params)
    {
        super(messageId, params);
        this.fieldName = fieldName;
    }

    public String getFieldName()
    {
        return this.fieldName;
    }
}
