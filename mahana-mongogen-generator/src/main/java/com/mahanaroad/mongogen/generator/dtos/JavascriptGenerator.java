package com.mahanaroad.mongogen.generator.dtos;

import com.mahanaroad.mongogen.generator.javascript.ClientJavascriptRenderer;
import com.mahanaroad.mongogen.generator.javascript.ValidationResultJavascriptRenderer;
import com.mahanaroad.mongogen.generator.javascript.ValidationUtilJavascriptRenderer;
import com.mahanaroad.mongogen.spec.definition.HtmlFormDef;
import com.mahanaroad.mongogen.spec.definition.ModelDef;

import java.io.File;
import java.util.Objects;


public final class JavascriptGenerator {


    private final ModelDef modelDef;
    private final File javaOutputDir;
    private final File javascriptOutputDir;


    public JavascriptGenerator(final ModelDef modelDef, final File javaOutputDir, final File javascriptOutputDir) {

        Objects.requireNonNull(modelDef, "modelDef");
        Objects.requireNonNull(javaOutputDir, "javaOutputDir");
        Objects.requireNonNull(javascriptOutputDir, "javascriptOutputDir");

        this.modelDef = modelDef;
        this.javaOutputDir = javaOutputDir;
        this.javascriptOutputDir = javascriptOutputDir;

    }


    public void generate() {

        this.modelDef.dtoDefStream().forEach(this::processDto);
        generateValidationResultJs();
        generateValidationUtilJs();
        generateClientJs();

    }


    private void processDto(final HtmlFormDef htmlFormDef) {

        renderDto(htmlFormDef);
        renderReactJsForm(htmlFormDef);

    }


    private void renderDto(HtmlFormDef htmlFormDef) {

        final DtoRenderer dtoRenderer = new DtoRenderer(htmlFormDef);
        dtoRenderer.renderToDir(this.javaOutputDir);

    }


    private void renderReactJsForm(HtmlFormDef htmlFormDef) {

        final ReactJsFormRenderer reactJsFormRenderer = new ReactJsFormRenderer(htmlFormDef);
        reactJsFormRenderer.renderToDir(this.javascriptOutputDir);

    }


    private void generateValidationResultJs() {

        final ValidationResultJavascriptRenderer renderer = new ValidationResultJavascriptRenderer();
        renderer.renderToDir(this.javascriptOutputDir);

    }


    private void generateValidationUtilJs() {

        final ValidationUtilJavascriptRenderer renderer = new ValidationUtilJavascriptRenderer();
        renderer.renderToDir(this.javascriptOutputDir);

    }


    private void generateClientJs() {

        final ClientJavascriptRenderer renderer = new ClientJavascriptRenderer();
        renderer.renderToDir(this.javascriptOutputDir);

    }


}
