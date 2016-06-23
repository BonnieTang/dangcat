package org.dangcat.persistence.model;

import java.sql.Time;
import java.util.Date;

import org.dangcat.commons.utils.DateUtils;
import org.dangcat.commons.utils.ValueUtils;

/**
 * �������ݶ���
 * @author dangcat
 * 
 */
public class Field implements java.io.Serializable, Comparable<Field>
{
    private static final long serialVersionUID = 1L;

    /**
     * �����µ�ʵ����
     * @return
     */
    public static Field newInstance()
    {
        return new Field();
    }

    /** ���ݶ����״̬���������޸ġ� */
    private DataState dataState = DataState.Browse;
    /** �ɵ�����ֵ�� */
    private Object oldValue;
    /** ���������С� */
    private Row parent;
    /** ��ǰ����ֵ�� */
    private Object value;

    /**
     * ���캯����
     */
    public Field()
    {
    }

    /**
     * ���캯����
     * @param value ��ǰ����ֵ��
     */
    public Field(Object value)
    {
        this.value = value;
    }

    /**
     * ���캯����
     * @param parent �������ж���
     */
    public Field(Row parent)
    {
        this.parent = parent;
    }

    /**
     * �ı䵱ǰֵ��
     * @param newValue �µ�ֵ����
     */
    private void changeValue(Object newValue)
    {
        if (this.oldValue == null)
            this.oldValue = this.value;
        this.value = newValue;

        if (this.dataState == DataState.Browse)
        {
            this.dataState = DataState.Modified;
            if (this.parent != null)
                this.parent.notify(this);
        }
    }

    /**
     * �ֶαȽϣ���ȷ���0�����ڷ��ش���0������С�ڷ���С��0������
     */

    public int compareTo(Field destField)
    {
        if (destField == null)
            return -1;
        return ValueUtils.compare(this.getObject(), destField.getObject());
    }

    public byte[] getBytes()
    {
        return (byte[]) this.value;
    }

    public Character getChar()
    {
        if (this.value instanceof char[])
            return ((char[]) this.value)[0];
        return (Character) this.value;
    }

    public char[] getChars()
    {
        return (char[]) this.value;
    }

    public DataState getDataState()
    {
        return this.dataState;
    }

    public Date getDate()
    {
        return (Date) this.value;
    }

    public Double getDouble()
    {
        return (Double) this.value;
    }

    public Integer getInteger()
    {
        return (Integer) this.value;
    }

    public Long getLong()
    {
        return (Long) this.value;
    }

    public Number getNumber()
    {
        return (Number) this.value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject()
    {
        return (T) this.value;
    }

    /**
     * ȡ�þɵ�����ֵ��
     * @return �ɵ�ֵ����
     */
    public Object getOldValue()
    {
        return this.oldValue;
    }

    public Row getParent()
    {
        return this.parent;
    }

    public Short getShort()
    {
        return (Short) this.value;
    }

    public String getString()
    {
        if (this.value instanceof String)
            return (String) this.value;
        return this.toString();
    }

    public Time getTime()
    {
        return (Time) this.value;
    }

    public void setBytes(byte[] newValue)
    {
        this.changeValue(newValue);
    }

    public void setChar(char newValue)
    {
        this.changeValue(new char[] { newValue });
    }

    public void setChars(char[] newValue)
    {
        this.changeValue(newValue);
    }

    /**
     * ��������״̬��
     * @param dataState ����״̬��
     */
    public void setDataState(DataState dataState)
    {
        this.dataState = dataState;
        if (dataState == DataState.Browse || dataState == DataState.Insert)
            this.oldValue = null;
    }

    public void setDate(Date newValue)
    {
        this.changeValue(newValue);
    }

    public void setDouble(Double newValue)
    {
        this.changeValue(newValue);
    }

    public void setInteger(Integer newValue)
    {
        this.changeValue(newValue);
    }

    public void setLong(Long newValue)
    {
        this.changeValue(newValue);
    }

    public void setObject(Object newValue)
    {
        this.changeValue(newValue);
    }

    public void setParent(Row parent)
    {
        this.parent = parent;
    }

    public void setShort(Short newValue)
    {
        this.changeValue(newValue);
    }

    public void setString(String newValue)
    {
        this.changeValue(newValue);
    }

    public void setTime(Time newValue)
    {
        this.changeValue(newValue);
    }

    /**
     * �ѵ�ǰֵת��SQL���ʽ��
     * @param transOldValue �Ƿ�ת����ֵ��
     * @return ���ʽ��
     */
    public String toSqlString(boolean transOldValue)
    {
        return TableStatementHelper.toSqlString(transOldValue ? this.oldValue : this.value);
    }

    /**
     * ����������ݡ�
     */
    public String toString()
    {
        if (this.value instanceof Date)
            return DateUtils.format((Date) this.value);
        return this.value == null ? null : this.value.toString();
    }
}
