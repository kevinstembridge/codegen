package com.mahanaroad.mongogen.spec.definition;


import com.mahanaroad.mongogen.spec.MongoGenConstants;
import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.ClassType;
import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.spec.definition.java.ParameterizedType;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;
import com.mahanaroad.mongogen.types.CollectionName;
import com.mahanaroad.mongogen.types.TypeDiscriminator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.mahanaroad.mongogen.spec.definition.builders.ClassDefBuilder.aClassDef;


public final class EntityDef {

    private static final EntityKey ABSTRACT_ENTITY_KEY = new EntityKey("ABSTRACT_ENTITY");


    public static final EntityDef ABSTRACT_ENTITY_DEF = initAbstractEntityDef();

    private static EntityDef initAbstractEntityDef() {

        final List<EntityFieldDef> abstractEntityFields = new LinkedList<>();

        final boolean modifiable = false;
        final boolean optional = false;
        final boolean unique = false;
        final boolean isMasked = false;
        final List<AnnotationDef> annotationDefs = Collections.emptyList();

        abstractEntityFields.add(new EntityFieldDef(
                new ClassFieldName("id"),
                new CollectionFieldName("_id"),
                FieldType.OBJECT_ID,
                Optional.empty(),
                Optional.empty(),
                modifiable,
                optional,
                unique,
                isMasked,
                annotationDefs,
                Optional.empty(),
                Optional.empty()));

        abstractEntityFields.add(new EntityFieldDef(
                new ClassFieldName("createdTimestampUtc"),
                new CollectionFieldName("c-ts"),
                FieldType.INSTANT,
                Optional.empty(),
                Optional.empty(),
                modifiable,
                optional,
                unique,
                isMasked,
                annotationDefs,
                Optional.empty(),
                Optional.empty()));

        abstractEntityFields.add(new EntityFieldDef(
                new ClassFieldName("lastModifiedTimestampUtc"),
                new CollectionFieldName("lm-ts"),
                FieldType.INSTANT,
                Optional.empty(),
                Optional.empty(),
                modifiable,
                true, // optional
                unique,
                isMasked,
                annotationDefs,
                Optional.empty(),
                Optional.empty()));

        return new EntityDef(
                ABSTRACT_ENTITY_KEY,
                new EntityName("Abstract"),
                Optional.empty(),
                new PackageName("com.mahanaroad.mongogen"),
                new PackageName("domain"),
                abstractEntityFields,
                Optional.empty(),
                true,
                Optional.empty(),
                Collections.emptyList());

    }


    public static EntityDef newInstance(
            final EntityKey entityKey,
            final EntityName entityName,
            final Optional<CollectionName> collectionNameOptional,
            final PackageName basePackage,
            final PackageName subPackage,
            final List<EntityFieldDef> entityFieldDefs,
            final Optional<EntityDef> superclassEntityDef,
            final boolean isAbstract,
            final Optional<TypeDiscriminator> typeDiscriminatorOptional,
            final List<IndexDef> indexDefs) {

        final Optional<EntityDef> superclassEntityDefOptional;

        if (superclassEntityDef.isPresent() == false) {
            superclassEntityDefOptional = Optional.of(ABSTRACT_ENTITY_DEF);
        } else {
            superclassEntityDefOptional = superclassEntityDef;
        }

        return new EntityDef(
                entityKey,
                entityName,
                collectionNameOptional,
                basePackage,
                subPackage,
                entityFieldDefs,
                superclassEntityDefOptional,
                isAbstract,
                typeDiscriminatorOptional,
                indexDefs);

    }


    private final EntityKey entityKey;
    private final EntityName entityName;
    private final CollectionName collectionName;
    private final ClassDef entityClassDef;
    private final ClassDef entityFilterClassDef;
    private final ClassDef entityFiltersClassDef;
    private final ClassDef entityUpdatersClassDef;
    private final ClassDef entityFieldConverterClassDef;
    private final ClassDef daoClassDef;
    private final Optional<EntityDef> superclassEntityDefOptional;
    private final boolean isAbstract;
    private final Optional<TypeDiscriminator> typeDiscriminatorOptional;
    private final List<IndexDef> indexDefs = new LinkedList<>();
    private final List<EntityFieldDef> entityFieldDefsNotInherited = new LinkedList<>();

    private EntityDef(
            final EntityKey entityKey,
            final EntityName entityName,
            final Optional<CollectionName> collectionNameOptional,
            final PackageName basePackage,
            final PackageName subPackage,
            final List<EntityFieldDef> entityFieldDefsNotInherited,
            final Optional<EntityDef> superclassEntityDefOptional,
            final boolean isAbstract,
            final Optional<TypeDiscriminator> typeDiscriminatorOptional,
            final List<IndexDef> indexDefs) {

        Objects.requireNonNull(entityKey, "entityKey");
        Objects.requireNonNull(entityName, "entityName");
        Objects.requireNonNull(collectionNameOptional, "collectionNameOptional");
        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(subPackage, "subPackage");
        Objects.requireNonNull(entityFieldDefsNotInherited, "entityFieldDefsNotInherited");
        Objects.requireNonNull(superclassEntityDefOptional, "superclassEntityDefOptional");
        Objects.requireNonNull(typeDiscriminatorOptional, "typeDiscriminatorOptional");
        Objects.requireNonNull(indexDefs, "indexDefs");

        this.entityKey = entityKey;
        this.entityName = entityName;
        this.superclassEntityDefOptional = superclassEntityDefOptional;
        this.collectionName = initCollectionName(collectionNameOptional);
        this.isAbstract = isAbstract;
        this.typeDiscriminatorOptional = typeDiscriminatorOptional;
        this.entityFieldDefsNotInherited.addAll(entityFieldDefsNotInherited);
        this.indexDefs.addAll(indexDefs);

        final Uqcn entityUqcn = new Uqcn(this.entityName + "Entity");
        final Uqcn entityFilterUqcn = entityUqcn.withSuffix("Filter");
        final Uqcn entityFiltersUqcn = entityUqcn.withSuffix("Filters");
        final Uqcn entityUpdaterUqcn = entityUqcn.withSuffix("Updater");
        final Uqcn entityFieldConverterUqcn = entityUqcn.withSuffix("FieldConverter");
        final Uqcn daoUqcn = new Uqcn(this.entityName + "Dao");

        final PackageName packageName = basePackage.plusSubPackage(subPackage);
        final Fqcn entityFqcn = packageName.uqcn(entityUqcn);
        final Fqcn entityFilterFqcn = packageName.uqcn(entityFilterUqcn);
        final Fqcn entityFiltersFqcn = packageName.uqcn(entityFiltersUqcn);
        final Fqcn entityUpdaterFqcn = packageName.uqcn(entityUpdaterUqcn);
        final Fqcn entityFieldConverterFqcn = packageName.uqcn(entityFieldConverterUqcn);
        final Fqcn daoFqcn = packageName.uqcn(daoUqcn);

        final Optional<ClassDef> superclassDefOptional = initSuperclassDef(superclassEntityDefOptional);
        this.entityClassDef = aClassDef(entityFqcn).withAbstract(isAbstract).withFieldDefsNotInherited(entityFieldDefsNotInherited).withSuperclass(superclassDefOptional).build();

        this.entityFilterClassDef = aClassDef(entityFilterFqcn).ofType(ClassType.INTERFACE).build();
        this.entityFiltersClassDef = aClassDef(entityFiltersFqcn).build();
        this.entityUpdatersClassDef = aClassDef(entityUpdaterFqcn).build();

        this.entityFieldConverterClassDef = aClassDef(entityFieldConverterFqcn).ofType(ClassType.INTERFACE).build();

        final ParameterizedType abstractDaoParameterizedType = new ParameterizedType(MongoGenFqcns.ABSTRACT_DAO, new ParameterizedType(entityFqcn));
        final ClassDef abstractDaoSuperclassDef = aClassDef(abstractDaoParameterizedType).build();
        this.daoClassDef = aClassDef(daoFqcn)
                .withClassAnnotation(MongoGenConstants.SPRING_REPOSITORY_ANNOTATION)
                .withConstructorAnnotation(MongoGenConstants.SPRING_AUTOWIRED_ANNOTATION)
                .withSuperclass(Optional.of(abstractDaoSuperclassDef))
                .build();

    }


    private CollectionName initCollectionName(final Optional<CollectionName> collectionNameOptional) {

        if (isRootEntity()) {

            return collectionNameOptional.orElse(new CollectionName(this.entityKey.firstToLower()));

        } else {

            return this.superclassEntityDefOptional.map(EntityDef::getCollectionName).orElse(collectionNameOptional.orElse(new CollectionName(this.entityKey.firstToLower())));

        }

    }


    private Optional<ClassDef> initSuperclassDef(final Optional<EntityDef> superclassEntityDef) {

        return superclassEntityDef.map(EntityDef::getEntityClassDef);

    }


    public ClassDef getEntityClassDef() {

        return this.entityClassDef;

    }


    public Stream<EntityFieldDef> getFieldsInheritedStream() {

        return this.superclassEntityDefOptional.map(EntityDef::getAllFieldsStream).orElse(Stream.empty()).sorted();

    }


    public Stream<EntityFieldDef> getFieldsNotInheritedStream() {

        return this.entityFieldDefsNotInherited.stream().sorted();

    }


    public Stream<EntityFieldDef> getAllFieldsStream() {

        return Stream.concat(this.entityFieldDefsNotInherited.stream(), getFieldsInheritedStream()).sorted();

    }


    public Uqcn getUqcn() {

        return this.entityClassDef.getUqcn();

    }


    public ClassDef getDaoClassDef() {

        return this.daoClassDef;

    }


    public CollectionName getCollectionName() {

        return this.collectionName;

    }


    public TypeDiscriminator getTypeDiscriminator() {

        return this.typeDiscriminatorOptional.orElseThrow(() -> new RuntimeException("Expected entity to have a type discriminator: " + this.entityKey));

    }


    public Optional<TypeDiscriminator> getTypeDiscriminatorOptional() {

        return this.typeDiscriminatorOptional;

    }


    public boolean isRootEntity() {

        return this.superclassEntityDefOptional.map(superclassEntityDef -> superclassEntityDef.getEntityKey().equals(ABSTRACT_ENTITY_KEY)).orElse(false);

    }


    public Optional<EntityDef> getSuperclassEntityDef() {

        return this.superclassEntityDefOptional;

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final EntityDef entityDef = (EntityDef) o;
        return Objects.equals(entityKey, entityDef.entityKey);

    }


    @Override
    public int hashCode() {

        return Objects.hash(entityKey);

    }


    @Override
    public String toString() {

        return "EntityDef{" + this.entityKey + "}";

    }


    public EntityKey getEntityKey() {

        return this.entityKey;

    }


    public boolean isSubclassOf(final EntityDef entityDef) {

        return this.superclassEntityDefOptional.map(superclassEntityDef -> superclassEntityDef.equals(entityDef)).orElse(false);

    }


    public boolean isConcrete() {

        return this.isAbstract == false;

    }


    public Stream<EntityFieldDef> getAllUniqueFieldsStream() {

        return getAllFieldsStream().filter(EntityFieldDef::isUnique);

    }


    public Stream<EntityFieldDef> getAllModifiableFieldDefStream() {
        return getAllFieldsStream().filter(EntityFieldDef::isModifiable);
    }


    public Stream<EntityFieldDef> getAllUnmodifiableFieldDefStream() {

        return getAllFieldsStream().filter(fieldDef -> fieldDef.isModifiable() == false);

    }


    public ClassDef getEntityFilterClassDef() {

        return this.entityFilterClassDef;

    }


    public ClassDef getEntityFiltersClassDef() {

        return this.entityFiltersClassDef;

    }


    public ClassDef getEntityUpdaterClassDef() {

        return this.entityUpdatersClassDef;

    }


    public ClassDef getEntityFieldConverterClassDef() {

        return this.entityFieldConverterClassDef;

    }


    public Stream<IndexDef> getIndexDefStream() {

        return this.indexDefs.stream();

    }


}