package org.dangcat.net.ftp.service.impl;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPFile;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.net.ftp.exceptions.FTPSessionException;

/**
 * FTP���ػỰ��
 * @author dangcat
 * 
 */
class FTPDeleteSession extends FTPSessionExecutor
{
    FTPDeleteSession(FTPContext ftpContext, FTPSession ftpSession)
    {
        super(ftpContext, ftpSession);
        ftpContext.setName(FTPContext.OPT_DELETE);
    }

    /**
     * ɾ��Զ���ļ���
     * @param ftpContext FTP�����ġ�
     * @param remotePath Զ��·����
     * @throws IOException
     */
    private void delete(String remotePath) throws IOException
    {
        FTPContext ftpContext = this.getFtpContext();
        FTPClient ftpClient = ftpContext.getFtpClient();
        FTPFile[] FTPFiles = this.listFTPFiles(remotePath);
        if (FTPFiles != null)
        {
            for (FTPFile ftpFile : FTPFiles)
            {
                if (this.isRootPath(ftpFile))
                    continue;

                String ftpFileName = this.getFTPFileName(remotePath, ftpFile.getName());
                if (ftpFile.isFile())
                {
                    if (remotePath.equalsIgnoreCase(ftpFile.getName()) || remotePath.endsWith(FILE_SEPARATOR + ftpFile.getName()))
                        ftpFileName = this.getFTPFileName(remotePath, null);
                    if (ftpClient.deleteFile(ftpFileName))
                        ftpContext.increaseTotalCount();
                }
                else if (ftpFile.isDirectory())
                {
                    this.delete(remotePath + FILE_SEPARATOR + ftpFile.getName());
                    if (ftpClient.removeDirectory(ftpFileName))
                        ftpContext.increaseTotalCount();
                }
            }
        }
        if (!ValueUtils.isEmpty(remotePath) && !remotePath.equals("/") && !remotePath.equals(".") && !remotePath.equals(".."))
        {
            if (ftpClient.removeDirectory(this.getFTPFileName(remotePath, null)))
                ftpContext.increaseTotalCount();
        }
    }

    @Override
    protected void innerExecute() throws FTPSessionException, IOException
    {
        this.delete(this.getRemoteRootPath());
    }
}
