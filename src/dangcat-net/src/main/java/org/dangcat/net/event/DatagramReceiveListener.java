package org.dangcat.net.event;

/**
 * �������������ӿڡ�
 *
 * @author dangcat
 */
public interface DatagramReceiveListener {
    /**
     * �������ݡ�
     *
     * @param datagramEvent �����¼���
     */
    void onReceive(DatagramEvent datagramEvent);
}
