package org.dangcat.boot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;

import org.dangcat.boot.ApplicationContext;
import org.dangcat.boot.config.ThreadPoolConfig;
import org.dangcat.boot.event.ChangeEventAdaptor;
import org.dangcat.boot.service.ThreadPoolService;
import org.dangcat.commons.utils.Environment;
import org.dangcat.framework.event.Event;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.ServiceStatus;
import org.dangcat.framework.service.impl.ServiceControlBase;

/**
 * �̳߳ط���
 * @author dangcat
 * 
 */
public class ThreadPoolFactory extends ServiceControlBase implements ThreadPoolService, RejectedExecutionHandler
{
    private static ThreadPoolFactory instance = null;
    private static final String SERVICE_NAME = "ThreadPoolWork";

    /**
     * ��������ʵ����
     * @param parent ����������
     * @return
     */
    public static synchronized ThreadPoolService createInstance(ServiceProvider parent)
    {
        if (instance == null)
        {
            instance = new ThreadPoolFactory(parent);
            instance.initialize();
        }
        return instance;
    }

    /**
     * ��ȡ����ʵ����
     * @return
     */
    public static ThreadPoolService getInstance()
    {
        return instance;
    }

    /** �ܾ��¼��б� */
    private List<RejectedExecutionHandler> rejectedExecutionHandlerList = new ArrayList<RejectedExecutionHandler>();
    /** �̳߳� */
    private ThreadPoolExecutor threadPoolExecutor = null;
    /** ����������������С� */
    private BlockingQueue<Runnable> workQueue = null;

    /**
     * ��������
     * @param parent ����������
     */
    private ThreadPoolFactory(ServiceProvider parent)
    {
        super(parent);

        ThreadPoolConfig.getInstance().addChangeEventAdaptor(new ChangeEventAdaptor()
        {
            @Override
            public void afterChanged(Object sender, Event event)
            {
                if (ThreadPoolFactory.this.threadPoolExecutor != null)
                    ThreadPoolFactory.this.restart();
            }
        });
    }

    /**
     * ��Ӿܾ�������������
     * @param rejectedExecutionHandler ��������
     */
    public void addRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler)
    {
        if (rejectedExecutionHandler != null && !this.rejectedExecutionHandlerList.contains(rejectedExecutionHandler))
            this.rejectedExecutionHandlerList.add(rejectedExecutionHandler);
    }

    /**
     * ִ�ж��߳�����
     */
    @Override
    public void execute(Runnable runnable)
    {
        if (Environment.isTestEnabled())
            runnable.run();
        else
        {
            ThreadPoolExecutor threadPoolExecutor = this.threadPoolExecutor;
            if (threadPoolExecutor != null)
                threadPoolExecutor.execute(runnable);
        }
    }

    /**
     * ��ǰ����ִ�е���������
     */
    @Override
    public int getActiveCount()
    {
        ThreadPoolExecutor threadPoolExecutor = this.threadPoolExecutor;
        if (threadPoolExecutor != null)
            return threadPoolExecutor.getExecutingCount();
        return 0;
    }

    /**
     * ��ǰ������������������
     */
    @Override
    public int getBlockCount()
    {
        BlockingQueue<Runnable> workQueue = this.workQueue;
        if (workQueue != null)
            return this.workQueue.size();
        return 0;
    }

    /**
     * ����߳�ִ�ж���
     */
    @Override
    public boolean isShutdown()
    {
        ThreadPoolExecutor threadPoolExecutor = this.threadPoolExecutor;
        if (threadPoolExecutor != null)
            return threadPoolExecutor.isShutdown();
        return true;
    }

    /**
     * �����ܾ�������������
     * @param runnable ��������
     * @param executor �̳߳ض���
     */
    @Override
    public void rejectedExecution(Runnable runnable, java.util.concurrent.ThreadPoolExecutor threadPoolExecutor)
    {
        for (RejectedExecutionHandler rejectedExecutionHandler : this.rejectedExecutionHandlerList)
            rejectedExecutionHandler.rejectedExecution(runnable, threadPoolExecutor);
    }

    /**
     * ɾ���ܾ�������������
     * @param rejectedExecutionHandler ��������
     */
    @Override
    public void removeRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler)
    {
        if (rejectedExecutionHandler != null && this.rejectedExecutionHandlerList.contains(rejectedExecutionHandler))
            this.rejectedExecutionHandlerList.remove(rejectedExecutionHandler);
    }

    /**
     * ��������
     */
    @Override
    public synchronized void start()
    {
        if (this.threadPoolExecutor == null && !Environment.isTestEnabled())
        {
            this.setServiceStatus(ServiceStatus.Starting);
            if (this.workQueue == null)
                this.workQueue = new ArrayBlockingQueue<Runnable>(ThreadPoolConfig.getInstance().getQueueCapacity());
            int corePoolSize = ThreadPoolConfig.getInstance().getMaximumPoolSize();
            int maximumPoolSize = ThreadPoolConfig.getInstance().getMaximumPoolSize();
            this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, SERVICE_NAME, this.workQueue, this);

            super.start();
        }
    }

    /**
     * ֹͣ����
     */
    @Override
    public void stop()
    {
        ThreadPoolExecutor threadPoolExecutor = this.threadPoolExecutor;
        if (threadPoolExecutor != null)
        {
            this.setServiceStatus(ServiceStatus.Stopping);
            synchronized (this.threadPoolExecutor)
            {
                this.threadPoolExecutor = null;
            }
            threadPoolExecutor.shutdown();
            super.stop();
        }
    }

    /**
     * ��ϵͳ�ύȫ���¼���
     * @param event �¼�����
     */
    public void submitEvent(final Event event)
    {
        this.execute(new Runnable()
        {
            @Override
            public void run()
            {
                ApplicationContext.getInstance().handle(event);
            }
        });
    }
}
