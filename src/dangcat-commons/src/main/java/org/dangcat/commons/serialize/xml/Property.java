package org.dangcat.commons.serialize.xml;

/**
 * ���Զ���
 * @author dangcat
 * 
 */
public class Property implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    /** �������� */
    private String name;
    /** ����ֵ�� */
    private String value;

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.name + " = " + this.value;
    }
}
