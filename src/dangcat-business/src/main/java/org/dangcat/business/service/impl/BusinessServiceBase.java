package org.dangcat.business.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dangcat.business.config.BusinessConfig;
import org.dangcat.business.exceptions.BusinessException;
import org.dangcat.business.service.BusinessService;
import org.dangcat.business.service.DataFilter;
import org.dangcat.business.service.QueryResult;
import org.dangcat.business.validator.BusinessValidator;
import org.dangcat.commons.reflect.GenericUtils;
import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.resource.ResourceReader;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.ServiceBase;
import org.dangcat.framework.service.ServiceContext;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.annotation.Context;
import org.dangcat.framework.service.impl.ServiceInfo;
import org.dangcat.persistence.entity.EntityBase;
import org.dangcat.persistence.entity.EntityManager;
import org.dangcat.persistence.entity.EntityManagerFactory;
import org.dangcat.persistence.model.DataState;
import org.dangcat.persistence.model.DataStatus;
import org.dangcat.persistence.tablename.TableName;
import org.dangcat.persistence.validator.EntityBasicDataValidator;
import org.dangcat.persistence.validator.EntityDataValidator;

/**
 * ҵ����������
 * @author dangcat
 * 
 * @param <V>
 * @param <F>
 */
public class BusinessServiceBase<Q extends EntityBase, V extends EntityBase, F extends DataFilter> extends ServiceBase implements BusinessService<Q, V, F>
{
    private static String[] SELECT_FIELDNAMES = { "id", "name" };
    private BusinessValidator<V> businessValidator = null;
    private Collection<EntityDataValidator> entityDataValidators = new LinkedList<EntityDataValidator>();
    protected Map<String, Class<?>> genericClassMap = null;

    @Context
    private ServiceContext serviceContext;

    /**
     * ��������
     * @param parent �������������
     */
    public BusinessServiceBase(ServiceProvider parent)
    {
        super(parent);
    }

    protected void addEntityDataValidator(EntityDataValidator entityDataValidator)
    {
        if (entityDataValidator != null && !this.entityDataValidators.contains(entityDataValidator))
            this.entityDataValidators.add(entityDataValidator);
    }

    /**
     * ����ɾ�����¼���
     * @param deleteContext ���������ġ�
     */
    protected void afterDelete(LoadContext<V> deleteContext)
    {
    }

    /**
     * �����������¼���
     * @param saveContext ���������ġ�
     */
    protected void afterInsert(SaveContext<V> saveContext)
    {
    }

    /**
     * �����������ݺ��¼���
     * @param loadContext ���������ġ�
     */
    protected void afterLoad(LoadContext<V> loadContext)
    {
    }

    /**
     * �������غ��¼���
     * @param queryContext ���������ġ�
     */
    protected void afterQuery(QueryContext<Q> queryContext)
    {
    }

    /**
     * �����洢���¼���
     * @param saveContext ���������ġ�
     */
    protected void afterSave(SaveContext<V> saveContext)
    {
    }

    /**
     * ����ɾ��ǰ�¼���
     * @param deleteContext ���������ġ�
     */
    protected void beforeDelete(LoadContext<V> deleteContext) throws ServiceException
    {
        BusinessValidator<V> businessValidator = this.getBusinessValidator();
        if (businessValidator != null)
            businessValidator.beforeDelete(deleteContext);
    }

    /**
     * ��������ǰ�¼���
     * @param saveContext ���������ġ�
     */
    protected void beforeInsert(SaveContext<V> saveContext) throws ServiceException
    {
        this.beforeSave(saveContext);
    }

    /**
     * ������������ǰ�¼���
     * @param loadContext ���������ġ�
     */
    protected void beforeLoad(LoadContext<V> loadContext) throws ServiceException
    {
    }

    /**
     * ��������ǰ�¼���
     * @param queryContext ���������ġ�
     */
    protected void beforeQuery(QueryContext<Q> queryContext) throws ServiceException
    {
    }

    /**
     * �����洢ǰ�¼���
     * @param saveContext ���������ġ�
     */
    protected void beforeSave(SaveContext<V> saveContext) throws ServiceException
    {
        this.validate(saveContext);

        BusinessValidator<V> businessValidator = this.getBusinessValidator();
        if (businessValidator != null)
            businessValidator.beforeSave(saveContext);
    }

    /**
     * ����������á�
     */
    public EntityBase config(EntityBase config) throws ServiceException
    {
        ServiceInfo serviceInfo = this.getServiceContext().getServiceInfo();
        BusinessConfig businessConfig = (BusinessConfig) serviceInfo.getConfigProvider();
        if (businessConfig != null)
        {
            businessConfig.validate(config);
            if (!config.hasError())
            {
                businessConfig.save(config);
                return businessConfig.getCurrentEntity();
            }
        }
        return config;
    }

    /**
     * �½����ݡ�
     */
    @Override
    public V create(V entity) throws ServiceException
    {
        if (entity instanceof DataStatus && entity.getDataState() == null)
            entity.setDataState(DataState.Insert);
        if (entity.getDataState() != DataState.Insert)
            throw new BusinessException(BusinessException.DATASTATE_INCORRECT);
        this.prepareStore(entity);
        return new BusinessServiceSave<Q, V, F>(this).execute(entity);
    }

    /**
     * ɾ��ָ�����������ݡ�
     */
    @Override
    public boolean delete(Integer id) throws ServiceException
    {
        return this.delete(null, null, id);
    }

    protected boolean delete(TableName tableName, String sqlName, Object... primaryKeyValues) throws ServiceException
    {
        return new BusinessServiceDelete<Q, V, F>(this).execute(tableName, sqlName, primaryKeyValues);
    }

    protected F filter(F dataFilter) throws ServiceException
    {
        return dataFilter;
    }

    @SuppressWarnings("unchecked")
    protected BusinessValidator<V> getBusinessValidator()
    {
        if (this.businessValidator == null)
        {
            org.dangcat.business.annotation.BusinessValidator businessValidatorAnnotation = this.getClass().getAnnotation(org.dangcat.business.annotation.BusinessValidator.class);
            if (businessValidatorAnnotation != null && businessValidatorAnnotation.value() != null)
            {
                if (BusinessValidator.class.isAssignableFrom(businessValidatorAnnotation.value()))
                    this.businessValidator = (BusinessValidator<V>) ReflectUtils.newInstance(businessValidatorAnnotation.value());
            }
        }
        return this.businessValidator;
    }

    protected String getDatabaseName()
    {
        return null;
    }

    public Class<?> getEntityClass(String name)
    {
        if (this.genericClassMap == null)
            this.genericClassMap = GenericUtils.getClassGenericInfo(this.getClass());
        return this.genericClassMap.get(name);
    }

    protected EntityManager getEntityManager()
    {
        return EntityManagerFactory.getInstance().open(this.getDatabaseName());
    }

    protected ResourceReader getResourceReader()
    {
        return this.getServiceInfo().getResourceReader();
    }

    protected String[] getSelectFieldNames()
    {
        return SELECT_FIELDNAMES;
    }

    protected ServiceContext getServiceContext()
    {
        return this.serviceContext;
    }

    protected ServiceInfo getServiceInfo()
    {
        return this.serviceContext.getServiceInfo();
    }

    /**
     * �ж��Ƿ���Ȩ�ޡ�
     * @param permissionId Ȩ���롣
     * @return �Ƿ���ָ��Ȩ�ޡ�
     */
    public boolean hasPermission(Integer permissionId)
    {
        return this.getServiceContext().hasPermission(permissionId);
    }

    @Override
    public void initialize()
    {
        super.initialize();
        this.addEntityDataValidator(new EntityBasicDataValidator());
    }

    protected V locate(Object... primaryKeyValues) throws ServiceException
    {
        return new BusinessServiceView<Q, V, F>(this).execute(null, null, primaryKeyValues);
    }

    /**
     * ����ʵ�����ݡ�
     */
    protected void onCreate(V entity)
    {
    }

    protected <T extends EntityBase> List<T> pick(Class<T> classType) throws ServiceException
    {
        return new BusinessServicePick<Q, V, F>(this).execute(classType);
    }

    protected <T extends EntityBase> List<T> pick(Class<T> classType, F dataFilter) throws ServiceException
    {
        return new BusinessServicePick<Q, V, F>(this).execute(classType, this.filter(dataFilter));
    }

    protected <T extends EntityBase> List<T> pick(Class<T> classType, F dataFilter, TableName tableName, String sqlName) throws ServiceException
    {
        return new BusinessServicePick<Q, V, F>(this).execute(classType, this.filter(dataFilter), tableName, sqlName);
    }

    protected void prepareStore(V entity)
    {
    }

    /**
     * ����ָ����Χ�����ݡ�
     */
    @Override
    public QueryResult<Q> query(F dataFilter) throws ServiceException
    {
        return this.query(this.filter(dataFilter), null, null);
    }

    protected QueryResult<Q> query(F dataFilter, TableName tableName, String sqlName) throws ServiceException
    {
        return new BusinessServiceQuery<Q, V, F>(this).execute(this.filter(dataFilter), tableName, sqlName);
    }

    /**
     * ����ʵ�����ݡ�
     */
    @Override
    public V save(V entity) throws ServiceException
    {
        if (entity instanceof DataStatus && entity.getDataState() == null)
            entity.setDataState(DataState.Modified);
        if (entity.getDataState() == DataState.Insert)
            throw new BusinessException(BusinessException.DATASTATE_INCORRECT);
        this.prepareStore(entity);
        return new BusinessServiceSave<Q, V, F>(this).execute(entity);
    }

    /**
     * ����ָ����Χ���б�
     */
    public Map<Integer, String> select(F dataFilter) throws ServiceException
    {
        return this.select(dataFilter, null, null);
    }

    protected <N extends Number> Map<N, String> select(F dataFilter, TableName tableName, String sqlName) throws ServiceException
    {
        return new BusinessServiceSelect<Q, V, F, N>(this).execute(this.filter(dataFilter), tableName, sqlName);
    }

    @Override
    public Map<Number, String> selectMap(F dataFilter) throws ServiceException
    {
        return this.select(dataFilter, null, null);
    }

    protected void validate(SaveContext<V> saveContext) throws ServiceException
    {
        for (EntityDataValidator entityDataValidator : this.entityDataValidators)
            entityDataValidator.validate(saveContext.getSaveEntityContext());
    }

    /**
     * ��ѯָ�����������ݡ�
     */
    @Override
    public V view(Integer id) throws ServiceException
    {
        return this.view(null, null, id);
    }

    protected V view(TableName tableName, String sqlName, Object... primaryKeyValues) throws ServiceException
    {
        return new BusinessServiceView<Q, V, F>(this).execute(tableName, sqlName, primaryKeyValues);
    }
}
