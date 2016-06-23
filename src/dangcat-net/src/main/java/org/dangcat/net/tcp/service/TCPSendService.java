package org.dangcat.net.tcp.service;

import java.io.IOException;
import java.net.InetAddress;

import org.dangcat.net.service.NetSendService;

public interface TCPSendService extends NetSendService
{
    /**
     * ��ָ���ĵ�ַ�Ͷ˿ڷ��ͱ��ġ�
     * @param remoteAddress Ŀ���ַ��
     * @param remotePort Ŀ��˿ڡ�
     * @param dataBuffer �������ݡ�
     * @param tryTimes ���Խ��ջ�Ӧ������
     * @param timeout ��Ӧ�ȴ�ʱ�䡣
     * @return �Ƿ�ɹ���
     * @throws IOException
     */
    public void send(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer, int tryTimes) throws IOException;
}
