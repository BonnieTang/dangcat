package org.dangcat.document.txt;

import org.dangcat.persistence.DataWriter;
import org.dangcat.persistence.entity.EntityDataReader;
import org.dangcat.persistence.entity.EntityDataWriter;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.model.Table;
import org.dangcat.persistence.model.TableDataReader;
import org.dangcat.persistence.model.TableDataWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Text�����ࡣ
 * @author dangcat
 * 
 */
public class TextUtils
{
    /**
     * �ӻ������������ݡ�
     * @param reader ���ݻ�������
     * @param classType ʵ�����͡�
     * @return �����������
     * @throws IOException �쳣��TextDocument
     * @throws EntityException
     */
    public static <T> List<T> read(BufferedReader reader, Class<T> classType) throws IOException, EntityException
    {
        List<T> entityList = new ArrayList<T>();
        DataWriter dataWriter = new EntityDataWriter<T>(entityList, classType);

        TextDocument textDocument = new TextDocument();
        textDocument.read(reader, dataWriter);
        return entityList;
    }

    /**
     * �ӻ������������ݡ�
     * @param reader ���ݻ�������
     * @param table �����
     * @return �����������
     * @throws IOException �쳣��
     */
    public static int read(BufferedReader reader, Table table) throws IOException
    {
        TextDocument textDocument = new TextDocument();
        return textDocument.read(reader, new TableDataWriter(table));
    }

    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param classType ʵ�����͡�
     * @return �����������
     * @throws IOException �쳣��
     */
    public static <T> List<T> read(File file, Class<T> classType)
    {
        List<T> entityList = new ArrayList<T>();
        DataWriter dataWriter = new EntityDataWriter<T>(entityList, classType);

        TextDocument textDocument = new TextDocument();
        textDocument.read(file, dataWriter);
        return entityList;
    }

    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param table �����
     * @return �����������
     * @throws IOException �쳣��
     */
    public static int read(File file, Table table)
    {
        TextDocument textDocument = new TextDocument();
        return textDocument.read(file, new TableDataWriter(table));
    }

    /**
     * ����ʵ��������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static <T> void write(File file, List<T> entityList)
    {
        TextDocument textDocument = new TextDocument();
        textDocument.write(file, new EntityDataReader<T>(entityList));
    }

    /**
     * ����Table�������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static void write(File file, Table table)
    {
        TextDocument textDocument = new TextDocument();
        textDocument.write(file, new TableDataReader(table));
    }

    /**
     * ����ʵ��������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static <T> void write(OutputStream outputStream, List<T> entityList)
    {
        TextDocument textDocument = new TextDocument();
        textDocument.setOutputStream(outputStream);
        textDocument.write(new EntityDataReader<T>(entityList));
    }

    /**
     * ����Table�������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static void write(OutputStream outputStream, Table table)
    {
        TextDocument textDocument = new TextDocument();
        textDocument.setOutputStream(outputStream);
        textDocument.write(new TableDataReader(table));
    }
}
