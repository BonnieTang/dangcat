package org.dangcat.business.staff.service;

import java.util.Collection;
import java.util.Map;

import org.dangcat.business.service.QueryResult;
import org.dangcat.business.staff.domain.OperatorGroup;
import org.dangcat.business.staff.domain.OperatorGroupBase;
import org.dangcat.business.staff.filter.OperatorGroupFilter;
import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

/**
 * The service interface for OperatorGroup.
 * @author dangcat
 * 
 */
@JndiName(module = "Staff", name = "OperatorGroup")
public interface OperatorGroupService
{
    /**
     * ����ʵ�����ݡ�
     * @param operatorGroup ʵ�����
     * @return ���н����
     */
    public OperatorGroup create(@Parameter(name = "operatorGroup") OperatorGroup operatorGroup) throws ServiceException;

    /**
     * ɾ��ָ�����������ݡ�
     * @param id ������
     * @return ִ�н����
     */
    public boolean delete(@Parameter(name = "id") Integer id) throws ServiceException;

    /**
     * �鿴��ǰ�û������Ĳ���Ա��������б�
     * @return ����Ա��ӳ���
     */
    public Map<Integer, String> loadMembers() throws ServiceException;

    /**
     * ��ѯָ���������б�
     * @param operatorGroupFilter ��ѯ������
     * @return ��ѯ�����
     */
    public Collection<OperatorGroupBase> pick(@Parameter(name = "operatorGroupFilter") OperatorGroupFilter operatorGroupFilter) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     * @param operatorGroupFilter ��ѯ������
     * @return ��ѯ�����
     */
    public QueryResult<OperatorGroup> query(@Parameter(name = "operatorGroupFilter") OperatorGroupFilter operatorGroupFilter) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     * @param operatorGroup ʵ�����
     * @return ���н����
     */
    public OperatorGroup save(@Parameter(name = "operatorGroup") OperatorGroup operatorGroup) throws ServiceException;

    /**
     * ��ѯָ���������б�
     * @param operatorGroupFilter ��ѯ������
     * @return ��ѯ�����
     */
    public Map<Integer, String> select(@Parameter(name = "operatorGroupFilter") OperatorGroupFilter operatorGroupFilter) throws ServiceException;

    /**
     * �鿴ָ�����������ݡ�
     * @param id ����ֵ��
     * @return �鿴�����
     */
    public OperatorGroup view(@Parameter(name = "id") Integer id) throws ServiceException;
}
