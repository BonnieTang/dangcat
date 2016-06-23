package org.dangcat.commons.reflect.examples;

import java.util.Collection;

public class LoadResult<T>
{
    /** ��ǰҳ���¼���� */
    private Collection<T> data = null;
    /** ��ǰҳ��,ȱʡΪ1����һҳΪ1������ֵ������ڵ���1�� */
    private int pageNum = 1;
    /** ÿҳ��ʾ�ļ�¼����ȱʡΪ10�� */
    private int pageSize = 10;
    /** ��ѯ��¼������ */
    private int totalSize;

    public Collection<T> getData()
    {
        return data;
    }

    public int getPageNum()
    {
        return pageNum;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public int getTotalSize()
    {
        return totalSize;
    }

    public void setData(Collection<T> data)
    {
        this.data = data;
    }

    public void setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public void setTotalSize(int totalSize)
    {
        this.totalSize = totalSize;
    }

}
