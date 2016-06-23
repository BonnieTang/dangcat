package org.dangcat.business.systeminfo;

import java.util.Collection;
import java.util.Map;

import org.dangcat.boot.menus.Menu;
import org.dangcat.framework.service.annotation.JndiName;

/**
 * ϵͳ����
 * @author Administrator
 * 
 */
@JndiName(module = "System", name = "SystemInfo")
public interface SystemInfoService
{
    /**
     * ����ϵͳ�˵��
     */
    public Collection<Menu> loadMenus();

    /**
     * ����ӳ���
     */
    public Map<Integer, String> loadParamMap(String name);

    /**
     * ����Ȩ����Ϣ��
     */
    public Collection<PermissionInfo> loadPermissions();

    /**
     * ϵͳ��Ϣ��
     */
    public SystemInfo loadSystemInfo();
}
