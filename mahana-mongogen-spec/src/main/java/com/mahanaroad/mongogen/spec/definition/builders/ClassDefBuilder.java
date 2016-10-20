package com.mahanaroad.mongogen.spec.definition.builders;

import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassType;
import com.mahanaroad.mongogen.spec.definition.java.ClassVisibility;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.NonPrimitiveType;
import com.mahanaroad.mongogen.spec.definition.java.ParameterizedType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ClassDefBuilder {


    private final NonPrimitiveType nonPrimitiveType;
    private ClassType classType = ClassType.CLASS;
    private ClassVisibility classVisibility = ClassVisibility.PUBLIC;
    private final List<ClassFieldDef> fieldDefsNotInherited = new LinkedList<>();
    private final List<AnnotationDef> classAnnotations = new LinkedList<>();
    private final List<AnnotationDef> constructorAnnotations = new LinkedList<>();
    private final List<ParameterizedType> interfacesImplemented = new LinkedList<>();
    private Optional<ClassDef> superclassDefOptional = Optional.empty();
    private boolean isAbstract;


    public static ClassDefBuilder aClassDef(final Fqcn fqcn) {

        return new ClassDefBuilder(new ParameterizedType(fqcn));

    }


    public static ClassDefBuilder aClassDef(final NonPrimitiveType nonPrimitiveType) {

        return new ClassDefBuilder(nonPrimitiveType);

    }


    private ClassDefBuilder(final NonPrimitiveType nonPrimitiveType) {

        Objects.requireNonNull(nonPrimitiveType, "nonPrimitiveType");
        this.nonPrimitiveType = nonPrimitiveType;

    }


    public ClassDef build() {

        return new ClassDef(
                this.nonPrimitiveType,
                this.isAbstract,
                this.classType,
                this.classVisibility,
                this.fieldDefsNotInherited,
                this.classAnnotations,
                this.constructorAnnotations,
                this.interfacesImplemented,
                this.superclassDefOptional);

    }


    public ClassDefBuilder withFieldDefsNotInherited(final List<? extends ClassFieldDef> fieldDefsNotInherited) {

        Objects.requireNonNull(fieldDefsNotInherited, "fieldDefsNotInherited");

        this.fieldDefsNotInherited.addAll(fieldDefsNotInherited);
        return this;

    }


    public ClassDefBuilder withSuperclass(final Optional<ClassDef> superclassDefOptional) {

        Objects.requireNonNull(superclassDefOptional, "superclassDefOptional");

        this.superclassDefOptional = superclassDefOptional;
        return this;

    }


    public ClassDefBuilder withInterfaces(final List<ParameterizedType> interfacesImplemented) {

        Objects.requireNonNull(interfacesImplemented, "interfacesImplemented");

        this.interfacesImplemented.addAll(interfacesImplemented);
        return this;

    }


    public ClassDefBuilder ofType(ClassType classType) {

        Objects.requireNonNull(classType, "classType");

        this.classType = classType;
        return this;

    }


    public ClassDefBuilder withClassAnnotation(final AnnotationDef annotationDef) {

        Objects.requireNonNull(annotationDef, "annotationDef");

        this.classAnnotations.add(annotationDef);
        return this;

    }


    public ClassDefBuilder withConstructorAnnotation(final AnnotationDef annotationDef) {

        Objects.requireNonNull(annotationDef, "annotationDef");

        this.constructorAnnotations.add(annotationDef);
        return this;

    }


    public ClassDefBuilder withAbstract(final boolean isAbstract) {

        this.isAbstract = isAbstract;
        return this;

    }


}
