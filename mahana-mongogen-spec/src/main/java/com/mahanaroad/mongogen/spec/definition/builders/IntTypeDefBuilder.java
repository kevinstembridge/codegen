package com.mahanaroad.mongogen.spec.definition.builders;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.spec.definition.IntTypeDef;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;

import java.util.Objects;

public final class IntTypeDefBuilder {


    private final Fqcn fqcn;
    private boolean provided;


    public IntTypeDefBuilder(final PackageName basePackage, final PackageName subPackage, final String typeName) {

        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(subPackage, "subPackage");
        BlankStringException.throwIfBlank(typeName, "typeName");

        this.fqcn = Fqcn.valueOf(basePackage.plusSubPackage(subPackage), new Uqcn(typeName));

    }


    public IntTypeDefBuilder(final String rawFqcn) {

        BlankStringException.throwIfBlank(rawFqcn, "rawFqcn");

        this.fqcn = Fqcn.valueOf(rawFqcn);

    }


    public IntTypeDefBuilder provided() {

        this.provided = true;
        return this;

    }


    public IntTypeDef build() {

        return new IntTypeDef(this.fqcn, this.provided);

    }


}
