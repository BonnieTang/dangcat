package org.dangcat.boot.service;

import org.dangcat.persistence.batch.EntityBatchStorer;

/**
 * ����������������
 * @author dangcat
 * 
 */
public interface EntityBatchService
{
    /**
     * ������д��������ݡ�
     */
    void clear();

    /**
     * �õ�ָ�����ݿ�������洢��
     * @return ������������
     */
    EntityBatchStorer getEntityBatchStorer();

    /**
     * �õ�ָ�����ݿ�������洢��
     * @param databaseName ���ݿ�����
     * @return ������������
     */
    EntityBatchStorer getEntityBatchStorer(String databaseName);

    /**
     * ���������洢������
     */
    void save();
}
