package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;


public final class LongTypeDef extends SimpleTypeDef {


    public LongTypeDef(final Fqcn fqcn, boolean provided) {

        super(fqcn, MongoGenFqcns.LONG_TYPE, FieldType.LONG, provided);

    }


}
