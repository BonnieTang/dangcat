package org.dangcat.net.rfc.template;

import org.dangcat.commons.utils.ParseUtils;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.net.rfc.attribute.AttributeData;
import org.dangcat.net.rfc.attribute.AttributeDataType;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ʱ����������ģ�塣
 * @author dangcat
 * 
 */
public class TimeAttributeTemplate extends AttributeTemplate
{
    private static final int TIME_ATTRIBUTE_LENGTH = 4;
    /** ���ֵ�� */
    private Integer maxValue;
    /** ��Сֵ�� */
    private Integer minValue;

    /**
     * �ڱ�����ģ���Ͻ����µ����Զ���
     * @param value ����ֵ��
     * @return ���Զ���
     * @throws ProtocolParseException
     */
    public AttributeData createAttribute(Object value) throws ProtocolParseException
    {
        Integer time = null;
        if (value instanceof String)
            time = ValueUtils.parseInt((String) value);
        else if (value instanceof Integer)
            time = (Integer) value;
        if (time == null)
            throw new ProtocolParseException(ProtocolParseException.ATTRIBUTE_TYPEERROR, this.getFullName());
        return super.createAttribute(time);
    }

    /**
     * �������͡�
     */
    @Override
    public AttributeDataType getDataType()
    {
        return AttributeDataType.integer;
    }

    public Integer getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getMinValue()
    {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
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
        int value = attributeData.getValue();
        byte[] bytes = ParseUtils.toBytes(value, TIME_ATTRIBUTE_LENGTH);
        outputStream.write(bytes);
    }

    /**
     * �ɱ��Ľ������Զ���
     * @throws ProtocolParseException
     */
    @Override
    public AttributeData parse(byte[] bytes, int beginIndex, int length) throws ProtocolParseException
    {
        int value = ParseUtils.getInt(bytes, beginIndex, length);
        return this.createAttribute(value);
    }

    /**
     * ��֤�����Ƿ���Ч��
     */
    @Override
    public void validate(Object value) throws ProtocolValidateException
    {
        super.validate(value);

        Integer intvalue = (Integer) value;
        if (this.minValue != null && this.minValue.compareTo(intvalue) > 0)
            throw new ProtocolValidateException(ProtocolValidateException.ATTRIBUTE_INVALID_MINVALUE, this.getFullName(), value, this.minValue);
        if (this.maxValue != null && this.maxValue.compareTo(intvalue) < 0)
            throw new ProtocolValidateException(ProtocolValidateException.ATTRIBUTE_INVALID_MAXVALUE, this.getFullName(), value, this.maxValue);
    }
}
