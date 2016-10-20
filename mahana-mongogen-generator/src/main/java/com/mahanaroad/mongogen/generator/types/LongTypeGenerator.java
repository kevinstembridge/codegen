package com.mahanaroad.mongogen.generator.types;

import com.mahanaroad.mongogen.spec.definition.LongTypeDef;
import com.mahanaroad.mongogen.spec.definition.ModelDef;

import java.io.File;
import java.util.Objects;


public final class LongTypeGenerator {


    private final ModelDef modelDef;
    private final File outputDir;


    public LongTypeGenerator(final ModelDef modelDef, final File outputDir) {

        Objects.requireNonNull(modelDef, "modelDef");
        Objects.requireNonNull(outputDir, "outputDir");

        this.modelDef = modelDef;
        this.outputDir = outputDir;

    }


    public void generate() {

        this.modelDef.longTypeDefStream().forEach(this::processDef);

    }


    private void processDef(final LongTypeDef longTypeDef) {

        renderLongType(longTypeDef);

    }


    private void renderLongType(final LongTypeDef longTypeDef) {

        if (longTypeDef.isNotProvided()) {
            final SimpleTypeRenderer renderer = new SimpleTypeRenderer(longTypeDef);
            renderer.renderToDir(this.outputDir);
        }

    }


}
