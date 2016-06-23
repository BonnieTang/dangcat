package org.dangcat.persistence.model;

import org.dangcat.persistence.exception.TableException;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ݱ��ϡ�
 * @author dangcat
 * 
 */
public class DataSet implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private List<Table> tables = new ArrayList<Table>();

    /**
     * �������ݱ����
     */
    public void add(Table table)
    {
        if (!tables.contains(table))
            tables.add(table);
    }

    /**
     * ���ݱ��ϡ�
     */
    public List<Table> getTables()
    {
        return this.tables;
    }

    /**
     * �������ݡ�
     */
    public void load() throws TableException
    {
        for (Table table : this.tables)
            table.load();
    }

    /**
     * ɾ�����ݱ����
     */
    public void remove(Table table)
    {
        if (tables.contains(table))
            tables.remove(table);
    }

    /**
     * �洢���ݡ�
     */
    public void save() throws TableException
    {
        for (Table table : this.tables)
            table.save();
    }
}
