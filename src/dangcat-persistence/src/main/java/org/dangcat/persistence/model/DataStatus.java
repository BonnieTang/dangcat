package org.dangcat.persistence.model;

/**
 * ʵ������״̬��
 * @author dangcat
 * 
 */
public interface DataStatus
{
    /**
     * ��ȡ״̬��
     * @return
     */
    public DataState getDataState();

    /**
     * ����״̬��
     * @param dataState
     */
    public void setDataState(DataState dataState);
}
