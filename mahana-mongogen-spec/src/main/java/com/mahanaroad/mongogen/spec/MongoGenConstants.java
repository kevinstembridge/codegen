package com.mahanaroad.mongogen.spec;

import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;

public final class MongoGenConstants {


    public static final AnnotationDef JSON_CREATOR_ANNOTATION = new AnnotationDef(MongoGenFqcns.JACKSON_JSON_CREATOR);
    public static final AnnotationDef SPRING_AUTOWIRED_ANNOTATION = new AnnotationDef(MongoGenFqcns.SPRING_AUTOWIRED);
    public static final AnnotationDef SPRING_COMPONENT_ANNOTATION = new AnnotationDef(MongoGenFqcns.SPRING_COMPONENT);
    public static final AnnotationDef SPRING_REPOSITORY_ANNOTATION = new AnnotationDef(MongoGenFqcns.SPRING_REPOSITORY);
    public static final AnnotationDef VALIDATION_CONSTRAINT_EMAIL = new AnnotationDef(MongoGenFqcns.VALIDATOR_CONSTRAINT_EMAIL);
    public static final AnnotationDef VALIDATION_CONSTRAINT_NOT_EMPTY = new AnnotationDef(MongoGenFqcns.VALIDATOR_CONSTRAINT_NOT_EMPTY);
    public static final AnnotationDef VALIDATION_CONSTRAINT_NOT_NULL = new AnnotationDef(MongoGenFqcns.VALIDATOR_CONSTRAINT_NOT_NULL);

}
