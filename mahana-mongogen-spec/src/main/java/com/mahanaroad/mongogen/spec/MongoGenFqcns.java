package com.mahanaroad.mongogen.spec;

import com.mahanaroad.mongogen.spec.definition.java.Fqcn;

public final class MongoGenFqcns {

    public static final Fqcn ABSTRACT_DAO = Fqcn.valueOf("com.mahanaroad.mongogen.persist.AbstractEntityDao");
    public static final Fqcn BLANK_STRING_EXCEPTION = Fqcn.valueOf("com.mahanaroad.mongogen.BlankStringException");
    public static final Fqcn BOOLEAN_TYPE = Fqcn.valueOf("com.mahanaroad.mongogen.types.BooleanType");
    public static final Fqcn BSON = Fqcn.valueOf("org.bson.conversions.Bson");
    public static final Fqcn BSON_DOCUMENT = Fqcn.valueOf("org.bson.Document");
    public static final Fqcn COLLECTION_NAME = Fqcn.valueOf("com.mahanaroad.mongogen.types.CollectionName");
    public static final Fqcn INT_TYPE = Fqcn.valueOf("com.mahanaroad.mongogen.types.IntType");
    public static final Fqcn JACKSON_JSON_CREATOR = Fqcn.valueOf("com.fasterxml.jackson.annotation.JsonCreator");
    public static final Fqcn JACKSON_JSON_PROPERTY = Fqcn.valueOf("com.fasterxml.jackson.annotation.JsonProperty");
    public static final Fqcn LONG_TYPE = Fqcn.valueOf("com.mahanaroad.mongogen.types.LongType");
    public static final Fqcn MONGO_FILTERS = Fqcn.valueOf("com.mongodb.client.model.Filters;");
    public static final Fqcn MONGO_FIND_ONE_AND_UPDATE_OPTIONS = Fqcn.valueOf("com.mongodb.client.model.FindOneAndUpdateOptions");
    public static final Fqcn MONGO_RETURN_DOCUMENT = Fqcn.valueOf("com.mongodb.client.model.ReturnDocument");
    public static final Fqcn MONGOGEN_CLIENT_FACADE = Fqcn.valueOf("com.mahanaroad.mongogen.persist.MongoClientFacade");
    public static final Fqcn OBJECTS = Fqcn.valueOf("java.util.Objects");
    public static final Fqcn SPRING_AUTOWIRED = Fqcn.valueOf("org.springframework.beans.factory.annotation.Autowired");
    public static final Fqcn SPRING_COMPONENT = Fqcn.valueOf("org.springframework.stereotype.Component");
    public static final Fqcn SPRING_PAGE = Fqcn.valueOf("org.springframework.data.domain.Page");
    public static final Fqcn SPRING_PAGEABLE = Fqcn.valueOf("org.springframework.data.domain.Pageable");
    public static final Fqcn SPRING_REPOSITORY = Fqcn.valueOf("org.springframework.stereotype.Repository");
    public static final Fqcn STRING_TYPE = Fqcn.valueOf("com.mahanaroad.mongogen.types.StringType");
    public static final Fqcn VALIDATOR_CONSTRAINT_EMAIL = Fqcn.valueOf("org.hibernate.validator.constraints.Email");
    public static final Fqcn VALIDATOR_CONSTRAINT_NOT_EMPTY = Fqcn.valueOf("org.hibernate.validator.constraints.NotEmpty");
    public static final Fqcn VALIDATOR_CONSTRAINT_NOT_NULL = Fqcn.valueOf("javax.validation.constraints.NotNull");

}
