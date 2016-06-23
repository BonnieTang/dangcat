package org.dangcat.chart.jfreechart;

import org.dangcat.chart.jfreechart.data.DataConverter;
import org.dangcat.chart.jfreechart.data.DataModule;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.Rotation;

import java.awt.*;

/**
 * ��״ͳ��ͼ��
 *
 * @author dangcat
 */
public class PieChart extends ChartBase {
    @Override
    protected JFreeChart createChart() {
        // ����ͳ�ƶ���
        JFreeChart chart = ChartFactory.createPieChart(this.getTitle(), // ����
                new DefaultPieDataset(), // ������Դ��
                true, // include legend
                true, // �Ƿ���ʾ��
                true);

        chart.setTextAntiAlias(false);
        // ����Legend��λ�á�
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        return chart;
    }

    @Override
    protected void initPlot(Plot plot) {
        super.initPlot(plot);
        // ���3D��ˮ����ͼ����
        PiePlot piePlot = (PiePlot) plot;
        // �̶�Բ�Ρ�
        piePlot.setCircular(true);
        // ��򲻿ɼ���
        piePlot.setOutlineVisible(false);
        // ���ÿ�ʼ�Ƕ�
        piePlot.setStartAngle(150D);
        // ���÷���Ϊ˳ʱ�뷽��
        piePlot.setDirection(Rotation.CLOCKWISE);
        // ����͸���ȣ�0.5FΪ��͸����1Ϊ��͸����0Ϊȫ͸��
        // piePlot.setBackgroundAlpha(0);
        // piePlot.setForegroundAlpha(0.7F);
        // ���������߿򲻿ɼ�
        piePlot.setSectionOutlinesVisible(false);
        // ���ñ�ǩ���塣
        Font chartFont = this.getChartFont();
        if (chartFont != null)
            piePlot.setLabelFont(chartFont);
        // ����������ǩ��ʾ��ʽ���ؼ��֣�ֵ(�ٷֱ�)
        CustomPieItemLabelGenerator customPieItemLabelGenerator = new CustomPieItemLabelGenerator(this);
        if (this.isShowItemLabel())
            piePlot.setLabelGenerator(customPieItemLabelGenerator);
        // ����������ʾ
        piePlot.setToolTipGenerator(customPieItemLabelGenerator);
        // �����ȵ�����
        piePlot.setURLGenerator(customPieItemLabelGenerator);
        piePlot.setLegendLabelGenerator(customPieItemLabelGenerator);
        // ȥ������ɫ
        piePlot.setLabelBackgroundPaint(null);
        // ȥ����Ӱ
        piePlot.setLabelShadowPaint(null);
        // ȥ���߿�
        piePlot.setLabelOutlinePaint(null);
        piePlot.setShadowPaint(null);
        // ͼ����״
        piePlot.setLegendItemShape(new Rectangle(10, 10));
    }

    private void initSectionPaint() {
        // ���ù̶���ɫ��Χ��
        PiePlot piePlot = (PiePlot) this.getChart().getPlot();
        DataModule dataModule = this.getDataModule();
        String[] labels = dataModule.getRowKeys().toArray(new String[0]);
        for (int i = 0; i < labels.length; i++) {
            Color color = ColorFactory.sequence(i);
            if (color != null)
                piePlot.setSectionPaint(labels[i], color);
        }
    }

    @Override
    public void load() {
        DataModule dataModule = this.getDataModule();

        DataConverter dataConverter = this.createDataConverter(false);
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (Comparable<?> rowKey : dataModule.getRowKeys()) {
            for (Comparable<?> columnKey : dataModule.getColumnKeys(rowKey)) {
                Double value = dataConverter.getValue(rowKey, columnKey);
                Comparable<?> key = null;
                if (!ChartUtils.isNull(rowKey) && !ChartUtils.isNull(columnKey))
                    key = dataModule.getLabel(rowKey, columnKey);
                else if (!ChartUtils.isNull(rowKey))
                    key = rowKey;
                else if (!ChartUtils.isNull(columnKey))
                    key = columnKey;
                pieDataset.setValue(key, value);
            }
        }
        PiePlot piePlot = (PiePlot) this.getChart().getPlot();
        piePlot.setDataset(pieDataset);

        this.initSectionPaint();
    }
}