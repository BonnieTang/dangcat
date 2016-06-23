package org.dangcat.boot.server.domain;

import org.dangcat.commons.formator.OctetsFormator;
import org.dangcat.commons.formator.PercentFormator;
import org.dangcat.commons.formator.ValueFormator;
import org.dangcat.commons.os.monitor.MonitorInfo;
import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.persistence.annotation.Column;
import org.dangcat.persistence.entity.EntityBase;

public class MonitorData extends EntityBase implements MonitorInfo {
    private static final long serialVersionUID = 1L;
    /**
     * ʣ��Ĵ��̿ռ䡣
     */
    @Column(index = 9, logic = "octets")
    private long freeDiskSpace;
    /**
     * �ٷ�����ʽ����
     */
    private PercentFormator percentFormator = new PercentFormator();
    /**
     * ���̵�CPUռ���ʡ�
     */
    @Column(index = 19, logic = "percent")
    private double processCpuRatio;
    /**
     * ����ռ�õ��ڴ档
     */
    @Column(index = 16, logic = "octets")
    private long processUsageMemory;
    /**
     * CPU�����ʡ�
     */
    @Column(index = 18, logic = "percent")
    private double totalCpuRatio;
    /**
     * ���̿ռ䡣
     */
    @Column(index = 10, logic = "octets")
    private long totalDiskSpace;
    /**
     * �������ڴ档
     */
    @Column(index = 13, logic = "octets")
    private long totalPhysicalMemory;
    /**
     * �Ѿ�ʹ�õ������ڴ档
     */
    @Column(index = 14, logic = "octets")
    private long totalUsageMemory;
    /**
     * ������ʽ����
     */
    private ValueFormator valueFormator = new OctetsFormator();

    private double calculatePercent(long used, long total) {
        if (total != 0)
            return used * 100.0 / total;
        return 0;
    }

    @Column(index = 11, logic = "octets", isCalculate = true)
    public long getDiskUsageSpace() {
        return this.totalDiskSpace - this.freeDiskSpace;
    }

    /**
     * ����ռ���ʡ�
     */
    @Column(index = 12, logic = "percent", isCalculate = true)
    public double getDiskUsageSpaceRatio() {
        return this.calculatePercent(this.getDiskUsageSpace(), this.totalDiskSpace);
    }

    public long getFreeDiskSpace() {
        return freeDiskSpace;
    }

    public void setFreeDiskSpace(long freeDiskSpace) {
        this.freeDiskSpace = freeDiskSpace;
    }

    public double getProcessCpuRatio() {
        return processCpuRatio;
    }

    public void setProcessCpuRatio(double processCpuRatio) {
        this.processCpuRatio = processCpuRatio;
    }

    public long getProcessUsageMemory() {
        return processUsageMemory;
    }

    public void setProcessUsageMemory(long processUsageMemory) {
        this.processUsageMemory = processUsageMemory;
    }

    /**
     * ���̵��ڴ������ʡ�
     */
    @Column(index = 17, logic = "percent", isCalculate = true)
    public double getProcessUsageMemoryRatio() {
        return this.calculatePercent(this.processUsageMemory, this.totalPhysicalMemory);
    }

    public double getTotalCpuRatio() {
        return totalCpuRatio;
    }

    public void setTotalCpuRatio(double totalCpuRatio) {
        this.totalCpuRatio = totalCpuRatio;
    }

    public long getTotalDiskSpace() {
        return totalDiskSpace;
    }

    public void setTotalDiskSpace(long totalDiskSpace) {
        this.totalDiskSpace = totalDiskSpace;
    }

    public long getTotalPhysicalMemory() {
        return totalPhysicalMemory;
    }

    public void setTotalPhysicalMemory(long totalPhysicalMemory) {
        this.totalPhysicalMemory = totalPhysicalMemory;
    }

    public long getTotalUsageMemory() {
        return totalUsageMemory;
    }

    public void setTotalUsageMemory(long totalUsageMemory) {
        this.totalUsageMemory = totalUsageMemory;
    }

    /**
     * �ܵ��ڴ������ʡ�
     */
    @Column(index = 15, logic = "percent", isCalculate = true)
    public double getTotalUsageMemoryRatio() {
        return this.calculatePercent(this.totalUsageMemory, this.totalPhysicalMemory);
    }

    @Override
    public Number getValue(String name) {
        return (Number) ReflectUtils.getProperty(this, name);
    }

    public String print() {
        StringBuilder info = new StringBuilder();
        info.append("TotalDiskSpace = ");
        info.append(this.valueFormator.format(this.getTotalDiskSpace()));
        info.append(", DiskUsageSpace = ");
        info.append(this.valueFormator.format(this.getDiskUsageSpace()));
        info.append(", DiskUsageSpaceRatio = ");
        info.append(this.percentFormator.format(this.getDiskUsageSpaceRatio()));
        info.append(", TotalPhysicalMemory = ");
        info.append(this.valueFormator.format(this.getTotalPhysicalMemory()));
        info.append(", TotalUsageMemory = ");
        info.append(this.valueFormator.format(this.getTotalUsageMemory()));
        info.append(", TotalUsageMemoryRatio = ");
        info.append(this.percentFormator.format(this.getTotalUsageMemoryRatio()));
        info.append(", ProcessUsageMemory = ");
        info.append(this.valueFormator.format(this.getProcessUsageMemory()));
        info.append(", ProcessUsageMemoryRatio = ");
        info.append(this.percentFormator.format(this.getProcessUsageMemoryRatio()));
        info.append(", TotalCpuRatio = ");
        info.append(this.percentFormator.format(this.getTotalCpuRatio()));
        info.append(", ProcessCpuRatio = ");
        info.append(this.percentFormator.format(this.getProcessCpuRatio()));
        return info.toString();
    }

    @Override
    public void setValue(String name, Number value) {
        ReflectUtils.setProperty(this, name, value);
    }

    @Override
    public String toString() {
        return this.print();
    }
}
