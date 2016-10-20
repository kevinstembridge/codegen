// This source was generated by the Mahana Mongogen generator
// Renderer class: class com.mahanaroad.mongogen.generator.entities.EntityRenderer
// Rendered at: 2016-09-30T22:30:59.702Z

package com.mahanaroad.mongogen.sample.simple;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.domain.AbstractEntity;
import com.mahanaroad.mongogen.sample.types.SomeBooleanType;
import com.mahanaroad.mongogen.sample.types.SomeIntType;
import com.mahanaroad.mongogen.sample.types.SomeLongType;
import com.mahanaroad.mongogen.sample.types.SomeProvidedBooleanType;
import com.mahanaroad.mongogen.sample.types.SomeProvidedIntType;
import com.mahanaroad.mongogen.sample.types.SomeProvidedLongType;
import com.mahanaroad.mongogen.sample.types.SomeProvidedStringType;
import com.mahanaroad.mongogen.sample.types.SomeStringType;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import org.bson.types.ObjectId;


public class SimpleEntity extends AbstractEntity {


    private final boolean someBoolean;

    private final SomeBooleanType someBooleanType;

    private final SomeIntType someIntType;

    private final List<SomeStatus> someListOfEnums;

    private final List<Instant> someListOfInstants;

    private final List<LocalDate> someListOfLocalDates;

    private final List<Period> someListOfPeriods;

    private final List<SomeStringType> someListOfStringTypes;

    private final List<String> someListOfStrings;

    private final SomeLongType someLongType;

    private final Map<String, Integer> someMapOfStringToInteger;

    private final Map<SomeStringType, SomeStringType> someMapOfStringTypeToStringType;

    private Instant someModifiableInstant;

    private int someModifiableInt;

    private LocalDate someModifiableLocalDate;

    private Period someModifiablePeriod;

    private String someModifiableString;

    private final String someNonNullableString;

    private final Optional<Boolean> someOptionalBoolean;

    private final Optional<SomeBooleanType> someOptionalBooleanType;

    private final Optional<Instant> someOptionalInstant;

    private OptionalInt someOptionalInt;

    private final Optional<SomeIntType> someOptionalIntType;

    private final Optional<SomeLongType> someOptionalLongType;

    private Optional<Instant> someOptionalModifiableInstant;

    private final Optional<Period> someOptionalPeriod;

    private final Optional<SomeProvidedBooleanType> someOptionalProvidedBooleanType;

    private final Optional<SomeProvidedIntType> someOptionalProvidedIntType;

    private final Optional<SomeProvidedLongType> someOptionalProvidedLongType;

    private final Optional<SomeProvidedStringType> someOptionalProvidedStringType;

    private final Optional<SomeStatus> someOptionalStatus;

    private final Optional<String> someOptionalString;

    private final Optional<SomeStringType> someOptionalStringType;

    private final SomeProvidedBooleanType someProvidedBooleanType;

    private final SomeProvidedIntType someProvidedIntType;

    private final SomeProvidedLongType someProvidedLongType;

    private final SomeProvidedStringType someProvidedStringType;

    private final SomeStatus someStatus;

    private final SomeStringType someStringType;


    public SimpleEntity(
            final Instant createdTimestampUtc,
            final ObjectId id,
            final Optional<Instant> lastModifiedTimestampUtc,
            final boolean someBoolean,
            final SomeBooleanType someBooleanType,
            final SomeIntType someIntType,
            final List<SomeStatus> someListOfEnums,
            final List<Instant> someListOfInstants,
            final List<LocalDate> someListOfLocalDates,
            final List<Period> someListOfPeriods,
            final List<SomeStringType> someListOfStringTypes,
            final List<String> someListOfStrings,
            final SomeLongType someLongType,
            final Map<String, Integer> someMapOfStringToInteger,
            final Map<SomeStringType, SomeStringType> someMapOfStringTypeToStringType,
            final Instant someModifiableInstant,
            final int someModifiableInt,
            final LocalDate someModifiableLocalDate,
            final Period someModifiablePeriod,
            final String someModifiableString,
            final String someNonNullableString,
            final Optional<Boolean> someOptionalBoolean,
            final Optional<SomeBooleanType> someOptionalBooleanType,
            final Optional<Instant> someOptionalInstant,
            final OptionalInt someOptionalInt,
            final Optional<SomeIntType> someOptionalIntType,
            final Optional<SomeLongType> someOptionalLongType,
            final Optional<Instant> someOptionalModifiableInstant,
            final Optional<Period> someOptionalPeriod,
            final Optional<SomeProvidedBooleanType> someOptionalProvidedBooleanType,
            final Optional<SomeProvidedIntType> someOptionalProvidedIntType,
            final Optional<SomeProvidedLongType> someOptionalProvidedLongType,
            final Optional<SomeProvidedStringType> someOptionalProvidedStringType,
            final Optional<SomeStatus> someOptionalStatus,
            final Optional<String> someOptionalString,
            final Optional<SomeStringType> someOptionalStringType,
            final SomeProvidedBooleanType someProvidedBooleanType,
            final SomeProvidedIntType someProvidedIntType,
            final SomeProvidedLongType someProvidedLongType,
            final SomeProvidedStringType someProvidedStringType,
            final SomeStatus someStatus,
            final SomeStringType someStringType) {

        super(createdTimestampUtc, id, lastModifiedTimestampUtc);

        Objects.requireNonNull(someBooleanType, "someBooleanType");
        Objects.requireNonNull(someIntType, "someIntType");
        Objects.requireNonNull(someListOfEnums, "someListOfEnums");
        Objects.requireNonNull(someListOfInstants, "someListOfInstants");
        Objects.requireNonNull(someListOfLocalDates, "someListOfLocalDates");
        Objects.requireNonNull(someListOfPeriods, "someListOfPeriods");
        Objects.requireNonNull(someListOfStringTypes, "someListOfStringTypes");
        Objects.requireNonNull(someListOfStrings, "someListOfStrings");
        Objects.requireNonNull(someLongType, "someLongType");
        Objects.requireNonNull(someMapOfStringToInteger, "someMapOfStringToInteger");
        Objects.requireNonNull(someMapOfStringTypeToStringType, "someMapOfStringTypeToStringType");
        Objects.requireNonNull(someModifiableInstant, "someModifiableInstant");
        Objects.requireNonNull(someModifiableLocalDate, "someModifiableLocalDate");
        Objects.requireNonNull(someModifiablePeriod, "someModifiablePeriod");
        BlankStringException.throwIfBlank(someModifiableString, "someModifiableString");
        BlankStringException.throwIfBlank(someNonNullableString, "someNonNullableString");
        Objects.requireNonNull(someOptionalBoolean, "someOptionalBoolean");
        Objects.requireNonNull(someOptionalBooleanType, "someOptionalBooleanType");
        Objects.requireNonNull(someOptionalInstant, "someOptionalInstant");
        Objects.requireNonNull(someOptionalInt, "someOptionalInt");
        Objects.requireNonNull(someOptionalIntType, "someOptionalIntType");
        Objects.requireNonNull(someOptionalLongType, "someOptionalLongType");
        Objects.requireNonNull(someOptionalModifiableInstant, "someOptionalModifiableInstant");
        Objects.requireNonNull(someOptionalPeriod, "someOptionalPeriod");
        Objects.requireNonNull(someOptionalProvidedBooleanType, "someOptionalProvidedBooleanType");
        Objects.requireNonNull(someOptionalProvidedIntType, "someOptionalProvidedIntType");
        Objects.requireNonNull(someOptionalProvidedLongType, "someOptionalProvidedLongType");
        Objects.requireNonNull(someOptionalProvidedStringType, "someOptionalProvidedStringType");
        Objects.requireNonNull(someOptionalStatus, "someOptionalStatus");
        Objects.requireNonNull(someOptionalString, "someOptionalString");
        Objects.requireNonNull(someOptionalStringType, "someOptionalStringType");
        Objects.requireNonNull(someProvidedBooleanType, "someProvidedBooleanType");
        Objects.requireNonNull(someProvidedIntType, "someProvidedIntType");
        Objects.requireNonNull(someProvidedLongType, "someProvidedLongType");
        Objects.requireNonNull(someProvidedStringType, "someProvidedStringType");
        Objects.requireNonNull(someStatus, "someStatus");
        Objects.requireNonNull(someStringType, "someStringType");

        this.someBoolean = someBoolean;
        this.someBooleanType = someBooleanType;
        this.someIntType = someIntType;
        this.someListOfEnums = new ArrayList<>(someListOfEnums);
        this.someListOfInstants = new ArrayList<>(someListOfInstants);
        this.someListOfLocalDates = new ArrayList<>(someListOfLocalDates);
        this.someListOfPeriods = new ArrayList<>(someListOfPeriods);
        this.someListOfStringTypes = new ArrayList<>(someListOfStringTypes);
        this.someListOfStrings = new ArrayList<>(someListOfStrings);
        this.someLongType = someLongType;
        this.someMapOfStringToInteger = someMapOfStringToInteger;
        this.someMapOfStringTypeToStringType = someMapOfStringTypeToStringType;
        this.someModifiableInstant = someModifiableInstant;
        this.someModifiableInt = someModifiableInt;
        this.someModifiableLocalDate = someModifiableLocalDate;
        this.someModifiablePeriod = someModifiablePeriod;
        this.someModifiableString = someModifiableString;
        this.someNonNullableString = someNonNullableString;
        this.someOptionalBoolean = someOptionalBoolean;
        this.someOptionalBooleanType = someOptionalBooleanType;
        this.someOptionalInstant = someOptionalInstant;
        this.someOptionalInt = someOptionalInt;
        this.someOptionalIntType = someOptionalIntType;
        this.someOptionalLongType = someOptionalLongType;
        this.someOptionalModifiableInstant = someOptionalModifiableInstant;
        this.someOptionalPeriod = someOptionalPeriod;
        this.someOptionalProvidedBooleanType = someOptionalProvidedBooleanType;
        this.someOptionalProvidedIntType = someOptionalProvidedIntType;
        this.someOptionalProvidedLongType = someOptionalProvidedLongType;
        this.someOptionalProvidedStringType = someOptionalProvidedStringType;
        this.someOptionalStatus = someOptionalStatus;
        this.someOptionalString = someOptionalString;
        this.someOptionalStringType = someOptionalStringType;
        this.someProvidedBooleanType = someProvidedBooleanType;
        this.someProvidedIntType = someProvidedIntType;
        this.someProvidedLongType = someProvidedLongType;
        this.someProvidedStringType = someProvidedStringType;
        this.someStatus = someStatus;
        this.someStringType = someStringType;

    }


    public boolean getSomeBoolean() {

        return this.someBoolean;

    }


    public SomeBooleanType getSomeBooleanType() {

        return this.someBooleanType;

    }


    public SomeIntType getSomeIntType() {

        return this.someIntType;

    }


    public List<SomeStatus> getSomeListOfEnums() {

        return new ArrayList<>(this.someListOfEnums);

    }


    public List<Instant> getSomeListOfInstants() {

        return new ArrayList<>(this.someListOfInstants);

    }


    public List<LocalDate> getSomeListOfLocalDates() {

        return new ArrayList<>(this.someListOfLocalDates);

    }


    public List<Period> getSomeListOfPeriods() {

        return new ArrayList<>(this.someListOfPeriods);

    }


    public List<SomeStringType> getSomeListOfStringTypes() {

        return new ArrayList<>(this.someListOfStringTypes);

    }


    public List<String> getSomeListOfStrings() {

        return new ArrayList<>(this.someListOfStrings);

    }


    public SomeLongType getSomeLongType() {

        return this.someLongType;

    }


    public Map<String, Integer> getSomeMapOfStringToInteger() {

        return this.someMapOfStringToInteger;

    }


    public Map<SomeStringType, SomeStringType> getSomeMapOfStringTypeToStringType() {

        return this.someMapOfStringTypeToStringType;

    }


    public Instant getSomeModifiableInstant() {

        return this.someModifiableInstant;

    }


    public void setSomeModifiableInstant(final Instant someModifiableInstant) {

        Objects.requireNonNull(someModifiableInstant, "someModifiableInstant");
        this.someModifiableInstant = someModifiableInstant;

    }


    public int getSomeModifiableInt() {

        return this.someModifiableInt;

    }


    public void setSomeModifiableInt(final int someModifiableInt) {

        this.someModifiableInt = someModifiableInt;

    }


    public LocalDate getSomeModifiableLocalDate() {

        return this.someModifiableLocalDate;

    }


    public void setSomeModifiableLocalDate(final LocalDate someModifiableLocalDate) {

        Objects.requireNonNull(someModifiableLocalDate, "someModifiableLocalDate");
        this.someModifiableLocalDate = someModifiableLocalDate;

    }


    public Period getSomeModifiablePeriod() {

        return this.someModifiablePeriod;

    }


    public void setSomeModifiablePeriod(final Period someModifiablePeriod) {

        Objects.requireNonNull(someModifiablePeriod, "someModifiablePeriod");
        this.someModifiablePeriod = someModifiablePeriod;

    }


    public String getSomeModifiableString() {

        return this.someModifiableString;

    }


    public void setSomeModifiableString(final String someModifiableString) {

        BlankStringException.throwIfBlank(someModifiableString, "someModifiableString");
        this.someModifiableString = someModifiableString;

    }


    public String getSomeNonNullableString() {

        return this.someNonNullableString;

    }


    public Optional<Boolean> getSomeOptionalBoolean() {

        return this.someOptionalBoolean;

    }


    public Optional<SomeBooleanType> getSomeOptionalBooleanType() {

        return this.someOptionalBooleanType;

    }


    public Optional<Instant> getSomeOptionalInstant() {

        return this.someOptionalInstant;

    }


    public OptionalInt getSomeOptionalInt() {

        return this.someOptionalInt;

    }


    public void setSomeOptionalInt(final OptionalInt someOptionalInt) {

        Objects.requireNonNull(someOptionalInt, "someOptionalInt");
        this.someOptionalInt = someOptionalInt;

    }


    public Optional<SomeIntType> getSomeOptionalIntType() {

        return this.someOptionalIntType;

    }


    public Optional<SomeLongType> getSomeOptionalLongType() {

        return this.someOptionalLongType;

    }


    public Optional<Instant> getSomeOptionalModifiableInstant() {

        return this.someOptionalModifiableInstant;

    }


    public void setSomeOptionalModifiableInstant(final Optional<Instant> someOptionalModifiableInstant) {

        Objects.requireNonNull(someOptionalModifiableInstant, "someOptionalModifiableInstant");
        this.someOptionalModifiableInstant = someOptionalModifiableInstant;

    }


    public Optional<Period> getSomeOptionalPeriod() {

        return this.someOptionalPeriod;

    }


    public Optional<SomeProvidedBooleanType> getSomeOptionalProvidedBooleanType() {

        return this.someOptionalProvidedBooleanType;

    }


    public Optional<SomeProvidedIntType> getSomeOptionalProvidedIntType() {

        return this.someOptionalProvidedIntType;

    }


    public Optional<SomeProvidedLongType> getSomeOptionalProvidedLongType() {

        return this.someOptionalProvidedLongType;

    }


    public Optional<SomeProvidedStringType> getSomeOptionalProvidedStringType() {

        return this.someOptionalProvidedStringType;

    }


    public Optional<SomeStatus> getSomeOptionalStatus() {

        return this.someOptionalStatus;

    }


    public Optional<String> getSomeOptionalString() {

        return this.someOptionalString;

    }


    public Optional<SomeStringType> getSomeOptionalStringType() {

        return this.someOptionalStringType;

    }


    public SomeProvidedBooleanType getSomeProvidedBooleanType() {

        return this.someProvidedBooleanType;

    }


    public SomeProvidedIntType getSomeProvidedIntType() {

        return this.someProvidedIntType;

    }


    public SomeProvidedLongType getSomeProvidedLongType() {

        return this.someProvidedLongType;

    }


    public SomeProvidedStringType getSomeProvidedStringType() {

        return this.someProvidedStringType;

    }


    public SomeStatus getSomeStatus() {

        return this.someStatus;

    }


    public SomeStringType getSomeStringType() {

        return this.someStringType;

    }


}
