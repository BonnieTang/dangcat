package org.dangcat.net.rfc.service;

import java.net.InetAddress;

/**
 * ��ʶ��ŷ���
 * @author dangcat
 * 
 */
public interface PacketIdentifierService
{
    /**
     * ��Ӱ����͵�ʶ��š�
     * @param radiusPacketType �����͡�
     */
    public void addPacketType(Integer... packetTypes);

    /**
     * ���ָ�����Ĵ���ʶ��š���
     * @param packetType �����͡�
     * @param identifier ʶ��š�
     * @return �Ƿ���Խ��д���
     */
    public boolean addProcess(InetAddress inetAddress, Integer packetType, Integer identifier);

    /**
     * ��ȡ�����͵�ʶ��š�
     * @param packetType �����͡�
     * @return ʶ��š�
     */
    public int nextIdentifier(Integer packetType);

    /**
     * ɾ��ָ�����Ĵ���ʶ��š���
     * @param packetType �����͡�
     * @param identifier ʶ��š�
     */
    public void removeProcess(InetAddress inetAddress, Integer packetType, Integer identifier);
}
