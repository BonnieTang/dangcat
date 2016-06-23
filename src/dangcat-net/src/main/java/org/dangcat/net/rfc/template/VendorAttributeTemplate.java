package org.dangcat.net.rfc.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.dangcat.commons.utils.ParseUtils;
import org.dangcat.net.rfc.attribute.AttributeCollection;
import org.dangcat.net.rfc.attribute.AttributeData;
import org.dangcat.net.rfc.attribute.AttributeDataType;
import org.dangcat.net.rfc.attribute.VendorAttribute;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;

/**
 * ��������������ģ�塣
 * @author dangcat
 * 
 */
public class VendorAttributeTemplate extends AttributeTemplate
{
    private static final int VENDORID_MAXLENGTH = 4;

    /**
     * �ڱ�����ģ���Ͻ����µ����Զ���
     * @param value ����ֵ��
     * @return ���Զ���
     */
    @Override
    public AttributeData createAttribute(Object value)
    {
        return value == null ? null : new VendorAttribute(this, (Integer) value);
    }

    /**
     * �������͡�
     */
    @Override
    public AttributeDataType getDataType()
    {
        return AttributeDataType.string;
    }

    /**
     * ת�����ֽ����顣
     * @param outputStream ���������
     * @param attributeData ����ֵ��
     * @return ת������ֽڡ�
     * @throws IOException
     */
    protected void outputValue(ByteArrayOutputStream outputStream, AttributeData attributeData) throws IOException
    {
        VendorAttribute vendorAttribute = (VendorAttribute) attributeData;
        AttributeCollection attributeCollection = vendorAttribute.getAttributeCollection();
        byte[] vendorBytes = ParseUtils.toBytes(attributeCollection.getVendorId(), VENDORID_MAXLENGTH);
        outputStream.write(vendorBytes);
        outputStream.write(attributeCollection.getAttributesBytes());
    }

    /**
     * �ɱ��Ľ������Զ���
     * @throws ProtocolParseException
     */
    @Override
    public AttributeData parse(byte[] bytes, int beginIndex, int length) throws ProtocolParseException
    {
        int vendorId = ParseUtils.getInt(bytes, beginIndex, VENDORID_MAXLENGTH);
        beginIndex += VENDORID_MAXLENGTH;

        VendorAttribute vendorAttribute = new VendorAttribute(this, vendorId);
        AttributeTemplateManager attributeTemplateManager = this.getVendorAttributeTemplateManager().getAttributeTemplateManager();
        VendorAttributeTemplateManager vendorAttributeTemplateManager = attributeTemplateManager.getVendorAttributeTemplateManager(vendorId);
        if (vendorAttributeTemplateManager == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_NOTSUPPORT, this.getFullName());
        vendorAttribute.getAttributeCollection().setVendorAttributeTemplateManager(vendorAttributeTemplateManager);
        vendorAttribute.getAttributeCollection().parse(bytes, beginIndex, length - VENDORID_MAXLENGTH);
        return vendorAttribute;
    }
}
