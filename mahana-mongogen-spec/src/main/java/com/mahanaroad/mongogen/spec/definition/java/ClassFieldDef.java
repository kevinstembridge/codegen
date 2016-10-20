package com.mahanaroad.mongogen.spec.definition.java;

import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;
import com.mahanaroad.mongogen.spec.definition.validation.ValidationConstraint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class ClassFieldDef implements Comparable<ClassFieldDef> {

    public static Builder aClassField(final String classFieldName, final Fqcn fqcn) {

        return new Builder(classFieldName, fqcn);

    }


    public static Builder aClassField(final String classFieldName, final FieldType fieldType) {

        return new Builder(classFieldName, fieldType);

    }


    public static Builder aClassField(final ClassFieldName classFieldName, final FieldType fieldType) {

        return new Builder(classFieldName, fieldType);

    }


    private final ClassFieldName classFieldName;
    private final FieldType fieldType;
    private final boolean modifiable;
    private final boolean optional;
    private final boolean isMasked;
    private final Optional<EnumDef> enumDef;
    private final Optional<SimpleTypeDef> simpleTypeDef;
    private final List<AnnotationDef> annotationDefs = new ArrayList<>();
    private final Set<ValidationConstraint> validationConstraints = new TreeSet<>();


    public ClassFieldDef(
            final ClassFieldName classFieldName,
            final FieldType providedFieldType,
            final Optional<EnumDef> enumDef,
            final Optional<SimpleTypeDef> simpleTypeDef,
            final boolean modifiable,
            final boolean optional,
            final boolean isMasked,
            final List<AnnotationDef> annotationDefs,
            final Set<ValidationConstraint> validationConstraints) {

        Objects.requireNonNull(classFieldName, "classFieldName");
        Objects.requireNonNull(providedFieldType, "providedFieldType");
        Objects.requireNonNull(simpleTypeDef, "simpleTypeDef");
        Objects.requireNonNull(enumDef, "enumDef");
        Objects.requireNonNull(annotationDefs, "annotationDefs");
        Objects.requireNonNull(validationConstraints, "validationConstraints");

        this.classFieldName = classFieldName;
        this.modifiable = modifiable;
        this.optional = optional && providedFieldType.isCollection() == false;
        this.isMasked = isMasked;
        this.fieldType = initFieldTypeFrom(providedFieldType);
        this.enumDef = enumDef;
        this.simpleTypeDef = simpleTypeDef;
        this.annotationDefs.addAll(annotationDefs);
        this.validationConstraints.addAll(validationConstraints);

    }


    private FieldType initFieldTypeFrom(final FieldType providedFieldType) {

        if (this.optional) {
            return providedFieldType.asOptional();
        } else {
            return providedFieldType;
        }

    }


    public final FieldType getFieldType() {

        return this.fieldType;

    }


    public final Optional<EnumDef> getEnumDef() {

        return this.enumDef;

    }


    public final Optional<SimpleTypeDef> getSimpleTypeDef() {

        return this.simpleTypeDef;

    }


    public final ClassFieldName getClassFieldName() {

        return this.classFieldName;

    }


    public final boolean isModifiable() {

        return this.modifiable;

    }


    public boolean isPrimitive() {

        return this.fieldType.isPrimitive();

    }


    public boolean isString() {

        return this.fieldType.isString();

    }


    public boolean isList() {

        return this.fieldType.isList();

    }


    @Override
    public String toString() {

        return "ClassFieldDef{" +
                "classFieldName=" + classFieldName +
                ", fieldType=" + fieldType +
                ", modifiable=" + modifiable +
                ", optional=" + optional +
                '}';

    }


    public boolean isOptionalEnum() {

        return this.fieldType.getParameterizedTypeOptional()
                .filter(pt -> pt.getFqcn().equals(Fqcn.OPTIONAL))
                .map(ft -> ft.getFirstParameter() instanceof EnumType)
                .orElse(false);

    }


    public boolean isEnumList() {

        return isList() && this.fieldType.getFirstParameterType() instanceof EnumType;

    }


    public boolean isMap() {

        return this.fieldType.isMap();

    }


    public boolean hasValidationConstraint(final ValidationConstraint validationConstraint) {

        return this.validationConstraints.contains(validationConstraint);

    }


    public ClassFieldDef unWrapIfComplexType() {

        if (this.enumDef.isPresent()) {
            return new ClassFieldDef(
                    this.classFieldName,
                    FieldType.STRING,
                    Optional.empty(),
                    this.simpleTypeDef,
                    this.modifiable,
                    this.optional,
                    this.isMasked,
                    this.annotationDefs,
                    this.validationConstraints);
        }

        if (this.simpleTypeDef.isPresent()) {
            return new ClassFieldDef(
                    this.classFieldName,
                    this.simpleTypeDef.get().getSuperTypeFieldType(),
                    this.enumDef,
                    Optional.empty(),
                    this.modifiable,
                    this.optional,
                    this.isMasked,
                    this.annotationDefs,
                    this.validationConstraints);
        }

        return this;

    }


    public final boolean isOptional() {

        return this.optional;

    }


    @Override
    public int compareTo(final ClassFieldDef that) {

        return this.classFieldName.compareTo(that.classFieldName);

    }


    public final String getGetterMethodName() {

        return "get" + this.classFieldName.firstToUpper();

    }


    public final String getSetterMethodName() {

        return "set" + this.classFieldName.firstToUpper();

    }


    public final boolean isInstant() {

        return this.fieldType.isInstant();

    }


    public final boolean isInt() {

        return this.fieldType.isInt();

    }


    public final Stream<AnnotationDef> getAnnotationDefStream() {

        return this.annotationDefs.stream();

    }


    public final boolean isMasked() {

        return this.isMasked;

    }


    public static class Builder {


        private final ClassFieldName classFieldName;
        private final FieldType fieldType;
        private boolean modifiable;
        private boolean optional;
        private final List<AnnotationDef> annotationDefs = new ArrayList<>();
        private final Set<ValidationConstraint> validationConstraints = new HashSet<>();
        private boolean isMasked;


        private Builder(final String classFieldName, final Fqcn fqcn) {
            this(new ClassFieldName(classFieldName), FieldType.valueOf(fqcn));

        }


        private Builder(final String classFieldName, final FieldType fieldType) {
            this(new ClassFieldName(classFieldName), fieldType);

        }


        public Builder(final ClassFieldName classFieldName, final FieldType fieldType) {

            this.classFieldName = classFieldName;
            this.fieldType = fieldType;

        }


        public ClassFieldDef build() {

            return new ClassFieldDef(
                    this.classFieldName,
                    this.fieldType,
                    Optional.empty(),
                    Optional.empty(),
                    this.modifiable,
                    this.optional,
                    this.isMasked,
                    this.annotationDefs,
                    this.validationConstraints);

        }


        public Builder modifiable() {

            this.modifiable = true;
            return this;

        }


        public Builder optional() {

            this.optional = true;
            return this;

        }


        public Builder masked() {

            this.isMasked = true;
            return this;

        }


        public Builder addAnnotation(final AnnotationDef annotationDef) {

            Objects.requireNonNull(annotationDef, "annotationDefs");
            this.annotationDefs.add(annotationDef);
            return this;

        }


        public Builder withEmailConstraint() {

            this.validationConstraints.add(ValidationConstraint.EMAIL);
            return this;

        }


    }


}
