package com.mahanaroad.mongogen.spec.definition.java;

import com.mahanaroad.mongogen.persist.BsonCompatibleType;

public enum PrimitiveType {

    BOOLEAN(Fqcn.valueOf("java.lang.Boolean"), BsonCompatibleType.BOOLEAN),

    BYTE(Fqcn.valueOf("java.lang.Byte"), null),

    SHORT(Fqcn.valueOf("java.lang.Short"), null),

    CHAR(Fqcn.valueOf("java.lang.Character"), BsonCompatibleType.STRING),

    INT(Fqcn.valueOf("java.lang.Integer"), BsonCompatibleType.INTEGER) {
        @Override
        public FieldType asOptional() {
            return FieldType.OPTIONAL_INT;
        }
    },

    FLOAT(Fqcn.valueOf("java.lang.Float"), null),

    LONG(Fqcn.valueOf("java.lang.Long"), BsonCompatibleType.LONG) {
        @Override
        public FieldType asOptional() {
            return FieldType.OPTIONAL_LONG;
        }
    },

    DOUBLE(Fqcn.valueOf("java.lang.Double"), BsonCompatibleType.DOUBLE) {
        @Override
        public FieldType asOptional() {
            return FieldType.OPTIONAL_DOUBLE;
        }
    };


    private final Fqcn autoboxedFqcn;
    private final BsonCompatibleType bsonCompatibleType;


    PrimitiveType(final Fqcn autoboxed, BsonCompatibleType bsonCompatibleType) {

        this.autoboxedFqcn = autoboxed;
        this.bsonCompatibleType = bsonCompatibleType;

    }


    @Override
    public String toString() {

        return name().toLowerCase();

    }


    public Fqcn autoboxed() {

        return this.autoboxedFqcn;

    }


    public FieldType asOptional() {

        final ParameterizedType parameterizedType = new ParameterizedType(Fqcn.OPTIONAL, new ParameterizedType(autoboxed()));
        return FieldType.valueOf(parameterizedType, this.bsonCompatibleType);

    }


}
