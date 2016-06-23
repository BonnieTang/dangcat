package org.dangcat.document.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.dangcat.persistence.DataWriter;
import org.dangcat.persistence.entity.EntityDataReader;
import org.dangcat.persistence.entity.EntityDataWriter;
import org.dangcat.persistence.entity.EntityHelper;
import org.dangcat.persistence.entity.EntityMetaData;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Columns;
import org.dangcat.persistence.model.Table;
import org.dangcat.persistence.model.TableDataReader;
import org.dangcat.persistence.model.TableDataWriter;

/**
 * Csv�����ࡣ
 * @author dangcat
 * 
 */
public class CsvUtils
{
    /**
     * �ӻ������������ݡ�
     * @param reader ���ݻ�������
     * @param classType ʵ�����͡�
     * @param firstHeader �Ƿ��һ��Ϊ��ͷ��
     * @return �����������
     * @throws IOException �쳣��
     * @throws EntityException
     */
    public static <T> List<T> read(BufferedReader reader, Class<T> classType, boolean firstHeader) throws IOException, EntityException
    {
        List<T> entityList = new ArrayList<T>();
        DataWriter dataWriter = new EntityDataWriter<T>(entityList, classType);

        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.read(reader, dataWriter);
        return entityList;
    }

    /**
     * �ӻ������������ݡ�
     * @param reader ���ݻ�������
     * @param table �����
     * @param firstHeader �Ƿ��һ��Ϊ��ͷ��
     * @return �����������
     * @throws IOException �쳣��
     */
    public static int read(BufferedReader reader, Table table, boolean firstHeader) throws IOException
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        return csvDocument.read(reader, new TableDataWriter(table));
    }

    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param classType ʵ�����͡�
     * @param firstHeader �Ƿ��һ��Ϊ��ͷ��
     * @return �����������
     * @throws IOException �쳣��
     */
    public static <T> List<T> read(File file, Class<T> classType, boolean firstHeader)
    {
        List<T> entityList = new ArrayList<T>();
        DataWriter dataWriter = new EntityDataWriter<T>(entityList, classType);

        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.read(file, dataWriter);
        return entityList;
    }

    /**
     * ���ļ��������ݡ�
     * @param file Ŀ���ļ���
     * @param table �����
     * @param firstHeader �Ƿ��һ��Ϊ��ͷ��
     * @return �����������
     * @throws IOException �쳣��
     */
    public static int read(File file, Table table, boolean firstHeader)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        return csvDocument.read(file, new TableDataWriter(table));
    }

    /**
     * ��ʵ����������һ���ı���
     * @param entity ʵ�����
     * @param fieldNames �ֶ������顣
     * @return ��ʽ�������ݡ�
     */
    public static String toCSV(Object entity, String... fieldNames)
    {
        return toText(entity, null, fieldNames);
    }

    /**
     * ��ʵ����������һ���ı���
     * @param entity ʵ�����
     * @param fieldText �ı��ֶΡ�
     * @param fieldNames �ֶ������顣
     * @return ��ʽ�������ݡ�
     */
    public static String toText(Object entity, WriteUserSettings userSettings, String... fieldNames)
    {
        String text = null;
        if (entity != null && fieldNames != null && fieldNames.length > 0)
        {
            EntityMetaData entityMetaData = EntityHelper.getEntityMetaData(entity.getClass());
            Columns columns = entityMetaData.getTable().getColumns();
            try
            {
                StringWriter writer = new StringWriter();
                CsvWriter csvWriter = new CsvWriter(writer, Letters.COMMA);
                if (userSettings != null)
                    csvWriter.setUserSettings(userSettings);
                for (String fieldName : fieldNames)
                {
                    Column column = columns.find(fieldName);
                    if (column != null)
                    {
                        Object value = entityMetaData.getValue(fieldName, entity);
                        csvWriter.write(column.toString(value));
                    }
                }
                if (writer.getBuffer().length() > 0)
                {
                    csvWriter.endRecord();
                    csvWriter.close();
                    text = writer.toString();
                }
            }
            catch (IOException e)
            {

            }
        }
        return text;
    }

    /**
     * ����ʵ��������ݵ���������
     * @param outputStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     */
    public static <T> void write(File file, List<T> entityList, boolean firstHeader)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.write(file, new EntityDataReader<T>(entityList));
    }

    /**
     * ����ʵ��������ݵ���������
     * @param outputStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     */
    public static <T> void write(File file, List<T> entityList, boolean firstHeader, WriteUserSettings writeuserSettings)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.setWriteUserSettings(writeuserSettings);
        csvDocument.write(file, new EntityDataReader<T>(entityList));
    }

    /**
     * ����Table�������ݵ���������
     * @param outputStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     */
    public static void write(File file, Table table, boolean firstHeader)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.write(file, new TableDataReader(table));
    }

    /**
     * ����Table�������ݵ���������
     * @param outputStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     */
    public static void write(File file, Table table, boolean firstHeader, WriteUserSettings writeuserSettings)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.setWriteUserSettings(writeuserSettings);
        csvDocument.write(file, new TableDataReader(table));
    }

    /**
     * ����ʵ��������ݵ���������
     * @param printStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     */
    public static <T> void write(PrintStream printStream, List<T> entityList, boolean firstHeader)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.setOutputStream(printStream);
        csvDocument.write(new EntityDataReader<T>(entityList));
    }

    /**
     * ����ʵ��������ݵ���������
     * @param printStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     */
    public static <T> void write(PrintStream printStream, List<T> entityList, boolean firstHeader, WriteUserSettings writeuserSettings)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.setWriteUserSettings(writeuserSettings);
        csvDocument.setOutputStream(printStream);
        csvDocument.write(new EntityDataReader<T>(entityList));
    }

    /**
     * ����Table�������ݵ���������
     * @param printStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     */
    public static void write(PrintStream printStream, Table table, boolean firstHeader)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.setOutputStream(printStream);
        csvDocument.write(new TableDataReader(table));
    }

    /**
     * ����Table�������ݵ���������
     * @param printStream �������
     * @param table �����
     * @param firstHeader �Ƿ����ͷ��
     * @param fieldText �ַ���ʶ��
     */
    public static void write(PrintStream printStream, Table table, boolean firstHeader, WriteUserSettings writeuserSettings)
    {
        CsvDocument csvDocument = new CsvDocument();
        csvDocument.setFirstHeader(firstHeader);
        csvDocument.setWriteUserSettings(writeuserSettings);
        csvDocument.setOutputStream(printStream);
        csvDocument.write(new TableDataReader(table));
    }
}
