package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.persist.BsonCompatibleType;
import com.mahanaroad.mongogen.spec.definition.builders.ClassDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.FieldDefBuilder;
import com.mahanaroad.mongogen.spec.definition.java.ClassDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.ParameterizedType;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;


public abstract class SimpleTypeDef {


    private final Fqcn fqcn;
    private final FieldType fieldType;
    private final ClassDef classDef;
    private final FieldType superTypeFieldType;
    private final boolean provided;


    SimpleTypeDef(final Fqcn fqcn, final Fqcn superTypeFqcn, final FieldType superTypeFieldType, boolean provided) {

        requireNonNull(fqcn, "fqcn");
        requireNonNull(superTypeFqcn, "superTypeFqcn");
        requireNonNull(superTypeFieldType, "superTypeFieldType");

        final Optional<ClassDef> superclassDefOptional = initSuperClassDef(fqcn, superTypeFqcn, superTypeFieldType);

        this.fqcn = fqcn;
        this.superTypeFieldType = superTypeFieldType;
        this.classDef = ClassDefBuilder.aClassDef(new ParameterizedType(fqcn, true)).withSuperclass(superclassDefOptional).build();
        this.provided = provided;

        final BsonCompatibleType bsonCompatibleType = this.superTypeFieldType.getBsonCompatibleType().orElseThrow(() -> new IllegalArgumentException("Supertype is expected to have an associated BsonCompatibleType"));

        this.fieldType = FieldType.valueOf(this.classDef.getNonPrimitiveType(), bsonCompatibleType);

    }


    private Optional<ClassDef> initSuperClassDef(final Fqcn fqcn, final Fqcn superTypeFqcn, final FieldType superTypeFieldType) {

        final ParameterizedType superclassParameterizedType = new ParameterizedType(superTypeFqcn, true, new ParameterizedType(fqcn, true));
        final EntityFieldDef valueEntityFieldDef = new FieldDefBuilder(
                new ClassFieldName("value"),
                superTypeFieldType,
                fieldType -> Optional.of(new ParameterizedType(Fqcn.BOOLEAN)),
                fieldType -> Optional.of(new ParameterizedType(Fqcn.BOOLEAN)))
                .build();

        final ClassDef superclassDef = ClassDefBuilder.aClassDef(superclassParameterizedType).withFieldDefsNotInherited(singletonList(valueEntityFieldDef)).build();
        return Optional.of(superclassDef);

    }


    public final ClassDef getClassDef() {

        return this.classDef;

    }


    public final Uqcn getUqcn() {

        return this.classDef.getUqcn();

    }


    public FieldType getFieldType() {

        return this.fieldType;

    }


    public final boolean isProvided() {

        return this.provided;

    }


    public final boolean isNotProvided() {

        return this.provided == false;

    }


    public final FieldType getSuperTypeFieldType() {

        return this.superTypeFieldType;

    }


    public Fqcn getFqcn() {
        return this.fqcn;
    }


    public ParameterizedType getParameterizedType() {

        return new ParameterizedType(this.fqcn, true);

    }


}
