package com.mahanaroad.mongogen;

import com.mahanaroad.mongogen.migration.MongogenMigrationLockDao;
import com.mahanaroad.mongogen.migration.MongogenMigrationLockNotAvailableException;
import com.mahanaroad.mongogen.persist.MongoClientFacade;
import com.mahanaroad.mongogen.types.CollectionName;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MongogenMigrationLockDaoTest extends AbstractIntegrationTest {


    @Autowired
    private MongogenMigrationLockDao dao;


    @Autowired
    private MongoClientFacade mongoClientFacade;


    @BeforeMethod
    public void beforeMethod() {

        this.mongoClientFacade.deleteMany(new CollectionName("mongogenMigrationLock"), new Document());

    }


    @Test
    public void testObtainAndReleaseLock() throws MongogenMigrationLockNotAvailableException {

        this.dao.obtainLock();

        try {
            this.dao.obtainLock();
            Assert.fail("Expected a MongogenMigrationLockNotAvailableException");
        } catch (MongogenMigrationLockNotAvailableException e) {
            //do nothing
        }

        this.dao.releaseLock();

        this.dao.obtainLock();

    }


}
