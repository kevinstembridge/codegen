package com.mahanaroad.mongogen.spec.definition.builders;


import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.EnumValueDef;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class EnumDefBuilder {


    private final PackageName basePackage;
    private final PackageName subPackage;
    private final String enumName;
    private final Uqcn enumUqcn;
    private final Fqcn enumFqcn;
    private final List<EnumValueDef> enumValueDefs = new LinkedList<>();


    public EnumDefBuilder(final PackageName basePackage, final PackageName subPackage, final String enumName) {

        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(subPackage, "subPackage");
        BlankStringException.throwIfBlank(enumName, "enumName");

        this.basePackage = basePackage;
        this.subPackage = subPackage;
        this.enumName = enumName;
        this.enumUqcn = new Uqcn(this.enumName);
        this.enumFqcn = this.basePackage.plusSubPackage(this.subPackage).uqcn(this.enumUqcn);

    }


    public EnumDef build() {

        return new EnumDef(this.enumFqcn, this.enumValueDefs);

    }


    public EnumDefBuilder values(final EnumValueDef... enumValueDefs) {

        if (enumValueDefs != null) {
            this.enumValueDefs.addAll(Arrays.asList(enumValueDefs));
        }

        return this;

    }


}
