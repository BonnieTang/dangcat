package org.dangcat.commons.serialize.xml;

/**
 * ��������
 * @author dangcat
 * 
 */
public class Value implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    /** �������͡� */
    private Class<?> classType = String.class;
    /** ����ֵ�� */
    private Object value;

    public Class<?> getClassType()
    {
        return classType;
    }

    public void setClassType(Class<?> classType)
    {
        this.classType = classType;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }
}
