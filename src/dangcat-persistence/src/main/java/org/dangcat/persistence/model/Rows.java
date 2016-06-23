package org.dangcat.persistence.model;

import org.dangcat.persistence.event.TableEventAdapter;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.index.IndexManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rows extends ArrayList<Row> implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    /** ���ݱ������״̬����ѯ���޸ġ� */
    private DataState dataState = DataState.Browse;
    /** �Ѿ�ɾ���������С� */
    private List<Row> deletedRows = new ArrayList<Row>();
    /** �������� */
    private IndexManager<Row> indexManager = new IndexManager<Row>(this);
    /** �������ݱ���� */
    private Table parent;

    public Rows()
    {
    }

    public Rows(Table parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean add(Row row)
    {
        for (TableEventAdapter tableEventAdapter : this.getParent().getTableEventAdapterList())
        {
            if (!tableEventAdapter.beforeRowLoad(row))
                return false;
        }

        row.setParent(this.parent);
        if (this.parent.getTableState() == TableState.Loading)
            row.setDataState(DataState.Browse);
        else
        {
            row.setDataState(DataState.Insert);
            this.setDataState(DataState.Modified);
        }
        boolean result = super.add(row);
        this.indexManager.add(row);
        for (TableEventAdapter tableEventAdapter : this.getParent().getTableEventAdapterList())
            tableEventAdapter.afterRowLoad(row);
        return result;
    }

    public void append(Rows rows)
    {
        for (Row row : rows)
            this.add(row);

        rows.clear();
    }

    @Override
    public void clear()
    {
        this.release(this);
        super.clear();

        this.setDataState(DataState.Browse);
    }

    /**
     * �����µ������С�
     * @return �µ��С�
     */
    public Row createNewRow()
    {
        Row row = Row.newInstance();
        row.setParent(this.parent);
        for (int i = 0; i < this.getParent().getColumns().size(); i++)
        {
            Field field = Field.newInstance();
            field.setParent(row);
            field.setObject(null);
            row.add(field);
        }
        for (TableEventAdapter tableEventAdapter : this.getParent().getTableEventAdapterList())
            tableEventAdapter.onCreateNewRow(row);
        return row;
    }

    /**
     * ����ָ���Ĺ��������ҵ����������������С�
     * @param filterExpress ��������
     * @return
     */
    public Collection<Row> find(FilterExpress filterExpress)
    {
        return this.indexManager.find(filterExpress);
    }

    /**
     * ��������ֵ�ҵ���¼�С�
     * @param params ��������ֵ��
     * @return �ҵ��������С�
     */
    public Row find(Object... params)
    {
        return this.indexManager.find(params);
    }

    /**
     * ָ����λ����ֵ���ҵ��������������������С�
     * @param fieldNames �ֶ���������ֶ��Էֺż����
     * @param params ����ֵ��
     * @return �ҵ��������м��ϡ�
     */
    public Collection<Row> find(String[] fieldNames, Object... params)
    {
        return this.indexManager.find(fieldNames, params);
    }

    public DataState getDataState()
    {
        return dataState;
    }

    public void setDataState(DataState dataState) {
        this.dataState = dataState;
        if (dataState == DataState.Browse || dataState == DataState.Insert) {
            for (Row row : this)
                row.setDataState(dataState);

            this.release(this.getDeletedRows());
            this.getDeletedRows().clear();
        }
    }

    /**
     * ȡ���Ѿ�ɾ�����������б�
     */
    public List<Row> getDeletedRows()
    {
        return deletedRows;
    }

    public IndexManager<Row> getIndexManager()
    {
        return indexManager;
    }

    /**
     * ȡ���������������б�
     */
    public List<Row> getInsertedRows()
    {
        List<Row> rowList = new ArrayList<Row>();
        for (Row row : this)
        {
            if (rowList != null)
            {
                if (row.getDataState() == DataState.Insert)
                    rowList.add(row);
            }
        }
        return rowList;
    }

    /**
     * ȡ���������������б�
     */
    public List<Row> getModifiedRows()
    {
        List<Row> rowList = new ArrayList<Row>();
        for (Row row : this)
        {
            if (rowList != null)
            {
                if (row.getDataState() == DataState.Modified)
                    rowList.add(row);
            }
        }
        return rowList;
    }

    public Table getParent()
    {
        return parent;
    }

    private boolean isChildRow(Row row)
    {
        Table parent = row.getParent();
        return parent != null && parent == this.getParent();
    }

    /**
     * ֪ͨ������״̬�����仯��
     * @param row ״̬�����仯�������С�
     * @param field ���޸ĵ���λ��
     */
    protected void notify(Row row, Field field)
    {
        if (row != null)
        {
            if (this.dataState == DataState.Browse && this.parent.getTableState() != TableState.Loading)
            {
                if (row.getDataState() == DataState.Modified || row.getDataState() == DataState.Insert || row.getDataState() == DataState.Deleted)
                    this.dataState = DataState.Modified;
                for (TableEventAdapter tableEventAdapter : this.getParent().getTableEventAdapterList())
                    tableEventAdapter.onRowStateChanged(row);
            }
            this.indexManager.update(row.getFieldName(field), row);
        }
    }

    private void release(List<Row> rows)
    {
        Row[] rowArray = rows.toArray(new Row[0]);
        for (Row row : rowArray)
        {
            if (this.isChildRow(row))
                row.release();
            rows.remove(row);
        }
    }

    @Override
    public Row remove(int index)
    {
        Row row = null;
        if (index != -1)
        {
            for (TableEventAdapter tableEventAdapter : this.getParent().getTableEventAdapterList())
            {
                if (!tableEventAdapter.beforeRowRemove(row))
                    return null;
            }

            row = super.remove(index);
            this.indexManager.remove(row);
            if (this.isChildRow(row) && row.getDataState() != DataState.Insert)
            {
                row.setDataState(DataState.Deleted);
                this.deletedRows.add(row);
                this.notify(row, null);
            }

            for (TableEventAdapter tableEventAdapter : this.getParent().getTableEventAdapterList())
                tableEventAdapter.afterRowRemove(row);
        }
        return row;
    }

    public boolean remove(Row row)
    {
        int index = this.indexOf(row);
        if (index != -1)
        {
            this.remove(index);
            return true;
        }
        return false;
    }

    /**
     * ɾ�����е������С�
     */
    public void removeAll()
    {
        this.removeRange(0, this.size());
    }

    @Override
    public void removeRange(int fromIndex, int toIndex)
    {
        Row[] deleteRows = this.subList(fromIndex, toIndex).toArray(new Row[0]);
        if (deleteRows != null)
        {
            for (Row deleteRow : deleteRows)
                this.remove(deleteRow);
        }
    }

    /**
     * ����ֶ����ݡ�
     */
    public String toString()
    {
        StringBuffer info = new StringBuffer();
        info.append("Row Size: " + this.size() + "\n");
        // �����ͷ
        for (Column column : this.getParent().getColumns())
        {
            info.append(column.getName());
            if (column.isPrimaryKey())
                info.append("(true)");
            info.append("\t");
        }
        info.append("\n");
        for (Row row : this)
            info.append(row + "\n");
        return info.toString();
    }
}
