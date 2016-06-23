package org.dangcat.boot.permission.xml;

import org.dangcat.boot.permission.JndiName;
import org.dangcat.boot.permission.Module;
import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ģ������������
 * @author dangcat
 * 
 */
public class ModuleXmlResolver extends XmlResolver
{
    /** ģ�顣 */
    private Module module = null;

    /**
     * ������������
     */
    public ModuleXmlResolver()
    {
        super(Module.class.getSimpleName());
        this.addChildXmlResolver(new JndiNameXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child instanceof JndiName)
            this.module.addJndiName((JndiName) child);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.module = new Module();
        this.setResolveObject(this.module);
    }
}
