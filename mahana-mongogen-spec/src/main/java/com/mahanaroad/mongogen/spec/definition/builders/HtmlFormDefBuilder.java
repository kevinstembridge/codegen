package com.mahanaroad.mongogen.spec.definition.builders;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.spec.definition.HtmlFormDef;
import com.mahanaroad.mongogen.spec.definition.HtmlFormKey;
import com.mahanaroad.mongogen.spec.definition.HtmlFormName;
import com.mahanaroad.mongogen.spec.definition.java.HtmlFormFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public final class HtmlFormDefBuilder {

    private final List<HtmlFormFieldDefBuilder> fieldDefBuilders = new LinkedList<>();
    private final PackageName basePackage;
    private final PackageName subPackage;
    private final HtmlFormName htmlFormName;
    private Optional<HtmlFormKey> dtoKeyOptional = Optional.empty();
    private Optional<String> onSuccessUrl = Optional.empty();


    public HtmlFormDefBuilder(
            final PackageName basePackage,
            final PackageName subPackage,
            final HtmlFormName htmlFormName) {

        this.basePackage = Objects.requireNonNull(basePackage, "basePackage");
        this.subPackage = Objects.requireNonNull(subPackage, "subPackage");
        this.htmlFormName = Objects.requireNonNull(htmlFormName, "htmlFormName");

    }


    public HtmlFormDef build() {

        final List<HtmlFormFieldDef> fieldDefs = buildFieldDefs();

        return HtmlFormDef.newInstance(
                this.dtoKeyOptional.orElse(new HtmlFormKey(this.htmlFormName.getValue())),
                this.htmlFormName,
                this.basePackage,
                this.subPackage,
                fieldDefs,
                this.onSuccessUrl);

    }


    private List<HtmlFormFieldDef> buildFieldDefs() {

        return this.fieldDefBuilders.stream().map(HtmlFormFieldDefBuilder::build).collect(toList());

    }


    public HtmlFormDefBuilder fields(final HtmlFormFieldDefBuilder... fieldDefBuilders) {

        if (fieldDefBuilders != null) {
            Collections.addAll(this.fieldDefBuilders, fieldDefBuilders);
        }

        return this;

    }


    public HtmlFormDefBuilder dtoKey(final String dtoKey) {

        BlankStringException.throwIfBlank(dtoKey, "dtoKey");

        this.dtoKeyOptional = Optional.of(new HtmlFormKey(dtoKey));
        return this;

    }


    public HtmlFormDefBuilder onSuccessUrl(String successUrl) {

        this.onSuccessUrl = Optional.of(successUrl);
        return this;

    }


}
