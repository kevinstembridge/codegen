package com.mahanaroad.mongogen;


import com.mahanaroad.mongogen.types.CollectionName;
import org.bson.types.ObjectId;


public final class InvalidFieldException extends RuntimeException {


    private final ObjectId id;
    private final CollectionName collectionName;
    private final String collectionFieldName;
    private final String classFieldName;


    public InvalidFieldException(
            final ObjectId id,
            final CollectionName collectionName,
            final String collectionFieldName,
            final String classFieldName,
            final Throwable t) {

        super("Invalid value in field: collection = "
                + collectionName
                + ", collectionField = "
                + collectionFieldName
                + ", id = "
                + id
                + ", classField = "
                + classFieldName
                + ".",
                t);

        this.id = id;
        this.collectionName = collectionName;
        this.collectionFieldName = collectionFieldName;
        this.classFieldName = classFieldName;

    }


    public String getCollectionFieldName() {

        return this.collectionFieldName;

    }


    public ObjectId getId() {

        return this.id;

    }


    public CollectionName getCollectionName() {

        return this.collectionName;

    }


    public String getClassFieldName() {

        return this.classFieldName;

    }


}
