package com.mahanaroad.mongogen.sample.person;

import com.mahanaroad.mongogen.SampleTestData;
import com.mahanaroad.mongogen.sample.types.FirstName;
import com.mahanaroad.mongogen.sample.types.LastName;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Optional;

public final class PersonEntityTestBuilder {


    private Instant createdTimestampUtc = SampleTestData.anyRandomInstant();
    private ObjectId id = ObjectId.get();
    private Optional<Instant> lastModifiedTimestampUtc = Optional.empty();
    private FirstName firstName = new FirstName(SampleTestData.anyRandomString());
    private LastName lastName = new LastName(SampleTestData.anyRandomString());


    public static PersonEntityTestBuilder aPerson() {

        return new PersonEntityTestBuilder();

    }


    private PersonEntityTestBuilder() {

    }


    public PersonEntity build() {

        return new PersonEntity(
                this.createdTimestampUtc,
                this.firstName,
                this.id,
                this.lastModifiedTimestampUtc,
                this.lastName);

    }


    public PersonEntityTestBuilder withCreatedTimestampUtc(final Instant createdTimestampUtc) {

        this.createdTimestampUtc = createdTimestampUtc;
        return this;

    }


    public PersonEntityTestBuilder withLastModifiedTimestampUtc(final Instant lastModifiedTimestampUtc) {

        this.lastModifiedTimestampUtc = Optional.ofNullable(lastModifiedTimestampUtc);
        return this;

    }


    public PersonEntityTestBuilder withId(final ObjectId id) {

        this.id = id;
        return this;

    }


    public PersonEntityTestBuilder withFirstName(final String firstName) {

        this.firstName = new FirstName(firstName);
        return this;

    }


    public PersonEntityTestBuilder withLastName(final String lastName) {

        this.lastName = new LastName(lastName);
        return this;

    }


}
