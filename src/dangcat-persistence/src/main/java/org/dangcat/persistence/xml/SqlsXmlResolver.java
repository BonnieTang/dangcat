package org.dangcat.persistence.xml;

import org.dangcat.commons.database.DatabaseType;
import org.dangcat.commons.serialize.xml.XmlResolver;
import org.dangcat.persistence.sql.Sql;
import org.dangcat.persistence.sql.Sqls;

/**
 * ��λ�����������
 * @author dangcat
 * 
 */
public class SqlsXmlResolver extends XmlResolver
{
    private DatabaseType databaseType = null;
    private Sqls sqls = null;

    /**
     * ������������
     */
    public SqlsXmlResolver()
    {
        super(Sqls.class.getSimpleName());
        this.addChildXmlResolver(new SqlXmlResolver());
    }

    /**
     * ������Ԫ�ض���
     * @param elementName ��Ԫ�����ơ�
     * @param child ��Ԫ�ض���
     */
    @Override
    protected void afterChildCreate(String elementName, Object child)
    {
        if (child != null)
        {
            Sql sql = (Sql) child;
            if (this.databaseType != null)
                sql.setDatabaseType(this.databaseType);
            this.sqls.add(sql);
        }
    }

    public DatabaseType getDatabaseType()
    {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType)
    {
        this.databaseType = databaseType;
    }

    @Override
    public void setResolveObject(Object resolveObject)
    {
        this.sqls = (Sqls) resolveObject;
        super.setResolveObject(resolveObject);
    }
}
