package org.dangcat.boot.permission.xml;

import org.dangcat.boot.permission.JndiName;
import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * JndiName�����������
 * @author dangcat
 * 
 */
public class JndiNameXmlResolver extends XmlResolver
{
    private JndiName jndiName = null;

    /**
     * ������������
     */
    public JndiNameXmlResolver()
    {
        super(JndiName.class.getSimpleName());
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.jndiName = new JndiName();
        this.setResolveObject(this.jndiName);
    }
}
