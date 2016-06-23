package org.dangcat.commons.utils;

import org.dangcat.commons.formator.DateFormator;
import org.dangcat.commons.formator.DateType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * ����Э�����ߡ�
 */
public class DateUtils
{
    public final static int DAY = Calendar.DAY_OF_YEAR;
    public final static int HOUR = Calendar.HOUR_OF_DAY;
    public final static int MILLISECOND = Calendar.MILLISECOND;
    public final static int MINUTE = Calendar.MINUTE;
    public final static int MONTH = Calendar.MONTH;
    public final static int SECOND = Calendar.SECOND;
    public final static int WEEK = Calendar.WEEK_OF_MONTH;
    public final static int YEAR = Calendar.YEAR;
    private static Date currentDate = null;

    /**
     * ����ʱ�䡣
     * @return �������ʱ�䡣
     */
    public static Date add(int field, Date date, int value)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, value);
        return calendar.getTime();
    }

    public static Date clear(DateType dateType, Date date)
    {
        if (DateType.Day.equals(dateType))
            return clear(HOUR, date);
        if (DateType.Hour.equals(dateType))
            return clear(MINUTE, date);
        if (DateType.Minute.equals(dateType))
            return clear(SECOND, date);
        if (DateType.Second.equals(dateType))
            return clear(MILLISECOND, date);
        return date;
    }

    /**
     * ���ָ��ʱ�䲿�֡�
     * @return ��ת����ʱ�䡣
     */
    public static Date clear(int field, Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (field <= HOUR)
            calendar.set(Calendar.HOUR_OF_DAY, 0);
        if (field <= MINUTE)
            calendar.set(Calendar.MINUTE, 0);
        if (field <= SECOND)
            calendar.set(Calendar.SECOND, 0);
        if (field <= MILLISECOND)
            calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * �Ƚ�����ʱ���С��
     * @return ���ĺ�������
     */
    public static int compare(Date beginDate, Date endDate)
    {
        long beginTime = 0l;
        if (beginDate != null)
            beginTime = beginDate.getTime();
        long endTime = 0l;
        if (endDate != null)
            endTime = endDate.getTime();
        if (beginTime > endTime)
            return 1;
        else if (beginTime < endTime)
            return -1;
        return 0;
    }

    /**
     * ��ȡ��ǰʱ��ĺ�������
     */
    public static long currentTimeMillis()
    {
        return currentDate == null ? System.currentTimeMillis() : currentDate.getTime();
    }

    /**
     * ��������ʱ���ࡣ
     * @return ָ��ʱ�䵥λ��������
     */
    public static int diff(int field, Date beginDate, Date endDate)
    {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        if (field == YEAR)
            return endCalendar.get(field) - beginCalendar.get(field);

        if (field == MONTH)
        {
            int year = endCalendar.get(Calendar.YEAR) - beginCalendar.get(Calendar.YEAR);
            int month = endCalendar.get(Calendar.MONTH) - beginCalendar.get(Calendar.MONTH);
            return year * 12 + month;
        }

        int scale = 1;
        if (field <= DAY)
        {
            beginCalendar.set(Calendar.HOUR_OF_DAY, 0);
            endCalendar.set(Calendar.HOUR_OF_DAY, 0);
            scale *= 24;
        }
        if (field <= HOUR)
        {
            beginCalendar.set(Calendar.MINUTE, 0);
            endCalendar.set(Calendar.MINUTE, 0);
            scale *= 60;
        }
        if (field <= MINUTE)
        {
            beginCalendar.set(Calendar.SECOND, 0);
            endCalendar.set(Calendar.SECOND, 0);
            scale *= 60;
        }
        if (field <= SECOND)
        {
            beginCalendar.set(Calendar.MILLISECOND, 0);
            endCalendar.set(Calendar.MILLISECOND, 0);
            scale *= 1000;
        }
        return (int) ((endCalendar.getTimeInMillis() - beginCalendar.getTimeInMillis()) / scale);
    }

    /**
     * ��ʽ�����ڡ�
     * @param date ���ڶ���
     * @return
     */
    public static String format(Date date)
    {
        return DateFormator.getDateFormat(DateType.Full).format(date);
    }

    /**
     * ��ʽ�����ڡ�
     * @param date ���ڶ���
     * @param dateStyle �������͡�
     * @return
     */
    public static String format(Date date, DateType dateType)
    {
        if (dateType != null)
            return DateFormator.getDateFormat(dateType).format(date);
        return format(date);
    }

    /**
     * ��ʽ��ʱ���ʽ��
     * @param pattern ��ʽ��ģ�塣
     * @param dateTime Ŀ��ʱ�䡣
     */
    public static String format(String pattern, Date dateTime)
    {
        String text = pattern;
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            if (dateTime == null)
                dateTime = now();
            text = dateFormat.format(dateTime);
        }
        catch (Exception e)
        {
        }
        return text;
    }

    public static Date getFirstDay(Date dateTime, int field)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        calendar.set(field, calendar.getActualMinimum(field));
        return getFirstTimeOfDay(calendar.getTime());
    }

    /**
     * ȡ�·ݵĵ�һ�����ڡ�
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getFirstDayOfMonth(Date dateTime)
    {
        return getFirstDay(dateTime, Calendar.DAY_OF_MONTH);
    }

    /**
     * ȡһ�ܵĵ�һ�����ڡ�
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getFirstDayOfWeek(Date dateTime)
    {
        return getFirstDay(dateTime, Calendar.DAY_OF_WEEK);
    }

    /**
     * ȡһ��ĵ�һ�����ڡ�
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getFirstDayOfYear(Date dateTime)
    {
        return getFirstDay(dateTime, Calendar.DAY_OF_YEAR);
    }

    /**
     * ȡһ��Ŀ�ʼʱ�䡣
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getFirstTimeOfDay(Date dateTime)
    {
        return DateUtils.clear(DateUtils.HOUR, dateTime);
    }

    public static Date getLastDay(Date dateTime, int field)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        calendar.set(field, calendar.getActualMaximum(field));
        return getLastTimeOfDay(calendar.getTime());
    }

    /**
     * ȡ�·ݵ����һ�����ڡ�
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getLastDayOfMonth(Date dateTime)
    {
        return getLastDay(dateTime, Calendar.DAY_OF_MONTH);
    }

    /**
     * ȡһ�ܵ����һ�����ڡ�
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getLastDayOfWeek(Date dateTime)
    {
        return getLastDay(dateTime, Calendar.DAY_OF_WEEK);
    }

    /**
     * ȡһ������һ�����ڡ�
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getLastDayOfYear(Date dateTime)
    {
        return getLastDay(dateTime, Calendar.DAY_OF_YEAR);
    }

    /**
     * ȡһ������ʱ�䡣
     * @param dateTime ��ǰʱ�䡣
     * @return �õ���ʱ�䡣
     */
    public static Date getLastTimeOfDay(Date dateTime)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * �Ƿ������ꡣ
     * @param dateTime ��ǰʱ�䡣
     * @return
     */
    public static boolean isLeapYear(Date dateTime)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        return new GregorianCalendar().isLeapYear(calendar.get(Calendar.YEAR));
    }

    /**
     * ��ǰʱ�䡣
     * @return
     */
    public static Date now()
    {
        return currentDate == null ? Calendar.getInstance().getTime() : currentDate;
    }

    public static Date parse(DateType dateType, String text, Locale locale)
    {
        Date value = null;
        try
        {
            value = DateFormator.getDateFormat(dateType, locale).parse(text);
        }
        catch (Exception e)
        {
        }
        return value;
    }

    /**
     * ���ִ��������ڡ�
     * @param text �ִ����֡�
     * @param defaultValue Ĭ��ֵ��
     * @return ����ֵ��
     */
    public static Date parse(String text, Date defaultValue)
    {
        Date value = defaultValue;
        if (!ValueUtils.isEmpty(text))
        {
            Long longValue = ValueUtils.parseLong(text);
            if (longValue != null)
                return new Date(longValue.longValue());

            if (text.indexOf(DateType.GMT.name()) != -1)
                value = parse(DateType.GMT, text, Locale.US);
            if (value == null)
            {
                for (DateType dateType : DateType.getParseValues())
                {
                    value = parse(dateType, text, null);
                    if (value != null)
                        break;
                }
            }
        }
        return value;
    }

    public static Date parse(String pattern, String text, Date defaultValue)
    {
        Date value = defaultValue;
        if (!ValueUtils.isEmpty(text))
        {
            try
            {
                DateFormat dateFormat = new SimpleDateFormat(pattern);
                value = dateFormat.parse(text);
            }
            catch (ParseException e)
            {
            }
        }
        return value;
    }

    /**
     * ���õ�ǰʱ�䡣
     */
    public static void setCurrentDate(Date date)
    {
        if (Environment.isTestEnabled())
            currentDate = date;
    }
}
