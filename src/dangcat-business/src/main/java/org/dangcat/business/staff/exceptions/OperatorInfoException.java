package org.dangcat.business.staff.exceptions;

import org.dangcat.business.exceptions.BusinessException;
import org.dangcat.business.staff.domain.OperatorInfoCreate;

/**
 * The service exception for Operator.
 *@author dangcat
 * 
 */
public class OperatorInfoException extends BusinessException
{
    /** ֻ����������������Ա��Ϣ�� */
    public static final Integer INSERT_VALIDATEGROUP = 103;
    /** ����Ա����ɾ���Լ����˺š� */
    public static final Integer KILL_HIMSELFUL = 100;
    /** ֻ��ɾ������������Ա�˺š� */
    public static final Integer KILL_VALIDATEGROUP = 101;
    /** �����޸Ĳ���Ա�˺š� */
    public static final Integer MODIFY_NO_DENY = 106;
    /** ֻ���޸ı���������Ա��Ϣ�� */
    public static final Integer MODIFY_VALIDATEGROUP = 102;
    /** ������Ч�� */
    public static final Integer PASSWORD_INVALID = 105;
    /** ������������벻һ�¡� */
    public static final Integer PASSWORD_NOTEQUALS = 104;

    private static final long serialVersionUID = 1L;

    public OperatorInfoException(Integer messageId, Object... params)
    {
        super(messageId, params);
    }

    public OperatorInfoException(String fieldName, Integer messageId, Object... params)
    {
        super(OperatorInfoCreate.class, fieldName, messageId, params);
    }
}
