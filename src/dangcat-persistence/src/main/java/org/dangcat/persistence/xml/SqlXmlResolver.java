package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.sql.Sql;

/**
 * ��λ�����������
 * @author dangcat
 * 
 */
public class SqlXmlResolver extends XmlResolver
{
    /** ��λ���� */
    private Sql sql = null;

    /**
     * ������������
     */
    public SqlXmlResolver()
    {
        super(Sql.class.getSimpleName());
    }

    /**
     * �����ı���
     * @param value �ı�ֵ��
     */
    @Override
    protected void resolveElementText(String value)
    {
        this.sql.setSql(value);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.sql = new Sql();
        this.setResolveObject(sql);
    }
}
