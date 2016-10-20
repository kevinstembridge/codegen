package com.mahanaroad.mongogen.spec.definition.builders;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.spec.definition.EntityDef;
import com.mahanaroad.mongogen.spec.definition.EntityKey;
import com.mahanaroad.mongogen.spec.definition.EntityName;
import com.mahanaroad.mongogen.spec.definition.IndexDef;
import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.types.CollectionName;
import com.mahanaroad.mongogen.types.TypeDiscriminator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public final class EntityDefBuilder {

    private final List<FieldDefBuilder> fieldDefBuilders = new LinkedList<>();
    private final List<IndexDefBuilder> indexDefBuilders = new LinkedList<>();
    private final PackageName basePackage;
    private final PackageName subPackage;
    private final EntityName entityName;
    private Optional<CollectionName> collectionNameOptional = Optional.empty();
    private boolean isAbstract;
    private Optional<EntityDef> superclassEntityDefOptional = Optional.empty();
    private Optional<TypeDiscriminator> typeDiscriminatorOptional = Optional.empty();
    private Optional<EntityKey> entityKeyOptional = Optional.empty();


    public EntityDefBuilder(
            final PackageName basePackage,
            final PackageName subPackage,
            final EntityName entityName) {

        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(subPackage, "subPackage");
        Objects.requireNonNull(entityName, "entityName");

        this.basePackage = basePackage;
        this.subPackage = subPackage;
        this.entityName = entityName;

    }


    public EntityDef build() {

        final List<EntityFieldDef> entityFieldDefs = buildFieldDefs();
        final List<IndexDef> indexDefs = buildIndexDefs(entityFieldDefs);

        return EntityDef.newInstance(
                this.entityKeyOptional.orElse(new EntityKey(this.entityName.getValue())),
                this.entityName,
                this.collectionNameOptional,
                this.basePackage,
                this.subPackage,
                entityFieldDefs,
                this.superclassEntityDefOptional,
                this.isAbstract,
                this.typeDiscriminatorOptional,
                indexDefs);

    }


    private List<EntityFieldDef> buildFieldDefs() {

        return this.fieldDefBuilders.stream().map(FieldDefBuilder::build).collect(toList());

    }


    private List<IndexDef> buildIndexDefs(final List<EntityFieldDef> entityFieldDefs) {



        return this.indexDefBuilders.stream().map(indexDefBuilder -> indexDefBuilder.build(entityFieldDefs)).collect(toList());

    }


    public EntityDefBuilder fields(final FieldDefBuilder... fieldDefBuilders) {

        if (fieldDefBuilders != null) {
            Collections.addAll(this.fieldDefBuilders, fieldDefBuilders);
        }

        return this;

    }


    public EntityDefBuilder superclass(final EntityDef superclassEntityDef) {

        Objects.requireNonNull(superclassEntityDef, "superclassEntityDefOptional");
        this.superclassEntityDefOptional = Optional.of(superclassEntityDef);
        return this;

    }


    public EntityDefBuilder collectionName(final String collectionName) {

        BlankStringException.throwIfBlank(collectionName, "collectionName");

        this.collectionNameOptional = Optional.of(new CollectionName(collectionName));
        return this;

    }


    public EntityDefBuilder entityKey(final String entityKey) {

        BlankStringException.throwIfBlank(entityKey, "entityKey");

        this.entityKeyOptional = Optional.of(new EntityKey(entityKey));
        return this;

    }


    public EntityDefBuilder isAbstract() {

        this.isAbstract = true;
        return this;

    }


    public EntityDefBuilder typeDiscriminator(final String typeDiscriminator) {

        this.typeDiscriminatorOptional = Optional.of(new TypeDiscriminator(typeDiscriminator));
        return this;

    }


    public IndexDefBuilder index() {

        final IndexDefBuilder indexDefBuilder = new IndexDefBuilder(this);
        this.indexDefBuilders.add(indexDefBuilder);
        return indexDefBuilder;

    }


    public static class IndexDefBuilder {


        private final EntityDefBuilder entityDefBuilder;
        private final List<String> fieldNames = new ArrayList<>();


        private IndexDefBuilder(EntityDefBuilder entityDefBuilder) {
            this.entityDefBuilder = entityDefBuilder;
        }


        public EntityDefBuilder and() {

            return this.entityDefBuilder;

        }


        public IndexDefBuilder withField(final String fieldName) {

            this.fieldNames.add(fieldName);
            return this;

        }


        private IndexDef build(final List<EntityFieldDef> entityFieldDefs) {

            final List<EntityFieldDef> entityFieldDefsForIndex = entityFieldDefs
                    .stream()
                    .filter(fieldDef -> this.fieldNames.contains(fieldDef.getClassFieldName().getValue()))
                    .collect(toList());

            return new IndexDef(entityFieldDefsForIndex);

        }


    }


}
