package org.dangcat.persistence.model;

import org.dangcat.commons.formator.DateFormator;
import org.dangcat.commons.formator.DateType;
import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.orderby.OrderBy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * ���ݱ������ߡ�
 * @author dangcat
 * 
 */
public class TableStatementHelper
{
    private static final String MACROS_DATE_BEGIN = "{ts '";
    private static final String MACROS_DATE_END = "'}";
    private static final String MACROS_TEXT = "'";
    private static final String MACROS_TEXT_REPLACE = "''";
    private static final String NULL = "NULL";

    /**
     * ���������䡣
     * @param table ���ݱ����
     * @param filter ����������
     * @param tableName ��ʱ�����
     * @return �����䡣
     */
    public static String createAggregationSql(Table table, FilterExpress filter, String tableName)
    {
        return createAggregationSql(table, filter, tableName, false, false);
    }

    /**
     * ���������䡣
     * @param table ���ݱ����
     * @param filter ����������
     * @param tableName ��ʱ�����
     * @param includeOrderby ��ʱ�����
     * @param includeTopN ��ʱ�����
     * @return �����䡣
     */
    public static String createAggregationSql(Table table, FilterExpress filter, String tableName, boolean includeOrderby, boolean includeTopN)
    {
        StringBuilder sql = new StringBuilder();
        Column[] primaryKeys = table.getColumns().getPrimaryKeys();

        // ����
        for (Column column : primaryKeys)
        {
            if (sql.length() == 0)
                sql.append("SELECT ");
            else
                sql.append(", ");
            sql.append(column.getFieldName());
            if (column.getName().equalsIgnoreCase(column.getFieldName()))
            {
                sql.append(" ");
                sql.append(column.getName());
            }
        }

        // ������λ
        for (Column column : table.getColumns())
        {
            if (!column.isPrimaryKey())
            {
                sql.append(", ");
                String fieldName = column.getFieldName();
                // ͳ���ֶ�
                if (fieldName.substring(0, 3).equalsIgnoreCase("max"))
                    sql.append("MAX(" + fieldName + ")");
                else if (fieldName.substring(0, 3).equalsIgnoreCase("min"))
                    sql.append("MIN(" + fieldName + ")");
                else
                    sql.append("SUM(" + fieldName + ")");
                sql.append(" AS " + fieldName);
            }
        }

        if (sql.length() > 0)
        {
            sql.append(Environment.LINE_SEPARATOR + "FROM " + tableName);
            if (filter != null)
            {
                String sqlFilter = filter.toString();
                if (sqlFilter != null && !sqlFilter.equals(""))
                    sql.append(Environment.LINE_SEPARATOR + "WHERE 1=1 AND " + sqlFilter);
            }

            boolean hasCreateGroupBy = false;
            for (Column column : primaryKeys)
            {
                if (!hasCreateGroupBy)
                    sql.append(Environment.LINE_SEPARATOR + "GROUP BY ");
                else
                    sql.append(", ");
                sql.append(column.getFieldName());
                hasCreateGroupBy = true;
            }

            // �����Ӿ�
            if (includeOrderby)
            {
                OrderBy orderBy = table.getOrderBy();
                sql.append(Environment.LINE_SEPARATOR + orderBy.toString());
            }

            // ��ѯ������Ҫ�ָ�TOPN
            if (includeTopN)
            {
                Range range = table.getRange();
                if (range != null)
                {
                    if (range.getFrom() == 0)
                        sql.append(Environment.LINE_SEPARATOR + "LIMIT " + range.getTo());
                    else
                        sql.append(Environment.LINE_SEPARATOR + "LIMIT " + (range.getFrom() - 1) + "," + range.getPageSize());
                }
            }
        }
        return sql.toString();
    }

    /**
     * �������������У��ҵ���Ӧ����ϸ�����С�
     * @param masterRow ���������С�
     * @param detailTable ��ϸ�����
     * @return �ҵ��������м��ϡ�
     */
    public static Collection<Row> find(Row masterRow, Table detailTable)
    {
        Table masterTable = masterRow.getParent();
        if (masterTable == null)
            return null;
        // ��ǰ�����λ���ϡ�
        Columns columns = detailTable.getColumns();
        // �����������ơ�
        List<String> primaryKeys = new ArrayList<String>();
        // ����ֵ��
        List<Object> params = new ArrayList<Object>();
        for (Column column : masterTable.getColumns().getPrimaryKeys())
        {
            // �����ǰ��������������������
            if (columns.find(column.getName()) == null)
                continue;
            // ��������Ͷ�Ӧֵ��
            Field field = masterRow.getField(column.getName());
            primaryKeys.add(column.getName());
            params.add(field.getObject());
        }
        return detailTable.getRows().find(primaryKeys.toArray(new String[0]), params.toArray());
    }

    /**
     * ���ַ�����ȡ��󳤶ȡ�
     * @param value ת��ֵ��
     * @param maxLength ��󳤶ȡ�
     * @return ���ʽ��
     */
    public static Object subString(Object value, int maxLength)
    {
        if (value != null && maxLength > 0)
        {
            Class<?> classType = value.getClass();
            if (classType.equals(String.class) || classType.equals(Character.class) || classType.equals(byte.class) || classType.equals(byte[].class))
            {
                String strValue = value.toString();
                // ȡ��󳤶ȡ�
                if (strValue.length() > maxLength)
                    value = strValue.substring(0, maxLength - 1);
            }
        }
        return value;
    }

    /**
     * �ѵ�ǰֵת��SQL���ʽ��
     * @param value ת��ֵ��
     * @return ���ʽ��
     */
    public static String toSqlString(Object value)
    {
        if (value != null)
        {
            Class<?> fieldClass = value.getClass();
            if (ValueUtils.isText(fieldClass))
            {
                String sqlValue = value.toString();
                if (sqlValue.contains(MACROS_TEXT))
                    sqlValue = sqlValue.replace(MACROS_TEXT, MACROS_TEXT_REPLACE);
                return MACROS_TEXT + sqlValue + MACROS_TEXT;
            }
            else if (Date.class.isAssignableFrom(fieldClass))
            {
                SimpleDateFormat simpleDateFormat = DateFormator.getDateFormat(DateType.Full);
                return MACROS_DATE_BEGIN + simpleDateFormat.format(value) + MACROS_DATE_END;
            }

            return value.toString();
        }
        return NULL;
    }
}
