package org.dangcat.net.rfc.template.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ѡ������������
 * @author dangcat
 * 
 */
public class OptionXmlResolver extends XmlResolver
{
    private Option option = null;

    /**
     * ������������
     */
    public OptionXmlResolver()
    {
        this(Option.class.getSimpleName());
    }

    /**
     * ������������
     */
    public OptionXmlResolver(String name)
    {
        super(name);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.option = new Option();
        this.setResolveObject(this.option);
    }
}
