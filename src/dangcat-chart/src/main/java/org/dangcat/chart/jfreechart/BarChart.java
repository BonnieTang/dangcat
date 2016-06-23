package org.dangcat.chart.jfreechart;

import org.dangcat.chart.jfreechart.data.DataConverter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.StandardGradientPaintTransformer;

/**
 * ��״ͳ��ͼ��
 * @author dangcat
 * 
 */
public class BarChart extends CategoryChart
{
    @Override
    protected JFreeChart createChart()
    {
        // ����ͳ�ƶ���
        JFreeChart chart = ChartFactory.createBarChart(this.getTitle(), // ����
                this.getDomainTitle(), // ��������⡣
                this.getRangeTitle(), // ��������⡣
                new DefaultCategoryDataset(), // ������Դ��
                PlotOrientation.VERTICAL, // ���귽��
                true, // include legend
                true, // �Ƿ���ʾ��
                false);

        // ����Legend��λ�á�
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        this.setShowItemLabel(false);
        this.setDomainAxisVisible(true);

        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        categoryPlot.setRenderer(new CustomBarRenderer(ColorFactory.getPaints()));
        return chart;
    }

    @Override
    protected void iniItemRenderer(CategoryItemRenderer categoryItemRenderer, int i)
    {
        super.iniItemRenderer(categoryItemRenderer, i);

        BarRenderer barRenderer = (BarRenderer) categoryItemRenderer;
        // �����ⲿ�߿ɼ�
        barRenderer.setDrawBarOutline(false);
        barRenderer.setItemLabelAnchorOffset(10D);
        barRenderer.setMaximumBarWidth(0.1);
        barRenderer.setItemMargin(0.01);
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_HORIZONTAL));
        barRenderer.setShadowVisible(false);
    }

    @Override
    public void load()
    {
        DataConverter dataConverter = this.createDataConverter(false);

        CategoryDataset categoryDataset = this.createCategoryDataset(dataConverter);
        if (categoryDataset != null)
            this.setCategoryDataset(categoryDataset);
    }
}
