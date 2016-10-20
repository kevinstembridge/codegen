package com.mahanaroad.mongogen.spec.definition.java;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.types.StringType;

public final class ClassFieldName extends StringType<ClassFieldName> {


    public ClassFieldName(final String value) {

        super(value);

    }


    public ClassFieldName withSuffix(final String suffix) {

        BlankStringException.throwIfBlank(suffix, "suffix");
        return new ClassFieldName(getValue() + suffix);

    }



}
