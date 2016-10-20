package com.mahanaroad.mongogen.spec.definition.java;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DefaultFieldTypeValues {


    private static final Map<FieldType, String> MAPPING = new HashMap<FieldType, String>() {{
        put(FieldType.BOOLEAN, "false");
        put(FieldType.STRING, "null");
        put(FieldType.SHORT, "TODO");
        put(FieldType.LONG, "OptionalLong.empty()");
        put(FieldType.INT, "OptionalInt.empty()");
        put(FieldType.BYTE, "TODO");
        put(FieldType.CHAR, "Optional.empty()");
        put(FieldType.DOUBLE, "TODO");
        put(FieldType.FLOAT, "TODO");
        put(FieldType.INSTANT, "null");
        put(FieldType.OBJECT_ID, "null");
    }};


    public static String forFieldType(final FieldType fieldType) {

        Objects.requireNonNull(fieldType, "fieldType");
        final String value = MAPPING.get(fieldType);

        if (value == null) {
            throw new RuntimeException("No mapping for FieldType " + fieldType);
        }

        return value;

    }


}
