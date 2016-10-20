package com.mahanaroad.mongogen.generator

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

class MongoGenTask extends DefaultTask {

    
    @TaskAction
    def generateModel() {

        def generatedJavaSourcesDir = project.file(project.mongogen.generatedSrcDir)
        def generatedJavaTestSourcesDir = project.file(project.mongogen.generatedTestSrcDir)
        def generatedJavascriptSourcesDir = project.file(project.mongogen.generatedJavascriptSrcDir)
        def modelDefProviderClassName = project.mongogen.modelDefProviderClass

        if (modelDefProviderClassName == null) {
            throw new InvalidUserDataException("Please specify the name of your model definition provider class under the property 'mongogen.modelDefProviderClass'.")
        }

        def modelGenerator = new ModelGenerator(modelDefProviderClassName, generatedJavaSourcesDir, generatedJavaTestSourcesDir, generatedJavascriptSourcesDir, logger);
        modelGenerator.generateModel();
        
    }
    
    
}
