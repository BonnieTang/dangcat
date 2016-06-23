package org.dangcat.persistence.entity;

import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.model.Range;
import org.dangcat.persistence.orderby.OrderBy;
import org.dangcat.persistence.orm.Session;

import java.util.List;

/**
 * ʵ���������
 *
 * @author dangcat
 */
public interface EntityManager {
    /**
     * ��ʼ����
     */
    Session beginTransaction();

    /**
     * �ύ����
     */
    void commit();

    /**
     * ɾ��ָ������������ʵ�����
     *
     * @param <T>           ʵ�����͡�
     * @param entityClass   ʵ�����͡�
     * @param filterExpress ����������
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    <T> int delete(Class<T> entityClass, FilterExpress filterExpress) throws EntityException;

    /**
     * ɾ��ָ��������ʵ�����
     *
     * @param <T>              ʵ�����͡�
     * @param entityClass      ʵ�����͡�
     * @param primaryKeyValues ����ֵ��
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    <T> int delete(Class<T> entityClass, Object... primaryKeyValues) throws EntityException;

    /**
     * ɾ��ָ�����Եĵ�ʵ�塣
     *
     * @param <T>         ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param fieldNames  �ֶ����б�
     * @param values      ����ֵ��
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    <T> int delete(Class<T> entityClass, String[] fieldNames, Object... values) throws EntityException;

    /**
     * ɾ��ָ����ʵ�����
     *
     * @param <T>                 ʵ�����͡�
     * @param deleteEntityContext ɾ��ʵ�������ġ�
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    int delete(DeleteEntityContext deleteEntityContext) throws EntityException;

    /**
     * ɾ��ָ����ʵ�����
     *
     * @param <T>     ʵ�����͡�
     * @param entites ɾ��ʵ�����
     * @return �Ƿ�ɾ����
     * @throws EntityException
     */
    int delete(Object... entites) throws EntityException;

    /**
     * ����ָ�����͵�ʵ�����
     *
     * @param <T>         ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @return ��ѯ�����
     */
    <T> List<T> load(Class<T> entityClass) throws EntityException;

    /**
     * ���չ�����������ָ�����͵�ʵ�塣
     *
     * @param <T>           ʵ�����͡�
     * @param entityClass   ʵ�����͡�
     * @param filterExpress ����������
     * @return ��ѯ�����
     */
    <T> List<T> load(Class<T> entityClass, FilterExpress filterExpress) throws EntityException;

    /**
     * ���չ�����������Χ����ָ�����͵�ʵ�塣
     *
     * @param <T>           ʵ�����͡�
     * @param entityClass   ʵ�����͡�
     * @param filterExpress ����������
     * @param range         ���뷶Χ��
     * @return ��ѯ�����
     */
    <T> List<T> load(Class<T> entityClass, FilterExpress filterExpress, Range range) throws EntityException;

    /**
     * ���չ�����������Χ��������������ָ�����͵�ʵ�塣
     *
     * @param <T>           ʵ�����͡�
     * @param entityClass   ʵ�����͡�
     * @param filterExpress ����������
     * @param range         ���뷶Χ��
     * @param orderBy       ����������
     * @return ��ѯ�����
     */
    <T> List<T> load(Class<T> entityClass, FilterExpress filterExpress, Range range, OrderBy orderBy) throws EntityException;

    /**
     * �ҵ�ָ��������ʵ�����
     *
     * @param <T>         ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param primaryKeys ����������
     * @return ��ѯ�����
     */
    <T> T load(Class<T> entityClass, Object... primaryKeyValues) throws EntityException;

    /**
     * �ҵ�ָ�����Ե�ʵ�����
     *
     * @param <T>         ʵ�����͡�
     * @param entityClass ʵ�����͡�
     * @param fieldNames  �ֶ����б�
     * @param values      ����ֵ��
     * @return �ҵ���ʵ�����
     */
    <T> List<T> load(Class<T> entityClass, String[] fieldNames, Object... values) throws EntityException;

    /**
     * ����ָ�����͵�ʵ�����
     *
     * @param <T>               ʵ�����͡�
     * @param loadEntityContext ����ʵ�������ġ�
     * @return ��ѯ�����
     */
    <T> List<T> load(LoadEntityContext loadEntityContext) throws EntityException;

    /**
     * �����ݿ�ˢ��ʵ��ʵ�����ݡ�
     *
     * @param <T>
     * @param entity ʵ�����
     * @throws EntityException �����쳣��
     */
    <T> T refresh(T entity) throws EntityException;

    /**
     * �ع�����
     */
    void rollback();

    /**
     * ����ָ��ʵ�����
     *
     * @param <T>      ʵ�����͡�
     * @param entities ����ʵ�����
     * @return ��������
     */
    void save(Object... entities) throws EntityException;

    /**
     * ����ָ��ʵ�����
     *
     * @param <T>               ʵ�����͡�
     * @param saveEntityContext ����ʵ�������ġ�
     * @param entities          ����ʵ�����
     * @return ��������
     */
    void save(SaveEntityContext saveEntityContext, Object... entities) throws EntityException;
}
