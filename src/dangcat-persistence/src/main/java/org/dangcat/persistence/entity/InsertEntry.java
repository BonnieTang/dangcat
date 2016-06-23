package org.dangcat.persistence.entity;

import java.util.Collection;

class InsertEntry
{
    private Object entity = null;
    private EntityField entityField = null;
    private Object value = null;

    InsertEntry(Object entity, EntityField entityField, Object value)
    {
        this.entity = entity;
        this.entityField = entityField;
        this.value = value;
    }

    /**
     * ����������¹����ֶΡ�
     */
    protected void update()
    {
        // ͬ����ϸ��Ĺ����ֶΡ�
        EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(this.entity.getClass());
        for (Relation relation : entityMetaData.getRelations())
        {
            String[] fieldNames = relation.getParentFieldNames();
            if (fieldNames.length == 1 && this.entityField.getName().equalsIgnoreCase(fieldNames[0]))
            {
                Collection<Object> members = relation.getMembers(this.entity);
                if (members != null)
                {
                    EntityField childEntityField = null;
                    for (Object member : members)
                    {
                        if (childEntityField == null)
                        {
                            EntityMetaData childEntityMetaData = EntityHelper.getEntityMetaData(member.getClass());
                            childEntityField = childEntityMetaData.getEntityField(relation.getChildFieldNames()[0]);
                        }
                        if (childEntityField != null)
                            childEntityField.setValue(member, this.value);
                    }
                }
            }
        }
    }

    protected void writeSequenceValue()
    {
        this.entityField.setValue(this.entity, this.value);
    }
}
