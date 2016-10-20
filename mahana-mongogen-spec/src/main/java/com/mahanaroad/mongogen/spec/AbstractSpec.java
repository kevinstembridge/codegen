package com.mahanaroad.mongogen.spec;

import com.mahanaroad.mongogen.persist.BsonCompatibleType;
import com.mahanaroad.mongogen.spec.definition.BooleanTypeDef;
import com.mahanaroad.mongogen.spec.definition.EntityDef;
import com.mahanaroad.mongogen.spec.definition.EntityHierarchy;
import com.mahanaroad.mongogen.spec.definition.EntityKey;
import com.mahanaroad.mongogen.spec.definition.EntityName;
import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.EnumValueDef;
import com.mahanaroad.mongogen.spec.definition.HtmlFormDef;
import com.mahanaroad.mongogen.spec.definition.HtmlFormName;
import com.mahanaroad.mongogen.spec.definition.IntTypeDef;
import com.mahanaroad.mongogen.spec.definition.LongTypeDef;
import com.mahanaroad.mongogen.spec.definition.ModelDef;
import com.mahanaroad.mongogen.spec.definition.ModelDefProvider;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;
import com.mahanaroad.mongogen.spec.definition.StringTypeDef;
import com.mahanaroad.mongogen.spec.definition.builders.BooleanTypeDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.EntityDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.EnumDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.FieldDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.HtmlFormDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.HtmlFormFieldDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.IntTypeDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.LongTypeDefBuilder;
import com.mahanaroad.mongogen.spec.definition.builders.StringTypeDefBuilder;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.NonPrimitiveType;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.spec.definition.java.ParameterizedType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;


public abstract class AbstractSpec implements ModelDefProvider {


    private final PackageName basePackage;
    private final List<HtmlFormDefBuilder> htmlFormDefBuilders = new LinkedList<>();
    private final List<EntityDefBuilder> entityDefBuilders = new LinkedList<>();
    private final List<EnumDefBuilder> enumDefBuilders = new LinkedList<>();
    private final List<StringTypeDefBuilder> stringTypeDefBuilders = new LinkedList<>();
    private final List<IntTypeDefBuilder> intTypeDefBuilders = new LinkedList<>();
    private final List<LongTypeDefBuilder> longTypeDefBuilders = new LinkedList<>();
    private final List<BooleanTypeDefBuilder> booleanTypeDefBuilders = new LinkedList<>();
    private final List<EntityHierarchy> rootEntityHierarchies = new LinkedList<>();
    private final Map<FieldType, ParameterizedType> fieldReadersByFieldType = new HashMap<>();
    private final Map<FieldType, ParameterizedType> fieldWritersByFieldType = new HashMap<>();


    private final Function<FieldType, Optional<ParameterizedType>> lookupFieldReaderByFieldType = (fieldType) -> Optional.ofNullable(this.fieldReadersByFieldType.get(fieldType));
    private final Function<FieldType, Optional<ParameterizedType>> lookupFieldWriterByFieldType = (fieldType) -> Optional.ofNullable(this.fieldWritersByFieldType.get(fieldType));


    protected AbstractSpec(final PackageName basePackage) {

        requireNonNull(basePackage, "basePackage");
        this.basePackage = basePackage;

    }


    @Override
    public ModelDef getModelDef() {

        populateEntityHierarchy();

        final ModelDef modelDef = new ModelDef(
                this.basePackage,
                this.rootEntityHierarchies,
                buildDtoDefs(),
                buildEnumDefs(),
                buildBooleanTypeDefs(),
                buildIntTypeDefs(),
                buildLongTypeDefs(),
                buildStringTypeDefs());
        //TODO run validators on ModelDef

        return modelDef;

    }


    private void populateEntityHierarchy() {

        final Set<EntityKey> allEntityKeys = new HashSet<>();

        for (EntityDefBuilder builder : this.entityDefBuilders) {

            final EntityDef entityDef = builder.build();

            boolean entityKeyExists = allEntityKeys.add(entityDef.getEntityKey()) == false;

            if (entityKeyExists) {
                throw new RuntimeException("Duplicate entity key: " + entityDef.getEntityKey());
            }

            addToEntityHierarchy(entityDef);

        }

    }


    private void addToEntityHierarchy(final EntityDef entityDef) {

        if (entityDef.isRootEntity()) {

            final EntityHierarchy entityHierarchy = new EntityHierarchy(entityDef);
            this.rootEntityHierarchies.add(entityHierarchy);

        } else {

            findEntityHierarchyFor(entityDef);

        }


    }


    private void findEntityHierarchyFor(final EntityDef entityDef) {

        for (EntityHierarchy entityHierarchy : this.rootEntityHierarchies) {

            if (entityHierarchy.addToHierarchyIfItBelongs(entityDef)) {
                return;
            }

        }

        throw new RuntimeException("Could not find an existing entity hierarchy for entity: " + entityDef.getEntityKey());

    }


    private List<HtmlFormDef> buildDtoDefs() {

        return this.htmlFormDefBuilders.stream().map(HtmlFormDefBuilder::build).collect(toList());

    }


    private List<EnumDef> buildEnumDefs() {

        return this.enumDefBuilders.stream().map(EnumDefBuilder::build).collect(toList());

    }


    private List<BooleanTypeDef> buildBooleanTypeDefs() {

        return this.booleanTypeDefBuilders.stream().map(BooleanTypeDefBuilder::build).collect(toList());

    }


    private List<IntTypeDef> buildIntTypeDefs() {

        return this.intTypeDefBuilders.stream().map(IntTypeDefBuilder::build).collect(toList());

    }


    private List<LongTypeDef> buildLongTypeDefs() {

        return this.longTypeDefBuilders.stream().map(LongTypeDefBuilder::build).collect(toList());

    }


    private List<StringTypeDef> buildStringTypeDefs() {

        return this.stringTypeDefBuilders.stream().map(StringTypeDefBuilder::build).collect(toList());

    }


    protected final void defaultFieldReader(final FieldType fieldType, final Class<?> fieldReaderClass) {

        requireNonNull(fieldType, "fieldType");
        requireNonNull(fieldReaderClass, "fieldReaderClass");

        this.fieldReadersByFieldType.put(fieldType, new ParameterizedType(Fqcn.valueOf(fieldReaderClass)));

    }


    protected final void defaultFieldReader(final FieldType fieldType, final Fqcn fieldReaderFqcn) {

        requireNonNull(fieldType, "fieldType");
        requireNonNull(fieldReaderFqcn, "fieldReaderFqcn");

        this.fieldReadersByFieldType.put(fieldType, new ParameterizedType(fieldReaderFqcn));

    }


    protected final void defaultFieldWriter(final FieldType fieldType, final Class<?> fieldWriterClass) {

        requireNonNull(fieldType, "fieldType");
        requireNonNull(fieldWriterClass, "fieldWriterClass");

        this.fieldWritersByFieldType.put(fieldType, new ParameterizedType(Fqcn.valueOf(fieldWriterClass)));

    }


    protected final void defaultFieldWriter(final FieldType fieldType, final Fqcn fieldWriterFqcn) {

        requireNonNull(fieldType, "fieldType");
        requireNonNull(fieldWriterFqcn, "fieldWriterFqcn");

        this.fieldWritersByFieldType.put(fieldType, new ParameterizedType(fieldWriterFqcn));

    }


    protected final EnumDefBuilder enumDef(final String subpackage, final String enumName) {

        final EnumDefBuilder builder = new EnumDefBuilder(this.basePackage, new PackageName(subpackage), enumName);
        this.enumDefBuilders.add(builder);
        return builder;

    }


    protected final StringTypeDefBuilder stringType(final String rawFqcn) {

        final StringTypeDefBuilder builder = new StringTypeDefBuilder(rawFqcn);
        this.stringTypeDefBuilders.add(builder);
        return builder;

    }


    protected final StringTypeDefBuilder stringType(final String subpackage, final String typeName) {

        final StringTypeDefBuilder builder = new StringTypeDefBuilder(this.basePackage, new PackageName(subpackage), typeName);
        this.stringTypeDefBuilders.add(builder);
        return builder;

    }


    protected final IntTypeDefBuilder intType(final String rawFqcn) {

        final IntTypeDefBuilder builder = new IntTypeDefBuilder(rawFqcn);
        this.intTypeDefBuilders.add(builder);
        return builder;

    }


    protected final IntTypeDefBuilder intType(final String subpackage, final String typeName) {

        final IntTypeDefBuilder builder = new IntTypeDefBuilder(this.basePackage, new PackageName(subpackage), typeName);
        this.intTypeDefBuilders.add(builder);
        return builder;

    }


    protected final LongTypeDefBuilder longType(final String rawFqcn) {

        final LongTypeDefBuilder builder = new LongTypeDefBuilder(rawFqcn);
        this.longTypeDefBuilders.add(builder);
        return builder;

    }


    protected final LongTypeDefBuilder longType(final String subpackage, final String typeName) {

        final LongTypeDefBuilder builder = new LongTypeDefBuilder(this.basePackage, new PackageName(subpackage), typeName);
        this.longTypeDefBuilders.add(builder);
        return builder;

    }


    protected final BooleanTypeDefBuilder booleanType(final String rawFqcn) {

        final BooleanTypeDefBuilder builder = new BooleanTypeDefBuilder(rawFqcn);
        this.booleanTypeDefBuilders.add(builder);
        return builder;

    }


    protected final BooleanTypeDefBuilder booleanType(final String subpackage, final String typeName) {

        final BooleanTypeDefBuilder builder = new BooleanTypeDefBuilder(this.basePackage, new PackageName(subpackage), typeName);
        this.booleanTypeDefBuilders.add(builder);
        return builder;

    }


    protected final EntityDefBuilder entity(final String subPackage, final String entityName) {

        final EntityDefBuilder builder = new EntityDefBuilder(this.basePackage, new PackageName(subPackage), new EntityName(entityName));
        this.entityDefBuilders.add(builder);
        return builder;

    }


    protected final HtmlFormDefBuilder htmlForm(final String subPackage, final String formName) {

        final HtmlFormDefBuilder builder = new HtmlFormDefBuilder(this.basePackage, new PackageName(subPackage), new HtmlFormName(formName));
        this.htmlFormDefBuilders.add(builder);
        return builder;

    }


    protected final HtmlFormFieldDefBuilder htmlFormField(final String fieldName, final String label, final FieldType fieldType) {

        return new HtmlFormFieldDefBuilder(new ClassFieldName(fieldName), label, fieldType);

    }


    protected final HtmlFormFieldDefBuilder htmlFormField(final String fieldName, final String label, final StringTypeDef stringTypeDef) {

        return new HtmlFormFieldDefBuilder(new ClassFieldName(fieldName), label, stringTypeDef);

    }


    protected final HtmlFormFieldDefBuilder htmlFormField(final String fieldName, final String label, final EnumDef enumDef) {

        return new HtmlFormFieldDefBuilder(new ClassFieldName(fieldName), label, enumDef);

    }


    protected final FieldDefBuilder field(final String fieldName, final FieldType fieldType) {

        return new FieldDefBuilder(
                new ClassFieldName(fieldName),
                fieldType,
                this.lookupFieldReaderByFieldType,
                this.lookupFieldWriterByFieldType);

    }


    protected final FieldDefBuilder field(final String fieldName, final ListFieldType listFieldType) {

        return new FieldDefBuilder(
                new ClassFieldName(fieldName),
                listFieldType.getFieldType(),
                this.lookupFieldReaderByFieldType,
                this.lookupFieldWriterByFieldType);

    }


    protected final FieldDefBuilder field(final String fieldName, final MapFieldType mapFieldType) {

        return new FieldDefBuilder(
                new ClassFieldName(fieldName),
                mapFieldType.getFieldType(),
                this.lookupFieldReaderByFieldType,
                this.lookupFieldWriterByFieldType);

    }


    protected final ListFieldType listOf(final FieldType fieldType) {

        return ListFieldType.of(fieldType);

    }


    protected final MapFieldType.Builder mapOfString() {

        return new MapFieldType.Builder(new ParameterizedType(Fqcn.STRING));

    }


    protected final MapFieldType.Builder mapOf(final StringTypeDef keyFieldType) {

        return new MapFieldType.Builder(keyFieldType.getParameterizedType());

    }


    protected final ListFieldType listOf(final EnumDef enumDef) {

        return ListFieldType.of(enumDef);

    }


    protected final ListFieldType listOf(final SimpleTypeDef simpleTypeDef) {

        return ListFieldType.of(simpleTypeDef);

    }


    protected final FieldDefBuilder field(final String fieldName, final EnumDef enumDef) {

        return new FieldDefBuilder(
                new ClassFieldName(fieldName),
                enumDef,
                this.lookupFieldReaderByFieldType,
                this.lookupFieldWriterByFieldType);

    }


    protected final FieldDefBuilder field(final String fieldName, final SimpleTypeDef simpleTypeDef) {

        return new FieldDefBuilder(
                new ClassFieldName(fieldName),
                simpleTypeDef,
                this.lookupFieldReaderByFieldType,
                this.lookupFieldWriterByFieldType);

    }


    protected final EnumValueDef enumValue(final String name) {

        return new EnumValueDef(name);

    }


    private static class ListFieldType {

        static ListFieldType of(final FieldType fieldType) {
            return new ListFieldType(fieldType);
        }


        static ListFieldType of(final EnumDef enumDef) {
            return new ListFieldType(enumDef.getFieldType());
        }


        static ListFieldType of(final SimpleTypeDef simpleTypeDef) {
            return new ListFieldType(simpleTypeDef.getFieldType());
        }


        private final FieldType fieldType;


        private ListFieldType(final FieldType listElementFieldType) {

            requireNonNull(listElementFieldType, "fieldType");


            final NonPrimitiveType listElementNonPrimitiveType = listElementFieldType.boxIfPrimitive().getNonPrimitiveType().orElseThrow(() -> new RuntimeException("Should never happen"));
            final ParameterizedType parameterizedType = new ParameterizedType(Fqcn.LIST, listElementNonPrimitiveType);
            final BsonCompatibleType bsonCompatibleType = listElementFieldType.getBsonCompatibleType().orElse(null);

            this.fieldType = FieldType.valueOf(parameterizedType, bsonCompatibleType);

        }


        public FieldType getFieldType() {

            return this.fieldType;

        }


    }


    private static class MapFieldType {

        private final NonPrimitiveType keyType;
        private final NonPrimitiveType valueType;


        private MapFieldType(final ParameterizedType keyType, final NonPrimitiveType valueType) {

            this.keyType = requireNonNull(keyType, "keyType");
            this.valueType = requireNonNull(valueType, "valueType");

        }


        public FieldType getFieldType() {

            return FieldType.valueOf(
                    new ParameterizedType(
                            Fqcn.MAP,
                            this.keyType,
                            this.valueType));

        }


        public static class Builder {

            private final ParameterizedType keyParameterizedType;


            private Builder(final ParameterizedType keyParameterizedType) {

                this.keyParameterizedType = keyParameterizedType;

            }


            public MapFieldType to(final Fqcn valueFqcn) {

                return new MapFieldType(this.keyParameterizedType, new ParameterizedType(valueFqcn));

            }


            public MapFieldType to(final SimpleTypeDef simpleTypeDef) {

                return new MapFieldType(this.keyParameterizedType, new ParameterizedType(simpleTypeDef.getFqcn(), true));

            }


        }


    }


}