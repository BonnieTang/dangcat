package org.dangcat.boot.server.domain;

import java.util.Date;

import org.dangcat.persistence.annotation.Column;
import org.dangcat.persistence.annotation.Index;
import org.dangcat.persistence.annotation.Indexes;
import org.dangcat.persistence.annotation.Table;

@Table
@Indexes( { @Index("ServerId, DateTime") })
public class ServerStatusLog extends MonitorData
{
    public static final String DateTime = "DateTime";
    public static final String Id = "Id";
    private static final long serialVersionUID = 1L;
    public static final String ServerId = "ServerId";

    @Column(index = 2, isNullable = false)
    private Date dateTime = null;

    @Column(isPrimaryKey = true, isAutoIncrement = true, index = 0)
    private Integer id = null;

    @Column(index = 1, displaySize = 32, isNullable = false)
    private Integer serverId = null;

    public Date getDateTime()
    {
        return dateTime;
    }

    public Integer getId()
    {
        return id;
    }

    public Integer getServerId()
    {
        return serverId;
    }

    @Column(index = 14, logic = "octets", isCalculate = true)
    public long getTotalDiskSpace()
    {
        return super.getTotalDiskSpace();
    }

    @Column(index = 15, logic = "octets", isCalculate = true)
    public long getTotalPhysicalMemory()
    {
        return super.getTotalPhysicalMemory();
    }

    public void setDateTime(Date dateTime)
    {
        this.dateTime = dateTime;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        if (this.getId() != null)
        {
            info.append(Id);
            info.append(" = ");
            info.append(this.getId());
        }
        if (this.getServerId() != null)
        {
            if (info.length() > 0)
                info.append(", ");
            info.append(ServerId);
            info.append(" = ");
            info.append(this.getServerId());
        }
        if (this.getDateTime() != null)
        {
            if (info.length() > 0)
                info.append(", ");
            info.append(DateTime);
            info.append(" = ");
            info.append(this.getDateTime());
        }
        info.append(", ");
        info.append(super.toString());
        return info.toString();
    }
}
