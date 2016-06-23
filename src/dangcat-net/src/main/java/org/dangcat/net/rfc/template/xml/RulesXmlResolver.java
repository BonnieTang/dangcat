package org.dangcat.net.rfc.template.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ���Թ����������
 *
 * @author dangcat
 */
public class RulesXmlResolver extends XmlResolver {
    private static final String RESOLVER_NAME = "Rules";
    private Rules rules = null;

    /**
     * ������������
     */
    public RulesXmlResolver() {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new RuleXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     *
     * @param elementName ��Ԫ�����ơ�
     * @param child       ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child) {
        Rule rule = (Rule) child;
        if (rule != null)
            this.rules.add(rule);
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement() {
        this.rules = new Rules();
        this.setResolveObject(this.rules);
    }
}
