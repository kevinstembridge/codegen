package com.mahanaroad.mongogen.generator.enums;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.EnumValueDef;

import java.util.Iterator;


public final class EnumRenderer extends AbstractJavaRenderer {


    private final EnumDef enumDef;


    public EnumRenderer(final EnumDef enumDef) {

        super(enumDef.getClassDef());

        this.enumDef = enumDef;

    }

    @Override
    protected void renderPreClassFields() {

        blankLine();

        final Iterator<EnumValueDef> itr = this.enumDef.getEnumValuesStream().iterator();

        while (itr.hasNext()) {

            final EnumValueDef enumValueDef = itr.next();
            append("    %s", enumValueDef.getName());

            if (itr.hasNext()) {
                append(",");
            } else {
                append(";");
            }

            newLine();

        }

    }


    @Override
    protected void renderMethods() {

        // do nothing

    }


}
