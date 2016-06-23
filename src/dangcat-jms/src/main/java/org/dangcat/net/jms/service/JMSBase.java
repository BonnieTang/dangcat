package org.dangcat.net.jms.service;

import org.dangcat.net.jms.activemq.JMSConnectionFactory;
import org.dangcat.net.jms.activemq.JMSSession;

/**
 * JMS��Ϣ����
 */
public abstract class JMSBase {
    private JMSSession jmsSession = null;
    private String name;

    /**
     * ��������
     *
     * @param parent ��������
     * @param name   ��Ϣ���ơ�
     */
    public JMSBase(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JMSBase other = (JMSBase) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    protected JMSSession getJMSSession() {
        if (this.jmsSession == null)
            this.jmsSession = JMSConnectionFactory.getInstance().openSession(this.getName());
        return jmsSession;
    }

    /**
     * �������ơ�
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public void initialize() {
    }

    /**
     * ֹͣ����
     */
    public void stop() {
        if (this.jmsSession == null) {
            this.jmsSession.release();
            this.jmsSession = null;
        }
    }
}
