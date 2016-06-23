package org.dangcat.boot.statistics;

import org.dangcat.commons.utils.ValueUtils;

/**
 * ���ݴ���ͳ�ơ�
 *
 * @author dangcat
 */
public abstract class StatisticsBasic implements StatisticsAble {
    /**
     * ʵʱ����
     */
    private static final String STATISTICS_REAL = "Real";
    /**
     * �ϼ�����
     */
    private static final String STATISTICS_TOTAL = "Total";
    /**
     * ͳ�����ơ�
     */
    private String name;
    /**
     * ʵʱ����
     */
    private StatisticsBase realData = null;
    /**
     * �ܼ����ݡ�
     */
    private StatisticsBase totalData = null;

    public StatisticsBasic(String name) {
        this.name = name;
    }

    public long begin() {
        return this.begin(StatisticsBase.TimeCost);
    }

    public long begin(String name) {
        return this.getRealData().begin(name);
    }

    protected abstract StatisticsBase creatStatisticsData(String name);

    public void end() {
        this.getRealData().end(StatisticsBase.TimeCost);
    }

    public void end(long beginTime) {
        this.getRealData().end(StatisticsBase.TimeCost, beginTime);
    }

    public void end(String name) {
        this.getRealData().end(name);
    }

    public void end(String name, long beginTime) {
        this.getRealData().end(name, beginTime);
    }

    public String getName() {
        return this.name;
    }

    protected StatisticsBase getRealData() {
        if (this.realData == null)
            this.realData = this.creatStatisticsData(STATISTICS_REAL);
        return this.realData;
    }

    protected StatisticsBase getTotalData() {
        if (this.totalData == null)
            this.totalData = this.creatStatisticsData(STATISTICS_TOTAL);
        return this.totalData;
    }

    public long getValue(String name) {
        return this.getRealData().getValue(name);
    }

    protected long increase(String name) {
        return this.getRealData().increase(name);
    }

    protected long increase(String name, long value) {
        return this.getRealData().increase(name, value);
    }

    @Override
    public boolean isValid() {
        return this.getTotalData().isValid() || this.getRealData().isValid();
    }

    @Override
    public String readRealInfo() {
        StatisticsBase realData = this.getRealData();
        this.realData = null;
        this.getTotalData().assign(realData);
        return this.toString(realData, this.getTotalData());
    }

    @Override
    public void reset() {
        this.realData = null;
        this.totalData = null;
    }

    protected long setValue(String name, long value) {
        return this.getRealData().setValue(name, value);
    }

    /**
     * ��ʾͳ����Ϣ��
     */
    @Override
    public String toString() {
        return this.toString(this.getRealData(), this.getTotalData());
    }

    private String toString(StatisticsBase realData, StatisticsBase totalData) {
        StringBuilder info = new StringBuilder();
        // ͳ���������ơ�
        info.append(this.getName());
        info.append(": ");
        String realDataText = realData.toString();
        if (!ValueUtils.isEmpty(realDataText)) {
            info.append(realDataText);
            info.append("\r\n\t");
        }
        info.append(totalData);
        return info.toString();
    }
}
