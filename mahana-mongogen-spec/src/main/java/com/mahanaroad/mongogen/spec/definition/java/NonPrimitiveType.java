package com.mahanaroad.mongogen.spec.definition.java;

import com.mahanaroad.mongogen.BlankStringException;

import java.util.Objects;

public abstract class NonPrimitiveType {

    private final Fqcn fqcn;
    private final String unqualifiedToString;


    protected NonPrimitiveType(final Fqcn fqcn, final String unqualifiedToString) {

        this.fqcn = Objects.requireNonNull(fqcn, "fqcn");
        this.unqualifiedToString = BlankStringException.throwIfBlank(unqualifiedToString, "getUnqualifiedToString");

    }


    public final String getUnqualifiedToString() {

        return this.unqualifiedToString;

    }


    public final Fqcn getFqcn() {

        return this.fqcn;

    }


    public abstract ParameterizedType asParameterTo(final Fqcn fqcn);


    public abstract boolean isSimpleTypeWrapper();


}
