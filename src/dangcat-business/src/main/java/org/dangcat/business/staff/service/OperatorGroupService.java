package org.dangcat.business.staff.service;

import org.dangcat.business.service.QueryResult;
import org.dangcat.business.staff.domain.OperatorGroup;
import org.dangcat.business.staff.domain.OperatorGroupBase;
import org.dangcat.business.staff.filter.OperatorGroupFilter;
import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

import java.util.Collection;
import java.util.Map;

/**
 * The service interface for OperatorGroup.
 *
 * @author dangcat
 */
@JndiName(module = "Staff", name = "OperatorGroup")
public interface OperatorGroupService {
    /**
     * ����ʵ�����ݡ�
     *
     * @param operatorGroup ʵ�����
     * @return ���н����
     */
    OperatorGroup create(@Parameter(name = "operatorGroup") OperatorGroup operatorGroup) throws ServiceException;

    /**
     * ɾ��ָ�����������ݡ�
     *
     * @param id ������
     * @return ִ�н����
     */
    boolean delete(@Parameter(name = "id") Integer id) throws ServiceException;

    /**
     * �鿴��ǰ�û������Ĳ���Ա��������б�
     *
     * @return ����Ա��ӳ���
     */
    Map<Integer, String> loadMembers() throws ServiceException;

    /**
     * ��ѯָ���������б�
     *
     * @param operatorGroupFilter ��ѯ������
     * @return ��ѯ�����
     */
    Collection<OperatorGroupBase> pick(@Parameter(name = "operatorGroupFilter") OperatorGroupFilter operatorGroupFilter) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     *
     * @param operatorGroupFilter ��ѯ������
     * @return ��ѯ�����
     */
    QueryResult<OperatorGroup> query(@Parameter(name = "operatorGroupFilter") OperatorGroupFilter operatorGroupFilter) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     *
     * @param operatorGroup ʵ�����
     * @return ���н����
     */
    OperatorGroup save(@Parameter(name = "operatorGroup") OperatorGroup operatorGroup) throws ServiceException;

    /**
     * ��ѯָ���������б�
     *
     * @param operatorGroupFilter ��ѯ������
     * @return ��ѯ�����
     */
    Map<Integer, String> select(@Parameter(name = "operatorGroupFilter") OperatorGroupFilter operatorGroupFilter) throws ServiceException;

    /**
     * �鿴ָ�����������ݡ�
     *
     * @param id ����ֵ��
     * @return �鿴�����
     */
    OperatorGroup view(@Parameter(name = "id") Integer id) throws ServiceException;
}
