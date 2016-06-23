package org.dangcat.persistence.model;

import org.dangcat.commons.utils.DateUtils;
import org.dangcat.persistence.event.TableEventAdapter;
import org.dangcat.persistence.exception.TableException;
import org.dangcat.persistence.orm.JdbcValueUtils;
import org.dangcat.persistence.orm.Session;
import org.dangcat.persistence.orm.SqlBuilder;
import org.dangcat.persistence.orm.TableSqlBuilder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * ���������
 * @author dangcat
 * 
 */
class TableLoadManagerImpl extends TableManagerBase
{
    private String databaseName = null;

    TableLoadManagerImpl(String databaseName)
    {
        this.databaseName = databaseName;
    }

    /**
     * ����ָ��������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    protected void load(Table table) throws TableException
    {
        SqlBuilder sqlBuilder = null;
        Session session = null;
        try
        {
            long beginTime = DateUtils.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug("Begin load the table: " + table.getTableName().getName());

            // ����ǰ�¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
            {
                if (!tableEventAdapter.beforeLoad(table))
                    return;
            }

            TableSqlBuilder tableSqlBuilder = new TableSqlBuilder(table, this.databaseName);
            // �����¼������
            Range range = table.getRange();
            if (range != null && range.isCalculateTotalSize())
            {
                int totalSize = 0;
                sqlBuilder = tableSqlBuilder.buildTotalSizeStatement();
                if (sqlBuilder.length() > 0)
                {
                    // ��ȡ�Ự����
                    session = this.openSession(this.databaseName);
                    ResultSet resultSet = session.executeQuery(sqlBuilder.toString());
                    while (resultSet.next())
                        totalSize = resultSet.getInt(Range.TOTALSIZE);
                }
                range.setTotalSize(totalSize);
                if (totalSize == 0)
                {
                    table.getRows().clear();
                    return;
                }
            }
            // �������ݡ�
            sqlBuilder = tableSqlBuilder.buildLoadStatement();
            if (sqlBuilder.length() > 0)
            {
                // ��ȡ�Ự����
                if (session == null)
                    session = this.openSession(this.databaseName);
                ResultSet resultSet = session.executeQuery(sqlBuilder.toString());

                // �޸�����״̬��
                table.setTableState(TableState.Loading);

                // �����λû�й�����Ҫ�Զ����ɡ�
                if (table.getColumns().size() == 0)
                    session.loadMetaData(table, resultSet);

                // �������ݽ����
                this.parseData(table, resultSet);
            }

            // ������¼���
            table.calculate();
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.afterLoad(table);

            if (logger.isDebugEnabled())
                logger.debug("End load table, cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
        }
        catch (Exception e)
        {
            String message = sqlBuilder == null ? e.toString() : sqlBuilder.toString();
            if (logger.isDebugEnabled())
                logger.error(message, e);

            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.onLoadError(table, e);

            throw new TableException(message, e);
        }
        finally
        {
            table.setTableState(TableState.Normal);
            if (session != null)
                session.release();
        }
    }

    /**
     * ����Ԫ�������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    protected void loadMetaData(Table table) throws TableException
    {
        Session session = null;
        try
        {
            long beginTime = DateUtils.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug("Begin loadMetaData: " + table.getTableName().getName());

            // ��ȡ�Ự����
            session = this.openSession(this.databaseName);
            session.loadMetaData(table, null);

            // ����Ԫ�����¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.onLoadMetaData(table);

            if (logger.isDebugEnabled())
                logger.debug("End loadMetaData, cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
        }
        catch (Exception e)
        {
            throw new TableException(e);
        }
        finally
        {
            if (session != null)
                session.release();
        }
    }

    /**
     * �������ݽ����
     * @param row �����ж���
     * @param resultSet ��ѯ�����
     * @throws SQLException
     * @throws TableException
     * @throws TableException
     */
    private void parseData(Table table, ResultSet resultSet) throws SQLException, TableException
    {
        Columns columns = table.getColumns();
        Rows rows = table.getRows();
        rows.clear();
        int position = 0;
        Range range = table.getRange();
        // ��ȡԪ���ݡ�
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        while (resultSet.next())
        {
            position++;
            if (range != null && range.getMode() != Range.BY_SQLSYNTAX)
            {
                if (position < range.getFrom())
                    continue;
                else if (position > range.getTo())
                    break;
            }

            Row row = rows.createNewRow();
            for (int i = 1; i <= columnCount; i++)
            {
                String fieldName = resultSetMetaData.getColumnLabel(i);
                Column column = columns.findByFieldName(fieldName);
                if (column != null)
                {
                    Field field = row.getField(column.getName());
                    field.setObject(JdbcValueUtils.read(fieldName, resultSet, column.getFieldClass()));
                }
            }
            rows.add(row);
        }
    }
}
