package org.dangcat.commons.io;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.dangcat.commons.utils.ValueUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * �����ļ�ǰ׺����׺���й��ˡ�
 * 
 */
public class FileNameFilter implements FilenameFilter, FileFilter
{
    private List<AbstractFileFilter> abstractFileFilterList = new ArrayList<AbstractFileFilter>();
    private List<AbstractFileFilter[]> abstractFileFiltersList = new ArrayList<AbstractFileFilter[]>();

    public FileNameFilter()
    {
    }

    /**
     * ���ļ���׺�������ˡ�
     * @param suffix ��׺����
     */
    public FileNameFilter(String suffix)
    {
        this.addSuffixFilter(suffix);
    }

    /**
     * ���ļ�ǰ׺+��׺�������ˡ�
     * @param prefix ǰ׺����
     * @param suffix ��׺����
     */
    public FileNameFilter(String prefix, String suffix)
    {
        this.addFilter(prefix, suffix);
    }

    @Override
    public boolean accept(File file)
    {
        if (file.getName().endsWith("~"))
            return false;

        for (AbstractFileFilter abstractFileFilter : this.abstractFileFilterList)
        {
            if (abstractFileFilter.accept(file))
                return true;
        }
        for (AbstractFileFilter[] abstractFileFilters : this.abstractFileFiltersList)
        {
            boolean reault = true;
            for (AbstractFileFilter abstractFileFilter : abstractFileFilters)
            {
                if (!abstractFileFilter.accept(file))
                {
                    reault = false;
                    break;
                }
            }
            if (reault)
                return true;
        }
        return false;
    }

    @Override
    public boolean accept(File directory, String fileName)
    {
        if (fileName.endsWith("~"))
            return false;

        for (AbstractFileFilter abstractFileFilter : this.abstractFileFilterList)
        {
            if (abstractFileFilter.accept(directory, fileName))
                return true;
        }
        for (AbstractFileFilter[] abstractFileFilters : this.abstractFileFiltersList)
        {
            boolean reault = true;
            for (AbstractFileFilter abstractFileFilter : abstractFileFilters)
            {
                if (!abstractFileFilter.accept(directory, fileName))
                {
                    reault = false;
                    break;
                }
            }
            if (reault)
                return true;
        }
        return false;
    }

    public boolean accept(String fileName)
    {
        return this.accept(null, fileName);
    }

    /**
     * ���ǰ׺+��׺���˱�־��
     * @param prefix ǰ׺��
     * @param suffix ��׺��
     */
    public void addFilter(String prefix, String suffix)
    {
        if (!ValueUtils.isEmpty(prefix) && !ValueUtils.isEmpty(suffix))
            this.abstractFileFiltersList.add(new AbstractFileFilter[] { new PrefixFileFilter(prefix, IOCase.INSENSITIVE), new SuffixFileFilter(suffix, IOCase.INSENSITIVE) });
        else
        {
            this.addPrefixFilter(prefix);
            this.addSuffixFilter(suffix);
        }
    }

    /**
     * ���ǰ׺���ˡ�
     * @param prefix ǰ׺����
     */
    public void addPrefixFilter(String prefix)
    {
        if (!ValueUtils.isEmpty(prefix))
            this.abstractFileFilterList.add(new PrefixFileFilter(prefix, IOCase.INSENSITIVE));
    }

    /**
     * ��Ӻ�׺���ˡ�
     * @param suffix ��׺��
     */
    public void addSuffixFilter(String suffix)
    {
        if (!ValueUtils.isEmpty(suffix))
            this.abstractFileFilterList.add(new SuffixFileFilter(suffix, IOCase.INSENSITIVE));
    }
}
