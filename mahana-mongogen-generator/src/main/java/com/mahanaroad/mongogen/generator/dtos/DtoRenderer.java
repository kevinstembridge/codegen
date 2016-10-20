package com.mahanaroad.mongogen.generator.dtos;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.HtmlFormDef;
import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.ConstructorArg;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.stream.Collectors.joining;


public final class DtoRenderer extends AbstractJavaRenderer {


    private final HtmlFormDef htmlFormDef;


    public DtoRenderer(final HtmlFormDef htmlFormDef) {

        super(htmlFormDef.getDtoClassDef());

        this.htmlFormDef = htmlFormDef;
        this.htmlFormDef.getAllHtmlFormFields()
                .forEach(classField -> {
                    final Optional<AnnotationDef> jsonPropertyAnnotation = Optional.of(new AnnotationDef(MongoGenFqcns.JACKSON_JSON_PROPERTY, classField.getClassFieldName().getValue()));
                    addConstructorArg(new ConstructorArg(classField.unWrapIfComplexType(), jsonPropertyAnnotation));
                    addImportFor(classField.getFieldType());
                });

    }


    @Override
    protected void renderConstructorArgNullChecks() {

        // Do nothing. We let the validation constraints take care of validating input.

    }


    @Override
    protected void renderMethods() {

        renderSettersAndGetters();
        renderMethod_toString();

    }


    private void renderSettersAndGetters() {

        this.htmlFormDef.getFieldsNotInheritedStream().forEach(this::renderGetterFor);

    }


    private void renderGetterFor(final ClassFieldDef fieldDef) {

        blankLine();
        blankLine();
        appendLine("    public %s %s() {", fieldDef.getFieldType().getUnqualifiedToString(), fieldDef.getGetterMethodName());
        blankLine();

        if (fieldDef.isList()) {
            addImportFor(ArrayList.class);
            appendLine("        return new ArrayList<>(this.%s);", fieldDef.getClassFieldName());
        } else if (fieldDef.getSimpleTypeDef().isPresent()) {
            appendLine("        return new %s(this.%s);", fieldDef.getFieldType().getUnqualifiedToString(), fieldDef.getClassFieldName());
        } else {
            appendLine("        return this.%s;", fieldDef.getClassFieldName());
        }

        blankLine();
        appendLine("    }");

    }


    private void renderMethod_toString() {

        blankLine();
        blankLine();
        appendLine("    @Override");
        appendLine("    public String toString() {");
        blankLine();
        appendLine("        return \"%s{\" +", this.htmlFormDef.getUqcn());
        append(    "                ");

        final String lines = this.htmlFormDef.getAllHtmlFormFields()
                .map(fd -> {

                    if (fd.isMasked()) {
                        return String.format("\"%s = 'MASKED'\" +", fd.getClassFieldName());
                    } else {
                        return String.format("\"%s = '\" + this.%s + '\\'' +", fd.getClassFieldName(), fd.getClassFieldName());
                    }
                })
                .collect(joining(" \", \" + \n                "));

        append(lines);

        appendLine("\n                \"}\";");
        blankLine();
        appendLine("    }");

    }


}
