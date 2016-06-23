package org.dangcat.framework.service;

import java.util.Map;

import org.dangcat.commons.reflect.Permission;

/**
 * Ȩ�޿��Ʊ�
 * @author dangcat
 * 
 */
public interface PermissionProvider
{
    /**
     * ��ȡ������Ȩ�޶���
     * @param methodName ��������
     * @return Ȩ�޶���
     */
    public Permission getMethodPermission(String methodName);

    /**
     * �ṩȨ�޿���ӳ���
     */
    public Map<Integer, Permission> getPermissionMap();

    /**
     * ��ȡ��Ȩ�޶���
     * @return Ȩ�޶���
     */
    public Permission getRootPermission();
}
