package com.mahanaroad.mongogen.spec.definition.java;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;



public final class ClassDef {


    private final NonPrimitiveType nonPrimitiveType;
    private final ClassType classType;
    private final ClassVisibility classVisibility;
    private final Optional<ClassDef> superclassDefOptional;
    private final List<ParameterizedType> interfacesImplemented = new LinkedList<>();
    private final List<ClassFieldDef> fieldDefsNotInherited = new LinkedList<>();
    private final List<AnnotationDef> classAnnotations = new LinkedList<>();
    private final List<AnnotationDef> constructorAnnotations = new LinkedList<>();
    private final boolean isAbstract;


    public ClassDef(
            final NonPrimitiveType nonPrimitiveType,
            final boolean isAbstract,
            final ClassType classType,
            final ClassVisibility classVisibility,
            final List<ClassFieldDef> fieldDefsNotInherited,
            final List<AnnotationDef> classAnnotations,
            final List<AnnotationDef> constructorAnnotations,
            final List<ParameterizedType> interfacesImplemented,
            final Optional<ClassDef> superclassDefOptional) {

        Objects.requireNonNull(nonPrimitiveType, "nonPrimitiveType");
        Objects.requireNonNull(classType, "classType");
        Objects.requireNonNull(classVisibility, "classVisibility");
        Objects.requireNonNull(fieldDefsNotInherited, "fieldDefsNotInherited");
        Objects.requireNonNull(classAnnotations, "classAnnotations");
        Objects.requireNonNull(constructorAnnotations, "constructorAnnotations");
        Objects.requireNonNull(interfacesImplemented, "interfacesImplemented");
        Objects.requireNonNull(superclassDefOptional, "superclassDefOptional");

        this.nonPrimitiveType = nonPrimitiveType;
        this.isAbstract = isAbstract;
        this.classType = classType;
        this.classVisibility = classVisibility;
        this.fieldDefsNotInherited.addAll(fieldDefsNotInherited);
        this.classAnnotations.addAll(classAnnotations);
        this.constructorAnnotations.addAll(constructorAnnotations);
        this.interfacesImplemented.addAll(interfacesImplemented);
        this.superclassDefOptional = superclassDefOptional;

    }


    /**
     * @return Never null.
     */
    public NonPrimitiveType getNonPrimitiveType() {

        return this.nonPrimitiveType;

    }


    /**
     * @return Never null.
     */
    public PackageName getPackageName() {

        return this.nonPrimitiveType.getFqcn().getPackageName();
        
    }


    /**
     * @return Never null.
     */
    public Uqcn getUqcn() {

        return this.nonPrimitiveType.getFqcn().getUqcn();

    }


    /**
     * @return Never null.
     */
    public ClassType getClassType() {

        return this.classType;

    }


    /**
     * @return Never null.
     */
    public ClassVisibility getClassVisibility() {

        return this.classVisibility;

    }


    public Stream<ClassFieldDef> getAllFieldsStream() {

        return Stream.concat(this.fieldDefsNotInherited.stream(), getFieldsInheritedStream()).sorted();

    }


    public Optional<ClassDef> getSuperclassDefOptional() {

        return this.superclassDefOptional;

    }


    public Stream<ClassFieldDef> getFieldsInheritedStream() {

        return this.superclassDefOptional.map(ClassDef::getAllFieldsStream).orElse(Stream.empty()).sorted();

    }


    public Stream<ClassFieldDef> getFieldsNotInheritedStream() {

        return this.fieldDefsNotInherited.stream().sorted();

    }


    public Stream<ParameterizedType> getInterfacesImplementedStream() {

        return this.interfacesImplemented.stream();

    }


    public boolean isFieldFromSuperclass(final ClassFieldDef classFieldDef) {

        return getFieldsInheritedStream()
                .filter(fieldDef -> fieldDef.getClassFieldName().equals(classFieldDef.getClassFieldName()))
                .findFirst()
                .isPresent();

    }


    public Stream<AnnotationDef> getClassAnnotationsStream() {

        return this.classAnnotations.stream();

    }


    public Stream<AnnotationDef> getConstructorAnnotationsStream() {

        return this.constructorAnnotations.stream();

    }


    public boolean isAbstract() {

        return this.isAbstract;

    }


}
