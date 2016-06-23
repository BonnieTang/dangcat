package org.dangcat.boot.menus.xml;

import org.dangcat.boot.menus.Menu;
import org.dangcat.boot.menus.MenuData;
import org.dangcat.boot.menus.Separator;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dom4j.Element;

/**
 * ģ������������
 * @author dangcat
 * 
 */
public class MenuXmlResolver extends XmlResolver
{
    /** ģ�顣 */
    private Menu menu = null;

    /**
     * ������������
     */
    public MenuXmlResolver()
    {
        super(Menu.class.getSimpleName());
        this.addChildXmlResolver(new MenuItemXmlResolver());
        this.addChildXmlResolver(new SubmenuXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child instanceof MenuData)
            this.menu.addMenuData((MenuData) child);
    }

    /**
     * ��ʼ������Ԫ�ر�ǩ��
     * @param element ��Ԫ�����ơ�
     */
    protected void resolveChildElement(Element element)
    {
        if (Separator.class.getSimpleName().equalsIgnoreCase(element.getName()))
            this.menu.addMenuData(new Separator());
        else
            super.resolveChildElement(element);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.menu = new Menu();
        this.setResolveObject(this.menu);
    }
}
