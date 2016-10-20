package com.mahanaroad.mongogen.migration;


import com.mahanaroad.mongogen.persist.MongoClientFacade;
import com.mahanaroad.mongogen.persist.MongoCollectionFacade;
import com.mahanaroad.mongogen.types.CollectionName;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class MongogenMigrationEntryDao {

    private static final CollectionName COLLECTION_NAME = new CollectionName("mongogenMigrationEntry");

    private final MongoCollectionFacade mongoCollectionFacade;


    @Autowired
    public MongogenMigrationEntryDao(final MongoClientFacade mongoClientFacade) {

        this.mongoCollectionFacade = new MongoCollectionFacade(COLLECTION_NAME, mongoClientFacade);

    }


    public boolean notExistsById(final MongogenMigrationId migrationId) {

        final Document query = new Document("migrationId", migrationId.getValue());
        return this.mongoCollectionFacade.count(query) == 0;

    }


    public void insert(final MongogenMigration migration) {

        final Document document = new Document();

        document.put("migrationId", migration.getId().getValue());
        migration.getChangeDescription().ifPresent(changeDescription -> document.put("description", changeDescription));
        document.put("ts", new Date());

        this.mongoCollectionFacade.insert(document);

    }


}
