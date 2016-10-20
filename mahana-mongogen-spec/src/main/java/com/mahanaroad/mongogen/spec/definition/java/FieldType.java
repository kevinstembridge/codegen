package com.mahanaroad.mongogen.spec.definition.java;


import com.mahanaroad.mongogen.persist.BsonCompatibleType;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a type of a field on a Java class. It can be either a {@link PrimitiveType}
 * or an {@link NonPrimitiveType}.
 */
public final class FieldType {


    public static final FieldType BOOLEAN = new FieldType(null, PrimitiveType.BOOLEAN, BsonCompatibleType.BOOLEAN);
    public static final FieldType BYTE = new FieldType(null, PrimitiveType.BYTE, null);
    public static final FieldType CHAR = new FieldType(null, PrimitiveType.CHAR, BsonCompatibleType.STRING);
    public static final FieldType DOUBLE = new FieldType(null, PrimitiveType.DOUBLE, BsonCompatibleType.DOUBLE);
    public static final FieldType FLOAT = new FieldType(null, PrimitiveType.FLOAT, null);
    public static final FieldType INSTANT = valueOf(Fqcn.INSTANT, BsonCompatibleType.INSTANT);
    public static final FieldType INT = new FieldType(null, PrimitiveType.INT, BsonCompatibleType.INT);
    public static final FieldType LOCAL_DATE = valueOf(Fqcn.LOCAL_DATE, BsonCompatibleType.LOCAL_DATE);
    public static final FieldType LONG = new FieldType(null, PrimitiveType.LONG, BsonCompatibleType.LONG);
    public static final FieldType OBJECT_ID = valueOf(Fqcn.valueOf("org.bson.types.ObjectId"), BsonCompatibleType.OBJECT_ID);
    public static final FieldType OPTIONAL_DOUBLE = valueOf(Fqcn.OPTIONAL_DOUBLE, BsonCompatibleType.DOUBLE);
    public static final FieldType OPTIONAL_INT = valueOf(Fqcn.OPTIONAL_INT, BsonCompatibleType.INT);
    public static final FieldType OPTIONAL_LONG = valueOf(Fqcn.OPTIONAL_LONG, BsonCompatibleType.LONG);
    public static final FieldType PERIOD = valueOf(Fqcn.PERIOD, BsonCompatibleType.PERIOD);
    public static final FieldType SHORT = new FieldType(null, PrimitiveType.SHORT, null);
    public static final FieldType STRING = valueOf(Fqcn.STRING, BsonCompatibleType.STRING);


    public static FieldType valueOf(final Fqcn fqcn) {

        return valueOf(fqcn, null);

    }


    public static FieldType valueOf(final Fqcn fqcn, final BsonCompatibleType bsonCompatibleType) {

        return new FieldType(new ParameterizedType(fqcn), null, bsonCompatibleType);

    }


    public static FieldType valueOf(final ParameterizedType parameterizedType) {

        return valueOf(parameterizedType, null);

    }


    public static FieldType valueOf(final ParameterizedType parameterizedType, final BsonCompatibleType BsonCompatibleType) {

        return new FieldType(parameterizedType, null, BsonCompatibleType);

    }


    public static FieldType valueOf(final NonPrimitiveType nonPrimitiveType, final BsonCompatibleType bsonCompatibleType) {

        return new FieldType(nonPrimitiveType, null, bsonCompatibleType);

    }


    // This can only be null in the case of a primitive fieldType
    private final NonPrimitiveType nonPrimitiveType;


    // This will be null if this instance represents a non-primitive type.
    private final PrimitiveType primitiveType;


    private final Optional<BsonCompatibleType> bsonCompatibleType;


    private FieldType(
            final NonPrimitiveType nonPrimitiveType,
            final PrimitiveType primitiveType,
            final BsonCompatibleType bsonCompatibleType) {

        this.nonPrimitiveType = nonPrimitiveType;
        this.primitiveType = primitiveType;
        this.bsonCompatibleType = Optional.ofNullable(bsonCompatibleType);

    }


    public boolean isPrimitive() {

        return this.primitiveType != null;

    }


    public boolean isNotPrimitive() {

        return isPrimitive() == false;

    }


    /**
     * @return Possibly null if this instance represents a primitive type.
     * @see #isPrimitive
     */
    public Optional<NonPrimitiveType> getNonPrimitiveType() {

        return Optional.ofNullable(this.nonPrimitiveType);

    }


    public Optional<ParameterizedType> getParameterizedTypeOptional() {

        return Optional.ofNullable(this.nonPrimitiveType)
                .filter(nonPrimitiveType -> nonPrimitiveType instanceof ParameterizedType)
                .map(parameterizedType -> (ParameterizedType) parameterizedType);

    }


    public Optional<EnumType> getEnumTypeOptional() {

        return Optional.ofNullable(this.nonPrimitiveType)
                .filter(nonPrimitiveType -> nonPrimitiveType instanceof EnumType)
                .map(enumType -> (EnumType) enumType);

    }


    public String getUnqualifiedToString() {

        if (isPrimitive()) {
            return this.primitiveType.toString();
        } else {
            return this.nonPrimitiveType.getUnqualifiedToString();
        }

    }


    public boolean isString() {

        return this.nonPrimitiveType != null && Fqcn.STRING.equals(this.nonPrimitiveType.getFqcn());

    }


    public FieldType asOptional() {

        if (this.primitiveType != null) {
            return this.primitiveType.asOptional();
        } else {
            final ParameterizedType newParameterizedType = this.nonPrimitiveType.asParameterTo(Fqcn.OPTIONAL);
            return new FieldType(newParameterizedType, null, this.bsonCompatibleType.orElse(null));
        }

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final FieldType that = (FieldType) o;

        return Objects.equals(this.nonPrimitiveType, that.nonPrimitiveType)
                && this.primitiveType == that.primitiveType;

    }


    @Override
    public int hashCode() {

        return Objects.hash(this.nonPrimitiveType, this.primitiveType);

    }


    @Override
    public String toString() {

        if (isPrimitive()) {
            return this.primitiveType.toString();
        } else {
            return this.nonPrimitiveType.toString();
        }

    }


    public boolean isInstant() {

        return isPrimitive() == false && this.nonPrimitiveType.getFqcn().equals(Fqcn.INSTANT);

    }


    public boolean isLocalDate() {

        return isPrimitive() == false && this.nonPrimitiveType.getFqcn().equals(Fqcn.LOCAL_DATE);

    }


    public boolean isPeriod() {

        return isPrimitive() == false && this.nonPrimitiveType.getFqcn().equals(Fqcn.PERIOD);

    }


    public boolean isInt() {

        return isPrimitiveType(PrimitiveType.INT);

    }


    private boolean isPrimitiveType(final PrimitiveType type) {

        return this.primitiveType == type;

    }


    private boolean isOptionalInt() {

        return getParameterizedTypeOptional().map(ParameterizedType::isOptionalInt).orElse(false);

    }


    private boolean isOptionalLong() {

        return getParameterizedTypeOptional().map(ParameterizedType::isOptionalLong).orElse(false);

    }


    private boolean isOptionalDouble() {

        return getParameterizedTypeOptional().map(ParameterizedType::isOptionalDouble).orElse(false);

    }


    public FieldType unwrapIfOptional() {

        if (isPrimitive()) {
            return this;
        }

        if (isOptionalInt()) {
            return FieldType.INT;
        }

        if (isOptionalLong()) {
            return FieldType.LONG;
        }

        if (isOptionalDouble()) {
            return FieldType.DOUBLE;
        }

        if (this.nonPrimitiveType instanceof ParameterizedType && this.nonPrimitiveType.getFqcn().equals(Fqcn.OPTIONAL)) {

            final ParameterizedType optionalParameterizedType = (ParameterizedType) this.nonPrimitiveType;
            final NonPrimitiveType optionalPayloadType = optionalParameterizedType.getParameterStream().findFirst().orElseThrow(() -> new RuntimeException("Expected to find a single type parameter but found none"));
            return new FieldType(optionalPayloadType, null, this.bsonCompatibleType.orElse(null));

        }

        return this;

    }


    public FieldType boxIfPrimitive() {

        if (isPrimitive()) {
            return new FieldType(new ParameterizedType(this.primitiveType.autoboxed(), false), null, this.bsonCompatibleType.orElse(null));
        }

        return this;

    }


    public boolean isList() {

        return getParameterizedTypeOptional().map(ParameterizedType::isList).orElse(false);

    }


    public boolean isMap() {

        return getParameterizedTypeOptional().map(ParameterizedType::isMap).orElse(false);

    }


    public NonPrimitiveType getFirstParameterType() {

        return getParameterizedTypeOptional()
                .map(ParameterizedType::getFirstParameter)
                .orElseThrow(() -> new IllegalStateException("Cannot get type parameter because this is not a ParameterizedType: " + this));

    }


    public NonPrimitiveType getSecondParameterType() {

        return getParameterizedTypeOptional()
                .map(ParameterizedType::getSecondParameter)
                .orElseThrow(() -> new IllegalStateException("Cannot get type parameter because this is not a ParameterizedType: " + this));

    }


    public Optional<BsonCompatibleType> getBsonCompatibleType() {

        return this.bsonCompatibleType;

    }


    public boolean isSimpleTypeWrapper() {

        return getParameterizedTypeOptional().map(ParameterizedType::isSimpleTypeWrapper).orElse(false);

    }


    public boolean isEnum() {

        return getEnumTypeOptional().isPresent();

    }


    public boolean isCollection() {

        return isList() || isMap();

    }


    public boolean isListOfSimpleTypeWrappers() {

        return isList() && getParameterizedTypeOptional().map(parameterizedType ->  parameterizedType.getFirstParameter().isSimpleTypeWrapper()).orElse(false);

    }


}
