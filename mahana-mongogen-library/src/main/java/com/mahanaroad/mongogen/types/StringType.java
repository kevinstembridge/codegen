package com.mahanaroad.mongogen.types;


import com.fasterxml.jackson.annotation.JsonValue;
import com.mahanaroad.mongogen.BlankStringException;

import java.util.function.Function;

public abstract class StringType<T extends StringType> implements Comparable<T> {


    private final String value;


    protected StringType(String value) {

        BlankStringException.throwIfBlank(value, "value");
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

        final StringType that = (StringType) o;

        return this.value.equals(that.value);

    }


    @Override
    public int hashCode() {

        return this.value.hashCode();

    }


    @Override
    public final String toString() {

        return this.value;

    }


    @JsonValue
    public final String getValue() {

        return this.value;

    }


    @Override
    public int compareTo(final T that) {

        return this.value.compareTo(that.getValue());

    }


    public String firstToUpper() {

        return transformFirstChar(Character::toUpperCase);

    }


    public String firstToLower() {

        return transformFirstChar(Character::toLowerCase);

    }


    private String transformFirstChar(final Function<Character, Character> f) {

        final char[] chars = this.value.toCharArray();
        chars[0] = f.apply(chars[0]);
        return new String(chars);

    }


}
