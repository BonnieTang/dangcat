package org.dangcat.net.rfc.template.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ���Թ����������
 *
 * @author dangcat
 */
public class RuleXmlResolver extends XmlResolver {
    private Rule rule = null;

    /**
     * ������������
     */
    public RuleXmlResolver() {
        this(Rule.class.getSimpleName());
    }

    /**
     * ������������
     */
    public RuleXmlResolver(String name) {
        super(name);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement() {
        this.rule = new Rule();
        this.setResolveObject(this.rule);
    }
}
