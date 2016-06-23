package org.dangcat.net.udp.service.impl;

import org.dangcat.framework.pool.ObjectPool;
import org.dangcat.net.event.DatagramEvent;
import org.dangcat.net.udp.service.UDPSendService;
import org.dangcat.net.udp.service.UDPSender;

import java.io.IOException;
import java.net.InetAddress;

/**
 * UDP���ķ��ͷ���
 * @author fanst174766
 * 
 */
public class UDPSendServiceImpl extends ObjectPool<UDPSender> implements UDPSendService
{
    @Override
    protected void close(UDPSender udpSender)
    {
        udpSender.close();
    }

    @Override
    protected UDPSender create()
    {
        return new UDPSender();
    }

    /**
     * ��ָ���ĵ�ַ�Ͷ˿ڷ��ͱ��ġ�
     * @param remoteAddress Ŀ���ַ��
     * @param remotePort Ŀ��˿ڡ�
     * @param dataBuffer �������ݡ�
     * @return �Ƿ�ɹ���
     * @throws IOException
     */
    public void send(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer) throws IOException
    {
        this.send(remoteAddress, remotePort, dataBuffer, 2, 0);
    }

    /**
     * ��ָ���ĵ�ַ�Ͷ˿ڷ��ͱ��ģ������ջ�Ӧ���ġ�
     * @param remoteAddress Ŀ���ַ��
     * @param remotePort Ŀ��˿ڡ�
     * @param dataBuffer �������ݡ�
     * @param tryTimes ���Խ��ջ�Ӧ������
     * @return �Ƿ�ɹ���
     * @throws IOException
     */
    public DatagramEvent send(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer, int tryTimes, int timeout) throws IOException
    {
        DatagramEvent replyEvent = null;
        UDPSender udpSender = this.poll();
        if (udpSender != null)
        {
            try
            {
                if (tryTimes > 0)
                    replyEvent = udpSender.send(remoteAddress, remotePort, dataBuffer, tryTimes, timeout);
                else
                    udpSender.send(remoteAddress, remotePort, dataBuffer);
            }
            finally
            {
                this.release(udpSender);
            }
        }
        return replyEvent;
    }
}
