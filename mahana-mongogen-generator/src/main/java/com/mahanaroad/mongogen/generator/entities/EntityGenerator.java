package com.mahanaroad.mongogen.generator.entities;

import com.mahanaroad.mongogen.spec.definition.EntityHierarchy;
import com.mahanaroad.mongogen.spec.definition.ModelDef;

import java.io.File;
import java.util.Objects;


public final class EntityGenerator {


    private final ModelDef modelDef;
    private final File outputDir;


    public EntityGenerator(final ModelDef modelDef, final File outputDir) {

        Objects.requireNonNull(modelDef, "modelDef");
        Objects.requireNonNull(outputDir, "outputDir");

        this.modelDef = modelDef;
        this.outputDir = outputDir;

    }


    public void generate() {

        this.modelDef.entityStream().forEach(this::processEntity);

    }


    private void processEntity(final EntityHierarchy entityHierarchy) {

        renderEntity(entityHierarchy);
        renderEntityFieldConverterInterface(entityHierarchy);
        renderEntityFilterInterface(entityHierarchy);
        renderEntityFilters(entityHierarchy);
        renderEntityUpdater(entityHierarchy);
        renderDao(entityHierarchy);

    }


    private void renderEntity(final EntityHierarchy entityHierarchy) {

        final EntityRenderer renderer = new EntityRenderer(entityHierarchy.getEntityDef());
        renderer.renderToDir(this.outputDir);

    }


    private void renderEntityFieldConverterInterface(final EntityHierarchy entityHierarchy) {

        final EntityFieldConverterRenderer renderer = new EntityFieldConverterRenderer(entityHierarchy.getEntityDef());
        renderer.renderToDir(this.outputDir);

    }


    private void renderEntityFilterInterface(final EntityHierarchy entityHierarchy) {

        final EntityFilterRenderer renderer = new EntityFilterRenderer(entityHierarchy.getEntityDef());
        renderer.renderToDir(this.outputDir);

    }


    private void renderEntityFilters(final EntityHierarchy entityHierarchy) {

        final EntityFiltersRenderer renderer = new EntityFiltersRenderer(entityHierarchy.getEntityDef());
        renderer.renderToDir(this.outputDir);

    }


    private void renderEntityUpdater(final EntityHierarchy entityHierarchy) {

        final EntityUpdaterRenderer renderer = new EntityUpdaterRenderer(entityHierarchy.getEntityDef());
        renderer.renderToDir(this.outputDir);

    }


    private void renderDao(final EntityHierarchy entityHierarchy) {

        final DaoRenderer renderer = new DaoRenderer(entityHierarchy);
        renderer.renderToDir(this.outputDir);

    }


}
