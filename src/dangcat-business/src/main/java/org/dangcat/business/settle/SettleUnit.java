package org.dangcat.business.settle;

import org.dangcat.persistence.tablename.DateTimeTableName;

/**
 * ���㵥Ԫ��
 * @author dangcat
 * 
 */
public interface SettleUnit
{
    /**
     * �������͡�
     */
    public Class<? extends SettleEntity> getClassType();

    /**
     * ��Դ���塣
     */
    public DateTimeTableName getSourceTableName();

    /**
     * �ϲ����ݡ�
     * @param srcEntity ��Դ���ݡ�
     * @param destEntity Ŀ�����ݡ�
     */
    public void merge(Object srcEntity, Object destEntity);
}
