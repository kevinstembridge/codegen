package com.mahanaroad.mongogen.generator.entities;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.EntityDef;


public final class EntityFilterRenderer extends AbstractJavaRenderer {


    private final EntityDef entityDef;


    public EntityFilterRenderer(final EntityDef entityDef) {

        super(entityDef.getEntityFilterClassDef());

        this.entityDef = entityDef;

    }


    @Override
    protected void renderMethods() {

        addImportFor(MongoGenFqcns.BSON);

        blankLine();
        blankLine();
        appendLine("    public Bson toBson(final %s fieldConverter);", this.entityDef.getEntityFieldConverterClassDef().getUqcn());

    }


}
