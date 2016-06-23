package org.dangcat.business.staff.service;

import org.dangcat.business.service.QueryResult;
import org.dangcat.business.staff.domain.OperateStat;
import org.dangcat.business.staff.domain.OperatorOptLog;
import org.dangcat.business.staff.filter.OperateLogFilter;
import org.dangcat.business.staff.filter.OperatorOptLogFilter;
import org.dangcat.commons.reflect.Parameter;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.annotation.JndiName;

/**
 * The service interface for OperateLog.
 * @author dangcat
 * 
 */
@JndiName(module = "Staff", name = "OperateLog")
public interface OperateLogService
{
    /**
     * ��ѯָ�����������ݡ�
     * @param operatorOptLogFilter ��ѯ������
     * @param month ��ѯ�·ݡ�
     * @return ��ѯ�����
     */
    public QueryResult<OperatorOptLog> load(@Parameter(name = "operatorOptLogFilter") OperatorOptLogFilter operatorOptLogFilter) throws ServiceException;

    /**
     * ��ѯָ�����������ݡ�
     * @param operateLogFilter ��ѯ������
     * @return ��ѯ�����
     */
    public QueryResult<OperateStat> query(@Parameter(name = "operateLogFilter") OperateLogFilter operateLogFilter) throws ServiceException;

    /**
     * �鿴ָ�����������ݡ�
     * @param id ����ֵ��
     * @param month ��ѯ�·ݡ�
     * @return �鿴�����
     */
    public OperateStat view(@Parameter(name = "id") Integer id, @Parameter(name = "month") Integer month) throws ServiceException;
}
