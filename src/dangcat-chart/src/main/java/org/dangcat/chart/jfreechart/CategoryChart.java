package org.dangcat.chart.jfreechart;

import org.dangcat.chart.jfreechart.data.DataConverter;
import org.dangcat.chart.jfreechart.data.DataModule;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * ��״ͳ��ͼ��
 * @author dangcat
 * 
 */
public abstract class CategoryChart extends AxisChart
{
    /** ���ǩ�Ƿ�ɼ��� */
    private boolean domainAxisVisible = true;
    /** ͼ�巽�� */
    private PlotOrientation orientation = PlotOrientation.VERTICAL;
    /** �ݱ�ǩ�Ƿ�ɼ��� */
    private boolean rangeAxisVisible = true;
    /** �Ƿ���ʾ��ǩ�� */
    private boolean showItemLabel = true;

    protected CategoryDataset createCategoryDataset(DataConverter dataConverter)
    {
        DataModule dataModule = this.getDataModule();
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        for (Comparable<?> rowKey : dataModule.getRowKeys())
        {
            for (Comparable<?> columnKey : dataModule.getColumnKeys(rowKey))
            {
                Double value = dataConverter.getValue(rowKey, columnKey);
                categoryDataset.addValue(value, rowKey, columnKey);
            }
        }
        return categoryDataset;
    }

    protected abstract JFreeChart createChart();

    public PlotOrientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(PlotOrientation orientation) {
        this.orientation = orientation;
    }

    protected void iniItemRenderer(CategoryItemRenderer categoryItemRenderer, int i)
    {
        // ��ʾÿ��������ֵ�����޸ĸ���ֵ����������
        CustomCategoryItemLabelGenerator categoryItemLabelGenerator = new CustomCategoryItemLabelGenerator(this);
        categoryItemRenderer.setBaseItemLabelGenerator(categoryItemLabelGenerator);
        categoryItemRenderer.setBaseToolTipGenerator(categoryItemLabelGenerator);
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        categoryItemRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE2, TextAnchor.TOP_LEFT));
        // �����ȵ�����
        categoryItemRenderer.setBaseItemURLGenerator(categoryItemLabelGenerator);
    }

    protected void initDomainAxis(CategoryAxis domainAxis)
    {
        this.initAxis(domainAxis);
        // �������Ƿ�ɼ���
        domainAxis.setVisible(this.isDomainAxisVisible());
        // ��������⡣
        domainAxis.setLabel(this.getDomainTitle());
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
    }

    @Override
    protected void initPlot(Plot plot)
    {
        super.initPlot(plot);

        CategoryPlot categoryPlot = (CategoryPlot) plot;
        // �������ɫ��
        categoryPlot.setDomainGridlinePaint(Color.GRAY);
        // ����߿ɼ���
        categoryPlot.setDomainGridlinesVisible(true);
        // ����������ɫ��
        categoryPlot.setRangeGridlinePaint(Color.GRAY);
        // ����ƫ�ƾ��롣
        categoryPlot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        // ��������λ�á�
        categoryPlot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        // ��������λ�á�
        categoryPlot.setOrientation(this.getOrientation());
        // �趨�����ꡣ
        this.initDomainAxis(categoryPlot.getDomainAxis());
        // �趨�����ꡣ
        this.initRangeAxis((NumberAxis) categoryPlot.getRangeAxis());
        // ���ù̶���ɫ��Χ��
        for (int i = 0; i < categoryPlot.getRendererCount(); i++)
        {
            CategoryItemRenderer categoryItemRenderer = categoryPlot.getRenderer(i);
            this.iniItemRenderer(categoryItemRenderer, i);
            for (int k = 0; k < ColorFactory.MAX_ITEM; k++)
            {
                int index = k + i;
                if (index >= ColorFactory.MAX_ITEM)
                    index = index - ColorFactory.MAX_ITEM;
                Color color = ColorFactory.sequence(index);
                if (color != null)
                    categoryItemRenderer.setSeriesPaint(k, color);
            }
        }

        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        for (int i = 0; i < ColorFactory.MAX_ITEM; i++)
        {
            Color color = ColorFactory.sequence(i);
            if (color != null)
                renderer.setSeriesPaint(i, color);
        }
    }

    private void initRangeAxis(NumberAxis rangeAxis)
    {
        this.initAxis(rangeAxis);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // ������ߵ�һ����������ͼƬ���˵ľ��롣
        rangeAxis.setUpperMargin(0.15);
        // �趨��������ʾ��ʽ��
        rangeAxis.setNumberFormatOverride(new DecimalFormat("#.###"));
        // �������Ƿ�ɼ���
        rangeAxis.setVisible(this.isRangeAxisVisible());
        // ��������⡣
        rangeAxis.setLabel(this.getRangeTitle());
        // �趨�߽�ֵ��
        rangeAxis.setUpperBound(this.getMaxValue());
    }

    public boolean isDomainAxisVisible()
    {
        return this.domainAxisVisible;
    }

    public void setDomainAxisVisible(boolean domainAxisVisible)
    {
        this.domainAxisVisible = domainAxisVisible;
    }

    public boolean isRangeAxisVisible()
    {
        return this.rangeAxisVisible;
    }

    public void setRangeAxisVisible(boolean rangeAxisVisible)
    {
        this.rangeAxisVisible = rangeAxisVisible;
    }

    public boolean isShowItemLabel() {
        return this.showItemLabel;
    }

    public void setShowItemLabel(boolean showItemLabel)
    {
        this.showItemLabel = showItemLabel;
    }

    protected void setCategoryDataset(CategoryDataset categoryDataset) {
        CategoryPlot categoryPlot = (CategoryPlot) this.getChart().getPlot();
        categoryPlot.setDataset(categoryDataset);
        this.initPlot(categoryPlot);
    }
}
