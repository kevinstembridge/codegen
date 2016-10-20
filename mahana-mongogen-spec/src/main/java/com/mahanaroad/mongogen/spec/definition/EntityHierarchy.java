package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class EntityHierarchy {

    private final EntityDef entityDef;
    private final List<EntityHierarchy> children = new LinkedList<>();


    public EntityHierarchy(final EntityDef entityDef) {

        Objects.requireNonNull(entityDef, "entityDef");
        this.entityDef = entityDef;

    }


    public EntityDef getEntityDef() {

        return this.entityDef;

    }


    public boolean addToHierarchyIfItBelongs(final EntityDef subclassEntityDef) {

        if (subclassEntityDef.isSubclassOf(this.entityDef)) {
            this.children.add(new EntityHierarchy(subclassEntityDef));
            return true;
        }

        for (EntityHierarchy child : this.children) {

            if (child.addToHierarchyIfItBelongs(subclassEntityDef)) {
                return true;
            }

        }

        return false;

    }


    public Stream<EntityDef> getEntityDefStream() {

        final Stream<EntityDef> childEntityStream = this.children.stream().map(EntityHierarchy::getEntityDefStream).flatMap(entityDefStream -> entityDefStream);
        return Stream.concat(childEntityStream, Stream.of(this.entityDef));

    }


    public Stream<EntityHierarchy> getEntityHierarchyStream() {

        final Stream<EntityHierarchy> childEntityStream = this.children.stream().map(EntityHierarchy::getEntityHierarchyStream).flatMap(entityHierarchyStream -> entityHierarchyStream);
        return Stream.concat(Stream.of(this), childEntityStream);

    }


    public Stream<EntityFieldDef> getAllFieldDefStream() {

        final List<EntityFieldDef> entityFieldDefs = new LinkedList<>();
        final Consumer<EntityFieldDef> fieldDefConsumer = entityFieldDefs::add;
        collectSubclassFields(fieldDefConsumer);

        return Stream.concat(this.entityDef.getAllFieldsStream(), entityFieldDefs.stream());

    }


    private void collectSubclassFields(final Consumer<EntityFieldDef> subclassFieldConsumer) {

        this.children.forEach(child -> {
            child.getEntityDef().getFieldsNotInheritedStream().forEach(subclassFieldConsumer::accept);
            child.collectSubclassFields(subclassFieldConsumer);
        });

    }


    public boolean hasSubclasses() {

        return this.children.isEmpty() == false;

    }


    public Stream<EntityDef> getConcreteEntityDefStream() {

        return getEntityDefStream().filter(EntityDef::isConcrete);

    }


    public boolean requiresTypeDiscriminatorFieldReader() {

        return hasSubclasses();

    }


}
