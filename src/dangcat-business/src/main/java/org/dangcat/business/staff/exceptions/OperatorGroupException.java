package org.dangcat.business.staff.exceptions;

import org.dangcat.business.exceptions.BusinessException;
import org.dangcat.business.staff.domain.OperatorGroup;

/**
 * The service exception for OperatorGroup.
 *@author dangcat
 * 
 */
public class OperatorGroupException extends BusinessException
{
    /** ��������Ա�鲻��ѭ���󶨡� */
    public static final Integer BIND_CYCLING = 100;
    /** �Ѿ��󶨲���Ա���鲻��ɾ���� */
    public static final Integer CHILE_OPERATOR_EXISTS = 101;
    /** ����ɾ��������Ա�顣 */
    public static final Integer DELETE_DENY_FOR_ISPARENT = 103;
    /** ֻ��ɾ���ӳ�Ա�顣 */
    public static final Integer DELETE_DENY_FOR_NOTMEMBER = 102;
    /** ֻ���޸ı�����ӳ�Ա�顣 */
    public static final Integer MODIFY_DENY_FOR_NOTMEMBER = 104;
    private static final long serialVersionUID = 1L;

    public OperatorGroupException(Integer messageId, Object... params)
    {
        super(messageId, params);
    }

    public OperatorGroupException(String fieldName, Integer messageId, Object... params)
    {
        super(OperatorGroup.class, fieldName, messageId, params);
    }
}
