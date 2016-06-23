package org.dangcat.net.tcp.service.impl;

import org.dangcat.framework.pool.ObjectPool;
import org.dangcat.net.tcp.service.TCPSendService;
import org.dangcat.net.tcp.service.TCPSender;

import java.io.IOException;
import java.net.InetAddress;

/**
 * UDP���ķ��ͷ���
 * @author fanst174766
 * 
 */
public class TCPSendServiceImpl extends ObjectPool<TCPSender> implements TCPSendService
{
    @Override
    protected void close(TCPSender tcpSender)
    {
        tcpSender.close();
    }

    @Override
    protected TCPSender create()
    {
        return new TCPSender(false);
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
        this.send(remoteAddress, remotePort, dataBuffer, 2);
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
    public void send(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer, int tryTimes) throws IOException
    {
        TCPSender tcpSender = this.poll();
        if (tcpSender != null)
        {
            try
            {
                if (tryTimes > 0)
                    tcpSender.send(remoteAddress, remotePort, dataBuffer, tryTimes);
                else
                    tcpSender.send(remoteAddress, remotePort, dataBuffer);
            }
            finally
            {
                this.release(tcpSender);
            }
        }
    }
}
