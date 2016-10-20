package com.mahanaroad.mongogen.types;


import com.fasterxml.jackson.annotation.JsonValue;

public abstract class IntType<T extends IntType> implements Comparable<T> {


    private final int value;


    protected IntType(int value) {

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

        final IntType that = (IntType) o;

        return this.value == that.value;

    }


    @Override
    public int hashCode() {

        return this.value;

    }


    @Override
    public final String toString() {

        return String.valueOf(this.value);

    }


    @JsonValue
    public final int getValue() {

        return this.value;

    }


    @Override
    public int compareTo(final T that) {

        return Integer.compare(this.value, that.getValue());

    }


}
