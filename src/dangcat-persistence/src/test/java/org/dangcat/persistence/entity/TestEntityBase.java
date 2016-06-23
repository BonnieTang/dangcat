package org.dangcat.persistence.entity;

import org.apache.log4j.Logger;
import org.dangcat.framework.pool.SessionException;
import org.dangcat.persistence.TestDatabase;
import org.dangcat.persistence.model.TableDataUtils;
import org.dangcat.persistence.simulate.SimulateUtils;
import org.junit.BeforeClass;

import java.io.IOException;

public abstract class TestEntityBase extends TestDatabase
{
    protected Logger logger = Logger.getLogger(this.getClass());

    @BeforeClass
    public static void setUpBeforeClass() throws IOException, SessionException
    {
        SimulateUtils.configure();
        EntityDataUtils.createEntitySimulator();
        TableDataUtils.createTableSimulator();
    }

    protected EntityManager getEntityManager()
    {
        return EntityManagerFactory.getInstance().open();
    }
}
