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
    DataState getDataState();

    /**
     * ����״̬��
     * @param dataState
     */
    void setDataState(DataState dataState);
}
