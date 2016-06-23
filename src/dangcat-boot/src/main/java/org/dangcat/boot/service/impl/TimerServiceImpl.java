package org.dangcat.boot.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.dangcat.boot.service.TimerService;
import org.dangcat.commons.timer.AlarmClock;
import org.dangcat.commons.timer.Timer;
import org.dangcat.commons.utils.DateUtils;
import org.dangcat.commons.utils.Environment;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.ServiceStatus;
import org.dangcat.framework.service.impl.ServiceControlBase;

/**
 * ��ʱ����ӿڡ�
 * @author dangcat
 * 
 */
public class TimerServiceImpl extends ServiceControlBase implements TimerService
{
    private static final int DEFAULT_INTERVAL = 1000;
    private static TimerServiceImpl instance = null;
    private static final int MAX_INTERVAL = 60000;

    /**
     * ��������ʵ����
     * @param parent ����������
     * @return
     */
    public static synchronized TimerService createInstance(ServiceProvider parent)
    {
        if (instance == null)
        {
            instance = new TimerServiceImpl(parent);
            instance.initialize();
        }
        return instance;
    }

    /**
     * ��ȡ����ʵ����
     * @return
     */
    public static TimerService getInstance()
    {
        return instance;
    }

    private List<AlarmClock> alarmClockList = new ArrayList<AlarmClock>();
    private Timer timer;

    /**
     * ��������
     * @param parent ��������
     */
    public TimerServiceImpl(ServiceProvider parent)
    {
        super(parent);
    }

    /**
     * ȡ����ʱ����
     * @param alarmClock ��ʱ������
     */
    public synchronized void cancelTimer(AlarmClock alarmClock)
    {
        this.removeTimer(alarmClock);
    }

    /**
     * ע�ᶨʱ����
     * @param alarmClock ��ʱ������
     */
    public synchronized void createTimer(AlarmClock alarmClock)
    {
        if (alarmClock != null)
        {
            this.removeTimer(alarmClock);
            this.alarmClockList.add(alarmClock);
            Collections.sort(this.alarmClockList);
        }
    }

    private void execute(AlarmClock alarmClock)
    {
        try
        {
            Object target = alarmClock.getTarget();
            // �����߳�ģʽ��
            if (target instanceof ThreadService)
            {
                ThreadService threadService = (ThreadService) target;
                if (threadService.isEnabled())
                {
                    threadService.resume();
                    return;
                }
            }
            // ʵ�����߳̽ӿڡ�
            Runnable runnable = alarmClock.getRunnable();
            if (runnable != null)
            {
                if (alarmClock.couldRun())
                    ThreadPoolFactory.getInstance().execute(runnable);
            }
        }
        catch (Exception e)
        {
            logger.error(this, e);
        }
    }

    /**
     * ��ʱִ�нӿڡ�
     */
    public synchronized void intervalExec()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.now());
        long waitingTime = MAX_INTERVAL;
        for (AlarmClock alarmClock : this.alarmClockList)
        {
            if (!alarmClock.isEnabled())
                continue;

            if (Environment.isTestEnabled() || alarmClock.isTimeout(calendar))
                this.execute(alarmClock);

            // ������С�ȴ�ʱ�䡣
            if (alarmClock.getNextAlramTime() != null)
            {
                long distanceTime = alarmClock.getNextAlramTime().getTime() - calendar.getTimeInMillis();
                if (distanceTime > 0)
                    waitingTime = Math.min(distanceTime, waitingTime);
            }
        }
        if (this.timer != null)
            this.timer.setPeriod(waitingTime);
    }

    private void removeTimer(AlarmClock alarmClock)
    {
        if (alarmClock != null)
        {
            if (this.alarmClockList.contains(alarmClock))
                this.alarmClockList.remove(alarmClock);
            else
            {
                AlarmClock found = null;
                for (AlarmClock childAlarmClock : this.alarmClockList)
                {
                    if (childAlarmClock.getTarget() == alarmClock.getTarget())
                    {
                        found = childAlarmClock;
                        break;
                    }
                }
                if (found != null)
                    this.alarmClockList.remove(found);
            }
        }
    }

    /**
     * ������ʱ��
     */
    @Override
    public void start()
    {
        if (this.getServiceStatus().equals(ServiceStatus.Stopped))
        {
            this.setServiceStatus(ServiceStatus.Starting);
            synchronized (this)
            {
                if (!Environment.isTestEnabled() && this.timer == null)
                {
                    // ��ض�ʱ����
                    this.timer = new Timer(TimerService.class.getSimpleName(), new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TimerServiceImpl.this.intervalExec();
                        }
                    }, DEFAULT_INTERVAL, DEFAULT_INTERVAL);
                    this.timer.start();
                }
            }
            super.start();
        }
    }

    /**
     * ֹͣ��ʱ��
     */
    @Override
    public void stop()
    {
        if (this.getServiceStatus().equals(ServiceStatus.Started))
        {
            this.setServiceStatus(ServiceStatus.Starting);
            synchronized (this)
            {
                if (this.timer != null)
                {
                    this.timer.stop();
                    this.timer = null;
                }
            }
            super.stop();
        }
    }
}
