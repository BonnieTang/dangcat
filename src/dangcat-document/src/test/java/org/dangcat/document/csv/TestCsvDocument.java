package org.dangcat.document.csv;

import junit.framework.Assert;
import org.dangcat.document.EntityData;
import org.dangcat.document.EntityDataUtils;
import org.dangcat.document.TableDataUtils;
import org.dangcat.document.TestEntityBase;
import org.dangcat.persistence.entity.EntityDataReader;
import org.dangcat.persistence.entity.EntityDataWriter;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.exception.TableException;
import org.dangcat.persistence.model.Table;
import org.dangcat.persistence.model.TableDataReader;
import org.dangcat.persistence.model.TableDataWriter;
import org.dangcat.persistence.model.TableUtils;
import org.dangcat.persistence.simulate.SimulateUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TestCsvDocument extends TestEntityBase {
    private static final int TEST_COUNT = 100;

    @Test
    public void testTextForEntity() throws IOException, EntityException {
        List<EntityData> srcEntityDataList = new LinkedList<EntityData>();
        EntityDataUtils.createEntityDataList(srcEntityDataList, TEST_COUNT);
        File file = File.createTempFile("Entity", ".txt");
        CsvDocument srcCsvDocument = new CsvDocument();
        srcCsvDocument.write(file, new EntityDataReader<EntityData>(srcEntityDataList));

        List<EntityData> dstEntityDataList = new LinkedList<EntityData>();
        CsvDocument dstCsvDocument = new CsvDocument();
        dstCsvDocument.read(file, new EntityDataWriter<EntityData>(dstEntityDataList, EntityData.class));
        Assert.assertTrue(SimulateUtils.compareDataCollection(srcEntityDataList, dstEntityDataList));
        file.delete();
    }

    @Test
    public void testTextForTable() throws IOException, TableException {
        Table srcTable = TableDataUtils.getTable();
        TableDataUtils.createTableData(srcTable, TEST_COUNT);
        File file = File.createTempFile("Table", ".txt");
        CsvDocument srcCsvDocument = new CsvDocument();
        srcCsvDocument.write(file, new TableDataReader(srcTable));

        Table dstTable = TableDataUtils.getTable();
        CsvDocument dstCsvDocument = new CsvDocument();
        dstCsvDocument.read(file, new TableDataWriter(dstTable));
        Assert.assertTrue(TableUtils.equalsContent(srcTable, dstTable));
        file.delete();
    }
}
