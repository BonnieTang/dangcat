package org.dangcat.net.rfc.template;

import org.dangcat.net.rfc.template.rules.PacketRuleValidator;
import org.dangcat.net.rfc.template.xml.Rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * ��������ģ�����
 * @author dangcat
 * 
 */
public class VendorAttributeTemplateManager
{
    /** ����ģ���ܹ��� */
    private AttributeTemplateManager attributeTemplateManager = null;
    /** ���Ժ���ģ���ӳ���б� */
    private Map<Integer, AttributeTemplate> attributeTemplateMap = new HashMap<Integer, AttributeTemplate>();
    /** ��������ģ���ӳ���б� */
    private Map<String, AttributeTemplate> attributeTemplateNameMap = null;
    /** �����͵�У��� */
    private Map<String, PacketRuleValidator> packetRuleValidatorMap = new HashMap<String, PacketRuleValidator>();
    /** ���̺š� */
    private Integer vendorId = null;

    public VendorAttributeTemplateManager(Integer vendorId, AttributeTemplateManager attributeTemplateManager)
    {
        this.vendorId = vendorId;
        this.attributeTemplateManager = attributeTemplateManager;
    }

    public void addAttributeTemplate(Integer type, AttributeTemplate attributeTemplate)
    {
        attributeTemplate.setVendorAttributeTemplateManager(this);
        this.attributeTemplateMap.put(type, attributeTemplate);
        this.attributeTemplateNameMap = null;
    }

    public void addPacketRules(Rules rules)
    {
        PacketRuleValidator packetRuleValidator = this.getPacketRuleValidator(rules.getPacketType());
        if (packetRuleValidator == null)
        {
            packetRuleValidator = new PacketRuleValidator(this);
            packetRuleValidator.setPacketType(rules.getPacketType());
            this.packetRuleValidatorMap.put(rules.getPacketType(), packetRuleValidator);
        }
        packetRuleValidator.addRules(rules);
    }

    /**
     * ͨ���������ͺŶ�ȡ����ģ�塣
     * @param type ���Ժš�
     * @return ����ģ�塣
     */
    public AttributeTemplate getAttributeTemplate(Integer type)
    {
        return this.attributeTemplateMap.get(type);
    }

    /**
     * ͨ�����̡��������ͺŶ�ȡ����ģ�塣
     * @param name ��������
     * @return ����ģ�塣
     */
    public AttributeTemplate getAttributeTemplate(String name)
    {
        if (this.attributeTemplateNameMap == null)
        {
            synchronized (this)
            {
                if (this.attributeTemplateNameMap == null)
                {
                    Map<String, AttributeTemplate> attributeTemplateNameMap = new HashMap<String, AttributeTemplate>();
                    for (AttributeTemplate attributeTemplate : this.attributeTemplateMap.values())
                        attributeTemplateNameMap.put(attributeTemplate.getName(), attributeTemplate);
                    this.attributeTemplateNameMap = attributeTemplateNameMap;
                }
            }
        }
        return this.attributeTemplateNameMap.get(name);
    }

    public AttributeTemplateManager getAttributeTemplateManager()
    {
        return attributeTemplateManager;
    }

    public Map<Integer, AttributeTemplate> getAttributeTemplateMap()
    {
        return this.attributeTemplateMap;
    }

    public Map<String, AttributeTemplate> getAttributeTemplateNameMap()
    {
        return this.attributeTemplateNameMap;
    }

    public Collection<AttributeTemplate> getAttributeTemplates()
    {
        return this.attributeTemplateMap.values();
    }

    /**
     * ָ�������Ͷ�ȡУ�����
     * @param packetType �����͡�
     * @return У�����
     */
    public PacketRuleValidator getPacketRuleValidator(String packetType)
    {
        return this.packetRuleValidatorMap.get(packetType);
    }

    public Integer getVendorId()
    {
        return this.vendorId;
    }
}
