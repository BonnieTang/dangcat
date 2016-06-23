package org.dangcat.framework.service.interceptor;

import org.dangcat.commons.reflect.MethodInfo;
import org.dangcat.framework.service.ServiceContext;

/**
 * ִ�к���������
 * @author dangcat
 * 
 */
public interface AfterInterceptor
{
    /**
     * ����ִ�з���ǰ��������
     * @param service �������
     * @param serviceContext �����ġ�
     * @param methodInfo ִ�з�����
     * @param args ִ�в�����
     * @param result ִ�н����
     */
    public void afterInvoke(Object service, ServiceContext serviceContext, MethodInfo methodInfo, Object[] args, Object result);
}
