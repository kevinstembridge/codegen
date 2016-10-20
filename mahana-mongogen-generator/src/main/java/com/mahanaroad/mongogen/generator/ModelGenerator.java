package com.mahanaroad.mongogen.generator;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.generator.dtos.JavascriptGenerator;
import com.mahanaroad.mongogen.generator.entities.EntityGenerator;
import com.mahanaroad.mongogen.generator.enums.EnumGenerator;
import com.mahanaroad.mongogen.generator.types.BooleanTypeGenerator;
import com.mahanaroad.mongogen.generator.types.IntTypeGenerator;
import com.mahanaroad.mongogen.generator.types.LongTypeGenerator;
import com.mahanaroad.mongogen.generator.types.StringTypeGenerator;
import com.mahanaroad.mongogen.spec.definition.ModelDef;
import com.mahanaroad.mongogen.spec.definition.ModelDefProvider;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public final class ModelGenerator {

    private final String specificationClassName;
    private final File srcMainJavaOutputDir;
    private final File srcTestJavaOutputDir;
    private final Logger logger;
    private final File srcMainJavascriptOutputDir;

    private ModelDef modelDefinition;


    public ModelGenerator(
            final String specificationClassName,
            final File srcMainJavaOutputDir,
            final File srcTestJavaOutputDir,
            final File srcMainJavascriptOutputDir,
            final Logger logger) {

        BlankStringException.throwIfBlank(specificationClassName, "specificationClassName");
        Objects.requireNonNull(srcMainJavaOutputDir, "srcMainJavaOutputDir");
        Objects.requireNonNull(srcTestJavaOutputDir, "srcTestJavaOutputDir");
        Objects.requireNonNull(srcMainJavascriptOutputDir, "srcMainJavascriptOutputDir");
        Objects.requireNonNull(logger, "logger");

        this.specificationClassName = specificationClassName;
        this.srcMainJavaOutputDir = srcMainJavaOutputDir;
        this.srcTestJavaOutputDir = srcTestJavaOutputDir;
        this.srcMainJavascriptOutputDir = srcMainJavascriptOutputDir;
        this.logger = logger;

    }


    public void generateModel() throws Throwable {

        this.logger.info("Generating model");
        this.logger.info("    Main output dir = {}", this.srcMainJavaOutputDir.getAbsolutePath());
        this.logger.info("    Test output dir = {}", this.srcTestJavaOutputDir.getAbsolutePath());
        this.logger.info("    Javascript output dir = {}", this.srcMainJavascriptOutputDir.getAbsolutePath());

        try {

            final ModelDefProvider modelDefinitionBuilder = instantiate();
            this.modelDefinition = modelDefinitionBuilder.getModelDef();

            new StringTypeGenerator(this.modelDefinition, this.srcMainJavaOutputDir).generate();
            new BooleanTypeGenerator(this.modelDefinition, this.srcMainJavaOutputDir).generate();
            new IntTypeGenerator(this.modelDefinition, this.srcMainJavaOutputDir).generate();
            new LongTypeGenerator(this.modelDefinition, this.srcMainJavaOutputDir).generate();
            new EnumGenerator(this.modelDefinition, this.srcMainJavaOutputDir).generate();
            new EntityGenerator(this.modelDefinition, this.srcMainJavaOutputDir).generate();
            new JavascriptGenerator(this.modelDefinition, this.srcMainJavaOutputDir, this.srcMainJavascriptOutputDir).generate();

        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (ExceptionInInitializerError e) {
            throw e.getCause();
        }

    }


    private ModelDefProvider instantiate() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final Class<?> clazz = Class.forName(this.specificationClassName);
        final Constructor<?> constructor = clazz.getConstructors()[0];
        return (ModelDefProvider) constructor.newInstance();

    }


}
