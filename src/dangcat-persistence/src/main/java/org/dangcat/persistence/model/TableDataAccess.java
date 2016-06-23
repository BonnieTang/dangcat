package org.dangcat.persistence.model;

/**
 * Table�������ݷ�������
 * @author dangcat
 * 
 */
class TableDataAccess extends DataAccessBase
{
    private Table table = null;

    public TableDataAccess(Table table)
    {
        this.table = table;
    }

    @Override
    protected Table getTable()
    {
        return this.table;
    }

    @Override
    public int size()
    {
        return this.table.getRows().size();
    }
}
