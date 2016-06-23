package org.dangcat.persistence.syntax;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Range;
import org.dangcat.persistence.orm.TableSqlBuilder;

/**
 * ���ʽ��������
 * @author dangcat
 * 
 */
public class OracleSyntaxAdapter extends SqlSyntaxHelperBase
{
    private static final String BLOB = "BLOB";
    private static final String CLOB = "CLOB";
    private static final String DATE = "DATE";
    private static final String INT = "INT";
    private static final String NUMERIC = "NUMERIC";
    private static final String SMALLINT = "SMALLINT";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String VARCHAR2 = "VARCHAR2";

    protected Map<String, String> defaultParams = new HashMap<String, String>();

    /**
     * ��������ڱ��ʽ��
     * @param databaseName ���ݿ�����
     * @param tableName ������
     * @return ��ѯ��䡣
     */
    @Override
    public String buildExistsStatement(String databaseName, String tableName)
    {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT 1 FROM USER_TABLES WHERE TABLE_NAME IN (UPPER('");
        sqlBuilder.append(tableName);
        sqlBuilder.append("'), LOWER('");
        sqlBuilder.append(tableName);
        sqlBuilder.append("'))");
        return sqlBuilder.toString();
    }

    /**
     * �������з�Χ����������ʽ��
     * @param sql ��ѯ��䡣
     * @param range ��ѯ��Χ��
     * @return ����ı����䡣
     */
    @Override
    public String buildRangeLoadStatement(String sql, Range range)
    {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT A.*, ROWNUM RN ");
        sqlBuilder.append(TableSqlBuilder.SEPERATE_LINE);
        sqlBuilder.append("FROM (");
        sqlBuilder.append(sql);
        sqlBuilder.append(") A ");
        sqlBuilder.append(TableSqlBuilder.SEPERATE_LINE);
        sqlBuilder.append("WHERE ROWNUM <= ");
        sqlBuilder.append(range.getTo());
        if (range.getFrom() > 1)
        {
            sqlBuilder.insert(0, "SELECT * FROM (");
            sqlBuilder.append(") WHERE RN >= ");
            sqlBuilder.append(range.getFrom());
        }
        range.setMode(Range.BY_SQLSYNTAX);
        return sqlBuilder.toString();
    }

    /**
     * ת��SQL��λ�������͡�
     * @param column ��λ����
     * @return SQL�������͡�
     */
    @Override
    public String getSqlType(Column column)
    {
        String sqlDataType = "";
        int displaySize = column.getDisplaySize();
        if (String.class.equals(column.getFieldClass()))
            sqlDataType = VARCHAR2 + "(" + displaySize + ")";
        else if (Byte.class.equals(column.getFieldClass()) || byte.class.equals(column.getFieldClass()) || Short.class.equals(column.getFieldClass()) || short.class.equals(column.getFieldClass())
                || Boolean.class.equals(column.getFieldClass()) || boolean.class.equals(column.getFieldClass()))
            sqlDataType = SMALLINT;
        else if (Integer.class.equals(column.getFieldClass()) || int.class.equals(column.getFieldClass()) || Long.class.equals(column.getFieldClass()) || long.class.equals(column.getFieldClass()))
            sqlDataType = INT;
        else if (Date.class.equals(column.getFieldClass()))
            sqlDataType = DATE;
        else if (Timestamp.class.equals(column.getFieldClass()))
            sqlDataType = TIMESTAMP;
        else if (Double.class.equals(column.getFieldClass()) || double.class.equals(column.getFieldClass()))
        {
            int scale = column.getScale();
            if (scale == 0)
                sqlDataType = NUMERIC + "(" + (displaySize - 1) + ")";
            else
                sqlDataType = NUMERIC + "(" + (displaySize - scale) + ", " + scale + ")";
        }
        else if (Byte[].class.equals(column.getFieldClass()) || byte[].class.equals(column.getFieldClass()))
            sqlDataType = BLOB;
        else if (Character[].class.equals(column.getFieldClass()) || char[].class.equals(column.getFieldClass()))
            sqlDataType = CLOB;
        return sqlDataType;
    }
}
