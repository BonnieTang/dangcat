package org.dangcat.business.exceptions;

import org.dangcat.persistence.validator.exception.DataValidateInformation;

/**
 * ҵ�������Ϣ��
 *
 * @author dangcat
 */
public class BusinessInformation extends DataValidateInformation {
    /**
     * û���ҵ����������Ľ����
     */
    public static final Integer DATA_NOTFOUND = 50;
    /**
     * �ɹ�ɾ�����ݡ�
     */
    public static final Integer DELETE_SUCCESS = 51;
    private static final long serialVersionUID = 1L;

    public BusinessInformation(Class<?> classType, String fieldName, Integer messageId, Object... params) {
        super(classType, fieldName, messageId, params);
    }

    public BusinessInformation(Integer messageId, Object... params) {
        super(messageId, params);
    }
}
