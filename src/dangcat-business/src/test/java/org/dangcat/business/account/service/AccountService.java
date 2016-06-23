package org.dangcat.business.account.service;

import java.util.Date;
import java.util.Map;

import org.dangcat.business.domain.AccountBasic;
import org.dangcat.business.domain.AccountInfo;
import org.dangcat.business.service.QueryResult;
import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

/**
 * �˻��������ӿڡ�
 * @author dangcat
 * 
 */
@JndiName(module = "Account", name = "AccountInfo")
public interface AccountService
{
    /**
     * ����ʵ�����ݡ�
     * @param entity ʵ�����
     * @return ���н����
     */
    public AccountInfo create(@Parameter(name = "accountInfo") AccountInfo accountInfo) throws ServiceException;

    /**
     * ���Զ�����������ӿڡ�
     * @param id ����ֵ��
     * @return ��ѯ�����
     */
    public Person createPerson(@Parameter(name = "name") String name, @Parameter(name = "age") int age, @Parameter(name = "balance") double balance, @Parameter(name = "borthDay") Date borthDay)
            throws ServiceException;

    /**
     * ɾ��ָ�����������ݡ�
     * @param id ������
     * @return ִ�н����
     */
    public boolean delete(@Parameter(name = "id") Integer id) throws ServiceException;

    /**
     * ���Զ�����������ӿڡ�
     * @param id ����ֵ��
     * @return ��ѯ�����
     */
    public String printPerson(@Parameter(name = "person") Person person) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     * @param dataFilter ��ѯ��Χ��
     * @return ��ѯ�����
     */
    public QueryResult<AccountBasic> query(@Parameter(name = "accountFilter") AccountFilter accountFilter) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     * @param entity ʵ�����
     * @return ���н����
     */
    public AccountInfo save(@Parameter(name = "accountInfo") AccountInfo accountInfo) throws ServiceException;

    /**
     * ��ѯָ���������б�
     * @param dataFilter ��ѯ��Χ��
     * @return ��ѯ�����
     */
    public Map<Integer, String> select(@Parameter(name = "accountFilter") AccountFilter accountFilter) throws ServiceException;

    /**
     * �鿴ָ�����������ݡ�
     * @param id ����ֵ��
     * @return ��ѯ�����
     */
    public AccountInfo view(@Parameter(name = "id") Integer id) throws ServiceException;
}
