package org.dangcat.persistence.entity;

import org.dangcat.persistence.filter.FilterExpress;

/**
 * ɾ��ʵ�������ġ�
 * @author dangcat
 * 
 */
public class DeleteEntityContext extends SaveEntityContext
{
    private Class<?> entityClass;
    private String[] fieldNames;
    private FilterExpress filterExpress = null;
    private Object[] values;

    /**
     * ���չ�������ɾ��ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     */
    public DeleteEntityContext(Class<?> entityClass, FilterExpress filterExpress)
    {
        this.entityClass = entityClass;
        this.filterExpress = filterExpress;
    }

    /**
     * ɾ��ָ��������ʵ�����
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param primaryKeyValues ����ֵ��
     */
    public DeleteEntityContext(Class<?> entityClass, Object... primaryKeyValues)
    {
        this(entityClass, null, primaryKeyValues);
    }

    /**
     * ɾ��ָ�����Եĵ�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param fieldNames �ֶ����б�
     * @param values ����ֵ��
     */
    public DeleteEntityContext(Class<?> entityClass, String[] fieldNames, Object... values)
    {
        this.entityClass = entityClass;
        this.fieldNames = fieldNames;
        this.values = values;
    }

    public Class<?> getEntityClass()
    {
        return this.entityClass;
    }

    public String[] getFieldNames()
    {
        return this.fieldNames;
    }

    public FilterExpress getFilterExpress()
    {
        return this.filterExpress;
    }

    public Object[] getValues()
    {
        return this.values;
    }

    @Override
    public void initialize()
    {
        if (this.filterExpress == null && this.values != null)
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(this.entityClass);
            String[] fieldNames = this.fieldNames;
            if (fieldNames == null)
                fieldNames = entityMetaData.getPrimaryKeyNames();
            this.filterExpress = entityMetaData.createFilterExpress(fieldNames, this.values);
        }
    }

    public void setFieldNames(String[] fieldNames)
    {
        this.fieldNames = fieldNames;
    }

    public void setFilterExpress(FilterExpress filterExpress)
    {
        this.filterExpress = filterExpress;
    }
}