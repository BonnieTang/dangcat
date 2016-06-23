package org.dangcat.net.process.service;

import org.apache.log4j.Logger;
import org.dangcat.commons.utils.DateUtils;
import org.dangcat.net.rfc.exceptions.ProtocolValidateException;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SessionBase
{
    /** ��ʼʱ�䡣 */
    private long beginTime = DateUtils.currentTimeMillis();
    /** �Ƿ�ȡ���������̡� */
    private boolean cancel = false;
    /** ����ʱ�䡣 */
    private Date createTime = DateUtils.now();
    /** �Ƿ��Ѿ���ɴ������̡� */
    private boolean handle = false;
    /** Ŀ����־�� */
    private Logger logger = null;
    /** ������ */
    private Map<String, Object> params = new HashMap<String, Object>();
    /** ��ʱͳ�Ʊ� */
    private Map<String, Long> timCostMap = new LinkedHashMap<String, Long>();
    /** ��ʱ�� */
    private long totalTimeCost = 0;

    public void end()
    {
        this.totalTimeCost = DateUtils.currentTimeMillis() - this.getCreateTime().getTime();

        Logger logger = this.getLogger();
        if (logger.isDebugEnabled())
            logger.debug(this.getDebugInfo());
        else
            logger.info(this);
    }

    public Date getCreateTime()
    {
        return this.createTime;
    }

    public abstract String getDebugInfo();

    public Logger getLogger()
    {
        if (this.logger == null)
            return Logger.getLogger(this.getClass());
        return this.logger;
    }

    public Map<String, Object> getParams()
    {
        return this.params;
    }

    public Map<String, Long> getTimCostMap()
    {
        return this.timCostMap;
    }

    public long getTimeCostThreshold()
    {
        return 0l;
    }

    public long getTotalTimeCost()
    {
        return this.totalTimeCost;
    }

    public boolean isCancel()
    {
        return this.cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isHandle()
    {
        return this.handle && !this.isCancel();
    }

    public void setHandle(boolean handle) {
        this.handle = handle;
    }

    public void logTimeCost(String message)
    {
        long timeCost = DateUtils.currentTimeMillis() - this.beginTime;
        this.totalTimeCost += timeCost;
        if (timeCost > 0)
        {
            long timeCostThreshold = this.getTimeCostThreshold();
            if (timeCostThreshold > 0 && timeCost > timeCostThreshold)
            {
                if (this.timCostMap == null)
                    this.timCostMap = new LinkedHashMap<String, Long>();
                this.timCostMap.put(message, timeCost);
            }
        }
        this.beginTime = DateUtils.currentTimeMillis();
    }

    /**
     * ��֤���ĵĺϷ��ԡ�
     * @param datagramPacket ���Ķ���
     * @return
     */
    public abstract void validate() throws ProtocolValidateException;
}
