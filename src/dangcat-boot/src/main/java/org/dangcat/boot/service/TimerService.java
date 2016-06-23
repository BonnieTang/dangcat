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
    void cancelTimer(AlarmClock alarmClock);

    /**
     * ע�ᶨʱ����
     * @param alarmClock ���Ӷ��塣
     */
    void createTimer(AlarmClock alarmClock);

    /**
     * ��ʱִ�нӿڡ�
     */
    void intervalExec();
}
