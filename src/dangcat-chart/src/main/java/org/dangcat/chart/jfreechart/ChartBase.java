package org.dangcat.chart.jfreechart;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.dangcat.chart.jfreechart.data.DataConverter;
import org.dangcat.chart.jfreechart.data.DataModule;
import org.dangcat.chart.jfreechart.theme.CustomChartTheme;
import org.dangcat.commons.resource.ResourceUtils;
import org.dangcat.commons.utils.ValueUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.TextTitle;

/**
 * ͳ��ͼ�λ��ࡣ
 * @author dangcat
 * 
 */
public abstract class ChartBase
{
    static
    {
        ChartFactory.setChartTheme(new CustomChartTheme("CN"));
    }
    /** ͳ�ƶ��� */
    private JFreeChart chart;
    /** �ؼ����塣 */
    private Font chartFont;
    /** ����ת������ */
    private List<DataConverter> dataConverters = new LinkedList<DataConverter>();
    /** ������Դ�� */
    private DataModule dataModule = null;
    /** ͳ��ͼƬ�ĸ߶ȡ� */
    private int height = 360;
    /** ����ͼƬ�ȵ���Ϣ�� */
    private String imageMap = null;
    /** ͼƬ�ȵ㡣 */
    private String imageMapId = null;
    /** ��ʾ��ǩ�Ƿ�ɼ��� */
    private boolean legendVisible = false;
    /** �Ƿ���ʾ��ǩ�� */
    private boolean showItemLabel = true;
    /** �������֡� */
    private String title;
    /** �������塣 */
    private Font titleFont;
    /** �����Ƿ�ɼ��� */
    private boolean titleVisible = true;
    /** ͳ��ͼƬ�Ŀ�ȡ� */
    private int width = 600;

    protected abstract JFreeChart createChart();

    private ChartRenderingInfo createChartRenderingInfo()
    {
        return new ChartRenderingInfo(new StandardEntityCollection());
    }

    protected DataConverter createDataConverter(boolean isStacked)
    {
        return this.createDataConverter(0, isStacked);
    }

    protected DataConverter createDataConverter(int i, boolean isStacked)
    {
        DataConverter dataConverter = this.getDataConverter(i);
        if (dataConverter == null)
        {
            dataConverter = new DataConverter(this.dataModule);
            this.putDataConverter(i, dataConverter);
            dataConverter.calculate(isStacked);
        }
        return dataConverter;
    }

    private void createImageMap(ChartRenderingInfo chartRenderingInfo)
    {
        // �����ȵ�����
        if (!ValueUtils.isEmpty(this.imageMapId))
        {
            CustomURLTagFragmentGenerator customURLTagFragmentGenerator = new CustomURLTagFragmentGenerator();
            this.imageMap = ChartUtilities.getImageMap(this.imageMapId, chartRenderingInfo, customURLTagFragmentGenerator, customURLTagFragmentGenerator);
        }
    }

    public JFreeChart getChart()
    {
        return this.chart;
    }

    public Font getChartFont()
    {
        if (this.chartFont == null)
        {
            String chartFont = ResourceUtils.getText(ChartBase.class, "ChartFont");
            this.chartFont = (Font) ValueUtils.parseValue(Font.class, chartFont);
        }
        return this.chartFont;
    }

    protected DataConverter getDataConverter(int i)
    {
        DataConverter dataConverter = null;
        if (i < this.dataConverters.size())
            dataConverter = this.dataConverters.get(i);
        return dataConverter;
    }

    public List<DataConverter> getDataConverters()
    {
        return dataConverters;
    }

    public DataModule getDataModule()
    {
        return this.dataModule;
    }

    public int getHeight()
    {
        return this.height;
    }

    public String getImageMap()
    {
        return imageMap;
    }

    public String getImageMapId()
    {
        return imageMapId;
    }

    public String getTitle()
    {
        return this.title;
    }

    public Font getTitleFont()
    {
        if (this.titleFont == null)
        {
            String titleFontText = ResourceUtils.getText(this.getClass(), "TitleFont");
            this.titleFont = (Font) ValueUtils.parseValue(Font.class, titleFontText);
        }
        return this.titleFont;
    }

    public int getWidth()
    {
        return this.width;
    }

    private void initChart(JFreeChart chart)
    {
        // ��ʾ��ǩ���Ƿ�ɼ���
        if (chart.getLegend() != null)
        {
            chart.getLegend().setVisible(this.isLegendVisible());
            Font chartFont = this.getChartFont();
            if (chartFont != null)
                chart.getLegend().setItemFont(this.getChartFont());
            // ���ñ�ע�ޱ߿�
            chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        }
        // ���ñ��⡣
        if (this.isTitleVisible())
        {
            TextTitle textTitle = new TextTitle();
            Font titleFont = this.getTitleFont();
            if (titleFont != null)
                textTitle.setFont(titleFont);
            textTitle.setText(this.getTitle());
            textTitle.setVisible(this.isTitleVisible());
            chart.setTitle(textTitle);
        }
        else
            chart.setTitle("");

        this.initPlot(chart.getPlot());
    }

    /**
     * ��ʼ��ͳ�ƶ���
     */
    public void initialize()
    {
        DataModule dataModule = this.getDataModule();
        if (dataModule != null)
            dataModule.initialize();
        this.chart = this.createChart();
        ChartUtilities.applyCurrentTheme(this.chart);
        this.initChart(this.chart);
        this.load();
    }

    protected void initPlot(Plot plot)
    {
        // ����ɫ��
        plot.setBackgroundPaint(Color.WHITE);
        // ���ð�͸��
        // plot.setForegroundAlpha(0.7f);
        Font chartFont = this.getChartFont();
        if (chartFont != null)
            plot.setNoDataMessageFont(chartFont);
        plot.setNoDataMessage(ResourceUtils.getText(this.getClass(), "NoDataMessage"));
    }

    public boolean isLegendVisible()
    {
        return this.legendVisible;
    }

    public boolean isShowItemLabel()
    {
        return this.showItemLabel;
    }

    public boolean isTitleVisible()
    {
        if (ValueUtils.isEmpty(this.getTitle()))
            return false;
        return this.titleVisible;
    }

    public abstract void load();

    protected void putDataConverter(int i, DataConverter dataConverter)
    {
        if (i < this.dataConverters.size())
            this.dataConverters.set(i, dataConverter);
        else
            this.dataConverters.add(dataConverter);
    }

    /**
     * ����ͳ��ͼƬ��
     * @throws IOException ����쳣��
     */
    public void render(File file) throws IOException
    {
        this.initialize();

        ChartRenderingInfo chartRenderingInfo = this.createChartRenderingInfo();
        ChartUtilities.saveChartAsJPEG(file, this.getChart(), this.getWidth(), this.getHeight(), chartRenderingInfo);
        this.createImageMap(chartRenderingInfo);
    }

    /**
     * ����ͳ��ͼƬ��
     * @throws IOException ����쳣��
     */
    public void render(OutputStream outStream) throws IOException
    {
        this.initialize();

        ChartRenderingInfo chartRenderingInfo = this.createChartRenderingInfo();
        ChartUtilities.writeChartAsJPEG(outStream, 1F, this.getChart(), this.getWidth(), this.getHeight(), chartRenderingInfo);
        this.createImageMap(chartRenderingInfo);
    }

    public void setChartFont(Font chartFont)
    {
        this.chartFont = chartFont;
    }

    protected void setDataConverter(DataConverter dataConverter)
    {
        this.putDataConverter(0, dataConverter);
    }

    public void setDataModule(DataModule dataModule)
    {
        this.dataModule = dataModule;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setImageMapId(String imageMapId)
    {
        this.imageMapId = imageMapId;
    }

    public void setLegendVisible(boolean legendVisible)
    {
        this.legendVisible = legendVisible;
    }

    public void setShowItemLabel(boolean showItemLabel)
    {
        this.showItemLabel = showItemLabel;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setTitleFont(Font titleFont)
    {
        this.titleFont = titleFont;
    }

    public void setTitleVisible(boolean titleVisible)
    {
        this.titleVisible = titleVisible;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}