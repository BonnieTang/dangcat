package org.dangcat.persistence.orm;

import org.dangcat.persistence.entity.SqlBuilderBase;
import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Range;

import java.util.Map;

/**
 * ��׼���ʽ��������
 *
 * @author dangcat
 */
public interface SqlSyntaxHelper {
    /**
     * ����������ʽ��
     *
     * @param sqlBuilder     ���ݿ�����
     * @param sqlBuilderBase ������
     */
    void buildCreateStatement(SqlBuilder sqlBuilder, SqlBuilderBase sqlBuilderBase);

    /**
     * ��������ڱ��ʽ��
     *
     * @param databaseName ���ݿ�����
     * @param tableName    ������
     * @return ��ѯ��䡣
     */
    String buildExistsStatement(String databaseName, String tableName);

    /**
     * �������з�Χ����������ʽ��
     *
     * @param sql   ��ѯ��䡣
     * @param range ��ѯ��Χ��
     * @return ����ı����䡣
     */
    String buildRangeLoadStatement(String sql, Range range);

    /**
     * ����Ĭ�ϵĲ������á�
     */
    void createDefaultParams(Map<String, String> params);

    /**
     * ת��SQL��λ�������͡�
     *
     * @param column ��λ����
     * @return SQL�������͡�
     */
    String getSqlType(Column column);
}
