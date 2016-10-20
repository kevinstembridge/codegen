package com.mahanaroad.mongogen.generator.javascript;

import com.mahanaroad.mongogen.generator.AbstractRenderer;

public class ValidationResultJavascriptRenderer extends AbstractRenderer {


    public ValidationResultJavascriptRenderer() {
        

    }


    @Override
    protected String createFilePath() {

        return "validationResult.js";

    }


    @Override
    protected String renderSource() {

        appendLine("export default class ValidationResult {");
        blankLine();
        blankLine();
        appendLine("    constructor() {");
        blankLine();
        appendLine("        this.validationMessages = [];");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    addWarning(propertyName, message) {");
        blankLine();
        appendLine("        this.validationMessages.push(new ValidationMessage(propertyName, 'warning', message));");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    addError(propertyName, message) {");
        blankLine();
        appendLine("        this.validationMessages.push(new ValidationMessage(propertyName, 'error', message));");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    hasErrors() {");
        blankLine();
        appendLine("        return this.validationMessages.some(v => v.severity == 'error');");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    getValidationMessages() {");
        blankLine();
        appendLine("        const result = {};");
        blankLine();
        appendLine("        for (var m of this.validationMessages) {");
        appendLine("            result[m.propertyName] = {'severity': m.severity, 'message': m.message};");
        appendLine("        }");
        blankLine();
        appendLine("        return result;");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    isFieldHasError(propertyName) {");
        blankLine();
        appendLine("        return this.validationMessages.some(m => m.propertyName == propertyName && m.severity == 'error');");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    isFieldHasWarning(propertyName) {");
        blankLine();
        appendLine("        return this.validationMessages.some(m => m.propertyName == propertyName && m.severity == 'warning');");
        blankLine();
        appendLine("    }");
        blankLine();
        appendLine("}");
        blankLine();
        blankLine();
        appendLine("export class ValidationMessage {");
        blankLine();
        appendLine("    constructor(propertyName, severity, message) {");
        blankLine();
        appendLine("        this.propertyName = propertyName;");
        appendLine("        this.severity = severity;");
        appendLine("        this.message = message;");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("}");
        blankLine();
        blankLine();
        appendLine("export class ValidationUtil {");
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
