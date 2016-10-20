package com.mahanaroad.mongogen.spec.definition.java;

import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.HtmlInputType;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;
import com.mahanaroad.mongogen.spec.definition.validation.ValidationConstraint;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.mahanaroad.mongogen.BlankStringException.throwIfBlank;
import static java.util.Objects.requireNonNull;


public class HtmlFormFieldDef extends ClassFieldDef {


    private final String label;
    private final Optional<String> placeholder;
    private final HtmlInputType htmlInputType;


    public HtmlFormFieldDef(
            ClassFieldName classFieldName,
            FieldType providedFieldType,
            Optional<EnumDef> enumDef,
            Optional<SimpleTypeDef> simpleTypeDef,
            boolean modifiable,
            boolean optional,
            boolean isMasked,
            List<AnnotationDef> annotationDefs,
            Set<ValidationConstraint> validationConstraints,
            String label,
            Optional<String> placeholder,
            HtmlInputType htmlInputType) {

        super(
                classFieldName,
                providedFieldType,
                enumDef,
                simpleTypeDef,
                modifiable,
                optional,
                isMasked,
                annotationDefs,
                validationConstraints);

        this.label = throwIfBlank(label, "label");
        this.placeholder = requireNonNull(placeholder, "placeholder");
        this.htmlInputType = requireNonNull(htmlInputType, "htmlInputType");

    }


    public String getLabel() {

        return this.label;

    }


    public Optional<String> getPlaceholder() {

        return this.placeholder;

    }


    public HtmlInputType getHtmlInputType() {

        return this.htmlInputType;

    }


}
