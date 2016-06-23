package org.dangcat.boot.service;

import org.dangcat.commons.timer.AlarmClock;
import org.dangcat.framework.service.ServiceControl;

/**
 * ��ʱ����ӿڡ�
 * @author dangcat
 * 
 */
public interface TimerService extends ServiceControl
{
    /**
     * ȡ����ʱ����
     * @param alarmClock ��ʱ������
     */
    public void cancelTimer(AlarmClock alarmClock);

    /**
     * ע�ᶨʱ����
     * @param alarmClock ���Ӷ��塣
     */
    public void createTimer(AlarmClock alarmClock);

    /**
     * ��ʱִ�нӿڡ�
     */
    public void intervalExec();
}
