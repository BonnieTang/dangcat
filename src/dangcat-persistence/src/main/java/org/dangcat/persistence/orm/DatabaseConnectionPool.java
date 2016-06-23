package org.dangcat.persistence.orm;

import org.dangcat.commons.database.DatabaseType;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.pool.ConnectionPool;
import org.dangcat.framework.pool.SessionException;
import org.dangcat.framework.service.impl.PropertiesManager;
import org.dangcat.persistence.syntax.SqlSyntaxHelperFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * ���ӳء�
 *
 * @author dangcat
 */
public class DatabaseConnectionPool extends ConnectionPool<Connection> {
    /**
     * �����ֶι���
     */
    private AutoIncrementManager autoIncrementManager = null;
    /**
     * ������Ĵ�С��
     */
    private int batchSize = 0;
    /**
     * ���ݿ����͡�
     */
    private DatabaseType databaseType = null;
    /**
     * ����Դ��
     */
    private DataSource dataSource;
    /**
     * �﷨��������
     */
    private SqlSyntaxHelper sqlSyntaxHelper = null;

    public DatabaseConnectionPool(String name, DataSource dataSource, Map<String, String> params) {
        this(name, params);
        this.dataSource = dataSource;
    }

    public DatabaseConnectionPool(String name, Map<String, String> params) {
        super(name, params);
        this.autoIncrementManager = new AutoIncrementManager(name);
    }

    @Override
    public void close() {
        super.close();
        AutoIncrementManager.reset(this.getName(), null);
    }

    /**
     * �ر�����
     */
    @Override
    protected void close(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            this.logger.error(this, e);
        }
    }

    @Override
    protected Connection create() {
        Connection connection = null;
        try {
            if (this.dataSource != null)
                connection = this.dataSource.getConnection();
            if (connection == null) {
                String url = this.getProperty(DatabaseParams.Url);
                String user = this.getProperty(DatabaseParams.User);
                String password = this.getProperty(DatabaseParams.Password);
                password = this.decryptPassWord(password);
                connection = DriverManager.getConnection(url, user, password);
                this.initialize(connection);
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (SQLException e) {
            this.logger.error(this, e);
        } catch (SessionException e) {
            this.logger.error(this, e);
        }
        return connection;
    }

    private void createDatabaseType() {
        Connection connection = this.create();
        if (connection != null) {
            DatabaseType databaseType = DatabaseType.Default;
            try {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                String databaseProductName = databaseMetaData.getDatabaseProductName();
                String driverName = databaseMetaData.getDriverName();
                String value = databaseProductName + " " + driverName;
                DatabaseType dbType = DatabaseType.parse(value);
                if (dbType == null)
                    this.logger.error(value + " is not surpport.");
                else
                    databaseType = dbType;
            } catch (SQLException e) {
                this.logger.error(this, e);
            } finally {
                this.release(connection);
            }
            this.databaseType = databaseType;
        }
    }

    public AutoIncrementManager getAutoIncrementManager() {
        return this.autoIncrementManager;
    }

    /**
     * ������Ĵ�С��
     */
    public int getBatchSize() {
        if (this.batchSize == 0)
            this.batchSize = this.getParamAsInt(DatabaseParams.BatchSize, 1000);
        return this.batchSize;
    }

    /**
     * ���ݿ����͡�
     */
    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }

    private String getProperty(String name) {
        String value = this.getParams().get(name);
        if (!ValueUtils.isEmpty(value))
            value = PropertiesManager.getInstance().getValue(value);
        return value;
    }

    public SqlSyntaxHelper getSqlSyntaxHelper() {
        if (this.sqlSyntaxHelper == null)
            this.sqlSyntaxHelper = SqlSyntaxHelperFactory.getInstance().getSqlSyntaxHelper(this.getDatabaseType());
        return this.sqlSyntaxHelper;
    }

    /**
     * ��ʼ�����ӳء�
     *
     * @throws SessionException
     */
    @Override
    public void initialize() throws SessionException {
        try {
            if (this.dataSource == null) {
                Object instance = this.loadDriverInstance();
                if (this.getDatabaseType() == null)
                    this.createDatabaseType();
                this.getSqlSyntaxHelper().createDefaultParams(this.getParams());
                this.initialize(instance);
                if (instance instanceof DataSource)
                    this.dataSource = (DataSource) instance;
            } else if (this.getDatabaseType() == null)
                this.createDatabaseType();
        } catch (SessionException e) {
            throw e;
        } catch (Exception e) {
            throw new SessionException(e);
        }
    }

    @Override
    public boolean isPoolEnabled() {
        return this.getParamAsBoolean(DatabaseParams.UseConnectionPool, true);
    }

    /**
     * ʹ���ⲿ���
     */
    public boolean isUseExtendTransaction() {
        return this.getParamAsBoolean(DatabaseParams.UseExtendTransaction, false);
    }

    private Object loadDriverInstance() throws Exception {
        Object instance = null;
        for (String propertyKey : this.getParams().keySet()) {
            if (propertyKey.equalsIgnoreCase(DatabaseParams.Driver)) {
                String propertyValue = this.getProperty(propertyKey);
                if (!ValueUtils.isEmpty(propertyValue)) {
                    Class<?> driverClass = Class.forName(propertyValue);
                    if (driverClass != null) {
                        instance = driverClass.newInstance();
                        if (this.databaseType == null)
                            this.databaseType = DatabaseType.parse(propertyValue);
                    } else
                        this.logger.error("The database driver " + propertyValue + "is not found.");
                }
                break;
            }
        }
        return instance;
    }

    /**
     * �������ӳ��е�һ�����ݿ����ӡ�
     */
    @Override
    public Connection poll() {
        Connection connection = super.poll();
        if (connection != null) {
            if (!this.isUseExtendTransaction()) {
                int status = 0;
                while (status != 1) {
                    try {
                        connection.setAutoCommit(this.getParamAsBoolean(DatabaseParams.AutoCommit, true));
                        status = 1;
                    } catch (SQLException e) {
                        if (status == 0 && this.isCommunicationsException(e)) {
                            this.destroy(connection);
                            this.closePooled();
                            connection = super.poll();
                            status = -1;
                        } else
                            status = 1;

                        if (this.logger.isDebugEnabled())
                            this.logger.error(this, e);
                        else
                            this.logger.error(e);
                    }
                }
            }
        }
        return connection;
    }
}
