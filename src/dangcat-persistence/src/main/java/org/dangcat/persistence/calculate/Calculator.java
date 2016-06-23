package org.dangcat.persistence.calculate;

import java.util.Collection;

/**
 * �������ӿڡ�
 *
 * @author dangcat
 */
public interface Calculator {
    /**
     * ����ʵ�弯�ϡ�
     *
     * @param entityCollection ʵ����󼯺ϡ�
     */
    void calculate(Collection<?> entityCollection);

    /**
     * ���㵥��ʵ�塣
     *
     * @param entity ʵ�����
     */
    void calculate(Object entity);
}
