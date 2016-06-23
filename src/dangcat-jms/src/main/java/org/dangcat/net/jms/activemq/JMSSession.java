package org.dangcat.net.jms.activemq;

import org.apache.log4j.Logger;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * JMS�Ự����
 * 
 */
public class JMSSession
{
    protected static final Logger logger = Logger.getLogger(JMSSession.class);
    private Connection connection = null;
    private Destination destination;
    private JMSConnectionPool jmsConnectionPool = null;
    private Session session;

    public JMSSession(JMSConnectionPool jmsConnectionPool)
    {
        this.jmsConnectionPool = jmsConnectionPool;
    }

    private void close()
    {
        try
        {
            if (this.session != null)
            {
                this.session.close();
                this.session = null;
            }
        }
        catch (JMSException e)
        {
            logger.error(this, e);
        }
    }

    protected void commit() throws JMSException
    {
        if (this.jmsConnectionPool.isTransacted())
            this.session.commit();
    }

    /**
     * ������Ϣ���Ѷ���
     * @throws JMSException �����쳣��
     */
    public JMSConsumer createJMSConsumer() throws JMSException
    {
        JMSConsumer jmsConsumer = new JMSConsumer(this);
        jmsConsumer.initialize();
        return jmsConsumer;
    }

    /**
     * ������Ϣ��������
     * @throws JMSException �����쳣��
     */
    public JMSProducer createJMSProducer() throws JMSException
    {
        JMSProducer jmsProducer = new JMSProducer(this);
        jmsProducer.initialize();
        return jmsProducer;
    }

    private Connection getConnection()
    {
        return this.connection;
    }

    protected Destination getDestination() throws JMSException
    {
        return this.destination;
    }

    public JMSConnectionPool getJMSConnectionPool()
    {
        return jmsConnectionPool;
    }

    public Session getSession() throws JMSException
    {
        if (this.session == null)
            this.initialize();
        return this.session;
    }

    /**
     * ��ʼ���Ự����
     */
    private void initialize() throws JMSException
    {
        this.connection = this.jmsConnectionPool.poll();
        this.session = this.getConnection().createSession(this.jmsConnectionPool.isTransacted(), this.jmsConnectionPool.getAcknownledge());
        if (this.jmsConnectionPool.isTopic())
            this.destination = session.createTopic(this.jmsConnectionPool.getSubject());
        else
            this.destination = session.createQueue(this.jmsConnectionPool.getSubject());
    }

    /**
     * �ͷŻỰ����
     */
    public void release()
    {
        this.close();
        if (this.connection != null)
        {
            this.jmsConnectionPool.release(this.connection);
            this.connection = null;
        }
    }
}
