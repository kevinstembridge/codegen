package com.mahanaroad.mongogen.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public final class MongogenMigrationId implements Comparable<MongogenMigrationId> {


    private final List<Integer> parts = new ArrayList<>();


    public static MongogenMigrationId of(final Integer... idParts) {

        return new MongogenMigrationId(idParts);

    }


    public MongogenMigrationId(final Integer... idParts) {

        if (idParts == null || idParts.length == 0) {
            throw new IllegalArgumentException("idParts must not be empty");
        }

        Collections.addAll(this.parts, idParts);

    }


    @Override
    public int compareTo(final MongogenMigrationId other) {

        final Iterator<Integer> thisIterator = this.parts.iterator();
        final Iterator<Integer> thatIterator = other.parts.iterator();

        while (thisIterator.hasNext()) {

            final Integer thisIdPart = thisIterator.next();

            if (thatIterator.hasNext() == false) {
                return 1;
            }

            final Integer thatIdPart = thatIterator.next();

            int result = thisIdPart.compareTo(thatIdPart);

            if (result != 0) {
                return result;
            }

        }

        if (thatIterator.hasNext()) {
            return -1;
        }

        return 0;

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final MongogenMigrationId that = (MongogenMigrationId) o;
        return Objects.equals(this.parts, that.parts);

    }


    @Override
    public int hashCode() {

        return Objects.hash(this.parts);

    }


    @Override
    public String toString() {

        return "MongogenMigrationId{" + this.parts + '}';

    }


    public String getValue() {

        return this.parts.stream().map(String::valueOf).collect(Collectors.joining("."));

    }


}
