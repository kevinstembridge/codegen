package com.mahanaroad.mongogen.sample.types;

import com.mahanaroad.mongogen.types.StringType;


public class SomeProvidedStringType extends StringType<SomeProvidedStringType> {


    public SomeProvidedStringType(final String value) {

        super(value);

    }


}
