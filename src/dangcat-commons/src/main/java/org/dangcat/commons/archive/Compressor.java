package org.dangcat.commons.archive;

import org.dangcat.commons.io.FileNameFilter;
import org.dangcat.commons.io.FileUtils;
import org.dangcat.commons.utils.ValueUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ѹ�������ࡣ
 * @author dangcat
 * 
 */
public class Compressor
{
    protected static final String FILE_SEPARATOR = "/";
    private long compressSize = 0;
    private String encoding = null;
    private Map<String, List<File>> entityMap = new HashMap<String, List<File>>();
    private FileNameFilter fileNameFilter = null;
    private long totalSize = 0;

    /**
     * ��ӹ鵵�ļ���Ŀ¼��
     * @param file �鵵�ļ���
     */
    public void addArchiveEntry(File file)
    {
        this.addArchiveEntry(file, null);
    }

    /**
     * ��ӹ鵵�ļ���Ŀ¼��
     * @param file �鵵�ļ���
     * @param path ѹ��·����
     */
    public void addArchiveEntry(File file, String path)
    {
        if (this.fileNameFilter != null && !this.fileNameFilter.accept(file))
            return;

        if (file == null || file.isFile())
            this.addFileArchiveEntry(file, path);
        else if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files != null)
            {
                if (files.length == 0)
                    this.addFileArchiveEntry(file, path);
                else
                {
                    String pathName = ValueUtils.isEmpty(path) ? file.getName() : path + FILE_SEPARATOR + file.getName();
                    for (File childFile : files)
                        this.addArchiveEntry(childFile, pathName);
                }
            }
        }
    }

    /**
     * ��ӹ鵵�ļ���
     * @param file �鵵�ļ���
     * @param path ѹ��·����
     */
    private void addFileArchiveEntry(File file, String path)
    {
        if (ValueUtils.isEmpty(path))
            path = "";

        List<File> fileList = this.entityMap.get(path);
        if (fileList == null)
        {
            fileList = new ArrayList<File>();
            this.entityMap.put(path, fileList);
        }
        if (file != null && file.exists() && !fileList.contains(file))
            fileList.add(file);
    }

    /**
     * ѹ���ļ���
     * @throws IOException
     */
    public void compress(File compressFile) throws IOException
    {
        this.totalSize = 0;
        this.compressSize = 0;

        ArchiveInfo archiveInfo = ArchiveType.getArchiveInfo(compressFile.getName());
        if (archiveInfo == null)
            throw new IOException("This archive file is not support: " + compressFile);

        Archiver archiver = archiveInfo.createArchiver();
        // ѹ���ĵ���
        if (archiveInfo.getCompressType() != null)
        {
            File archiveFile = File.createTempFile("TMP", archiveInfo.getArchiverType());
            try
            {
                // �����鵵��
                archiver.pack(this, archiveFile);
                // ѹ���ĵ���
                CompressUtils.compress(archiveInfo.getCompressType(), archiveFile, compressFile);
            }
            finally
            {
                archiveFile.delete();
            }
        }
        else
            archiver.pack(this, compressFile);

        this.compressSize = compressFile.length();
    }

    /**
     * ��ѹ����
     * @param destPath Ŀ��·����
     * @throws IOException
     */
    public void decompress(File compressFile, File destPath) throws IOException
    {
        this.totalSize = 0;
        if (!compressFile.exists())
            throw new FileNotFoundException(compressFile.getAbsolutePath());

        if (destPath.isFile())
            throw new IOException("The dest path is file exists.");

        if (!destPath.exists())
            FileUtils.mkdir(destPath.getAbsolutePath());

        if (!destPath.exists() || !destPath.isDirectory())
            throw new FileNotFoundException(destPath.getAbsolutePath());

        ArchiveInfo archiveInfo = ArchiveType.getArchiveInfo(compressFile.getName());
        if (archiveInfo == null)
            throw new IOException("This archive file is not support: " + compressFile);

        Archiver archiver = archiveInfo.createArchiver();
        if (archiver == null)
            throw new IOException("This archive type is not declare: " + compressFile);

        // ѹ���ĵ���
        if (archiveInfo.getCompressType() != null)
        {
            File archiveFile = File.createTempFile("TMP", archiveInfo.getArchiverType());
            try
            {
                // ��ѹ���ĵ���
                CompressUtils.decompressFile(archiveInfo.getCompressType(), compressFile, archiveFile);
                // �����鵵��
                archiver.unpack(this, archiveFile, destPath);
            }
            finally
            {
                archiveFile.delete();
            }
        }
        else
            archiver.unpack(this, compressFile, destPath);
    }

    public int getCompressRate()
    {
        return (int) (this.totalSize == 0 ? 0 : 100 - this.compressSize * 100.0 / this.totalSize);
    }

    public long getCompressSize()
    {
        return this.compressSize;
    }

    public String getEncoding()
    {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    protected Map<String, List<File>> getEntityMap()
    {
        return this.entityMap;
    }

    public FileNameFilter getFileNameFilter()
    {
        return this.fileNameFilter;
    }

    public void setFileNameFilter(FileNameFilter fileNameFilter) {
        this.fileNameFilter = fileNameFilter;
    }

    public long getTotalSize()
    {
        return this.totalSize;
    }

    protected void processFile(File uncompressFile)
    {
        this.totalSize += uncompressFile.length();
    }
}
