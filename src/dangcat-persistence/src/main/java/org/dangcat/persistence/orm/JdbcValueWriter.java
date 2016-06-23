package org.dangcat.persistence.orm;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.dangcat.commons.utils.DateUtils;
import org.dangcat.persistence.model.Column;

class JdbcValueWriter
{

    /**
     * д����ʽֵ��
     * @param preparedStatement ���ʽ����
     * @param parameterIndex ������š�
     * @param value ֵ����
     * @param column ���ݿ����͡�
     * @throws SQLException �����쳣��
     */
    protected static void write(PreparedStatement preparedStatement, int parameterIndex, Object value, Column column) throws SQLException
    {
        int sqlType = column.getSqlType();
        if (value == null)
        {
            preparedStatement.setNull(parameterIndex, sqlType);
            return;
        }

        if (writeBlob(preparedStatement, parameterIndex, value))
            return;
        if (writeClob(preparedStatement, parameterIndex, value))
            return;
        if (writeDateTime(preparedStatement, parameterIndex, column, value))
            return;

        if (sqlType != Types.NULL)
        {
            switch (sqlType)
            {
                case Types.NCHAR:
                case Types.NVARCHAR:
                    if (value instanceof String)
                        preparedStatement.setNString(parameterIndex, (String) value);
                    else
                        preparedStatement.setNString(parameterIndex, value.toString());
                    break;
                case Types.LONGNVARCHAR:
                case Types.LONGVARCHAR:
                case Types.NCLOB:
                case Types.CLOB:
                    writeClob(preparedStatement, parameterIndex, value, sqlType);
                    break;
                case Types.LONGVARBINARY:
                case Types.BLOB:
                    writeBlob(preparedStatement, parameterIndex, value);
                    break;
                case Types.BIT:
                case Types.BOOLEAN:
                    if (value instanceof Boolean)
                        preparedStatement.setBoolean(parameterIndex, (Boolean) value);
                    else
                        preparedStatement.setObject(parameterIndex, value, sqlType);
                    break;
                default:
                    preparedStatement.setObject(parameterIndex, value, sqlType);
                    break;
            }
        }
        else
            preparedStatement.setObject(parameterIndex, value);
    }

    /**
     * д���ֽ������ݡ�
     * @param fieldName �ֶ�����
     * @param resultSet ������ϡ�
     * @param targetClass Ŀ�����͡�
     * @return ���������
     * @throws SQLException �����쳣��
     */
    private static boolean writeBlob(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException
    {
        if (value instanceof byte[])
        {
            byte[] byteValue = (byte[]) value;
            InputStream inputStream = new ByteArrayInputStream(byteValue);
            preparedStatement.setBinaryStream(parameterIndex, inputStream, byteValue.length);
            return true;
        }
        return false;
    }

    /**
     * д���ַ������ݡ�
     * @param fieldName �ֶ�����
     * @param resultSet ������ϡ�
     * @param targetClass Ŀ�����͡�
     * @return ���������
     * @throws SQLException �����쳣��
     */
    private static boolean writeClob(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException
    {
        if (value instanceof char[])
        {
            char[] charValue = (char[]) value;
            Reader reader = new CharArrayReader(charValue);
            preparedStatement.setCharacterStream(parameterIndex, reader, charValue.length);
            return true;
        }
        return false;
    }

    /**
     * д���ִ����͡�
     * @param fieldName �ֶ�����
     * @param resultSet ������ϡ�
     * @param targetClass Ŀ�����͡�
     * @return ���������
     * @throws SQLException �����쳣��
     */
    private static boolean writeClob(PreparedStatement preparedStatement, int parameterIndex, Object value, int sqlType) throws SQLException
    {
        Reader reader = null;
        long length = 0;
        if (value instanceof String)
        {
            String strValue = (String) value;
            reader = new StringReader(strValue);
            length = strValue.length();
        }
        else if (value instanceof char[])
        {
            char[] charValue = (char[]) value;
            reader = new CharArrayReader(charValue);
            length = charValue.length;
        }
        if (reader != null)
        {
            if (sqlType == Types.NCLOB || sqlType == Types.LONGNVARCHAR)
                preparedStatement.setNCharacterStream(parameterIndex, reader, length);
            else if (sqlType == Types.CLOB || sqlType == Types.LONGVARCHAR)
                preparedStatement.setCharacterStream(parameterIndex, reader, length);
            return true;
        }
        return false;
    }

    /**
     * д���ַ������ݡ�
     * @param fieldName �ֶ�����
     * @param resultSet ������ϡ�
     * @param targetClass Ŀ�����͡�
     * @return ���������
     * @throws SQLException �����쳣��
     */
    private static boolean writeDateTime(PreparedStatement preparedStatement, int parameterIndex, Column column, Object value) throws SQLException
    {
        if (value instanceof Date) // Sql Server��֧��java.util.Date
        {
            Date date = DateUtils.clear(column.getDateType(), (Date) value);
            preparedStatement.setTimestamp(parameterIndex, new java.sql.Timestamp(date.getTime()));
            return true;
        }
        return false;
    }
}
