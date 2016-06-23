package org.dangcat.boot.permission.xml;

import org.dangcat.boot.permission.Module;
import org.dangcat.boot.permission.PermissionManager;
import org.dangcat.commons.serialize.xml.XmlResolver;

import java.util.Collection;

/**
 * Ȩ�޹����������
 * @author dangcat
 * 
 */
public class PermissionManagerXmlResolver extends XmlResolver
{
    private static final String RESOLVER_NAME = "Permissions";
    /** Ȩ�޹��� */
    private PermissionManager permissionManager;

    /**
     * ������������
     */
    public PermissionManagerXmlResolver(PermissionManager permissionManager)
    {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new ModuleXmlResolver());
        this.addChildXmlResolver(new RolesXmlResolver());
        this.permissionManager = permissionManager;
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    @SuppressWarnings("unchecked")
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child instanceof Module)
            this.permissionManager.addModule((Module) child);
        else if (elementName.equalsIgnoreCase(RolesXmlResolver.RESOLVER_NAME))
            this.permissionManager.setRolePermissions((Collection<RolePermission>) child);
    }
}
