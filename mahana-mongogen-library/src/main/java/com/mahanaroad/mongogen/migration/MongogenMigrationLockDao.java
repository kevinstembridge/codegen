package com.mahanaroad.mongogen.migration;

import com.mahanaroad.mongogen.persist.MongoClientFacade;
import com.mahanaroad.mongogen.persist.MongoCollectionFacade;
import com.mahanaroad.mongogen.types.CollectionName;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Date;

@Component
public class MongogenMigrationLockDao {


    private static final CollectionName COLLECTION_NAME = new CollectionName("mongogenMigrationLock");
    private static final String UNIQUE_FIELD_NAME = "locked";
    private static final boolean UNIQUE_FIELD_VALUE = true;
    private static final String CREATED_TIMESTAMP_FIELD_NAME = "c-ts";
    private static final String HOSTNAME_FIELD_NAME = "hostname";
    private static final String PROCESS_NAME_FIELD_NAME = "processName";

    private final MongoCollectionFacade mongoCollectionFacade;


    @Autowired
    public MongogenMigrationLockDao(final MongoClientFacade mongoClientFacade) {

        this.mongoCollectionFacade = new MongoCollectionFacade(COLLECTION_NAME, mongoClientFacade);
        createIndex();

    }


    private void createIndex() {

        final int sortAscending = 1;
        final Document indexKeys = new Document(UNIQUE_FIELD_NAME, sortAscending);
        final IndexOptions indexOptions = new IndexOptions().unique(true).name("mongogenMigrationLock_idx");

        this.mongoCollectionFacade.createIndex(indexKeys, indexOptions);

    }


    public void obtainLock() throws MongogenMigrationLockNotAvailableException {

        final Document lockDocument = createLockDocument();

        try {
            this.mongoCollectionFacade.insert(lockDocument);
        } catch (MongoWriteException e) {

            final WriteError error = e.getError();
            final int errorCode = error.getCode();

            if (errorCode == 11000) {
                throwLockNotAvailableException();
            } else {
                throw e;
            }

        }

    }


    private Document createLockDocument() {

        final Document insertDocument = new Document();
        insertDocument.put(UNIQUE_FIELD_NAME, UNIQUE_FIELD_VALUE);
        insertDocument.put(CREATED_TIMESTAMP_FIELD_NAME, new Date());
        insertDocument.put(HOSTNAME_FIELD_NAME, getHostName());
        insertDocument.put(PROCESS_NAME_FIELD_NAME, getProcessName());
        return insertDocument;

    }


    private void throwLockNotAvailableException() throws MongogenMigrationLockNotAvailableException {

        final Document existingDocument = this.mongoCollectionFacade.findOne(new Document(UNIQUE_FIELD_NAME, UNIQUE_FIELD_VALUE));
        final Instant lockCreatedTimestampUtc = existingDocument.getDate(CREATED_TIMESTAMP_FIELD_NAME).toInstant();
        final String hostname = existingDocument.getString(HOSTNAME_FIELD_NAME);
        final String processName = existingDocument.getString(PROCESS_NAME_FIELD_NAME);
        throw new MongogenMigrationLockNotAvailableException(lockCreatedTimestampUtc, hostname, processName);

    }


    private String getHostName() {

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }

    }


    private String getProcessName() {

        return ManagementFactory.getRuntimeMXBean().getName();

    }


    public void releaseLock() {

        this.mongoCollectionFacade.delete(new Document(UNIQUE_FIELD_NAME, true));

    }


}
