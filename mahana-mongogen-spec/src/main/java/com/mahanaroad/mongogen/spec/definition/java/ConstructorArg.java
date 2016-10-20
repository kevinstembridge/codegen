package com.mahanaroad.mongogen.spec.definition.java;

import java.util.Optional;

public final class ConstructorArg implements Comparable<ConstructorArg> {

    private final ClassFieldDef classFieldDef;
    private final Optional<AnnotationDef> annotationDef;


    public ConstructorArg(final ClassFieldDef classFieldDef) {
        this(classFieldDef, Optional.empty());
    }


    public ConstructorArg(ClassFieldDef classFieldDef, Optional<AnnotationDef> annotationDef) {

        this.annotationDef = annotationDef;
        this.classFieldDef = classFieldDef;

    }


    public ClassFieldDef getClassFieldDef() {

        return this.classFieldDef;

    }


    public Optional<AnnotationDef> getAnnotationDef() {

        return this.annotationDef;

    }


    @Override
    public int compareTo(final ConstructorArg o) {

        return this.classFieldDef.compareTo(o.classFieldDef);

    }


}
