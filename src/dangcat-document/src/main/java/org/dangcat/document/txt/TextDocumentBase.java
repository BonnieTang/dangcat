package org.dangcat.document.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.dangcat.document.DocumentBase;
import org.dangcat.persistence.DataWriter;

/**
 * �ĵ����������
 * @author dangcat
 * 
 */
public abstract class TextDocumentBase extends DocumentBase
{
    private boolean firstHeader = true;
    private PrintWriter printWriter = null;

    @Override
    public void close()
    {
        if (this.printWriter != null)
        {
            this.printWriter.close();
            this.printWriter = null;
        }
        super.close();
    }

    protected PrintWriter getPrintWriter()
    {
        if (this.printWriter == null && this.getOutputStream() != null)
            this.printWriter = new PrintWriter(this.getOutputStream());
        return this.printWriter;
    }

    public boolean isFirstHeader()
    {
        return this.firstHeader;
    }

    /**
     * ���ļ��������ݡ�
     * @param dataWriter ��������ӿڡ�
     * @return �����������
     */
    @Override
    public int read(DataWriter dataWriter)
    {
        return 0;
    }

    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param dataWriter ��������ӿڡ�
     * @return �����������
     */
    @Override
    public int read(File file, DataWriter dataWriter)
    {
        BufferedReader bufferedReader = null;
        int result = 0;
        try
        {
            bufferedReader = new BufferedReader(new FileReader(file));
            result = this.read(bufferedReader, dataWriter);
        }
        catch (Exception e)
        {
            this.logger.error(file, e);
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return result;
    }

    /**
     * �ӻ������������ݡ�
     * @param bufferedReader ���ݻ�������
     * @param dataWriter ��������ӿڡ�
     * @return �����������
     * @throws IOException �쳣��
     */
    public abstract int read(Reader reader, DataWriter dataWriter) throws IOException;

    public void setFirstHeader(boolean firstHeader)
    {
        this.firstHeader = firstHeader;
    }
}
