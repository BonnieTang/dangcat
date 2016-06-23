package org.dangcat.framework.service;

import org.apache.log4j.Logger;
import org.dangcat.framework.event.Event;
import org.dangcat.framework.event.EventHandler;

import java.util.*;

/**
 * �������
 *
 * @author dangcat
 */
public abstract class ServiceBase implements ServiceProvider, EventHandler {
    static {
        ServiceHelper.addInjectProvider(new ServiceInjectProvider());
    }

    public final Logger logger = Logger.getLogger(this.getClass());
    private List<Object> childrenList = new LinkedList<Object>();
    private boolean isEnabled = true;
    private ServiceProvider parent;
    private Map<Class<?>, Object> serviceContainer = new LinkedHashMap<Class<?>, Object>();

    /**
     * ����������
     *
     * @param parent ��������
     */
    public ServiceBase(ServiceProvider parent) {
        this.parent = parent;
    }

    /**
     * ��ӷ���
     */
    public void addService(Class<?> classType, Object service) {
        if (service != this) {
            this.serviceContainer.put(classType, service);
            if (!this.childrenList.contains(service))
                this.childrenList.add(service);
        }
    }

    /**
     * ��ȡ�ӷ����б�
     *
     * @return �ӷ��񼯺ϡ�
     */
    public Collection<Object> getChildren() {
        return this.childrenList;
    }

    protected Logger getLogger() {
        return this.logger;
    }

    /**
     * ���ʸ�����
     *
     * @return
     */
    public ServiceProvider getParent() {
        return this.parent;
    }

    /**
     * ��ȡָ�����͵ķ���
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> classType) {
        if (this.serviceContainer.containsKey(classType))
            return (T) this.serviceContainer.get(classType);
        if (this.parent != null)
            return this.parent.getService(classType);
        return null;
    }

    /**
     * �����¼���
     */
    public Object handle(Event event) {
        Object result = null;
        if (this.isEnabled()) {
            for (Object serviceObject : this.childrenList) {
                if (event.isCancel() || event.isHandled())
                    break;

                if (serviceObject instanceof EventHandler) {
                    EventHandler eventHandler = (EventHandler) serviceObject;
                    result = eventHandler.handle(event);
                }
            }
        }
        return result;
    }

    /**
     * ��ʼ������
     */
    public void initialize() {
        // �������ļ������ӷ���
        ServiceHelper.loadFromServiceXml(this);
        // ע�����
        this.inject();
    }

    protected void inject() {
        // ע�����
        ServiceHelper.inject(this, this);
        // ע���ӷ����е�
        for (Object childService : this.getChildren()) {
            if (childService instanceof ServiceBase) {
                ServiceBase serviceBase = (ServiceBase) childService;
                serviceBase.inject();
            } else
                ServiceHelper.inject(this, childService);
        }
    }

    /**
     * �����Ƿ�������
     */
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * ɾ������
     */
    public void removeService(Object service) {
        Class<?> classType = null;
        for (Class<?> key : this.serviceContainer.keySet()) {
            if (this.serviceContainer.get(key) == service) {
                classType = key;
                break;
            }
        }
        if (classType != null)
            this.serviceContainer.remove(classType);
        if (this.childrenList.contains(service))
            this.childrenList.remove(service);
    }
}
