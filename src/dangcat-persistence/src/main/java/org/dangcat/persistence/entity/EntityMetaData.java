package org.dangcat.persistence.entity;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.resource.ResourceReader;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.filter.FilterGroup;
import org.dangcat.persistence.filter.FilterType;
import org.dangcat.persistence.filter.FilterUnit;
import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Table;
import org.dangcat.persistence.orm.SessionFactory;
import org.dangcat.persistence.resource.EntityResourceManager;
import org.dangcat.persistence.tablename.TableName;
import org.dangcat.persistence.validator.EntityDataValidatorCreater;

import java.lang.reflect.Method;
import java.util.*;

/**
 * ʵ��Ԫ���ݡ�
 * @author dangcat
 * 
 */
public class EntityMetaData
{
    /** �ύ���¼������� */
    private Collection<Method> afterCommitCollection = new HashSet<Method>();
    /** ɾ�����¼������� */
    private Collection<Method> afterDeleteCollection = new HashSet<Method>();
    /** �������¼������� */
    private Collection<Method> afterInsertCollection = new HashSet<Method>();
    /** �����¼����á� */
    private Collection<Method> afterLoadCollection = new HashSet<Method>();
    /** ������¼������� */
    private Collection<Method> afterSaveCollection = new HashSet<Method>();
    /** ���������ֶΡ� */
    private EntityField autoIncrement = null;
    /** ɾ��ǰ�¼������� */
    private Collection<Method> beforeDeleteCollection = new HashSet<Method>();
    /** ����ǰǰ�¼������� */
    private Collection<Method> beforeInsertCollection = new HashSet<Method>();
    /** ����ǰ�¼������� */
    private Collection<Method> beforeSaveCollection = new HashSet<Method>();
    /** ʵ���ࡣ */
    private Class<?> entityClass = null;
    /** ������ӳ��� */
    private Map<String, EntityField> entityFieldMap = new HashMap<String, EntityField>();
    /** ���ݿ���صı����䡣 */
    private Map<String, EntityStatement> entityStatementMap = new HashMap<String, EntityStatement>();
    /** ���ӱ����á� */
    private Collection<JoinTable> joinTableCollection = new HashSet<JoinTable>();
    /** �����ֶΡ� */
    private Collection<EntityField> primaryKeyFieldCollection = null;
    /** �����ֶ����� */
    private Collection<String> primaryKeyNameCollection = null;
    /** ���������á� */
    private Collection<Relation> relations = new HashSet<Relation>();
    /** ��Դ��ȡ�� */
    private ResourceReader resourceReader = null;
    /** ����� */
    private Table table = new Table();

    public EntityMetaData(Class<?> entityClass)
    {
        this.entityClass = entityClass;
        EntityHelper.createEntityMetaData(this);
    }

    public void addEntityField(EntityField entityField)
    {
        this.getEntityFieldMap().put(entityField.getName(), entityField);
    }

    public void addRelation(Relation relation)
    {
        this.relations.add(relation);
    }

    /**
     * �Ƚ�����ʵ���С��
     * @param srcEntity ��Դʵ�����
     * @param dstEntity Ŀ��ʵ�����
     * @return
     */
    public int compare(Object srcEntity, Object dstEntity)
    {
        return EntityMetaUtils.compare(this, srcEntity, dstEntity);
    }

    /**
     * �Ƚ�����ʵ���С��
     * @param srcEntity ��Դʵ�����
     * @param dstEntity Ŀ��ʵ�����
     * @return
     */
    public int compareByPrimaryKey(Object srcEntity, Object dstEntity)
    {
        return EntityMetaUtils.compareByPrimaryKey(this, srcEntity, dstEntity);
    }

    /**
     * ͨ���������ݲ�������������
     * @param entityMetaData ʵ��Ԫ���ݡ�
     * @param fieldNames �ֶ�����
     * @param values ��ֵ��
     * @return ����������
     */
    public FilterExpress createFilterExpress(String[] fieldNames, Object[] values)
    {
        FilterExpress filterExpress = null;
        if (fieldNames != null && values != null && fieldNames.length == values.length)
        {
            FilterGroup filterGroup = new FilterGroup();
            for (int i = 0; i < fieldNames.length; i++)
            {
                EntityField entityField = this.getEntityField(fieldNames[i]);
                if (entityField == null)
                    throw new EntityException("The field name " + fieldNames[i] + " is not found.");
                filterGroup.add(new FilterUnit(entityField.getFilterFieldName(), FilterType.eq, values[i]));
            }
            if (filterGroup.getFilterExpressList().size() == 1)
                filterExpress = filterGroup.getFilterExpressList().get(0);
            else
                filterExpress = filterGroup;
        }
        return filterExpress;
    }

    public Table createTable()
    {
        return (Table) this.getTable().clone();
    }

    public Collection<Method> getAfterCommitCollection()
    {
        return this.afterCommitCollection;
    }

    public Collection<Method> getAfterDeleteCollection()
    {
        return this.afterDeleteCollection;
    }

    public Collection<Method> getAfterInsertCollection()
    {
        return this.afterInsertCollection;
    }

    public Collection<Method> getAfterLoadCollection()
    {
        return this.afterLoadCollection;
    }

    public Collection<Method> getAfterSaveCollection()
    {
        return this.afterSaveCollection;
    }

    public EntityField getAutoIncrement()
    {
        return this.autoIncrement;
    }

    public void setAutoIncrement(EntityField autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Collection<Method> getBeforeDeleteCollection()
    {
        return this.beforeDeleteCollection;
    }

    public Collection<Method> getBeforeInsertCollection()
    {
        return this.beforeInsertCollection;
    }

    public Collection<Method> getBeforeSaveCollection()
    {
        return this.beforeSaveCollection;
    }

    public Class<?> getEntityClass()
    {
        return this.entityClass;
    }

    public EntityField getEntityField(String name)
    {
        return this.getEntityFieldMap().get(ReflectUtils.toFieldName(name));
    }

    public EntityField getEntityFieldByFieldName(String fieldName)
    {
        Column column = this.table.getColumns().findByFieldName(fieldName);
        if (column != null)
            return this.getEntityField(column.getName());
        return this.getEntityField(fieldName);
    }

    public Collection<EntityField> getEntityFieldCollection()
    {
        return this.getEntityFieldMap().values();
    }

    private Map<String, EntityField> getEntityFieldMap()
    {
        return this.entityFieldMap;
    }

    /**
     * ��ȡָ�����ݿ�ı��ʽ��
     * @param databaseName
     * @return
     */
    public EntityStatement getEntityStatement(String databaseName)
    {
        if (ValueUtils.isEmpty(databaseName))
            databaseName = SessionFactory.getInstance().getDefaultDatabase();

        EntityStatement entityStatement = this.entityStatementMap.get(databaseName);
        if (entityStatement == null)
        {
            synchronized (this.entityStatementMap)
            {
                entityStatement = new EntityStatement(this, databaseName);
                entityStatement.initialize();
                this.entityStatementMap.put(databaseName, entityStatement);
            }
        }
        return entityStatement;
    }

    protected Map<String, EntityStatement> getEntityStatementMap()
    {
        return this.entityStatementMap;
    }

    public Collection<JoinTable> getJoinTableCollection()
    {
        return this.joinTableCollection;
    }

    public Collection<EntityField> getPrimaryKeyFieldCollection()
    {
        if (this.primaryKeyFieldCollection == null)
        {
            Collection<EntityField> primaryKeyFieldCollection = new LinkedHashSet<EntityField>();
            for (Column column : this.table.getColumns().getPrimaryKeys())
                primaryKeyFieldCollection.add(this.getEntityField(column.getName()));
            this.primaryKeyFieldCollection = primaryKeyFieldCollection;
        }
        return this.primaryKeyFieldCollection;
    }

    public String[] getPrimaryKeyNames()
    {
        if (this.primaryKeyNameCollection == null)
        {
            Collection<String> primaryKeyNameCollection = new LinkedHashSet<String>();
            for (EntityField primaryKeyEntityField : this.getPrimaryKeyFieldCollection())
                primaryKeyNameCollection.add(primaryKeyEntityField.getName());
            this.primaryKeyNameCollection = primaryKeyNameCollection;
        }
        return this.primaryKeyNameCollection.toArray(new String[0]);
    }

    public Object[] getPrimaryKeyValues(Object entity)
    {
        Collection<Object> primaryKeyNameCollection = new LinkedList<Object>();
        for (EntityField primaryKeyEntityField : this.getPrimaryKeyFieldCollection())
            primaryKeyNameCollection.add(primaryKeyEntityField.getValue(entity));
        return primaryKeyNameCollection.toArray();
    }

    public Collection<Relation> getRelations()
    {
        return this.relations;
    }

    public ResourceReader getResourceReader()
    {
        if (this.resourceReader == null)
            this.resourceReader = EntityResourceManager.getInstance().getResourceReader(this);
        return this.resourceReader;
    }

    public Table getTable()
    {
        return this.table;
    }

    public TableName getTableName()
    {
        return this.getTable().getTableName();
    }

    public void setTableName(TableName tableName) {
        this.getTable().setTableName(tableName);
    }

    public Object getValue(String fieldName, Object instance)
    {
        EntityField entityField = this.getEntityField(fieldName);
        if (entityField != null)
            return entityField.getValue(instance);
        return null;
    }

    protected void initialize()
    {
        // ����У����
        new EntityDataValidatorCreater(this).create();
        // �¼���
        EntityHelper.createEntityEvent(this);
    }

    /**
     * ����ֵ�Ƿ���Ч��
     * @param primaryKeyValues ����ֵ��
     * @return
     */
    public boolean isPrimaryKeyValueValid(Object[] primaryKeyValues)
    {
        return EntityMetaUtils.isPrimaryKeyValueValid(primaryKeyValues);
    }

    public void setValue(String fieldName, Object instance, Object value)
    {
        EntityField entityField = this.getEntityField(fieldName);
        if (entityField != null)
            entityField.setValue(instance, value);
    }

    /**
     * ������λ˳��
     */
    public void sort()
    {
        this.entityFieldMap = EntityMetaUtils.sort(this);
    }

    @Override
    public String toString()
    {
        return EntityMetaUtils.toString(this);
    }
}
