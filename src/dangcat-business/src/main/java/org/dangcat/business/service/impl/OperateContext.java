package org.dangcat.business.service.impl;

import org.dangcat.persistence.entity.EntityManager;

/**
 * ҵ����������ġ�
 * @author dangcat
 * 
 * @param <T>
 */
public class OperateContext
{
    private Class<?> classType = null;
    private EntityManager entityManager = null;

    public Class<?> getClassType()
    {
        return classType;
    }

    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    protected void setClassType(Class<?> entityClass)
    {
        this.classType = entityClass;
    }

    protected void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }
}
