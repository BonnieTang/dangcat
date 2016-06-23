package org.dangcat.framework.service.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.commons.utils.Environment;
import org.dangcat.framework.service.impl.ServiceInfo;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * ��������������
 * @author dangcat
 * 
 */
public class ServicesXmlResolver extends XmlResolver
{
    private static final String RESOLVER_NAME = "Services";
    /** ���������á� */
    private Collection<Class<?>> interceptors = new LinkedHashSet<Class<?>>();
    /** ���õ��ӷ����б� */
    private List<ServiceInfo> serviceInfos = new LinkedList<ServiceInfo>();

    /**
     * ������������
     */
    public ServicesXmlResolver()
    {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new ServiceXmlResolver());
        this.addChildXmlResolver(new InterceptorsXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    @Override
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child instanceof ServiceInfo)
            this.serviceInfos.add((ServiceInfo) child);
    }

    /**
     * ������Ԫ��֮ǰ��
     * @param name �������ơ�
     * @param xmlResolver ��������
     */
    @Override
    protected void beforeChildResolve(String elementName, XmlResolver xmlResolver)
    {
        if (InterceptorsXmlResolver.RESOLVER_NAME.equalsIgnoreCase(xmlResolver.getName()))
            xmlResolver.setResolveObject(this.getInterceptors());
    }

    public Collection<Class<?>> getInterceptors()
    {
        return this.interceptors;
    }

    public List<ServiceInfo> getServiceInfos()
    {
        return this.serviceInfos;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("Services: ");
        for (ServiceInfo serviceInfo : this.serviceInfos)
        {
            info.append(Environment.LINETAB_SEPARATOR);
            info.append(serviceInfo);
        }
        info.append(Environment.LINE_SEPARATOR);
        info.append("Interceptors: ");
        for (Class<?> interceptor : this.interceptors)
        {
            info.append(Environment.LINETAB_SEPARATOR);
            info.append(interceptor.getName());
        }
        return info.toString();
    }
}
