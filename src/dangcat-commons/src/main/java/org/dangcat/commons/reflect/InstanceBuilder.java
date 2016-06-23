package org.dangcat.commons.reflect;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.ValueUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class InstanceBuilder
{
    protected static final Logger logger = Logger.getLogger(InstanceBuilder.class);
    /**
     * ���������
     */
    private static List<ClassLoader> classLoaderList = new ArrayList<ClassLoader>();
    private static List<Class<?>> constClassList = new ArrayList<Class<?>>();

    /**
     * �������������
     * @param classLoader ���������
     */
    protected static void addClassLoader(ClassLoader classLoader)
    {
        if (classLoader != null && !classLoaderList.contains(classLoader))
            classLoaderList.add(classLoader);
    }

    protected static List<Class<?>> getConstClassList()
    {
        if (constClassList.size() == 0)
        {
            constClassList.add(String.class);
            constClassList.add(Short.class);
            constClassList.add(Long.class);
            constClassList.add(Integer.class);
            constClassList.add(Double.class);
            constClassList.add(Boolean.class);
            constClassList.add(Byte.class);
            constClassList.add(Character.class);
            constClassList.add(Timestamp.class);
            constClassList.add(Date.class);

            constClassList.add(short.class);
            constClassList.add(long.class);
            constClassList.add(int.class);
            constClassList.add(double.class);
            constClassList.add(boolean.class);
            constClassList.add(byte.class);
            constClassList.add(byte[].class);
            constClassList.add(char.class);
            constClassList.add(char[].class);
        }
        return constClassList;
    }

    /**
     * �����������͡�
     */
    protected static Class<?> loadClass(String className)
    {
        Class<?> classType = null;
        for (Class<?> constClassType : getConstClassList())
        {
            if (constClassType.getSimpleName().equalsIgnoreCase(className))
                return constClassType;
        }

        if (!ValueUtils.isEmpty(className))
        {
            for (ClassLoader classLoader : classLoaderList)
            {
                try
                {
                    classType = classLoader.loadClass(className);
                    if (classType != null)
                        break;
                }
                catch (ClassNotFoundException e)
                {
                }
            }
            if (classType == null)
            {
                try
                {
                    classType = Class.forName(className.trim());
                }
                catch (ClassNotFoundException e)
                {
                }
            }
        }
        return classType;
    }

    /**
     * �������Ʋ���ʵ����
     */
    protected static Object newInstance(Class<?> classType)
    {
        Object result = null;
        try
        {
            if (classType != null)
            {
                if (classType.isArray() && classType.getComponentType() != null)
                    result = Array.newInstance(classType.getComponentType(), 0);
                else
                    result = classType.newInstance();
            }
        }
        catch (Exception e)
        {
            logger.error(classType, e);
        }
        return result;
    }

    /**
     * ����ָ�����͡��������ͺͲ�����������ʵ����
     * @param classType ���͡�
     * @param parameterTypes �������͡�
     * @param parameters ����������
     * @return �����Ķ���ʵ����
     */
    protected static Object newInstance(Class<?> classType, Class<?>[] parameterTypes, Object[] parameters)
    {
        Object result = null;
        try
        {
            if (classType != null)
            {
                Constructor<?> found = null;
                for (Constructor<?> constructor : classType.getConstructors())
                {
                    Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
                    if (constructorParameterTypes.length == 0 || found == null)
                        found = constructor;
                    else if (parameterTypes != null && constructorParameterTypes.length == parameterTypes.length)
                    {
                        boolean isAssignAble = true;
                        for (int i = 0; i < constructorParameterTypes.length; i++)
                        {
                            Class<?> parameterType = parameterTypes[i];
                            Class<?> constructorParameterType = constructorParameterTypes[i];
                            if (!constructorParameterType.isAssignableFrom(parameterType))
                            {
                                isAssignAble = false;
                                break;
                            }
                        }
                        if (isAssignAble)
                            found = constructor;
                    }
                }
                if (found != null)
                {
                    if (found.getParameterTypes().length == 0)
                        result = found.newInstance();
                    else
                        result = found.newInstance(parameters);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(classType, e);
        }
        return result;
    }

    /**
     * �������Ʋ���ʵ����
     */
    protected static Object newInstance(String className)
    {
        Object result = null;
        Class<?> classType = loadClass(className);
        if (classType != null)
            result = newInstance(classType);
        return result;
    }

    /**
     * ����ָ���������������ͺͲ�����������ʵ����
     * @param className �������ơ�
     * @param parameterTypes �������͡�
     * @param parameters ����������
     * @return �����Ķ���ʵ����
     */
    protected static Object newInstance(String className, Class<?>[] parameterTypes, Object[] parameters)
    {
        Object result = null;
        Class<?> classType = loadClass(className);
        if (classType != null)
            result = newInstance(classType, parameterTypes, parameters);
        return result;
    }
}
