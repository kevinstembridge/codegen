package com.mahanaroad.mongogen.spec.definition.java;

import com.mahanaroad.mongogen.spec.definition.CollectionFieldName;
import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public final class EntityFieldDef extends ClassFieldDef {


    private final CollectionFieldName collectionFieldName;
    private final boolean unique;
    private final Optional<ParameterizedType> fieldReaderParameterizedType;
    private final Optional<ParameterizedType> fieldWriterParameterizedType;


    public EntityFieldDef(
            final ClassFieldName classFieldName,
            final CollectionFieldName collectionFieldName,
            final FieldType fieldType,
            final Optional<EnumDef> enumDef,
            final Optional<SimpleTypeDef> simpleTypeDef,
            final boolean modifiable,
            final boolean optional,
            final boolean unique,
            final boolean isMasked,
            final List<AnnotationDef> annotationDefs,
            final Optional<ParameterizedType> fieldReaderParameterizedType,
            final Optional<ParameterizedType> fieldWriterParameterizedType) {

        super(
                classFieldName,
                fieldType,
                enumDef,
                simpleTypeDef,
                modifiable,
                optional,
                isMasked,
                annotationDefs,
                Collections.emptySet());

        this.collectionFieldName = Objects.requireNonNull(collectionFieldName, "collectionFieldName");
        this.unique = unique;
        this.fieldReaderParameterizedType = Objects.requireNonNull(fieldReaderParameterizedType, "fieldReaderParameterizedType");
        this.fieldWriterParameterizedType = Objects.requireNonNull(fieldWriterParameterizedType, "fieldWriterParameterizedType");

    }


    public boolean isUnique() {

        return this.unique;

    }


    public CollectionFieldName getCollectionFieldName() {

        return this.collectionFieldName;

    }



    public Optional<ParameterizedType> getFieldReaderParameterizedType() {

        return this.fieldReaderParameterizedType;

    }


    public Optional<ParameterizedType> getFieldWriterParameterizedType() {

        return this.fieldWriterParameterizedType;

    }


    public Optional<ClassFieldDef> getFieldReaderClassField() {

        return getFieldReaderParameterizedType().map(readerType -> {
            final ClassFieldName classFieldName = getCombinedClassAndCollectionFieldName().withSuffix("_FieldReader");
            final FieldType fieldType = FieldType.valueOf(readerType);
            return aClassField(classFieldName, fieldType).build();
        });

    }


    public Optional<ClassFieldDef> getFieldWriterClassField() {

        return getFieldWriterParameterizedType().map(writerType -> {
            final ClassFieldName classFieldName = getCombinedClassAndCollectionFieldName().withSuffix("_FieldWriter");
            final FieldType fieldType = FieldType.valueOf(writerType);
            return aClassField(classFieldName, fieldType).build();
        });

    }


    private ClassFieldName getCombinedClassAndCollectionFieldName() {

        return getClassFieldName().withSuffix("_" + this.collectionFieldName.toValidJavaIdentifier());

    }


}
