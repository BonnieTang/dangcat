package org.dangcat.net.event;

/**
 * �������������ӿڡ�
 * @author dangcat
 * 
 */
public interface DatagramReceiveListener
{
    /**
     * �������ݡ�
     * @param datagramEvent �����¼���
     */
    public void onReceive(DatagramEvent datagramEvent);
}
