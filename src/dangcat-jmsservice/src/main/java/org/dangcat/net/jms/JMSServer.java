package org.dangcat.net.jms;

import java.net.URI;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.dangcat.boot.ApplicationContext;
import org.dangcat.boot.Launcher;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.impl.ExtensionClassLoader;
import org.dangcat.framework.service.impl.ServiceControlBase;
import org.dangcat.net.jms.conf.JMSConfig;

/**
 * DangCat Message Service��
 * 
 */
public class JMSServer extends ServiceControlBase
{
    public static final String SERVICE_NAME = "jmsservice";

    public static void main(String[] args)
    {
        Launcher.start(JMSServer.class, SERVICE_NAME, false);
    }

    private BrokerService brokerService = null;

    /**
     * ��������
     * @param parent ����������
     */
    public JMSServer(ServiceProvider parent)
    {
        super(parent);
    }

    @Override
    public boolean isRunning()
    {
        return this.brokerService != null;
    }

    /**
     * ����Ƕ��ʽJMS����
     */
    @Override
    public void start()
    {
        if (!this.isRunning())
        {
            try
            {
                System.setProperty("activemq.home", ApplicationContext.getInstance().getContextPath().getBaseDir());
                System.setProperty("activemq.conf", ApplicationContext.getInstance().getContextPath().getConf());

                String brokerURI = "xbean:" + JMSConfig.getInstance().getConfigFile();
                // ����Ƕ�����
                this.brokerService = BrokerFactory.createBroker(new URI(brokerURI));
                this.brokerService.start();
            }
            catch (Exception e)
            {
                this.logger.error(ExtensionClassLoader.getInstance(), e);
            }
        }
        super.start();
    }

    /**
     * ֹͣ����
     */
    @Override
    public void stop()
    {
        super.stop();

        if (this.isRunning())
        {
            try
            {
                this.brokerService.stop();
                this.brokerService = null;
            }
            catch (Exception e)
            {
                this.logger.error(this, e);
            }
        }
    }
}
