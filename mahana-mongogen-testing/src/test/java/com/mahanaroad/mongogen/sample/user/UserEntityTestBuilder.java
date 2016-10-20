package com.mahanaroad.mongogen.sample.user;

import com.mahanaroad.mongogen.SampleTestData;
import com.mahanaroad.mongogen.sample.types.FirstName;
import com.mahanaroad.mongogen.sample.types.LastName;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Optional;

public final class UserEntityTestBuilder {


    private Instant createdTimestampUtc = SampleTestData.anyRandomInstant();
    private ObjectId id = ObjectId.get();
    private Optional<Instant> lastModifiedTimestampUtc = Optional.empty();
    private FirstName firstName = new FirstName(SampleTestData.anyRandomString());
    private LastName lastName = new LastName(SampleTestData.anyRandomString());
    private String encryptedPassword = SampleTestData.anyRandomString();


    public static UserEntityTestBuilder aUser() {

        return new UserEntityTestBuilder();

    }


    private UserEntityTestBuilder() {

    }


    public UserEntity build() {

        return new UserEntity(
                this.createdTimestampUtc,
                this.encryptedPassword,
                this.firstName,
                this.id,
                this.lastModifiedTimestampUtc,
                this.lastName);

    }


    public UserEntityTestBuilder withCreatedTimestampUtc(final Instant createdTimestampUtc) {

        this.createdTimestampUtc = createdTimestampUtc;
        return this;

    }


    public UserEntityTestBuilder withLastModifiedTimestampUtc(final Instant lastModifiedTimestampUtc) {

        this.lastModifiedTimestampUtc = Optional.ofNullable(lastModifiedTimestampUtc);
        return this;

    }


    public UserEntityTestBuilder withId(final ObjectId id) {

        this.id = id;
        return this;

    }


    public UserEntityTestBuilder withFirstName(final String firstName) {

        this.firstName = new FirstName(firstName);
        return this;

    }


    public UserEntityTestBuilder withLastName(final String lastName) {

        this.lastName = new LastName(lastName);
        return this;

    }


    public UserEntityTestBuilder withEncryptedPassword(final String encryptedPassword) {

        this.encryptedPassword = encryptedPassword;
        return this;

    }


}
