package org.dangcat.commons.serialize.json;

/**
 * ʵ������״̬��
 * @author dangcat
 * 
 */
interface DataStatus
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
