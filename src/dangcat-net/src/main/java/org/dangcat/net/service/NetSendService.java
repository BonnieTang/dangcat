package org.dangcat.net.service;

import java.io.IOException;
import java.net.InetAddress;

public interface NetSendService
{
    /**
     * ��ָ���ĵ�ַ�Ͷ˿ڷ��ͱ��ġ�
     * @param remoteAddress Ŀ���ַ��
     * @param remotePort Ŀ��˿ڡ�
     * @param dataBuffer �������ݡ�
     * @throws IOException
     */
    void send(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer) throws IOException;

}
