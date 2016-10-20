package com.mahanaroad.mongogen.migration;

import java.time.Duration;
import java.time.Instant;

public class MongogenMigrationLockNotAvailableException extends Exception {

    private final Instant lockCreatedTimestampUtc;
    private final String hostname;
    private final String processName;


    public MongogenMigrationLockNotAvailableException(Instant lockCreatedTimestampUtc, String hostname, String processName) {

        super(
                "Unable to obtain Mongogen migration lock. Existing lock held by process ["
                        + processName
                        + "] on host ["
                        + hostname
                        + "] since "
                        + lockCreatedTimestampUtc
                        + " (" + Duration.between(lockCreatedTimestampUtc, Instant.now()) + ")");

        this.lockCreatedTimestampUtc = lockCreatedTimestampUtc;
        this.hostname = hostname;
        this.processName = processName;

    }


    public Instant getLockCreatedTimestampUtc() {

        return this.lockCreatedTimestampUtc;

    }


    public String getHostname() {

        return this.hostname;

    }


    public String getProcessName() {

        return this.processName;

    }


}
