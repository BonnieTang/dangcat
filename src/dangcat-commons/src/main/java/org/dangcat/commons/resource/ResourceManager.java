package org.dangcat.commons.resource;

import java.util.*;

/**
 * ��Դ��������
 */
public class ResourceManager
{
    private static ResourceManager instance = new ResourceManager();
    private Map<ClassLoader, ResourceLocaleLoader> classLoaderMap = new HashMap<ClassLoader, ResourceLocaleLoader>();
    private Map<Class<?>, ResourceReader> resourceReaderMap = new HashMap<Class<?>, ResourceReader>();

    public static ResourceManager getInstance()
    {
        return instance;
    }

    public void addResourceReader(Class<?> classType, ResourceReader resourceReader)
    {
        if (resourceReader != null)
        {
            synchronized (this.resourceReaderMap)
            {
                this.resourceReaderMap.put(classType, resourceReader);
            }
        }
    }

    /**
     * ����λ�����ƺ���Դ���Ƶõ�λ�ü��ϡ�
     * @param baseName λ�����ơ�
     * @param resourceName ��Դ���ơ�
     * @return ��Դ�����ϡ�
     */
    protected List<ResourceBundle> getResourceBundleList(ClassLoader classLoader, String baseName, String resourceName, Locale locale)
    {
        ResourceLocaleLoader resourceLocaleLoader = this.getResourceLocaleLoader(classLoader);
        if (resourceLocaleLoader == null)
        {
            resourceLocaleLoader = new ResourceLocaleLoader(classLoader);
            this.putResourceLocaleLoader(classLoader, resourceLocaleLoader);
        }
        return resourceLocaleLoader.getResourceBundleList(baseName, resourceName, locale);
    }

    private ResourceLocaleLoader getResourceLocaleLoader(ClassLoader classLoader)
    {
        synchronized (this.classLoaderMap)
        {
            return this.classLoaderMap.get(classLoader);
        }
    }

    public ResourceReader getResourceReader(Class<?> classType)
    {
        ResourceReader resourceReader = null;
        synchronized (this.resourceReaderMap)
        {
            resourceReader = this.resourceReaderMap.get(classType);
        }
        if (resourceReader == null && classType != null && !Object.class.equals(classType))
        {
            resourceReader = new ResourceReaderImpl(classType);
            this.addResourceReader(classType, resourceReader);
        }
        return resourceReader;
    }

    private void putResourceLocaleLoader(ClassLoader classLoader, ResourceLocaleLoader resourceLocaleLoader)
    {
        synchronized (this.classLoaderMap)
        {
            this.classLoaderMap.put(classLoader, resourceLocaleLoader);
        }
    }
}
