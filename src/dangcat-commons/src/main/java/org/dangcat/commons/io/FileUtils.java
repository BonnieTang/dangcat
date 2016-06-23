package org.dangcat.commons.io;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.ValueUtils;

import java.io.*;
import java.net.URLDecoder;

/**
 * �ļ�������
 */
public class FileUtils
{
    public static final String ENCODING_UTF_8 = "UTF-8";
    private static final Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * �ر���������
     * @param inputStream ��������
     * @return �رպ�Ľ����
     */
    public static InputStream close(InputStream inputStream)
    {
        return FileCloser.close(inputStream);
    }

    /**
     * �ر��������
     * @param outputStream �������
     * @return �رպ�Ľ����
     */
    public static OutputStream close(OutputStream outputStream)
    {
        return FileCloser.close(outputStream);
    }

    public static Reader close(Reader reader)
    {
        return FileCloser.close(reader);
    }

    public static Writer close(Writer writer)
    {
        return FileCloser.close(writer);
    }

    /**
     * ���ļ����߿�����Ŀ���ļ�
     * @param srcFile ��Դ�ļ���Ŀ¼��
     * @param dstFile Ŀ���ļ���Ŀ¼��
     * @return
     */
    public static File copy(File srcFile, File dstFile)
    {
        return FileOperator.copy(srcFile, dstFile);
    }

    public static boolean copy(File input, OutputStream outputStream)
    {
        return FileOperator.copy(input, outputStream);
    }

    public static boolean copy(InputStream inputStream, File output)
    {
        return FileOperator.copy(inputStream, output);
    }

    public static boolean copy(InputStream inputStream, OutputStream outputStream)
    {
        return FileOperator.copy(inputStream, outputStream);
    }

    /**
     * ��·�����б���ת����
     * @param path ·����
     * @return ������·����
     */
    public static String decodePath(String path)
    {
        String directory = null;
        try
        {
            directory = URLDecoder.decode(path, ENCODING_UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error(path, e);
        }
        return directory;
    }

    /**
     * ǿ��ɾ��ָ���ļ���Ŀ¼��
     * @param file ָ���ļ���Ŀ¼��
     */
    public static void delete(File file)
    {
        try
        {
            if (file.exists())
                org.apache.commons.io.FileUtils.forceDelete(file);
        }
        catch (IOException e)
        {
            logger.error("delete " + file.getAbsolutePath() + " failed! ", e);
        }
    }

    /**
     * ���ָ��·����ָ����չ�����ļ���
     * @param path ·����
     * @param postFix ��׺����
     */
    public static void deleteFiles(String path, final String postFix)
    {
        File filePath = new File(path);
        File[] fileArray = filePath.listFiles((FilenameFilter) new FileNameFilter(postFix));
        if (fileArray != null)
        {
            for (File file : fileArray)
            {
                if (logger.isDebugEnabled())
                    logger.info("Delete the file " + file.getAbsolutePath());
                file.delete();
            }
        }
    }

    /**
     * ��ȡ�淶·����
     * @param path ·����
     * @return �淶��׼·����
     */
    public static String getCanonicalPath(String path)
    {
        File filePath = new File(path);
        if (filePath.exists())
        {
            try
            {
                return filePath.getCanonicalPath();
            }
            catch (IOException e)
            {
            }
        }
        return path;
    }

    /**
     * ��ȡ�ļ�����չ����
     * @param fileName �ļ�����
     * @return ��չ����
     */
    public static String getExtension(String fileName)
    {
        String extension = null;
        if (!ValueUtils.isEmpty(fileName))
        {
            int index = fileName.lastIndexOf('.');
            if (index > -1 && index < fileName.length() - 1)
                extension = fileName.substring(index + 1).trim();
        }
        return extension;
    }

    /**
     * �õ�ָ�����͵���Դ·������ת�����ļ�����
     * @param classType ���͡�
     * @param path ·����
     * @return Ŀ¼����
     */
    public static String getResourcePath(Class<?> classType, String path)
    {
        String directory = classType.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (!ValueUtils.isEmpty(path))
            directory += File.separator + path;
        String reousrcePath = decodePath(directory);
        if (reousrcePath.startsWith("file:/") && reousrcePath.endsWith("!/"))
            reousrcePath = reousrcePath.substring(6, reousrcePath.length() - 2);
        return reousrcePath;
    }

    /**
     * �õ��ļ�����Ŀ¼�Ĵ�С��
     * @param file �ļ�����Ŀ¼��
     * @return ռ�ÿռ��С��byte����
     */
    public static long getTotalSize(File file)
    {
        long totalSize = 0;
        if (file != null && file.exists())
        {
            if (file.isDirectory())
                totalSize = org.apache.commons.io.FileUtils.sizeOfDirectory(file);
            else if (file.isFile())
                totalSize = org.apache.commons.io.FileUtils.sizeOf(file);
        }
        return totalSize;
    }

    public static boolean isEmptyDirectory(File dir)
    {
        boolean result = true;
        if (dir.exists() && dir.isDirectory())
        {
            String[] files = dir.list();
            result = files == null || files.length == 0;
        }
        return result;
    }

    /**
     * ����ָ��·����
     * @param path ·����
     * @return �Ƿ�ɹ���
     */
    public static boolean mkdir(String path)
    {
        File directory = new File(path);
        try
        {
            if (!directory.exists())
                org.apache.commons.io.FileUtils.forceMkdir(directory);
        }
        catch (Exception e)
        {
            logger.error("mkdir " + path + " failed! ", e);
        }
        return directory.exists() && directory.isDirectory();
    }

    /**
     * �ƶ��ļ���
     * @param srcFile ��Դ�ļ���
     * @param dstFile Ŀ���ļ���
     * @param deleteExists ���Ŀ���ļ���������ɾ����
     * @return �Ƿ�ɹ���
     * @throws IOException
     */
    public static boolean move(File srcFile, File dstFile, boolean deleteExists)
    {
        return FileOperator.move(srcFile, dstFile, deleteExists);
    }

    /**
     * ���ļ������������ļ�·�����������Զ�������
     * @param file �ļ�����
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(File file) throws IOException
    {
        return org.apache.commons.io.FileUtils.openOutputStream(file);
    }

    /**
     * ��ȡ�ļ������ݡ�
     * @param file �ļ�����
     * @return �ļ����ݡ�
     * @throws IOException
     */
    public static byte[] readFileToBytes(File file) throws IOException
    {
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }

    /**
     * ��ȡ�ļ������ݡ�
     * @param file �ļ�����
     * @return �ļ����ݡ�
     * @throws IOException
     */
    public static String readFileToString(File file, String encoding) throws IOException
    {
        return org.apache.commons.io.FileUtils.readFileToString(file, encoding);
    }

    /**
     * ���ļ�����չ����
     * @param oldExtension �ɵ��ļ�����
     * @param newExtension �µ��ļ���չ����
     * @return �޸ĺ��ļ���
     */
    public static File renameFileExtName(File file, String oldExtension, String newExtension)
    {
        return FileOperator.renameFileExtName(file, oldExtension, newExtension);
    }
}
