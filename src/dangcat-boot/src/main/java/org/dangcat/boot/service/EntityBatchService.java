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
    public void clear();

    /**
     * �õ�ָ�����ݿ�������洢��
     * @return ������������
     */
    public EntityBatchStorer getEntityBatchStorer();

    /**
     * �õ�ָ�����ݿ�������洢��
     * @param databaseName ���ݿ�����
     * @return ������������
     */
    public EntityBatchStorer getEntityBatchStorer(String databaseName);

    /**
     * ���������洢������
     */
    public void save();
}
