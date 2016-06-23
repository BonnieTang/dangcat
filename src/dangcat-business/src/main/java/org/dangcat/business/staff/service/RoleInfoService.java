package org.dangcat.business.staff.service;

import org.dangcat.business.service.QueryResult;
import org.dangcat.business.staff.domain.RoleBasic;
import org.dangcat.business.staff.domain.RoleInfo;
import org.dangcat.business.staff.filter.RoleInfoFilter;
import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

import java.util.Map;

/**
 * The service interface for Role.
 * @author dangcat
 * 
 */
@JndiName(module = "Staff", name = "RoleInfo")
public interface RoleInfoService
{
    /**
     * ����ʵ�����ݡ�
     * @param roleInfo ʵ�����
     * @return ���н����
     */
    RoleInfo create(@Parameter(name = "roleInfo") RoleInfo roleInfo) throws ServiceException;

    /**
     * ɾ��ָ�����������ݡ�
     * @param id ������
     * @return ִ�н����
     */
    boolean delete(@Parameter(name = "id") Integer id) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     * @param roleInfoFilter ��ѯ������
     * @return ��ѯ�����
     */
    QueryResult<RoleBasic> query(@Parameter(name = "roleInfoFilter") RoleInfoFilter roleInfoFilter) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     * @param roleInfo ʵ�����
     * @return ���н����
     */
    RoleInfo save(@Parameter(name = "roleInfo") RoleInfo roleInfo) throws ServiceException;

    /**
     * ��ѯָ���������б�
     * @param roleInfoFilter ��ѯ������
     * @return ��ѯ�����
     */
    Map<Integer, String> select(@Parameter(name = "roleInfoFilter") RoleInfoFilter roleInfoFilter) throws ServiceException;

    /**
     * �鿴ָ�����������ݡ�
     * @param id ����ֵ��
     * @return �鿴�����
     */
    RoleInfo view(@Parameter(name = "id") Integer id) throws ServiceException;
}
