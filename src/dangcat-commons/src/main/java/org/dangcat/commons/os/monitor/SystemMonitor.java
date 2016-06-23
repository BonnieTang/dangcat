package org.dangcat.commons.os.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.OSType;
import org.dangcat.commons.utils.ValueUtils;

/**
 * ϵͳ��Դ��ء�
 * @author dangcat
 * 
 */
public class SystemMonitor
{
    private static SystemMonitor instance = new SystemMonitor();
    protected static final Logger logger = Logger.getLogger(SystemMonitor.class);
    private static OSMonitor osMonitor = null;

    public static SystemMonitor getInstance()
    {
        return instance;
    }

    private List<File> monitorDiskPathList = new ArrayList<File>();

    private SystemMonitor()
    {
    }

    /**
     * ��Ӵ�����Դ���·����
     * @param path ·������
     */
    public void addDiskPath(File path)
    {
        if (path != null && path.exists())
        {
            synchronized (monitorDiskPathList)
            {
                if (!monitorDiskPathList.contains(path))
                    monitorDiskPathList.add(path);
            }
        }
    }

    public void addDiskPath(String path)
    {
        if (!ValueUtils.isEmpty(path))
        {
            for (String pathName : path.split(";"))
                this.addDiskPath(new File(pathName));
        }
    }

    public List<File> getMonitorDiskPathList()
    {
        return monitorDiskPathList;
    }

    /**
     * �����Դ��Ϣ��
     */
    public void monitor(MonitorInfo monitorInfo)
    {
        if (osMonitor == null)
        {
            OSType osType = OSType.getOSType();
            if (OSType.Linux.equals(osType))
                osMonitor = new LinuxMonitor();
            else if (OSType.Windows.equals(osType))
                osMonitor = new WindowsMonitor();
            else
                throw new RuntimeException("The os is unknown! ");
        }
        osMonitor.monitor(monitorInfo);
    }
}
