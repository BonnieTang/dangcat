package org.dangcat.commons.serialize.xml;

import org.apache.log4j.Logger;
import org.dangcat.commons.reflect.ReflectUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Xml�ļ���������
 * @author dangcat
 * 
 */
public abstract class XmlResolver
{
    /** ������־�� */
    protected static final Logger logger = Logger.getLogger(XmlResolver.class);
    /** Ԫ�ؽ���Ӱ���. */
    private Map<String, XmlResolver> childXmlResolverMap = new HashMap<String, XmlResolver>();
    /** Xml�ĵ��� */
    private Document document = null;
    /** ���������ơ� */
    private String name;
    /** �������� */
    private Object resolveObject = null;

    /**
     * ������������
     */
    public XmlResolver(String name)
    {
        this.name = name;
    }

    /**
     * ������Ԫ�ؽ�������
     * @param xmlResolver ��������
     */
    protected void addChildXmlResolver(XmlResolver xmlResolver)
    {
        if (xmlResolver != null)
        {
            String elementName = xmlResolver.getName();
            if (containsChildXmlResolver(elementName))
                childXmlResolverMap.remove(elementName);
            childXmlResolverMap.put(elementName, xmlResolver);
        }
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        ReflectUtils.setProperty(this.resolveObject, elementName, child);
    }

    /**
     * ������Ԫ��֮ǰ��
     * @param name �������ơ�
     * @param xmlResolver ��������
     */
    protected void beforeChildResolve(String elementName, XmlResolver xmlResolver)
    {
    }

    /**
     * �Ƿ��Ѿ����ڽ�������
     * @param elementName Ԫ�����ơ�
     */
    protected boolean containsChildXmlResolver(String elementName)
    {
        return childXmlResolverMap.containsKey(elementName);
    }

    /**
     * ����Ԫ�ر�ǩ������
     * @return ��������
     */
    protected Object endElement()
    {
        return this.getResolveObject();
    }

    public String getName()
    {
        return this.name;
    }

    public Object getResolveObject()
    {
        return this.resolveObject;
    }

    public void setResolveObject(Object resolveObject) {
        this.resolveObject = resolveObject;
    }

    /**
     * �����������
     * @param xmlFile Xml�ĵ���
     * @return ��������
     * @throws XMLStreamException �����쳣��
     * @throws DocumentException
     */
    public void open(File xmlFile) throws DocumentException
    {
        if (xmlFile != null && xmlFile.exists())
        {
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEntityResolver(new NullEntityResolver());
            this.document = xmlReader.read(xmlFile);
        }
    }

    /**
     * �����������
     * @param inputStream ��������
     * @return ��������
     * @throws XMLStreamException �����쳣��
     * @throws DocumentException
     */
    public void open(InputStream inputStream) throws DocumentException
    {
        if (inputStream != null)
        {
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEntityResolver(new NullEntityResolver());
            this.document = xmlReader.read(inputStream);
        }
    }

    /**
     * ��������
     * @return ��������
     * @throws XMLStreamException �����쳣��
     */
    public Object resolve()
    {
        return this.resolve(this.document.getRootElement());
    }

    /**
     * ����Ԫ�ء�
     * @param parent ��Ԫ�ء�
     * @return ��������
     */
    public Object resolve(Element parent)
    {
        String elementName = parent.getName();

        if (!this.getName().equals(elementName))
            return null;

        // ��ʼ����Ԫ�ر�ǩ��
        logger.debug("StartElement: " + elementName);
        this.startElement();

        // �������ԡ�
        for (Object attributeObject : parent.attributes())
        {
            DefaultAttribute chileElement = (DefaultAttribute) attributeObject;
            logger.debug("Attribute " + chileElement.getName() + ": " + chileElement.getValue());
            this.resolveAttribute(chileElement.getName(), chileElement.getValue());
        }

        // �������֡�
        String text = parent.getTextTrim();
        if (text != null && !text.equals(""))
        {
            logger.debug("ElementText : " + text);
            this.resolveElementText(parent.getText());
        }

        // ������Ԫ�ء�
        for (Object elementObject : parent.elements())
        {
            Element childElement = (Element) elementObject;
            String childElementName = childElement.getName();
            logger.debug("Resolve ChildElement: " + childElementName);
            this.resolveChildElement(childElement);
        }

        // ���ؽ�������
        logger.debug("EndElement: " + elementName);
        return this.endElement();
    }

    /**
     * �������ԡ�
     * @param name �������ơ�
     * @param value ����ֵ��
     */
    protected void resolveAttribute(String name, String value)
    {
        ReflectUtils.setProperty(this.resolveObject, name, value);
    }

    /**
     * ��ʼ������Ԫ�ر�ǩ��
     * @param element ��Ԫ�����ơ�
     */
    protected void resolveChildElement(Element element)
    {
        String elementName = element.getName();
        if (this.childXmlResolverMap.containsKey(elementName))
        {
            XmlResolver xmlResolver = this.childXmlResolverMap.get(elementName);
            this.beforeChildResolve(elementName, xmlResolver);
            Object propertyValue = ReflectUtils.getProperty(this.resolveObject, elementName);
            if (propertyValue != null)
                xmlResolver.setResolveObject(propertyValue);
            Object result = xmlResolver.resolve(element);
            if (result != null)
                this.afterChildCreate(elementName, result);
        }
    }

    /**
     * �����ı���
     * @param value �ı�ֵ��
     */
    protected void resolveElementText(String value)
    {
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    protected void startElement()
    {
    }
}
