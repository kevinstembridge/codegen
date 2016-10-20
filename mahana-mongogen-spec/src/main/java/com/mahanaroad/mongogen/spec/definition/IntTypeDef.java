package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;


public final class IntTypeDef extends SimpleTypeDef {


    public IntTypeDef(final Fqcn fqcn, boolean provided) {

        super(fqcn, MongoGenFqcns.INT_TYPE, FieldType.INT, provided);

    }


}
