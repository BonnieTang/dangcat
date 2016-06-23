package org.dangcat.net.jms.activemq;

import org.apache.log4j.Logger;

import javax.jms.*;

/**
 * JMS��������
 * 
 */
public class JMSProducer
{
    protected static final Logger logger = Logger.getLogger(JMSProducer.class);
    private JMSSession jmsSession;
    private MessageProducer messageProducer = null;
    private long messagesSended = 0;

    public JMSProducer(JMSSession jmsSession)
    {
        this.jmsSession = jmsSession;
    }

    private void close()
    {
        try
        {
            if (this.messageProducer != null)
            {
                this.messageProducer.close();
                this.messageProducer = null;
            }
        }
        catch (JMSException e)
        {
            logger.error(this, e);
        }
    }

    /**
     * ��ʼ����Ϣ��������
     */
    protected void initialize() throws JMSException
    {
        JMSConnectionPool jmsConnectionPool = this.jmsSession.getJMSConnectionPool();
        try
        {
            Session session = this.jmsSession.getSession();
            this.messageProducer = session.createProducer(this.jmsSession.getDestination());
            // �Ƿ�־û���Ϣ��
            if (jmsConnectionPool.isPersistent())
                this.messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            else
                this.messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // ��Ϣ���ʱ�䡣
            if (jmsConnectionPool.getTimeToLive() != 0)
                this.messageProducer.setTimeToLive(jmsConnectionPool.getTimeToLive());
        }
        catch (JMSException e)
        {
            if (jmsConnectionPool.isCommunicationsException(e))
            {
                this.release();
                jmsConnectionPool.closePooled();
            }
            throw e;
        }
    }

    /**
     * �ͷŻỰ����
     */
    public void release()
    {
        this.close();
        this.jmsSession.release();
    }

    /**
     * ����JMS��Ϣ��
     * @param message ��Ϣ����
     * @throws JMSException �����쳣��
     */
    public void send(Message message) throws JMSException
    {
        if (message != null)
        {
            this.messagesSended++;
            this.messageProducer.send(message);
            this.jmsSession.commit();
        }
    }
}
