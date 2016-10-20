package com.mahanaroad.mongogen.spec.definition.builders;


import com.mahanaroad.mongogen.spec.definition.CollectionFieldName;
import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;
import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.ParameterizedType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;


public final class FieldDefBuilder {


    private final ClassFieldName classFieldName;
    private final FieldType fieldType;
    private CollectionFieldName collectionFieldName;
    private boolean modifiable = false;
    private boolean optional = false;
    private boolean unique = false;
    private boolean isMasked = false;
    private final Optional<SimpleTypeDef> simpleTypeDefOptional;
    private final Optional<EnumDef> enumDefOptional;
    private final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldReaderProvider;
    private final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldWriterProvider;
    private Optional<ParameterizedType> fieldReaderParameterizedTypeOptional = Optional.empty();
    private Optional<ParameterizedType> fieldWriterParameterizedTypeOptional = Optional.empty();
    private final List<AnnotationDef> annotationDefs = new ArrayList<>();


    public FieldDefBuilder(
            final ClassFieldName classFieldName,
            final EnumDef enumDef,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldReaderProvider,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldWriterProvider) {

        this(classFieldName, enumDef.getFieldType(), Optional.empty(), Optional.of(enumDef), defaultFieldTypeFieldReaderProvider, defaultFieldTypeFieldWriterProvider);

    }


    public FieldDefBuilder(
            final ClassFieldName classFieldName,
            final SimpleTypeDef simpleTypeDef,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldReaderProvider,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldWriterProvider) {

        this(classFieldName, simpleTypeDef.getFieldType(), Optional.of(simpleTypeDef), Optional.empty(), defaultFieldTypeFieldReaderProvider, defaultFieldTypeFieldWriterProvider);

    }


    public FieldDefBuilder(
            final ClassFieldName classFieldName,
            final FieldType fieldType,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldReaderProvider,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldWriterProvider) {

        this(classFieldName, fieldType, Optional.empty(), Optional.empty(), defaultFieldTypeFieldReaderProvider, defaultFieldTypeFieldWriterProvider);

    }


    private FieldDefBuilder(
            final ClassFieldName classFieldName,
            final FieldType fieldType,
            final Optional<SimpleTypeDef> simpleTypeDef,
            final Optional<EnumDef> enumDef,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldReaderProvider,
            final Function<FieldType, Optional<ParameterizedType>> defaultFieldTypeFieldWriterProvider) {

        requireNonNull(simpleTypeDef, "simpleTypeDef");

        this.classFieldName = requireNonNull(classFieldName, "classFieldName");
        this.fieldType = requireNonNull(fieldType, "fieldType");
        this.collectionFieldName = new CollectionFieldName(classFieldName.getValue());
        this.defaultFieldTypeFieldReaderProvider = requireNonNull(defaultFieldTypeFieldReaderProvider, "defaultFieldTypeFieldReaderProvider");
        this.defaultFieldTypeFieldWriterProvider = requireNonNull(defaultFieldTypeFieldWriterProvider, "defaultFieldTypeFieldWriterProvider");
        this.simpleTypeDefOptional = simpleTypeDef;
        this.enumDefOptional = enumDef;

    }


    public FieldDefBuilder modifiable() {

        this.modifiable = true;
        return this;

    }


    public FieldDefBuilder optional() {

        this.optional = true;
        return this;

    }


    public FieldDefBuilder unique() {

        this.unique = true;
        return this;

    }


    public EntityFieldDef build() {

        final Optional<ParameterizedType> fieldReaderClassName = getFieldReaderClassName();
        final Optional<ParameterizedType> fieldWriterClassName = getFieldWriterClassName();

        return new EntityFieldDef(
                this.classFieldName,
                this.collectionFieldName,
                this.fieldType,
                this.enumDefOptional,
                this.simpleTypeDefOptional,
                this.modifiable,
                this.optional,
                this.unique,
                this.isMasked,
                this.annotationDefs,
                fieldReaderClassName,
                fieldWriterClassName);

    }


    private Optional<ParameterizedType> getFieldReaderClassName() {

        if (this.fieldReaderParameterizedTypeOptional.isPresent()) {
            return this.fieldReaderParameterizedTypeOptional;
        }

        final Optional<ParameterizedType> fieldReaderFqcnOptional = this.defaultFieldTypeFieldReaderProvider.apply(this.fieldType);

        if (fieldReaderFqcnOptional.isPresent()) {
            return fieldReaderFqcnOptional;
        }

        return Optional.empty();

    }


    private Optional<ParameterizedType> getFieldWriterClassName() {

        if (this.fieldWriterParameterizedTypeOptional.isPresent()) {
            return this.fieldWriterParameterizedTypeOptional;
        }

        final Optional<ParameterizedType> fieldReaderFqcnOptional = this.defaultFieldTypeFieldWriterProvider.apply(this.fieldType);

        if (fieldReaderFqcnOptional.isPresent()) {
            return fieldReaderFqcnOptional;
        }

        return Optional.empty();

    }


    public FieldDefBuilder collectionFieldName(final String collectionFieldName) {

        this.collectionFieldName = new CollectionFieldName(collectionFieldName);
        return this;

    }


    public FieldDefBuilder fieldReader(final Fqcn fieldReaderFqcn) {

        return fieldReader(new ParameterizedType(fieldReaderFqcn));

    }


    public FieldDefBuilder fieldReader(final ParameterizedType fieldReaderParameterizedType) {

        requireNonNull(fieldReaderParameterizedType, "fieldReaderParameterizedType");
        this.fieldReaderParameterizedTypeOptional = Optional.of(fieldReaderParameterizedType);
        return this;

    }


    public FieldDefBuilder fieldWriter(final Fqcn fieldReaderFqcn) {

        return fieldWriter(new ParameterizedType(fieldReaderFqcn));

    }


    public FieldDefBuilder fieldWriter(final ParameterizedType fieldWriterParameterizedType) {

        requireNonNull(fieldWriterParameterizedType, "fieldWriterParameterizedType");
        this.fieldWriterParameterizedTypeOptional = Optional.of(fieldWriterParameterizedType);
        return this;

    }


    public FieldDefBuilder masked() {

        this.isMasked = true;
        return this;

    }


}
