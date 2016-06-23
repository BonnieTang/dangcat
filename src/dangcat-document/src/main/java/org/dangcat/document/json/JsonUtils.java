package org.dangcat.document.json;

import org.dangcat.persistence.DataWriter;
import org.dangcat.persistence.entity.EntityDataReader;
import org.dangcat.persistence.entity.EntityDataWriter;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.model.Table;
import org.dangcat.persistence.model.TableDataReader;
import org.dangcat.persistence.model.TableDataWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Json�����ࡣ
 * @author dangcat
 * 
 */
public class JsonUtils
{
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

        JsonDocument jsonDocument = new JsonDocument();
        jsonDocument.read(file, dataWriter);
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
        JsonDocument jsonDocument = new JsonDocument();
        return jsonDocument.read(file, new TableDataWriter(table));
    }

    /**
     * �ӻ������������ݡ�
     * @param inputStream ���ݻ�������
     * @param classType ʵ�����͡�
     * @return �����������
     * @throws IOException �쳣��JsonDocument
     * @throws EntityException
     */
    public static <T> List<T> read(InputStream inputStream, Class<T> classType) throws IOException, EntityException
    {
        List<T> entityList = new ArrayList<T>();
        DataWriter dataWriter = new EntityDataWriter<T>(entityList, classType);

        JsonDocument jsonDocument = new JsonDocument();
        jsonDocument.setInputStream(inputStream);
        jsonDocument.read(dataWriter);
        return entityList;
    }

    /**
     * �ӻ������������ݡ�
     * @param reader ���ݻ�������
     * @param table �����
     * @return �����������
     * @throws IOException �쳣��
     */
    public static int read(InputStream inputStream, Table table) throws IOException
    {
        JsonDocument jsonDocument = new JsonDocument();
        jsonDocument.setInputStream(inputStream);
        return jsonDocument.read(new TableDataWriter(table));
    }

    /**
     * ����ʵ��������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static <T> void write(File file, List<T> entityList)
    {
        JsonDocument jsonDocument = new JsonDocument();
        jsonDocument.write(file, new EntityDataReader<T>(entityList));
    }

    /**
     * ����Table�������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static void write(File file, Table table)
    {
        JsonDocument jsonDocument = new JsonDocument();
        jsonDocument.write(file, new TableDataReader(table));
    }

    /**
     * ����ʵ��������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static <T> void write(OutputStream outputStream, List<T> entityList)
    {
        JsonDocument jsonDocument = new JsonDocument();
        jsonDocument.setOutputStream(outputStream);
        jsonDocument.write(new EntityDataReader<T>(entityList));
    }

    /**
     * ����Table�������ݵ���������
     * @param outputStream �������
     * @param table �����
     */
    public static void write(OutputStream outputStream, Table table)
    {
        JsonDocument jsonDocument = new JsonDocument();
        jsonDocument.setOutputStream(outputStream);
        jsonDocument.write(new TableDataReader(table));
    }
}
