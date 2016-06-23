package org.dangcat.boot.service.impl;

import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.impl.ServiceControlBase;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ���ж��̷߳�������ࡣ
 * @author dangcat
 * 
 */
public class QueueThreadService<T> extends ServiceControlBase implements Runnable
{
    private static final int DEFAULT_CONCURRENT_SIZE = 1;
    private static final int DEFAULT_MAX_QUEUECAPACITY = 10000;
    /** ����ִ�е�������С� */
    private Queue<T> executingCache = new LinkedList<T>();
    /** ��󲢷������� */
    private int maxConcurrentSize = DEFAULT_CONCURRENT_SIZE;
    /** ������������ */
    private int maxQueueCapacity = DEFAULT_MAX_QUEUECAPACITY;
    /** ����ִ�нӿڡ� */
    private QueueExecuteor<T> queueExecuteor = null;
    /** �ȴ�ִ�е�������С� */
    private Queue<T> waitingCache = new LinkedList<T>();

    /**
     * �������
     * @param parent ��������
     */
    public QueueThreadService(ServiceProvider parent)
    {
        super(parent);
    }

    /**
     * �������
     * @param parent ��������
     */
    public QueueThreadService(ServiceProvider parent, String serviceName)
    {
        super(parent, serviceName);
    }

    /**
     * �������
     * @param parent ��������
     * @param name �������ơ�
     * @param queueExecuteor ����ִ�нӿڡ�
     */
    public QueueThreadService(ServiceProvider parent, String serviceName, QueueExecuteor<T> queueExecuteor)
    {
        this(parent, serviceName);
        this.queueExecuteor = queueExecuteor;
    }

    /**
     * ����ء�
     */
    public void addTask(T... tasks)
    {
        if (tasks != null)
        {
            for (T task : tasks)
            {
                if (this.isWaitingQueueFull())
                    this.onIgnoreProcess(task);
                else
                {
                    synchronized (this.waitingCache)
                    {
                        this.waitingCache.add(task);
                    }
                }
            }
            this.innerExecute();
        }
    }

    protected void execute(T data)
    {
        if (this.queueExecuteor != null)
            this.queueExecuteor.execute(data);
    }

    public int getExecuteSize()
    {
        return this.executingCache.size();
    }

    public int getMaxConcurrentSize()
    {
        return maxConcurrentSize;
    }

    public void setMaxConcurrentSize(int maxConcurrentSize) {
        this.maxConcurrentSize = maxConcurrentSize;
    }

    public int getMaxQueueCapacity()
    {
        return maxQueueCapacity;
    }

    public void setMaxQueueCapacity(int maxQueueCapacity) {
        this.maxQueueCapacity = maxQueueCapacity;
    }

    public QueueExecuteor<T> getQueueExecuteor()
    {
        return queueExecuteor;
    }

    public void setQueueExecuteor(QueueExecuteor<T> queueExecuteor) {
        this.queueExecuteor = queueExecuteor;
    }

    public int getWaitingSize()
    {
        return this.waitingCache.size();
    }

    private void innerExecute()
    {
        while (!this.isExecuteQueueFull() && this.getWaitingSize() > 0)
        {
            T task = null;
            synchronized (this.waitingCache)
            {
                task = this.waitingCache.poll();
            }
            if (task != null)
            {
                synchronized (this.executingCache)
                {
                    this.executingCache.add(task);
                }
                ThreadPoolFactory.getInstance().execute(this);
            }
        }
    }

    /**
     * ִ�ж����Ƿ��Ѿ������ɡ�
     */
    public boolean isExecuteQueueFull()
    {
        return this.getExecuteSize() >= this.getMaxConcurrentSize();
    }

    /**
     * �ȴ������Ƿ��Ѿ������ɡ�
     */
    public boolean isWaitingQueueFull()
    {
        return this.getWaitingSize() >= this.getMaxQueueCapacity();
    }

    /**
     * ������Ϊ��������������Ե����ݡ�
     */
    protected void onIgnoreProcess(T data)
    {
    }

    /**
     * �����߳�ִ�нӿڡ�
     */
    @Override
    public void run()
    {
        T task = null;
        synchronized (this.executingCache)
        {
            task = this.executingCache.poll();
        }
        if (task != null)
        {
            try
            {
                this.execute(task);
            }
            finally
            {
                this.innerExecute();
            }
        }
    }
}
