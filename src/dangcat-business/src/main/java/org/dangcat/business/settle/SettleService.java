package org.dangcat.business.settle;

/**
 * �������
 * @author dangcat
 * 
 */
public interface SettleService
{
    /**
     * ��ӽ������
     * @param SettleUnit �������
     */
    void addSettleUnit(SettleUnit settleUnit);

    /**
     * ִ�н��㡣
     */
    void execute();

    /**
     * ɾ���������
     * @param settleUnit �������
     */
    void removeSettleUnit(SettleUnit settleUnit);
}
