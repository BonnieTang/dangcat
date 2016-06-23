package org.dangcat.chart.jfreechart;

import org.dangcat.commons.utils.DateUtils;
import org.dangcat.commons.utils.ValueUtils;

import java.util.*;

class ChartUtils {
    protected static final String NULL = "NULL";

    private static void fixFirstDate(TimeChart timeChart, Date firstDate, Map<Date, Double> valueMap) {
        // ����ʱ������
        int timeLength = (int) ((firstDate.getTime() - timeChart.getBeginTime().getTime()) / 1000);
        if (timeLength > 2 * timeChart.getTimeStep())// ������������û���������ÿա�
        {
            Date beforeFirstDate = DateUtils.add(DateUtils.SECOND, firstDate, -1);
            valueMap.put(beforeFirstDate, null);
        }
    }

    private static void fixLastDate(TimeChart timeChart, Date lastDate, Map<Date, Double> valueMap) {
        // ����ʱ������
        int timeLength = (int) ((timeChart.getEndTime().getTime() - lastDate.getTime()) / 1000);
        if (timeLength > 2 * timeChart.getTimeStep())// ������������û���������ÿա�
        {
            Date afterLastDate = DateUtils.add(DateUtils.SECOND, lastDate, 1);
            valueMap.put(afterLastDate, null);
        }
    }

    protected static void fixValueMap(TimeChart timeChart, Map<Date, Double> valueMap) {
        List<Date> dateList = new ArrayList<Date>();
        dateList.addAll(valueMap.keySet());
        Collections.sort(dateList);

        // �޸���ʼ�˵�����ڡ�
        fixFirstDate(timeChart, dateList.get(0), valueMap);

        for (int i = 1; i < dateList.size(); i++) {
            Date beginDate = dateList.get(i - 1);
            Date endDate = dateList.get(i);
            // ����ʱ������
            int timeLength = (int) ((endDate.getTime() - beginDate.getTime()) / 1000);
            if (timeLength > 2 * timeChart.getTimeStep())// ������������û���������ÿա�
            {
                Date afterBeginDate = DateUtils.add(DateUtils.SECOND, beginDate, 1);
                valueMap.put(afterBeginDate, null);
                Date beforeEndDate = DateUtils.add(DateUtils.SECOND, endDate, -1);
                valueMap.put(beforeEndDate, null);
            }
        }

        // �޸���ֹ�˵�����ڡ�
        fixLastDate(timeChart, dateList.get(dateList.size() - 1), valueMap);

        List<Map.Entry<Date, Double>> sortedList = new ArrayList<Map.Entry<Date, Double>>();
        sortedList.addAll(valueMap.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<Date, Double>>() {
            public int compare(Map.Entry<Date, Double> srcEntry, Map.Entry<Date, Double> dstEntry) {
                return ValueUtils.compare(srcEntry.getKey(), dstEntry.getKey());
            }
        });
        valueMap.clear();
        for (Map.Entry<Date, Double> entry : sortedList)
            valueMap.put(entry.getKey(), entry.getValue());
    }

    protected static double getMaxValue(double doubleValue) {
        double maxValue = Math.abs(doubleValue);
        if (ValueUtils.isZero(maxValue))
            return 0.0;

        double maxSize = Math.abs(maxValue) * 1.01;
        if (maxValue % 10 != 0)
            maxSize = maxValue * 1.1;
        if (maxSize < 1)
            maxSize = 1.0;
        if (maxSize > 1 && maxSize % 10 != 0) {
            double value = ((int) maxSize / 100) * 100;
            if (value > maxValue)
                maxSize = value;
            else {
                value = ((int) maxSize / 10) * 10;
                if (value > maxValue)
                    maxSize = value;
                else {
                    value = (int) maxSize;
                    if (value > maxValue)
                        maxSize = value;
                    else if (value + 1 > maxValue)
                        maxSize = value + 1;
                }
            }
        }
        return doubleValue > 0 ? maxSize : maxSize * -1;
    }

    protected static boolean isNull(Object value) {
        return value == null || NULL.equals(value);
    }
}
