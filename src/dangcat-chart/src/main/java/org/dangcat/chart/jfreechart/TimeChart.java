package org.dangcat.chart.jfreechart;

import org.dangcat.chart.jfreechart.data.DataModule;
import org.dangcat.commons.utils.DateUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class TimeChart extends AxisChart {
    protected static final Double ZREO = new Double(0);
    /**
     * ��ʼ��ɫ��
     */
    private int beginColorIndex = 0;
    /**
     * ��ʼʱ�䡣
     */
    private Date beginTime = null;
    /**
     * ��ֹʱ�䡣
     */
    private Date endTime = null;
    /**
     * ������Ӳ�����
     */
    private int timeStep = 0;
    /**
     * ��ʾ��塣
     */
    private XYPlot xyPlot = null;

    /**
     * ��ʼ��ͳ�ƶ���
     */
    @Override
    protected JFreeChart createChart() {
        // ��ͼ����
        XYPlot xyPlot = new XYPlot();
        // ��������⡣
        xyPlot.setDomainAxis(new DateAxis(this.getDomainTitle()));
        // ��������⡣
        xyPlot.setRangeAxis(new NumberAxis(this.getRangeTitle()));
        // ����ͼ�ı���ģ�顣
        xyPlot.setRenderer(this.createXYItemRenderer());
        // ����ͳ��ͼ����
        return new JFreeChart(xyPlot);
    }

    protected XYDataset createXYDataset() {
        return this.createXYDataset(null);
    }

    protected abstract XYDataset createXYDataset(Collection<Comparable<?>> rowKeys);

    protected abstract XYItemRenderer createXYItemRenderer();

    protected int getBeginColorIndex() {
        return beginColorIndex;
    }

    protected void setBeginColorIndex(int beginColorIndex) {
        this.beginColorIndex = beginColorIndex;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(this.getDatePattern());
    }

    /**
     * ʱ�䷶Χ��������
     */
    private long getDateLength() {
        return this.getDateRange() / 60 / 60 / 24;
    }

    /**
     * ���ڸ�ʽģ�档
     */
    private String getDatePattern() {
        // ʱ�䷶Χ��������
        long days = this.getDateLength();
        // ����ʱ��ģʽ��
        if (days >= 365)
            return "yy/MM";
        else if (days >= 28)
            return "MM-dd";
        else if (days > 7)
            return "MM-dd HH";
        else if (days == 7)
            return "E";
        return "HH:mm";
    }

    /**
     * ��ʼ�ͽ�ֹʱ�䳤�ȣ���λ�롣
     */
    private long getDateRange() {
        Long beginTime = this.getBeginTime().getTime();
        Long endTime = this.getEndTime().getTime();
        return (endTime - beginTime) / 1000;
    }

    /**
     * ��ÿ̶ȵ�Ԫ��
     *
     * @return
     */
    private DateTickUnit getDateTickUnit() {
        DateTickUnit dateTickUnit = null;
        // ʱ�䷶Χ��������
        long days = this.getDateLength();
        if (days >= 365)
            dateTickUnit = new DateTickUnit(DateTickUnitType.MONTH, 1);
        else {
            int domainAxisStep = (int) this.getDomainAxisStep();
            // ����������ǩ������
            int muniteStep = domainAxisStep / 60;
            if (muniteStep == 0)
                dateTickUnit = new DateTickUnit(DateTickUnitType.SECOND, domainAxisStep);
            else
                dateTickUnit = new DateTickUnit(DateTickUnitType.MINUTE, muniteStep);
        }
        return dateTickUnit;
    }

    /**
     * ��������경������λ�롣
     *
     * @return ���경����
     */
    private long getDomainAxisStep() {
        // ʱ�䷶Χ��������
        long days = this.getDateLength();
        // ����ʱ��ģʽ��
        if (days == 7)
            this.setDomainAxiaCount(7);
        // ����������ǩ������
        long domainAxisStep = this.getDateRange() / this.getDomainAxiaCount();
        if (domainAxisStep % 60 != 0)
            domainAxisStep = (domainAxisStep / 60 + 1) * 60;
        return domainAxisStep;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    protected Map<Date, Double> getTimeData(Comparable<?> rowKey) {
        return this.getTimeData(rowKey, 0);
    }

    protected Map<Date, Double> getTimeData(Comparable<?> rowKey, int index) {
        DataModule dataModule = this.getDataModule();
        Map<Date, Double> valueMap = new LinkedHashMap<Date, Double>();
        for (Comparable<?> columnKey : dataModule.getColumnKeys(rowKey)) {
            double value = this.getDataConverter(index).getValue(rowKey, columnKey);
            valueMap.put((Date) columnKey, value);
        }
        ChartUtils.fixValueMap(this, valueMap);
        return valueMap;
    }

    public int getTimeStep() {
        return this.timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

    protected XYPlot getXYPlot() {
        if (this.xyPlot != null)
            return this.xyPlot;
        return (XYPlot) this.getChart().getPlot();
    }

    protected void setXYPlot(XYPlot xyPlot) {
        this.xyPlot = xyPlot;
    }

    protected void iniItemRenderer(XYItemRenderer xyItemRenderer, int i) {
        CustomXYItemLabelGenerator customXYItemLabelGenerator = new CustomXYItemLabelGenerator(this, i);
        // ������ʾ��
        xyItemRenderer.setBaseToolTipGenerator(customXYItemLabelGenerator);
        // �����ȵ�����
        xyItemRenderer.setURLGenerator(customXYItemLabelGenerator);
    }

    /**
     * ���ù̶���ɫ��Χ��
     */
    protected void initAxis(XYPlot xyPlot) {
        for (int i = 0; i < xyPlot.getDomainAxisCount(); i++) {
            DateAxis domainAxis = (DateAxis) xyPlot.getDomainAxis(i);
            this.initDomainAxis(domainAxis);
            // ��ʼʱ�䡣
            domainAxis.setMinimumDate(DateUtils.add(DateUtils.SECOND, this.getBeginTime(), -1));
            // ��ֹʱ�䡣
            domainAxis.setMaximumDate(DateUtils.add(DateUtils.SECOND, this.getEndTime(), 1));
            // ��������⡣
            domainAxis.setLabel(this.getDomainTitle(i));
        }

        for (int i = 0; i < xyPlot.getRangeAxisCount(); i++) {
            NumberAxis rangeAxis = (NumberAxis) xyPlot.getRangeAxis(i);
            if (rangeAxis != null) {
                this.initRangeAxis(rangeAxis);
                // ����Y������ֵ
                rangeAxis.setUpperBound(this.getMaxValue(i));
                // ����Y�����Сֵ
                rangeAxis.setLowerBound(this.getMinValue(i));
                // ��������⡣
                rangeAxis.setLabel(this.getRangeTitle(i));
            }
        }
    }

    /**
     * ��ʼ�������ꡣ
     *
     * @param domainAxis
     */
    protected void initDomainAxis(DateAxis domainAxis) {
        this.initAxis(domainAxis);
        // �趨���ڸ�ʽ��
        domainAxis.setDateFormatOverride(this.getDateFormat());
        // ���ÿ̶ȵ�Ԫ��
        domainAxis.setTickUnit(this.getDateTickUnit());
        domainAxis.setTickMarkPosition(DateTickMarkPosition.START);
        // �Ƿ��Զ�ѡ��̶ȡ�
        domainAxis.setAutoTickUnitSelection(false);
    }

    @Override
    protected void initPlot(Plot plot) {
        super.initPlot(plot);
        XYPlot xyPlot = (XYPlot) plot;
        // �������ɫ��
        xyPlot.setDomainGridlinePaint(Color.BLACK);
        // ����߿ɼ���
        xyPlot.setDomainGridlinesVisible(true);
        // ����������ɫ��
        xyPlot.setRangeGridlinePaint(Color.BLACK);
        xyPlot.setRangeCrosshairVisible(true);
        // ����ƫ�ƾ��롣
        xyPlot.setAxisOffset(new RectangleInsets(2.0, 2.0, 2.0, 2.0));
        // ���ù̶���ɫ��Χ��
        this.initAxis(xyPlot);

        this.initSeriesPaint();
    }

    /**
     * ��ʼ�������ꡣ
     *
     * @param rangeAxis
     */
    protected void initRangeAxis(NumberAxis rangeAxis) {
        this.initAxis(rangeAxis);
        // ������ߵ�һ�� Item ��ͼƬ���˵ľ���
        rangeAxis.setUpperMargin(0.15);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // �趨��������ʾ��ʽ��
        rangeAxis.setNumberFormatOverride(new DecimalFormat("#.###"));
        // ������ߵ�һ�� Item ��ͼƬ���˵ľ���
        rangeAxis.setUpperMargin(0.15);
    }

    protected void initSeriesPaint() {
        XYPlot xyPlot = this.getXYPlot();
        for (int i = 0; i < xyPlot.getRendererCount(); i++) {
            XYItemRenderer xyItemRenderer = xyPlot.getRenderer(i);
            if (xyItemRenderer != null) {
                this.iniItemRenderer(xyItemRenderer, i);
                for (int k = 0; k < ColorFactory.MAX_ITEM; k++) {
                    int index = k + i + this.beginColorIndex;
                    Color color = ColorFactory.sequence(index);
                    if (color != null)
                        xyItemRenderer.setSeriesPaint(k, color);
                }
            }
        }
    }

    protected void setXYDataset(XYDataset dataset) {
        XYPlot xyPlot = this.getXYPlot();
        xyPlot.setDataset(dataset);
        this.initPlot(xyPlot);
    }
}
