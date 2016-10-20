package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;

import static java.util.Objects.requireNonNull;


public final class StringTypeDef extends SimpleTypeDef {


    private final CaseMode caseMode;


    public StringTypeDef(final Fqcn fqcn, boolean provided, final CaseMode caseMode) {

        super(fqcn, MongoGenFqcns.STRING_TYPE, FieldType.STRING, provided);

        this.caseMode = requireNonNull(caseMode, "caseMode");

    }


    public CaseMode getCaseMode() {

        return this.caseMode;

    }


    public enum CaseMode {

        ALWAYS_UPPER,
        ALWAYS_LOWER,
        AS_PROVIDED

    }


}
