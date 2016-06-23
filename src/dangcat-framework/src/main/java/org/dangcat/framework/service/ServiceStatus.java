package org.dangcat.framework.service;

/**
 * ����״̬��
 * @author dangcat
 * 
 */
public enum ServiceStatus
{
    /** ��ͣ�� */
    Pause(4),
    /** �Ѿ������� */
    Started(2),
    /** ���������� */
    Starting(1),
    /** �Ѿ�ֹͣ�� */
    Stopped(0),
    /** ����ֹͣ�� */
    Stopping(3);

    private final int value;

    ServiceStatus(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }
}
