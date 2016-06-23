package org.dangcat.net.rfc.template.xml;

import java.util.Map;

import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ѡ���б�����������
 * @author dangcat
 * 
 */
public class OptionsXmlResolver extends XmlResolver
{
    public static final String RESOLVER_NAME = "Options";

    /**
     * ������������
     */
    public OptionsXmlResolver()
    {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new OptionXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        Option option = (Option) child;
        if (option != null)
            this.getOptions().put(option.getKey(), option.getValue());
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, String> getOptions()
    {
        return (Map<Integer, String>) this.getResolveObject();
    }
}
