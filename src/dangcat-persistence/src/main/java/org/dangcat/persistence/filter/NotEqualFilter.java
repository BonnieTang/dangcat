package org.dangcat.persistence.filter;

import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.model.TableStatementHelper;

import java.util.ArrayList;
import java.util.List;

class NotEqualFilter implements Filter
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
        if (values != null && values.length > 0)
        {
            for (Object srcObject : values)
            {
                if (ValueUtils.compare(srcObject, value) == 0)
                    return false;
            }
        }
        return true;
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
        List<String> valuesList = new ArrayList<String>();
        for (Object valueObject : values)
        {
            if (valueObject != null)
                valuesList.add(TableStatementHelper.toSqlString(valueObject));
        }
        if (valuesList.size() > 1)
        {
            StringBuilder sql = new StringBuilder();
            for (String value : valuesList)
            {
                if (sql.length() == 0)
                {
                    sql.append(fieldName);
                    sql.append(" NOT IN (");
                }
                else
                    sql.append(", ");
                sql.append(value);
            }
            sql.append(")");
            return sql.toString();
        }
        else if (valuesList.size() == 1)
            return fieldName + " != " + valuesList.get(0);
        return null;
    }

}
