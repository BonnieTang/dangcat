package org.dangcat.framework.conf;

import java.util.Map;

/**
 * ���ÿ��Ʊ�
 * @author dangcat
 * 
 */
public interface ConfigProvider
{
    public Map<String, ConfigValue> getConfigValueMap();
}
