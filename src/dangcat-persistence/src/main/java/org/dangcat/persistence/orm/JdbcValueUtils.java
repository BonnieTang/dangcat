package org.dangcat.persistence.orm;

import org.dangcat.persistence.model.Column;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class JdbcValueUtils {
    /**
     * ����Ԫ���ݶ����ֶ����͡�
     *
     * @param resultSetMetaData Ԫ���ݶ���
     * @param columnIndex       ��λ������
     * @return SQL�������͡�
     * @throws SQLException
     */
    public static Class<?> getFieldType(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        Class<?> fieldClass = null;
        int sqlType = resultSetMetaData.getColumnType(columnIndex);
        switch (sqlType) {
            case Types.NCLOB:
            case Types.CLOB:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                fieldClass = String.class;
                break;
            case Types.TINYINT:
                fieldClass = Byte.class;
                break;
            case Types.BIGINT:
                fieldClass = Long.class;
                break;
            case Types.SMALLINT:
                fieldClass = Short.class;
                break;
            case Types.INTEGER:
                fieldClass = Integer.class;
                break;
            case Types.NUMERIC:
                int scale = resultSetMetaData.getScale(columnIndex);
                if (scale == 0)
                    fieldClass = Integer.class;
                else
                    fieldClass = Double.class;
                break;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.DECIMAL:
                fieldClass = Double.class;
                break;
            case Types.BOOLEAN:
                fieldClass = Boolean.class;
                break;
            case Types.TIMESTAMP:
            case Types.DATE:
                fieldClass = Date.class;
                break;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
                fieldClass = byte[].class;
                break;
            case Types.BIT:
                fieldClass = Boolean.class;
                break;
        }
        return fieldClass;
    }

    /**
     * ������������ȡ��SQL���͡�
     *
     * @param fieldClass �ֶ����͡�
     * @return SQL�������͡�
     */
    public static int getSqlType(Class<?> fieldClass) {
        if (String.class.equals(fieldClass) || Character[].class.equals(fieldClass) || char[].class.equals(fieldClass))
            return Types.VARCHAR;
        else if (Byte.class.equals(fieldClass) || byte.class.equals(fieldClass))
            return Types.TINYINT;
        else if (Short.class.equals(fieldClass) || short.class.equals(fieldClass))
            return Types.SMALLINT;
        else if (Integer.class.equals(fieldClass) || int.class.equals(fieldClass))
            return Types.INTEGER;
        else if (Boolean.class.equals(fieldClass) || boolean.class.equals(fieldClass))
            return Types.BOOLEAN;
        else if (Long.class.equals(fieldClass) || long.class.equals(fieldClass))
            return Types.BIGINT;
        else if (Date.class.equals(fieldClass) || Timestamp.class.equals(fieldClass))
            return Types.TIMESTAMP;
        else if (Double.class.equals(fieldClass) || double.class.equals(fieldClass))
            return Types.NUMERIC;
        else if (Character[].class.equals(fieldClass) || char[].class.equals(fieldClass))
            return Types.CLOB;
        else if (Byte[].class.equals(fieldClass) || byte[].class.equals(fieldClass))
            return Types.BLOB;
        return Types.NULL;
    }

    /**
     * �����ݼ����������С�
     *
     * @param fieldName �ֶ�����
     * @param resultSet ԭʼ���ݼ��ϡ�
     * @throws SQLException �쳣����
     * @throws IOException
     */
    public static Object read(String fieldName, ResultSet resultSet, Class<?> targetClass) throws SQLException {
        return JdbcValueReader.read(fieldName, resultSet, targetClass);
    }

    /**
     * д����ʽֵ��
     *
     * @param preparedStatement ���ʽ����
     * @param parameterIndex    ������š�
     * @param value             ֵ����
     * @param column            ���ݿ����͡�
     * @throws SQLException �����쳣��
     */
    public static void write(PreparedStatement preparedStatement, int parameterIndex, Object value, Column column) throws SQLException {
        JdbcValueWriter.write(preparedStatement, parameterIndex, value, column);
    }
}
