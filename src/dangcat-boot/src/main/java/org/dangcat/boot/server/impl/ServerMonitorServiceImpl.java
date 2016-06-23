package org.dangcat.boot.server.impl;

import org.dangcat.boot.event.ChangeEventAdaptor;
import org.dangcat.boot.event.service.EventSendService;
import org.dangcat.boot.server.config.ServerConfig;
import org.dangcat.boot.server.domain.ServerInfo;
import org.dangcat.boot.server.event.ServerEvent;
import org.dangcat.boot.service.impl.ThreadService;
import org.dangcat.commons.os.monitor.SystemMonitor;
import org.dangcat.commons.timer.CronAlarmClock;
import org.dangcat.framework.event.Event;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.annotation.Service;

/**
 * ϵͳ��ط���
 * @author dangcat
 * 
 */
public class ServerMonitorServiceImpl extends ThreadService
{
    private static final String SERVICE_NAME = "SERVER-MONITOR";

    @Service
    private EventSendService eventSendService = null;

    /**
     * ��������
     * @param parent ����������
     */
    public ServerMonitorServiceImpl(ServiceProvider parent)
    {
        super(parent, SERVICE_NAME);
    }

    private void createAlarmClock()
    {
        CronAlarmClock cronAlarmClock = new CronAlarmClock(this)
        {
            @Override
            public String getCronExpression()
            {
                return ServerConfig.getInstance().getCronExpression();
            }

            @Override
            public boolean isEnabled()
            {
                return ServerConfig.getInstance().isEnabled();
            }
        };
        if (cronAlarmClock.isValidExpression())
            this.setAlarmClock(cronAlarmClock);
    }

    @Override
    protected boolean executeAtStarting()
    {
        return true;
    }

    @Override
    public void initialize()
    {
        super.initialize();

        SystemMonitor.getInstance().addDiskPath(ServerConfig.getInstance().getDiskPath());

        ServerConfig.getInstance().addConfigChangeEventAdaptor(new ChangeEventAdaptor()
        {
            @Override
            public void afterChanged(Object sender, Event event)
            {
                if (ServerConfig.CronExpression.equals(event.getId()))
                    ServerMonitorServiceImpl.this.createAlarmClock();
            }
        });
        this.createAlarmClock();
    }

    /**
     * �����߳�ִ�нӿڡ�
     */
    @Override
    protected void innerExecute()
    {
        if (ServerConfig.getInstance().isEnabled())
        {
            ServerInfo serverInfo = ServerManager.getInstance().getServerInfo();
            if (serverInfo != null)
            {
                SystemMonitor.getInstance().monitor(serverInfo);
                this.logger.info(serverInfo.print());

                ServerEvent serverEvent = new ServerEvent(ServerEvent.KeepLive, serverInfo);
                this.eventSendService.send(ServerConfig.getInstance().getMessageName(), serverEvent);
            }
        }
    }
}
