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
    public DataState getDataState();

    /**
     * ����״̬��
     * @param dataState
     */
    public void setDataState(DataState dataState);
}
