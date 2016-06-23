package org.dangcat.net.rfc.template.rules;

import org.dangcat.net.rfc.attribute.AttributeData;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;
import org.dangcat.net.rfc.template.VendorAttributeTemplateManager;
import org.dangcat.net.rfc.template.xml.Rule;
import org.dangcat.net.rfc.template.xml.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �������������
 * @author dangcat
 * 
 */
public class PacketRuleValidator extends ArrayList<RuleValidator>
{
    private static final long serialVersionUID = 1L;
    private String packetType;
    private VendorAttributeTemplateManager vendorAttributeTemplateManager = null;

    public PacketRuleValidator(VendorAttributeTemplateManager vendorAttributeTemplateManager)
    {
        this.vendorAttributeTemplateManager = vendorAttributeTemplateManager;
    }

    /**
     * ��ӱ����ݰ��ļ�������
     * @param vendorAttributeTemplateManager �������Թ���
     * @param rules ���򼯺ϡ�
     */
    public void addRules(Rules rules)
    {
        for (Rule rule : rules)
        {
            RuleValidator ruleValidator = RuleValidator.createInstance(rule);
            if (ruleValidator != null)
            {
                ruleValidator.setPacketRuleValidator(this);
                this.add(ruleValidator);
            }
        }
    }

    public String getPacketType()
    {
        return packetType;
    }

    public void setPacketType(String packetType)
    {
        this.packetType = packetType;
    }

    protected VendorAttributeTemplateManager getVendorAttributeTemplateManager()
    {
        return vendorAttributeTemplateManager;
    }

    /**
     * �����Լ��Ͻ��й�����֤��
     * @param attributeDataList ���Լ��ϡ�
     * @throws ProtocolValidateException ��֤�쳣��
     */
    public void validate(List<AttributeData> attributeDataList) throws ProtocolValidateException
    {
        if (attributeDataList.size() > 0)
        {
            Map<Integer, AttributeData> attributeDataMap = new HashMap<Integer, AttributeData>();
            Map<Integer, Integer> attributeDataCountMap = new HashMap<Integer, Integer>();
            for (AttributeData attributeData : attributeDataList)
            {
                Integer type = attributeData.getType();
                Integer value = 1;
                if (attributeDataCountMap.containsKey(type))
                    value = attributeDataCountMap.get(type) + value;
                attributeDataCountMap.put(type, value);
                attributeDataMap.put(type, attributeData);
            }

            for (RuleValidator ruleValidator : this)
            {
                try
                {
                    ruleValidator.validate(attributeDataCountMap);
                }
                catch (ProtocolValidateException e)
                {
                    AttributeData attributeData = attributeDataMap.get(ruleValidator.getAttributeTemplate().getType());
                    e.setAttributeData(attributeData);
                    throw e;
                }
            }
        }
    }
}
