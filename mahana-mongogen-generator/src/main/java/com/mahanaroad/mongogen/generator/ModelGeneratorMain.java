package com.mahanaroad.mongogen.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Stream;

public final class ModelGeneratorMain {


    private static final Logger logger = LoggerFactory.getLogger(ModelGeneratorMain.class);

    
    public static void main(String[] args) {

        if (args.length < 4) {
            System.err.println("Invalid command line args!");
            System.err.println("Expected:");
            System.err.println("    <modelDescriptorClassName>");
            System.err.println("    <mainJavaOutputDirLocation>");
            System.err.println("    <testJavaOutputDirLocation>");
            System.err.println("    <mainJavascriptOutputDirLocation>");
            System.err.println("But was:");
            Stream.of(args).forEach(arg -> System.err.println("    " + arg));
            System.exit(1);
        }

        final String modelDescriptorClassName = args[0];
        final String mainOutputDirLocation = args[1];
        final String testOutputDirLocation = args[2];
        final String srcMainJavascriptDirLocation = args[3];
        final File srcMainOutputDir = new File(mainOutputDirLocation);
        final File srcTestOutputDir = new File(testOutputDirLocation);
        final File srcMainJavascriptDir = new File(srcMainJavascriptDirLocation);

        final ModelGenerator modelGenerator = new ModelGenerator(modelDescriptorClassName, srcMainOutputDir, srcTestOutputDir, srcMainJavascriptDir, logger);

        try {
            modelGenerator.generateModel();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
    
}
