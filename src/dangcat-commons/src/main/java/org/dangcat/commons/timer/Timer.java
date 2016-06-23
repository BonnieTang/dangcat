package org.dangcat.commons.timer;

/**
 * ��ʱ����
 * @author dangcat
 * 
 */
public class Timer
{
    private long delay = 0;
    private boolean isDaemon = false;
    private boolean isRunning = false;
    private String name = null;
    private long period = 0;
    private Runnable runnable = null;
    private Thread thread = null;

    /**
     * ������ʱ����
     * @param name ��ʱ�����ơ�
     */
    public Timer(String name)
    {
        this(name, null, 0, 0);
    }

    /**
     * ������ʱ����
     * @param name ��ʱ�����ơ�
     * @param runnable ����ӿڡ�
     */
    public Timer(String name, Runnable runnable)
    {
        this(name, runnable, 0, 0);
    }

    /**
     * ������ʱ����
     * @param name ��ʱ�����ơ�
     * @param runnable ����ӿڡ�
     * @param period �������ڣ���λ�����룩��
     */
    public Timer(String name, Runnable runnable, long period)
    {
        this(name, runnable, 0, period);
    }

    /**
     * ������ʱ����
     * @param name ��ʱ�����ơ�
     * @param runnable ����ӿڡ�
     * @param delay ��ʱ���ȣ���λ�����룩��
     * @param period �������ڣ���λ�����룩��
     */
    public Timer(String name, Runnable runnable, long delay, long period)
    {
        this.name = name;
        this.runnable = runnable;
        this.delay = delay;
        this.period = period;
    }

    public long getDelay()
    {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getPeriod()
    {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public Runnable getRunnable()
    {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    private void intervalExec()
    {
        if (this.getDelay() > 0)
            this.waiting(this.getDelay());

        while (this.isRunning)
        {
            this.waiting(this.getPeriod());
            this.getRunnable().run();
        }
    }

    public boolean isDaemon()
    {
        return isDaemon;
    }

    public void setDaemon(boolean isDaemon)
    {
        this.isDaemon = isDaemon;
    }

    /**
     * ������ʱ����
     */
    public synchronized void start()
    {
        if (this.getRunnable() != null && this.getPeriod() > 0)
        {
            this.isRunning = true;
            this.thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    intervalExec();
                }
            }, this.name);
            this.thread.setDaemon(this.isDaemon());
            this.thread.start();
        }
    }

    /**
     * ֹͣ��ʱ����
     */
    public synchronized void stop()
    {
        if (this.isRunning)
        {
            this.isRunning = false;
            this.thread.interrupt();
            this.thread = null;
        }
    }

    /**
     * �ȴ���ʱ��
     * @param milliSecond ��ʱ���ȣ���λ���롣
     */
    private synchronized void waiting(long milliSecond)
    {
        try
        {
            if (milliSecond > 0)
                this.wait(milliSecond);
        }
        catch (InterruptedException e)
        {
        }
    }
}
