package org.dangcat.net.rfc.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.dangcat.commons.utils.ParseUtils;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.net.rfc.attribute.AttributeData;
import org.dangcat.net.rfc.attribute.AttributeDataType;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;

/**
 * ��������������ģ�塣
 * @author dangcat
 * 
 */
public class BigIntegerAttributeTemplate extends NumberAttributeTemplate<Long>
{
    private static final int BIGINTEGER_ATTRIBUTE_LENGTH = 8;

    /**
     * �ڱ�����ģ���Ͻ����µ����Զ���
     * @param value ����ֵ��
     * @return ���Զ���
     * @throws ProtocolParseException
     */
    public AttributeData createAttribute(Object value) throws ProtocolParseException
    {
        Long longValue = null;
        if (value instanceof String)
            longValue = ValueUtils.parseLong((String) value);
        else if (value instanceof Long)
            longValue = (Long) value;
        if (longValue == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_TYPEERROR, this.getFullName());
        return super.createAttribute(longValue);
    }

    /**
     * �������͡�
     */
    @Override
    public AttributeDataType getDataType()
    {
        return AttributeDataType.bigInteger;
    }

    @Override
    public int getMaxLength()
    {
        return BIGINTEGER_ATTRIBUTE_LENGTH;
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
        long value = (Long) attributeData.getValue();
        byte[] bytes = ParseUtils.toBytes(value, this.getLength());
        outputStream.write(bytes);
    }

    /**
     * �ɱ��Ľ������Զ���
     * @throws ProtocolParseException
     */
    @Override
    public AttributeData parse(byte[] bytes, int beginIndex, int length) throws ProtocolParseException
    {
        long value = ParseUtils.getLong(bytes, beginIndex, length);
        return this.createAttribute(value);
    }
}
