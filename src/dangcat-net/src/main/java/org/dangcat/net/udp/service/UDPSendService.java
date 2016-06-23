package org.dangcat.net.udp.service;

import java.io.IOException;
import java.net.InetAddress;

import org.dangcat.net.event.DatagramEvent;
import org.dangcat.net.service.NetSendService;

public interface UDPSendService extends NetSendService
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
    public DatagramEvent send(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer, int tryTimes, int timeout) throws IOException;
}
