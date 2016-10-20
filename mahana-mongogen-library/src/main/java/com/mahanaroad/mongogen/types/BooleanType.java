package com.mahanaroad.mongogen.types;


import com.fasterxml.jackson.annotation.JsonValue;

public abstract class BooleanType<T extends BooleanType> implements Comparable<T> {


    private final boolean value;


    protected BooleanType(boolean value) {

        this.value = value;

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BooleanType that = (BooleanType) o;

        return this.value == that.value;

    }


    @Override
    public int hashCode() {

        return Boolean.hashCode(this.value);

    }


    @Override
    public final String toString() {

        return String.valueOf(this.value);

    }


    @JsonValue
    public final boolean getValue() {

        return this.value;

    }


    @Override
    public int compareTo(final T that) {

        return Boolean.compare(this.value, that.getValue());

    }


}
