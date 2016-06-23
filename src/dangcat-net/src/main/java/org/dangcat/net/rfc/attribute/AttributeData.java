package org.dangcat.net.rfc.attribute;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.net.rfc.exceptions.ProtocolParseException;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;
import org.dangcat.net.rfc.template.AttributeTemplate;
import org.dangcat.net.rfc.template.ValueParser;

/**
 * ���Զ���ӿڡ�
 *
 * @author dangcat
 */
public class AttributeData implements Comparable<AttributeData> {
    protected static final Logger logger = Logger.getLogger(AttributeData.class);
    /**
     * ����ģ�塣
     */
    private AttributeTemplate attributeTemplate = null;
    /**
     * ��������ֽ����顣
     */
    private byte[] bytes = null;
    /**
     * ���Գ��ȡ�
     */
    private int length = 0;
    /**
     * �������Լ��ϡ�
     */
    private AttributeCollection parent = null;
    /**
     * ����ֵ��
     */
    private Integer type = null;
    /**
     * ����ֵ��
     */
    private Object value = null;

    public AttributeData(AttributeTemplate attributeTemplate) {
        this.attributeTemplate = attributeTemplate;
    }

    public AttributeData(AttributeTemplate attributeTemplate, Object value) {
        this.attributeTemplate = attributeTemplate;
        this.value = value;
    }

    /**
     * �Ƚ������������ݡ�
     */
    @Override
    public int compareTo(AttributeData attributeData) {
        if (attributeData == null)
            return 1;

        int result = ValueUtils.compare(this.getType(), attributeData.getType());
        if (result != 0)
            return result;

        Object srcValue = this.getValue();
        Object dstValue = attributeData.getValue();
        if (srcValue instanceof ValueParser && dstValue instanceof ValueParser) {
            ValueParser valueParser = (ValueParser) srcValue;
            return valueParser.compareTo(dstValue);
        }

        return ValueUtils.compare(srcValue, dstValue);
    }

    public int compareValue(Object value) throws ProtocolParseException {
        return this.compareTo(this.getAttributeTemplate().createAttribute(value));
    }

    /**
     * ����ģ�塣
     */
    public AttributeTemplate getAttributeTemplate() {
        return this.attributeTemplate;
    }

    public void setAttributeTemplate(AttributeTemplate attributeTemplate) {
        this.attributeTemplate = attributeTemplate;
    }

    public int getLength() {
        if (this.length == 0)
            this.length = this.toBytes().length;
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    protected AttributeCollection getParent() {
        return parent;
    }

    protected void setParent(AttributeCollection parent) {
        this.parent = parent;
    }

    public Integer getType() {
        if (this.type == null)
            this.type = this.getAttributeTemplate().getType();
        return this.type;
    }

    /**
     * ��ȡ����ֵ��
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) this.value;
    }

    /**
     * ��������ֵ��
     */
    public void setValue(Object value) {
        this.value = value;
        this.bytes = null;
        this.length = 0;
        this.notifyChanged();
    }

    public String getValueText() {
        return this.attributeTemplate.toString(this.getValue());
    }

    public Integer getVendorId() {
        return this.getAttributeTemplate().getVendorAttributeTemplateManager().getVendorId();
    }

    public boolean isEmpty() {
        return ValueUtils.isEmpty(this.value);
    }

    public void notifyChanged() {
        if (this.getParent() != null)
            this.getParent().onAttributeChanged(this);
    }

    /**
     * ����ֵ��
     */
    public byte[] toBytes() {
        if (this.bytes == null) {
            this.bytes = this.attributeTemplate.toBytes(this);
            this.length = bytes.length;
        }
        return this.bytes;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        String lengthText = "   " + this.getLength();
        info.append(lengthText.substring(lengthText.length() - 3));
        info.append(" ");
        info.append(this.attributeTemplate.getFullName());
        info.append(" = ");
        info.append(this.getValueText());
        return info.toString();
    }

    /**
     * �����Ƿ���Ч��
     */
    public void validate() throws ProtocolValidateException {
        try {
            this.attributeTemplate.validate(this.getValue());
        } catch (ProtocolValidateException e) {
            e.setAttributeData(this);
            throw e;
        }
    }
}
