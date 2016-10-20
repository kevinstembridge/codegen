package com.mahanaroad.mongogen.generator.types;

import com.mahanaroad.mongogen.spec.definition.BooleanTypeDef;
import com.mahanaroad.mongogen.spec.definition.ModelDef;

import java.io.File;
import java.util.Objects;


public final class BooleanTypeGenerator {


    private final ModelDef modelDef;
    private final File outputDir;


    public BooleanTypeGenerator(final ModelDef modelDef, final File outputDir) {

        Objects.requireNonNull(modelDef, "modelDef");
        Objects.requireNonNull(outputDir, "outputDir");

        this.modelDef = modelDef;
        this.outputDir = outputDir;

    }


    public void generate() {

        this.modelDef.booleanTypeDefStream().forEach(this::processDef);

    }


    private void processDef(final BooleanTypeDef booleanTypeDef) {

        renderBooleanType(booleanTypeDef);

    }


    private void renderBooleanType(final BooleanTypeDef booleanTypeDef) {

        if (booleanTypeDef.isNotProvided()) {
            final SimpleTypeRenderer renderer = new SimpleTypeRenderer(booleanTypeDef);
            renderer.renderToDir(this.outputDir);
        }

    }


}
