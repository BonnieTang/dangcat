package org.dangcat.net.rfc.template.xml;

import org.dangcat.net.rfc.attribute.AttributeDataType;
import org.dangcat.net.rfc.template.AttributeTemplate;
import org.dangcat.net.rfc.template.IntegerAttributeTemplate;
import org.dangcat.net.rfc.template.StringAttributeTemplate;

import java.util.Map;

/**
 * �������ԡ�
 *
 * @author dangcat
 */
public class Attribute {
    /**
     * ���Զ������͡�
     */
    private String classType;
    /**
     * �������͡�
     */
    private AttributeDataType dataType;
    /**
     * �������ԡ�
     */
    private Boolean encrypt = Boolean.FALSE;
    /**
     * ���Գ��ȡ�
     */
    private Integer length;
    /**
     * ���ֵ��
     */
    private Integer max;
    /**
     * ��Сֵ��
     */
    private Integer min;
    /**
     * �������ơ�
     */
    private String name;
    /**
     * ѡ���б�
     */
    private Map<Integer, String> options = null;
    /**
     * �Ƿ����ѡ��У�顣
     */
    private boolean optionValidate = true;
    /**
     * ���Ա�š�
     */
    private Integer type;

    public AttributeTemplate createAttributeTemplate() {
        AttributeTemplate attributeTemplate = AttributeTemplate.createInstance(this.getDataType());
        if (attributeTemplate == null)
            throw new RuntimeException("The attribute " + this.getName() + "(" + this.getType() + ") is not found! ");
        attributeTemplate.setType(this.getType());
        attributeTemplate.setName(this.getName());
        attributeTemplate.setEncrypt(this.getEncrypt());
        if (attributeTemplate instanceof IntegerAttributeTemplate) {
            IntegerAttributeTemplate integerAttributeTemplate = (IntegerAttributeTemplate) attributeTemplate;
            integerAttributeTemplate.setLength(this.getLength());
            integerAttributeTemplate.setMinValue(this.getMin());
            integerAttributeTemplate.setMaxValue(this.getMax());
            if (this.getOptions() != null && this.getOptions().size() > 0) {
                integerAttributeTemplate.setOptionValidate(this.isOptionValidate());
                integerAttributeTemplate.getOptions().putAll(this.getOptions());
            }
        } else if (attributeTemplate instanceof StringAttributeTemplate) {
            StringAttributeTemplate stringAttributeTemplate = (StringAttributeTemplate) attributeTemplate;
            stringAttributeTemplate.setClassType(this.getClassType());
        }
        return attributeTemplate;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public AttributeDataType getDataType() {
        return dataType;
    }

    public void setDataType(AttributeDataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Boolean encrypt) {
        this.encrypt = encrypt;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, String> getOptions() {
        return options;
    }

    public void setOptions(Map<Integer, String> options) {
        this.options = options;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isOptionValidate() {
        return optionValidate;
    }

    public void setOptionValidate(boolean optionValidate) {
        this.optionValidate = optionValidate;
    }
}
