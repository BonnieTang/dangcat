package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.filter.FilterGroup;

/**
 * ��λ�����������
 * @author dangcat
 * 
 */
public class FilterGroupXmlResolver extends XmlResolver
{
    /**
     * ��λ����
     */
    private FilterGroup filterGroup = null;

    /**
     * ������������
     */
    public FilterGroupXmlResolver()
    {
        this(FilterGroup.class.getSimpleName());
    }

    /**
     * ������������
     */
    public FilterGroupXmlResolver(String name)
    {
        super(name);
        this.addChildXmlResolver(new FilterUnitXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    @Override
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child != null)
            this.filterGroup.getFilterExpressList().add((FilterExpress) child);
    }

    /**
     * ����Ԫ�ر�ǩ������
     * @return ��������
     */
    @Override
    protected Object endElement()
    {
        if (filterGroup.getFilterExpressList().size() == 1)
            return filterGroup.getFilterExpressList().get(0);
        return super.endElement();
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        filterGroup = new FilterGroup();
        if (!containsChildXmlResolver(FilterGroup.class.getSimpleName()))
            this.addChildXmlResolver(new FilterGroupXmlResolver());
        this.setResolveObject(filterGroup);
    }
}
