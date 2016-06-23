package org.dangcat.commons.io;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.dangcat.commons.utils.NameThreadFactory;

/**
 * �ļ���Ϊ��������
 * @author dangcat
 * 
 */
public class FileActionMonitor
{
    private static FileActionMonitor instance = new FileActionMonitor();

    public static FileActionMonitor getInstance()
    {
        return instance;
    }

    private List<FileActionObserver> fileActionObserverList = new ArrayList<FileActionObserver>();
    private FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor();
    private boolean isRunning = false;

    private FileActionMonitor()
    {

    }

    public void addFileWatcherAdaptor(FileWatcherAdaptor fileWatcherAdaptor)
    {
        FileActionObserver fileActionObserver = new FileActionObserver(fileWatcherAdaptor);
        this.fileActionObserverList.add(fileActionObserver);
        this.fileAlterationMonitor.addObserver(fileActionObserver);
        this.start();
    }

    public void removeFileWatcherAdaptor(FileWatcherAdaptor fileWatcherAdaptor)
    {
        FileActionObserver fileActionObserver = new FileActionObserver(fileWatcherAdaptor);
        this.fileActionObserverList.remove(fileActionObserver);
        this.fileAlterationMonitor.removeObserver(fileActionObserver);
        this.stop();
    }

    public void start()
    {
        if (isRunning || fileActionObserverList.size() == 0)
            return;

        try
        {
            this.fileAlterationMonitor.setThreadFactory(new NameThreadFactory("FILEMONITOR-"));
            this.fileAlterationMonitor.start();
            this.isRunning = true;
        }
        catch (Exception e)
        {
        }
    }

    public void stop()
    {
        if (!isRunning || fileActionObserverList.size() == 0)
            return;

        try
        {
            this.fileAlterationMonitor.stop();
            this.isRunning = false;
        }
        catch (Exception e)
        {
        }
    }
}
