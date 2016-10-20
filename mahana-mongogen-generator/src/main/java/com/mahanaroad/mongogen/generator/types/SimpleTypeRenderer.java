package com.mahanaroad.mongogen.generator.types;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;

import static com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef.aClassField;


public final class SimpleTypeRenderer extends AbstractJavaRenderer {


    public SimpleTypeRenderer(final SimpleTypeDef simpleTypeDef) {

        super(simpleTypeDef.getClassDef());


        addConstructorArg(aClassField("value", simpleTypeDef.getSuperTypeFieldType()).build());

    }


    @Override
    protected void renderMethods() {

        // do nothing

    }


}
