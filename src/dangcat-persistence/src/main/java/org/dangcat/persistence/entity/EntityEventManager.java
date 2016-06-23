package org.dangcat.persistence.entity;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.persistence.exception.EntityException;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * ʵ���¼�����
 * @author dangcat
 * 
 */
class EntityEventManager
{
    /**
     * �����ύ���¼���
     * @param saveEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void afterCommit(SaveEntityContext saveEntityContext, Collection<Object> entityCollection) throws EntityException
    {
        if (entityCollection != null)
        {
            for (Object entity : entityCollection)
            {
                EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
                for (Method method : entityMetaData.getAfterCommitCollection())
                    ReflectUtils.invoke(entity, method, saveEntityContext);
            }
        }
    }

    /**
     * ɾ�����¼���
     * @param saveEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void afterDelete(SaveEntityContext saveEntityContext, Object entity) throws EntityException
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        for (Method method : entityMetaData.getAfterDeleteCollection())
            ReflectUtils.invoke(entity, method, saveEntityContext);
    }

    /**
     * �������¼���
     * @param saveEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void afterInsert(SaveEntityContext saveEntityContext, Object entity) throws EntityException
    {
        RelationManager.update(entity);

        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        for (Method method : entityMetaData.getAfterInsertCollection())
            ReflectUtils.invoke(entity, method, saveEntityContext);
    }

    /**
     * ʵ�������¼���
     * @param loadEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void afterLoad(LoadEntityContext loadEntityContext, Object entity) throws EntityException
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        for (Method method : entityMetaData.getAfterLoadCollection())
            ReflectUtils.invoke(entity, method, loadEntityContext);
    }

    /**
     * �洢���¼���
     * @param saveEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void afterSave(SaveEntityContext saveEntityContext, Object entity) throws EntityException
    {
        RelationManager.update(entity);

        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        for (Method method : entityMetaData.getAfterSaveCollection())
            ReflectUtils.invoke(entity, method, saveEntityContext);
    }

    /**
     * ɾ��ǰ�¼���
     * @param saveEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void beforeDelete(SaveEntityContext saveEntityContext, Object entity) throws EntityException
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        for (Method method : entityMetaData.getBeforeDeleteCollection())
            ReflectUtils.invoke(entity, method, saveEntityContext);
    }

    /**
     * ����ǰ�¼���
     * @param saveEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void beforeInsert(SaveEntityContext saveEntityContext, Object entity) throws EntityException
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        for (Method method : entityMetaData.getBeforeInsertCollection())
            ReflectUtils.invoke(entity, method, saveEntityContext);
    }

    /**
     * �洢ǰ�¼���
     * @param saveEntityContext ʵ��Ự����
     * @param entity ʵ�����
     * @throws EntityException ִ���쳣��
     */
    protected static void beforeSave(SaveEntityContext saveEntityContext, Object entity) throws EntityException
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        for (Method method : entityMetaData.getBeforeSaveCollection())
            ReflectUtils.invoke(entity, method, saveEntityContext);
    }
}
