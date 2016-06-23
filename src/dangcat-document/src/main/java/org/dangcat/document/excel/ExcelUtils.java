package org.dangcat.document.excel;

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
 * Excel�����ࡣ
 * @author dangcat
 * 
 */
public class ExcelUtils
{
    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param classType ʵ�����͡�
     * @param sheetIndex ��ǩλ�á�
     * @return �����������
     * @throws IOException �쳣��
     */
    public static <T> List<T> read(File file, Class<T> classType, int sheetIndex) throws IOException
    {
        List<T> entityList = new ArrayList<T>();
        DataWriter dataWriter = new EntityDataWriter<T>(entityList, classType);

        ExcelDocumentReader excelDocumentReader = new ExcelDocumentReader();
        excelDocumentReader.read(file);
        excelDocumentReader.setDataWriter(dataWriter);
        excelDocumentReader.readSheet(sheetIndex);
        return entityList;
    }

    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param table �����
     * @param sheetIndex ��ǩλ�á�
     * @return �����������
     * @throws IOException �쳣��
     */
    public static int read(File file, Table table, int sheetIndex) throws IOException
    {
        ExcelDocumentReader excelDocumentReader = new ExcelDocumentReader();
        excelDocumentReader.read(file);
        excelDocumentReader.setDataWriter(new TableDataWriter(table));
        excelDocumentReader.readSheet(sheetIndex);
        return table.getRows().size();
    }

    /**
     * �ӻ������������ݡ�
     * @param inputStream ���ݻ�������
     * @param classType ʵ�����͡�
     * @param sheetIndex ��ǩλ�á�
     * @return �����������
     * @throws IOException �쳣��
     * @throws EntityException
     */
    public static <T> List<T> read(InputStream inputStream, Class<T> classType, int sheetIndex) throws IOException, EntityException
    {
        List<T> entityList = new ArrayList<T>();
        DataWriter dataWriter = new EntityDataWriter<T>(entityList, classType);

        ExcelDocumentReader excelDocumentReader = new ExcelDocumentReader();
        excelDocumentReader.read(inputStream);
        excelDocumentReader.setDataWriter(dataWriter);
        excelDocumentReader.readSheet(sheetIndex);
        return entityList;
    }

    /**
     * �ӻ������������ݡ�
     * @param inputStream ���ݻ�������
     * @param table �����
     * @param sheetIndex ��ǩλ�á�
     * @return �����������
     * @throws IOException �쳣��
     */
    public static int read(InputStream inputStream, Table table, int sheetIndex) throws IOException
    {
        ExcelDocumentReader excelDocumentReader = new ExcelDocumentReader();
        excelDocumentReader.read(inputStream);
        excelDocumentReader.setDataWriter(new TableDataWriter(table));
        excelDocumentReader.readSheet(sheetIndex);
        return table.getRows().size();
    }

    /**
     * ����ʵ��������ݵ���������
     * @param file �������
     * @param table �����
     * @param sheetTitle ��ǩ���⡣
     * @throws IOException
     */
    public static <T> void write(File file, List<T> entityList, String sheetTitle) throws IOException
    {
        ExcelDocumentWriter excelDocumentWriter = new ExcelDocumentWriter();
        excelDocumentWriter.setSheetTitle(sheetTitle);
        excelDocumentWriter.setDataReader(new EntityDataReader<T>(entityList));
        excelDocumentWriter.write(file);
    }

    /**
     * ����Table�������ݵ���������
     * @param outputStream �������
     * @param table �����
     * @param sheetTitle ��ǩ���⡣
     * @throws IOException
     */
    public static void write(File file, Table table, String sheetTitle) throws IOException
    {
        ExcelDocumentWriter excelDocumentWriter = new ExcelDocumentWriter();
        excelDocumentWriter.setSheetTitle(sheetTitle);
        excelDocumentWriter.setDataReader(new TableDataReader(table));
        excelDocumentWriter.write(file);
    }

    /**
     * ����Table�������ݵ���������
     * @param printStream �������
     * @param table �����
     * @param sheetIndex ��ǩλ�á�
     * @throws IOException
     */
    public static <T> void write(OutputStream outputStream, List<T> entityList, String sheetTitle) throws IOException
    {
        ExcelDocumentWriter excelDocumentWriter = new ExcelDocumentWriter();
        excelDocumentWriter.setSheetTitle(sheetTitle);
        excelDocumentWriter.setDataReader(new EntityDataReader<T>(entityList));
        excelDocumentWriter.write(outputStream);
    }

    /**
     * ����Table�������ݵ���������
     * @param printStream �������
     * @param table �����
     * @param sheetIndex ��ǩλ�á�
     * @param fieldText �ַ���ʶ��
     * @throws IOException
     */
    public static void write(OutputStream outputStream, Table table, String sheetTitle) throws IOException
    {
        ExcelDocumentWriter excelDocumentWriter = new ExcelDocumentWriter();
        excelDocumentWriter.setSheetTitle(sheetTitle);
        excelDocumentWriter.setDataReader(new TableDataReader(table));
        excelDocumentWriter.write(outputStream);
    }
}
