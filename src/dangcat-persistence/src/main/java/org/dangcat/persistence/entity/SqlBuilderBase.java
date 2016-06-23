package org.dangcat.persistence.entity;

import org.dangcat.commons.database.DatabaseType;
import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.model.*;
import org.dangcat.persistence.orderby.OrderBy;
import org.dangcat.persistence.orm.SessionFactory;
import org.dangcat.persistence.orm.SqlBuilder;
import org.dangcat.persistence.orm.SqlSyntaxHelper;
import org.dangcat.persistence.sql.Sql;
import org.dangcat.persistence.tablename.DynamicTableUtils;
import org.dangcat.persistence.tablename.TableName;

import java.util.List;
import java.util.Map;

/**
 * ��乹��������
 * @author dangcat
 * 
 */
public abstract class SqlBuilderBase
{
    public static final String SEPERATE_LINE = Environment.LINE_SEPARATOR;
    private String databaseName;

    public SqlBuilderBase(String databaseName)
    {
        this.databaseName = databaseName;
    }

    /**
     * �����������ݱ�ı��ʽ��
     * @param table ���ݱ����
     * @return ����ı����䡣
     */
    public SqlBuilder buildCreateStatement()
    {
        SqlBuilder sqlBuilder = this.getSql(Sql.CREATE);
        if (sqlBuilder != null)
            return sqlBuilder;
        sqlBuilder = new SqlBuilder();
        Columns columns = this.getTable().getColumns();
        if (columns == null || columns.size() == 0)
            return sqlBuilder;

        SqlSyntaxHelper sqlSyntaxHelper = SessionFactory.getInstance().getSqlSyntaxHelper(this.getDatabaseName());
        sqlSyntaxHelper.buildCreateStatement(sqlBuilder, this);
        return this.replaceTableName(sqlBuilder);
    }

    /**
     * ����ɾ����ı��ʽ��
     * @return �洢�ı����䡣
     */
    public SqlBuilder buildDeleteStatement()
    {
        SqlBuilder sqlBuilder = this.getSql(Sql.DELETE);
        if (sqlBuilder != null)
            return sqlBuilder;
        sqlBuilder = new SqlBuilder();
        sqlBuilder.append("DELETE FROM ");
        sqlBuilder.append(this.getName());
        String sqlFilter = this.getFilterSql();
        if (!ValueUtils.isEmpty(sqlFilter))
        {
            sqlBuilder.append(SEPERATE_LINE);
            if (!ValueUtils.isEmpty(sqlFilter))
            {
                sqlBuilder.append("WHERE 1=1");
                sqlBuilder.append(sqlFilter);
            }
        }
        sqlBuilder.append(SEPERATE_LINE);
        return this.replaceTableName(sqlBuilder);
    }

    /**
     * ���������е�ɾ���ı��ʽ��
     * @return �洢�ı����䡣
     */
    public String buildDeleteStatement(List<String> fieldNameList)
    {
        // ��������
        Column[] keyColumns = this.getTable().getColumns().getPrimaryKeys();
        if (keyColumns == null || keyColumns.length == 0)
            return null;

        fieldNameList.clear();
        SqlBuilder sqlBuilder = new SqlBuilder();
        for (Column column : keyColumns)
        {
            if (fieldNameList.size() > 0)
                sqlBuilder.append("AND ");
            else
            {
                sqlBuilder.append("DELETE FROM ");
                sqlBuilder.append(this.getName());
                sqlBuilder.append(" WHERE ");
            }
            sqlBuilder.append(column.getFieldName() + " = ? ");
            fieldNameList.add(column.getName());
        }
        return sqlBuilder.toString();
    }

    /**
     * ����ɾ�����ݱ�ı��ʽ��
     * @param table ���ݱ����
     * @return ɾ���ı����䡣
     */
    public SqlBuilder buildDropStatement()
    {
        SqlBuilder sqlBuilder = this.getSql(Sql.DROP);
        if (sqlBuilder != null)
            return sqlBuilder;
        sqlBuilder = new SqlBuilder();
        sqlBuilder.append("DROP TABLE ");
        sqlBuilder.append(this.getActualTableName());
        return this.replaceTableName(sqlBuilder);
    }

    /**
     * ����ִ�еı��ʽ��
     * @return �����䡣
     */
    public SqlBuilder buildExecuteStatement()
    {
        SqlBuilder sqlBuilder = this.getSql(Sql.EXECUTE);
        if (sqlBuilder != null)
            return sqlBuilder;
        return this.getSql(null);
    }

    /**
     * ��������ڲ�ѯ�����䡣
     * @return
     */
    public String buildExistsStatement()
    {
        String databaseName = this.getDatabaseName();
        String tableName = this.getActualTableName();
        SqlSyntaxHelper sqlSyntaxHelper = SessionFactory.getInstance().getSqlSyntaxHelper(databaseName);
        return sqlSyntaxHelper.buildExistsStatement(databaseName, tableName);
    }

    /**
     * �����������ʽ��
     * @param tableName ���ݱ�����
     * @param seq ������š�
     * @param orderBy ����ʽ��
     * @return ������䡣
     */
    public String buildIndexStatement(Integer seq, OrderBy orderBy)
    {
        String index = orderBy.toIndex();
        if (ValueUtils.isEmpty(index))
            return null;

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE INDEX IX_");
        sqlBuilder.append(this.getTableName().getName());
        sqlBuilder.append("_");
        sqlBuilder.append(seq);
        sqlBuilder.append(" ON ");
        sqlBuilder.append(this.getName());
        sqlBuilder.append("(");
        sqlBuilder.append(index);
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    /**
     * ���������еĲ���ı��ʽ��
     * @return �洢�ı����䡣
     */
    public String buildInsertStatement(List<String> fieldNameList)
    {
        fieldNameList.clear();
        // �ֶ����ݡ�
        StringBuilder sqlFields = new StringBuilder();
        // ֵ���ݡ�
        StringBuilder sqlValues = new StringBuilder();
        for (Column column : this.getTable().getColumns())
        {
            // �Զ������Ĳ���Ҫ���롣
            if (column.isIndentityGeneration() || column.isCalculate())
                continue;

            if (sqlFields.length() > 0)
            {
                sqlFields.append(", ");
                sqlValues.append(", ");
            }

            sqlFields.append(column.getFieldName());
            sqlValues.append("?");
            fieldNameList.add(column.getName());
        }
        // ���������ű���
        StringBuilder sqlBuilder = new StringBuilder();
        if (sqlFields.length() > 0)
        {
            sqlBuilder.append("INSERT INTO ");
            sqlBuilder.append(this.getName());
            sqlBuilder.append("(");
            sqlBuilder.append(sqlFields.toString());
            sqlBuilder.append(")");
            sqlBuilder.append(SEPERATE_LINE);
            sqlBuilder.append("VALUES(");
            sqlBuilder.append(sqlValues.toString());
            sqlBuilder.append(") ");
        }
        return sqlBuilder.toString();
    }

    /**
     * �������ݱ�����ı��ʽ��
     * @return ����ı����䡣
     */
    public abstract SqlBuilder buildLoadStatement();

    /**
     * �������ݱ�����ı��ʽ��
     * @return ����ı����䡣
     */
    protected SqlBuilder buildLoadStatement(SqlBuilder sqlBuilder, Range range)
    {
        String sqlExpress = sqlBuilder.toString();
        if (range != null && !ValueUtils.isEmpty(sqlExpress))
        {
            SqlSyntaxHelper sqlSyntaxHelper = SessionFactory.getInstance().getSqlSyntaxHelper(this.getDatabaseName());
            String sqlStatement = sqlSyntaxHelper.buildRangeLoadStatement(sqlExpress, range);
            sqlExpress = sqlStatement.replace(Range.TOP_SQLFLAG, "");
            sqlBuilder = new SqlBuilder(sqlExpress);
        }
        return this.replaceTableName(sqlBuilder);
    }

    /**
     * ������ѯ���ݱ��¼���ı��ʽ��
     * @return ����ı����䡣
     */
    public SqlBuilder buildTotalSizeStatement()
    {
        // ��ѯ��Χ
        SqlBuilder sqlBuilder = this.getSql(Sql.TOTALSIZE);
        if (sqlBuilder != null)
            return this.replaceTableName(sqlBuilder);
        sqlBuilder = this.getTable().getSql();
        if (sqlBuilder.length() == 0 && !ValueUtils.isEmpty(this.getName()))
        {
            sqlBuilder = new SqlBuilder();
            sqlBuilder.setTableName(this.getLoadTableName());
            sqlBuilder.setFilter(this.getFilterSql());
            sqlBuilder.setParams(this.getParams());

            sqlBuilder.append("SELECT COUNT(*) ");
            sqlBuilder.append(Range.TOTALSIZE);
            sqlBuilder.append(" FROM ");
            sqlBuilder.append(SqlBuilder.TABLENAME_FLAG);
            sqlBuilder.append(SEPERATE_LINE);
            sqlBuilder.append("WHERE 1=1 ");
            sqlBuilder.append(SqlBuilder.FILTER_FLAG);
            return this.replaceTableName(sqlBuilder);
        }

        sqlBuilder.setTableName(this.getName());
        sqlBuilder.setFilter(this.getFilterSql());
        sqlBuilder.append("SELECT COUNT(*) ");
        sqlBuilder.append(Range.TOTALSIZE);
        sqlBuilder.append(" FROM (");

        SqlBuilder result = new SqlBuilder();
        result.append(SEPERATE_LINE);
        result.append(sqlBuilder.toString());
        result.append(SEPERATE_LINE);
        result.append(") T");
        return this.replaceTableName(result);
    }

    /**
     * ���������ı��ʽ��
     * @return �洢�ı����䡣
     */
    public SqlBuilder buildTruncateStatement()
    {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.append("TRUNCATE TABLE ");
        sqlBuilder.append(this.getName());
        return this.replaceTableName(sqlBuilder);
    }

    /**
     * ���������е��޸ĵı��ʽ��
     * @return �洢�ı����䡣
     */
    public String buildUpdateStatement(Row row, List<String> fieldNameList, List<String> primaryKeyList)
    {
        // ��������
        Table table = this.getTable();
        Column[] keyColumns = table.getColumns().getPrimaryKeys();
        if (keyColumns == null || keyColumns.length == 0)
            return null;

        fieldNameList.clear();
        primaryKeyList.clear();

        // ���޸ĵ��ֶ�ֵ��
        StringBuilder sqlBuilder = new StringBuilder();
        for (Column column : table.getColumns())
        {
            if (column.isCalculate())
                continue;

            if (row == null || row.getField(column.getName()).getDataState() == DataState.Modified)
            {
                if (sqlBuilder.length() == 0)
                {
                    sqlBuilder.append("UPDATE ");
                    sqlBuilder.append(this.getName());
                    sqlBuilder.append(" SET ");
                }
                else
                    sqlBuilder.append(", ");

                sqlBuilder.append(column.getFieldName());
                sqlBuilder.append(" = ? ");
                fieldNameList.add(column.getName());
            }
        }
        // ����������
        if (sqlBuilder.length() > 0)
        {
            for (Column column : keyColumns)
            {
                if (primaryKeyList.size() == 0)
                    sqlBuilder.append(" WHERE ");
                else
                    sqlBuilder.append("AND ");
                sqlBuilder.append(column.getFieldName());
                sqlBuilder.append(" = ? ");
                primaryKeyList.add(column.getName());
            }
        }
        return sqlBuilder.toString();
    }

    protected String getActualTableName()
    {
        return DynamicTableUtils.getActualTableName(this.getTableName());
    }

    /**
     * ���ݿ�����
     */
    protected String getDatabaseName()
    {
        return this.databaseName;
    }

    protected DatabaseType getDatabaseType()
    {
        return SessionFactory.getInstance().getDatabaseType(this.getDatabaseName());
    }

    /**
     * ����������
     */
    protected abstract String getFilterSql();

    protected String getLoadTableName()
    {
        TableName tableName = this.getTableName();
        String loadTableName = DynamicTableUtils.getTableName(tableName);
        String alias = tableName.getAlias();
        if (!ValueUtils.isEmpty(alias) && !alias.equals(tableName.getName()))
            loadTableName += " " + alias;
        return loadTableName;
    }

    /**
     * ���ݱ�������
     */
    public String getName()
    {
        return DynamicTableUtils.getTableName(this.getTableName());
    }

    protected Map<String, Object> getParams()
    {
        return this.getTable().getParams();
    }

    protected SqlBuilder getSql(String name)
    {
        SqlBuilder sqlBuilder = null;
        Table table = this.getTable();
        // �Ե�ǰ����Ϊ��ѡ��
        if (!ValueUtils.isEmpty(this.getSqlName()))
            sqlBuilder = table.getSql(this.getDatabaseType(), this.getSqlName());
        if (sqlBuilder == null)
            sqlBuilder = table.getSql(this.getDatabaseType(), name);
        return sqlBuilder;
    }

    protected abstract String getSqlName();

    /**
     * ���ݱ��塣
     */
    public abstract Table getTable();

    /**
     * ���ݱ�������
     */
    protected abstract TableName getTableName();

    protected SqlBuilder replaceTableName(SqlBuilder sqlBuilder)
    {
        return DynamicTableUtils.replaceTableName(sqlBuilder, this.getTableName());
    }
}
