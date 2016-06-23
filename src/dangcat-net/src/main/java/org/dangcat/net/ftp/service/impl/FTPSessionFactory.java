package org.dangcat.net.ftp.service.impl;

import org.dangcat.framework.pool.ConnectionFactory;
import org.dangcat.framework.pool.SessionException;

import java.util.Map;

public class FTPSessionFactory extends ConnectionFactory<FTPClientPool, FTPSession>
{
    public static final String RESOURCETYPE = "ftp";
    private static FTPSessionFactory instance = null;

    /**
     * ˽�й��캯����
     */
    private FTPSessionFactory() {
    }

    /**
     * ���õ���ģʽ��ȡ�Ự����ʵ����
     */
    public static FTPSessionFactory getInstance()
    {
        if (instance == null)
        {
            synchronized (FTPSessionFactory.class)
            {
                try
                {
                    FTPSessionFactory instance = new FTPSessionFactory();
                    instance.initialize();
                    FTPSessionFactory.instance = instance;
                }
                catch (SessionException e)
                {
                    logger.error("Create ftp connection error!", e);
                }
            }
        }
        return instance;
    }

    @Override
    protected void close(FTPClientPool connectionPool)
    {
        connectionPool.close();
    }

    @Override
    protected FTPClientPool createConnectionPool(String name, Map<String, String> params) throws SessionException
    {
        FTPClientPool ftpClientPool = new FTPClientPool(name, params);
        ftpClientPool.initialize();
        return ftpClientPool;
    }

    @Override
    protected FTPSession createSession(FTPClientPool connectionPool)
    {
        return new FTPSession(connectionPool);
    }

    @Override
    public String getResourceType()
    {
        return RESOURCETYPE;
    }
}
