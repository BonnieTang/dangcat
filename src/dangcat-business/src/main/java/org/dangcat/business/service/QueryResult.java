package org.dangcat.business.service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ִ�н��
 * @author dangcat
 * 
 */
public class QueryResult<T>
{
    /** ���ݼ��ϡ� */
    private Collection<T> dataCollection = new ArrayList<T>();
    /** ��ʼλ�á� */
    private Integer startRow = null;
    /** �ܼ�¼���� */
    private Integer totalSize = null;

    public Collection<T> getData()
    {
        return this.dataCollection;
    }

    public Integer getStartRow()
    {
        return startRow;
    }

    public void setStartRow(Integer startRow)
    {
        this.startRow = startRow;
    }

    public Integer getTotalSize()
    {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize)
    {
        this.totalSize = totalSize;
    }
}
