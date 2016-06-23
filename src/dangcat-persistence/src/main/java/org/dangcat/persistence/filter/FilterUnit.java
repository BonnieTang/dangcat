package org.dangcat.persistence.filter;

import org.dangcat.commons.utils.CloneAble;

/**
 * ���˱��ʽ��
 *
 * @author dangcat
 */
public class FilterUnit implements FilterExpress, ValueObject, CloneAble<FilterExpress> {
    private static final long serialVersionUID = 1L;
    /**
     * �ֶ�����
     */
    private String fieldName = null;
    /**
     * �������͡�
     */
    private FilterType filterType = null;
    /**
     * �Ƿ���NOT���㡣
     */
    private boolean not = false;
    /**
     * ����ֵ��
     */
    private Object[] params;
    /**
     * �������ơ�
     */
    private Object value;

    public FilterUnit() {
    }

    /**
     * ������˱��ʽ��
     *
     * @param fieldName  �ֶ�����
     * @param filterType �������͡�
     * @param not        ȡ�����ˡ�
     * @param params     ���˲�����
     */
    public FilterUnit(String fieldName, FilterType filterType, Object... values) {
        this.fieldName = fieldName;
        this.filterType = filterType;
        this.params = values;
    }

    @Override
    public FilterExpress clone() {
        FilterUnit filterUnit = new FilterUnit(this.getFieldName(), this.getFilterType(), this.getParams());
        filterUnit.setNot(this.isNot());
        filterUnit.setValue(this.getValue());
        return filterUnit;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FilterType getFilterType() {
        return this.filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public Object[] getParams() {
        return this.params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    /**
     * �ж������Ƿ�����Ҫ��
     *
     * @param instance ���ݶ���
     * @return ������Ϊtrue������Ϊfalse��
     */
    public boolean isValid(Object instance) {
        Object value = FilterUtils.getValue(instance, this.getFieldName());
        if (value == null)
            value = instance;
        return this.isValidData(value);
    }

    /**
     * �ж��������Ƿ�����Ҫ��
     *
     * @param field �����ֶΡ�
     * @return ������Ϊtrue������Ϊfalse��
     */
    private boolean isValidData(Object value) {
        Filter filter = FilterFactory.getInstance().getFilter(this);
        if (filter != null)
            return filter.isValid(this.params, value);
        return false;
    }

    /**
     * ת��SQL�����䡣
     */
    @Override
    public String toString() {
        if (this.params != null && this.params.length > 0) {
            Filter filter = FilterFactory.getInstance().getFilter(this);
            if (filter != null)
                return filter.toSql(this.fieldName, this.params);
        }
        return null;
    }
}
