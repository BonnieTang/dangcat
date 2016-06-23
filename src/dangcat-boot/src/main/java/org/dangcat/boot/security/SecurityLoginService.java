package org.dangcat.boot.security;

import org.dangcat.boot.security.exceptions.SecurityLoginException;
import org.dangcat.framework.service.ServicePrincipal;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.framework.service.annotation.JndiName;
import org.dangcat.framework.service.annotation.MethodId;

/**
 * ��ȫ����ӿڡ�
 * @author dangcat
 * 
 */
@JndiName(module = "System", name = "Security")
public interface SecurityLoginService extends ServiceProvider
{
    /**
     * ��ָ���û��������½��
     * @param no �û�����
     * @param password ���롣
     * @return ��½��Ϣ��
     * @throws SecurityLoginException ��½�쳣��
     */
    @MethodId(1)
    public ServicePrincipal login(String no, String password) throws SecurityLoginException;

    /**
     * �ǳ�ָ���û���
     * @param servicePrincipal �û���Ϣ��
     * @return �ǳ������
     * @throws SecurityLoginException ��½�쳣��
     */
    @MethodId(2)
    public boolean logout(ServicePrincipal servicePrincipal) throws SecurityLoginException;

    /**
     * ��ǩ����ʽ��¼��
     * @param signId ��¼ǩ����
     * @return ��½��Ϣ��
     * @throws SecurityLoginException ��½�쳣��
     */
    @MethodId(3)
    public ServicePrincipal signin(String signId) throws SecurityLoginException;
}
