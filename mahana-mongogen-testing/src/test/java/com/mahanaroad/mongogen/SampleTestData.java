package com.mahanaroad.mongogen;

import java.time.Instant;
import java.time.Period;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.UUID;

public final class SampleTestData {


    private static final Random RANDOM = new Random(System.currentTimeMillis());


    public static boolean anyRandomBoolean() {

        return RANDOM.nextBoolean();

    }


    public static Instant anyRandomInstant() {

        return Instant.now();

    }


    public static int anyRandomInt() {

        return RANDOM.nextInt();

    }


    public static long anyRandomLong() {

        return RANDOM.nextLong();

    }


    public static int anyRandomMonth() {

        return RANDOM.nextInt(12);

    }


    public static int anyRandomDayOfMonth() {

        return RANDOM.nextInt(28);

    }


    public static Optional<String> anyOptionalString() {

        if (anyRandomBoolean()) {
            return Optional.of(UUID.randomUUID().toString());
        } else {
            return Optional.empty();
        }

    }


    public static Optional<Instant> anyOptionalRandomInstant() {

        if (anyRandomBoolean()) {
            return Optional.of(anyRandomInstant());
        } else {
            return Optional.empty();
        }

    }


    public static <T> Optional<T> anyOptionalRandomValueFrom(T[] values) {

        if (anyRandomBoolean()) {
            return Optional.of(anyRandomValueFrom(values));
        } else {
            return Optional.empty();
        }

    }


    public static <T> T anyRandomValueFrom(T[] values) {

        final int idx = RANDOM.nextInt(values.length);
        return values[idx];

    }


    public static OptionalInt anyOptionalRandomInt() {

        if (anyRandomBoolean()) {
            return OptionalInt.of(anyRandomInt());
        } else {
            return OptionalInt.empty();
        }

    }


    public static Optional<Boolean> anyOptionalRandomBoolean() {

        if (anyRandomBoolean()) {
            return Optional.of(anyRandomBoolean());
        } else {
            return Optional.empty();
        }

    }


    public static String anyRandomString() {

        return UUID.randomUUID().toString();

    }


    public static Optional<String> anyOptionalRandomString() {

        if (anyRandomBoolean()) {
            return Optional.of(anyRandomString());
        } else {
            return Optional.empty();
        }

    }


    public static Period anyRandomPeriod() {

        return Period.of(anyRandomInt(), anyRandomMonth(), anyRandomDayOfMonth());

    }


    public static Optional<Period> anyOptionalRandomPeriod() {

        if (anyRandomBoolean()) {
            return Optional.of(anyRandomPeriod());
        } else {
            return Optional.empty();
        }

    }


}
