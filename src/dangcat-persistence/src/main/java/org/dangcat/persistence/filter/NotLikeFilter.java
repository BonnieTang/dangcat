package org.dangcat.persistence.filter;

import org.dangcat.persistence.model.TableStatementHelper;

public class NotLikeFilter implements Filter {
    /**
     * У����ֵ�Ƿ���Ч��
     *
     * @param values ��ֵ���顣
     * @param value  �Ƚ϶���
     * @return �Ƿ���Ч��
     */
    @Override
    public boolean isValid(Object[] values, Object value) {
        if (values != null && values.length > 0 && value instanceof String) {
            String compareObject = (String) value;
            for (Object valueObject : values) {
                if (valueObject instanceof String) {
                    if (compareObject.indexOf((String) valueObject) == -1)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * ת����SQL���ʽ��
     *
     * @param fieldName �ֶ�����
     * @param values    ��ֵ���顣
     * @return ���ʽ��
     */
    @Override
    public String toSql(String fieldName, Object[] values) {
        for (Object valueObject : values) {
            if (valueObject instanceof String) {
                String value = TableStatementHelper.toSqlString(valueObject);
                return fieldName + " NOT LIKE " + value.substring(0, value.length() - 1) + "%'";
            }
        }
        return null;
    }
}
