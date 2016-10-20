package com.mahanaroad.mongogen.spec.definition.java;


public enum ClassVisibility {


    PUBLIC("public"),
    PROTECTED("protected"),
    PACKAGE_PRIVATE(""),
    PRIVATE("private");


    private final String javaKeyword;


    ClassVisibility(String javaKeyword) {

        this.javaKeyword = javaKeyword;

    }


    public String getJavaKeyword() {

        return this.javaKeyword;

    }


}
