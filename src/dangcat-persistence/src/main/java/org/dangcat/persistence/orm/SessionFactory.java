package org.dangcat.persistence.orm;

import java.util.Map;

import javax.sql.DataSource;

import org.dangcat.commons.database.DatabaseType;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.pool.ConnectionFactory;
import org.dangcat.framework.pool.SessionException;

/**
 * �Ự������
 * @author dangcat
 * 
 */
public class SessionFactory extends ConnectionFactory<DatabaseConnectionPool, Session>
{
    private static SessionFactory instance = null;
    public static final String RESOURCETYPE = "database";

    /**
     * ���õ���ģʽ��ȡ�Ự����ʵ����
     */
    public static SessionFactory getInstance()
    {
        if (instance == null)
        {
            synchronized (SessionFactory.class)
            {
                try
                {
                    SessionFactory instance = new SessionFactory();
                    instance.initialize();
                    SessionFactory.instance = instance;
                }
                catch (SessionException e)
                {
                    logger.error("Create database connection error!", e);
                }
            }
        }
        return instance;
    }

    /**
     * ˽�й��캯����
     */
    private SessionFactory()
    {
    }

    /**
     * ���Ĭ�ϵ�����Դ��
     * @param dataSource ����Դ����
     */
    public void addDataSource(DataSource dataSource)
    {
        this.addDataSource(DEFAULT, dataSource, null);
    }

    /**
     * ����µ�����Դ��
     * @param databaseName ���ݿ������
     * @param dataSource ����Դ����
     */
    public void addDataSource(String databaseName, DataSource dataSource)
    {
        this.addDataSource(databaseName, dataSource, null);
    }

    /**
     * ����µ�����Դ��
     * @param databaseName ���ݿ������
     * @param dataSource ����Դ����
     * @param databaseConfig ���ݿ����á�
     */
    public void addDataSource(String databaseName, DataSource dataSource, Map<String, String> databaseParams)
    {
        if (dataSource != null && !ValueUtils.isEmpty(databaseName))
        {
            if (databaseName != null)
                databaseName = databaseName.toLowerCase();
            DatabaseConnectionPool databaseConnectionPool = new DatabaseConnectionPool(databaseName, dataSource, databaseParams);
            try
            {
                databaseConnectionPool.initialize();
            }
            catch (SessionException e)
            {
                logger.error(this, e);
            }
            this.put(databaseName, databaseConnectionPool);
        }
    }

    @Override
    protected void close(DatabaseConnectionPool connectionPool)
    {
        connectionPool.close();
    }

    @Override
    protected DatabaseConnectionPool createConnectionPool(String name, Map<String, String> params) throws SessionException
    {
        DatabaseConnectionPool databaseConnectionPool = new DatabaseConnectionPool(name, params);
        databaseConnectionPool.initialize();
        return databaseConnectionPool;
    }

    @Override
    protected Session createSession(DatabaseConnectionPool connectionPool)
    {
        return new Session(connectionPool);
    }

    /**
     * �õ�ָ������Դ���������͡�
     * @param databaseName ���ݿ����ơ�
     * @return �������͡�
     */
    public DatabaseType getDatabaseType(String databaseName)
    {
        return this.get(databaseName).getDatabaseType();
    }

    public String getDefaultDatabase()
    {
        return this.get(DEFAULT).getName();
    }

    @Override
    public String getResourceType()
    {
        return RESOURCETYPE;
    }

    /**
     * ��ȡ�﷨�������ߡ�
     * @param databaseName ���ݿ⡣
     * @return
     */
    public SqlSyntaxHelper getSqlSyntaxHelper(String databaseName)
    {
        return this.get(databaseName).getSqlSyntaxHelper();
    }
}
