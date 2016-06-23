package org.dangcat.persistence.xml;

import java.util.ArrayList;
import java.util.List;

import org.dangcat.commons.serialize.xml.Value;
import org.dangcat.commons.serialize.xml.ValueXmlResolver;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.filter.FilterUnit;

/**
 * ��λ�����������
 * @author dangcat
 * 
 */
public class FilterUnitXmlResolver extends XmlResolver
{
    /**
     * ��λ����
     */
    private FilterUnit filterUnit = null;

    /**
     * ��λ����
     */
    private List<Object> paramList = null;

    /**
     * ������������
     */
    public FilterUnitXmlResolver()
    {
        super(FilterUnit.class.getSimpleName());
        this.addChildXmlResolver(new ValueXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    @Override
    protected void afterChildCreate(String elementName, Object child)
    {
        if (elementName.equalsIgnoreCase(Value.class.getSimpleName()))
        {
            Value value = (Value) child;
            this.paramList.add(value.getValue());
        }
    }

    /**
     * ����Ԫ�ر�ǩ������
     * @return ��������
     */
    @Override
    protected Object endElement()
    {
        this.filterUnit.setParams(paramList.toArray());
        return super.endElement();
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.filterUnit = new FilterUnit();
        this.paramList = new ArrayList<Object>();
        this.setResolveObject(filterUnit);
    }
}
