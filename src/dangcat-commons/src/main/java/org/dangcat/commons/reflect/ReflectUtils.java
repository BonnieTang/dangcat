package org.dangcat.commons.reflect;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.ValueUtils;

/**
 * ת���������ߡ�
 */
public class ReflectUtils
{
    protected static final Logger logger = Logger.getLogger(ReflectUtils.class);

    /**
     * �������������
     * @param classLoader ���������
     */
    public static void addClassLoader(ClassLoader classLoader)
    {
        InstanceBuilder.addClassLoader(classLoader);
    }

    /**
     * ���������󿽱����ԡ�
     * @param srcObject ��Դ����
     * @param dstObject Ŀ�����
     */
    public static void copyProperties(Object srcObject, Object dstObject)
    {
        if (srcObject == null || dstObject == null)
            return;

        try
        {
            boolean isSameClass = srcObject.getClass().equals(dstObject.getClass());
            BeanInfo beanInfo = Introspector.getBeanInfo(srcObject.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors())
            {
                if (Class.class.getSimpleName().equalsIgnoreCase(propertyDescriptor.getName()))
                    continue;

                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod != null)
                {
                    Object value = readMethod.invoke(srcObject);
                    if (isSameClass)
                    {
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        if (writeMethod != null)
                            writeMethod.invoke(dstObject, value);
                    }
                    else
                        setProperty(dstObject, propertyDescriptor.getName(), value);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(srcObject, e);
        }
    }

    /**
     * ��һ�������ҵ�����ָ��ע�͵�ע�Ͷ���
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     */
    public static <T extends Annotation> T findAnnotation(Class<?> classType, Class<T> annotationType)
    {
        return BeanUtils.findAnnotation(classType, annotationType);
    }

    /**
     * ��һ�������ҵ�����ָ��ע�͵�����ע�Ͷ���
     * @param annotationList ע���б�
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     */
    public static void findAnnotations(Class<?> classType, Class<? extends Annotation> annotationType, List<Annotation> annotationList)
    {
        BeanUtils.findAnnotations(classType, annotationType, annotationList);
    }

    /**
     * ��һ�������ҵ�����ָ��ע�͵��ֶΡ�
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     */
    public static Field findField(Class<?> classType, Class<? extends Annotation> annotationType)
    {
        return BeanUtils.findField(classType, annotationType);
    }

    /**
     * ��һ�������ҵ�����ָ��ע�͵������ֶΡ�
     * @param classType ָ�����͡�
     * @param annotationType ע�����͡�
     */
    public static List<Field> findFields(Class<?> classType, Class<? extends Annotation> annotationType)
    {
        List<Field> fieldList = new LinkedList<Field>();
        BeanUtils.findFields(classType, annotationType, fieldList);
        return fieldList;
    }

    /**
     * ͨ�������ȡ�����ֶε�ֵ��
     * @param instance ����ʵ����
     * @param fieldName �ֶ����ơ�
     */
    public static Object getFieldValue(Object instance, String fieldName)
    {
        return BeanUtils.getFieldValue(instance, fieldName);
    }

    public static Object getInstance(Class<?> classType)
    {
        return ReflectUtils.invoke(classType, "getInstance");
    }

    /**
     * ��ȡ�������ԡ�
     * @param instance ����ʵ����
     * @param propertyName ��������
     */
    public static Object getProperty(Object instance, String propertyName)
    {
        return BeanUtils.getProperty(instance, propertyName);
    }

    public static Object invoke(Object instance, Method method, Object... params)
    {
        return BeanUtils.invoke(instance, method, params);
    }

    public static Object invoke(Object instance, String methodName, Object... params)
    {
        Object result = null;
        try
        {
            result = BeanUtils.invoke(instance, methodName, params);
        }
        catch (Exception e)
        {
            logger.error(instance, e);
        }
        return result;
    }

    /**
     * �Ƿ��ǳ������͡�
     */
    public static boolean isConstClassType(Class<?> classType)
    {
        return InstanceBuilder.getConstClassList().contains(classType);
    }

    /**
     * �������Ͷ���
     */
    public static Class<?> loadClass(String className)
    {
        return InstanceBuilder.loadClass(className.trim());
    }

    /**
     * �������Ʋ���ʵ����
     */
    public static Object newInstance(Class<?> classType)
    {
        return InstanceBuilder.newInstance(classType);
    }

    /**
     * ����ָ���������������ͺͲ�����������ʵ����
     * @param classType �������ơ�
     * @param parameterTypes �������͡�
     * @param parameters ����������
     * @return �����Ķ���ʵ����
     */
    public static Object newInstance(Class<?> classType, Class<?>[] parameterTypes, Object[] parameters)
    {
        return InstanceBuilder.newInstance(classType, parameterTypes, parameters);
    }

    /**
     * �������Ʋ���ʵ����
     */
    public static Object newInstance(String className)
    {
        return InstanceBuilder.newInstance(className);
    }

    /**
     * ����ָ���������������ͺͲ�����������ʵ����
     * @param className �������ơ�
     * @param parameterTypes �������͡�
     * @param parameters ����������
     * @return �����Ķ���ʵ����
     */
    public static Object newInstance(String className, Class<?>[] parameterTypes, Object[] parameters)
    {
        return InstanceBuilder.newInstance(className, parameterTypes, parameters);
    }

    /**
     * ����ֵת����ʵ��ֵ����
     */
    public static Object parseValue(Class<?> classType, String value)
    {
        Object result = null;
        try
        {
            if (ValueUtils.isEmpty(value))
                return result;
            if (value.getClass().equals(classType))
                return value;
            if (classType.equals(Class.class))
                result = InstanceBuilder.loadClass(value.trim());
            else
                result = ValueUtils.parseValue(classType, value);
        }
        catch (Exception e)
        {
            logger.error(classType, e);
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
        BeanUtils.setFieldValue(instance, fieldName, value);
    }

    /**
     * ���ö������ԡ�
     * @param instance ����ʵ����
     * @param propertyName ��������
     * @param value ����ֵ��
     */
    public static void setProperty(Object instance, String propertyName, Object value)
    {
        BeanUtils.setProperty(instance, propertyName, value);
    }

    public static String toFieldName(String value)
    {
        if (!ValueUtils.isEmpty(value) && Character.isLowerCase(value.charAt(0)))
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        return value;
    }

    public static String toPropertyName(String value)
    {
        if (!ValueUtils.isEmpty(value) && Character.isUpperCase(value.charAt(0)))
            return value.substring(0, 1).toLowerCase() + value.substring(1);
        return value;
    }
}
