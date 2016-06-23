package org.dangcat.net.jms.activemq;

import org.dangcat.framework.pool.ConnectionFactory;
import org.dangcat.framework.pool.SessionException;

import java.util.Map;

/**
 * JMS���ӹ�����
 */
public class JMSConnectionFactory extends ConnectionFactory<JMSConnectionPool, JMSSession> {
    public static final String RESOURCETYPE = "jms";
    private static JMSConnectionFactory instance = null;

    /**
     * ˽�й��캯����
     */
    private JMSConnectionFactory() {
    }

    public static JMSConnectionFactory getInstance() {
        if (instance == null) {
            synchronized (JMSConnectionFactory.class) {
                try {
                    JMSConnectionFactory instance = new JMSConnectionFactory();
                    instance.initialize();
                    JMSConnectionFactory.instance = instance;
                } catch (SessionException e) {
                    logger.error("Create jms connection error!", e);
                }
            }
        }
        return instance;
    }

    @Override
    protected void close(JMSConnectionPool jmsConnectionPool) {
        jmsConnectionPool.close();
    }

    @Override
    protected JMSConnectionPool createConnectionPool(String name, Map<String, String> params) throws SessionException {
        JMSConnectionPool jmsConnectionPool = new JMSConnectionPool(name, params);
        jmsConnectionPool.initialize();
        return jmsConnectionPool;
    }

    @Override
    protected JMSSession createSession(JMSConnectionPool jmsConnectionPool) {
        return new JMSSession(jmsConnectionPool);
    }

    @Override
    public String getResourceType() {
        return RESOURCETYPE;
    }
}
