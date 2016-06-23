package org.dangcat.commons.serialize.xml;

/**
 * ���Զ����������
 * @author dangcat
 * 
 */
public class PropertyXmlResolver extends XmlResolver
{
    /** ���Զ��� */
    private Property property = null;

    /**
     * ������������
     */
    public PropertyXmlResolver()
    {
        super(Property.class.getSimpleName());
    }

    /**
     * �����ı���
     * @param value �ı�ֵ��
     */
    @Override
    protected void resolveElementText(String valueText)
    {
        this.property.setValue(valueText);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.property = new Property();
        this.setResolveObject(this.property);
    }
}
