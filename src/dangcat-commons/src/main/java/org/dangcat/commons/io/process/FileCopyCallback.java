package org.dangcat.commons.io.process;

import java.io.File;

/**
 * �ļ������ص��ӿڡ�
 */
public interface FileCopyCallback {
    /**
     * ������ص��ӿڡ�
     *
     * @param source ��Դ�ļ���
     * @param dest   Ŀ���ļ���
     */
    void afterCopy(File source, File dest);

    /**
     * ����ǰ�ص��ӿڡ�
     *
     * @param source ��Դ�ļ���
     * @param dest   Ŀ���ļ���
     * @return ���ȡ������false�����򷵻�true��
     */
    boolean beforeCopy(File source, File dest);
}
