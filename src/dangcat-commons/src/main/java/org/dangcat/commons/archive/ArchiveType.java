package org.dangcat.commons.archive;

import java.util.ArrayList;
import java.util.List;

/**
 * �鵵ѹ�����͡�
 * @author dangcat
 * 
 */
public class ArchiveType
{
    private static final List<ArchiveInfo> archiveTypeList = new ArrayList<ArchiveInfo>();
    public static final String cpgz = ".cpgz";
    public static final String cpio = ".cpio";
    public static final String jar = ".jar";
    public static final String tar = ".tar";
    public static final String tar_bz2 = ".tar.bz2";
    public static final String taz = ".taz";
    public static final String tbz = ".tbz";
    public static final String tbz2 = ".tbz2";
    public static final String tgz = ".tgz";
    public static final String txz = ".txz";
    public static final String zip = ".zip";

    static
    {
        archiveTypeList.add(new ArchiveInfo(cpgz, CompressUtils.gz, cpio));
        archiveTypeList.add(new ArchiveInfo(cpio, null, cpio));
        archiveTypeList.add(new ArchiveInfo(jar, null, jar));
        archiveTypeList.add(new ArchiveInfo(tar, null, tar));
        archiveTypeList.add(new ArchiveInfo(tar_bz2, CompressUtils.bz2, tar));
        archiveTypeList.add(new ArchiveInfo(taz, CompressUtils.gz, tar));
        archiveTypeList.add(new ArchiveInfo(tbz, CompressUtils.bz2, tar));
        archiveTypeList.add(new ArchiveInfo(tbz2, CompressUtils.bz2, tar));
        archiveTypeList.add(new ArchiveInfo(tgz, CompressUtils.gz, tar));
        archiveTypeList.add(new ArchiveInfo(zip, null, zip));
        archiveTypeList.add(new ArchiveInfo(txz, CompressUtils.xz, tar));
    }

    /**
     * ���еĹ鵵���͡�
     */
    public static String[] getAllArchiveTypes()
    {
        List<String> fileExtNameList = new ArrayList<String>();
        for (ArchiveInfo archiveInfo : archiveTypeList)
            fileExtNameList.add(archiveInfo.getFileExtName());
        return fileExtNameList.toArray(new String[0]);
    }

    /**
     * �����ļ�����ȡ�鵵���͡�
     * @param fileName �ļ�����
     * @return �鵵��Ϣ��
     */
    protected static ArchiveInfo getArchiveInfo(String fileName)
    {
        fileName = fileName.toLowerCase();
        ArchiveInfo result = null;
        for (ArchiveInfo archiveInfo : archiveTypeList)
        {
            if (fileName.endsWith(archiveInfo.getFileExtName()))
            {
                result = archiveInfo;
                break;
            }
        }
        return result;
    }
}
