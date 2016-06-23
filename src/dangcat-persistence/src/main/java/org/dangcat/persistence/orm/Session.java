package org.dangcat.persistence.orm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.DataReader;
import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Columns;
import org.dangcat.persistence.model.Table;

/**
 * �Ự����
 * @author dangcat
 * 
 */
public class Session
{
    /** ��������������� */
    protected int batchCount = 0;
    /** ���Ӷ��� */
    private Connection connection = null;
    /** ���ݿ����Ӷ��� */
    private DatabaseConnectionPool databaseConnectionPool;
    /** ��ѯ��� */
    protected ResultSet resultSet = null;
    /** ִ���� */
    private SessionExecutor sessionExecutor = new SessionExecutor(this);
    /** SQL�������� */
    private SqlProfile sqlProfile = new SqlProfile();
    /** ��ѯ���ʽ�� */
    protected Statement statement = null;
    /** ʹ������ */
    private boolean useTransaction = false;

    /**
     * ����Ự����
     * @param connection ���Ӷ���
     */
    public Session(DatabaseConnectionPool connectionPool)
    {
        this.databaseConnectionPool = connectionPool;
    }

    /**
     * ��������
     * @throws SQLException
     */
    public void beginTransaction() throws SQLException
    {
        if (!this.databaseConnectionPool.isUseExtendTransaction())
            this.getConnection().setAutoCommit(false);
        this.useTransaction = true;
    }

    /**
     * �ύ����
     * @throws SQLException
     */
    public void commit() throws SQLException
    {
        this.useTransaction = false;
        this.sqlProfile.end();
        Connection connection = this.getConnection();
        this.getAutoIncrementManager().update(connection);
        if (!this.databaseConnectionPool.isUseExtendTransaction())
            connection.commit();
    }

    public void destroy()
    {
        this.releaseStatement();
        this.databaseConnectionPool.destroy(this.connection);
        this.connection = null;
    }

    /**
     * ִ�����
     * @param sql �����䡣
     * @throws SQLException
     */
    public int execute(String sql) throws SQLException
    {
        if (ValueUtils.isEmpty(sql))
            return 0;
        if (!this.useTransaction)
        {
            this.sqlProfile.begin();
            this.sqlProfile.appendSql(sql);
        }
        SessionExecutor sessionExecutor = new SessionExecutor(this);
        int result = sessionExecutor.execute(SessionExecutor.EXECUTE_SQL, sql);
        if (!this.useTransaction)
            this.sqlProfile.end();
        return result;
    }

    /**
     * ������ԴΪ��������ִ��SQL���
     * @param dataReader ������Դ��
     * @param sqlBatchList ������䡣
     * @throws SQLException �����쳣��
     */
    public int executeBatch(DataReader dataReader, List<String> sqlBatchList) throws SQLException
    {
        if (!this.useTransaction)
        {
            this.sqlProfile.begin();
            this.sqlProfile.appendSql(sqlBatchList);
        }

        BatchExecutSession batchExecutSession = new BatchExecutSession(this, dataReader, sqlBatchList);
        try
        {
            SessionExecutor sessionExecutor = new SessionExecutor(this);
            batchExecutSession.prepare();
            for (int index = 0; index < dataReader.size(); index++)
            {
                boolean submit = this.batchCount >= this.databaseConnectionPool.getBatchSize();
                for (BatchExecutor batchExecutor : batchExecutSession.getBatchExecutorList())
                {
                    if (this.statement != null)
                    {
                        sessionExecutor.execute(SessionExecutor.EXECUTE_BATCHUPDATE, submit);
                        submit = false;
                    }
                    batchExecutor.prepare(index);
                    this.statement = batchExecutor.getPreparedStatement();
                }
                this.batchCount++;
            }
            if (this.batchCount > 0)
                sessionExecutor.execute(SessionExecutor.EXECUTE_BATCHUPDATE, true);
        }
        finally
        {
            batchExecutSession.release();
            this.statement = null;
            if (!this.useTransaction)
                this.sqlProfile.end();
        }
        return this.batchCount;
    }

    /**
     * ����ִ�д洢���
     * @param rows �����ж���
     * @throws SQLException
     */
    public int executeBatch(List<String> sqlBatchList) throws SQLException
    {
        if (!this.useTransaction)
        {
            this.sqlProfile.begin();
            this.sqlProfile.appendSql(sqlBatchList);
        }
        this.sessionExecutor.execute(SessionExecutor.EXECUTE_BATCH, sqlBatchList);
        if (!this.useTransaction)
            this.sqlProfile.end();
        return 1;
    }

    /**
     * ִ�и������
     * @param values ��������
     * @throws SQLException
     */
    public int executeBatchUpdate(boolean submit) throws SQLException
    {
        this.batchCount++;
        if (!submit)
            submit = this.batchCount >= this.databaseConnectionPool.getBatchSize();
        return this.sessionExecutor.execute(SessionExecutor.EXECUTE_BATCHUPDATE, submit);
    }

    /**
     * ִ�в�ѯ����
     * @param sql
     * @return ResultSet ��ѯ���
     * @throws SQLException
     */
    public ResultSet executeQuery(String sql) throws SQLException
    {
        if (!this.useTransaction)
        {
            this.sqlProfile.begin();
            this.sqlProfile.appendSql(sql);
        }
        this.sessionExecutor.execute(SessionExecutor.EXECUTE_QUERY, sql);
        if (!this.useTransaction)
            this.sqlProfile.end();
        return this.resultSet;
    }

    /**
     * ִ�и������
     * @param values ��������
     * @throws SQLException
     */
    public void executeUpdate() throws SQLException
    {
        this.sessionExecutor.execute(SessionExecutor.EXECUTE_UPDATE);
    }

    private AutoIncrementManager getAutoIncrementManager()
    {
        return this.databaseConnectionPool.getAutoIncrementManager();
    }

    public Connection getConnection()
    {
        if (this.connection == null)
            this.connection = this.databaseConnectionPool.poll();
        return this.connection;
    }

    protected DatabaseConnectionPool getDatabaseConnectionPool()
    {
        return this.databaseConnectionPool;
    }

    /**
     * ���ز���������ֵ����
     * @throws SQLException
     */
    public ResultSet getGeneratedKeys() throws SQLException
    {
        return this.statement.getGeneratedKeys();
    }

    public String getName()
    {
        return this.databaseConnectionPool.getName();
    }

    private ResultSet getResultSet(Table table, ResultSet resultSet) throws SQLException
    {
        ResultSet metaDataResultSet = resultSet;
        if (metaDataResultSet == null)
        {
            String sql = null;
            SqlBuilder sqlBuilder = table.getSql();
            if (sqlBuilder.length() == 0 && !ValueUtils.isEmpty(table.getTableName().getName()))
                sql = "SELECT * FROM " + table.getTableName().getName() + " WHERE 1=2 ";
            else
                sql = sqlBuilder.toString();
            if (!ValueUtils.isEmpty(sql))
                metaDataResultSet = this.executeQuery(sql);
        }
        return metaDataResultSet;
    }

    /**
     * ��ָ��������л�ԭԪ���ݡ�
     * @param table ���ݱ����
     * @param resultSet �������
     * @throws SQLException ִ���쳣��
     */
    public void loadMetaData(Table table, ResultSet resultSet) throws SQLException
    {
        ResultSet metaDataResultSet = this.getResultSet(table, resultSet);
        if (metaDataResultSet == null)
            return;
        // ��ȡ������Ϣ��
        Collection<String> primayKeys = this.loadPrimayKeyMetaData(table.getTableName().getName());
        // ��ȡԪ���ݡ�
        ResultSetMetaData resultSetMetaData = metaDataResultSet.getMetaData();
        // ��ԭ��λ����Ϣ��
        Columns columns = table.getColumns();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
        {
            String fieldName = resultSetMetaData.getColumnLabel(columnIndex);
            Class<?> fieldClass = JdbcValueUtils.getFieldType(resultSetMetaData, columnIndex);
            int displaySize = resultSetMetaData.getColumnDisplaySize(columnIndex);
            boolean isPrimaryKey = primayKeys.contains(fieldName);
            Column column = columns.find(fieldName);
            if (column == null)
                column = columns.add(fieldName, fieldClass, displaySize, isPrimaryKey);
            else
            {
                column.setFieldClass(fieldClass);
                column.setDisplaySize(displaySize);
                column.setPrimaryKey(isPrimaryKey);
            }
            column.setNullable(resultSetMetaData.isNullable(columnIndex) != ResultSetMetaData.columnNoNulls);
            column.setScale(resultSetMetaData.getScale(columnIndex));
            column.setAutoIncrement(resultSetMetaData.isAutoIncrement(columnIndex));
            if (column.getSqlType() == 0)
                column.setSqlType(resultSetMetaData.getColumnType(columnIndex));
        }
    }

    /**
     * ��������Ԫ���ݡ�
     * @param table ���ݱ����
     */
    private Collection<String> loadPrimayKeyMetaData(String tableName)
    {
        Collection<String> primayKeys = new HashSet<String>();
        try
        {
            DatabaseMetaData databaseMetaData = this.getConnection().getMetaData();
            ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (resultSet.next())
                primayKeys.add(resultSet.getString("COLUMN_NAME"));
            resultSet.close();
        }
        catch (Exception e)
        {
        }
        return primayKeys;
    }

    /**
     * ���ݱ����ֶ�����ȡ��һ���������С�
     * @param tableName ������
     * @param fieldName �ֶ�����
     * @param classType �ֶ����͡�
     * @param tableGenerator ��Ų��ԡ�
     * @return �������С�
     * @throws SQLException
     */
    public Object nextSequence(String tableName, String fieldName, Class<?> classType, TableGenerator tableGenerator) throws SQLException
    {
        return this.getAutoIncrementManager().nextSequence(this.getConnection(), tableName, fieldName, classType, tableGenerator);
    }

    /**
     * Ԥ��ѯ��
     * @param sql ��ѯ��䡣
     * @param fieldNames �����ֶ�����
     * @throws SQLException
     */
    public void prepare(String sql, String... fieldNames) throws SQLException
    {
        if (!this.useTransaction)
        {
            this.sqlProfile.begin();
            this.sqlProfile.appendSql(sql);
        }
        this.releaseStatement();
        if (fieldNames != null && fieldNames.length > 0)
            this.statement = this.getConnection().prepareStatement(sql, fieldNames);
        else
            this.statement = this.getConnection().prepareStatement(sql);
    }

    /**
     * �ͷŻỰ����
     */
    public void release()
    {
        this.releaseStatement();
        this.databaseConnectionPool.release(this.connection);
        this.connection = null;
        this.sqlProfile.end();
        this.useTransaction = false;
    }

    /**
     * �ͷŲ�ѯ�������
     */
    private void releaseStatement()
    {
        try
        {
            if (this.resultSet != null)
            {
                this.resultSet.close();
                this.resultSet = null;
            }
        }
        catch (SQLException e)
        {
        }

        try
        {
            if (this.statement != null)
            {
                this.statement.close();
                this.statement = null;
            }
        }
        catch (SQLException e)
        {
        }
    }

    /**
     * �ع�����
     */
    public void rollback()
    {
        this.sqlProfile.end();
        this.useTransaction = false;
        if (!this.databaseConnectionPool.isUseExtendTransaction())
        {
            try
            {
                this.getConnection().rollback();
            }
            catch (SQLException e)
            {
            }
        }
    }

    /**
     * ���ò�����
     * @param parameterIndex ��������λ�á�
     * @param value ����ֵ��
     * @param column ���ݿ����͡�
     * @throws SQLException
     */
    public void setParam(int parameterIndex, Object value, Column column) throws SQLException
    {
        JdbcValueUtils.write((PreparedStatement) this.statement, parameterIndex + 1, value, column);
    }
}
