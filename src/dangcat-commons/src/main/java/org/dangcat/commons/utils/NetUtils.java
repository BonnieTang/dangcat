package org.dangcat.commons.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetUtils
{
    public static void checkPortValid(String ip, Integer port) throws IOException
    {
        if (port != null)
        {
            Socket socket = new Socket();
            socket.bind(new InetSocketAddress(ip, port));
            socket.close();
        }
    }

    public static boolean isInetAddress(String ip)
    {
        return InetAddressUtils.isInetAddress(ip);
    }

    /**
     * �жϱ��ض˿��Ƿ���Ч��
     * @param port �˿ںš�
     * @return �˿��Ƿ���Ч��
     */
    public static boolean isPortValid(Integer port)
    {
        return isPortValid("127.0.0.1", port) && isPortValid("0.0.0.0", port);
    }

    /**
     * �ж�Զ�̶˿��Ƿ���Ч��
     * @param ip Զ�̵�ַ��
     * @param port �˿ںš�
     * @return �˿��Ƿ���Ч��
     */
    public static boolean isPortValid(String ip, Integer port)
    {
        boolean result = false;
        try
        {
            if (port != null)
            {
                checkPortValid(ip, port);
                result = true;
            }
        }
        catch (IOException e)
        {
        }
        return result;
    }

    /**
     * ������ַ��
     * @param ip ��ַ��
     * @return ��ַ����
     */
    public static InetAddress toInetAddress(String ip) throws UnknownHostException
    {
        return InetAddressUtils.toInetAddress(ip);
    }
}
