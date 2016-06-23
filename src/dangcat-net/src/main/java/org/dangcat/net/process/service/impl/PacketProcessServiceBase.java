package org.dangcat.net.process.service.impl;

import org.dangcat.boot.event.ChangeEventAdaptor;
import org.dangcat.boot.service.DataProcessService;
import org.dangcat.boot.service.impl.QueueThreadService;
import org.dangcat.boot.statistics.StatisticsService;
import org.dangcat.framework.event.Event;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.net.event.DatagramEvent;
import org.dangcat.net.event.DatagramReceiveListener;
import org.dangcat.net.process.statistics.PacketProcessStatistics;
import org.dangcat.net.rfc.Packet;
import org.dangcat.net.rfc.PacketSession;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;
import org.dangcat.net.rfc.service.impl.ReplyPacketServiceImpl;
import org.dangcat.net.udp.conf.ListenerConfig;
import org.dangcat.net.udp.service.UDPListener;

/**
 * UDP���Ĵ����������ࡣ
 * @author dangcat
 * 
 */
public abstract class PacketProcessServiceBase<T extends Packet> extends QueueThreadService<DatagramEvent> implements DatagramReceiveListener
{
    private PacketProcessStatistics packetProcessStatistics = null;
    private DataProcessService<PacketSession<?>> replyPacketService = null;
    private UDPListener udpListener = null;

    /**
     * ��������
     * @param parent ��������
     */
    public PacketProcessServiceBase(ServiceProvider parent, String serviceName)
    {
        super(parent, serviceName);
    }

    /**
     * �����Ự����
     * @param datagramEvent ���յ����ݱ��ġ�
     * @return ���Ự����
     */
    protected abstract PacketSession<T> createPacketSession(DatagramEvent datagramEvent) throws ServiceException;

    /**
     * ���̲߳�������ӿڡ�
     */
    @Override
    protected void execute(DatagramEvent datagramEvent)
    {
        this.executeProcess(datagramEvent);
    }

    protected PacketSession<T> executeProcess(DatagramEvent datagramEvent)
    {
        // ��¼���յ�������
        PacketProcessStatistics packetProcessStatistics = this.getPacketProcessStatistics();

        T packet = null;
        PacketSession<T> packetSession = null;
        try
        {
            packetSession = this.createPacketSession(datagramEvent);
            packetSession.parse();
            packetSession.logTimeCost("parse");

            if (packetSession.getRequestPacket() != null)
            {
                packetSession.validate();
                packetSession.logTimeCost("validate");

                packetSession = this.executeProcess(packetSession);
                packetProcessStatistics.increaseSuccess();
            }
        }
        catch (ProtocolParseException e)
        {
            packetProcessStatistics.increaseParseError();
            packetSession.logError("Parse the packet error: ", datagramEvent, e);
        }
        catch (ProtocolValidateException e)
        {
            packetProcessStatistics.increaseValidError();
            packetSession.logError("Validate the packet error: ", packet, e);
        }
        catch (Exception e)
        {
            packetProcessStatistics.increaseError();
            if (packetSession != null)
                packetSession.logError("the packet error: ", packet, e);
            else
                this.getLogger().error(this, e);
        }
        finally
        {
            if (packetSession != null)
                packetSession.end();
        }
        return packetSession;
    }

    protected abstract PacketSession<T> executeProcess(PacketSession<T> packetSession);

    protected abstract ListenerConfig getListenerConfig();

    protected PacketProcessStatistics getPacketProcessStatistics()
    {
        if (this.packetProcessStatistics == null)
            this.packetProcessStatistics = new PacketProcessStatistics(this.getServiceName());
        return this.packetProcessStatistics;
    }

    /**
     * ������Ӧ���ݰ�����
     * @return
     */
    protected DataProcessService<PacketSession<?>> getReplyPacketService()
    {
        if (this.replyPacketService == null)
            this.replyPacketService = new ReplyPacketServiceImpl(this);
        return this.replyPacketService;
    }

    @Override
    public void initialize()
    {
        super.initialize();

        // �󶨶˿��޸��¼���
        this.getListenerConfig().addConfigChangeEventAdaptor(new ChangeEventAdaptor()
        {
            @Override
            public void afterChanged(Object sender, Event event)
            {
                if (ListenerConfig.Port.equals(event.getId()) || ListenerConfig.BindAddress.equals(event.getId()))
                    PacketProcessServiceBase.this.restart();
                else if (ListenerConfig.MaxConcurrentSize.equals(event.getId()) || ListenerConfig.MaxQueueCapacity.equals(event.getId()))
                    PacketProcessServiceBase.this.listenerConfigChanged();
            }
        });
        this.listenerConfigChanged();
        // ע��ͳ�ƶ���
        StatisticsService statisticsService = this.getService(StatisticsService.class);
        if (statisticsService != null)
            statisticsService.addStatistics(this.getPacketProcessStatistics());
    }

    private void listenerConfigChanged()
    {
        this.setMaxConcurrentSize(this.getListenerConfig().getMaxConcurrentSize());
        this.setMaxQueueCapacity(this.getListenerConfig().getMaxQueueCapacity());
    }

    @Override
    protected void onIgnoreProcess(DatagramEvent data)
    {
        // ͳ����Ϊ���ܲ��㿪ʼ���Ե����ݡ�
        this.getPacketProcessStatistics().increaseIgnore();

        super.onIgnoreProcess(data);
    }

    /**
     * �������ݰ���
     */
    @Override
    public void onReceive(DatagramEvent datagramEvent)
    {
        // ��¼���յ�������
        this.getPacketProcessStatistics().increaseReceive();

        this.addTask(datagramEvent);
    }

    /**
     * ��������
     */
    @Override
    public void start()
    {
        super.start();
        if (this.udpListener == null)
        {
            ListenerConfig listenerConfig = this.getListenerConfig();
            UDPListener udpListener = new UDPListener(this.getServiceName(), listenerConfig.getPort(), this);
            udpListener.setBindAddress(listenerConfig.getBindAddress());
            udpListener.setBufferSize(listenerConfig.getBufferSize());
            udpListener.setSoTimeout(listenerConfig.getTimeout());
            udpListener.startListener();
            if (udpListener.isRunning())
                this.udpListener = udpListener;
            else
                this.stop();
        }
    }

    /**
     * ����󶨼ƷѶ˿ڡ�
     */
    @Override
    public void stop()
    {
        if (this.udpListener != null)
        {
            this.udpListener.stopListener();
            this.udpListener = null;
        }
        super.stop();
    }
}
