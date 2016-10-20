package com.mahanaroad.mongogen.spec.definition;


import com.mahanaroad.mongogen.BlankStringException;

public final class EnumValueDef {


    private final String name;


    public EnumValueDef(final String name) {

        BlankStringException.throwIfBlank(name, "name");

        this.name = name;

    }


    public String getName() {

        return this.name;

    }


}
