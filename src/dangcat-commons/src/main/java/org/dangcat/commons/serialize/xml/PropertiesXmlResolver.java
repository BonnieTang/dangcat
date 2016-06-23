package org.dangcat.commons.serialize.xml;

import java.util.List;

/**
 * �����б�����������
 * @author dangcat
 * 
 */
public class PropertiesXmlResolver extends XmlResolver
{
    private static final String RESOLVER_NAME = "Properties";
    /** �����б� */
    private List<Property> propertyList;

    /**
     * ������������
     */
    public PropertiesXmlResolver()
    {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new PropertyXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        Property property = (Property) child;
        if (property != null)
            this.propertyList.add(property);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setResolveObject(Object resolveObject)
    {
        this.propertyList = (List<Property>) resolveObject;
        super.setResolveObject(resolveObject);
    }
}
