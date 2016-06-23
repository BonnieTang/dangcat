package org.dangcat.commons.reflect;

import org.apache.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtils
{
    protected static final Logger logger = Logger.getLogger(BeanUtils.class);

    /**
     * ��һ�������ҵ�����ָ��ע�͵�ע�Ͷ���
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     */
    public static <T extends Annotation> T findAnnotation(Class<?> classType, Class<T> annotationType)
    {
        if (Object.class.equals(classType) || classType == null)
            return null;

        T annotation = classType.getAnnotation(annotationType);
        if (annotation == null)
            annotation = findAnnotation(classType.getSuperclass(), annotationType);
        return annotation;
    }

    /**
     * ��һ�������ҵ�����ָ��ע�͵�����ע�Ͷ���
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     * @param annotationList ע���б�
     */
    public static void findAnnotations(Class<?> classType, Class<? extends Annotation> annotationType, List<Annotation> annotationList)
    {
        if (Object.class.equals(classType) || classType == null)
            return;

        findAnnotations(classType.getSuperclass(), annotationType, annotationList);

        Annotation annotation = classType.getAnnotation(annotationType);
        if (annotation != null)
            annotationList.add(annotation);
    }

    /**
     * ��һ�������ҵ�����ָ��ע�͵��ֶΡ�
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     */
    public static Field findField(Class<?> classType, Class<? extends Annotation> annotationType)
    {
        if (Object.class.equals(classType) || classType == null)
            return null;

        Field found = null;
        for (Field field : classType.getDeclaredFields())
        {
            if (Modifier.isStatic(field.getModifiers()))
                continue;

            if (field.getAnnotation(annotationType) != null)
            {
                found = field;
                break;
            }
        }
        if (found == null)
            found = findField(classType.getSuperclass(), annotationType);
        return found;
    }

    /**
     * ��һ�������ҵ�����ָ�����ֵ��ֶΡ�
     * @param classType ָ�����͡�
     * @param fieldName �ֶ�����
     */
    public static Field findField(Class<?> classType, String fieldName)
    {
        return findField(classType, fieldName, true);
    }

    public static Field findField(Class<?> classType, String fieldName, boolean excludeStatic)
    {
        if (Object.class.equals(classType) || classType == null)
            return null;

        Field found = null;
        for (Field field : classType.getDeclaredFields())
        {
            if (excludeStatic && Modifier.isStatic(field.getModifiers()))
                continue;

            if (field.getName().equalsIgnoreCase(fieldName))
            {
                found = field;
                break;
            }
        }
        if (found == null)
            found = findField(classType.getSuperclass(), fieldName);
        return found;
    }

    /**
     * ��һ�������ҵ�����ָ��ע�͵������ֶΡ�
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     * @param fieldList �ֶ��б�
     */
    public static void findFields(Class<?> classType, Class<? extends Annotation> annotationType, List<Field> fieldList)
    {
        if (Object.class.equals(classType) || classType == null)
            return;

        findFields(classType.getSuperclass(), annotationType, fieldList);

        for (Field field : classType.getDeclaredFields())
        {
            if (field.getAnnotation(annotationType) != null)
                fieldList.add(field);
        }
    }

    public static <T extends Annotation> T getAnnotation(Class<?> classType, String name, Class<T> annotationType)
    {
        if (classType == null || Object.class.equals(classType))
            return null;

        BeanInfo beanInfo = BeanUtils.getBeanInfo(classType);
        T annotation = null;
        if (beanInfo != null)
        {
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors())
            {
                if (Class.class.getSimpleName().equalsIgnoreCase(propertyDescriptor.getName()) || !name.equalsIgnoreCase(propertyDescriptor.getName()))
                    continue;
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod != null)
                    annotation = readMethod.getAnnotation(annotationType);
                break;
            }
        }
        if (annotation == null)
        {
            for (Field field : beanInfo.getBeanDescriptor().getBeanClass().getDeclaredFields())
            {
                if (Class.class.getSimpleName().equalsIgnoreCase(field.getName()) || !name.equalsIgnoreCase(field.getName()) || Modifier.isStatic(field.getModifiers()))
                    continue;
                annotation = field.getAnnotation(annotationType);
                break;
            }
        }
        if (annotation == null)
            annotation = getAnnotation(classType.getSuperclass(), name, annotationType);
        return annotation;
    }

    public static BeanInfo getBeanInfo(Class<?> classType)
    {
        BeanInfo beanInfo = null;
        try
        {
            beanInfo = Introspector.getBeanInfo(classType);
        }
        catch (IntrospectionException e)
        {
        }
        return beanInfo;
    }

    /**
     * ͨ�������ȡ�����ֶε�ֵ��
     * @param instance ����ʵ����
     * @param fieldName �ֶ����ơ�
     */
    public static Object getFieldValue(Object instance, String fieldName)
    {
        Object result = null;
        if (instance != null)
        {
            try
            {
                if (instance instanceof Class<?>)
                {
                    Field field = findField((Class<?>) instance, fieldName);
                    if (field != null)
                    {
                        field.setAccessible(true);
                        result = field.get(null);
                    }
                }
                else
                {
                    Field field = findField(instance.getClass(), fieldName);
                    if (field != null)
                    {
                        field.setAccessible(true);
                        result = field.get(instance);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ��ȡ�������ԡ�
     * @param instance ����ʵ����
     * @param propertyName ��������
     */
    public static Object getProperty(Object instance, String propertyName)
    {
        if (instance != null)
        {
            try
            {
                BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
                for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors())
                {
                    if (propertyDescriptor.getName().equalsIgnoreCase(propertyName))
                    {
                        Method method = propertyDescriptor.getReadMethod();
                        if (method != null)
                            return method.invoke(instance);
                    }
                }
            }
            catch (Exception e)
            {
                logger.error(instance, e);
            }
        }
        return null;
    }

    public static List<PropertyDescriptor> getPropertyDescriptorList(Class<?> classType)
    {
        List<PropertyDescriptor> propertyDescriptorList = new ArrayList<PropertyDescriptor>();
        try
        {
            BeanInfo beanInfo = Introspector.getBeanInfo(classType);
            if (beanInfo != null)
            {
                for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors())
                {
                    if ("class".equalsIgnoreCase(propertyDescriptor.getName()))
                        continue;
                    propertyDescriptorList.add(propertyDescriptor);
                }
            }
        }
        catch (NullPointerException e)
        {
            System.out.println(e);
        }
        catch (IntrospectionException e)
        {
        }
        return propertyDescriptorList;
    }

    public static Map<String, Method> getPropertyMethodMap(Class<?> classType, boolean couldWrite)
    {
        Map<String, Method> propertyMap = null;
        List<PropertyDescriptor> propertyDescriptorList = BeanUtils.getPropertyDescriptorList(classType);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorList)
        {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null)
            {
                if (!Modifier.isPublic(readMethod.getModifiers()) || Modifier.isAbstract(readMethod.getModifiers()))
                    continue;

                if (couldWrite && propertyDescriptor.getWriteMethod() == null)
                    continue;

                if (propertyMap == null)
                    propertyMap = new HashMap<String, Method>();
                propertyMap.put(propertyDescriptor.getName(), readMethod);
            }
        }
        return propertyMap;
    }

    public static Object invoke(Object instance, Method method, Object... params)
    {
        Object result = null;
        Class<?>[] parameterTypes = method.getParameterTypes();
        try
        {
            if (parameterTypes == null || parameterTypes.length == 0)
                result = method.invoke(instance);
            else
                result = method.invoke(instance, params);
        }
        catch (Exception e)
        {
            logger.error(instance, e);
        }
        return result;
    }

    public static Object invoke(Object instance, String methodName, Object... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
            NoSuchMethodException
    {
        Object result = null;
        Class<?> classType = instance.getClass();
        if (Class.class.equals(classType))
            classType = (Class<?>) instance;

        if (params != null && params.length > 0) // �в����ĵ��÷�ʽ��
        {
            Class<?>[] classes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++)
            {
                if (params[i] != null)
                    classes[i] = params[i].getClass();
            }
            Method method = classType.getMethod(methodName, classes);
            if (method != null)
                result = method.invoke(instance, params);
        }
        else
        // �޲����ĵ��÷�ʽ��
        {
            Method method = classType.getMethod(methodName);
            if (method != null)
                result = method.invoke(instance);
        }
        return result;
    }

    /**
     * ͨ�������ȡ�����ֶε�ֵ��
     * @param instance ����ʵ����
     * @param fieldName �ֶ����ơ�
     */
    public static void setFieldValue(Object instance, String fieldName, Object value)
    {
        if (instance != null)
        {
            try
            {
                Field field = findField(instance.getClass(), fieldName);
                if (field != null)
                {
                    field.setAccessible(true);
                    field.set(instance, value);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * ���ö������ԡ�
     * @param instance ����ʵ����
     * @param propertyName ��������
     * @param value ����ֵ��
     */
    public static void setProperty(Object instance, String propertyName, Object value)
    {
        if (instance == null)
            return;
        try
        {
            BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors())
            {
                if (propertyDescriptor.getName().equalsIgnoreCase(propertyName))
                {
                    if (value instanceof String)
                        value = ReflectUtils.parseValue(propertyDescriptor.getPropertyType(), (String) value);
                    Method method = propertyDescriptor.getWriteMethod();
                    if (method != null)
                        method.invoke(instance, value);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(instance, e);
        }
    }
}
