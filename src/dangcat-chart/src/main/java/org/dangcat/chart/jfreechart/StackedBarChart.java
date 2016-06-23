package org.dangcat.chart.jfreechart;

import org.dangcat.chart.jfreechart.data.DataConverter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

/**
 * ��ջ��״ͳ��ͼ��
 * @author dangcat
 * 
 */
public class StackedBarChart extends CategoryChart
{
    @Override
    protected JFreeChart createChart()
    {
        // ����ͳ�ƶ���
        JFreeChart chart = ChartFactory.createStackedBarChart(this.getTitle(), // ����
                this.getDomainTitle(), // ��������⡣
                this.getRangeTitle(), // ��������⡣
                new DefaultCategoryDataset(), // ������Դ��
                this.getOrientation(), // ���귽��
                true, // include legend
                true, // �Ƿ���ʾ��
                false);
        return chart;
    }

    @Override
    protected void iniItemRenderer(CategoryItemRenderer categoryItemRenderer, int i)
    {
        super.iniItemRenderer(categoryItemRenderer, i);
        BarRenderer renderer = (BarRenderer) categoryItemRenderer;
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE1, TextAnchor.CENTER_RIGHT));
        renderer.setShadowVisible(false);
    }

    @Override
    public void load()
    {
        DataConverter dataConverter = this.createDataConverter(true);
        CategoryDataset categoryDataset = this.createCategoryDataset(dataConverter);
        if (categoryDataset != null)
            this.setCategoryDataset(categoryDataset);
    }
}
