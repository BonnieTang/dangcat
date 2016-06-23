package org.dangcat.net.rfc.template.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.net.rfc.template.AttributeTemplate;
import org.dangcat.net.rfc.template.VendorAttributeTemplateManager;

/**
 * ��λ�����������
 *
 * @author dangcat
 */
public class AttributesXmlResolver extends XmlResolver {
    private static final String RESOLVER_NAME = "Attributes";
    /**
     * ��������ģ���������
     */
    private VendorAttributeTemplateManager vendorAttributeTemplateManager = null;

    /**
     * ������������
     */
    public AttributesXmlResolver() {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new AttributeXmlResolver());
        this.addChildXmlResolver(new RulesXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     *
     * @param elementName ��Ԫ�����ơ�
     * @param child       ��Ԫ�ض���
     */
    @Override
    protected void afterChildCreate(String elementName, Object child) {
        if (child != null) {
            if (child instanceof Attribute) {
                Attribute attribute = (Attribute) child;
                AttributeTemplate attributeTemplate = attribute.createAttributeTemplate();
                if (attributeTemplate != null)
                    this.vendorAttributeTemplateManager.addAttributeTemplate(attributeTemplate.getType(), attributeTemplate);
            } else if (child instanceof Rules)
                this.vendorAttributeTemplateManager.addPacketRules((Rules) child);
        }
    }

    public VendorAttributeTemplateManager getVendorAttributeTemplateManager() {
        return this.vendorAttributeTemplateManager;
    }

    public void setVendorAttributeTemplateManager(VendorAttributeTemplateManager vendorAttributeTemplateManager) {
        this.vendorAttributeTemplateManager = vendorAttributeTemplateManager;
    }
}
