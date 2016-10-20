package com.mahanaroad.mongogen.spec.definition.validation;

import com.mahanaroad.mongogen.spec.MongoGenConstants;
import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;

public enum ValidationConstraint {


    EMAIL(MongoGenConstants.VALIDATION_CONSTRAINT_EMAIL),
    NOT_EMPTY(MongoGenConstants.VALIDATION_CONSTRAINT_NOT_EMPTY),
    NOT_NULL(MongoGenConstants.VALIDATION_CONSTRAINT_NOT_NULL);

    private final AnnotationDef associatedAnnotationDef;


    ValidationConstraint(AnnotationDef associatedAnnotationDef) {

        this.associatedAnnotationDef = associatedAnnotationDef;

    }


    public AnnotationDef getAssociatedAnnotationDef() {

        return this.associatedAnnotationDef;

    }


}
