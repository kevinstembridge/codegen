package com.mahanaroad.mongogen.generator

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * This is a Gradle plugin that performs the task of generating
 * source code for model classes.
 *
 */
class MongoGenPlugin implements Plugin<Project> {
    
    
    @Override
    public void apply(Project project) {

        project.apply plugin: 'java'
        
        project.extensions.create("mongogen", MongoGenExtension, project)

        project.sourceSets {
            main {
                java {
                    srcDir 'build/generated-src/java/main'
                }
            }
            test {
                java {
                    srcDir 'build/generated-src/java/test'
                }
            }
            mongogen {
                compileClasspath = project.sourceSets.main.compileClasspath
                output.classesDir = project.sourceSets.main.output.classesDir
            }
        }

        def generateModelTask = project.tasks.create("generateModel", MongoGenTask)

        generateModelTask.dependsOn project.tasks[project.sourceSets.mongogen.getCompileTaskName('java')]

        project.compileJava.dependsOn generateModelTask

    }


}
