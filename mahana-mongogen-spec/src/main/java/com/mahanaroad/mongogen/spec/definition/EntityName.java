package com.mahanaroad.mongogen.spec.definition;

import com.mahanaroad.mongogen.types.StringType;


public final class EntityName extends StringType<EntityName> {


    /**
     * @param value Must not be blank.
     * @throws com.mahanaroad.mongogen.BlankStringException if {@code value} is blank.
     */
    public EntityName(final String value) {
        super(value);
    }


}
