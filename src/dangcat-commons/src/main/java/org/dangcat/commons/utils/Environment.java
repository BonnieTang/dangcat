package org.dangcat.commons.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.dangcat.commons.io.FileUtils;

public class Environment
{
    private static Locale currentLocale = null;
    public static final String DANGCAT_HOME = "DANGCAT_HOME";
    private static String homePath = null;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String LINETAB_SEPARATOR = LINE_SEPARATOR + "\t";
    private static final String LOCALE_KEY = "dangcat.server.language";
    private static ThreadLocal<Locale> localeThreadLocal = new ThreadLocal<Locale>();
    protected static final Logger logger = Logger.getLogger(Environment.class);
    private static String SystemDangCatHome = null;

    public static void clearHomePath()
    {
        homePath = null;
        System.setProperty(DANGCAT_HOME, "");
    }

    public static Locale getCurrentLocale()
    {
        Locale locale = localeThreadLocal.get();
        return locale == null ? getDefaultLocale() : locale;
    }

    /**
     * ��ȡ��ǰ�Ľ���ID��
     */
    public static Integer getCurrentPID()
    {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return ValueUtils.parseInt(name.substring(0, name.indexOf('@')));
    }

    /**
     * ��ȡ�����趨��
     */
    public static Locale getDefaultLocale()
    {
        if (currentLocale == null)
            currentLocale = LocaleUtils.parse(System.getProperty(LOCALE_KEY));
        return currentLocale;
    }

    /**
     * ��ȡ�����趨��
     */
    public static String getHomePath()
    {
        if (homePath == null)
            setHomePath(null);
        return homePath;
    }

    /**
     * �Ƿ����ģʽ��
     * @return
     */
    public static boolean isDebugEnabled()
    {
        return isModuleEnabled("debug", Boolean.FALSE);
    }

    /**
     * ģ���Ƿ�ģʽ������
     */
    public static boolean isModuleEnabled(String module, Boolean defaultValue)
    {
        String value = System.getProperty("dangcat." + module + ".enabled");
        return ValueUtils.parseBoolean(value, defaultValue);
    }

    /**
     * �Ƿ����ģʽ��
     * @return
     */
    public static boolean isTestEnabled()
    {
        return isModuleEnabled("test", Boolean.FALSE);
    }

    public static void removeLocale()
    {
        localeThreadLocal.remove();
    }

    public static void setDefaultLocale(String localeName)
    {
        if (!ValueUtils.isEmpty(localeName) && LocaleUtils.parse(localeName) != null)
            System.setProperty(LOCALE_KEY, localeName);
    }

    /**
     * ���ñ����趨��
     */
    public static void setHomePath(Class<?> classType)
    {
        try
        {
            if (SystemDangCatHome == null)
            {
                SystemDangCatHome = System.getProperty(DANGCAT_HOME);
                if (ValueUtils.isEmpty(SystemDangCatHome))
                    SystemDangCatHome = "";
            }
            File currentDirectory = null;
            if (!ValueUtils.isEmpty(SystemDangCatHome))
            {
                File directory = new File(SystemDangCatHome);
                if (directory.exists() && directory.isDirectory())
                    currentDirectory = directory;
            }
            if (currentDirectory == null)
            {
                if (classType != null)
                    currentDirectory = new File(FileUtils.getResourcePath(classType, null));
                else
                {
                    currentDirectory = new File("./target");
                    if (!currentDirectory.exists())
                        currentDirectory = new File(".");
                }
            }
            // ��������Ѿ�����Jar��Ҫȡ����Ŀ¼��
            if (currentDirectory.getName().endsWith(".jar") || currentDirectory.getName().endsWith("classes"))
                currentDirectory = currentDirectory.getParentFile();
            // ���л�����������lib�¡�
            if (currentDirectory.getName().endsWith("lib"))
                currentDirectory = currentDirectory.getParentFile();
            if (currentDirectory.exists())
            {
                homePath = currentDirectory.getCanonicalPath();
                System.setProperty(DANGCAT_HOME, homePath);
                System.out.println(DANGCAT_HOME + ": " + homePath);
            }
        }
        catch (Exception e)
        {

        }
    }

    public static void setLocale(Locale locale)
    {
        localeThreadLocal.set(locale);
    }

    /**
     * ����ģ�������
     */
    public static void setModuleEnabled(String module, boolean enabled)
    {
        System.setProperty("dangcat." + module + ".enabled", enabled ? "true" : "false");
    }
}
