package org.dangcat.net.rfc.template;

import org.apache.log4j.Logger;
import org.dangcat.net.rfc.attribute.AttributeData;
import org.dangcat.net.rfc.attribute.AttributeDataType;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ����ģ�塣
 *
 * @author dangcat
 */
public abstract class AttributeTemplate {
    public static final int OFFSET = 2;
    protected static final Logger logger = Logger.getLogger(AttributeTemplate.class);
    /**
     * �Ƿ���ܡ�
     */
    private boolean encrypt = false;
    /**
     * �������ơ�
     */
    private String name;
    /**
     * �����б�
     */
    private Map<String, Object> params = null;
    /**
     * ���Ա�š�
     */
    private Integer type;
    /**
     * ����ģ�����
     */
    private VendorAttributeTemplateManager vendorAttributeTemplateManager = null;

    /**
     * ��������ģ�塣
     *
     * @param dataType �������͡�
     * @return ����������ģ�塣
     */
    public static AttributeTemplate createInstance(AttributeDataType dataType) {
        AttributeTemplate attributeTemplate = null;
        if (AttributeDataType.address.equals(dataType))
            attributeTemplate = new AddressAttributeTemplate();
        else if (AttributeDataType.integer.equals(dataType))
            attributeTemplate = new IntegerAttributeTemplate();
        else if (AttributeDataType.bigInteger.equals(dataType))
            attributeTemplate = new BigIntegerAttributeTemplate();
        else if (AttributeDataType.string.equals(dataType))
            attributeTemplate = new StringAttributeTemplate();
        else if (AttributeDataType.text.equals(dataType))
            attributeTemplate = new TextAttributeTemplate();
        else if (AttributeDataType.time.equals(dataType))
            attributeTemplate = new TimeAttributeTemplate();
        else if (AttributeDataType.vendor.equals(dataType))
            attributeTemplate = new VendorAttributeTemplate();
        return attributeTemplate;
    }

    /**
     * �ڱ�����ģ���Ͻ����µ����Զ���
     *
     * @param value ����ֵ��
     * @return ���Զ���
     * @throws ProtocolValidateException
     */
    public AttributeData createAttribute(Object value) throws ProtocolParseException {
        if (value == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_ERROR, this.getFullName());
        return value == null ? null : new AttributeData(this, value);
    }

    /**
     * �������͡�
     */
    public abstract AttributeDataType getDataType();

    /**
     * ����ȫ���ơ�
     */
    public String getFullName() {
        return this.name + "(" + this.type + ")";
    }

    /**
     * ������󳤶ȡ�
     */
    public int getMaxLength() {
        return -1;
    }

    /**
     * �������ơ�
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getParams() {
        if (this.params == null)
            this.params = new HashMap<String, Object>();
        return this.params;
    }

    /**
     * ���Ա�š�
     */
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public VendorAttributeTemplateManager getVendorAttributeTemplateManager() {
        return vendorAttributeTemplateManager;
    }

    protected void setVendorAttributeTemplateManager(VendorAttributeTemplateManager vendorAttributeTemplateManager) {
        this.vendorAttributeTemplateManager = vendorAttributeTemplateManager;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * ת�����ֽ����顣
     *
     * @param outputStream ���������
     * @param attribute    ����ֵ��
     * @return ת������ֽڡ�
     */
    protected abstract void outputValue(ByteArrayOutputStream outputStream, AttributeData attribute) throws IOException;

    /**
     * �������ԡ�
     *
     * @param bytes      ԭʼ���ݡ�
     * @param beginIndex ��ʼ����λ�á�
     * @param length     ���ȡ�
     * @return ���Զ���
     */
    public abstract AttributeData parse(byte[] bytes, int beginIndex, int length) throws ProtocolParseException;

    /**
     * ת�����ֽ����顣
     *
     * @param attributeData ����ֵ��
     * @return ת������ֽڡ�
     */
    public byte[] toBytes(AttributeData attributeData) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] header = new byte[2];
            header[0] = this.getType().byteValue();
            header[1] = (byte) 0;
            outputStream.write(header);
            this.outputValue(outputStream, attributeData);
            bytes = outputStream.toByteArray();
            bytes[1] = (byte) bytes.length;
        } catch (Exception e) {
        }
        return bytes;
    }

    public String toString(Object value) {
        return value.toString();
    }

    /**
     * ��֤�����Ƿ���Ч��
     */
    public void validate(Object value) throws ProtocolValidateException {
        if (value == null)
            throw new ProtocolValidateException(ProtocolValidateException.ATTRIBUTE_ISNULL, this.getFullName());
    }
}
