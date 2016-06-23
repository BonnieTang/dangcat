package org.dangcat.net.rfc.template;

import org.apache.log4j.Logger;
import org.dangcat.commons.io.FileUtils;
import org.dangcat.commons.io.Resource;
import org.dangcat.commons.io.ResourceLoader;
import org.dangcat.net.rfc.PacketMetaInfo;
import org.dangcat.net.rfc.attribute.VendorManager;
import org.dangcat.net.rfc.template.rules.PacketRuleValidator;
import org.dangcat.net.rfc.template.rules.RuleValidator;
import org.dangcat.net.rfc.template.xml.AttributesXmlResolver;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * ����ģ���������
 *
 * @author dangcat
 */
public class AttributeTemplateManager {
    public static final Integer DEFAULT_VENDORID = 0;
    protected static final Logger logger = Logger.getLogger(AttributeTemplateManager.class);
    private static final String RFC_PREFIX = "rfc-";

    /**
     * ��Ԫ���ݡ�
     */
    private PacketMetaInfo packetMetaInfo = null;
    /**
     * ����������ģ�������ӳ���
     */
    private Map<Integer, VendorAttributeTemplateManager> vendorAttributeTemplateManagerMap = new HashMap<Integer, VendorAttributeTemplateManager>();

    public AttributeTemplateManager(PacketMetaInfo packetMetaInfo) {
        this.packetMetaInfo = packetMetaInfo;
    }

    /**
     * ͨ���������ͺŶ�ȡ����ģ�塣
     *
     * @param type ���Ժš�
     * @return ����ģ�塣
     */
    public AttributeTemplate getAttributeTemplate(Integer type) {
        return this.getAttributeTemplate(DEFAULT_VENDORID, type);
    }

    /**
     * ͨ�����̡��������ͺŶ�ȡ����ģ�塣
     *
     * @param vendorId ���̺š�
     * @param type     ���Ժš�
     * @return ����ģ�塣
     */
    public AttributeTemplate getAttributeTemplate(Integer vendorId, Integer type) {
        VendorAttributeTemplateManager vendorAttributeTemplateManager = this.getVendorAttributeTemplateManager(vendorId);
        return vendorAttributeTemplateManager == null ? null : vendorAttributeTemplateManager.getAttributeTemplate(type);
    }

    /**
     * ͨ�����̡��������ͺŶ�ȡ����ģ�塣
     *
     * @param vendorId ���̺š�
     * @param name     ��������
     * @return ����ģ�塣
     */
    public AttributeTemplate getAttributeTemplate(Integer vendorId, String name) {
        VendorAttributeTemplateManager vendorAttributeTemplateManager = this.getVendorAttributeTemplateManager(vendorId);
        return vendorAttributeTemplateManager == null ? null : vendorAttributeTemplateManager.getAttributeTemplate(name);
    }

    public Collection<AttributeTemplate> getAttributeTemplates(Integer vendorId, String packetType) {
        List<AttributeTemplate> attributeTemplates = null;
        VendorAttributeTemplateManager vendorAttributeTemplateManager = this.getVendorAttributeTemplateManager(vendorId);
        if (vendorAttributeTemplateManager != null) {
            PacketRuleValidator packetRuleValidator = vendorAttributeTemplateManager.getPacketRuleValidator(packetType);
            if (packetRuleValidator != null) {
                for (RuleValidator ruleValidator : packetRuleValidator) {
                    if (RuleValidator.MUSTNOT_PRESENT.equalsIgnoreCase(ruleValidator.getRuleType()))
                        continue;

                    AttributeTemplate attributeTemplate = vendorAttributeTemplateManager.getAttributeTemplate(ruleValidator.getName());
                    if (attributeTemplate != null) {
                        if (attributeTemplates == null)
                            attributeTemplates = new ArrayList<AttributeTemplate>();
                        attributeTemplates.add(attributeTemplate);
                    }
                }
            }
        }
        return attributeTemplates;
    }

    /**
     * ���ݰ����͵õ���ǰ����У������
     *
     * @param packetType �����͡�
     * @return У������
     */
    public PacketRuleValidator getPacketRuleValidator(Integer vendorId, String packetType) {
        VendorAttributeTemplateManager vendorAttributeTemplateManager = this.getVendorAttributeTemplateManager(vendorId);
        return vendorAttributeTemplateManager == null ? null : vendorAttributeTemplateManager.getPacketRuleValidator(packetType);
    }

    /**
     * ��ȡ���Ժ�ģ���������
     *
     * @param vendorId ���̺š�
     * @return ����ģ�塣
     */
    public VendorAttributeTemplateManager getVendorAttributeTemplateManager(Integer vendorId) {
        return this.vendorAttributeTemplateManagerMap.get(vendorId == null ? DEFAULT_VENDORID : vendorId);
    }

    private Integer getVendorId(String name) {
        Integer vendorId = null;
        String vendorName = this.getVendorName(name);
        if (!vendorName.toLowerCase().startsWith(RFC_PREFIX))
            vendorId = VendorManager.parse(vendorName);
        return vendorId == null ? DEFAULT_VENDORID : vendorId;
    }

    /**
     * ���ļ�����ȡ��������
     *
     * @param file �ļ�����
     * @return ��������
     */
    private String getVendorName(String resourceName) {
        return resourceName.replace(this.packetMetaInfo.getFileNamePrefix(), "").replace(this.packetMetaInfo.getFileNamePostfix(), "");
    }

    /**
     * ��ʼ������ģ�塣
     */
    public void initialize() {
        String fileNamePreFix = this.packetMetaInfo.getFileNamePrefix();
        String fileNamePostFix = this.packetMetaInfo.getFileNamePostfix();
        ResourceLoader resourceLoader = new ResourceLoader(this.packetMetaInfo.getPacketTypeClass(), fileNamePreFix, fileNamePostFix);
        try {
            resourceLoader.load();
            if (resourceLoader.getResourceList().size() == 0)
                logger.error("No attributes files is found: Prefix=(" + fileNamePreFix + ") Postfix=(" + fileNamePostFix + ")");
            else {
                for (Resource resource : resourceLoader.getResourceList()) {
                    Integer vendorId = this.getVendorId(resource.getName());
                    if (vendorId == null) {
                        logger.error("The vendor resource name is invalid : " + resource.getName());
                        continue;
                    }
                    logger.info("Load resource from " + resource + " by " + this.packetMetaInfo.getPacketTypeClass().getSimpleName());
                    this.load(resource.getInputStream(), vendorId);
                }
            }
        } finally {
            resourceLoader.close();
        }
    }

    /**
     * ���ļ�����������Դ����
     *
     * @param inputStream ��������
     * @param vendorId    ���̺š�
     */
    public void load(File file) {
        if (file == null || !file.exists() || !file.isFile())
            return;

        Integer vendorId = this.getVendorId(file.getName());
        if (vendorId == null) {
            logger.error("The attribut file is invalid : " + file.getAbsolutePath());
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            this.load(inputStream, vendorId);
        } catch (FileNotFoundException e) {
        } finally {
            inputStream = FileUtils.close(inputStream);
        }
    }

    /**
     * ������������������Դ����
     *
     * @param inputStream ��������
     * @param vendorId    ���̺š�
     */
    private void load(InputStream inputStream, Integer vendorId) {
        VendorAttributeTemplateManager vendorAttributeTemplateManager = this.getVendorAttributeTemplateManager(vendorId);
        if (vendorAttributeTemplateManager == null)
            vendorAttributeTemplateManager = new VendorAttributeTemplateManager(vendorId, this);

        try {
            AttributesXmlResolver attributesXmlResolver = new AttributesXmlResolver();
            attributesXmlResolver.setVendorAttributeTemplateManager(vendorAttributeTemplateManager);
            attributesXmlResolver.open(inputStream);
            attributesXmlResolver.resolve();
            if (!this.vendorAttributeTemplateManagerMap.containsKey(vendorId))
                this.vendorAttributeTemplateManagerMap.put(vendorId, vendorAttributeTemplateManager);
        } catch (DocumentException e) {
            logger.error(this, e);
        }
    }
}
