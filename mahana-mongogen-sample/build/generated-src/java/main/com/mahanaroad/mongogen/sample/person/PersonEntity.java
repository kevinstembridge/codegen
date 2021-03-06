// This source was generated by the Mahana Mongogen generator
// Renderer class: class com.mahanaroad.mongogen.generator.entities.EntityRenderer
// Rendered at: 2016-09-30T22:30:59.765Z

package com.mahanaroad.mongogen.sample.person;

import com.mahanaroad.mongogen.sample.party.PartyEntity;
import com.mahanaroad.mongogen.sample.types.FirstName;
import com.mahanaroad.mongogen.sample.types.LastName;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.bson.types.ObjectId;


public class PersonEntity extends PartyEntity {


    private FirstName firstName;

    private LastName lastName;


    public PersonEntity(
            final Instant createdTimestampUtc,
            final FirstName firstName,
            final ObjectId id,
            final Optional<Instant> lastModifiedTimestampUtc,
            final LastName lastName) {

        super(createdTimestampUtc, id, lastModifiedTimestampUtc);

        Objects.requireNonNull(firstName, "firstName");
        Objects.requireNonNull(lastName, "lastName");

        this.firstName = firstName;
        this.lastName = lastName;

    }


    public FirstName getFirstName() {

        return this.firstName;

    }


    public void setFirstName(final FirstName firstName) {

        Objects.requireNonNull(firstName, "firstName");
        this.firstName = firstName;

    }


    public LastName getLastName() {

        return this.lastName;

    }


    public void setLastName(final LastName lastName) {

        Objects.requireNonNull(lastName, "lastName");
        this.lastName = lastName;

    }


}
