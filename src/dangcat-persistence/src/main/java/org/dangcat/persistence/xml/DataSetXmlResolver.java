package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.model.DataSet;
import org.dangcat.persistence.model.Table;

/**
 * ��λ�����������
 * @author dangcat
 * 
 */
public class DataSetXmlResolver extends XmlResolver
{
    /**
     * ��λ����
     */
    private DataSet dataSet = null;

    /**
     * ������������
     */
    public DataSetXmlResolver()
    {
        super(DataSet.class.getSimpleName());
        this.addChildXmlResolver(new TableXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        Table table = (Table) child;
        if (table != null)
            this.dataSet.add(table);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement()
    {
        this.dataSet = new DataSet();
        this.setResolveObject(dataSet);
    }
}
