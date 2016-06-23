package org.dangcat.boot;

import org.dangcat.boot.config.WebServiceConfig;
import org.dangcat.boot.event.service.EventListenService;
import org.dangcat.boot.event.service.EventSendService;
import org.dangcat.boot.event.service.impl.EventListenServiceImpl;
import org.dangcat.boot.event.service.impl.EventSendServiceImpl;
import org.dangcat.boot.security.SecurityLoginService;
import org.dangcat.boot.security.impl.SecurityLoginServiceImpl;
import org.dangcat.boot.server.impl.ServerManager;
import org.dangcat.boot.server.impl.ServerMonitorServiceImpl;
import org.dangcat.boot.service.EntityBatchService;
import org.dangcat.boot.service.ThreadPoolService;
import org.dangcat.boot.service.TimerService;
import org.dangcat.boot.service.impl.*;
import org.dangcat.boot.statistics.StatisticsService;
import org.dangcat.boot.statistics.StatisticsServiceImpl;
import org.dangcat.framework.service.ServiceBase;
import org.dangcat.framework.service.ServiceLocator;
import org.dangcat.framework.service.impl.ServiceFactory;
import org.dangcat.persistence.entity.EntityManagerFactory;

class CoreServiceLoader {
    protected static void load(ServiceBase serviceBase) {
        // ���񹤳�
        ServiceFactory serviceFactory = ServiceFactory.createInstance(serviceBase);
        serviceBase.addService(ServiceLocator.class, serviceFactory);
        serviceBase.addService(ServiceFactory.class, serviceFactory);

        // �̳߳ط���
        ThreadPoolService threadPoolService = ThreadPoolFactory.createInstance(serviceBase);
        serviceBase.addService(ThreadPoolService.class, threadPoolService);

        // ��ʱ������
        TimerService timerService = TimerServiceImpl.createInstance(serviceBase);
        serviceBase.addService(TimerService.class, timerService);

        // ͳ�Ʒ���
        StatisticsServiceImpl statisticsService = new StatisticsServiceImpl(serviceBase);
        serviceBase.addService(StatisticsService.class, statisticsService);
        statisticsService.initialize();

        // �������
        ServerManager serverManager = ServerManager.createInstance(serviceBase);
        serviceBase.addService(ServerManager.class, serverManager);
        serverManager.initialize();

        // ϵͳ��ط���
        ServerMonitorServiceImpl systemMonitorService = new ServerMonitorServiceImpl(serviceBase);
        serviceBase.addService(ServerMonitorServiceImpl.class, systemMonitorService);
        systemMonitorService.initialize();

        // ���ݻ������
        EntityManagerFactory.getInstance();
        EntityCacheServiceImpl entityCacheService = new EntityCacheServiceImpl(serviceBase);
        serviceBase.addService(EntityCacheServiceImpl.class, entityCacheService);
        entityCacheService.initialize();

        // ����������������
        EntityBatchService entityBatchService = EntityBatchServiceImpl.createInstance(serviceBase);
        serviceBase.addService(EntityBatchService.class, entityBatchService);

        // Web����
        if (WebServiceConfig.getInstance().isEnabled()) {
            WebService webService = new WebService(serviceBase);
            serviceBase.addService(WebService.class, webService);
            webService.initialize();
        }

        // ��Ϣ���ͷ���
        EventSendServiceImpl eventSendService = new EventSendServiceImpl(serviceBase);
        serviceBase.addService(EventSendService.class, eventSendService);
        eventSendService.initialize();

        // ��Ϣ��������
        EventListenServiceImpl eventListenService = new EventListenServiceImpl(serviceBase);
        serviceBase.addService(EventListenService.class, eventListenService);
        eventListenService.initialize();

        // ��ȫ����
        SecurityLoginService securityLoginService = new SecurityLoginServiceImpl(serviceBase);
        serviceFactory.addService(SecurityLoginService.class, securityLoginService);
    }
}
