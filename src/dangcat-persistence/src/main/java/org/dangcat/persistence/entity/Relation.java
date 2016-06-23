package org.dangcat.persistence.entity;

import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.filter.FilterGroup;
import org.dangcat.persistence.filter.FilterType;
import org.dangcat.persistence.filter.FilterUnit;
import org.dangcat.persistence.orderby.OrderBy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * ��������
 * @author dangcat
 * 
 */
public class Relation
{
    private boolean associateDelete = true;
    private boolean associateLoad = true;
    private boolean associateSave = true;
    private String[] childFieldNames;
    private EntityField entityField;
    private Class<?> memberType;
    private String[] parentFieldNames;
    private OrderBy sortBy = null;

    public Relation(EntityField entityField, org.dangcat.persistence.annotation.Relation relationAnnotation)
    {
        this(entityField, relationAnnotation.parentFieldNames(), relationAnnotation.childFieldNames(), relationAnnotation.associateLoad(), relationAnnotation.associateSave(), relationAnnotation
                .associateDelete(), relationAnnotation.sortBy());
    }

    /**
     * ����������ϵ��
     * @param fieldName �ֶ�����
     * @param parentFieldNames �����ֶι�ϵ��
     * @param childEntityMetaData ��ʵ��Ԫ���ݡ�
     * @param childFieldNames �ӱ�ӳ���ϵ��
     */
    public Relation(EntityField entityField, String[] parentFieldNames, String[] childFieldNames, boolean associateLoad, boolean associateSave, boolean associateDelete, String sortBy)
    {
        this.entityField = entityField;
        this.parentFieldNames = parentFieldNames;
        this.childFieldNames = childFieldNames;
        if (this.parentFieldNames.length == 0)
        {
            this.associateLoad = false;
            this.associateSave = false;
            this.associateDelete = false;
        }
        else
        {
            this.associateLoad = associateLoad;
            this.associateSave = associateSave;
            this.associateDelete = associateDelete;
        }
        this.sortBy = OrderBy.parse(sortBy);
    }

    public String[] getChildFieldNames()
    {
        return childFieldNames;
    }

    /**
     * �ɸ���ʵ������ӱ����������
     * @param parentEntity ��ʵ�����
     * @return ����������
     * @throws EntityException
     */
    public FilterExpress getChildFilterExpress(Object parentEntity) throws EntityException
    {
        if (parentEntity == null)
            return null;

        EntityMetaData parentEntityMetaData = EntityHelper.getEntityMetaData(parentEntity.getClass());
        EntityMetaData childEntityMetaData = EntityHelper.getEntityMetaData(this.getMemberType());

        FilterGroup filterGroup = new FilterGroup();
        for (int i = 0; i < this.parentFieldNames.length; i++)
        {
            Object value = null;
            EntityField parentEntityField = parentEntityMetaData.getEntityField(this.parentFieldNames[i]);
            if (parentEntityField != null)
                value = parentEntityField.getValue(parentEntity);
            if (value != null)
            {
                EntityField childEntityField = childEntityMetaData.getEntityField(this.childFieldNames[i]);
                if (childEntityField == null)
                    return null;
                filterGroup.add(new FilterUnit(childEntityField.getFilterFieldName(), FilterType.eq, value));
            }
        }
        if (filterGroup.getFilterExpressList().size() == 0)
            return null;
        return filterGroup.getFilterExpressList().size() == 1 ? filterGroup.getFilterExpressList().get(0) : filterGroup;
    }

    /**
     * ��ȡ��ϸ���ݵĹ�������
     * @param instance ��ʵ������
     * @return
     */
    @SuppressWarnings("unchecked")
    public Collection<Object> getMembers(Object instance)
    {
        Collection<Object> members = null;
        try
        {
            Object value = this.entityField.getValue(instance);
            if (value != null)
            {
                if (this.isCollectionMember())
                    members = (Collection) value;
                else
                {
                    members = new HashSet<Object>();
                    members.add(value);
                }
            }
        }
        catch (Exception e)
        {
        }
        return members;
    }

    /**
     * ��ȡ�ֶεķ������͡�
     * @param field �ֶζ���
     * @param index ����λ�á�
     * @return ��������
     */
    public Class<?> getMemberType()
    {
        if (this.memberType == null)
        {
            this.memberType = this.entityField.getClassType();
            if (Collection.class.isAssignableFrom(this.entityField.getClassType()))
            {
                Type genericFieldType = this.entityField.getGenericType();
                if (genericFieldType instanceof ParameterizedType)
                {
                    ParameterizedType parameterizedType = (ParameterizedType) genericFieldType;
                    Type[] actualTypes = parameterizedType.getActualTypeArguments();
                    if (actualTypes.length > 0)
                        this.memberType = (Class<?>) actualTypes[0];
                }
            }
        }
        return this.memberType;
    }

    public String getName()
    {
        return this.entityField.getName();
    }

    public String[] getParentFieldNames()
    {
        return parentFieldNames;
    }

    public OrderBy getSortBy()
    {
        return sortBy;
    }

    public boolean isAssociateDelete()
    {
        return associateDelete;
    }

    public void setAssociateDelete(boolean associateDelete) {
        this.associateDelete = associateDelete;
    }

    public boolean isAssociateLoad()
    {
        return associateLoad;
    }

    public void setAssociateLoad(boolean associateLoad) {
        this.associateLoad = associateLoad;
    }

    public boolean isAssociateSave()
    {
        return this.associateSave;
    }

    public void setAssociateSave(boolean associateSave) {
        this.associateSave = associateSave;
    }

    public boolean isCollectionMember()
    {
        return Collection.class.isAssignableFrom(this.entityField.getClassType());
    }

    /**
     * ����������ԡ�
     * @param entity ��ʵ�塣
     * @param entityList ��ʵ���б�
     * @throws EntityException
     */
    @SuppressWarnings("unchecked")
    public void load(Object entity, List<?> entityList)
    {
        if (entity == null || entityList == null || entityList.size() == 0)
            return;

        try
        {
            Object value = this.entityField.getValue(entity);
            // ���ͼ��ϱ�������ϸ����
            if (Collection.class.isAssignableFrom(entityField.getClassType()))
            {
                if (value != null) // �����ڽ�ʵ������
                {
                    Collection entityCollection = (Collection) value;
                    entityCollection.clear();
                    entityCollection.addAll(entityList);
                }
            }
            else if (entityList.size() == 1) // �Ǽ���ֻ����һ������
            {
                if (this.entityField.getClassType().isAssignableFrom(entityList.get(0).getClass()))
                    this.entityField.setValue(entity, entityList.get(0));
            }
            else
                this.entityField.setValue(entity, null);
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append(Relation.class.getSimpleName());
        info.append(" : ");
        info.append("fieldName = ");
        info.append(this.getName());
        info.append(", parentFieldNames = {");
        boolean isFirst = true;
        for (String fieldName : this.getParentFieldNames())
        {
            if (!isFirst)
                info.append(", ");
            info.append(fieldName);
            isFirst = false;
        }
        info.append("}, childFieldNames = {");
        isFirst = true;
        for (String fieldName : this.getChildFieldNames())
        {
            if (!isFirst)
                info.append(", ");
            info.append(fieldName);
            isFirst = false;
        }
        info.append("}, associateLoad = ");
        info.append(this.associateLoad);
        info.append(", associateSave = ");
        info.append(this.isAssociateSave());
        info.append(", associateDelete = ");
        info.append(this.isAssociateDelete());
        return info.toString();
    }

    /**
     * ͬ����ʵ�����ʵ��������ݡ�
     * @param parentEntity ��ʵ�塣
     * @param childEntity ��ʵ�塣
     * @throws EntityException
     */
    public void update(Object parentEntity, Collection<Object> members) throws EntityException
    {
        EntityMetaData parentEntityMetaData = EntityHelper.getEntityMetaData(parentEntity.getClass());
        for (int i = 0; i < this.parentFieldNames.length; i++)
        {
            EntityField parentEntityField = parentEntityMetaData.getEntityField(this.parentFieldNames[i]);
            if (parentEntityField != null)
            {
                EntityField childEntityField = null;
                for (Object member : members)
                {
                    if (childEntityField == null)
                    {
                        EntityMetaData childEntityMetaData = EntityHelper.getEntityMetaData(member.getClass());
                        childEntityField = childEntityMetaData.getEntityField(this.childFieldNames[i]);
                    }
                    if (childEntityField != null)
                    {
                        Object value = parentEntityField.getValue(parentEntity);
                        if (parentEntityField.getColumn().isSequenceGeneration() && value == null)
                            continue;
                        childEntityField.setValue(member, value);
                    }
                }
            }
        }
    }
}
