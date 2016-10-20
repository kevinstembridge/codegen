package com.mahanaroad.mongogen.spec.definition.builders;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.spec.definition.StringTypeDef;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;

import java.util.Objects;

public final class StringTypeDefBuilder {


    private final Fqcn fqcn;
    private boolean provided;
    private StringTypeDef.CaseMode caseMode = StringTypeDef.CaseMode.AS_PROVIDED;


    public StringTypeDefBuilder(final PackageName basePackage, final PackageName subPackage, final String typeName) {

        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(subPackage, "subPackage");
        BlankStringException.throwIfBlank(typeName, "typeName");

        this.fqcn = Fqcn.valueOf(basePackage.plusSubPackage(subPackage), new Uqcn(typeName));

    }


    public StringTypeDefBuilder(final String rawFqcn) {

        BlankStringException.throwIfBlank(rawFqcn, "rawFqcn");

        this.fqcn = Fqcn.valueOf(rawFqcn);

    }


    public StringTypeDefBuilder provided() {

        this.provided = true;
        return this;

    }


    public StringTypeDefBuilder alwaysUpperCase() {

        this.caseMode = StringTypeDef.CaseMode.ALWAYS_UPPER;
        return this;

    }


    public StringTypeDefBuilder alwaysLowerCase() {

        this.caseMode = StringTypeDef.CaseMode.ALWAYS_LOWER;
        return this;

    }


    public StringTypeDef build() {

        return new StringTypeDef(this.fqcn, this.provided, this.caseMode);

    }


}
