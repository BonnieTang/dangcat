package org.dangcat.net.process.service.impl;

import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.net.rfc.PacketSession;

/**
 * ���Ĵ����������ࡣ
 * @author dangcat
 * 
 */
public class PacketSessionProcessServiceImpl<T extends PacketSession<?>> extends SessionProcessServiceBase<T>
{
    /**
     * ��������
     * @param parent
     */
    public PacketSessionProcessServiceImpl(ServiceProvider parent)
    {
        super(parent);
    }
}
