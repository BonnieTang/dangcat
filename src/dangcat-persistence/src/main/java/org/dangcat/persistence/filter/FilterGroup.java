package org.dangcat.persistence.filter;

import java.util.ArrayList;
import java.util.List;

import org.dangcat.commons.utils.CloneAble;
import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;

/**
 * ������ϡ�
 * @author dangcat
 * 
 */
public class FilterGroup implements FilterExpress, ValueObject, CloneAble<FilterExpress>
{
    private static final long serialVersionUID = 1L;
    /**
     * �����ϡ�
     */
    private List<FilterExpress> filterExpressList = new ArrayList<FilterExpress>();
    /**
     * ������͡�
     */
    private FilterGroupType groupType = FilterGroupType.and;
    /**
     * �������ơ�
     */
    private Object value;

    /**
     * ���������ϣ�Ĭ�Ϲ�ϵ��AND��
     */
    public FilterGroup()
    {
    }

    /**
     * ͨ����Ϲ�ϵ�ͱ��ʽ���������ϡ�
     * @param groupType ��Ϲ�ϵ��
     * @param filterExpresses ���ʽ���顣
     */
    public FilterGroup(FilterGroupType groupType, FilterExpress... filterExpresses)
    {
        this.groupType = groupType;
        this.add(filterExpresses);
    }

    /**
     * ���������ϣ�Ĭ�Ϲ�ϵ��AND��
     * @param name ����������
     * @param groupType ��Ϲ�ϵ��
     */
    public FilterGroup(Object value, FilterGroupType groupType)
    {
        this.value = value;
        this.groupType = groupType;
    }

    /**
     * ��ӱ��ʽ��
     * @param filterExpresses ���ʽ���顣
     */
    public void add(FilterExpress... filterExpresses)
    {
        if (filterExpresses != null && filterExpresses.length > 0)
        {
            for (FilterExpress filterExpress : filterExpresses)
                this.filterExpressList.add(filterExpress);
        }
    }

    /**
     * ��ӱ��ʽ��
     * @param fieldName �ֶ�����
     * @param filterType �������͡�
     * @param params ���˲�����
     */
    public FilterUnit add(String fieldName, FilterType filterType, Object... params)
    {
        FilterUnit filterUnit = null;
        if (params != null && params.length > 0)
        {
            filterUnit = new FilterUnit(fieldName, filterType, params);
            this.filterExpressList.add(filterUnit);
        }
        return filterUnit;
    }

    /**
     * ������б��ʽ��
     */
    public void clear()
    {
        this.filterExpressList.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public FilterExpress clone()
    {
        FilterGroup clone = new FilterGroup();
        clone.setGroupType(this.getGroupType());
        clone.setValue(this.getValue());
        for (FilterExpress filterExpress : this.getFilterExpressList())
            clone.getFilterExpressList().add(((CloneAble<FilterExpress>) filterExpress).clone());
        return clone;
    }

    /**
     * �жϹ���������Ƿ�������˶���
     * @param filterExpress ���˶���
     */
    public boolean contains(FilterExpress filterExpress)
    {
        return this.filterExpressList.contains(filterExpress);
    }

    public List<FilterExpress> getFilterExpressList()
    {
        return this.filterExpressList;
    }

    public FilterGroupType getGroupType()
    {
        return this.groupType;
    }

    @Override
    public Object getValue()
    {
        return this.value;
    }

    /**
     * �ж������Ƿ�����Ҫ��
     * @param value ���ݶ���
     * @return ������Ϊtrue������Ϊfalse��
     */
    public boolean isValid(Object value)
    {
        boolean isValid = true;
        for (FilterExpress filterExpress : this.filterExpressList)
        {
            if (filterExpress.isValid(value))
            {
                if (this.groupType.equals(FilterGroupType.or))
                    return true;
            }
            else
            {
                if (this.groupType.equals(FilterGroupType.and))
                    return false;
                else if (isValid)
                    isValid = false;
            }
        }
        return isValid;
    }

    public void setGroupType(FilterGroupType groupType)
    {
        this.groupType = groupType;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    /**
     * ת��CASE�����䡣
     */
    public String toCaseExpress()
    {
        return FilterHelper.toCaseExpress(this);
    }

    /**
     * ת��SQL�����䡣
     */
    @Override
    public String toString()
    {
        int count = 0;
        StringBuilder info = new StringBuilder();
        for (FilterExpress filterExpress : this.getFilterExpressList())
        {
            String sql = filterExpress.toString();
            if (!ValueUtils.isEmpty(sql))
            {
                // Ϊ�˷�ֹ���̫����ÿ20����һ���С�
                if (count > 20)
                {
                    info.append(Environment.LINE_SEPARATOR);
                    count = 0;
                }

                if (info.length() > 0)
                {
                    info.append(" ");
                    info.append(this.groupType.name().toUpperCase());
                    info.append(" ");
                }
                // ����ǻ����㣬��Ҫ���ӹ�������б�����
                if (this.getGroupType() == FilterGroupType.or && filterExpress instanceof FilterGroup)
                    info.append("(");

                info.append(filterExpress);

                // ����ǻ����㣬��Ҫ���ӹ�������б�����
                if (this.getGroupType() == FilterGroupType.or && filterExpress instanceof FilterGroup)
                    info.append(")");

                count++;
            }
        }
        // ֻ���ڻ��ϵ���ұ��ʽ����һ��������²ż����ű�����
        if (info.length() > 0 && this.getFilterExpressList().size() > 1 && this.getGroupType() == FilterGroupType.or)
        {
            info.insert(0, "(");
            info.append(")");
        }
        return info.toString();
    }
}
