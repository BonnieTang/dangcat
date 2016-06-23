package org.dangcat.net.rfc;

import org.dangcat.net.rfc.attribute.VendorManager;
import org.dangcat.net.rfc.template.AttributeTemplate;
import org.dangcat.net.rfc.template.VendorAttributeTemplateManager;
import org.dangcat.net.rfc.template.rules.PacketRuleValidator;
import org.dangcat.net.rfc.template.rules.RuleValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �����Թ���
 *
 * @author dangcat
 */
public class PacketAttributeManager {
    private Map<String, List<PacketVendorAttributeCollection>> packetAttributeMap = new HashMap<String, List<PacketVendorAttributeCollection>>();
    private PacketMetaInfo packetMetaInfo = null;

    public PacketAttributeManager(PacketMetaInfo packetMetaInfo) {
        this.packetMetaInfo = packetMetaInfo;
    }

    /**
     * ���ݳ��̺źͰ������ռ�����ģ�塣
     *
     * @param vendorId   ���̺š�
     * @param packetType �����͡�
     * @return �����õ������Լ��ϡ�
     */
    public List<AttributeTemplate> getAttributeTemplates(Integer vendorId, String packetType) {
        List<AttributeTemplate> attributeTemplateList = null;
        VendorAttributeTemplateManager vendorAttributeTemplateManager = this.packetMetaInfo.getAttributeTemplateManager().getVendorAttributeTemplateManager(vendorId);
        if (vendorAttributeTemplateManager != null) {
            PacketRuleValidator packetRuleValidator = vendorAttributeTemplateManager.getPacketRuleValidator(packetType);
            if (packetRuleValidator != null) {
                for (RuleValidator ruleValidator : packetRuleValidator) {
                    if (RuleValidator.MUSTNOT_PRESENT.equalsIgnoreCase(ruleValidator.getRuleType()))
                        continue;

                    AttributeTemplate attributeTemplate = vendorAttributeTemplateManager.getAttributeTemplate(ruleValidator.getName());
                    if (attributeTemplate != null) {
                        if (attributeTemplateList == null)
                            attributeTemplateList = new ArrayList<AttributeTemplate>();
                        attributeTemplateList.add(attributeTemplate);
                    }
                }
            }
        }
        return attributeTemplateList;
    }

    /**
     * ����ָ���İ����Ͷ�ȡ��������ģ�塣
     *
     * @param packetType �����͡�
     * @return �������Լ��ϡ�
     */
    public List<PacketVendorAttributeCollection> getVendorPacketAttributeMap(String packetType) {
        List<PacketVendorAttributeCollection> vendorPacketAttributeCollectionList = this.packetAttributeMap.get(packetType);
        if (vendorPacketAttributeCollectionList == null) {
            for (Integer vendorId : VendorManager.getAllVendorId()) {
                List<AttributeTemplate> attributeTemplateList = getAttributeTemplates(vendorId, packetType);
                if (attributeTemplateList != null) {
                    if (vendorPacketAttributeCollectionList == null) {
                        vendorPacketAttributeCollectionList = new ArrayList<PacketVendorAttributeCollection>();
                        this.packetAttributeMap.put(packetType, vendorPacketAttributeCollectionList);
                    }
                    vendorPacketAttributeCollectionList.add(new PacketVendorAttributeCollection(vendorId, VendorManager.getName(vendorId), attributeTemplateList));
                }
            }

        }
        return vendorPacketAttributeCollectionList;
    }
}
