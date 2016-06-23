package org.dangcat.business.staff.service;

import org.dangcat.business.service.QueryResult;
import org.dangcat.business.staff.config.StaffSetup;
import org.dangcat.business.staff.domain.OperatorInfo;
import org.dangcat.business.staff.domain.OperatorInfoBase;
import org.dangcat.business.staff.domain.OperatorInfoCreate;
import org.dangcat.business.staff.filter.OperatorInfoFilter;
import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

import java.util.Collection;
import java.util.Map;

/**
 * The service interface for Operator.
 * @author dangcat
 * 
 */
@JndiName(module = "Staff", name = "OperatorInfo")
public interface OperatorInfoService
{
    /**
     * �޸�ָ���˺ŵ����롣
     * @param orgPassword ԭʼ���롣
     * @param newPassword �����롣
     * @return ִ�н����
     */
    boolean changePassword(@Parameter(name = "orgPassword") String orgPassword, @Parameter(name = "newPassword") String newPassword) throws ServiceException;

    /**
     * ����������á�
     */
    StaffSetup config(@Parameter(name = "config") StaffSetup staffSetup) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     * @param operatorInfo ʵ�����
     * @return ���н����
     */
    OperatorInfo create(@Parameter(name = "operatorInfo") OperatorInfoCreate operatorInfo) throws ServiceException;

    /**
     * ɾ��ָ�����������ݡ�
     * @param id ������
     * @return ִ�н����
     */
    boolean delete(@Parameter(name = "id") Integer id) throws ServiceException;

    /**
     * ��ѯָ�������Ļ�������Ա��Ϣ��
     * @param operatorInfoFilter ��ѯ������
     * @return ��ѯ�����
     */
    Collection<OperatorInfoBase> pick(OperatorInfoFilter operatorInfoFilter) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     * @param operatorInfoFilter ��ѯ������
     * @return ��ѯ�����
     */
    QueryResult<OperatorInfo> query(@Parameter(name = "operatorInfoFilter") OperatorInfoFilter operatorInfoFilter) throws ServiceException;

    /**
     * ����ָ���˺ŵ����롣
     * @param no ����Ա�˺š�
     * @param password �����롣
     * @return ִ�н����
     */
    boolean resetPassword(@Parameter(name = "no") String no, @Parameter(name = "password") String password) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     * @param operatorInfo ʵ�����
     * @return ���н����
     */
    OperatorInfo save(@Parameter(name = "operatorInfo") OperatorInfo operatorInfo) throws ServiceException;

    /**
     * ��ѯָ���������б�
     * @param operatorInfoFilter ��ѯ������
     * @return ��ѯ�����
     */
    Map<Integer, String> select(@Parameter(name = "operatorInfoFilter") OperatorInfoFilter operatorInfoFilter) throws ServiceException;

    /**
     * �鿴ָ�����������ݡ�
     * @param id ����ֵ��
     * @return �鿴�����
     */
    OperatorInfo view(@Parameter(name = "id") Integer id) throws ServiceException;
}
