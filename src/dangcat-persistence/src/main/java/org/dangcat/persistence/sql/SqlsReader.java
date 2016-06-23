package org.dangcat.persistence.sql;

import org.apache.log4j.Logger;
import org.dangcat.commons.database.DatabaseType;
import org.dangcat.commons.io.Resource;
import org.dangcat.commons.io.ResourceLoader;
import org.dangcat.persistence.xml.SqlsXmlResolver;
import org.dom4j.DocumentException;

/**
 * ��ѯ�����ļ���ȡ����
 *
 * @author dangcat
 */
public class SqlsReader {
    protected static final Logger logger = Logger.getLogger(SqlsReader.class);
    private Class<?> classType;
    private String namePrefix = null;
    private Sqls sqls = null;

    /**
     * ������ȡ����
     *
     * @param classType  ��Դ����λ�á�
     * @param namePrefix �ļ���ǰ׺��
     * @param sqls       ��ѯ��伯�ϡ�
     */
    public SqlsReader(Class<?> classType, String namePrefix, Sqls sqls) {
        this.classType = classType;
        this.namePrefix = namePrefix;
        this.sqls = sqls;
    }

    /**
     * �������ݿ����͡�
     *
     * @param name ��Դ������
     * @return ���ݿ����͡�
     */
    private DatabaseType getDatabaseType(String name) {
        String resourceName = name.toLowerCase();
        DatabaseType databaseType = null;
        for (DatabaseType defineDatabaseType : DatabaseType.values()) {
            if (resourceName.contains("_" + defineDatabaseType.name().toLowerCase() + ".")) {
                databaseType = defineDatabaseType;
                break;
            }
        }
        return databaseType;
    }

    /**
     * ����Դ�ļ���ȡ��ѯ���á�
     */
    public void read() {
        ResourceLoader resourceLoader = new ResourceLoader(this.classType, this.namePrefix, ".xml");
        try {
            resourceLoader.load();
            if (resourceLoader.getResourceList().size() == 0) {
                if (logger.isDebugEnabled())
                    logger.debug("No sql files is found.");
                return;
            }

            SqlsXmlResolver sqlsXmlResolver = new SqlsXmlResolver();
            sqlsXmlResolver.setResolveObject(this.sqls);
            for (Resource resource : resourceLoader.getResourceList()) {
                try {
                    logger.info("Load resource from " + resource + " by " + this.classType.getSimpleName());
                    sqlsXmlResolver.setDatabaseType(this.getDatabaseType(resource.getName()));
                    sqlsXmlResolver.open(resource.getInputStream());
                    sqlsXmlResolver.resolve();
                } catch (DocumentException e) {
                }
            }
        } finally {
            resourceLoader.close();
        }
    }
}
