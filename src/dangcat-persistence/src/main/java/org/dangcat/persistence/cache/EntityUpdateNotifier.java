package org.dangcat.persistence.cache;

import java.util.Collection;

/**
 * ���ݸ���֪ͨ��
 */
public interface EntityUpdateNotifier {
    /**
     * ֪ͨ�������ݡ�
     *
     * @param tableName          ������
     * @param deletedPrimaryKeys ��ɾ�����������ϡ�
     * @param updateEntities     ���µ����ݡ�
     */
    void notifyUpdate(String tableName, Collection<Object> deletedPrimaryKeys, Collection<Object> updateEntities);
}
