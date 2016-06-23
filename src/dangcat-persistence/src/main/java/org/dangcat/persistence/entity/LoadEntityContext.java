package org.dangcat.persistence.entity;

import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.model.Range;
import org.dangcat.persistence.orderby.OrderBy;

import java.util.Collection;

/**
 * ����ʵ�������ġ�
 * @author dangcat
 * 
 */
public class LoadEntityContext extends DeleteEntityContext
{
    private Object entity = null;
    private Collection<?> entityCollection = null;
    private OrderBy orderBy = null;
    private Range range;

    /**
     * ���չ�����������ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     */
    public LoadEntityContext(Class<?> entityClass)
    {
        this(entityClass, null, null, null);
    }

    /**
     * ���չ�����������ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     */
    public LoadEntityContext(Class<?> entityClass, FilterExpress filterExpress)
    {
        this(entityClass, filterExpress, null, null);
    }

    /**
     * ���չ�����������Χ����ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     * @param range ���뷶Χ��
     */
    public LoadEntityContext(Class<?> entityClass, FilterExpress filterExpress, Range range)
    {
        this(entityClass, filterExpress, range, null);
    }

    /**
     * ���չ�����������Χ��������������ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param filterExpress ����������
     * @param range ���뷶Χ��
     * @param orderBy ����������
     */
    public LoadEntityContext(Class<?> entityClass, FilterExpress filterExpress, Range range, OrderBy orderBy)
    {
        super(entityClass, filterExpress);
        this.range = range;
        this.orderBy = orderBy;
    }

    /**
     * ��������ֵ����ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param primaryKeyValues ����ֵ��
     */
    public LoadEntityContext(Class<?> entityClass, Object... primaryKeyValues)
    {
        this(entityClass, null, primaryKeyValues);
    }

    /**
     * ���շ�Χ����ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param range ���뷶Χ��
     */
    public LoadEntityContext(Class<?> entityClass, Range range)
    {
        this(entityClass, null, range, null);
    }

    /**
     * ���շ�Χ��������������ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param range ���뷶Χ��
     * @param orderBy ����������
     */
    public LoadEntityContext(Class<?> entityClass, Range range, OrderBy orderBy)
    {
        this(entityClass, null, range, null);
    }

    /**
     * ��������ֵ����ָ�����͵�ʵ�塣
     * @param entityManager ʵ���������
     * @param entityClass ʵ�����͡�
     * @param fieldNames �������ơ�
     * @param values ����ֵ��
     */
    public LoadEntityContext(Class<?> entityClass, String[] fieldNames, Object... values)
    {
        super(entityClass, fieldNames, values);
    }

    /**
     * ˢ��ָ����ʵ�塣
     * @param entity ʵ�����
     * @param entityClass ʵ�����͡�
     */
    public LoadEntityContext(Object entity)
    {
        this(entity.getClass(), null, null, null);
        this.entity = entity;
    }

    protected void afterLoad()
    {
        if (this.getEntityEventAdapter() != null)
            this.getEntityEventAdapter().afterLoad(this);
    }

    protected boolean beforeLoad()
    {
        if (this.getEntityEventAdapter() != null)
            return this.getEntityEventAdapter().beforeLoad(this);
        return true;
    }

    public Object getEntity()
    {
        return this.entity;
    }

    public Collection<?> getEntityCollection()
    {
        return this.entityCollection;
    }

    protected void setEntityCollection(Collection<?> entityCollection) {
        this.entityCollection = entityCollection;
    }

    public OrderBy getOrderBy()
    {
        return this.orderBy;
    }

    public Range getRange()
    {
        return this.range;
    }

    @Override
    public void initialize()
    {
        super.initialize();
        if (this.getFilterExpress() == null && this.entity != null)
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(this.getEntityClass());
            String[] primaryKeyNames = entityMetaData.getPrimaryKeyNames();
            Object[] primaryKeyValues = entityMetaData.getPrimaryKeyValues(this.entity);
            this.setFilterExpress(entityMetaData.createFilterExpress(primaryKeyNames, primaryKeyValues));
        }
    }

    protected void onLoadError(EntityException entityException)
    {
        if (this.getEntityEventAdapter() != null)
            this.getEntityEventAdapter().onLoadError(this, entityException);
    }
}