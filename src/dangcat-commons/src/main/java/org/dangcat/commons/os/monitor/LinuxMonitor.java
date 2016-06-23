package org.dangcat.commons.os.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;

import org.dangcat.commons.utils.CommandExecutor;
import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;

/**
 * Linuxϵͳ��Դ��ء�
 * @author dangcat
 * 
 */
class LinuxMonitor extends OSMonitor
{
    /** ���̵�CPUռ���ʡ�����ռ�õ��ڴ档 */
    private static final String MONITOR_PROCESS_CMD = "ps -eo pid,rss,pcpu | grep '' *{0}''";
    /** �ܵ�CPU�����ʡ� */
    private static final String MONITOR_TOTALCPU_CMD = "top -b -n 1 | grep ^Cpu | awk '{print $5}' | sed -e 's/%.*'//g";
    /** �������ڴ档 �Ѿ�ʹ�õ������ڴ档 */
    private static final String MONITOR_TOTALMEM_CMD = "free | grep ^Mem: | awk '{print $2 \"\\t\" $3 \"\\t\" $6 \"\\t\" $7}'";

    private String[] calculate(String command)
    {
        String[] values = null;
        try
        {
            String info = CommandExecutor.execute(command);
            BufferedReader bufferedReader = new BufferedReader(new StringReader(info));
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line == null || line.trim().length() == 0)
                    continue;

                line = line.replaceAll("\t", " ");
                while (line.indexOf("  ") != -1)
                    line = line.replaceAll("  ", " ");
                values = line.split(" ");
            }
        }
        catch (IOException e)
        {

        }
        return values;
    }

    /**
     * ���CPU��
     */
    @Override
    protected void monitorCPU(MonitorInfo monitorInfo)
    {
        this.monitorProcessCPU(monitorInfo);
        this.monitorTotalCPU(monitorInfo);
    }

    /**
     * ����ڴ档
     */
    @Override
    protected void monitorMemory(MonitorInfo monitorInfo)
    {
        String[] memoryValues = calculate(MONITOR_TOTALMEM_CMD);
        long totalPhysicalMemory = ValueUtils.parseLong(memoryValues[0]);
        monitorInfo.setValue(MonitorInfo.TotalPhysicalMemory, totalPhysicalMemory);
        long usedMemoryValue = ValueUtils.parseLong(memoryValues[1]);
        long buffersMemoryValue = ValueUtils.parseLong(memoryValues[2]);
        long cachedMemoryValue = ValueUtils.parseLong(memoryValues[3]);
        monitorInfo.setValue(MonitorInfo.TotalUsageMemory, (usedMemoryValue - buffersMemoryValue - cachedMemoryValue) * 1024);
    }

    /**
     * ��ؽ��̵�CPU���ú��ڴ�ռ���ʡ�
     */
    private void monitorProcessCPU(MonitorInfo monitorInfo)
    {
        Integer currentPID = Environment.getCurrentPID();
        String command = MessageFormat.format(MONITOR_PROCESS_CMD, currentPID.toString());
        String[] processValues = this.calculate(command);
        // �����ڴ�ռ������
        long processUsageMemory = ValueUtils.parseLong(processValues[1]);
        monitorInfo.setValue(MonitorInfo.ProcessUsageMemory, processUsageMemory);
        // ����CPUռ����
        double processCpuRatio = ValueUtils.parseDouble(processValues[2]);
        monitorInfo.setValue(MonitorInfo.ProcessCpuRatio, processCpuRatio);
    }

    /**
     * ����ܵ�CPU�����ʡ�
     */
    private void monitorTotalCPU(MonitorInfo monitorInfo)
    {
        String[] totalCpuValues = this.calculate(MONITOR_TOTALCPU_CMD);
        // CPU������
        double idleCpuRatio = ValueUtils.parseDouble(totalCpuValues[0]);
        monitorInfo.setValue(MonitorInfo.TotalCpuRatio, 100 - idleCpuRatio);
    }
}
