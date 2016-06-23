package org.dangcat.persistence.model;

import org.dangcat.commons.database.DatabaseType;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.calculate.CalculatorImpl;
import org.dangcat.persistence.event.TableEventAdapter;
import org.dangcat.persistence.exception.TableException;
import org.dangcat.persistence.filter.FilterExpress;
import org.dangcat.persistence.orderby.OrderBy;
import org.dangcat.persistence.orm.SqlBuilder;
import org.dangcat.persistence.sql.Sqls;
import org.dangcat.persistence.tablename.TableName;
import org.dangcat.persistence.validator.TableDataValidator;
import org.dangcat.persistence.validator.exception.DataValidateException;

import java.util.*;

/**
 * ���ݱ����
 * @author dangcat
 * 
 */
public class Table implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    /** ���������á� */
    private CalculatorImpl calculators = new CalculatorImpl();
    /** ��λ���ϡ� */
    private Columns columns;
    /** �������ݿ⡣ */
    private String databaseName;
    /** ���˱��ʽ�� */
    private FilterExpress filter;
    /** �̶����˱��ʽ�� */
    private FilterExpress fixFilter;
    /** �����б� */
    private Collection<OrderBy> indexes = new HashSet<OrderBy>();
    /** ���ݱ�������� */
    private String name;
    /** �����ֶΣ�����ֶ��Էֺŷָ��� */
    private OrderBy orderBy;
    /** �����б� */
    private Map<String, Object> params = new HashMap<String, Object>();
    /** �������ݷ�Χ�� */
    private Range range;
    /** �����С� */
    private Rows rows;
    /** ��ѯ���ʽ�� */
    private SqlBuilder sqlBuilder;
    /** ��ǰSQL�ű����� */
    private String sqlName;
    /** ��ѯ������ü��ϡ� */
    private Sqls sqls = new Sqls();
    /** �¼����伯�ϡ� */
    private List<TableEventAdapter> tableEventAdapterList = new LinkedList<TableEventAdapter>();
    /** ��������� */
    private TableManager tableManager = new TableManagerImpl();
    /** ���ݱ����� */
    private TableName tableName;
    /** ���ݱ�״̬�� */
    private TableState tableState = TableState.Normal;
    /** ���ݼ����С� */
    private Row total;

    public Table()
    {
        this.rows = new Rows(this);
        this.columns = new Columns(this);
    }

    public Table(String name)
    {
        this();
        this.name = name;
    }

    public Table(TableName tableName)
    {
        this();
        this.tableName = tableName;
    }

    public void addTableEventAdapter(TableEventAdapter tableEventAdapter)
    {
        if (tableEventAdapter != null && !this.tableEventAdapterList.contains(tableEventAdapter))
            this.tableEventAdapterList.add(tableEventAdapter);
    }

    public void calculate()
    {
        this.calculateRowNum();
        this.getCalculators().calculate(this);
    }

    public void calculateRowNum()
    {
        Column rowNumColumn = this.getColumns().getRowNumColumn();
        if (rowNumColumn != null)
        {
            Integer startRow = null;
            if (this.getRange() != null)
                startRow = this.getRange().getFrom();
            TableUtils.calculateRowNum(this.getRows(), startRow);
        }
    }

    public void calculateTotal() throws TableException
    {
        TableUtils.calculateTotal(this);
    }

    @Override
    public Object clone()
    {
        Table cloneTable = new Table(this.getName());
        cloneTable.getIndexes().addAll(this.getIndexes());
        cloneTable.setDatabaseName(this.getDatabaseName());
        for (Column column : this.getColumns())
            cloneTable.getColumns().add((Column) column.clone());
        return cloneTable;
    }

    /**
     * �ڵ�ǰ������Դ�й������ݱ�
     */
    public int create() throws TableException
    {
        return this.tableManager.create(this);
    }

    private void decorateSqlBuilder(SqlBuilder sqlBuilder)
    {
        sqlBuilder.setFilter(this.getFilterSql());
        sqlBuilder.setTableName(this.getTableName().getName());
        sqlBuilder.setParams(this.getParams());
    }

    public int delete() throws TableException
    {
        return this.tableManager.delete(this);
    }

    public int drop() throws TableException
    {
        return this.tableManager.drop(this);
    }

    public int execute() throws TableException
    {
        return this.tableManager.execute(this);
    }

    public boolean exists()
    {
        return this.tableManager.exists(this);
    }

    public CalculatorImpl getCalculators()
    {
        return this.calculators;
    }

    public Columns getColumns()
    {
        return this.columns;
    }

    public String getDatabaseName()
    {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public DataState getDataState()
    {
        return this.rows.getDataState();
    }

    public FilterExpress getFilter()
    {
        return this.filter;
    }

    public void setFilter(FilterExpress filter) {
        this.filter = filter;
    }

    public String getFilterSql()
    {
        // ����������
        StringBuilder sqlFilter = new StringBuilder();
        if (this.getFilter() != null)
        {
            sqlFilter.append(" AND ");
            sqlFilter.append(this.getFilter());
        }
        // �̶�����������
        if (this.getFixFilter() != null)
        {
            sqlFilter.append(" AND ");
            sqlFilter.append(this.getFixFilter());
        }
        return sqlFilter.toString();
    }

    public FilterExpress getFixFilter()
    {
        return this.fixFilter;
    }

    public void setFixFilter(FilterExpress fixFilter) {
        this.fixFilter = fixFilter;
    }

    public Collection<OrderBy> getIndexes()
    {
        return this.indexes;
    }

    public String getName()
    {
        if (this.name == null && this.tableName != null)
            return this.tableName.getName();
        return this.name;
    }

    public void setName(String name) {
        if (this.tableName != null && name != null && !name.equals(this.tableName.toString()))
            this.tableName = null;
        this.name = name;
    }

    public OrderBy getOrderBy()
    {
        return this.orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Map<String, Object> getParams()
    {
        return this.params;
    }

    public Range getRange()
    {
        return this.range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Rows getRows()
    {
        return this.rows;
    }

    public SqlBuilder getSql()
    {
        if (this.sqlBuilder == null)
            this.sqlBuilder = new SqlBuilder();
        this.decorateSqlBuilder(this.sqlBuilder);
        return this.sqlBuilder;
    }

    public SqlBuilder getSql(DatabaseType databaseType, String name)
    {
        SqlBuilder sqlBuilder = null;
        if (!ValueUtils.isEmpty(name))
        {
            sqlBuilder = this.getSqls().find(databaseType, name);
            if (sqlBuilder != null)
                this.decorateSqlBuilder(sqlBuilder);
        }
        else
            sqlBuilder = this.getSql();
        return sqlBuilder;
    }

    public String getSqlName()
    {
        return this.sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public Sqls getSqls()
    {
        return this.sqls;
    }

    public List<TableEventAdapter> getTableEventAdapterList()
    {
        return this.tableEventAdapterList;
    }

    public TableName getTableName()
    {
        if (this.tableName == null)
            this.tableName = new TableName(this.name);
        return this.tableName;
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    public TableState getTableState()
    {
        return this.tableState;
    }

    public void setTableState(TableState tableState) {
        if (tableState == TableState.Insert) // ����ȫ��Ϊ����״̬��
            this.getRows().setDataState(DataState.Insert);
        else
            this.tableState = tableState;
        for (TableEventAdapter tableEventAdapter : this.tableEventAdapterList)
            tableEventAdapter.onTableStateChanged(this);
    }

    public Row getTotal()
    {
        return this.total;
    }

    public void setTotal(Row row) {
        if (row != null)
            row.setParent(this);
        this.total = row;
        if (row != null) {
            for (TableEventAdapter tableEventAdapter : this.tableEventAdapterList)
                tableEventAdapter.onCalculateTotal(row);
        }
    }

    public void load() throws TableException
    {
        this.tableManager.load(this);
    }

    public void loadMetaData() throws TableException
    {
        this.tableManager.loadMetaData(this);
    }

    public void removeTableEventAdapter(TableEventAdapter tableEventAdapter)
    {
        if (tableEventAdapter != null && this.tableEventAdapterList.contains(tableEventAdapter))
            this.tableEventAdapterList.remove(tableEventAdapter);
    }

    public void save() throws TableException
    {
        this.tableManager.save(this);
    }

    @Override
    public String toString()
    {
        // ��ӡTable������
        StringBuffer info = new StringBuffer();
        info.append("Name : " + this.getName() + " \n");
        // �����λ��Ϣ��
        if (this.getRows().size() == 0)
            info.append(this.getColumns());
        // �����������Ϣ��
        info.append(this.getRows());
        return info.toString();
    }

    public int truncate() throws TableException
    {
        return this.tableManager.truncate(this);
    }

    public void validate() throws DataValidateException
    {
        TableDataValidator.validate(this);
    }
}