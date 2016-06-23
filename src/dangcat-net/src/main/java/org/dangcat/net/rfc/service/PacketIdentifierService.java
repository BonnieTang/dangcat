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
    void addPacketType(Integer... packetTypes);

    /**
     * ���ָ�����Ĵ���ʶ��š���
     * @param packetType �����͡�
     * @param identifier ʶ��š�
     * @return �Ƿ���Խ��д���
     */
    boolean addProcess(InetAddress inetAddress, Integer packetType, Integer identifier);

    /**
     * ��ȡ�����͵�ʶ��š�
     * @param packetType �����͡�
     * @return ʶ��š�
     */
    int nextIdentifier(Integer packetType);

    /**
     * ɾ��ָ�����Ĵ���ʶ��š���
     * @param packetType �����͡�
     * @param identifier ʶ��š�
     */
    void removeProcess(InetAddress inetAddress, Integer packetType, Integer identifier);
}
