package com.mahanaroad.mongogen.types;


import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class LongType<T extends LongType> implements Comparable<T> {


    private final long value;


    protected LongType(long value) {

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

        final LongType that = (LongType) o;

        return this.value == that.value;

    }


    @Override
    public int hashCode() {

        return Long.hashCode(this.value);

    }


    @Override
    public final String toString() {

        return String.valueOf(this.value);

    }


    @JsonValue
    public final long getValue() {

        return this.value;

    }


    @Override
    public int compareTo(final T that) {

        return Long.compare(this.value, that.getValue());

    }


    public final boolean isLessThan(final T other) {

        return Long.compare(getValue(), other.getValue()) < 0;

    }


    public final boolean isGreaterThan(final T other) {

        return Long.compare(getValue(), other.getValue()) > 0;

    }


    public final T plus(final T other) {

        return plus(other.getValue());

    }


    public final T plus(final long increment) {

        final long sum = getValue() + increment;

        try {
            final Constructor<? extends LongType> constructor = getClass().getConstructor(long.class);
            //noinspection unchecked
            return (T) constructor.newInstance(sum);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Should never happen");
        }

    }


    public final T subtract(final T other) {

        return subtract(other.getValue());

    }


    public final T subtract(final long decrement) {

        final long newValue = getValue() - decrement;

        try {
            final Constructor<? extends LongType> constructor = getClass().getConstructor(long.class);
            //noinspection unchecked
            return (T) constructor.newInstance(newValue);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Should never happen");
        }

    }


}
