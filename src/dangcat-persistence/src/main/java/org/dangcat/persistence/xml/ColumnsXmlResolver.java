package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Columns;

/**
 * ��λ�����������
 *
 * @author dangcat
 */
public class ColumnsXmlResolver extends XmlResolver {
    /**
     * ��λ����
     */
    private Columns columns = null;

    /**
     * ������������
     */
    public ColumnsXmlResolver() {
        super(Columns.class.getSimpleName());
        this.addChildXmlResolver(new ColumnXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     *
     * @param elementName ��Ԫ�����ơ�
     * @param child       ��Ԫ�ض���
     */
    @Override
    protected void afterChildCreate(String elementName, Object child) {
        if (child != null)
            columns.add((Column) child);
    }

    @Override
    public void setResolveObject(Object resolveObject) {
        this.columns = (Columns) resolveObject;
        super.setResolveObject(resolveObject);
    }
}
