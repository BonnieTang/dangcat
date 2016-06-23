package org.dangcat.persistence.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.orm.SqlBuilder;

/**
 * ��λ�����������
 *
 * @author dangcat
 */
public class SqlBuilderXmlResolver extends XmlResolver {
    public static final String RESOLVER_NAME = "Sql";

    /**
     * ������������
     */
    public SqlBuilderXmlResolver() {
        super(RESOLVER_NAME);
    }

    /**
     * �����ı���
     *
     * @param value �ı�ֵ��
     */
    @Override
    protected void resolveElementText(String value) {
        SqlBuilder sqlBuilder = (SqlBuilder) this.getResolveObject();
        sqlBuilder.append(value);
    }
}
