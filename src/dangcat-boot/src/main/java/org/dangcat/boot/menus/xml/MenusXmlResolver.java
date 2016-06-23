package org.dangcat.boot.menus.xml;

import org.dangcat.boot.menus.Menu;
import org.dangcat.boot.menus.Menus;
import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ϵͳ�˵������������
 *
 * @author dangcat
 */
public class MenusXmlResolver extends XmlResolver {
    /**
     * �����б�
     */
    private Menus menus;

    /**
     * ������������
     */
    public MenusXmlResolver() {
        super(Menus.class.getSimpleName());
        this.addChildXmlResolver(new MenuXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     *
     * @param elementName ��Ԫ�����ơ�
     * @param child       ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child) {
        if (child instanceof Menu)
            this.menus.getData().add((Menu) child);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement() {
        this.menus = new Menus();
        this.setResolveObject(this.menus);
    }
}
