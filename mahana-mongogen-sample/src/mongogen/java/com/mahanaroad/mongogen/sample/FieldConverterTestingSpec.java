package com.mahanaroad.mongogen.sample;

import com.mahanaroad.mongogen.spec.AbstractSpec;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;

public class FieldConverterTestingSpec extends AbstractSpec {


    public FieldConverterTestingSpec() {

        super(new PackageName("com.mahanaroad.mongogen.sample"));

        defaultFieldReader(FieldType.STRING, Fqcn.valueOf("com.mahanaroad.mongogen.sample.fieldconverters.FieldConverterTestFieldTypeLevelFieldReader"));
        defaultFieldWriter(FieldType.STRING, Fqcn.valueOf("com.mahanaroad.mongogen.sample.fieldconverters.FieldConverterTestFieldTypeLevelFieldWriter"));

        entity("fieldconverters", "FieldConversion")
                .fields(
                        field("someStringWithFieldTypeLevelReader", FieldType.STRING).collectionFieldName("ft"),
                        field("someStringWithFieldLevelReader", FieldType.STRING)
                                .collectionFieldName("fl")
                                .fieldReader(Fqcn.valueOf("com.mahanaroad.mongogen.sample.fieldconverters.FieldConverterTestFieldLevelFieldReader"))
                                .fieldWriter(Fqcn.valueOf("com.mahanaroad.mongogen.sample.fieldconverters.FieldConverterTestFieldLevelFieldWriter"))
                ).build();

    }


}
