package org.dangcat.business.service;

import org.dangcat.framework.exception.ServiceException;
import org.dangcat.persistence.entity.EntityBase;

import java.util.Map;

/**
 * ҵ�����ӿڡ�
 *
 * @param <V>
 * @author dangcat
 */
public interface BusinessService<Q extends EntityBase, V extends EntityBase, F extends DataFilter> {
    String QUERY_TABLENAME = "Q";
    String VIEW_TABLENAME = "V";

    /**
     * ����������á�
     */
    EntityBase config(EntityBase config) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     *
     * @param entity ʵ�����
     * @return ���н����
     */
    V create(V entity) throws ServiceException;

    /**
     * ɾ��ָ�����������ݡ�
     *
     * @param id ������
     * @return ִ�н����
     */
    boolean delete(Integer id) throws ServiceException;

    /**
     * �ж��Ƿ���Ȩ�ޡ�
     *
     * @param permissionId Ȩ���롣
     * @return �Ƿ���ָ��Ȩ�ޡ�
     */
    boolean hasPermission(Integer permissionId);

    /**
     * ��ѯָ�����������ݡ�
     *
     * @param dataFilter ��ѯ��Χ��
     * @return ��ѯ�����
     */
    QueryResult<Q> query(F dataFilter) throws ServiceException;

    /**
     * ����ʵ�����ݡ�
     *
     * @param entity ʵ�����
     * @return ���н����
     */
    V save(V entity) throws ServiceException;

    /**
     * ��ѯָ���������б�
     *
     * @param dataFilter ��ѯ��Χ��
     * @return ��ѯ�����
     */
    Map<Number, String> selectMap(F dataFilter) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     *
     * @param id ����ֵ��
     * @return ��ѯ�����
     */
    V view(Integer id) throws ServiceException;
}
