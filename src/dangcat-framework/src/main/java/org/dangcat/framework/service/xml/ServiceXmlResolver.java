package org.dangcat.framework.service.xml;

import org.dangcat.commons.serialize.xml.PropertiesXmlResolver;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.framework.service.impl.ServiceInfo;

/**
 * ��λ�����������
 *
 * @author dangcat
 */
class ServiceXmlResolver extends XmlResolver {
    private static final String RESOLVER_NAME = "Service";

    /**
     * ������Ϣ��
     */
    private ServiceInfo serviceInfo = null;

    /**
     * ������������
     */
    ServiceXmlResolver() {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new PropertiesXmlResolver());
        this.addChildXmlResolver(new InterceptorsXmlResolver());
    }

    /**
     * ��ʼ����Ԫ�ر�ǩ��
     */
    @Override
    protected void startElement() {
        this.serviceInfo = new ServiceInfo();
        this.setResolveObject(this.serviceInfo);
    }
}
