package org.dangcat.boot.permission.xml;

import java.util.Collection;
import java.util.HashSet;

import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ��ɫ�����������
 * @author dangcat
 * 
 */
public class RolesXmlResolver extends XmlResolver
{
    protected static final String RESOLVER_NAME = "Roles";

    /** ��ɫȨ�ޡ� */
    private Collection<RolePermission> rolePermissions = new HashSet<RolePermission>();;

    /**
     * ������������
     */
    public RolesXmlResolver()
    {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new RoleXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child instanceof RolePermission)
            this.rolePermissions.add((RolePermission) child);
    }

    @Override
    public Object getResolveObject()
    {
        return this.rolePermissions;
    }
}
