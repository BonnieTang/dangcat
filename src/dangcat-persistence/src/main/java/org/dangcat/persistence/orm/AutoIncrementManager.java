package org.dangcat.persistence.orm;

import org.dangcat.commons.utils.ValueUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * �����ֶι���
 * @author dangcat
 * 
 */
public class AutoIncrementManager
{
    /** �����ֶ�ӳ��� */
    private static Map<String, Map<String, AutoIncrement>> databaseAutoIncrementMap = new HashMap<String, Map<String, AutoIncrement>>();
    private String databaseName = null;

    AutoIncrementManager(String databaseName) {
        this.databaseName = databaseName;
    }

    public static void reset()
    {
        reset(null, null);
    }

    public static void reset(String databaseName, String tableName)
    {
        synchronized (databaseAutoIncrementMap)
        {
            if (ValueUtils.isEmpty(tableName))
                databaseAutoIncrementMap.clear();
            else
            {
                Map<String, AutoIncrement> autoIncrementMap = databaseAutoIncrementMap.get(databaseName);
                if (autoIncrementMap != null)
                {
                    synchronized (autoIncrementMap)
                    {
                        if (ValueUtils.isEmpty(tableName))
                            autoIncrementMap.clear();
                        else
                            autoIncrementMap.remove(tableName);
                    }
                }
            }
        }
    }

    private Map<String, AutoIncrement> getDatabaseAutoIncrementMap()
    {
        Map<String, AutoIncrement> autoIncrementMap = null;
        synchronized (databaseAutoIncrementMap)
        {
            autoIncrementMap = databaseAutoIncrementMap.get(this.databaseName);
            if (autoIncrementMap == null)
            {
                autoIncrementMap = new HashMap<String, AutoIncrement>();
                databaseAutoIncrementMap.put(this.databaseName, autoIncrementMap);
            }
        }
        return autoIncrementMap;
    }

    /**
     * ���ݱ����ֶ�����ȡ��һ���������С�
     * @param tableName ������
     * @param fieldName �ֶ�����
     * @param classType �ֶ����͡�
     * @param tableGenerator ��Ų��ԡ�
     * @return �������С�
     * @throws SQLException
     */
    protected Object nextSequence(Connection connection, String tableName, String fieldName, Class<?> classType, TableGenerator tableGenerator) throws SQLException
    {
        Object sequence = null;
        Map<String, AutoIncrement> autoIncrementMap = this.getDatabaseAutoIncrementMap();
        synchronized (autoIncrementMap)
        {
            AutoIncrement autoIncrement = autoIncrementMap.get(tableName);
            if (autoIncrement == null)
            {
                autoIncrement = new AutoIncrement(tableName, fieldName, tableGenerator);
                autoIncrement.initialize(connection);
                autoIncrementMap.put(tableName, autoIncrement);
            }
            sequence = autoIncrement.nextSequence(classType);
        }
        return sequence;
    }

    protected void update(Connection connection) throws SQLException
    {
        Map<String, AutoIncrement> autoIncrementMap = this.getDatabaseAutoIncrementMap();
        synchronized (autoIncrementMap)
        {
            for (AutoIncrement autoIncrement : autoIncrementMap.values())
                autoIncrement.update(connection);
        }
    }
}
