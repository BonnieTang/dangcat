package org.dangcat.persistence.entity;

import java.util.Collection;

/**
 * ʵ���������
 * @author dangcat
 * 
 */
public class EntityCalculator
{
    /**
     * ����ʵ�弯�ϡ�
     * @param entityCollection ʵ����󼯺ϡ�
     */
    public static void calculate(Collection<?> entityCollection)
    {
        if (entityCollection != null && !entityCollection.isEmpty())
        {
            for (Object entity : entityCollection)
            {
                EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
                calculateRelations(entityMetaData.getRelations(), entity);
                entityMetaData.getTable().getCalculators().calculate(entityCollection);
            }
        }
    }

    /**
     * ���㵥��ʵ�塣
     * @param entity ʵ�����
     */
    public static void calculate(Object entity)
    {
        if (entity != null)
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
            entityMetaData.getTable().getCalculators().calculate(entity);
            calculateRelations(entityMetaData.getRelations(), entity);
        }
    }

    private static int calculateMinRowNum(Collection<?> entityCollection)
    {
        int minValue = 1;
        Integer zero = 0;
        for (Object instance : entityCollection)
        {
            if (!(instance instanceof EntityBase))
                break;

            EntityBase entity = (EntityBase) instance;
            if (entity.getRowNum() == null || zero.equals(entity.getRowNum()))
            {
                minValue = 1;
                break;
            }
            minValue = Math.min(minValue, entity.getRowNum().intValue());
        }
        return minValue;
    }

    private static void calculateRelations(Collection<Relation> relations, Object entity)
    {
        if (relations != null && !relations.isEmpty())
        {
            for (Relation relation : relations)
            {
                Collection<?> childEntityCollection = relation.getMembers(entity);
                if (childEntityCollection != null && !childEntityCollection.isEmpty())
                    calculate(childEntityCollection);
            }
        }
    }

    private static void calculateRelationsRowNum(Object entity)
    {
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
        Collection<Relation> relations = entityMetaData.getRelations();
        if (relations != null && !relations.isEmpty())
        {
            for (Relation relation : relations)
            {
                Collection<?> childEntityCollection = relation.getMembers(entity);
                calculateRowNum(childEntityCollection, null);
            }
        }
    }

    public static void calculateRowNum(Collection<?> entityCollection, Integer startRow)
    {
        if (entityCollection != null && !entityCollection.isEmpty())
        {
            int index = startRow == null ? 1 : startRow;
            if (startRow == null)
                index = calculateMinRowNum(entityCollection);

            for (Object instance : entityCollection)
            {
                if (!(instance instanceof EntityBase))
                    return;

                EntityBase entity = (EntityBase) instance;
                entity.setRowNum(index++);
                calculateRelationsRowNum(entity);
            }
        }
    }
}
