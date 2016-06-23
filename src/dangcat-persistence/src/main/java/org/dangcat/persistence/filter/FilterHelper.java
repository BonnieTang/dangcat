package org.dangcat.persistence.filter;

import org.dangcat.persistence.model.Row;
import org.dangcat.persistence.model.TableStatementHelper;

import java.util.List;
import java.util.Map;

/**
 * �������ת�����ߡ�
 *
 * @author dangcat
 */
public class FilterHelper {
    /**
     * ��������������ĸ��������
     *
     * @param row            �����ж���
     * @param srcFilterGroup ��Դ�������
     * @return �������������������򷵻�������֣����򷵻�null��
     */
    public static Object checkGroup(Row row, FilterGroup srcFilterGroup) {
        if (srcFilterGroup != null && srcFilterGroup.getFilterExpressList().size() > 0) {
            for (FilterExpress filterExpress : srcFilterGroup.getFilterExpressList()) {
                FilterGroup filterGroup = (FilterGroup) filterExpress;
                if (filterGroup != null && filterGroup.isValid(row))
                    return filterGroup.getValue();
            }
        }
        return null;
    }

    /**
     * �ж��Ƿ����ת��CASE�����䡣
     *
     * @param srcFilterGroup ��Դ�������
     */
    private static boolean couldToCaseExpress(FilterGroup srcFilterGroup) {
        boolean result = false;
        if (srcFilterGroup != null && srcFilterGroup.getGroupType().equals(FilterGroupType.or)) {
            List<FilterExpress> filterExpressList = srcFilterGroup.getFilterExpressList();
            if (filterExpressList != null && filterExpressList.size() > 0) {
                result = true;
                // ������ȫ��Ϊ������ſ���ת�� CASE���ʽ
                for (FilterExpress filterExpress : filterExpressList) {
                    if (!(filterExpress instanceof FilterGroup)) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * �ж��Ƿ����ת��CASE��ELSE�����䡣
     *
     * @param filterGroup �������
     */
    private static boolean isElseGroup(FilterGroup filterGroup) {
        List<FilterExpress> filterExpressList = filterGroup.getFilterExpressList();
        if (filterExpressList != null && filterExpressList.size() > 0) {
            // ������ȫ��Ϊ���ԵĲſ���ת�� ELSE
            for (FilterExpress filterExpress : filterGroup.getFilterExpressList()) {
                if (filterExpress instanceof FilterUnit) {
                    FilterUnit filterUnit = (FilterUnit) filterExpress;
                    if (!filterUnit.getFilterType().equals(FilterType.ignore))
                        return false;
                } else if (filterExpress instanceof FilterGroup) {
                    if (!isElseGroup((FilterGroup) filterExpress))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * ��ȡ�����а������ֶ����ݡ�
     *
     * @param filterExpress ���˵�Ԫ��
     */
    public static void pickMap(FilterExpress filterExpress, Map<String, Object> params) {
        if (filterExpress instanceof FilterUnit) {
            FilterUnit filterUnit = (FilterUnit) filterExpress;
            params.put(filterUnit.getFieldName(), null);
        } else if (filterExpress instanceof FilterGroup) {
            FilterGroup filterGroup = (FilterGroup) filterExpress;
            for (FilterExpress childFilterExpress : filterGroup.getFilterExpressList())
                pickMap(childFilterExpress, params);
        }
    }

    /**
     * ת��CASE�����䡣
     *
     * @param srcFilterGroup ��Դ�������
     */
    public static String toCaseExpress(FilterGroup srcFilterGroup) {
        StringBuilder caseBuilder = new StringBuilder();
        if (couldToCaseExpress(srcFilterGroup) && srcFilterGroup.getFilterExpressList().size() > 0) {
            FilterGroup elseFilterGroup = null;
            for (FilterExpress filterExpress : srcFilterGroup.getFilterExpressList()) {
                FilterGroup filterGroup = (FilterGroup) filterExpress;
                // �������ELSE������¼������Ϊ�����롣
                if (isElseGroup(filterGroup))
                    elseFilterGroup = filterGroup;
                else {
                    String sql = filterGroup.toString();
                    if (sql != null && sql.length() > 0) {
                        if (caseBuilder.length() > 0)
                            caseBuilder.append("     ");
                        else
                            caseBuilder.append("CASE ");
                        caseBuilder.append("WHEN ");
                        caseBuilder.append(filterGroup);
                        caseBuilder.append(" THEN ");
                        caseBuilder.append(TableStatementHelper.toSqlString(filterGroup.getValue()));
                        caseBuilder.append("\n");
                    }
                }
            }
            if (caseBuilder.length() > 0) {
                if (elseFilterGroup != null) {
                    caseBuilder.append("     ELSE ");
                    caseBuilder.append(TableStatementHelper.toSqlString(elseFilterGroup.getValue()));
                }
                caseBuilder.append(" END");
            }
        }
        return caseBuilder.toString();
    }
}
