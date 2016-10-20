package com.mahanaroad.mongogen.generator.entities;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.EntityDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;

import java.util.ArrayList;


public final class EntityRenderer extends AbstractJavaRenderer {


    private final EntityDef entityDef;


    public EntityRenderer(final EntityDef entityDef) {

        super(entityDef.getEntityClassDef());

        this.entityDef = entityDef;
        this.entityDef.getAllFieldsStream().forEach(this::addConstructorArg);

    }


    @Override
    protected void renderMethods() {

        renderSettersAndGetters();

    }


    private void renderSettersAndGetters() {

        this.entityDef.getFieldsNotInheritedStream().forEach(this::renderSetterAndGetterFor);

    }


    private void renderSetterAndGetterFor(final EntityFieldDef entityFieldDef) {

        renderGetterFor(entityFieldDef);

        if (entityFieldDef.isModifiable()) {
            renderSetterFor(entityFieldDef);
        }

    }


    private void renderGetterFor(final EntityFieldDef entityFieldDef) {

        blankLine();
        blankLine();
        appendLine("    public %s %s() {", entityFieldDef.getFieldType().getUnqualifiedToString(), entityFieldDef.getGetterMethodName());
        blankLine();

        if (entityFieldDef.isList()) {
            addImportFor(ArrayList.class);
            appendLine("        return new ArrayList<>(this.%s);", entityFieldDef.getClassFieldName());
        } else {
            appendLine("        return this.%s;", entityFieldDef.getClassFieldName());
        }

        blankLine();
        appendLine("    }");

    }


    private void renderSetterFor(final EntityFieldDef entityFieldDef) {

        final ClassFieldName classFieldName = entityFieldDef.getClassFieldName();

        blankLine();
        blankLine();
        appendLine("    public void %s(final %s %s) {", entityFieldDef.getSetterMethodName(), entityFieldDef.getFieldType().getUnqualifiedToString(), classFieldName);
        blankLine();

        if (entityFieldDef.isString()) {
            addImportFor(MongoGenFqcns.BLANK_STRING_EXCEPTION);
            appendLine("        BlankStringException.throwIfBlank(%s, \"%s\");", classFieldName, classFieldName);
        } else if (entityFieldDef.isPrimitive() == false) {
            addImportFor(MongoGenFqcns.OBJECTS);
            appendLine("        Objects.requireNonNull(%s, \"%s\");", classFieldName, classFieldName);
        }

        if (entityFieldDef.isList()) {
            addImportFor(ArrayList.class);
            appendLine("        this.%s = new ArrayList<>(%s);", classFieldName, classFieldName);
        } else {
            appendLine("        this.%s = %s;", classFieldName, classFieldName);
        }

        blankLine();
        appendLine("    }");

    }


}
