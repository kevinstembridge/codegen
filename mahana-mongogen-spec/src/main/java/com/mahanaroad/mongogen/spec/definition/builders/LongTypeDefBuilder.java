package com.mahanaroad.mongogen.spec.definition.builders;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.spec.definition.LongTypeDef;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;

import java.util.Objects;

public final class LongTypeDefBuilder {


    private final Fqcn fqcn;
    private boolean provided;


    public LongTypeDefBuilder(final PackageName basePackage, final PackageName subPackage, final String typeName) {

        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(subPackage, "subPackage");
        BlankStringException.throwIfBlank(typeName, "typeName");

        this.fqcn = Fqcn.valueOf(basePackage.plusSubPackage(subPackage), new Uqcn(typeName));

    }


    public LongTypeDefBuilder(final String rawFqcn) {

        BlankStringException.throwIfBlank(rawFqcn, "rawFqcn");

        this.fqcn = Fqcn.valueOf(rawFqcn);

    }


    public LongTypeDefBuilder provided() {

        this.provided = true;
        return this;

    }


    public LongTypeDef build() {

        return new LongTypeDef(this.fqcn, this.provided);

    }


}
