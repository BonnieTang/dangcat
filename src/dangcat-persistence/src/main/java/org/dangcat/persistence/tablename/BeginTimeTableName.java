package org.dangcat.persistence.tablename;

/**
 * ��ʱ��ֱ�������
 * @author dangcat
 * 
 */
public class BeginTimeTableName extends DateTimeTableName
{
    private static final String DEFAULT_FIELDNAME = "BeginTime";

    public BeginTimeTableName(String name)
    {
        super(name, DEFAULT_FIELDNAME);
    }
}
