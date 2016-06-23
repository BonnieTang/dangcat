package org.dangcat.system.upload;

import java.io.File;
import java.io.InputStream;

import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceInformation;
import org.dangcat.framework.service.annotation.JndiName;
import org.dangcat.system.upload.impl.UploadProcess;
import org.dangcat.web.annotation.InvokeProcess;

/**
 * �ļ��ϴ���
 * @author Administrator
 * 
 */
@JndiName(module = "System", name = "UploadDemo")
public interface UploadDemoService
{
    /**
     * �ϴ��ֽ����顣
     */
    @InvokeProcess(UploadProcess.class)
    public ServiceInformation uploadBytes(@Parameter(name = "bytes") byte[] bytes);

    /**
     * �ϴ��ļ���
     */
    @InvokeProcess(UploadProcess.class)
    public ServiceInformation uploadFile(@Parameter(name = "file") File file);

    /**
     * �ϴ���������
     */
    @InvokeProcess(UploadProcess.class)
    public ServiceInformation uploadInputStream(@Parameter(name = "inputStream") InputStream inputStream);
}
