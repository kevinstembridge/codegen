package com.mahanaroad.mongogen.domain;

import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractEntity {


    private final ObjectId id;
    private final Instant createdTimestampUtc;
    private final Optional<Instant> lastModifiedTimestampUtc;


    protected AbstractEntity(
            final Instant createdTimestampUtc,
            final ObjectId id,
            final Optional<Instant> lastModifiedTimestampUtc) {

        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(createdTimestampUtc, "createdTimestampUtc");
        Objects.requireNonNull(lastModifiedTimestampUtc, "lastModifiedTimestampUtc");

        this.id = id;
        this.createdTimestampUtc = createdTimestampUtc;
        this.lastModifiedTimestampUtc = lastModifiedTimestampUtc;

    }


    /**
     * @return Never null.
     */
    public final ObjectId getId() {

        return this.id;

    }


    /**
     * @return Never null.
     */
    public final Instant getCreatedTimestampUtc() {

        return this.createdTimestampUtc;

    }


    /**
     * @return Never null.
     */
    public final Optional<Instant> getLastModifiedTimestampUtc() {

        return this.lastModifiedTimestampUtc;

    }


}
