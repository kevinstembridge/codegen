package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.types.StringType;


public final class HtmlFormName extends StringType<HtmlFormName> {


    /**
     * @param value Must not be blank.
     * @throws com.mahanaroad.mongogen.BlankStringException if {@code value} is blank.
     */
    public HtmlFormName(final String value) {
        super(value);
    }


}
