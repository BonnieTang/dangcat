package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.orderby.OrderBy;

/**
 * ��λ�����������
 *
 * @author dangcat
 */
public class OrderByXmlResolver extends XmlResolver {
    /**
     * ��λ����
     */
    private OrderBy orderBy = null;

    /**
     * ������������
     */
    public OrderByXmlResolver() {
        super(OrderBy.class.getSimpleName());
    }

    /**
     * �����ı���
     *
     * @param value �ı�ֵ��
     */
    @Override
    protected void resolveElementText(String value) {
        this.orderBy = OrderBy.parse(value);
        this.setResolveObject(orderBy);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement() {
        orderBy = new OrderBy();
        this.setResolveObject(orderBy);
    }
}
