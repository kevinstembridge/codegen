package com.mahanaroad.mongogen.spec.definition.java;

public class EnumType extends NonPrimitiveType {


    public EnumType(final Fqcn fqcn) {
        super(fqcn, fqcn.getUqcn().toString());
    }


    @Override
    public ParameterizedType asParameterTo(final Fqcn fqcn) {

        return new ParameterizedType(fqcn, false, this);

    }


    @Override
    public boolean isSimpleTypeWrapper() {

        return false;

    }


}
