package org.dangcat.persistence.syntax;

import org.dangcat.persistence.model.Column;

/**
 * ���ʽ��������
 * @author dangcat
 * 
 */
public class SqlServerSyntaxAdapter extends StandSqlSyntaxHelper
{
    private static final String BIT = "BIT";
    private static final String IMAGE = "IMAGE";
    private static final String TEXT = "TEXT";

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
        sqlBuilder.append("SELECT 1 FROM sysobjects WHERE TYPE = 'U' AND id = OBJECT_ID('");
        sqlBuilder.append(tableName);
        sqlBuilder.append("')");
        return sqlBuilder.toString();
    }

    @Override
    protected void createIdentityColumnSql(Column column, StringBuilder sqlFields)
    {
        sqlFields.append(" ");
        sqlFields.append("IDENTITY(1, 1)");
    }

    /**
     * ת��SQL��λ�������͡�
     * @param column ��λ����
     * @return SQL�������͡�
     */
    @Override
    public String getSqlType(Column column)
    {
        String sqlDataType = super.getSqlType(column);
        if (Boolean.class.equals(column.getFieldClass()) || boolean.class.equals(column.getFieldClass()))
            sqlDataType = BIT;
        else if (char[].class.equals(column.getFieldClass()) || Character[].class.equals(column.getFieldClass()))
            sqlDataType = TEXT;
        else if (byte[].class.equals(column.getFieldClass()) || Byte[].class.equals(column.getFieldClass()))
            sqlDataType = IMAGE;

        return sqlDataType;
    }
}
