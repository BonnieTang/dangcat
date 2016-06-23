package org.dangcat.framework.service.interceptor;

import org.dangcat.commons.reflect.MethodInfo;
import org.dangcat.framework.exception.ServiceException;
import org.dangcat.framework.service.ServiceContext;

/**
 * ִ��ǰ��������
 * @author dangcat
 * 
 */
public interface BeforeInterceptor
{
    /**
     * ����ִ�з���ǰ��������
     * @param service �������
     * @param serviceContext �����ġ�
     * @param methodInfo ִ�з�����
     * @param args ִ�в�����
     * @throws ServiceException ִ���쳣��
     */
    void beforeInvoke(Object service, ServiceContext serviceContext, MethodInfo methodInfo, Object[] args) throws ServiceException;
}
