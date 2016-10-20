package com.mahanaroad.mongogen.generator;


import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.java.AnnotationDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.ClassType;
import com.mahanaroad.mongogen.spec.definition.java.ConstructorArg;
import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.NonPrimitiveType;
import com.mahanaroad.mongogen.spec.definition.java.ParameterizedType;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


public abstract class AbstractJavaRenderer extends AbstractRenderer {


    private static final String IMPORTS_PLACEHOLDER = "IMPORTS_PLACEHOLDER";

    private final Set<String> importStatements = new TreeSet<>();
    private final List<ConstructorArg> constructorArgs = new LinkedList<>();
    private final ClassDef classDef;


    protected AbstractJavaRenderer(final ClassDef classDef) {

        Objects.requireNonNull(classDef, "classDef");
        this.classDef = classDef;

    }


    public final ClassDef getClassDef() {

        return this.classDef;

    }


    @Override
    protected String renderSource() {

        renderGeneratedCodeStatement();
        renderPackageStatement();
        renderImportsPlaceholder();
        renderClassAnnotations();
        renderClassSignature();
        renderPreClassFields();
        renderStaticMethods();
        renderFieldsForConstructorArgs();
        renderConstructor();
        renderMethods();
        renderInnerStaticClasses();
        blankLine();
        blankLine();
        appendLine("}");

        final String sourceWithImportPlaceholder = getSourceCode();
        final String renderedImportStatements = this.importStatements.stream().collect(joining("\n"));

        return sourceWithImportPlaceholder.replace(IMPORTS_PLACEHOLDER, renderedImportStatements);

    }


    protected String createFilePath() {

        final Fqcn fqcn = this.classDef.getNonPrimitiveType().getFqcn();
        return fqcn.toString().replace(".", "/") + ".java";

    }


    private void renderPackageStatement() {

        blankLine();
        appendLine("package " + this.classDef.getPackageName() + ";");

    }


    /**
     * We don't know what all the imports will be until after the subclass has rendered the methods in the class
     * body. So we just insert a placeholder for now and it will be replaced later.
     */
    private void renderImportsPlaceholder() {

        blankLine();
        appendLine(IMPORTS_PLACEHOLDER);

    }


    private void renderClassAnnotations() {

        blankLine();
        blankLine();

        this.classDef.getClassAnnotationsStream().forEach(annotationDef -> {
            addImportFor(annotationDef);
            appendLine("@%s", annotationDef.getUnqualifiedToString());
        });

    }


    private void renderClassSignature() {

        append(this.classDef.getClassVisibility().getJavaKeyword());
        append(" ");

        if (this.classDef.isAbstract()) {
            append("abstract ");
        }

        append(this.classDef.getClassType().name().toLowerCase());
        append(" ");
        append(this.classDef.getUqcn());

        this.classDef.getSuperclassDefOptional().ifPresent((superclassDef) -> {

            addImportFor(superclassDef.getNonPrimitiveType());

            append(" extends %s", superclassDef.getNonPrimitiveType().getUnqualifiedToString());

        });

        final String implementedInterfacesText = this.classDef.getInterfacesImplementedStream()
                .peek(this::addImportFor)
                .map(ParameterizedType::getUnqualifiedToString)
                .collect(joining(", "));

        if (implementedInterfacesText.isEmpty() == false) {
            append(" implements ");
            append(implementedInterfacesText);
        }

        append(" {");
        newLine();

    }


    // To be overridden by subclasses that need to render anything before the class fields are rendered
    protected void renderPreClassFields() {

        // Do nothing

    }


    // To be overridden by subclasses that need to render any static methods before the instance members are rendered
    protected void renderStaticMethods() {

        // Do nothing

    }


    private void renderFieldsForConstructorArgs() {

        blankLine();

        this.constructorArgs
                .stream()
                .filter(this::notAnInheritedField)
                .map(ConstructorArg::getClassFieldDef)
                .forEach(classField -> {

                    final String finalModifier = classField.isModifiable() ? "" : " final";
                    final String fieldTypeUqcn = classField.getFieldType().getUnqualifiedToString();
                    final ClassFieldName fieldName = classField.getClassFieldName();

                    blankLine();
                    classField.getAnnotationDefStream().forEach(ad -> {
                        addImportFor(ad);
                        appendLine("    %s", ad.toString());
                    });
                    appendLine("    private" + finalModifier + " " + fieldTypeUqcn + " " + fieldName + ";");

                });

    }


    private boolean notAnInheritedField(final ConstructorArg constructorArg) {

        return this.classDef.isFieldFromSuperclass(constructorArg.getClassFieldDef()) == false;

    }


    protected void renderConstructor() {

        if (this.classDef.getClassType() == ClassType.CLASS) {

            blankLine();
            blankLine();
            renderConstructorAnnotations();

            if (this.classDef.isAbstract()) {
                append("    protected ");
            } else {
                append("    public ");
            }

            append("%s(", this.classDef.getUqcn());
            renderConstructorArgs();
            append(") {");
            newLine();

            this.classDef.getSuperclassDefOptional().ifPresent(this::renderCallToSuperConstructor);

            blankLine();
            renderConstructorArgNullChecks();
            blankLine();

            this.constructorArgs
                    .stream()
                    .filter(this::notAnInheritedField)
                    .map(ConstructorArg::getClassFieldDef)
                    .forEach(classField -> {

                        if (classField.isList()) {
                            addImportFor(ArrayList.class);
                            appendLine("        this.%s = new ArrayList<>(%s);", classField.getClassFieldName(), classField.getClassFieldName());
                        } else {
                            appendLine("        this.%s = %s;", classField.getClassFieldName(), classField.getClassFieldName());
                        }
                    });

            blankLine();
            appendLine("    }");

        }

    }


    private void renderConstructorAnnotations() {

        this.classDef.getConstructorAnnotationsStream().forEach(annotationDef -> {

            addImportFor(annotationDef);
            appendLine("    @%s", annotationDef.getUnqualifiedToString());

        });

    }


    private void renderConstructorArgs() {

        final String separator;

        if (this.constructorArgs.size() > 3) {
            newLine();
            append("            ");
            separator = ",\n            ";
        } else {
            separator = ", ";
        }

        final String textToRender = this.constructorArgs
                .stream()
                .map(constructorArg -> {
                    final String annotationString = constructorArg.getAnnotationDef().map(ad -> ad + " ").orElse("");
                    final ClassFieldDef classField = constructorArg.getClassFieldDef();
                    return annotationString + "final " + classField.getFieldType().getUnqualifiedToString() + " " + classField.getClassFieldName();
                }).collect(joining(separator));

        append(textToRender);

    }


    protected void renderCallToSuperConstructor(final ClassDef superclassDef) {

        final String separator;

        blankLine();
        append("        super(");

        final List<ClassFieldDef> inheritedFields = superclassDef.getAllFieldsStream().collect(toList());

        if (inheritedFields.size() > 3) {
            newLine();
            append("            ");
            separator = ",\n            ";
        } else {
            separator = ", ";
        }

        final String textToRender = inheritedFields
                .stream()
                .map(fieldDef -> fieldDef.getClassFieldName().getValue())
                .collect(joining(separator));

        append(textToRender);

        append(");");
        newLine();

    }


    protected void renderConstructorArgNullChecks() {

        this.constructorArgs.stream()
                .filter(this::notAnInheritedField)
                .map(ConstructorArg::getClassFieldDef)
                .filter(arg -> arg.isPrimitive() == false)
                .forEach(arg -> {

                    if (arg.isOptional()) {

                        addImportFor(MongoGenFqcns.OBJECTS);
                        appendLine("        Objects.requireNonNull(%s, \"%s\");", arg.getClassFieldName(), arg.getClassFieldName());

                    } else {

                        if (arg.isString()) {
                            addImportFor(MongoGenFqcns.BLANK_STRING_EXCEPTION);
                            appendLine("        BlankStringException.throwIfBlank(%s, \"%s\");", arg.getClassFieldName(), arg.getClassFieldName());
                        } else {
                            addImportFor(MongoGenFqcns.OBJECTS);
                            appendLine("        Objects.requireNonNull(%s, \"%s\");", arg.getClassFieldName(), arg.getClassFieldName());
                        }

                    }

                });

    }


    /**
     * Subclasses may override this if they need to render any methods in the class body.
     */
    protected void renderMethods() {
        // do nothing
    }


    /**
     * Subclasses may override this if they need to render any static classes in the class body.
     */
    protected void renderInnerStaticClasses() {
        // do nothing
    }


    protected final void addConstructorArg(final ClassFieldDef classFieldDef) {

        Objects.requireNonNull(classFieldDef, "classFieldDef");

        addImportFor(classFieldDef.getFieldType());
        this.constructorArgs.add(new ConstructorArg(classFieldDef));
        Collections.sort(this.constructorArgs);

    }


    protected final void addConstructorArg(final ConstructorArg constructorArg) {

        Objects.requireNonNull(constructorArg, "constructorArg");

        addImportFor(constructorArg);
        this.constructorArgs.add(constructorArg);
        Collections.sort(this.constructorArgs);

    }


    protected final void append(final Uqcn uqcn) {

        super.append(uqcn.toString());

    }

    protected final void addImportFor(final Class<?> clazz) {

        addImportFor(clazz.getCanonicalName());

    }


    protected final void addImportFor(final String rawFqcn) {

        addImportFor(Fqcn.valueOf(rawFqcn));

    }


    protected final void addImportFor(final Fqcn fqcn) {

        if (fqcn.isInLangPackage() == false && fqcn.notInSamePackageAs(this.classDef.getNonPrimitiveType().getFqcn())) {
            this.importStatements.add("import " + fqcn + ";");
        }

    }


    protected final void addImportFor(final FieldType fieldType) {

        fieldType.getNonPrimitiveType().ifPresent(this::addImportFor);

    }


    protected final void addImportFor(final ConstructorArg constructorArg) {

        addImportFor(constructorArg.getClassFieldDef().getFieldType());
        constructorArg.getAnnotationDef().ifPresent(this::addImportFor);

    }


    protected final void addImportFor(final NonPrimitiveType nonPrimitiveType) {

        addImportFor(nonPrimitiveType.getFqcn());

        if (nonPrimitiveType instanceof ParameterizedType) {
            ((ParameterizedType) nonPrimitiveType).getParameterStream().forEach(this::addImportFor);
        }

    }


    private void addImportFor(final AnnotationDef annotationDef) {

        addImportFor(annotationDef.getFqcn());

    }


    protected final String fieldNamesAnded(final Stream<EntityFieldDef> fieldDefStream) {

        return fieldDefStream
                .map(fieldDef -> fieldDef.getClassFieldName().firstToUpper())
                .collect(joining("And"));

    }


    protected final String buildMethodParametersFrom(final Stream<EntityFieldDef> fieldDefStream) {

        return fieldDefStream
                    .map(fieldDef -> "final " + fieldDef.getFieldType().getUnqualifiedToString() + " " + fieldDef.getClassFieldName())
                    .collect(Collectors.joining(", "));

    }


    protected final String buildMethodParametersWithUnwrappedOptionalsFrom(final Stream<EntityFieldDef> fieldDefStream) {

        return fieldDefStream
                    .map(fieldDef -> "final " + fieldDef.getFieldType().unwrapIfOptional().getUnqualifiedToString() + " " + fieldDef.getClassFieldName())
                    .collect(Collectors.joining(", "));

    }


}
