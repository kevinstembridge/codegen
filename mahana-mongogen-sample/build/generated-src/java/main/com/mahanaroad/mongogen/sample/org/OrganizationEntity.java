// This source was generated by the Mahana Mongogen generator
// Renderer class: class com.mahanaroad.mongogen.generator.entities.EntityRenderer
// Rendered at: 2016-09-30T22:30:59.761Z

package com.mahanaroad.mongogen.sample.org;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.sample.party.PartyEntity;
import java.time.Instant;
import java.util.Optional;
import org.bson.types.ObjectId;


public class OrganizationEntity extends PartyEntity {


    private String name;


    public OrganizationEntity(
            final Instant createdTimestampUtc,
            final ObjectId id,
            final Optional<Instant> lastModifiedTimestampUtc,
            final String name) {

        super(createdTimestampUtc, id, lastModifiedTimestampUtc);

        BlankStringException.throwIfBlank(name, "name");

        this.name = name;

    }


    public String getName() {

        return this.name;

    }


    public void setName(final String name) {

        BlankStringException.throwIfBlank(name, "name");
        this.name = name;

    }


}
