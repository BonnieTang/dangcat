package org.dangcat.persistence.orm;

import java.util.Map;

import org.dangcat.persistence.entity.SqlBuilderBase;
import org.dangcat.persistence.model.Column;
import org.dangcat.persistence.model.Range;

/**
 * ��׼���ʽ��������
 * @author dangcat
 * 
 */
public interface SqlSyntaxHelper
{
    /**
     * ����������ʽ��
     * @param sqlBuilder ���ݿ�����
     * @param sqlBuilderBase ������
     */
    public void buildCreateStatement(SqlBuilder sqlBuilder, SqlBuilderBase sqlBuilderBase);

    /**
     * ��������ڱ��ʽ��
     * @param databaseName ���ݿ�����
     * @param tableName ������
     * @return ��ѯ��䡣
     */
    public String buildExistsStatement(String databaseName, String tableName);

    /**
     * �������з�Χ����������ʽ��
     * @param sql ��ѯ��䡣
     * @param range ��ѯ��Χ��
     * @return ����ı����䡣
     */
    public String buildRangeLoadStatement(String sql, Range range);

    /**
     * ����Ĭ�ϵĲ������á�
     */
    public void createDefaultParams(Map<String, String> params);

    /**
     * ת��SQL��λ�������͡�
     * @param column ��λ����
     * @return SQL�������͡�
     */
    public String getSqlType(Column column);
}
