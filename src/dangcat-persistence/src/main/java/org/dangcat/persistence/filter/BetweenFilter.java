package org.dangcat.persistence.filter;

import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.model.TableStatementHelper;

class BetweenFilter implements Filter
{
    /**
     * У����ֵ�Ƿ���Ч��
     * @param values ��ֵ���顣
     * @param value �Ƚ϶���
     * @return �Ƿ���Ч��
     */
    @Override
    public boolean isValid(Object[] values, Object value)
    {
        boolean result = false;
        if (values != null)
        {
            Object from = values.length > 0 ? values[0] : null;
            Object to = values.length > 1 ? values[1] : null;
            if (from != null && to != null)
                result = ValueUtils.compare(value, from) >= 0 && ValueUtils.compare(value, to) <= 0;
            else if (from != null)
                result = ValueUtils.compare(value, from) >= 0;
            else if (to != null)
                result = ValueUtils.compare(value, to) <= 0;
        }
        return result;
    }

    /**
     * ת����SQL���ʽ��
     * @param fieldName �ֶ�����
     * @param values ��ֵ���顣
     * @return ���ʽ��
     */
    @Override
    public String toSql(String fieldName, Object[] values)
    {
        StringBuilder sql = new StringBuilder();
        if (values != null)
        {
            Object from = values.length > 0 ? values[0] : null;
            Object to = values.length > 1 ? values[1] : null;
            if (from != null && to != null)
            {
                sql.append(fieldName + " BETWEEN ");
                sql.append(TableStatementHelper.toSqlString(from));
                sql.append(" AND ");
                sql.append(TableStatementHelper.toSqlString(to));
            }
            else if (from != null)
            {
                sql.append(fieldName + " >= ");
                sql.append(TableStatementHelper.toSqlString(from));
            }
            else if (to != null)
            {
                sql.append(fieldName + " <= ");
                sql.append(TableStatementHelper.toSqlString(to));
            }
        }
        return sql.toString();
    }
}
