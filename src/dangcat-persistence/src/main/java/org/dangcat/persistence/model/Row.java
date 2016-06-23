package org.dangcat.persistence.model;

import org.dangcat.persistence.ValueReader;
import org.dangcat.persistence.ValueWriter;
import org.dangcat.persistence.event.TableEventAdapter;
import org.dangcat.persistence.tablename.DynamicTable;
import org.dangcat.persistence.tablename.TableName;

import java.util.ArrayList;

/**
 * �����ж���
 * @author dangcat
 * 
 */
public class Row extends ArrayList<Field> implements ValueWriter, ValueReader, java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    /** ������״̬��������ɾ�����޸ġ� */
    private DataState dataState = DataState.Browse;
    /** ������Ϣ�� */
    private String error;
    /** �����д��󼶱� */
    private String errorLevel;
    /** ����������� */
    private Table parent;
    /**
     * ���캯����
     * @param parent �������
     */
    public Row()
    {
    }

    /**
     * ���캯����
     * @param parent �������
     */
    public Row(Table parent)
    {
        this.parent = parent;
    }

    /**
     * �����µ�ʵ����
     *
     * @return
     */
    public static Row newInstance() {
        return new Row();
    }

    @Override
    public void clear()
    {
        for (Field field : this)
            field.setParent(null);
        super.clear();
    }

    private boolean containsNull(Object[] values)
    {
        if (values != null)
        {
            for (Object value : values)
            {
                if (value == null)
                    return true;
            }
        }
        return false;
    }

    /**
     * ��Ŀ���п�����ͬ���ֵ���λ��
     * @param dstRow Ŀ�������С�
     */
    public void copy(Row srcRow)
    {
        for (Column column : this.parent.getColumns())
        {
            Field srcField = srcRow.getField(column.getName());
            if (srcField != null)
            {
                Field dstField = this.getField(column.getName());
                dstField.setObject(srcField.getObject());
            }
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o != null && o instanceof Row)
        {
            Row srcRow = (Row) o;
            if (srcRow.size() != this.size())
                return false;
            if (srcRow.getParent() != null && this.getParent() != null)
            {
                if (!this.getTableName().equals(srcRow.getTableName()))
                    return false;

                Column[] primaryKeys = this.getParent().getColumns().getPrimaryKeys();
                for (Column column : primaryKeys)
                {
                    Field srcField = this.getField(column.getName());
                    Field dstField = srcRow.getField(column.getName());
                    if (srcField == null || srcField.compareTo(dstField) != 0)
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    public DataState getDataState()
    {
        return this.dataState;
    }

    public void setDataState(DataState dataState) {
        this.dataState = dataState;
        if (dataState == DataState.Browse || dataState == DataState.Insert) {
            for (Field field : this)
                field.setDataState(dataState);
        }
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorLevel()
    {
        return errorLevel;
    }

    public void setErrorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
    }

    /**
     * ͨ����λ����ֵ��õ�Ԫ���ݶ���
     * @param index ��λ����ֵ��
     * @return ��Ԫ���ݶ���
     */
    public Field getField(int index)
    {
        if (index >= 0 && index < this.size())
            return this.get(index);
        return null;
    }

    /**
     * ͨ����λ����õ�Ԫ���ݶ���
     * @param fieldName ��λ����
     * @return ��Ԫ���ݶ���
     */
    public Field getField(String fieldName)
    {
        int index = this.getParent().getColumns().indexOf(fieldName);
        if (index == -1)
            return null;
        return this.get(index);
    }

    /**
     * ��ȡ�ֶζ�����ֶ�������
     * @param field �ֶζ���
     * @return �ֶ�����
     */
    public String getFieldName(Field field)
    {
        int index = this.indexOf(field);
        if (index == -1)
            return null;
        return this.getParent().getColumns().get(index).getName();
    }

    public Integer getNum()
    {
        Column rowNumColumn = this.getParent().getColumns().getRowNumColumn();
        if (rowNumColumn == null)
            return null;
        return this.getValue(rowNumColumn.getName());
    }

    public void setNum(Integer value) {
        Column rowNumColumn = this.getParent().getColumns().getRowNumColumn();
        if (rowNumColumn != null)
            this.setValue(rowNumColumn.getName(), value);
    }

    public Table getParent()
    {
        return parent;
    }

    public void setParent(Table parent) {
        this.parent = parent;
    }

    /**
     * ��ȡ�����ֶ���ֵ��
     * @return ��ֵ���顣
     */
    public Object[] getPrimaryKeyValues()
    {
        Column[] primaryKeys = this.getParent().getColumns().getPrimaryKeys();
        Object[] values = new Object[primaryKeys.length];
        for (int i = 0; i < primaryKeys.length; i++)
            values[i] = this.getField(primaryKeys[i].getName()).getObject();
        return values;
    }

    public String getTableName()
    {
        TableName tableName = TableUtils.getTableName(this);
        if (tableName instanceof DynamicTable)
            return ((DynamicTable) tableName).getName(this);
        return tableName == null ? null : tableName.getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String name)
    {
        T value = null;
        Field field = this.getField(name);
        if (field != null)
            value = field.getObject();
        return value;
    }

    @Override
    public int hashCode()
    {
        if (this.getParent() != null)
        {
            Object[] srcPrimaryValues = this.getPrimaryKeyValues();
            if (srcPrimaryValues != null && !this.containsNull(srcPrimaryValues))
            {
                final int prime = 31;
                int result = this.getTableName() == null ? 1 : this.getTableName().hashCode();
                for (int i = 0; i < srcPrimaryValues.length; i++)
                    result = prime * result + (srcPrimaryValues[i] == null ? 0 : srcPrimaryValues[i].hashCode());
                return result;
            }
        }
        return super.hashCode();
    }

    /**
     * ֪ͨ�����У����ݶ������˸ı䡣
     */
    public void notify(Field field)
    {
        if (field.getDataState() == DataState.Modified)
        {
            this.dataState = DataState.Modified;
            this.parent.getRows().notify(this, field);
            for (TableEventAdapter tableEventAdapter : this.getParent().getTableEventAdapterList())
                tableEventAdapter.onFieldStateChanged(this, field);
        }
    }

    public void release()
    {
        this.clear();
        this.parent = null;
    }

    @Override
    public void setValue(String name, Object value)
    {
        Field field = this.getField(name);
        if (field != null)
            field.setObject(value);
    }

    /**
     * ������������ݡ�
     */
    public String toString()
    {
        StringBuffer info = new StringBuffer();
        for (Field field : this)
            info.append(field + "\t");
        return info.toString();
    }
}
