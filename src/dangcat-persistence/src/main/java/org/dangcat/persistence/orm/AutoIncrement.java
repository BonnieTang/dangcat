package org.dangcat.persistence.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * �Զ�������������
 * @author dangcat
 * 
 */
class AutoIncrement
{
    private int count = 0;
    private long currentValue = -1l;
    private String fieldName = null;
    private TableGenerator tableGenerator = null;
    private String tableName = null;

    AutoIncrement(String tableName, String fieldName, TableGenerator tableGenerator)
    {
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.tableGenerator = tableGenerator;
    }

    /**
     * ��ʼ�����кá�
     * @param connection ���ݿ����ӡ�
     * @throws SQLException �����쳣��
     */
    protected void initialize(Connection connection) throws SQLException
    {
        if (this.tableGenerator != null)
        {
            Long currentValue = this.tableGenerator.query(connection, this.tableName);
            if (currentValue != null)
                this.currentValue = currentValue;
        }
        else
        {
            Statement statement = null;
            try
            {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(" + this.fieldName + ") MAXID FROM " + this.tableName);
                if (resultSet.next() && resultSet.getObject("MAXID") != null)
                    this.currentValue = resultSet.getLong("MAXID");
                resultSet.close();
            }
            finally
            {
                if (statement != null)
                    statement.close();
            }
        }
    }

    /**
     * ��ȡ��һ�����кš�
     * @param classType �������͡�
     * @return ���кš�
     */
    protected synchronized Object nextSequence(Class<?> classType)
    {
        this.currentValue++;
        this.count++;
        if (Long.class.equals(classType))
            return this.currentValue;
        return (int) this.currentValue;
    }

    /**
     * ������Ų��Ա��ֵ��
     * @param connection ���ݿ����ӡ�
     * @throws SQLException ִ���쳣��
     */
    protected void update(Connection connection) throws SQLException
    {
        if (this.tableGenerator != null && this.count > 0)
            this.tableGenerator.update(connection, this.tableName, this.currentValue);
    }
}
