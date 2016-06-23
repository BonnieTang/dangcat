package org.dangcat.persistence.model;

import org.dangcat.persistence.exception.TableException;

/**
 * ���������
 * @author dangcat
 * 
 */
public interface TableManager
{
    /**
     * �ڵ�ǰ������Դ�й������ݱ�
     * @param table �����
     * @return ��������
     * @throws TableException �����쳣��
     */
    int create(Table table) throws TableException;

    /**
     * ɾ��ָ��������ݡ�
     * @param table �����
     * @return ɾ��������
     * @throws TableException �����쳣��
     */
    int delete(Table table) throws TableException;

    /**
     * ɾ��ָ���ı�
     * @param tableName ������
     * @return ɾ�������
     * @throws TableException �����쳣��
     */
    int drop(String tableName) throws TableException;

    /**
     * ɾ��ָ���ı�
     * @param table �����
     * @return ɾ�������
     * @throws TableException �����쳣��
     */
    int drop(Table table) throws TableException;

    /**
     * ִ�б��SQL��䡣
     * @param table �����
     * @return ִ�н����
     * @throws TableException �����쳣��
     */
    int execute(Table table) throws TableException;

    /**
     * �жϱ��Ƿ���ڡ�
     * @param tableName ������
     * @return �Ƿ���ڡ�
     */
    boolean exists(String tableName);

    /**
     * �жϱ��Ƿ���ڡ�
     * @param table �����
     * @return �Ƿ���ڡ�
     */
    boolean exists(Table table);

    /**
     * ����ָ��������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    void load(Table table) throws TableException;

    /**
     * ����Ԫ�������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    void loadMetaData(Table table) throws TableException;

    /**
     * �洢ָ��������ݡ�
     * @param table �����
     * @throws TableException �����쳣��
     */
    void save(Table table) throws TableException;

    /**
     * ���ָ�������ݡ�
     * @param tableName �����ơ�
     * @return ��������
     * @throws TableException �����쳣��
     */
    int truncate(String tableName) throws TableException;

    /**
     * ���ָ�������ݡ�
     * @param table �����
     * @return ��������
     * @throws TableException �����쳣��
     */
    int truncate(Table table) throws TableException;

}
