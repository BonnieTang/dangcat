package org.dangcat.net.rfc.template;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.utils.ParseUtils;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.net.rfc.attribute.AttributeData;
import org.dangcat.net.rfc.attribute.AttributeDataType;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ��������������ģ�塣
 *
 * @author dangcat
 */
public class StringAttributeTemplate extends AttributeTemplate {
    private static final int STRING_MAXLENGTH = 253;
    /**
     * ���Զ������͡�
     */
    private String classType;

    /**
     * �ڱ�����ģ���Ͻ����µ����Զ���
     *
     * @param value ����ֵ��
     * @return ���Զ���
     * @throws ProtocolParseException
     */
    public AttributeData createAttribute(Object value) throws ProtocolParseException {
        AttributeData attributeData = this.createCustom(value);
        if (attributeData != null)
            return attributeData;

        Object data = null;
        if (value instanceof String)
            data = ParseUtils.parseHex((String) value);
        else if (value instanceof byte[])
            data = value;
        if (data == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_TYPEERROR, this.getFullName());
        return super.createAttribute(data);
    }

    private AttributeData createCustom(Object value) throws ProtocolParseException {
        if (ValueUtils.isEmpty(this.getClassType()))
            return null;

        if (value instanceof ValueParser)
            return super.createAttribute(value);

        ValueParser valueParser = (ValueParser) ReflectUtils.newInstance(this.getClassType(), new Class<?>[]{AttributeTemplate.class}, new Object[]{this});
        if (valueParser == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_INVALID_CLASSTYPE, this.getFullName(), this.getClassType());

        if (value instanceof String)
            valueParser.parse((String) value);
        else if (value instanceof byte[]) {
            byte[] bytes = (byte[]) value;
            valueParser.parse(bytes, 0, bytes.length);
        }
        if (valueParser.toString() == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_TYPEERROR, this.getFullName());
        return super.createAttribute(valueParser);
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    /**
     * �������͡�
     */
    @Override
    public AttributeDataType getDataType() {
        return AttributeDataType.string;
    }

    @Override
    public int getMaxLength() {
        return STRING_MAXLENGTH;
    }

    /**
     * ת�����ֽ����顣
     *
     * @param outputStream  ���������
     * @param attributeData ����ֵ��
     * @return ת������ֽڡ�
     * @throws IOException
     */
    protected void outputValue(ByteArrayOutputStream outputStream, AttributeData attributeData) throws IOException {
        byte[] bytes = null;
        if (attributeData.getValue() instanceof ValueParser) {
            ValueParser valueParser = attributeData.getValue();
            bytes = valueParser.toBytes();
        } else if (attributeData.getValue() instanceof byte[])
            bytes = attributeData.getValue();
        if (bytes != null && bytes.length > 0)
            outputStream.write(bytes, 0, bytes.length > STRING_MAXLENGTH ? STRING_MAXLENGTH : bytes.length);
    }

    /**
     * �ɱ��Ľ������Զ���
     *
     * @throws ProtocolParseException
     */
    @Override
    public AttributeData parse(byte[] bytes, int beginIndex, int length) throws ProtocolParseException {
        AttributeData attributeData = this.parseCustom(bytes, beginIndex, length);
        if (attributeData != null)
            return attributeData;

        byte[] value = new byte[length];
        System.arraycopy(bytes, beginIndex, value, 0, value.length);
        return this.createAttribute(value);
    }

    private AttributeData parseCustom(byte[] bytes, int beginIndex, int length) throws ProtocolParseException {
        if (ValueUtils.isEmpty(this.getClassType()))
            return null;

        ValueParser valueParser = (ValueParser) ReflectUtils.newInstance(this.getClassType(), new Class<?>[]{AttributeTemplate.class}, new Object[]{this});
        if (valueParser == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_INVALID_CLASSTYPE, this.getFullName(), this.getClassType());
        valueParser.parse(bytes, beginIndex, length);
        return this.createAttribute(valueParser);
    }

    @Override
    public String toString(Object value) {
        if (value instanceof byte[])
            return ParseUtils.toHex((byte[]) value);
        return super.toString(value);
    }

    @Override
    public void validate(Object value) throws ProtocolValidateException {
        super.validate(value);

        if (value instanceof ValueParser) {
            ValueParser valueParser = (ValueParser) value;
            valueParser.validate();
            return;
        }

        byte[] bytes = (byte[]) value;
        if (bytes == null || bytes.length == 0 || bytes.length > STRING_MAXLENGTH)
            throw new ProtocolValidateException(ProtocolValidateException.ATTRIBUTE_INVALID_LENGTH, this.getFullName(), bytes.length, 1, this.getMaxLength());
    }
}
