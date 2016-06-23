package org.dangcat.commons.serialize.xml;

import org.dangcat.commons.reflect.ReflectUtils;

/**
 * ���������������
 *
 * @author dangcat
 */
public class ParamXmlResolver extends XmlResolver {
    /**
     * ��������
     */
    private Param param = null;

    /**
     * ������������
     */
    public ParamXmlResolver() {
        this(Param.class.getSimpleName());
    }

    /**
     * ������������
     */
    public ParamXmlResolver(String name) {
        super(name == null ? Param.class.getSimpleName() : name);
    }

    /**
     * �����ı���
     *
     * @param value �ı�ֵ��
     */
    @Override
    protected void resolveElementText(String value) {
        this.param.setValue(ReflectUtils.parseValue(this.param.getClassType(), value));
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement() {
        this.param = new Param();
        this.setResolveObject(this.param);
    }
}
