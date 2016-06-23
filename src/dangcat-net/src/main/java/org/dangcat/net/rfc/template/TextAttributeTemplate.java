package org.dangcat.net.rfc.template;

import org.dangcat.net.rfc.attribute.AttributeData;
import org.dangcat.net.rfc.attribute.AttributeDataType;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * �ִ���������ģ�塣
 * @author dangcat
 * 
 */
public class TextAttributeTemplate extends AttributeTemplate
{
    private static final int TEXT_MAXLENGTH = 253;

    /**
     * �ڱ�����ģ���Ͻ����µ����Զ���
     * @param value ����ֵ��
     * @return ���Զ���
     * @throws ProtocolParseException
     */
    public AttributeData createAttribute(Object value) throws ProtocolParseException
    {
        String text = null;
        if (value instanceof String)
            text = (String) value;
        if (text == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_TYPEERROR, this.getFullName());
        return super.createAttribute(text);
    }

    /**
     * �������͡�
     */
    @Override
    public AttributeDataType getDataType()
    {
        return AttributeDataType.text;
    }

    @Override
    public int getMaxLength()
    {
        return TEXT_MAXLENGTH;
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
        String textValue = attributeData.getValue();
        byte[] bytes = textValue.getBytes();
        outputStream.write(bytes, 0, bytes.length > TEXT_MAXLENGTH ? TEXT_MAXLENGTH : bytes.length);
    }

    /**
     * �ɱ��Ľ������Զ���
     * @throws ProtocolParseException
     */
    @Override
    public AttributeData parse(byte[] bytes, int beginIndex, int length) throws ProtocolParseException
    {
        byte[] value = new byte[length];
        System.arraycopy(bytes, beginIndex, value, 0, value.length);
        return this.createAttribute(new String(value));
    }

    @Override
    public void validate(Object value) throws ProtocolValidateException
    {
        super.validate(value);

        String textValue = (String) value;
        byte[] bytes = textValue.getBytes();
        if (bytes == null || bytes.length == 0 || bytes.length > TEXT_MAXLENGTH)
            throw new ProtocolValidateException(ProtocolValidateException.ATTRIBUTE_INVALID_LENGTH, this.getFullName(), bytes.length, 1, this.getMaxLength());
    }
}
