package com.mahanaroad.mongogen.generator.enums;

import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.ModelDef;

import java.io.File;
import java.util.Objects;


public final class EnumGenerator {


    private final ModelDef modelDef;
    private final File outputDir;


    public EnumGenerator(final ModelDef modelDef, final File outputDir) {

        Objects.requireNonNull(modelDef, "modelDef");
        Objects.requireNonNull(outputDir, "outputDir");

        this.modelDef = modelDef;
        this.outputDir = outputDir;

    }


    public void generate() {

        this.modelDef.enumDefStream().forEach(this::processEnum);

    }


    private void processEnum(final EnumDef enumDef) {

        renderEnum(enumDef);

    }


    private void renderEnum(final EnumDef enumDef) {

        final EnumRenderer renderer = new EnumRenderer(enumDef);
        renderer.renderToDir(this.outputDir);

    }


}
