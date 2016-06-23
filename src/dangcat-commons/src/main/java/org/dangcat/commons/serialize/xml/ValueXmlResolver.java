package org.dangcat.commons.serialize.xml;

import org.dangcat.commons.reflect.ReflectUtils;

/**
 * ��λ�����������
 * @author dangcat
 * 
 */
public class ValueXmlResolver extends XmlResolver
{
    /**
     * ��λ����
     */
    private Value value = null;

    /**
     * ������������
     */
    public ValueXmlResolver()
    {
        this(Value.class.getSimpleName());
    }

    /**
     * ������������
     */
    public ValueXmlResolver(String name)
    {
        super(name);
    }

    /**
     * �����ı���
     * @param value �ı�ֵ��
     */
    @Override
    protected void resolveElementText(String valueText)
    {
        this.value.setValue(ReflectUtils.parseValue(this.value.getClassType(), valueText));
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.value = new Value();
        this.setResolveObject(this.value);
    }
}
