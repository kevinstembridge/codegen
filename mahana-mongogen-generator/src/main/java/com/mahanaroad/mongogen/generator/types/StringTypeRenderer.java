package com.mahanaroad.mongogen.generator.types;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.definition.StringTypeDef;

import static com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef.aClassField;


public final class StringTypeRenderer extends AbstractJavaRenderer {

    private final StringTypeDef.CaseMode caseMode;


    public StringTypeRenderer(final StringTypeDef abstractTypeDef) {

        super(abstractTypeDef.getClassDef());

        this.caseMode = abstractTypeDef.getCaseMode();

        addConstructorArg(aClassField("value", abstractTypeDef.getSuperTypeFieldType()).build());

    }


    @Override
    protected void renderConstructor() {

        blankLine();
        appendLine("    public %s(final String value) {", getClassDef().getUqcn());
        blankLine();

        switch (this.caseMode) {
            case ALWAYS_UPPER:
                appendLine("        super(value.toUpperCase());");
                break;
            case ALWAYS_LOWER:
                appendLine("        super(value.toLowerCase());");
                break;
            default:
                appendLine("        super(value);");
        }

        blankLine();
        appendLine("    }");

    }


    @Override
    protected void renderMethods() {

        // do nothing

    }


}
