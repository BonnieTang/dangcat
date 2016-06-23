package org.dangcat.net.ftp.service;

import java.io.File;

import org.dangcat.net.ftp.service.impl.FTPContext;

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
    public void onFailure(FTPContext ftpContext, File localPath, String remotePath, Exception exception);

    /**
     * ���سɹ���
     * @param ftpContext FTP�Ự��
     * @param localPath ����·����
     * @param remotePath Զ��·����
     */
    public void onSucess(FTPContext ftpContext, File localPath, String remotePath);
}
