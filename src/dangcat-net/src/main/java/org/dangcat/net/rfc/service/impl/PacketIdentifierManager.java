package org.dangcat.net.rfc.service.impl;

import org.dangcat.framework.service.ServiceBase;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.net.rfc.service.PacketIdentifierService;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * ��ʶ��ŷ���
 * @author dangcat
 * 
 */
public class PacketIdentifierManager extends ServiceBase implements PacketIdentifierService
{
    private static PacketIdentifierManager instance = null;
    private Map<Integer, PacketIdentifier> packetIdentifierMap = new HashMap<Integer, PacketIdentifier>();

    public PacketIdentifierManager(ServiceProvider parent) {
        super(parent);
    }

    /**
     * ��������ʵ����
     * @param parent ����������
     * @return
     */
    public static synchronized PacketIdentifierService createInstance(ServiceProvider parent)
    {
        if (instance == null)
        {
            instance = new PacketIdentifierManager(parent);
            instance.initialize();
        }
        return instance;
    }

    /**
     * ��Ӱ����͵�ʶ��š�
     * @param radiusPacketType �����͡�
     */
    public void addPacketType(Integer... packetTypes)
    {
        Map<Integer, PacketIdentifier> packetIdentifierMap = new HashMap<Integer, PacketIdentifier>();
        packetIdentifierMap.putAll(this.packetIdentifierMap);
        for (Integer packetType : packetTypes)
            packetIdentifierMap.put(packetType, new PacketIdentifier(packetType));
        this.packetIdentifierMap = packetIdentifierMap;
    }

    /**
     * ���ָ�����Ĵ���ʶ��š���
     * @param packetType �����͡�
     * @param identifier ʶ��š�
     * @return �Ƿ���Խ��д���
     */
    public boolean addProcess(InetAddress inetAddress, Integer packetType, Integer identifier)
    {
        return this.packetIdentifierMap.get(packetType).addProcess(inetAddress, identifier);
    }

    /**
     * ��ȡ�����͵�ʶ��š�
     * @param packetType �����͡�
     * @return ʶ��š�
     */
    public int nextIdentifier(Integer packetType)
    {
        return this.packetIdentifierMap.get(packetType).nextIdentifier();
    }

    /**
     * ɾ��ָ�����Ĵ���ʶ��š���
     * @param packetType �����͡�
     * @param identifier ʶ��š�
     */
    public void removeProcess(InetAddress inetAddress, Integer packetType, Integer identifier)
    {
        this.packetIdentifierMap.get(packetType).removeProcess(inetAddress, identifier);
    }
}
