package org.dangcat.persistence.model;

import org.dangcat.commons.formator.*;
import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.persistence.orm.JdbcValueUtils;
import org.dangcat.persistence.tablename.TableName;
import org.dangcat.persistence.validator.DataValidator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ������λ���Զ���
 * @author dangcat
 * 
 */
public class Column implements java.io.Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    /** ���ݸ�ʽ���� */
    private DataFormator dataFormator = null;
    /** �������͡� */
    private DateType dateType = null;
    /** �ֶγ��ȡ� */
    private int displaySize;
    /** �ֶ����͡� */
    private Class<?> fieldClass;
    /** �ֶ����� */
    private String fieldName;
    /** ��ʽ���ִ��� */
    private String format;
    /** ��ʽ�ṩ���� */
    private FormatProvider formatProvider = null;
    /** �����ֶβ������ԡ� */
    private GenerationType generationType = GenerationType.SEQUENCE;
    /** ��λλ�á� */
    private int index = -1;
    /** �Ƿ�Ϊ�����ֶΡ� */
    private boolean isAssociate = false;
    /** �Ƿ����Ϊ�����ֶΡ� */
    private boolean isAutoIncrement = false;
    /** �Ƿ��Ǽ����ֶΡ� */
    private boolean isCalculate = false;
    /** �Ƿ�Ϊ����� */
    private boolean isForeignKey = false;
    /** �Ƿ����Ϊnull�� */
    private boolean isNullable = true;
    /** �Ƿ�Ϊ������ */
    private boolean isPrimaryKey = false;
    /** �Ƿ�Ϊֻ���� */
    private boolean isReadOnly = false;
    /** �Ƿ�Ϊ�޷��š� */
    private boolean isUnsigned = false;
    /** �߼����͡� */
    private String logic = null;
    /** ��λ���� */
    private String name;
    /** �����б� */
    private Map<String, Object> params = new HashMap<String, Object>();
    /** �Ƿ��������λ�� */
    private boolean rowNum = false;
    /** ���ܶȡ� */
    private int scale = 0;
    /** ���ݿ��������͡� */
    private int sqlType = 0;
    /** �ֶα����� */
    private TableName tableName;
    /** �ֶα��⡣ */
    private String title = null;
    /** ���ݻ���У������ */
    private List<DataValidator> validatorList = null;
    /** �Ƿ�����ڽ�������ʾ�� */
    private boolean visible = true;

    public void addDataValidator(DataValidator dataValidator)
    {
        if (dataValidator != null)
        {
            if (this.validatorList == null)
                this.validatorList = new ArrayList<DataValidator>();
            if (!this.validatorList.contains(dataValidator))
                this.validatorList.add(dataValidator);
        }
    }

    public void assign(org.dangcat.persistence.annotation.Column columnAnnotation)
    {
        ColumnUtils.assign(this, columnAnnotation);
    }

    @Override
    public Object clone()
    {
        Column column = new Column();
        ReflectUtils.copyProperties(this, column);
        column.getParams().clear();
        column.getParams().putAll(this.params);
        return column;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (this.getName() != null && obj != null)
        {
            if (obj instanceof Column)
                return this.getName().equalsIgnoreCase(((Column) obj).getName());
            if (obj instanceof String)
                return this.getName().equalsIgnoreCase((String) obj);
        }
        return super.equals(obj);
    }

    public DataFormator getDataFormator()
    {
        if (this.dataFormator == null && !ValueUtils.isEmpty(this.logic))
            this.dataFormator = DataFormatorFactory.getDataFormator(this.logic);
        if (this.dataFormator == null)
            this.dataFormator = DataFormatorFactory.getDataFormator(this.getFieldClass(), this.getFormat());
        return this.dataFormator;
    }

    public void setDataFormator(DataFormator dataFormator) {
        this.dataFormator = dataFormator;
    }

    public DataValidator[] getDataValidators()
    {
        return this.validatorList == null ? null : this.validatorList.toArray(new DataValidator[0]);
    }

    public DateType getDateType()
    {
        return dateType;
    }

    public void setDateType(DateType dateType) {
        this.dateType = dateType;
    }

    public int getDisplaySize()
    {
        if (this.displaySize == 0)
        {
            if (this.isDoubleType())
                return 20;
        }

        return this.displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public Class<?> getFieldClass()
    {
        return this.fieldClass;
    }

    public void setFieldClass(Class<?> fieldClass) {
        this.fieldClass = fieldClass;
        if (this.isDoubleType())
            this.scale = 5;
        else
            this.scale = 0;
    }

    public String getFieldName()
    {
        return ValueUtils.isEmpty(this.fieldName) ? this.getName() : this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFormat()
    {
        String format = null;
        if (this.getFormatProvider() != null)
            format = this.getFormatProvider().getFormat();
        if (!ValueUtils.isEmpty(format))
            return format;
        return this.format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public FormatProvider getFormatProvider()
    {
        return formatProvider;
    }

    public void setFormatProvider(FormatProvider formatProvider)
    {
        this.formatProvider = formatProvider;
    }

    public GenerationType getGenerationType()
    {
        return generationType;
    }

    public void setGenerationType(GenerationType generationType)
    {
        this.generationType = generationType;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getLogic()
    {
        return logic;
    }

    public void setLogic(String logic)
    {
        this.logic = logic;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Map<String, Object> getParams()
    {
        return params;
    }

    public int getScale()
    {
        return scale;
    }

    public void setScale(int scale)
    {
        if (this.isDoubleType())
            this.scale = scale;
    }

    public int getSqlType()
    {
        if (this.sqlType == Types.NULL)
            this.sqlType = JdbcValueUtils.getSqlType(this.fieldClass);
        return sqlType;
    }

    public void setSqlType(int sqlType)
    {
        this.sqlType = sqlType;
    }

    public TableName getTableName()
    {
        return tableName;
    }

    public void setTableName(TableName tableName)
    {
        this.tableName = tableName;
    }

    public String getTitle()
    {
        if (ValueUtils.isEmpty(this.title))
            return this.getName();
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void initialize()
    {
        ColumnUtils.reset(this);
    }

    public boolean isAssociate()
    {
        return isAssociate;
    }

    public void setAssociate(boolean isAssociate)
    {
        this.isAssociate = isAssociate;
    }

    public boolean isAutoIncrement()
    {
        if (this.isPrimaryKey && Number.class.isAssignableFrom(this.getFieldClass()) && GenerationType.IDENTITY.equals(this.getGenerationType()))
            this.isAutoIncrement = true;
        return this.isAutoIncrement;
    }

    public void setAutoIncrement(boolean isAutoIncrement)
    {
        this.isAutoIncrement = isAutoIncrement;
    }

    public boolean isCalculate()
    {
        return isCalculate;
    }

    public void setCalculate(boolean isCalculate)
    {
        this.isCalculate = isCalculate;
    }

    public boolean isDoubleType()
    {
        return Double.class.equals(this.getFieldClass()) || double.class.equals(this.getFieldClass());
    }

    public boolean isForeignKey()
    {
        return isForeignKey;
    }

    public void setForeignKey(boolean isForeignKey)
    {
        this.isForeignKey = isForeignKey;
    }

    public boolean isIndentityGeneration()
    {
        return this.isAutoIncrement() && GenerationType.IDENTITY.equals(this.getGenerationType());
    }

    public boolean isNullable()
    {
        if (this.isPrimaryKey)
            return false;
        return isNullable;
    }

    public void setNullable(boolean isNullable)
    {
        this.isNullable = isNullable;
    }

    public boolean isPrimaryKey()
    {
        return this.isPrimaryKey;
    }

    public void setPrimaryKey(boolean isPrimaryKey)
    {
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean isReadOnly()
    {
        return this.isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly)
    {
        this.isReadOnly = isReadOnly;
    }

    public boolean isRowNum()
    {
        return rowNum;
    }

    public void setRowNum(boolean rowNum)
    {
        this.rowNum = rowNum;
    }

    public boolean isSequenceGeneration()
    {
        return this.isAutoIncrement() && GenerationType.SEQUENCE.equals(this.getGenerationType());
    }

    public boolean isUnsigned()
    {
        return isUnsigned;
    }

    public void setUnsigned(boolean isUnsigned)
    {
        this.isUnsigned = isUnsigned;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public Object parse(String text)
    {
        DataFormator dataFormator = this.getDataFormator();
        if (dataFormator != null && dataFormator instanceof DateFormator)
            return ((DateFormator) dataFormator).parse(text, null);
        return ValueUtils.parseValue(this.getFieldClass(), text);
    }

    public String toString()
    {
        return ColumnUtils.toString(this);
    }

    public String toString(Object value)
    {
        return ColumnUtils.toString(this, value);
    }
}