package org.dangcat.commons.io.process;

import java.io.File;

import org.dangcat.commons.io.FileUtils;

/**
 * �ļ�ɾ�����̡�
 * 
 */
public class FileDeleteProcess extends FileProcess
{
    @Override
    protected long getFileSize(File file)
    {
        return 1l;
    }

    @Override
    protected void process(File file)
    {
        FileUtils.delete(file);
        if (this.getLogger().isDebugEnabled())
            this.getLogger().debug("delete " + file.getAbsolutePath());
    }
}
