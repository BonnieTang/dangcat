package org.dangcat.net.jms.service;

import org.dangcat.boot.event.service.EventListenService;
import org.dangcat.boot.event.service.EventSendService;
import org.dangcat.boot.event.service.impl.EventSendServiceImpl;
import org.dangcat.framework.service.ServiceBase;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.net.jms.activemq.JMSConnectionFactory;

/**
 * ��Ϣ����
 */
public class JMSServiceImpl extends ServiceBase {
    /**
     * �����������
     *
     * @param parent ��������
     */
    public JMSServiceImpl(ServiceProvider parent) {
        super(parent);
    }

    /**
     * ��ʼ������
     */
    @Override
    public void initialize() {
        super.initialize();

        EventListenService eventListenService = this.getService(EventListenService.class);
        EventSendServiceImpl eventSendService = (EventSendServiceImpl) this.getService(EventSendService.class);

        // ������Դ���ó�ʼ����Ϣ�����
        for (String name : JMSConnectionFactory.getInstance().getResourceNames()) {
            // ��Ϣ���ͷ���
            JMSSender jmsSender = new JMSSender(name);
            jmsSender.initialize();
            eventSendService.addEventSender(jmsSender);

            // ��Ϣ��������
            JMSListener jmsListener = new JMSListener(name, eventListenService);
            jmsListener.initialize();
            eventListenService.addEventListener(jmsListener);
        }
    }
}
