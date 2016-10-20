package com.mahanaroad.mongogen.spec.definition.java;


import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.types.StringType;

public final class Uqcn extends StringType<Uqcn> {


    public Uqcn(final String value) {

        super(value);

    }


    public Uqcn withPrefix(final String prefix) {

        BlankStringException.throwIfBlank(prefix, "prefix");
        return new Uqcn(prefix + getValue());

    }


    public Uqcn withSuffix(final String suffix) {

        BlankStringException.throwIfBlank(suffix, "suffix");
        return new Uqcn(getValue() + suffix);

    }


}
