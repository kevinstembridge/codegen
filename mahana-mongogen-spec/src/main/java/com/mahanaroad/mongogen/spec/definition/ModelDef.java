package com.mahanaroad.mongogen.spec.definition;


import com.mahanaroad.mongogen.spec.definition.java.PackageName;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ModelDef {


    private final PackageName basePackage;
    private final List<EntityHierarchy> rootEntityHierarchies = new LinkedList<>();
    private final List<HtmlFormDef> htmlFormDefs = new LinkedList<>();
    private final List<EnumDef> enumDefs = new LinkedList<>();
    private final List<BooleanTypeDef> booleanTypeDefs = new LinkedList<>();
    private final List<IntTypeDef> intTypeDefs = new LinkedList<>();
    private final List<LongTypeDef> longTypeDefs = new LinkedList<>();
    private final List<StringTypeDef> stringTypeDefs = new LinkedList<>();


    public ModelDef(
            final PackageName basePackage,
            final List<EntityHierarchy> rootEntityHierarchies,
            final List<HtmlFormDef> htmlFormDefs,
            final List<EnumDef> enumDefs,
            final List<BooleanTypeDef> booleanTypeDefs,
            final List<IntTypeDef> intTypeDefs,
            final List<LongTypeDef> longTypeDefs,
            final List<StringTypeDef> stringTypeDefs) {

        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(rootEntityHierarchies, "rootEntityHierarchies");
        Objects.requireNonNull(htmlFormDefs, "htmlFormDefs");
        Objects.requireNonNull(enumDefs, "enumDefs");
        Objects.requireNonNull(booleanTypeDefs, "booleanTypeDefs");
        Objects.requireNonNull(intTypeDefs, "intTypeDefs");
        Objects.requireNonNull(longTypeDefs, "longTypeDefs");
        Objects.requireNonNull(stringTypeDefs, "stringTypeDefs");

        this.basePackage = basePackage;

        this.rootEntityHierarchies.addAll(rootEntityHierarchies);
        this.htmlFormDefs.addAll(htmlFormDefs);
        this.enumDefs.addAll(enumDefs);
        this.booleanTypeDefs.addAll(booleanTypeDefs);
        this.intTypeDefs.addAll(intTypeDefs);
        this.longTypeDefs.addAll(longTypeDefs);
        this.stringTypeDefs.addAll(stringTypeDefs);

    }


    public Stream<EntityDef> entityDefStream() {

        return this.rootEntityHierarchies.stream().flatMap(EntityHierarchy::getEntityDefStream);

    }


    public Stream<EntityHierarchy> entityStream() {

        return this.rootEntityHierarchies.stream().flatMap(EntityHierarchy::getEntityHierarchyStream);

    }


    public Stream<HtmlFormDef> dtoDefStream() {

        return this.htmlFormDefs.stream();

    }


    public Stream<EnumDef> enumDefStream() {

        return this.enumDefs.stream();

    }


    public Stream<BooleanTypeDef> booleanTypeDefStream() {

        return this.booleanTypeDefs.stream();

    }


    public Stream<IntTypeDef> intTypeDefStream() {

        return this.intTypeDefs.stream();

    }


    public Stream<LongTypeDef> longTypeDefStream() {

        return this.longTypeDefs.stream();

    }


    public Stream<StringTypeDef> stringTypeDefStream() {

        return this.stringTypeDefs.stream();

    }


}
