package org.dangcat.chart.jfreechart.theme;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.ui.RectangleInsets;

public class CustomChartTheme extends StandardChartTheme
{
    public static Color[] COLORS = { new Color(31, 129, 188), new Color(92, 92, 97), new Color(144, 237, 125), new Color(255, 188, 117), new Color(153, 158, 255), new Color(255, 117, 153),
            new Color(253, 236, 109), new Color(128, 133, 232), new Color(158, 90, 102), new Color(255, 204, 102) };

    private static final long serialVersionUID = 1L;
    private Font defaultFont = new Font("����", Font.PLAIN, 12);

    private Paint[] outLinePaintSequence = new Paint[] { Color.WHITE };

    public CustomChartTheme(String name)
    {
        super(name);
        this.initialize();
    }

    public DefaultDrawingSupplier getDefaultDrawingSupplier()
    {
        return new DefaultDrawingSupplier(COLORS, COLORS, outLinePaintSequence, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
    }

    public Font getDefaultFont()
    {
        return defaultFont;
    }

    public void initialize()
    {
        // ���ñ�������
        this.setExtraLargeFont(this.getDefaultFont());
        // ����ͼ��������
        this.setRegularFont(this.getDefaultFont());
        // �������������
        this.setLargeFont(this.getDefaultFont());
        this.setSmallFont(this.getDefaultFont());
        this.setTitlePaint(new Color(51, 51, 51));
        this.setSubtitlePaint(new Color(85, 85, 85));

        // ���ñ�ע
        this.setLegendBackgroundPaint(Color.WHITE);
        this.setLegendItemPaint(Color.BLACK);//
        this.setChartBackgroundPaint(Color.WHITE);

        this.setDrawingSupplier(this.getDefaultDrawingSupplier());
        // ��������
        this.setPlotBackgroundPaint(Color.WHITE);
        // ����������߿�
        // this.setPlotOutlinePaint(Color.WHITE);
        // ���ӱ�ǩ��ɫ
        this.setLabelLinkPaint(new Color(8, 55, 114));
        this.setLabelLinkStyle(PieLabelLinkStyle.CUBIC_CURVE);

        this.setAxisOffset(new RectangleInsets(5, 12, 5, 12));
        // X�����ᴹֱ������ɫ
        this.setDomainGridlinePaint(new Color(192, 208, 224));
        // Y������ˮƽ������ɫ
        this.setRangeGridlinePaint(new Color(192, 192, 192));

        this.setBaselinePaint(Color.WHITE);
        this.setCrosshairPaint(Color.BLUE);
        // ���������������ɫ
        this.setAxisLabelPaint(new Color(51, 51, 51));
        // �̶�����
        this.setTickLabelPaint(new Color(67, 67, 72));
        // ������״ͼ��Ⱦ
        this.setBarPainter(new StandardBarPainter());
        // XYBar ��Ⱦ
        this.setXYBarPainter(new StandardXYBarPainter());
        this.setItemLabelPaint(Color.black);
        // �¶ȼ�
        this.setThermometerPaint(Color.white);
    }

    public void setDefaultFont(Font defaultFont)
    {
        this.defaultFont = defaultFont;
    }
}
