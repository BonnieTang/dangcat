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
    public void addSettleUnit(SettleUnit settleUnit);

    /**
     * ִ�н��㡣
     */
    public void execute();

    /**
     * ɾ���������
     * @param settleUnit �������
     */
    public void removeSettleUnit(SettleUnit settleUnit);
}
