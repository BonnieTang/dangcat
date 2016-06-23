package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.model.Column;

/**
 * ��λ�����������
 * @author dangcat
 * 
 */
public class ColumnXmlResolver extends XmlResolver
{
    /** ��λ���� */
    private Column column = null;

    /**
     * ������������
     */
    public ColumnXmlResolver()
    {
        super(Column.class.getSimpleName());
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.column = new Column();
        this.setResolveObject(column);
    }
}
