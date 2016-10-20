package com.mahanaroad.mongogen.generator.types;

import com.mahanaroad.mongogen.spec.definition.IntTypeDef;
import com.mahanaroad.mongogen.spec.definition.ModelDef;

import java.io.File;
import java.util.Objects;


public final class IntTypeGenerator {


    private final ModelDef modelDef;
    private final File outputDir;


    public IntTypeGenerator(final ModelDef modelDef, final File outputDir) {

        Objects.requireNonNull(modelDef, "modelDef");
        Objects.requireNonNull(outputDir, "outputDir");

        this.modelDef = modelDef;
        this.outputDir = outputDir;

    }


    public void generate() {

        this.modelDef.intTypeDefStream().forEach(this::processDef);

    }


    private void processDef(final IntTypeDef intTypeDef) {

        renderIntType(intTypeDef);

    }


    private void renderIntType(final IntTypeDef intTypeDef) {

        if (intTypeDef.isNotProvided()) {
            final SimpleTypeRenderer renderer = new SimpleTypeRenderer(intTypeDef);
            renderer.renderToDir(this.outputDir);
        }

    }


}
