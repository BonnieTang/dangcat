package org.dangcat.commons.io;

import java.io.*;

/**
 * �ļ�������
 */
class FileCloser
{
    /**
     * �ر���������
     * @param inputStream ��������
     * @return �رպ�Ľ����
     */
    protected static InputStream close(InputStream inputStream)
    {
        if (inputStream != null)
        {
            try
            {
                inputStream.close();
                inputStream = null;
            }
            catch (IOException e)
            {
            }
        }
        return inputStream;
    }

    /**
     * �ر��������
     * @param outputStream �������
     * @return �رպ�Ľ����
     */
    protected static OutputStream close(OutputStream outputStream)
    {
        if (outputStream != null)
        {
            try
            {
                outputStream.close();
                outputStream = null;
            }
            catch (IOException e)
            {
            }
        }
        return outputStream;
    }

    protected static Reader close(Reader reader)
    {
        if (reader != null)
        {
            try
            {
                reader.close();
                reader = null;
            }
            catch (IOException e)
            {
            }
        }
        return reader;
    }

    protected static Writer close(Writer writer)
    {
        if (writer != null)
        {
            try
            {
                writer.close();
                writer = null;
            }
            catch (IOException e)
            {
            }
        }
        return writer;
    }

}
