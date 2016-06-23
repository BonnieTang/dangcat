package org.dangcat.boot.service;

import org.dangcat.commons.io.FileWriter;

/**
 * �ļ�д�����
 * @author dangcat
 * 
 */
public interface FileWriteService
{
    /**
     * ����ļ�д�����
     * @param fileWriter �ļ�д�����
     */
    public void addFileWriter(FileWriter fileWriter);

    /**
     * ɾ���ļ�д�����
     * @param fileWriter �ļ�д�����
     */
    public void removeFileWriter(FileWriter fileWriter);
}
