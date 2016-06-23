package org.dangcat.persistence.cache;

import org.dangcat.commons.utils.DateUtils;
import org.dangcat.persistence.domain.EntityData;
import org.dangcat.persistence.entity.EntityDataUtils;
import org.dangcat.persistence.exception.EntityException;
import org.dangcat.persistence.exception.TableException;
import org.dangcat.persistence.filter.FilterGroup;
import org.dangcat.persistence.filter.FilterGroupType;
import org.dangcat.persistence.filter.FilterType;
import org.dangcat.persistence.filter.FilterUnit;
import org.dangcat.persistence.simulate.SimulateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TestMemCache extends TestCacheBase {
    private void testCacheFilter1(MemCache<EntityData> memCache) {
        // ������򵥵Ĺ�������
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.add(new FilterUnit(EntityData.FieldA, FilterType.eq, "A-00000000000000000000000000000000000006"));
        // ��������
        Collection<EntityData> entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(1, entityDataList.size());
        EntityData entityData = entityDataList.iterator().next();
        Assert.assertEquals("A-00000000000000000000000000000000000006", entityData.getFieldA());
        Assert.assertTrue(filterGroup.isValid(entityData));

        // ����BETWEEN
        filterGroup.clear();
        // �����ֶ�FieldA�Ĺ�������
        filterGroup.add(new FilterUnit(EntityData.FieldA, FilterType.between, "A-00000000000000000000000000000000000001", "A-00000000000000000000000000000000000009"));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(9, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldB�Ĺ�������
        filterGroup.add(new FilterUnit(EntityData.FieldB, FilterType.between, 2, 9));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(8, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldC�Ĺ�������
        filterGroup.add(new FilterUnit(EntityData.FieldC, FilterType.between, 3 * 3.14, 9 * 3.14 + 0.01));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(7, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldD�Ĺ�������
        filterGroup.add(new FilterUnit(EntityData.FieldD, FilterType.between, (long) 4 * 100000000, (long) 9 * 100000000));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(6, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldE�Ĺ�������
        Date fromDay = DateUtils.clear(DateUtils.DAY, DateUtils.add(DateUtils.DAY, DateUtils.now(), 5));
        Date toDay = DateUtils.add(DateUtils.DAY, DateUtils.now(), 9);
        filterGroup.add(new FilterUnit(EntityData.FieldE, FilterType.between, fromDay, toDay));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(5, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // ���Ի�����㡣
        filterGroup.setGroupType(FilterGroupType.or);
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(9, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));
    }

    private void testCacheFilter2(MemCache<EntityData> memCache) {
        // ������򵥵Ĺ�������
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.add(new FilterUnit(EntityData.FieldA, FilterType.eq, "A-00000000000000000000000000000000000006", null, "A-00000000000000000000000000000000000009"));
        // ��������
        Collection<EntityData> entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(2, entityDataList.size());
        for (EntityData entityData : entityDataList) {
            if (entityData.getFieldB() == 6)
                Assert.assertEquals("A-00000000000000000000000000000000000006", entityData.getFieldA());
            else if (entityData.getFieldB() == 9)
                Assert.assertEquals("A-00000000000000000000000000000000000009", entityData.getFieldA());
            else
                Assert.assertEquals("A-00000000000000000000000000000000000009", entityData.getFieldA());
            Assert.assertTrue(filterGroup.isValid(entityData));
        }

        // ����BETWEEN
        filterGroup.clear();
        // �����ֶ�FieldA�Ĺ�������
        FilterUnit filterUnit = new FilterUnit(EntityData.FieldA, FilterType.between, "A-00000000000000000000000000000000000001", "A-00000000000000000000000000000000000009");
        filterUnit.setNot(true);
        filterGroup.add(filterUnit);
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(1, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // ����BETWEEN
        filterGroup.clear();
        // �����ֶ�FieldA�Ĺ�������
        filterGroup.add(new FilterUnit(EntityData.FieldA, FilterType.lt, "A-00000000000000000000000000000000000002"));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(2, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldB�Ĺ�������
        filterGroup.add(new FilterUnit(EntityData.FieldB, FilterType.between, 1, null));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(1, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldC�Ĺ�������
        filterGroup.clear();
        filterGroup.add(new FilterUnit(EntityData.FieldC, FilterType.between, null, 7 * 3.14 + 0.01));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(8, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldD�Ĺ�������
        filterGroup.add(new FilterUnit(EntityData.FieldD, FilterType.eq, (long) 4 * 100000000, (long) 5 * 100000000));
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(2, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // �����ֶ�FieldE�Ĺ�������
        Date fromDay = DateUtils.clear(DateUtils.SECOND, DateUtils.add(DateUtils.DAY, DateUtils.now(), 6));
        Date toDay = DateUtils.add(DateUtils.DAY, DateUtils.now(), 8);
        filterUnit = new FilterUnit(EntityData.FieldE, FilterType.between, fromDay, toDay);
        filterUnit.setNot(true);
        filterGroup.add(filterUnit);
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(2, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));

        // ���Ի�����㡣
        filterGroup.setGroupType(FilterGroupType.or);
        // ��������
        entityDataList = memCache.find(filterGroup);
        // �жϹ�������
        Assert.assertEquals(10, entityDataList.size());
        for (EntityData entityData1 : entityDataList)
            Assert.assertTrue(filterGroup.isValid(entityData1));
    }

    private void testCacheFilter3(MemCache<EntityData> memCache) {
        EntityData srcEntityData = EntityDataUtils.createEntityData(6);
        // ���Զ�λ���ݡ�
        Collection<EntityData> entityDataList = memCache.find(new String[]{EntityData.FieldA}, srcEntityData.getFieldA());
        Assert.assertEquals(1, entityDataList.size());
        Assert.assertTrue(SimulateUtils.compareData(srcEntityData, entityDataList.iterator().next()));

        // ����ɾ�����ݡ�
        memCache.remove(entityDataList.iterator().next());
        Collection<EntityData> entityDataList2 = memCache.find(new String[]{EntityData.FieldA}, srcEntityData.getFieldA());
        Assert.assertNull(entityDataList2);

        // ��������������ݡ�
        memCache.clear(true);
        Assert.assertEquals(0, memCache.size());
    }

    @Override
    protected void testDatabase(String databaseName) throws TableException, EntityException {
    }

    @Test
    public void testMemCache() {
        EntityCacheManager entityCacheManager = EntityCacheManager.getInstance();
        entityCacheManager.clear(true);
        MemCache<EntityData> memCache = entityCacheManager.getMemCache(EntityData.class);
        Assert.assertNotNull(memCache);

        // ��������ڴ����ݡ�
        List<EntityData> entityList = new ArrayList<EntityData>();
        EntityDataUtils.createEntityDataList(entityList, TEST_COUNT);
        for (EntityData entityData : entityList)
            memCache.add(entityData);
        Assert.assertEquals(TEST_COUNT, memCache.size());

        this.testCacheFilter1(memCache);
        this.testCacheFilter2(memCache);
        this.testCacheFilter3(memCache);
    }
}
