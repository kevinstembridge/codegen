package com.mahanaroad.mongogen.generator

import org.gradle.api.Project


class MongoGenExtension {

    def String generatedSrcDir
    def String generatedTestSrcDir
    def String generatedJavascriptSrcDir
    def String specSrcDir = 'src/mongogen/java'
    def String modelDefProviderClass
    
    def MongoGenExtension(Project project) {
        generatedSrcDir = "${project.buildDir.path}/generated-src/java/main"
        generatedTestSrcDir = "${project.buildDir.path}/generated-src/java/test"
        generatedJavascriptSrcDir = "${project.buildDir.path}/generated-src/javascript/main"
    }
    
    
}
