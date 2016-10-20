package com.mahanaroad.mongogen.spec.definition.java;

import com.mahanaroad.mongogen.types.StringType;

public final class PackageName extends StringType<PackageName> {


    public PackageName(final String value) {
        super(value);
    }


    public PackageName plusSubPackage(final PackageName subPackage) {

        return new PackageName(getValue() + "." + subPackage);

    }


    public Fqcn uqcn(final Uqcn uqcn) {

        return Fqcn.valueOf(this, uqcn);

    }


}
