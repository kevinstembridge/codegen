package com.mahanaroad.mongogen;


import com.mahanaroad.mongogen.types.CollectionName;
import org.bson.types.ObjectId;

import java.util.Date;


public final class MissingFieldException extends RuntimeException {


    public static void throwIfNull(final Date fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName) {

        throwIfNull(fieldValue, id, collectionFieldName, classFieldName, collectionName, Date.class);

    }


    public static void throwIfNull(final ObjectId fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName) {

        throwIfNull(fieldValue, id, collectionFieldName, classFieldName, collectionName, Date.class);

    }


    public static void throwIfNull(final Boolean fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName) {

        throwIfNull(fieldValue, id, collectionFieldName, classFieldName, collectionName, boolean.class);

    }


    public static void throwIfNull(final Integer fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName) {

        throwIfNull(fieldValue, id, collectionFieldName, classFieldName, collectionName, int.class);

    }


    public static void throwIfNull(final Long fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName) {

        throwIfNull(fieldValue, id, collectionFieldName, classFieldName, collectionName, long.class);

    }


    public static void throwIfNull(final Double fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName) {

        throwIfNull(fieldValue, id, collectionFieldName, classFieldName, collectionName, double.class);

    }


    public static void throwIfNull(final Object fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName, final Class<?> fieldClass) {

        if (fieldValue == null) {
            throw new MissingFieldException(id, collectionName, collectionFieldName, classFieldName, fieldClass);
        }

    }


    public static void throwIfBlank(final String fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName) {

        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            throw new MissingFieldException(id, collectionName, collectionFieldName, classFieldName, String.class);
        }

    }


    private static void throwIfNull(final Double fieldValue, final ObjectId id, final String collectionFieldName, final String classFieldName, final CollectionName collectionName, Class<?> fieldClass) {

        if (fieldValue == null) {
            throw new MissingFieldException(id, collectionName, collectionFieldName, classFieldName, fieldClass);
        }

    }


    private final ObjectId id;
    private final CollectionName collectionName;
    private final String collectionFieldName;
    private final String classFieldName;
    private final Class<?> fieldClass;


    public MissingFieldException(final ObjectId id, final CollectionName collectionName, final String collectionFieldName, final String classFieldName, final Class<?> fieldClass) {

        super("Missing a value for required field: collection = "
                + collectionName
                + ", collectionField = "
                + collectionFieldName
                + ", type = "
                + fieldClass.getName()
                + ", id = "
                + id
                + ", classField = "
                + classFieldName
                + ".");

        this.id = id;
        this.collectionName = collectionName;
        this.collectionFieldName = collectionFieldName;
        this.fieldClass = fieldClass;
        this.classFieldName = classFieldName;

    }


    public String getCollectionFieldName() {

        return this.collectionFieldName;

    }


    public ObjectId getId() {

        return this.id;

    }


    public Class<?> getFieldClass() {

        return this.fieldClass;

    }


    public CollectionName getCollectionName() {

        return this.collectionName;

    }


    public String getClassFieldName() {

        return this.classFieldName;

    }


}
