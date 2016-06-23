package org.dangcat.business.service;

import java.util.Map;

import org.dangcat.framework.exception.ServiceException;
import org.dangcat.persistence.entity.EntityBase;

/**
 * ҵ�����ӿڡ�
 * @author dangcat
 * 
 * @param <V>
 */
public interface BusinessService<Q extends EntityBase, V extends EntityBase, F extends DataFilter>
{
    public static final String QUERY_TABLENAME = "Q";
    public static final String VIEW_TABLENAME = "V";

    /**
     * ����������á�
     */
    public EntityBase config(EntityBase config) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     * @param entity ʵ�����
     * @return ���н����
     */
    public V create(V entity) throws ServiceException;

    /**
     * ɾ��ָ�����������ݡ�
     * @param id ������
     * @return ִ�н����
     */
    public boolean delete(Integer id) throws ServiceException;

    /**
     * �ж��Ƿ���Ȩ�ޡ�
     * @param permissionId Ȩ���롣
     * @return �Ƿ���ָ��Ȩ�ޡ�
     */
    public boolean hasPermission(Integer permissionId);

    /**
     * ��ѯָ�����������ݡ�
     * @param dataFilter ��ѯ��Χ��
     * @return ��ѯ�����
     */
    public QueryResult<Q> query(F dataFilter) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     * @param entity ʵ�����
     * @return ���н����
     */
    public V save(V entity) throws ServiceException;

    /**
     * ��ѯָ���������б�
     * @param dataFilter ��ѯ��Χ��
     * @return ��ѯ�����
     */
    public Map<Number, String> selectMap(F dataFilter) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     * @param id ����ֵ��
     * @return ��ѯ�����
     */
    public V view(Integer id) throws ServiceException;
}
