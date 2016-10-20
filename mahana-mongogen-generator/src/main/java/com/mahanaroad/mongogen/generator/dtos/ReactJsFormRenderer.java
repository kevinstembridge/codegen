package com.mahanaroad.mongogen.generator.dtos;

import com.mahanaroad.mongogen.generator.AbstractRenderer;
import com.mahanaroad.mongogen.spec.definition.HtmlFormDef;
import com.mahanaroad.mongogen.spec.definition.validation.ValidationConstraint;

public class ReactJsFormRenderer extends AbstractRenderer {


    private final HtmlFormDef htmlFormDef;


    public ReactJsFormRenderer(final HtmlFormDef htmlFormDef) {

        this.htmlFormDef = htmlFormDef;

    }


    @Override
    protected String createFilePath() {

        return this.htmlFormDef.getHtmlFormName() + "Form.jsx";

    }


    @Override
    protected String renderSource() {

        renderImportStatements();

        blankLine();
        blankLine();
        appendLine("class %sForm extends React.Component {", this.htmlFormDef.getHtmlFormName());
        renderConstructor();
        renderOnChangeFunctions();
        renderFunction_validateFormIfPreviouslySubmitted();
        renderFunction_validateForm();
        renderFunction_updateStateWithValidationResult();
        renderValidateFieldFunctions();
        renderFunction_determineFormFieldClassNames();
        renderFunction_getClassNamesFor();
        renderFunction_getFormFieldState();
        renderFunction_onSubmit();
        renderFunction_handleSuccessResponse();
        renderFunction_handleErrorResponse();
        renderFunction_handleBadRequest();
        renderFunction_render();

        blankLine();
        blankLine();
        appendLine("}");
        blankLine();
        blankLine();
        appendLine("ReactDOM.render(");
        appendLine("    <%sForm />,", this.htmlFormDef.getHtmlFormName());
        appendLine("    document.getElementById('%sFormDiv')", this.htmlFormDef.getHtmlFormName());
        appendLine(");");

        return getSourceCode();

    }


    private void renderImportStatements() {

        appendLine("import ReactDOM from 'react-dom';");
        appendLine("import React from 'react';");
        appendLine("import client from './client';");
        appendLine("import classNames from 'classnames';");
        appendLine("import ValidationResult from './validationResult';");
        appendLine("import ValidationUtil from './validationUtil';");

    }


    private void renderConstructor() {

        blankLine();
        blankLine();
        appendLine("    constructor(props) {");
        appendLine("        super(props);");
        appendLine("        this.state = {");

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef ->
                appendLine("            %s: '',", classFieldDef.getClassFieldName())
        );

        appendLine("            validationMessages: {},");
        appendLine("            formFieldClassNames: {");

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef ->
            appendLine("                %s: 'form-group',", classFieldDef.getClassFieldName())
        );

        appendLine("            },");
        appendLine("            previouslySubmitted: false");
        appendLine("        };");

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef ->
            appendLine("        this.onChange%s = this.onChange%s.bind(this);", classFieldDef.getClassFieldName().firstToUpper(), classFieldDef.getClassFieldName().firstToUpper())
        );

        appendLine("        this.onSubmit = this.onSubmit.bind(this);");
        appendLine("        this.handleSuccessResponse = this.handleSuccessResponse.bind(this);");
        appendLine("        this.handleErrorResponse = this.handleErrorResponse.bind(this);");
        appendLine("        this.validationUtil = new ValidationUtil();");
        appendLine("    }");

    }


    private void renderOnChangeFunctions() {

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef -> {
                blankLine();
                blankLine();
                appendLine("    onChange%s(e) {", classFieldDef.getClassFieldName().firstToUpper());
                appendLine("        this.setState({%s: e.target.value}, this.validateFormIfPreviouslySubmitted);", classFieldDef.getClassFieldName());
                appendLine("    }");
            });

    }


    private void renderFunction_validateFormIfPreviouslySubmitted() {

        blankLine();
        blankLine();
        appendLine("    validateFormIfPreviouslySubmitted() {");
        blankLine();
        appendLine("        if (this.state.previouslySubmitted) {");
        appendLine("            this.validateForm();");
        appendLine("        }");
        blankLine();
        appendLine("    }");

    }


    private void renderFunction_validateForm() {

        blankLine();
        blankLine();
        appendLine("    validateForm() {");
        blankLine();
        appendLine("        const validationResult = new ValidationResult();");
        blankLine();

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef ->
            appendLine("        this.validate%s(validationResult);", classFieldDef.getClassFieldName().firstToUpper())
        );

        blankLine();
        appendLine("        this.updateStateWith(validationResult);");
        blankLine();
        appendLine("        return validationResult.hasErrors();");
        blankLine();
        appendLine("    }");

    }


    private void renderFunction_updateStateWithValidationResult() {

        blankLine();
        blankLine();
        appendLine("    updateStateWith(validationResult) {");
        blankLine();
        appendLine("        const validationMessages = validationResult.getValidationMessages();");
        appendLine("        const formFieldClassNames = this.determineFormFieldClassNames(validationResult);");
        blankLine();
        appendLine("        this.setState({");
        appendLine("            validationMessages: validationMessages,");
        appendLine("            formFieldClassNames: formFieldClassNames");
        appendLine("        });");
        blankLine();
        appendLine("    }");

    }


    private void renderValidateFieldFunctions() {

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef -> {

            blankLine();
            blankLine();
            appendLine("    validate%s(validationResult) {", classFieldDef.getClassFieldName().firstToUpper());

            if (classFieldDef.hasValidationConstraint(ValidationConstraint.NOT_EMPTY) || classFieldDef.hasValidationConstraint(ValidationConstraint.NOT_NULL)) {
                blankLine();
                appendLine("        if (this.state.%s.trim() == '') {", classFieldDef.getClassFieldName());
                appendLine("            validationResult.addError('%s', 'Required field.');", classFieldDef.getClassFieldName());
                appendLine("        }");
            }

            if (classFieldDef.hasValidationConstraint(ValidationConstraint.EMAIL)) {
                blankLine();
                appendLine("        if (this.validationUtil.isInvalidEmail(this.state.%s.trim())) {", classFieldDef.getClassFieldName(), classFieldDef.getClassFieldName());
                appendLine("            validationResult.addError('%s', 'Not a valid email address.');", classFieldDef.getClassFieldName());
                appendLine("        }");
            }

            blankLine();
            appendLine("    }");

        });

    }
    
    
    private void renderFunction_determineFormFieldClassNames() {

        blankLine();
        blankLine();
        appendLine("    determineFormFieldClassNames(validationResult) {");
        blankLine();

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef ->
            appendLine("        const %sClassNames = this.getClassNamesFor('%s', validationResult);", classFieldDef.getClassFieldName(), classFieldDef.getClassFieldName())
        );

        blankLine();
        appendLine("        return {");

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef ->
                appendLine("            %s: %sClassNames,", classFieldDef.getClassFieldName(), classFieldDef.getClassFieldName())
        );

        appendLine("        };");
        blankLine();
        appendLine("    }");


    }

    
    private void renderFunction_getClassNamesFor() {

        blankLine();
        blankLine();
        appendLine("    getClassNamesFor(propertyName, validationResult) {");
        blankLine();
        appendLine("        if (validationResult.isFieldHasError(propertyName)) {");
        appendLine("            return 'form-group has-error';");
        appendLine("        } else if (validationResult.isFieldHasWarning(propertyName)) {");
        appendLine("            return 'form-group has-warning';");
        appendLine("        } else {");
        appendLine("            return 'form-group';");
        appendLine("        }");
        blankLine();
        appendLine("    }");

    }

    
    private void renderFunction_getFormFieldState() {

        blankLine();
        blankLine();
        appendLine("    getFormFieldState() {");
        blankLine();
        appendLine("        return {");
        appendLine("                firstName: this.state.firstName.trim(),");
        appendLine("                lastName: this.state.lastName.trim(),");
        appendLine("                emailAddress: this.state.emailAddress.trim(),");
        appendLine("                password: this.state.password.trim()");
        appendLine("        };");
        blankLine();
        appendLine("    }");

    }
    
    
    private void renderFunction_onSubmit() {

        blankLine();
        blankLine();
        appendLine("    onSubmit(e) {");
        blankLine();
        appendLine("        e.preventDefault();");
        blankLine();
        appendLine("        var hasErrors = this.validateForm();");
        blankLine();
        appendLine("        if (hasErrors) {");
        appendLine("            this.setState({previouslySubmitted: true});");
        appendLine("            return;");
        appendLine("        }");
        blankLine();
        appendLine("        var formFieldState = this.getFormFieldState();");
        blankLine();
        appendLine("        client({");
        appendLine("            path: '/api/%s',", this.htmlFormDef.getFormSubmissionPath());
        appendLine("            entity: formFieldState,");
        appendLine("            headers: {");
        appendLine("                'Content-Type': 'application/json'");
        appendLine("            }");
        appendLine("        })");
        appendLine("        .then(");
        appendLine("            this.handleSuccessResponse,");
        appendLine("            this.handleErrorResponse");
        appendLine("        ).catch(function(error) {");
        appendLine("            console.log('An unexpected error: ' + error);");
        appendLine("        });");
        blankLine();
        appendLine("    }");
        
    }
    
    
    private void renderFunction_handleSuccessResponse() {

        blankLine();
        blankLine();
        appendLine("    handleSuccessResponse(response) {");
        blankLine();
        appendLine("        this.setState({");

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef ->
            appendLine("            %s: '',", classFieldDef.getClassFieldName())
        );

        appendLine("        });");
        blankLine();
        appendLine("        window.location.assign('%s');", this.htmlFormDef.getOnSuccessUrl().orElseThrow(() -> new RuntimeException("onSuccessUrl has not been set on DTO " + this.htmlFormDef)));
        blankLine();
        appendLine("    }");

    }
    
    
    private void renderFunction_handleErrorResponse() {

        blankLine();
        blankLine();
        appendLine("    handleErrorResponse(response) {");
        blankLine();
        appendLine("        console.log('Handling an error response: ' + response);");
        blankLine();
        appendLine("        this.setState({previouslySubmitted: true});");
        blankLine();
        appendLine("        if (response.status.code == 400) {");
        appendLine("            this.handleBadRequest(response);");
        appendLine("        } else {");
        appendLine("            console.log('No handler for error response: ' + response);");
        appendLine("        }");
        blankLine();
        appendLine("    }");

    }
    
    
    private void renderFunction_handleBadRequest() {

        blankLine();
        blankLine();
        appendLine("    handleBadRequest(response) {");
        blankLine();
        appendLine("        var errors = response.entity.errors;");
        appendLine("        const validationResult = new ValidationResult();");
        blankLine();
        appendLine("        for (var e of errors) {");
        appendLine("            validationResult.addError(e.field, e.defaultMessage);");
        appendLine("        }");
        blankLine();
        appendLine("        this.updateStateWith(validationResult);");
        blankLine();
        appendLine("    }");
        
    }
    
    
    private void renderFunction_render() {

        blankLine();
        blankLine();
        appendLine("    render() {");
        appendLine("        return (");
        appendLine("            <form className=\"form-horizontal\" onSubmit={this.onSubmit}>");

        this.htmlFormDef.getAllHtmlFormFields().forEach(classFieldDef -> {

            appendLine("                <div className={this.state.formFieldClassNames.%s}>", classFieldDef.getClassFieldName());
            appendLine("                    <label htmlFor=\"%s\" className=\"col-sm-2 control-label\">%s</label>", classFieldDef.getClassFieldName(), classFieldDef.getLabel());
            appendLine("                    <div className=\"col-sm-5\">");
            appendLine("                        <input");
            appendLine("                            id=\"%s\"", classFieldDef.getClassFieldName());

            classFieldDef.getPlaceholder().ifPresent(placeholder ->
                    appendLine("                            placeholder=\"%s\"", placeholder)
            );

            appendLine("                            type=\"%s\"", classFieldDef.getHtmlInputType());
            appendLine("                            className=\"form-control\"");
            appendLine("                            value={this.state.%s}", classFieldDef.getClassFieldName());
            appendLine("                            onChange={this.onChange%s} />", classFieldDef.getClassFieldName().firstToUpper());
            appendLine("                    </div>");
            appendLine("                    { this.state.validationMessages.%s ? <div className=\"col-sm-5\"><span>{this.state.validationMessages.%s.message}</span></div> : null }", classFieldDef.getClassFieldName(), classFieldDef.getClassFieldName());
            appendLine("                </div>");
        });

        appendLine("                <div className=\"form-group\">");
        appendLine("                    <div className=\"col-sm-offset-2 col-sm-10\">");
        appendLine("                        <button type=\"submit\" className=\"btn btn-default\">Submit</button>");
        appendLine("                    </div>");
        appendLine("                </div>");
        appendLine("            </form>");
        appendLine("        );");
        appendLine("    }");
    
    }
    
    
}
