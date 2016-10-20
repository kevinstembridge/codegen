package com.mahanaroad.mongogen.spec.definition.builders;


import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.HtmlInputType;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;
import com.mahanaroad.mongogen.spec.definition.StringTypeDef;
import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.HtmlFormFieldDef;
import com.mahanaroad.mongogen.spec.definition.validation.ValidationConstraint;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;


public final class HtmlFormFieldDefBuilder {


    private final ClassFieldName classFieldName;
    private final String label;
    private Optional<String> placeholder = Optional.empty();
    private final FieldType fieldType;
    private boolean optional = false;
    private boolean isMasked = false;
    private final Optional<SimpleTypeDef> simpleTypeDefOptional;
    private final Optional<EnumDef> enumDefOptional;
    private final Set<ValidationConstraint> validationConstraints = new TreeSet<>();
    private HtmlInputType htmlInputType = HtmlInputType.text;


    public HtmlFormFieldDefBuilder(final ClassFieldName classFieldName, final String label, final EnumDef enumDef) {

        this(classFieldName, label, enumDef.getFieldType(), Optional.empty(), Optional.of(enumDef));

    }


    public HtmlFormFieldDefBuilder(final ClassFieldName classFieldName, final String label, final SimpleTypeDef simpleTypeDef) {

        this(classFieldName, label, simpleTypeDef.getFieldType(), Optional.of(simpleTypeDef), Optional.empty());

    }


    public HtmlFormFieldDefBuilder(final ClassFieldName classFieldName, final String label, final FieldType fieldType) {

        this(classFieldName, label, fieldType, Optional.empty(), Optional.empty());

    }


    private HtmlFormFieldDefBuilder(
            final ClassFieldName classFieldName,
            final String label,
            final FieldType fieldType,
            final Optional<SimpleTypeDef> simpleTypeDef,
            final Optional<EnumDef> enumDef) {

        this.classFieldName = requireNonNull(classFieldName, "classFieldName");
        this.label = label;
        this.fieldType = requireNonNull(fieldType, "fieldType");
        this.simpleTypeDefOptional = requireNonNull(simpleTypeDef, "simpleTypeDef");
        this.enumDefOptional = requireNonNull(enumDef, "enumDef");

    }


    public HtmlFormFieldDefBuilder optional() {

        this.optional = true;
        return this;

    }


    public HtmlFormFieldDefBuilder masked() {

        this.isMasked = true;
        return this;

    }


    public HtmlFormFieldDefBuilder withEmailConstraint() {

        this.validationConstraints.add(ValidationConstraint.EMAIL);
        return this;

    }


    public HtmlFormFieldDefBuilder placeholder(final String placeholder) {

        this.placeholder = Optional.of(placeholder);
        return this;

    }


    public HtmlFormFieldDef build() {

        final boolean modifiable = false;

        if (this.optional == false && this.fieldType.isNotPrimitive()) {

            this.validationConstraints.add(ValidationConstraint.NOT_NULL);

            if (this.fieldType.isString() || this.simpleTypeDefOptional.filter(simpleTypeDef -> simpleTypeDef instanceof StringTypeDef).isPresent()) {
                this.validationConstraints.add(ValidationConstraint.NOT_EMPTY);
            }

        }

        final List<AnnotationDef> annotationDefs = this.validationConstraints
                .stream()
                .map(ValidationConstraint::getAssociatedAnnotationDef)
                .collect(toList());

        return new HtmlFormFieldDef(
                this.classFieldName,
                this.fieldType,
                this.enumDefOptional,
                this.simpleTypeDefOptional,
                modifiable,
                this.optional,
                this.isMasked,
                annotationDefs,
                this.validationConstraints,
                this.label,
                this.placeholder,
                this.htmlInputType);

    }


    public HtmlFormFieldDefBuilder inputType(HtmlInputType htmlInputType) {

        this.htmlInputType = htmlInputType;
        return this;

    }


}
