package org.dangcat.chart.jfreechart;

import java.util.Date;

import javax.swing.JTabbedPane;

import org.dangcat.chart.jfreechart.AreaChart;
import org.dangcat.chart.jfreechart.data.DataModule;
import org.dangcat.persistence.filter.FilterType;
import org.dangcat.persistence.filter.FilterUnit;

public class AreaChartDemo extends ChartDemoBase
{
    private static final long serialVersionUID = 1L;

    public static void main(final String[] args)
    {
        show(new AreaChartDemo());
    }

    public AreaChartDemo()
    {
        super(AreaChart.class.getSimpleName());
    }

    @Override
    protected void createTabbedPane(JTabbedPane tabbedPane)
    {
        Date[] dates = SimulateTimeData.today();
        DataModule columnDataModule = SimulateTimeData.createMultiColumnDataModule();
        columnDataModule.getDataReader().setFilterExpress(new FilterUnit(SimulateCategoryData.Name, FilterType.eq, "Name 2"));
        AreaChart areaChart1 = new AreaChart();
        areaChart1.setTitle("����ͳ��ͼ");
        areaChart1.setRangeTitle("����");
        areaChart1.setDataModule(columnDataModule);
        this.initTimeChart(areaChart1, dates[0], dates[1]);
        areaChart1.initialize();
        tabbedPane.add("Column Module Data", this.createChartPanel(areaChart1.getChart()));
        this.renderFile(areaChart1, "AreaChart1");

        DataModule rowDataModule = SimulateTimeData.createMultiRowDataModule();
        rowDataModule.getDataReader().setFilterExpress(new FilterUnit(SimulateCategoryData.Name, FilterType.eq, "Name 8"));
        AreaChart areaChart2 = new AreaChart();
        areaChart2.setLegendVisible(true);
        areaChart2.setTitle("����ͳ��ͼ");
        areaChart2.setRangeTitle("����");
        areaChart2.setDataModule(rowDataModule);
        this.initTimeChart(areaChart2, dates[0], dates[1]);
        areaChart2.initialize();
        tabbedPane.add("Row Module Data", this.createChartPanel(areaChart2.getChart()));
        this.renderFile(areaChart2, "AreaChart2");
    }
}
