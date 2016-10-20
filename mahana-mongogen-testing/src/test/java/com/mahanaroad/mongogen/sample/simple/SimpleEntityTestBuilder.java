package com.mahanaroad.mongogen.sample.simple;

import com.mahanaroad.mongogen.SampleTestData;
import com.mahanaroad.mongogen.sample.types.*;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public final class SimpleEntityTestBuilder {


    private Instant createdTimestampUtc = SampleTestData.anyRandomInstant();
    private ObjectId id = ObjectId.get();
    private Optional<Instant> lastModifiedTimestampUtc = Optional.empty();
    private int someModifiableInt = SampleTestData.anyRandomInt();
    private LocalDate someModifiableLocalDate = LocalDate.now();
    private Period someModifiablePeriod = SampleTestData.anyRandomPeriod();
    private String someModifiableString = SampleTestData.anyRandomString();
    private String someNonNullableString = SampleTestData.anyRandomString();
    private Optional<String> someOptionalString = SampleTestData.anyOptionalRandomString();
    private Optional<Instant> someOptionalInstant = SampleTestData.anyOptionalRandomInstant();
    private Optional<SomeStatus> someOptionalStatus = SampleTestData.anyOptionalRandomValueFrom(SomeStatus.values());
    private Instant someModifiableInstant = SampleTestData.anyRandomInstant();
    private boolean someBoolean = SampleTestData.anyRandomBoolean();
    private SomeBooleanType someBooleanType = new SomeBooleanType(SampleTestData.anyRandomBoolean());
    private SomeIntType someIntType = new SomeIntType(SampleTestData.anyRandomInt());
    private SomeLongType someLongType = new SomeLongType(SampleTestData.anyRandomLong());
    private SomeStatus someStatus = SampleTestData.anyRandomValueFrom(SomeStatus.values());
    private Optional<Boolean> someOptionalBoolean = SampleTestData.anyOptionalRandomBoolean();
    private Optional<SomeBooleanType> someOptionalBooleanType = Optional.of(new SomeBooleanType(SampleTestData.anyRandomBoolean()));
    private OptionalInt someOptionalInt = SampleTestData.anyOptionalRandomInt();
    private Optional<SomeIntType> someOptionalIntType = Optional.of(new SomeIntType(SampleTestData.anyRandomInt()));
    private Optional<SomeLongType> someOptionalLongType = Optional.of(new SomeLongType(SampleTestData.anyRandomLong()));
    private Optional<Period> someOptionalPeriod = SampleTestData.anyOptionalRandomPeriod();
    private SomeStringType someStringType = new SomeStringType(SampleTestData.anyRandomString());
    private SomeProvidedStringType someProvidedStringType = new SomeProvidedStringType(SampleTestData.anyRandomString());
    private SomeProvidedBooleanType someProvidedBooleanType = new SomeProvidedBooleanType(SampleTestData.anyRandomBoolean());
    private SomeProvidedIntType someProvidedIntType = new SomeProvidedIntType(SampleTestData.anyRandomInt());
    private SomeProvidedLongType someProvidedLongType = new SomeProvidedLongType(SampleTestData.anyRandomLong());
    private Optional<Instant> someOptionalModifiableInstant = SampleTestData.anyOptionalRandomInstant();
    private Optional<SomeStringType> someOptionalStringType = SampleTestData.anyOptionalRandomString().map(SomeStringType::new);
    private Optional<SomeProvidedStringType> someOptionalProvidedStringType = SampleTestData.anyOptionalRandomString().map(SomeProvidedStringType::new);
    private Optional<SomeProvidedBooleanType> someOptionalProvidedBooleanType = SampleTestData.anyOptionalRandomBoolean().map(SomeProvidedBooleanType::new);
    private Optional<SomeProvidedIntType> someOptionalProvidedIntType = Optional.of(new SomeProvidedIntType(SampleTestData.anyRandomInt()));
    private Optional<SomeProvidedLongType> someOptionalProvidedLongType = Optional.of(new SomeProvidedLongType(SampleTestData.anyRandomLong()));
    private List<String> someListOfStrings = new ArrayList<String>() {{add("string1"); add("string2");}};
    private List<SomeStringType> someListOfStringTypes = someListOfStrings.stream().map(SomeStringType::new).collect(Collectors.toList());
    private List<SomeStatus> someListOfEnums = Arrays.asList(SomeStatus.NOT_OK, SomeStatus.OK);
    private List<Instant> someListOfInstants = Arrays.asList(Instant.now(), Instant.now().plusSeconds(5));
    private List<LocalDate> someListOfLocalDates = Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(5));
    private List<Period> someListOfPeriods = Arrays.asList(Period.of(1, 1, 1), Period.of(2, 0, 0));
    private Map<String, Integer> someMapOfStringToInteger = new HashMap<>();
    private Map<SomeStringType, SomeStringType> someMapOfStringTypeToStringType = new HashMap<>();


    public static SimpleEntityTestBuilder aSimpleEntity() {

        return new SimpleEntityTestBuilder();

    }


    private SimpleEntityTestBuilder() {

    }


    public SimpleEntity build() {

        return new SimpleEntity(
                this.createdTimestampUtc,
                this.id,
                this.lastModifiedTimestampUtc,
                this.someBoolean,
                this.someBooleanType,
                this.someIntType,
                this.someListOfEnums,
                this.someListOfInstants,
                this.someListOfLocalDates,
                this.someListOfPeriods,
                this.someListOfStringTypes,
                this.someListOfStrings,
                this.someLongType,
                this.someMapOfStringToInteger,
                this.someMapOfStringTypeToStringType,
                this.someModifiableInstant,
                this.someModifiableInt,
                this.someModifiableLocalDate,
                this.someModifiablePeriod,
                this.someModifiableString,
                this.someNonNullableString,
                this.someOptionalBoolean,
                this.someOptionalBooleanType,
                this.someOptionalInstant,
                this.someOptionalInt,
                this.someOptionalIntType,
                this.someOptionalLongType,
                this.someOptionalModifiableInstant,
                this.someOptionalPeriod,
                this.someOptionalProvidedBooleanType,
                this.someOptionalProvidedIntType,
                this.someOptionalProvidedLongType,
                this.someOptionalProvidedStringType,
                this.someOptionalStatus,
                this.someOptionalString,
                this.someOptionalStringType,
                this.someProvidedBooleanType,
                this.someProvidedIntType,
                this.someProvidedLongType,
                this.someProvidedStringType,
                this.someStatus,
                this.someStringType);

    }


    public SimpleEntityTestBuilder withId(final ObjectId id) {

        this.id = id;
        return this;

    }


    public SimpleEntityTestBuilder withLaterCreatedTimestampUtc() {

        this.createdTimestampUtc = this.createdTimestampUtc.plusSeconds(1);
        return this;

    }


    public SimpleEntityTestBuilder withSomeNonNullableString(final String someNonNullableString) {

        this.someNonNullableString = someNonNullableString;
        return this;

    }

    public SimpleEntityTestBuilder withSomeOptionalString(final Optional<String> someOptionalString) {

        this.someOptionalString = someOptionalString;
        return this;

    }


    public SimpleEntityTestBuilder withSomeBoolean(final boolean someBoolean) {

        this.someBoolean = someBoolean;
        return this;

    }


    public SimpleEntityTestBuilder withSomeModifiableString(final String someModifiableString) {

        this.someModifiableString = someModifiableString;
        return this;

    }


    public SimpleEntityTestBuilder withSomeModifiableInstant(final Instant someModifiableInstant) {

        this.someModifiableInstant = someModifiableInstant;
        return this;

    }


    public SimpleEntityTestBuilder withSomeModifiableInt(final int someModifiableInt) {

        this.someModifiableInt = someModifiableInt;
        return this;

    }


    public SimpleEntityTestBuilder withSomeStatus(final SomeStatus someStatus) {

        this.someStatus = someStatus;
        return this;

    }


    public SimpleEntityTestBuilder withSomeOptionalStatus(final SomeStatus someOptionalStatus) {

        this.someOptionalStatus = Optional.ofNullable(someOptionalStatus);
        return this;

    }


    public SimpleEntityTestBuilder withSomeOptionalInstant(final Instant someOptionalInstant) {

        this.someOptionalInstant = Optional.ofNullable(someOptionalInstant);
        return this;

    }


    public SimpleEntityTestBuilder withSomeOptionalStringType(final SomeStringType someStringType) {

        this.someOptionalStringType = Optional.ofNullable(someStringType);
        return this;

    }


    public SimpleEntityTestBuilder withSomeStringType(final SomeStringType someStringType) {

        this.someStringType = someStringType;
        return this;

    }


    public SimpleEntityTestBuilder withLastModifiedTimestampUtc(final Instant lastModifiedTimestampUtc) {

        this.lastModifiedTimestampUtc = Optional.ofNullable(lastModifiedTimestampUtc);
        return this;

    }


    public SimpleEntityTestBuilder withSomeListOfStrings(final List<String> someListOfStrings) {

        this.someListOfStrings = someListOfStrings;
        return this;

    }


    public SimpleEntityTestBuilder withSomeListOfStringTypes(final List<SomeStringType> someListOfStringTypes) {

        this.someListOfStringTypes = someListOfStringTypes;
        return this;

    }


    public SimpleEntityTestBuilder withSomeOptionalModifiableInstant(final Optional<Instant> someOptionalModifiableInstant) {

        this.someOptionalModifiableInstant = someOptionalModifiableInstant;
        return this;

    }


}
