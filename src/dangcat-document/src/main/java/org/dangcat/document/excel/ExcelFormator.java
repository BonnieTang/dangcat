package org.dangcat.document.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * ��ʽ���ĵ���
 */
public interface ExcelFormator
{
    /**
     * ��ʽ���ĵ���
     * @param workbook ��������
     * @param sheet ��ǩҳ��
     */
    void format(Workbook workbook, Sheet sheet);
}
