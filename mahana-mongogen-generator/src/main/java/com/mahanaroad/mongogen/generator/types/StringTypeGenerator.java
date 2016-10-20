package com.mahanaroad.mongogen.generator.types;

import com.mahanaroad.mongogen.spec.definition.ModelDef;
import com.mahanaroad.mongogen.spec.definition.StringTypeDef;

import java.io.File;
import java.util.Objects;


public final class StringTypeGenerator {


    private final ModelDef modelDef;
    private final File outputDir;


    public StringTypeGenerator(final ModelDef modelDef, final File outputDir) {

        Objects.requireNonNull(modelDef, "modelDef");
        Objects.requireNonNull(outputDir, "outputDir");

        this.modelDef = modelDef;
        this.outputDir = outputDir;

    }


    public void generate() {

        this.modelDef.stringTypeDefStream().forEach(this::processDef);

    }


    private void processDef(final StringTypeDef stringTypeDef) {

        renderStringType(stringTypeDef);

    }


    private void renderStringType(final StringTypeDef stringTypeDef) {

        if (stringTypeDef.isNotProvided()) {
            final StringTypeRenderer renderer = new StringTypeRenderer(stringTypeDef);
            renderer.renderToDir(this.outputDir);
        }

    }


}
