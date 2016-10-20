package com.mahanaroad.mongogen.spec.definition;


import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


public final class IndexDef {


    private final List<EntityFieldDef> entityFieldDefs = new LinkedList<>();


    public IndexDef(final List<EntityFieldDef> entityFieldDefs) {

        this.entityFieldDefs.addAll(entityFieldDefs);

    }


    public Stream<EntityFieldDef> getFieldDefStream() {

        return this.entityFieldDefs.stream();

    }


}
