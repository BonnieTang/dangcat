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
    Class<? extends SettleEntity> getClassType();

    /**
     * ��Դ���塣
     */
    DateTimeTableName getSourceTableName();

    /**
     * �ϲ����ݡ�
     * @param srcEntity ��Դ���ݡ�
     * @param destEntity Ŀ�����ݡ�
     */
    void merge(Object srcEntity, Object destEntity);
}
