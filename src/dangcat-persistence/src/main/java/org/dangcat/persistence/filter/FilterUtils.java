package org.dangcat.persistence.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.persistence.ValueReader;
import org.dangcat.persistence.entity.EntityField;
import org.dangcat.persistence.entity.EntityHelper;
import org.dangcat.persistence.entity.EntityMetaData;
import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Table;

public class FilterUtils
{
    /**
     * �����ݱ������ϸ��Ĺ���������
     * @param table ���ݱ�
     * @param fieldNames ��λ���ơ�
     * @param beginIndex ��ʼλ�á�
     * @param count ��Ҫ��ȡ��������
     * @return ����������
     */
    public static FilterExpress createEqualsFilter(Table table, String[] fieldNames, Integer beginIndex, Integer count)
    {
        if (table.getRows().size() == 0)
            return null;
        if (beginIndex == null)
            beginIndex = 0;
        if (count == null || beginIndex + count > table.getRows().size())
            count = table.getRows().size() - beginIndex;

        FilterExpress filterExpress = null;
        if (fieldNames.length == 1)
        {
            String fieldName = fieldNames[0];
            List<Object> valueList = new ArrayList<Object>();
            for (int i = beginIndex; i < count; i++)
            {
                Object value = table.getRows().get(i).getField(fieldName).getObject();
                if (value != null)
                    valueList.add(value);
            }
            filterExpress = new FilterUnit(fieldName, FilterType.eq, valueList.toArray());
        }
        else if (fieldNames.length > 1)
        {
            FilterGroup filterGroup = new FilterGroup();
            filterGroup.setGroupType(FilterGroupType.or);
            for (int i = beginIndex; i < count; i++)
            {
                FilterGroup filterGroupUnit = new FilterGroup();
                for (String fieldName : fieldNames)
                {
                    Object value = table.getRows().get(i).getField(fieldName).getObject();
                    if (value != null)
                        filterGroupUnit.add(fieldName, FilterType.eq, value);
                }
                filterGroup.add(filterGroupUnit);
            }
            filterExpress = filterGroup;
        }
        return filterExpress;
    }

    /**
     * �����ݱ������ϸ��Ĺ���������
     * @param table ���ݱ�
     * @param beginIndex ��ʼλ�á�
     * @param count ��Ҫ��ȡ��������
     * @return ����������
     */
    public static FilterExpress createPrimaryFilter(Table table, Integer beginIndex, Integer count)
    {
        String[] primaryFieldNames = findPrimaryFieldNames(table);
        return createEqualsFilter(table, primaryFieldNames, beginIndex, count);
    }

    /**
     * �������ڷ�Χ�Ĺ���������
     * @param fieldNames �ֶ����ơ�
     * @param values ��ֵ���顣
     * @param filterType �������͡�
     * @return �����Ĺ��˶���
     */
    public static FilterExpress createRangeFilter(String[] fieldNames, Object[] values, FilterType filterType)
    {
        if (fieldNames.length == 1)
            return new FilterUnit(fieldNames[0], filterType, values);

        FilterType transFilterType = filterType;
        if (FilterType.ge.equals(filterType))
            transFilterType = FilterType.gt;
        else if (FilterType.le.equals(filterType))
            transFilterType = FilterType.lt;
        else if (!FilterType.gt.equals(filterType) && !FilterType.lt.equals(filterType))
            return null;

        FilterGroup filterGroup = new FilterGroup();
        filterGroup.setGroupType(FilterGroupType.or);
        for (int i = 0; i < fieldNames.length; i++)
        {
            if (i == 0)
                filterGroup.add(fieldNames[i], transFilterType, values[i]);
            else
            {
                FilterGroup childFilterGroup = new FilterGroup();
                for (int j = 0; j < i; j++)
                    childFilterGroup.add(fieldNames[j], FilterType.eq, values[j]);
                if (i == fieldNames.length - 1)
                    childFilterGroup.add(fieldNames[i], filterType, values[i]);
                else
                    childFilterGroup.add(fieldNames[i], transFilterType, values[i]);
                filterGroup.add(childFilterGroup);
            }
        }
        return filterGroup;
    }

    /**
     * �ҵ��ų�������λ��������λ��
     * @param table
     * @return
     */
    private static String[] findPrimaryFieldNames(Table table)
    {
        List<String> fieldNameList = new ArrayList<String>();
        for (Column column : table.getColumns().getPrimaryKeys())
        {
            if (column.isCalculate())
                continue;
            fieldNameList.add(column.getFieldName());
        }
        return fieldNameList.toArray(new String[0]);
    }

    /**
     * ��ȡ�����ָ���ֶ�ֵ��
     * @param data ָ������
     * @param fieldName �ֶ�����
     * @return ��ȡ��ֵ��
     */
    @SuppressWarnings("unchecked")
    public static Object getValue(Object instance, String fieldName)
    {
        Object value = null;
        if (instance instanceof ValueReader)
        {
            ValueReader valueReader = (ValueReader) instance;
            value = valueReader.getValue(fieldName);
        }
        else if (instance instanceof Map<?, ?>)
        {
            Map<String, Object> map = (Map<String, Object>) instance;
            if (map.containsKey(fieldName))
                value = map.get(fieldName);
        }
        else
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(instance);
            if (entityMetaData != null)
            {
                EntityField entityField = entityMetaData.getEntityField(fieldName);
                if (entityField != null)
                    value = entityField.getValue(instance);
            }
            else if (instance != null && !ReflectUtils.isConstClassType(instance.getClass()))
                value = ReflectUtils.getFieldValue(instance, fieldName);
        }
        return value;
    }

    /**
     * ��ȡ�෴�Ĺ���������
     * @param filterUnit ���˶���
     * @return
     */
    public static FilterType transFilterType(FilterUnit filterUnit)
    {
        if (!filterUnit.isNot())
            return filterUnit.getFilterType();

        FilterType filterType = null;
        if (FilterType.ge.equals(filterUnit.getFilterType()))
            filterType = FilterType.lt;
        else if (FilterType.gt.equals(filterUnit.getFilterType()))
            filterType = FilterType.le;
        else if (FilterType.le.equals(filterUnit.getFilterType()))
            filterType = FilterType.gt;
        else if (FilterType.lt.equals(filterUnit.getFilterType()))
            filterType = FilterType.ge;
        return filterType;
    }
}
