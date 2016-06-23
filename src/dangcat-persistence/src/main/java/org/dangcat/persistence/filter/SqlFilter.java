package org.dangcat.persistence.filter;

class SqlFilter implements Filter {
    /**
     * У����ֵ�Ƿ���Ч��
     *
     * @param values ��ֵ���顣
     * @param value  �Ƚ϶���
     * @return �Ƿ���Ч��
     */
    @Override
    public boolean isValid(Object[] values, Object value) {
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
            if (valueObject instanceof String)
                return (String) valueObject;
        }
        return null;
    }
}
