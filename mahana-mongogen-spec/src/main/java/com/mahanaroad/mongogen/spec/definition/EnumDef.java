package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.persist.BsonCompatibleType;
import com.mahanaroad.mongogen.spec.definition.java.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.mahanaroad.mongogen.spec.definition.builders.ClassDefBuilder.aClassDef;


public final class EnumDef {


    private final Fqcn fqcn;
    private final FieldType fieldType;
    private final ClassDef classDef;
    private final List<EnumValueDef> enumValueDefs = new LinkedList<>();


    public EnumDef(final Fqcn fqcn, final List<EnumValueDef> enumValueDefs) {

        Objects.requireNonNull(fqcn, "fqcn");
        Objects.requireNonNull(enumValueDefs, "enumValueDefs");

        this.fqcn = fqcn;
        this.enumValueDefs.addAll(enumValueDefs);
        this.classDef = aClassDef(new EnumType(fqcn)).ofType(ClassType.ENUM).build();
        this.fieldType = FieldType.valueOf(this.classDef.getNonPrimitiveType(), BsonCompatibleType.STRING);

    }


    public ClassDef getClassDef() {

        return this.classDef;

    }


    public Fqcn getFqcn() {

        return this.fqcn;

    }


    public FieldType getFieldType() {

        return this.fieldType;

    }


    public Stream<EnumValueDef> getEnumValuesStream() {

        return this.enumValueDefs.stream();

    }


    public Uqcn getUqcn() {

        return this.classDef.getUqcn();

    }


}
