package org.dangcat.commons.serialize.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * �����б�����������
 * @author dangcat
 * 
 */
public class ParamsXmlResolver extends XmlResolver
{
    private static final String RESOLVER_NAME = "Params";
    /**
     * �����б����
     */
    private Map<String, Object> params;

    /**
     * ������������
     */
    public ParamsXmlResolver()
    {
        this(RESOLVER_NAME, null);
        this.addChildXmlResolver(new ParamXmlResolver());
    }

    public ParamsXmlResolver(String resolverName, String elementName)
    {
        super(resolverName);
        this.addChildXmlResolver(new ParamXmlResolver(elementName));
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        Param param = (Param) child;
        if (param != null)
            this.params.put(param.getName(), param.getValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setResolveObject(Object resolveObject)
    {
        this.params = (Map<String, Object>) resolveObject;
        super.setResolveObject(resolveObject);
    }

    @Override
    protected void startElement()
    {
        if (this.params == null)
        {
            this.params = new HashMap<String, Object>();
            this.setResolveObject(this.params);
        }
    }
}
