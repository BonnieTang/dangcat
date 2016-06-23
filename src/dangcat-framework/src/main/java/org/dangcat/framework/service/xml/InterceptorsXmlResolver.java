package org.dangcat.framework.service.xml;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.commons.utils.ValueUtils;
import org.dom4j.Element;

import java.util.Collection;

/**
 * �����������������
 *
 * @author dangcat
 */
public class InterceptorsXmlResolver extends XmlResolver {
    protected static final String RESOLVER_NAME = "Interceptors";
    private static final String CHILDELEMENT_NAME = "Interceptor";

    /**
     * ������������
     */
    public InterceptorsXmlResolver() {
        super(RESOLVER_NAME);
    }

    @SuppressWarnings("unchecked")
    public Collection<Class<?>> getInterceptors() {
        return (Collection<Class<?>>) this.getResolveObject();
    }

    /**
     * ��ʼ������Ԫ�ر�ǩ��
     *
     * @param element ��Ԫ�����ơ�
     */
    protected void resolveChildElement(Element element) {
        if (CHILDELEMENT_NAME.equalsIgnoreCase(element.getName()) && !ValueUtils.isEmpty(element.getText())) {
            Class<?> interceptorClass = ReflectUtils.loadClass(element.getTextTrim());
            if (interceptorClass == null)
                logger.error("The interceptor class " + element.getTextTrim() + " is not found.");
            else
                this.getInterceptors().add(interceptorClass);
        }
    }
}
