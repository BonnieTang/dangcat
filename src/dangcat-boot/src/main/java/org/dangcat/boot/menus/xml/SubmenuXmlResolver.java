package org.dangcat.boot.menus.xml;

import org.dangcat.boot.menus.MenuData;
import org.dangcat.boot.menus.Separator;
import org.dangcat.boot.menus.Submenu;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dom4j.Element;

/**
 * ģ������������
 * @author dangcat
 * 
 */
public class SubmenuXmlResolver extends XmlResolver
{
    private SubmenuXmlResolver childSubmenuXmlResolver = null;
    private Submenu submenu = null;

    /**
     * ������������
     */
    public SubmenuXmlResolver()
    {
        super(Submenu.class.getSimpleName());
        this.addChildXmlResolver(new MenuItemXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child instanceof MenuData)
            this.submenu.addMenuData((MenuData) child);
    }

    /**
     * ��ʼ������Ԫ�ر�ǩ��
     * @param element ��Ԫ�����ơ�
     */
    protected void resolveChildElement(Element element)
    {
        if (Separator.class.getSimpleName().equalsIgnoreCase(element.getName()))
            this.submenu.addMenuData(new Separator());
        else if (Submenu.class.getSimpleName().equalsIgnoreCase(element.getName()))
            this.resolveChildSubMenu(element);
        else
            super.resolveChildElement(element);
    }

    private void resolveChildSubMenu(Element element)
    {
        if (this.childSubmenuXmlResolver == null)
            this.childSubmenuXmlResolver = new SubmenuXmlResolver();
        this.childSubmenuXmlResolver.resolve(element);
        MenuData menuData = (MenuData) this.childSubmenuXmlResolver.getResolveObject();
        if (menuData != null)
            this.submenu.addMenuData(menuData);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.submenu = new Submenu();
        this.setResolveObject(this.submenu);
    }
}
