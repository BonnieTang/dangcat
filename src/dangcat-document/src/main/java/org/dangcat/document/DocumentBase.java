package org.dangcat.document;

import org.apache.log4j.Logger;
import org.dangcat.commons.io.FileUtils;
import org.dangcat.persistence.DataReader;
import org.dangcat.persistence.DataWriter;

import java.io.*;

/**
 * �ĵ����������
 *
 * @author dangcat
 */
public abstract class DocumentBase {
    protected final Logger logger = Logger.getLogger(this.getClass());
    protected int lineCount = 0;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    public void close() {
        this.lineCount = 0;
        try {
            if (this.outputStream != null) {
                this.outputStream.close();
                this.outputStream = null;
            }
        } catch (IOException e) {
        }
        this.inputStream = FileUtils.close(this.inputStream);
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * ���ļ��������ݡ�
     *
     * @param dataWriter ��������ӿڡ�
     * @return �����������
     */
    public abstract int read(DataWriter dataWriter);

    /**
     * ���ļ��������ݡ�
     *
     * @param file       Ŀ���ļ���
     * @param dataWriter ��������ӿڡ�
     * @return �����������
     */
    public int read(File file, DataWriter dataWriter) {
        int result = 0;
        try {
            this.inputStream = new FileInputStream(file);
            result = this.read(dataWriter);
        } catch (FileNotFoundException e) {
            this.logger.error(file, e);
        } finally {
            this.close();
        }
        return result;
    }

    /**
     * ����ʵ��������ݵ���������
     *
     * @param outputStream �������
     * @param dataReader   ������Դ��
     * @param ���������
     */
    public abstract int write(DataReader dataReader);

    /**
     * ����ʵ��������ݵ���������
     *
     * @param file         ����ļ���
     * @param dataReader   ��Դ���ݡ�
     * @param exportHeader �Ƿ����ͷ��
     */
    public int write(File file, DataReader dataReader) {
        int result = 0;
        try {
            this.outputStream = new FileOutputStream(file);
            result = this.write(dataReader);
        } catch (FileNotFoundException e) {
            this.logger.error(file, e);
        } finally {
            this.close();
        }
        return result;
    }
}
