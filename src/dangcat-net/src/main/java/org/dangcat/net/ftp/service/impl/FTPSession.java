package org.dangcat.net.ftp.service.impl;

import org.apache.commons.net.ftp.FTPFile;
import org.dangcat.boot.service.impl.WatchThreadExecutor;
import org.dangcat.net.ftp.exceptions.FTPSessionException;
import org.dangcat.net.ftp.service.FTPCallBack;

import java.io.File;

/**
 * FTP�Ự��
 * @author dangcat
 * 
 */
public class FTPSession
{
    private Boolean continueLoad = null;
    private String controlEncoding = null;
    private FTPClientPool ftpClientPool = null;

    public FTPSession(FTPClientPool ftpClientPool)
    {
        this.ftpClientPool = ftpClientPool;
    }

    /**
     * ɾ��Զ���ļ���
     * @param ftpContext FTP�����ġ�
     * @throws FTPSessionException Զ�̲����쳣��
     */
    public void delete(FTPContext ftpContext) throws FTPSessionException
    {
        this.execute(new FTPDeleteSession(ftpContext, this));
    }

    /**
     * ɾ��Զ���ļ���
     * @param remotePath Զ��·����
     * @throws FTPSessionException
     */
    public void delete(String remotePath) throws FTPSessionException
    {
        this.delete(new FTPContext(remotePath));
    }

    /**
     * �����ļ���
     * @param localPath �����ļ�·����
     * @param remotePath Զ��·����
     * @param ftpCallBack �ص��ӿڡ�
     * @throws FTPSessionException
     */
    public void download(File localPath, String remotePath, FTPCallBack ftpCallBack) throws FTPSessionException
    {
        this.download(new FTPContext(localPath, remotePath, ftpCallBack));
    }

    /**
     * �����ļ���
     * @param ftpContext FTP�����ġ�
     * @throws FTPSessionException �����쳣��
     */
    public void download(FTPContext ftpContext) throws FTPSessionException
    {
        this.execute(new FTPDownloadSession(ftpContext, this));
    }

    private void execute(FTPSessionExecutor ftpSessionExecutor) throws FTPSessionException
    {
        try
        {
            ftpSessionExecutor.beginSubmit();
            WatchThreadExecutor.getInstance().submit(ftpSessionExecutor);
        }
        finally
        {
            ftpSessionExecutor.endSubmit();
        }
    }

    public String getControlEncoding()
    {
        if (this.controlEncoding == null)
            return this.ftpClientPool.getControlEncoding();
        return this.controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

    protected FTPClientPool getFtpClientPool()
    {
        return this.ftpClientPool;
    }

    public Boolean isContinueLoad()
    {
        if (this.continueLoad == null)
            return this.ftpClientPool.isContinueLoad();
        return this.continueLoad;
    }

    /**
     * �г�Զ���ļ�����
     * @param ftpContext FTP�����ġ�
     * @throws FTPSessionException
     */
    public FTPFile[] list(FTPContext ftpContext) throws FTPSessionException
    {
        FTPListSession ftpListSession = new FTPListSession(ftpContext, this);
        this.execute(ftpListSession);
        return ftpListSession.getFtpFiles();
    }

    /**
     * ö��Զ��·�������ݡ�
     * @param remotePath Զ��·����
     * @return
     * @throws FTPSessionException Զ�̲����쳣��
     */
    public FTPFile[] list(String remotePath) throws FTPSessionException
    {
        return this.list(new FTPContext(remotePath));
    }

    /**
     * ����������
     * @param from ��Դ�ļ���
     * @param to Ŀ���ļ���
     */
    public boolean rename(String from, String to)
    {
        FTPRenameSession ftpRenameSession = new FTPRenameSession(new FTPContext(from), this);
        try
        {
            ftpRenameSession.setFrom(from);
            ftpRenameSession.setTo(to);
            this.execute(ftpRenameSession);
        }
        catch (FTPSessionException e)
        {
        }
        return ftpRenameSession.isSuccess();
    }

    public void setContinueLoad(Boolean continueLoad)
    {
        this.continueLoad = continueLoad;
    }

    /**
     * �����Ƿ�����������ӵ���������
     */
    public FTPSessionException testConnect()
    {
        FTPSessionExecutor ftpSessionExecutor = new FTPTestConnectionSession(new FTPContext(""), this);
        ftpSessionExecutor.run();
        return ftpSessionExecutor.getException();
    }

    /**
     * �ϴ��ļ���Ŀ¼��
     * @param localPath ����·����
     * @param remotePath Զ��·����
     * @param ftpCallBack �ص��ӿڡ�
     * @throws FTPSessionException
     */
    public void upload(File localPath, String remotePath, FTPCallBack ftpCallBack) throws FTPSessionException
    {
        this.upload(new FTPContext(localPath, remotePath, ftpCallBack));
    }

    /**
     * �ϴ��ļ���Ŀ¼��
     * @param ftpContext FTP�����ġ�
     * @throws FTPSessionException
     */
    public void upload(FTPContext ftpContext) throws FTPSessionException
    {
        this.execute(new FTPUploadSession(ftpContext, this));
    }
}