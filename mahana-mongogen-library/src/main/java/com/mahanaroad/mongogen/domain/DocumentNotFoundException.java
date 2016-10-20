package com.mahanaroad.mongogen.domain;

import com.mahanaroad.mongogen.types.CollectionName;
import org.bson.conversions.Bson;


public class DocumentNotFoundException extends RuntimeException {


    private final CollectionName collectionName;
    private final Bson query;


    public DocumentNotFoundException(final CollectionName collectionName, final Bson query) {

        super("Unable to find document in collection [" + collectionName + "] with query " + query);

        this.collectionName = collectionName;
        this.query = query;

    }


    public CollectionName getCollectionName() {

        return this.collectionName;

    }


    public Bson getQuery() {

        return this.query;

    }


}
