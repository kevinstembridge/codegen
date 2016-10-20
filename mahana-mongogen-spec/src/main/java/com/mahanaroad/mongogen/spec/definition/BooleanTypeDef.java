package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;


public final class BooleanTypeDef extends SimpleTypeDef {


    public BooleanTypeDef(final Fqcn fqcn, boolean provided) {

        super(fqcn, MongoGenFqcns.BOOLEAN_TYPE, FieldType.BOOLEAN, provided);

    }


}
