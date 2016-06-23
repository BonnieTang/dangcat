package org.dangcat.net.ftp.service;

import org.dangcat.net.ftp.service.impl.FTPContext;

import java.io.File;

/**
 * ���ػص��ӿڡ�
 * @author dangcat
 * 
 */
public interface FTPCallBack
{
    /**
     * ����ʧ�ܡ�
     * @param ftpContext FTP�Ự��
     * @param localPath ����·����
     * @param remotePath Զ��·����
     * @param exception �����쳣��
     */
    void onFailure(FTPContext ftpContext, File localPath, String remotePath, Exception exception);

    /**
     * ���سɹ���
     * @param ftpContext FTP�Ự��
     * @param localPath ����·����
     * @param remotePath Զ��·����
     */
    void onSucess(FTPContext ftpContext, File localPath, String remotePath);
}
