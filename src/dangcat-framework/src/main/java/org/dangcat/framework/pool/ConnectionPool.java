package org.dangcat.framework.pool;

import org.dangcat.commons.crypto.CryptoUtils;
import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.service.impl.PropertiesManager;

import java.util.HashMap;
import java.util.Map;

public abstract class ConnectionPool<T> extends ObjectPool<T>
{
    public static final String Password = "password";
    private static final String[] COMMUNICATION_ERRORS = {"communication", "connection", "socket", "�׽���", "����", "server has gone away"};
    private String name;
    /** ���Ӳ����� */
    private Map<String, String> params = new HashMap<String, String>();

    public ConnectionPool(String name, Map<String, String> params)
    {
        this.name = name;
        if (params != null)
            this.params.putAll(params);
    }

    protected String decryptPassWord(String passWord) throws SessionException
    {
        if (!ValueUtils.isEmpty(passWord))
        {
            passWord = CryptoUtils.decrypt(passWord);
            if (passWord == null)
                throw new SessionException("**password decrypt failed!");
        }
        return passWord;
    }

    public String getName()
    {
        return name;
    }

    protected boolean getParamAsBoolean(String propertyName, boolean defaultValue)
    {
        String value = this.getProperty(propertyName);
        return ValueUtils.parseBoolean(value, defaultValue);
    }

    protected Integer getParamAsInt(String propertyName, Integer defaultValue)
    {
        String value = this.getProperty(propertyName);
        return ValueUtils.parseInt(value, defaultValue);
    }

    protected String getParamAsString(String propertyName, String defaultValue)
    {
        String value = this.getProperty(propertyName);
        return ValueUtils.isEmpty(value) ? defaultValue : value;
    }

    public Map<String, String> getParams()
    {
        return params;
    }

    private String getProperty(String propertyName)
    {
        String propertyValue = this.getParams().get(propertyName);
        return PropertiesManager.getInstance().getValue(propertyValue);
    }

    /**
     * ��ʼ�����ӳء�
     * @throws SessionException
     */
    public void initialize() throws SessionException
    {
        try
        {
            this.initialize(this);
        }
        catch (SessionException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new SessionException(e);
        }
    }

    /**
     * ��ʼ�����ӳء�
     * @throws SessionException
     */
    protected void initialize(Object instance) throws SessionException
    {
        for (String propertyName : this.getParams().keySet())
        {
            String propertyValue = this.getProperty(propertyName);
            // ������������
            if (propertyName.equalsIgnoreCase(Password)) // ���ÿ���
                propertyValue = decryptPassWord(propertyValue);
            ReflectUtils.setProperty(instance, propertyName, propertyValue);
        }
    }

    /**
     * �Ƿ�ΪͨѶ�쳣��
     * @param exception
     * @return
     */
    public boolean isCommunicationsException(Exception exception)
    {
        String exceptionText = exception.toString().toLowerCase();
        if (exceptionText != null)
        {
            for (String error : COMMUNICATION_ERRORS)
            {
                if (exceptionText.contains(error))
                    return true;
            }
        }
        return false;
    }

    public boolean isDefault()
    {
        return this.getParamAsBoolean("default", false);
    }
}
