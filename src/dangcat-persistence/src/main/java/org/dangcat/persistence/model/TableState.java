package org.dangcat.persistence.model;

/**
 * ���ݱ�״̬��
 *
 * @author dangcat
 */
public enum TableState {
    /**
     * ����ȫ��Ϊ����״̬��
     */
    Insert,
    /**
     * �������롣
     */
    Loading,
    /**
     * ������
     */
    Normal,
    /**
     * ���ڴ浵��
     */
    Saving
}
