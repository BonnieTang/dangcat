package org.dangcat.boot.service.impl;

import org.dangcat.boot.service.FileWriteService;
import org.dangcat.commons.io.FileWriter;
import org.dangcat.commons.timer.AlarmClock;
import org.dangcat.commons.timer.IntervalAlarmClock;
import org.dangcat.framework.service.ServiceBase;
import org.dangcat.framework.service.ServiceProvider;

import java.util.Collection;
import java.util.HashSet;

/**
 * �ļ�д�����
 *
 * @author dangcat
 */
public class FileWriteServiceImpl extends ServiceBase implements Runnable, FileWriteService {
    private static final int INTERVAL = 30;
    private Collection<FileWriter> fileWriters = new HashSet<FileWriter>();

    /**
     * ����������
     *
     * @param parent
     */
    public FileWriteServiceImpl(ServiceProvider parent) {
        super(parent);
    }

    /**
     * ����ļ�д�����
     *
     * @param fileWriter �ļ�д�����
     */
    public void addFileWriter(FileWriter fileWriter) {
        this.fileWriters.add(fileWriter);
    }

    @Override
    public void initialize() {
        super.initialize();

        AlarmClock alarmClock = new IntervalAlarmClock(this) {
            @Override
            public long getInterval() {
                return INTERVAL;
            }

            @Override
            public boolean isEnabled() {
                return fileWriters.size() > 0;
            }
        };
        TimerServiceImpl.getInstance().createTimer(alarmClock);
    }

    /**
     * ɾ���ļ�д�����
     *
     * @param fileWriter �ļ�д�����
     */
    public void removeFileWriter(FileWriter fileWriter) {
        this.fileWriters.remove(fileWriter);
    }

    /**
     * ��ʱ������ڵĻ������ݡ�
     */
    @Override
    public void run() {
        for (FileWriter fileWriter : this.fileWriters) {
            synchronized (fileWriter) {
                fileWriter.switchFile();
            }
        }
    }
}
