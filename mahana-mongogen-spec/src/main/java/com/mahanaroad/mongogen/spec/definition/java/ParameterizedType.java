package com.mahanaroad.mongogen.spec.definition.java;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;


public final class ParameterizedType extends NonPrimitiveType {


    private final Fqcn fqcn;
    private final boolean isSimpleTypeWrapper;
    private final List<NonPrimitiveType> parameters = new LinkedList<>();


    public ParameterizedType(final Fqcn fqcn, final NonPrimitiveType... parameters) {

        this(fqcn, false, parameters);

    }


    public ParameterizedType(final Fqcn fqcn, boolean isSimpleTypeWrapper, final NonPrimitiveType... parameters) {

        super(fqcn, initUnqualifiedToString(fqcn, Arrays.asList(parameters)));
        Objects.requireNonNull(fqcn, "fqcn");
        Objects.requireNonNull(parameters, "parameters");

        this.fqcn = fqcn;
        this.isSimpleTypeWrapper = isSimpleTypeWrapper;
        this.parameters.addAll(Arrays.asList(parameters));

    }


    private static String initUnqualifiedToString(final Fqcn fqcn, final List<NonPrimitiveType> parameters) {

        if (parameters.isEmpty()) {
            return fqcn.getUqcn().getValue();
        } else {
            return fqcn.getUqcn() + "<" + parameters.stream().map(NonPrimitiveType::getUnqualifiedToString).collect(joining(", ")) + ">";
        }

    }


    @Override
    public ParameterizedType asParameterTo(final Fqcn outerParameterizedType) {

        return new ParameterizedType(outerParameterizedType, this.isSimpleTypeWrapper, this);

    }


    public Stream<NonPrimitiveType> getParameterStream() {

        return this.parameters.stream();

    }


    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final ParameterizedType that = (ParameterizedType) other;
        return Objects.equals(this.fqcn, that.fqcn)
                && Objects.equals(this.parameters, that.parameters);
    }


    @Override
    public int hashCode() {

        return Objects.hash(this.fqcn, this.parameters);

    }


    @Override
    public String toString() {

        if (this.parameters.isEmpty()) {
            return this.fqcn.toString();
        } else {
            return this.fqcn.toString() + "<" + this.parameters.stream().map(NonPrimitiveType::toString).collect(joining(", ")) + ">";
        }

    }


    public boolean isOptionalPrimitive() {

        return isOptionalInt() || isOptionalDouble() || isOptionalLong();

    }


    public boolean isOptionalInt() {

        return this.fqcn.equals(Fqcn.OPTIONAL_INT);

    }


    public boolean isOptionalDouble() {

        return this.fqcn.equals(Fqcn.OPTIONAL_DOUBLE);

    }


    public boolean isOptionalLong() {

        return this.fqcn.equals(Fqcn.OPTIONAL_LONG);

    }


    public boolean isList() {

        return this.fqcn.equals(Fqcn.LIST);

    }


    public boolean isMap() {

        return this.fqcn.equals(Fqcn.MAP);

    }


    @Override
    public boolean isSimpleTypeWrapper() {

        return this.isSimpleTypeWrapper;

    }


    public boolean isInstant() {

        return this.fqcn.equals(Fqcn.INSTANT);

    }


    public boolean isLocalDate() {

        return this.fqcn.equals(Fqcn.LOCAL_DATE);

    }


    public boolean isPeriod() {

        return this.fqcn.equals(Fqcn.PERIOD);

    }


    public NonPrimitiveType getFirstParameter() {

        return getParameterStream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expected at least one parameter type but found none: " + this));

    }


    public NonPrimitiveType getSecondParameter() {

        return getParameterStream()
                .skip(1)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expected at least two parameters type but found less than two: " + this));

    }


}
