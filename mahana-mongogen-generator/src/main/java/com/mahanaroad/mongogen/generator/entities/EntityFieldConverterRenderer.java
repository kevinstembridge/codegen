package com.mahanaroad.mongogen.generator.entities;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.definition.EntityDef;


public final class EntityFieldConverterRenderer extends AbstractJavaRenderer {



    public EntityFieldConverterRenderer(final EntityDef entityDef) {

        super(entityDef.getEntityFieldConverterClassDef());

    }


    @Override
    protected void renderMethods() {

        blankLine();
        blankLine();
        appendLine("    public Object convert(final String collectionFieldName, final Object inputValue);");

    }


}
