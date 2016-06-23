package org.dangcat.net.rfc.template.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;

import java.util.HashMap;

class AttributeXmlResolver extends XmlResolver
{
    private static final String RESOLVER_NAME = "Attribute";
    /** �������� */
    private Attribute attribute = null;

    AttributeXmlResolver()
    {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new OptionsXmlResolver());
    }

    /**
     * ������Ԫ��֮ǰ��
     * @param name �������ơ�
     * @param xmlResolver ��������
     */
    protected void beforeChildResolve(String elementName, XmlResolver xmlResolver)
    {
        if (OptionsXmlResolver.RESOLVER_NAME.equalsIgnoreCase(elementName))
        {
            this.attribute.setOptions(new HashMap<Integer, String>());
            xmlResolver.setResolveObject(this.attribute.getOptions());
        }
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.attribute = new Attribute();
        this.setResolveObject(this.attribute);
    }
}
