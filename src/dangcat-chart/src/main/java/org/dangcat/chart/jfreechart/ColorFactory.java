package org.dangcat.chart.jfreechart;

import org.dangcat.chart.jfreechart.theme.CustomChartTheme;

import java.awt.*;

/**
 * ��ɫ������
 * @author dangcat
 * 
 */
public class ColorFactory
{
    /** ���ͼ����ֵ����ݡ� */
    public static final int MAX_ITEM = 10;
    /**
     * �̶���ɫ�б�
     */
    private static final Color[] colors = CustomChartTheme.COLORS;
    private static Paint[] paints = CustomChartTheme.COLORS;

    public static Color[] getAllColors()
    {
        return colors;
    }

    /**
     * ��ȡ��ɫ�ı��ʽ��
     */
    public static String getColorStyle(Color color)
    {
        String style = null;
        if (color != null)
        {
            String styleText = String.format("%1$#6x", color.getRGB());
            styleText = "#" + styleText.substring(styleText.length() - 6);
            style = styleText.toUpperCase();
        }
        return style;
    }

    public static Paint[] getPaints()
    {
        if (paints == null)
        {
            Paint[] paintArray = new Paint[colors.length];
            for (int i = 0; i < colors.length; i++)
                paintArray[i] = new GradientPaint(0.0F, 0.0F, colors[i], 0.0F, 0.0F, Color.white);
            paints = paintArray;
        }
        return paints;
    }

    /**
     * �������ȡ��ɫ��
     * @param index ���λ�á�
     */
    public static Color sequence(int index)
    {
        if (index < 0)
            index = 0;
        else if (index >= colors.length)
            index = index % colors.length;
        return colors[index];
    }
}
