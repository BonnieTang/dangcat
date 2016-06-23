package org.dangcat.business.staff.config;

import org.dangcat.persistence.annotation.Column;
import org.dangcat.persistence.annotation.Table;
import org.dangcat.persistence.entity.EntityBase;

/**
 * ����Ա�������á�
 *
 * @author dangcat
 */
@Table
public class StaffSetup extends EntityBase {
    public static final String DefaultUseAble = "DefaultUseAble";
    public static final String LogKeepMonth = "LogKeepMonth";
    public static final String ValidDays = "ValidDays";
    private static final long serialVersionUID = 1L;
    /**
     * �½��˻�Ĭ�Ͽ�����
     */
    @Column(isNullable = false)
    private Boolean defaultUseAble = null;
    /**
     * ������־�������ڡ�
     */
    @Column
    private Integer logKeepMonth = null;
    /**
     * �˻���Ч������
     */
    @Column
    private Integer validDays = null;

    public Boolean getDefaultUseAble() {
        return defaultUseAble;
    }

    public void setDefaultUseAble(Boolean defaultUseAble) {
        this.defaultUseAble = defaultUseAble;
    }

    public Integer getLogKeepMonth() {
        return logKeepMonth;
    }

    public void setLogKeepMonth(Integer logKeepMonth) {
        this.logKeepMonth = logKeepMonth;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }
}
