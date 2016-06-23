package org.dangcat.persistence.xml;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.calculate.CalculatorImpl;
import org.dom4j.Element;

/**
 * �����������������
 * @author dangcat
 * 
 */
public class CalculatorsXmlResolver extends XmlResolver
{
    protected static final String RESOLVER_NAME = "Calculators";
    private static final String CHILDELEMENT_NAME = "Calculator";
    private CalculatorImpl calculators = null;

    /**
     * ������������
     */
    public CalculatorsXmlResolver()
    {
        super(RESOLVER_NAME);
    }

    @Override
    protected Object endElement()
    {
        this.calculators.initialize();
        return super.endElement();
    }

    /**
     * ��ʼ������Ԫ�ر�ǩ��
     * @param element ��Ԫ�����ơ�
     */
    protected void resolveChildElement(Element element)
    {
        if (CHILDELEMENT_NAME.equalsIgnoreCase(element.getName()) && !ValueUtils.isEmpty(element.getText()))
        {
            Class<?> classType = ReflectUtils.loadClass(element.getTextTrim());
            if (classType != null)
                this.calculators.add(classType);
        }
    }

    public void setResolveObject(Object resolveObject)
    {
        this.calculators = (CalculatorImpl) resolveObject;
        super.setResolveObject(resolveObject);
    }
}
