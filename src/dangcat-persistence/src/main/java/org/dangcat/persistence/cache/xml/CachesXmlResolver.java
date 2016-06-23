package org.dangcat.persistence.cache.xml;

import java.util.List;

import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * �������ý�������
 * @author dangcat
 * 
 */
public class CachesXmlResolver extends XmlResolver
{
    private static final String RESOLVER_NAME = "Caches";
    /** ���漯�ϡ� */
    private List<Cache> cacheList = null;

    /**
     * ������������
     */
    public CachesXmlResolver()
    {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new CacheXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    @Override
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child != null)
            this.cacheList.add((Cache) child);
    }

    public List<Cache> getCacheList()
    {
        return cacheList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setResolveObject(Object resolveObject)
    {
        this.cacheList = (List<Cache>) resolveObject;
        super.setResolveObject(resolveObject);
    }
}
