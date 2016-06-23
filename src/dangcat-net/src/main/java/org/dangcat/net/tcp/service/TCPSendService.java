package org.dangcat.net.tcp.service;

import org.dangcat.net.service.NetSendService;

import java.io.IOException;
import java.net.InetAddress;

public interface TCPSendService extends NetSendService {
    /**
     * ��ָ���ĵ�ַ�Ͷ˿ڷ��ͱ��ġ�
     *
     * @param remoteAddress Ŀ���ַ��
     * @param remotePort    Ŀ��˿ڡ�
     * @param dataBuffer    �������ݡ�
     * @param tryTimes      ���Խ��ջ�Ӧ������
     * @param timeout       ��Ӧ�ȴ�ʱ�䡣
     * @return �Ƿ�ɹ���
     * @throws IOException
     */
    void send(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer, int tryTimes) throws IOException;
}
