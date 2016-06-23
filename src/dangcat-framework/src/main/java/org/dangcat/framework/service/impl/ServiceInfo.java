package org.dangcat.framework.service.impl;

import org.apache.log4j.Logger;
import org.dangcat.commons.reflect.MethodInfo;
import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.resource.ResourceReader;
import org.dangcat.commons.resource.ResourceReaderCollection;
import org.dangcat.commons.serialize.xml.Property;
import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.conf.ConfigProvider;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.PermissionProvider;
import org.dangcat.framework.service.ServiceContext;
import org.dangcat.framework.service.ServiceParams;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.annotation.Context;
import org.dangcat.framework.service.interceptor.AfterInterceptor;
import org.dangcat.framework.service.interceptor.BeforeInterceptor;
import org.dangcat.framework.service.resource.ExceptionReader;

import java.lang.reflect.Field;
import java.util.*;

/**
 * ������Ϣ��
 * @author dangcat
 * 
 */
public class ServiceInfo extends ServiceParams
{
    public static final String DEFAULT_MODULE = "Default";
    protected static final Logger logger = Logger.getLogger(ServiceInfo.class);
    private static final long serialVersionUID = 1L;
    private static final String SERVICE_TITLE = "service.title";

    /** ����������͡� */
    private Class<?> accessClassType = null;
    /** ������������� */
    private String accessType = null;
    /** �º�������ӳ��� */
    private Map<Class<?>, AfterInterceptor> afterInvokeMap = new LinkedHashMap<Class<?>, AfterInterceptor>();
    /** ��ǰ������ӳ��� */
    private Map<Class<?>, BeforeInterceptor> beforeInvokeMap = new LinkedHashMap<Class<?>, BeforeInterceptor>();
    /** ����ֵ�� */
    private ConfigProvider configProvider = null;
    /** �쳣��Ϣ��ȡ���� */
    private ExceptionReader exceptionReader = null;
    /** ģ���š� */
    private Integer id = null;
    /** ����ʵ���� */
    private Object instance = null;
    /** ���������ñ� */
    private Collection<Class<?>> interceptors = new LinkedHashSet<Class<?>>();
    /** �����Ƿ�ػ��� */
    private boolean isPool = false;
    /** �����Ƿ�ʹ�ô��� */
    private boolean isProxy = false;
    /** �����󶨡� */
    private String jndiName = null;
    /** ģ�������� */
    private String moduleName = null;
    /** ���������� */
    private String name = null;
    /** Ȩ�����á� */
    private PermissionProvider permissionProvider = null;
    /** �������ԡ� */
    private List<Property> properties = new ArrayList<Property>();
    /** ��Դ��ȡ���� */
    private ResourceReader resourceReader = null;
    /** ����ʵ�����͡� */
    private Class<?> serviceClassType = null;
    /** ������Ϣ�� */
    private ServiceMethodInfo serviceMethodInfo = null;
    /** ����ʵ�������� */
    private String serviceType = null;

    public void addInterceptors(Object... interceptors)
    {
        for (Object interceptor : interceptors)
        {
            Class<?> classType = interceptor.getClass();
            if (interceptor instanceof BeforeInterceptor && !this.beforeInvokeMap.containsKey(classType))
                this.beforeInvokeMap.put(classType, (BeforeInterceptor) interceptor);
            if (interceptor instanceof AfterInterceptor && !this.afterInvokeMap.containsKey(classType))
                this.afterInvokeMap.put(classType, (AfterInterceptor) interceptor);
        }
    }

    public void afterInvoke(Object service, ServiceContext serviceContext, MethodInfo methodInfo, Object[] args, Object result)
    {
        for (AfterInterceptor afterInterceptor : this.afterInvokeMap.values())
            afterInterceptor.afterInvoke(service, serviceContext, methodInfo, args, result);
    }

    public void beforeInvoke(Object service, ServiceContext serviceContext, MethodInfo methodInfo, Object[] args) throws ServiceException
    {
        for (BeforeInterceptor beforeInterceptor : this.beforeInvokeMap.values())
            beforeInterceptor.beforeInvoke(service, serviceContext, methodInfo, args);
    }

    public Object createInstance(ServiceProvider parent)
    {
        return ServiceUtils.createInstance(this, parent);
    }

    public Class<?> getAccessClassType()
    {
        return this.accessClassType;
    }

    public void setAccessClassType(Class<?> accessClassType) {
        this.accessClassType = accessClassType;
    }

    public String getAccessType()
    {
        return this.accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public ConfigProvider getConfigProvider()
    {
        return this.configProvider;
    }

    protected void setConfigProvider(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    public String getException(Locale locale, Integer messageId)
    {
        if (this.exceptionReader == null)
        {
            ServiceMethodInfo serviceMethodInfo = this.getServiceMethodInfo();
            Collection<Class<?>> classTypes = serviceMethodInfo.getResourceClassTypes();
            this.exceptionReader = new ExceptionReader(classTypes.toArray(new Class<?>[0]));
        }
        return this.exceptionReader.getText(locale, messageId);
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getInstance()
    {
        return this.instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Collection<Class<?>> getInterceptors()
    {
        return this.interceptors;
    }

    public String getJndiName()
    {
        return this.jndiName;
    }

    public void setJndiName(String jndiName) {
        String[] values = jndiName.split("/");
        if (values.length > 1 && !ValueUtils.isEmpty(values[0])) {
            this.setModuleName(values[0]);
            this.setName(values[1]);
        } else if (values.length > 0) {
            this.setModuleName(DEFAULT_MODULE);
            this.setName(values[0]);
        }
        this.jndiName = this.getModuleName() + "/" + this.getName();
    }

    public String getMethodTitle(Locale locale, String name)
    {
        if (locale == null)
            locale = Environment.getDefaultLocale();

        return this.getResourceReader().getText(locale, "service." + name + ".title");
    }

    public String getModuleName()
    {
        return this.moduleName;
    }

    protected void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getName()
    {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public PermissionProvider getPermissionProvider()
    {
        return this.permissionProvider;
    }

    protected void setPermissionProvider(PermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
    }

    public Collection<Property> getProperties()
    {
        return this.properties;
    }

    public ResourceReader getResourceReader()
    {
        if (this.resourceReader == null)
        {
            ServiceMethodInfo serviceMethodInfo = this.getServiceMethodInfo();
            Collection<Class<?>> classTypeCollection = serviceMethodInfo.getResourceClassTypes();
            if (this.getPermissionProvider() != null)
                classTypeCollection.add(this.getPermissionProvider().getClass());
            if (this.getConfigProvider() != null)
                classTypeCollection.add(this.getConfigProvider().getClass());
            this.resourceReader = new ResourceReaderCollection(classTypeCollection);
        }
        return this.resourceReader;
    }

    public Class<?> getServiceClassType()
    {
        return this.serviceClassType;
    }

    public void setServiceClassType(Class<?> serviceClassType) {
        this.serviceClassType = serviceClassType;
    }

    public ServiceMethodInfo getServiceMethodInfo()
    {
        if (this.serviceMethodInfo == null)
        {
            ServiceMethodInfo serviceMethodInfo = new ServiceMethodInfo(this.getAccessClassType(), this.getServiceClassType());
            serviceMethodInfo.initialize();
            this.serviceMethodInfo = serviceMethodInfo;
        }
        return this.serviceMethodInfo;
    }

    public String getServiceType()
    {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTitle(Locale locale)
    {
        if (locale == null)
            locale = Environment.getDefaultLocale();

        return this.getResourceReader().getText(locale, SERVICE_TITLE);
    }

    public void initialize()
    {
        if (this.serviceClassType == null)
            this.serviceClassType = ReflectUtils.loadClass(this.getServiceType());

        if (this.accessClassType == null)
        {
            if (ValueUtils.isEmpty(this.getAccessType()))
                this.accessClassType = this.serviceClassType;
            else
                this.accessClassType = ReflectUtils.loadClass(this.getAccessType());
        }

        if (this.isValid())
            ServiceUtils.initialize(this);
    }

    public Object invoke(String methodName, Object... params) throws Exception
    {
        MethodInfo methodInfo = this.getServiceMethodInfo().getMethodInfo(methodName);
        if (methodInfo == null)
            throw new RuntimeException("The method " + methodName + " can't found.");
        return methodInfo.invoke(this.getInstance(), params);
    }

    public boolean isPool()
    {
        return this.isPool;
    }

    public void setPool(boolean isPool) {
        this.isPool = isPool;
    }

    public boolean isProxy()
    {
        if (this.accessClassType == null || !this.accessClassType.isInterface())
            return false;

        List<Field> fieldList = ReflectUtils.findFields(this.serviceClassType, Context.class);
        if (fieldList.size() > 0)
            return true;

        if (this.afterInvokeMap.size() > 0 || this.beforeInvokeMap.size() > 0)
            return true;

        return this.isProxy;
    }

    public void setProxy(boolean isProxy)
    {
        this.isProxy = isProxy;
    }

    public boolean isValid()
    {
        return this.getAccessClassType() != null && this.getServiceClassType() != null;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        if (!ValueUtils.isEmpty(this.getJndiName()))
        {
            info.append("JndiName : ");
            info.append(this.getJndiName());
            info.append(Environment.LINE_SEPARATOR);
        }
        info.append("AccessType : ");
        info.append(this.getAccessType());
        info.append(Environment.LINE_SEPARATOR);
        info.append("ServiceType : ");
        info.append(this.getServiceType());
        if (this.isPool())
        {
            info.append(Environment.LINE_SEPARATOR);
            info.append("Pool : ");
            info.append(this.isPool());
        }
        if (this.isProxy())
        {
            info.append(Environment.LINE_SEPARATOR);
            info.append("Proxy : ");
            info.append(this.isProxy());
        }
        if (this.properties.size() > 0)
        {
            info.append(Environment.LINE_SEPARATOR);
            info.append("Properties: ");
            for (Property property : this.properties)
            {
                info.append(Environment.LINETAB_SEPARATOR);
                info.append(property);
            }
        }
        if (this.interceptors.size() > 0)
        {
            info.append(Environment.LINE_SEPARATOR);
            info.append("Interceptors: ");
            for (Class<?> interceptor : this.interceptors)
            {
                info.append(Environment.LINETAB_SEPARATOR);
                info.append(interceptor.getName());
            }
        }
        if (this.serviceMethodInfo != null)
        {
            info.append(Environment.LINE_SEPARATOR);
            info.append(this.serviceMethodInfo);
        }
        return info.toString();
    }
}
