package org.dangcat.business.server;

import org.dangcat.business.service.QueryResult;
import org.dangcat.chart.TimeData;
import org.dangcat.chart.TimeRange;
import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

import java.util.Date;

/**
 * ����������
 *
 * @author
 */
@JndiName(module = "Device", name = "ServerInfo")
public interface ServerInfoService {
    /**
     * ɾ��ָ�����������ݡ�
     *
     * @param Id �������š�
     * @return ɾ�������
     */
    boolean delete(@Parameter(name = "id") Integer id) throws ServiceException;

    /**
     * ��ѯָ�������������ڴ��С��
     *
     * @param Id �������š�
     * @return ϵͳ���ڴ档
     */
    long getTotalPhysicalMemory(@Parameter(name = "id") Integer id);

    /**
     * �����������Դ״̬���ݡ�
     *
     * @param id        �������š�
     * @param timeRange ʱ�䷶Χ��
     * @param baseTime  ��׼ʱ�䡣
     * @param lastTime  ���������ʼʱ�䡣
     * @return ��������Դ״̬���ݡ�
     */
    TimeData<ServerResourceLog> loadServerResourceLogs(@Parameter(name = "id") Integer id, @Parameter(name = "timeRange") TimeRange timeRange, @Parameter(name = "lastTime") Date lastTime);

    /**
     * ��ѯָ�����������ݡ�
     *
     * @param serverInfoFilter ��ѯ��Χ��
     * @return ��ѯ�����
     */
    QueryResult<ServerInfoQuery> query(@Parameter(name = "serverInfoFilter") ServerInfoFilter serverInfoFilter) throws ServiceException;

    /**
     * �鿴��������Ϣ��
     *
     * @param Id �������š�
     * @return ��ѯ�����
     */
    ServerInfoQuery view(@Parameter(name = "id") Integer id) throws ServiceException;
}
