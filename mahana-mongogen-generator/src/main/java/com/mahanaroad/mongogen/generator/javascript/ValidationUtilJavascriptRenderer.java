package com.mahanaroad.mongogen.generator.javascript;

import com.mahanaroad.mongogen.generator.AbstractRenderer;

public class ValidationUtilJavascriptRenderer extends AbstractRenderer {


    public ValidationUtilJavascriptRenderer() {
        

    }


    @Override
    protected String createFilePath() {

        return "validationUtil.js";

    }


    @Override
    protected String renderSource() {

        appendLine("export default class ValidationUtil {");
        blankLine();
        appendLine("    constructor() {");
        blankLine();
        appendLine("        this.emailRegex = /^(?:[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]|[^\\u0000-\\u007F])+@(?:[a-zA-Z0-9]|[^\\u0000-\\u007F])(?:(?:[a-zA-Z0-9-]|[^\\u0000-\\u007F]){0,61}(?:[a-zA-Z0-9]|[^\\u0000-\\u007F]))?(?:\\.(?:[a-zA-Z0-9]|[^\\u0000-\\u007F])(?:(?:[a-zA-Z0-9-]|[^\\u0000-\\u007F]){0,61}(?:[a-zA-Z0-9]|[^\\u0000-\\u007F]))?)*$/;");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    isInvalidEmail(email) {");
        blankLine();
        appendLine("        if (email.trim() == '' || this.emailRegex.test(email)) {");
        blankLine();
        appendLine("            return false;");
        blankLine();
        appendLine("        }");
        blankLine();
        appendLine("        return true;");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("}");

        return getSourceCode();

    }


}
