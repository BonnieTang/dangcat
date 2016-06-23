package org.dangcat.boot.permission.xml;

import org.dangcat.commons.serialize.xml.XmlResolver;

/**
 * ��ɫ�����������
 *
 * @author dangcat
 */
public class RoleXmlResolver extends XmlResolver {
    private static final String RESOLVER_NAME = "Role";
    private static final String ROLE_PERMISSION_NAME = "name";
    /**
     * ��ɫȨ�ޡ�
     */
    private RolePermission rolePermission = null;

    /**
     * ������������
     */
    public RoleXmlResolver() {
        super(RESOLVER_NAME);
        this.addChildXmlResolver(new PermissionsXmlResolver());
    }

    @Override
    protected void resolveAttribute(String name, String value) {
        if (ROLE_PERMISSION_NAME.equalsIgnoreCase(name)) {
            this.rolePermission = new RolePermission(value);
            this.setResolveObject(this.rolePermission);
        }
        super.resolveAttribute(name, value);
    }
}
