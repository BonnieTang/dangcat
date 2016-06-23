package org.dangcat.persistence.syntax;

/**
 * ���ʽ��������
 * @author dangcat
 * 
 */
public class HsqldbSyntaxAdapter extends StandSqlSyntaxHelper
{
    @Override
    protected String createCreateStatement(String tableName)
    {
        String statement = "CREATE ";
        if (this.isUseCachedTable())
            statement += "CACHED ";
        return statement + "TABLE " + tableName;
    }

    private boolean isUseCachedTable()
    {
        return this.containsParam("useCachedTable", "true");
    }
}
