package org.dangcat.business.server.chart;

import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

/**
 * ����������
 *
 * @author
 */
@JndiName(module = "Device", name = "ServerChart")
public interface ServerChartService {
    /**
     * ������Դ�����ȵ���Ϣ��
     *
     * @param id     �������š�
     * @param width  ͼƬ��ȡ�
     * @param height ͼƬ�߶ȡ�
     * @return ͼƬ�ȵ���Ϣ��
     */
    String createActiveArea(@Parameter(name = "id") Integer id, @Parameter(name = "width") Integer width, @Parameter(name = "height") Integer height) throws ServiceException;

    /**
     * ������Դ����ͼ��
     *
     * @param id     �������š�
     * @param width  ͼƬ��ȡ�
     * @param height ͼƬ�߶ȡ�
     * @return ͼƬ��
     */
    void renderChartImg(@Parameter(name = "id") Integer id, @Parameter(name = "width") Integer width, @Parameter(name = "height") Integer height) throws ServiceException;
}
