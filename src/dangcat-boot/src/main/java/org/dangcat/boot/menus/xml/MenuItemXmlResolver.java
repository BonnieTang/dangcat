package org.dangcat.boot.menus.xml;

import java.util.Map;

import org.dangcat.boot.menus.MenuItem;
import org.dangcat.commons.serialize.xml.ParamsXmlResolver;
import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ģ������������
 * @author dangcat
 * 
 */
public class MenuItemXmlResolver extends XmlResolver
{
    /** ģ�顣 */
    private MenuItem menuItem = null;

    /**
     * ������������
     */
    public MenuItemXmlResolver()
    {
        super(MenuItem.class.getSimpleName());
        this.addChildXmlResolver(new ParamsXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    @SuppressWarnings("unchecked")
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child instanceof Map)
            this.menuItem.setParams((Map<String, Object>) child);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.menuItem = new MenuItem();
        this.setResolveObject(this.menuItem);
    }
}
