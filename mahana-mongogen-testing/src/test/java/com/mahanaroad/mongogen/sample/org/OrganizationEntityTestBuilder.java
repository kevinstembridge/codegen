package com.mahanaroad.mongogen.sample.org;

import com.mahanaroad.mongogen.SampleTestData;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Optional;

public final class OrganizationEntityTestBuilder {


    private Instant createdTimestampUtc = SampleTestData.anyRandomInstant();
    private ObjectId id = ObjectId.get();
    private Optional<Instant> lastModifiedTimestampUtc = Optional.empty();
    private String name = SampleTestData.anyRandomString();


    public static OrganizationEntityTestBuilder anOrganization() {

        return new OrganizationEntityTestBuilder();

    }


    private OrganizationEntityTestBuilder() {

    }


    public OrganizationEntity build() {

        return new OrganizationEntity(
                this.createdTimestampUtc,
                this.id,
                this.lastModifiedTimestampUtc,
                this.name);

    }


    public OrganizationEntityTestBuilder withCreatedTimestampUtc(final Instant createdTimestampUtc) {

        this.createdTimestampUtc = createdTimestampUtc;
        return this;

    }


    public OrganizationEntityTestBuilder withLastModifiedTimestampUtc(final Instant lastModifiedTimestampUtc) {

        this.lastModifiedTimestampUtc = Optional.ofNullable(lastModifiedTimestampUtc);
        return this;

    }


    public OrganizationEntityTestBuilder withId(final ObjectId id) {

        this.id = id;
        return this;

    }


    public OrganizationEntityTestBuilder withName(final String name) {

        this.name = name;
        return this;

    }


}
