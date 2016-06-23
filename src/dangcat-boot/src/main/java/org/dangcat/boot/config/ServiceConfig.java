package org.dangcat.boot.config;

import org.dangcat.boot.ApplicationContext;
import org.dangcat.boot.ConfigureReader;
import org.dangcat.boot.event.ChangeEventAdaptor;
import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.conf.ConfigBase;
import org.dangcat.framework.conf.ConfigValue;
import org.dangcat.framework.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ���û��ࡣ
 * @author dangcat
 * 
 */
public abstract class ServiceConfig extends ConfigBase
{
    private static final String Enabled = "Enabled";
    private List<ChangeEventAdaptor> changeEventAdaptorList = new ArrayList<ChangeEventAdaptor>();
    private List<ChangeEventAdaptor> configChangeEventAdaptorList = new ArrayList<ChangeEventAdaptor>();

    public ServiceConfig(String name)
    {
        super(name);
        this.getApplicationContext().addChangeEventAdaptor(new ChangeEventAdaptor()
        {
            @Override
            public void afterChanged(Object sender, Event event)
            {
                onConfigChanged();
            }
        });
        this.addConfigValue(Enabled, boolean.class, this.getDefaultEnabled());
    }

    /**
     * ��������޸�������
     * @param changedListener �޸���������
     */
    public void addChangeEventAdaptor(ChangeEventAdaptor changeEventAdaptor)
    {
        if (changeEventAdaptor != null && !this.changeEventAdaptorList.contains(changeEventAdaptor))
            this.changeEventAdaptorList.add(changeEventAdaptor);
    }

    public void addConfigChangeEventAdaptor(ChangeEventAdaptor changeEventAdaptor)
    {
        if (changeEventAdaptor != null && !this.configChangeEventAdaptorList.contains(changeEventAdaptor))
            this.configChangeEventAdaptorList.add(changeEventAdaptor);
    }

    @Override
    protected ConfigValue addConfigValue(String configName, Class<?> classType, Object defaultValue)
    {
        ConfigValue configValue = super.addConfigValue(configName, classType, defaultValue);
        configValue.setConfigValue(this.getXmlValue(configName));
        return configValue;
    }

    /**
     * �����޸��¼���
     * @param name �������ơ�
     * @param value �޸�ֵ��
     */
    private void fireAfterChanged(Map<String, String> changedConfigMap)
    {
        Event event = new Event(this.getName());
        event.getParams().put(Map.class.getSimpleName(), changedConfigMap);
        for (ChangeEventAdaptor changeEventAdaptor : this.changeEventAdaptorList)
            changeEventAdaptor.afterChanged(this, event);

        StringBuilder info = new StringBuilder();
        info.append("Config ");
        info.append(this.getName());
        info.append(" changed: ");
        if (changedConfigMap != null)
        {
            for (String configName : changedConfigMap.keySet())
            {
                info.append(Environment.LINETAB_SEPARATOR);
                info.append(configName);
                info.append(" = ");
                info.append(this.getCurrentValue(configName));
            }
        }
        logger.info(info);
    }

    /**
     * �����޸��¼���
     * @param name �������ơ�
     * @param value �޸�ֵ��
     */
    private void fireBeforeChanged(Map<String, String> changedConfigMap)
    {
        Event event = new Event(this.getName());
        event.getParams().put(Map.class.getSimpleName(), changedConfigMap);
        for (ChangeEventAdaptor changeEventAdaptor : this.changeEventAdaptorList)
            changeEventAdaptor.beforeChange(this, event);
    }

    private void fireConfigChanged(String key, String value)
    {
        Event event = new Event(key);
        for (ChangeEventAdaptor changeEventAdaptor : this.configChangeEventAdaptorList)
            changeEventAdaptor.beforeChange(this, event);
        this.getConfigValueMap().get(key).setConfigValue(value);
        for (ChangeEventAdaptor changeEventAdaptor : this.configChangeEventAdaptorList)
            changeEventAdaptor.afterChanged(this, event);
    }

    /**
     * ��ȡȫ�������ġ�
     */
    protected ApplicationContext getApplicationContext()
    {
        return ApplicationContext.getInstance();
    }

    /**
     * �Ƿ���á�
     */
    protected boolean getDefaultEnabled()
    {
        return true;
    }

    protected String getParentPath()
    {
        return this.getName();
    }

    private String getXmlValue(String configName)
    {
        ConfigureReader configureReader = this.getApplicationContext().getConfigureReader();
        String value = configureReader.getAttributValue(this.getParentPath(), configName);
        if (!ValueUtils.isEmpty(value))
            return value;
        return configureReader.getXmlValue(this.getParentPath() + "/" + configName);
    }

    public boolean isEnabled()
    {
        return this.getBooleanValue(Enabled);
    }

    /**
     * ���������޸��¼���
     */
    private void onConfigChanged()
    {
        Map<String, String> configChangedMap = new HashMap<String, String>();
        for (Entry<String, ConfigValue> entry : this.getConfigValueMap().entrySet())
        {
            String newValue = this.getXmlValue(entry.getKey());
            String oldValue = entry.getValue().getConfigValue();
            if (ValueUtils.compare(newValue, oldValue) != 0)
                configChangedMap.put(entry.getKey(), newValue);
        }
        if (configChangedMap.size() > 0)
        {
            this.fireBeforeChanged(configChangedMap);
            for (String key : configChangedMap.keySet())
                this.fireConfigChanged(key, configChangedMap.get(key));
            this.fireAfterChanged(configChangedMap);
        }
    }

    public void removeChangeEventAdaptor(ChangeEventAdaptor changeEventAdaptor)
    {
        if (changeEventAdaptor != null && this.changeEventAdaptorList.contains(changeEventAdaptor))
            this.changeEventAdaptorList.remove(changeEventAdaptor);
    }

    public void removeConfigChangeEventAdaptor(ChangeEventAdaptor changeEventAdaptor)
    {
        if (changeEventAdaptor != null && this.configChangeEventAdaptorList.contains(changeEventAdaptor))
            this.configChangeEventAdaptorList.remove(changeEventAdaptor);
    }
}
