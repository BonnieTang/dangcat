package org.dangcat.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.dangcat.commons.io.FileUtils;
import org.dangcat.persistence.DataReader;
import org.dangcat.persistence.DataWriter;

/**
 * �ĵ����������
 * @author dangcat
 * 
 */
public abstract class DocumentBase
{
    private InputStream inputStream = null;
    protected int lineCount = 0;
    protected final Logger logger = Logger.getLogger(this.getClass());
    private OutputStream outputStream = null;

    public void close()
    {
        this.lineCount = 0;
        try
        {
            if (this.outputStream != null)
            {
                this.outputStream.close();
                this.outputStream = null;
            }
        }
        catch (IOException e)
        {
        }
        this.inputStream = FileUtils.close(this.inputStream);
    }

    public InputStream getInputStream()
    {
        return this.inputStream;
    }

    public int getLineCount()
    {
        return this.lineCount;
    }

    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }

    /**
     * ���ļ��������ݡ�
     * @param dataWriter ��������ӿڡ�
     * @return �����������
     */
    public abstract int read(DataWriter dataWriter);

    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param dataWriter ��������ӿڡ�
     * @return �����������
     */
    public int read(File file, DataWriter dataWriter)
    {
        int result = 0;
        try
        {
            this.inputStream = new FileInputStream(file);
            result = this.read(dataWriter);
        }
        catch (FileNotFoundException e)
        {
            this.logger.error(file, e);
        }
        finally
        {
            this.close();
        }
        return result;
    }

    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public void setOutputStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
    }

    /**
     * ����ʵ��������ݵ���������
     * @param outputStream �������
     * @param dataReader ������Դ��
     * @param ���������
     */
    public abstract int write(DataReader dataReader);

    /**
     * ����ʵ��������ݵ���������
     * @param file ����ļ���
     * @param dataReader ��Դ���ݡ�
     * @param exportHeader �Ƿ����ͷ��
     */
    public int write(File file, DataReader dataReader)
    {
        int result = 0;
        try
        {
            this.outputStream = new FileOutputStream(file);
            result = this.write(dataReader);
        }
        catch (FileNotFoundException e)
        {
            this.logger.error(file, e);
        }
        finally
        {
            this.close();
        }
        return result;
    }
}
