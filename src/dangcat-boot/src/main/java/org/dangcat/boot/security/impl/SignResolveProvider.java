package org.dangcat.boot.security.impl;

/**
 * ǩ�������ӿڡ�
 */
public interface SignResolveProvider
{
    /**
     * ��¼��Ϣת����ǩ���ִ���
     * @param loginUser ��¼��Ϣ��
     * @param clientIp �ͻ��˵�ַ��
     * @return ǩ���ִ���
     */
    public String createSignId(LoginUser loginUser);

    /**
     * ǩ���ִ�ת���ɵ�¼��Ϣ��
     * @param signId ǩ���ִ���
     * @param clientIp �ͻ��˵�ַ��
     * @return ��¼��Ϣ��
     */
    public LoginUser parseLoginUser(String signId);
}
