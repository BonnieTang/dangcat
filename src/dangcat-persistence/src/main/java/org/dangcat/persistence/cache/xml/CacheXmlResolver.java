package org.dangcat.persistence.cache.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.commons.utils.ValueUtils;
import org.dom4j.Element;

/**
 * �������ý�������
 * @author dangcat
 * 
 */
public class CacheXmlResolver extends XmlResolver
{
    private static final String TEXT_INDEX = "Index";
    /** ��λ���� */
    private Cache cache = null;

    /**
     * ������������
     */
    public CacheXmlResolver()
    {
        super(Cache.class.getSimpleName());
    }

    /**
     * ��ʼ������Ԫ�ر�ǩ��
     * @param element ��Ԫ�����ơ�
     */
    protected void resolveChildElement(Element element)
    {
        if (element.getName().equalsIgnoreCase(TEXT_INDEX))
        {
            if (!ValueUtils.isEmpty(element.getText()))
            {
                String index = element.getText().trim();
                if (!this.cache.getIndexList().contains(index))
                    this.cache.getIndexList().add(index);
            }
        }
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.cache = new Cache();
        this.setResolveObject(this.cache);
    }
}
