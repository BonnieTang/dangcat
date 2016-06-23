package org.dangcat.business.systeminfo;

import org.dangcat.boot.menus.Menu;
import org.dangcat.framework.service.annotation.JndiName;

import java.util.Collection;
import java.util.Map;

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
    Collection<Menu> loadMenus();

    /**
     * ����ӳ���
     */
    Map<Integer, String> loadParamMap(String name);

    /**
     * ����Ȩ����Ϣ��
     */
    Collection<PermissionInfo> loadPermissions();

    /**
     * ϵͳ��Ϣ��
     */
    SystemInfo loadSystemInfo();
}
