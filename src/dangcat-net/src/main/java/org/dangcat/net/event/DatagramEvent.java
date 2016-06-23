package org.dangcat.net.event;

import org.dangcat.commons.utils.Environment;
import org.dangcat.commons.utils.ParseUtils;
import org.dangcat.framework.event.Event;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * �����¼���
 *
 * @author dangcat
 */
public class DatagramEvent extends Event {
    private static final long serialVersionUID = 1L;
    /**
     * �յ����������ݡ�
     */
    private byte[] dataBuffer;
    /**
     * ��ӦSocket��
     */
    private DatagramSocket datagramSocket = null;
    /**
     * ���ص�ַ��
     */
    private InetAddress localAddress;
    /**
     * ���ض˿ڡ�
     */
    private Integer localPort = null;
    /**
     * ��Դ��ַ��
     */
    private InetAddress remoteAddress;
    /**
     * ��Դ�˿ڡ�
     */
    private Integer remotePort = null;

    public DatagramEvent(InetAddress remoteAddress, Integer remotePort, byte[] dataBuffer) {
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
        this.dataBuffer = dataBuffer;
    }

    public DatagramEvent(InetAddress remoteAddress, Integer remotePort, InetAddress localAddress, Integer localPort, byte[] dataBuffer) {
        this(remoteAddress, remotePort, localAddress, localPort, dataBuffer, null);
    }

    public DatagramEvent(InetAddress remoteAddress, Integer remotePort, InetAddress localAddress, Integer localPort, byte[] dataBuffer, DatagramSocket datagramSocket) {
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
        this.localAddress = localAddress;
        this.localPort = localPort;
        this.dataBuffer = dataBuffer;
        this.datagramSocket = datagramSocket;
    }

    public DatagramEvent createReply(byte[] dataBuffer) {
        return new DatagramEvent(this.getRemoteAddress(), this.getRemotePort(), dataBuffer);
    }

    public byte[] getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(byte[] dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public InetAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public void reply(byte[] dataBuffer) throws IOException {
        DatagramSocket datagramSocket = this.datagramSocket;
        if (datagramSocket != null)
            datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, this.getRemoteAddress(), this.getRemotePort()));
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append("RemoteAddress = ");
        info.append(this.getRemoteAddress());
        info.append(", RemotePort = ");
        info.append(this.getRemotePort());
        info.append(", LocalAddress = ");
        info.append(this.getLocalAddress());
        info.append(", LocalPort = ");
        info.append(this.getLocalPort());
        info.append(": ");
        info.append(Environment.LINE_SEPARATOR);
        info.append("DataBuffer: ");
        info.append(ParseUtils.toHex(this.getDataBuffer()));
        return info.toString();
    }
}
