package org.dangcat.persistence.model;

import org.dangcat.commons.utils.DateUtils;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.event.TableEventAdapter;
import org.dangcat.persistence.exception.TableException;
import org.dangcat.persistence.orm.BatchExecutHelper;
import org.dangcat.persistence.orm.Session;
import org.dangcat.persistence.orm.SqlBuilder;
import org.dangcat.persistence.orm.TableSqlBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * ���������
 * @author dangcat
 * 
 */
public class TableManagerImpl extends TableManagerBase implements TableManager
{
    /**
     * �ڵ�ǰ������Դ�й������ݱ�
     * @param table �����
     * @return ��������
     * @throws TableException �����쳣��
     */
    public int create(Table table) throws TableException
    {
        int result = 0;
        SqlBuilder sqlBuilder = null;
        Session session = null;
        try
        {
            long beginTime = DateUtils.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug("Begin create :");

            // ��������ǰ�¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
            {
                if (!tableEventAdapter.beforeCreate(table))
                    return result;
            }

            // ������
            TableSqlBuilder tableSqlBuilder = new TableSqlBuilder(table, this.getDatabaseName(table));
            sqlBuilder = tableSqlBuilder.buildCreateStatement();

            // ��ȡ�Ự����
            session = this.openSession(this.getDatabaseName(table));
            session.beginTransaction();
            result = this.executeBatch(session, table, sqlBuilder.getBatchSqlList());
            session.commit();
            result = 1;

            // �����¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.afterCreate(table);

            if (logger.isDebugEnabled())
                logger.debug("End create, cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
        }
        catch (Exception e)
        {
            if (session != null)
                session.rollback();

            String message = sqlBuilder == null ? e.toString() : sqlBuilder.toString();
            if (logger.isDebugEnabled())
                logger.error(message, e);

            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.onCreateError(table, e);

            throw new TableException(message, e);
        }
        finally
        {
            if (session != null)
                session.release();
        }
        return result;
    }

    /**
     * ɾ��ָ��������ݡ�
     * @param table �����
     * @return ɾ��������
     * @throws TableException �����쳣��
     */
    public int delete(Table table) throws TableException
    {
        int result = 0;
        SqlBuilder sqlBuilder = null;
        Session session = null;
        try
        {
            long beginTime = DateUtils.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug("Begin delete table: " + table.getTableName().getName());

            // ɾ��ǰ�¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
            {
                if (!tableEventAdapter.beforeDelete(table))
                    return result;
            }

            TableSqlBuilder tableSqlBuilder = new TableSqlBuilder(table, this.getDatabaseName(table));
            sqlBuilder = tableSqlBuilder.buildDeleteStatement();

            // ��ȡ�Ự����
            session = this.openSession(this.getDatabaseName(table));
            session.beginTransaction();
            result = this.executeBatch(session, table, sqlBuilder.getBatchSqlList());
            session.commit();

            // ɾ�����¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.afterDelete(table);

            if (logger.isDebugEnabled())
                logger.debug("End delete table, cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
        }
        catch (Exception e)
        {
            if (session != null)
                session.rollback();

            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.onDeleteError(table, e);

            String message = sqlBuilder == null ? e.toString() : sqlBuilder.toString();
            if (logger.isDebugEnabled())
                logger.error(message, e);
            throw new TableException(message, e);
        }
        finally
        {
            if (session != null)
                session.release();
        }
        return result;
    }

    /**
     * ɾ��ָ���ı�
     * @param tableName ������
     * @return ɾ�������
     * @throws TableException �����쳣��
     */
    public int drop(String tableName) throws TableException
    {
        return this.drop(new Table(tableName));
    }

    /**
     * ɾ��ָ���ı�
     * @param table �����
     * @return ɾ�������
     * @throws TableException �����쳣��
     */
    public int drop(Table table) throws TableException
    {
        int result = 0;
        SqlBuilder sqlBuilder = null;
        Session session = null;
        try
        {
            long beginTime = DateUtils.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug("Begin drop :");

            // ɾ����¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
            {
                if (!tableEventAdapter.beforeDrop(table))
                    return result;
            }

            TableSqlBuilder tableSqlBuilder = new TableSqlBuilder(table, this.getDatabaseName(table));
            sqlBuilder = tableSqlBuilder.buildDropStatement();

            // ��ȡ�Ự����
            session = this.openSession(this.getDatabaseName(table));
            result = this.executeBatch(session, table, sqlBuilder.getBatchSqlList());

            // ɾ��ǰ�¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.afterDrop(table);

            if (logger.isDebugEnabled())
                logger.debug("End drop, cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
        }
        catch (Exception e)
        {
            String message = sqlBuilder == null ? e.toString() : sqlBuilder.toString();
            if (logger.isDebugEnabled())
                logger.error(message, e);

            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.onDropError(table, e);

            throw new TableException(message, e);
        }
        finally
        {
            if (session != null)
                session.release();
        }
        return result;
    }

    /**
     * ִ�б��SQL��䡣
     * @param table �����
     * @param name SQL������
     * @return ִ�н����
     * @throws TableException �����쳣��
     */
    public int execute(Table table) throws TableException
    {
        int result = 0;
        SqlBuilder sqlBuilder = null;
        Session session = null;
        try
        {
            TableSqlBuilder tableSqlBuilder = new TableSqlBuilder(table, this.getDatabaseName(table));
            sqlBuilder = tableSqlBuilder.buildExecuteStatement();
            List<String> sqlBatchList = sqlBuilder.getBatchSqlList();
            if (sqlBatchList.size() == 0)
                return result;

            long beginTime = DateUtils.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug("Begin execute :");

            // ִ��ǰ�¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
            {
                if (!tableEventAdapter.beforeExecute(table))
                    return result;
            }

            // ��ȡ�Ự����
            session = this.openSession(this.getDatabaseName(table));
            session.beginTransaction();
            this.executeBatch(session, table, sqlBatchList);
            session.commit();

            // ִ�к��¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.afterExecute(table);

            if (logger.isDebugEnabled())
                logger.debug("End execute, cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
        }
        catch (Exception e)
        {
            if (session != null)
                session.rollback();

            String message = sqlBuilder == null ? e.toString() : sqlBuilder.toString();
            if (logger.isDebugEnabled())
                logger.error(message, e);

            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.onExecuteError(table, e);

            throw new TableException(message, e);
        }
        finally
        {
            if (session != null)
                session.release();
        }
        return result;
    }

    private int executeBatch(Session session, Table table, List<String> sqlBatchList) throws SQLException
    {
        int result = 0;
        if (BatchExecutHelper.isContainsParams(sqlBatchList))
            result = session.executeBatch(new TableDataReader(table), sqlBatchList);
        else
        {
            if (sqlBatchList.size() > 1)
                result = session.executeBatch(sqlBatchList);
            else
                result = session.execute(sqlBatchList.get(0));
        }
        return result;
    }

    /**
     * �жϱ��Ƿ���ڡ�
     * @param tableName ������
     * @return �Ƿ���ڡ�
     */
    public boolean exists(String tableName)
    {
        return this.exists(new Table(tableName));
    }

    /**
     * �жϱ��Ƿ���ڡ�
     * @param table �����
     * @return �Ƿ���ڡ�
     */
    public boolean exists(Table table)
    {
        boolean result = true;
        Session session = null;
        try
        {
            // ��ȡ�Ự����
            session = this.openSession(this.getDatabaseName(table));
            result = TableUtils.exists(table, session);
        }
        catch (Exception e)
        {
            result = false;
        }
        finally
        {
            if (session != null)
                session.release();
        }
        return result;
    }

    /**
     * ���ݿ����ơ�
     */
    protected String getDatabaseName()
    {
        return null;
    }

    /**
     * ���ݿ����ơ�
     */
    private String getDatabaseName(Table table)
    {
        String databaseName = this.getDatabaseName();
        return ValueUtils.isEmpty(databaseName) ? table.getDatabaseName() : databaseName;
    }

    /**
     * ����ָ��������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    public void load(Table table) throws TableException
    {
        String databaseName = this.getDatabaseName(table);
        TableLoadManagerImpl tableLoadManager = new TableLoadManagerImpl(databaseName);
        tableLoadManager.load(table);
    }

    /**
     * ����Ԫ�������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    public void loadMetaData(Table table) throws TableException
    {
        String databaseName = this.getDatabaseName(table);
        TableLoadManagerImpl tableLoadManager = new TableLoadManagerImpl(databaseName);
        tableLoadManager.loadMetaData(table);
    }

    /**
     * �洢ָ��������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    public void save(Table table) throws TableException
    {
        String databaseName = this.getDatabaseName(table);
        TableSaveManagerImpl tableSaveManager = new TableSaveManagerImpl(databaseName);
        tableSaveManager.save(table);
    }

    /**
     * ���ָ�������ݡ�
     * @param tableName �����ơ�
     * @return ��������
     * @throws TableException �����쳣��
     */
    public int truncate(String tableName) throws TableException
    {
        return this.truncate(new Table(tableName));
    }

    /**
     * ���ָ�������ݡ�
     * @param table �����
     * @return ��������
     * @throws TableException �����쳣��
     */
    public int truncate(Table table) throws TableException
    {
        int result = 0;
        SqlBuilder sqlBuilder = null;
        Session session = null;
        try
        {
            long beginTime = DateUtils.currentTimeMillis();
            if (logger.isDebugEnabled())
                logger.debug("Begin truncate table: " + table.getTableName().getName());

            // ���ǰ�¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
            {
                if (!tableEventAdapter.beforeTruncate(table))
                    return result;
            }

            TableSqlBuilder tableSqlBuilder = new TableSqlBuilder(table, this.getDatabaseName(table));
            sqlBuilder = tableSqlBuilder.buildTruncateStatement();

            // ��ȡ�Ự����
            session = this.openSession(this.getDatabaseName(table));
            result = session.executeBatch(sqlBuilder.getBatchSqlList());

            // ������¼���
            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.afterTruncate(table);

            if (logger.isDebugEnabled())
                logger.debug("End truncate table, cost " + (DateUtils.currentTimeMillis() - beginTime) + " (ms)");
        }
        catch (Exception e)
        {
            String message = sqlBuilder == null ? e.toString() : sqlBuilder.toString();
            if (logger.isDebugEnabled())
                logger.error(message, e);

            for (TableEventAdapter tableEventAdapter : table.getTableEventAdapterList())
                tableEventAdapter.onTruncateError(table, e);

            throw new TableException(message, e);
        }
        finally
        {
            if (session != null)
                session.release();
        }
        return result;
    }
}
