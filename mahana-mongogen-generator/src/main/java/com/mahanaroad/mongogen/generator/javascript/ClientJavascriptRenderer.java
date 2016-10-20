package com.mahanaroad.mongogen.generator.javascript;

import com.mahanaroad.mongogen.generator.AbstractRenderer;

public class ClientJavascriptRenderer extends AbstractRenderer {


    public ClientJavascriptRenderer() {
        

    }


    @Override
    protected String createFilePath() {

        return "client.js";

    }


    @Override
    protected String renderSource() {

        appendLine("'use strict';");
        blankLine();
        appendLine("var rest = require('rest');");
        appendLine("var defaultRequest = require('rest/interceptor/defaultRequest');");
        appendLine("var mime = require('rest/interceptor/mime');");
        appendLine("var errorCode = require('rest/interceptor/errorCode');");
        appendLine("var baseRegistry = require('rest/mime/registry');");
        blankLine();
        appendLine("var registry = baseRegistry.child();");
        blankLine();
        appendLine("registry.register('application/json', require('rest/mime/type/application/json'));");
        blankLine();
        appendLine("module.exports = rest");
        appendLine("        .wrap(mime, { registry: registry })");
		appendLine("        .wrap(errorCode);");

        return getSourceCode();

    }


}
