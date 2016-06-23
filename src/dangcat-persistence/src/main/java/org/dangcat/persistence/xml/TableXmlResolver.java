package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.ParamsXmlResolver;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.model.Table;

/**
 * ��λ�����������
 *
 * @author dangcat
 */
public class TableXmlResolver extends XmlResolver {
    /**
     * ��λ����
     */
    private Table table = null;

    /**
     * ������������
     */
    public TableXmlResolver() {
        super(Table.class.getSimpleName());
        this.addChildXmlResolver(new ColumnsXmlResolver());
        this.addChildXmlResolver(new SqlBuilderXmlResolver());
        this.addChildXmlResolver(new SqlsXmlResolver());
        this.addChildXmlResolver(new ParamsXmlResolver());
        this.addChildXmlResolver(new OrderByXmlResolver());
        this.addChildXmlResolver(new FilterGroupXmlResolver("FixFilter"));
        this.addChildXmlResolver(new FilterGroupXmlResolver("Filter"));
        this.addChildXmlResolver(new CalculatorsXmlResolver());
    }

    public Table getTable() {
        return table;
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement() {
        this.table = new Table();
        this.setResolveObject(table);
    }
}
