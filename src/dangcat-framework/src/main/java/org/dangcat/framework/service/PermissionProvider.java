package org.dangcat.framework.service;

import org.dangcat.commons.reflect.Permission;

import java.util.Map;

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
    Permission getMethodPermission(String methodName);

    /**
     * �ṩȨ�޿���ӳ���
     */
    Map<Integer, Permission> getPermissionMap();

    /**
     * ��ȡ��Ȩ�޶���
     * @return Ȩ�޶���
     */
    Permission getRootPermission();
}
