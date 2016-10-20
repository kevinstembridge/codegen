package com.mahanaroad.mongogen.spec.definition.java;


import com.mahanaroad.mongogen.BlankStringException;

import java.util.Objects;

public final class Fqcn {


    public static final Fqcn BOOLEAN = Fqcn.valueOf("java.lang.Boolean");
    public static final Fqcn INSTANT = Fqcn.valueOf("java.time.Instant");
    public static final Fqcn INTEGER = Fqcn.valueOf("java.lang.Integer");
    public static final Fqcn LIST = Fqcn.valueOf("java.util.List");
    public static final Fqcn LOCAL_DATE = Fqcn.valueOf("java.time.LocalDate");
    public static final Fqcn MAP = Fqcn.valueOf("java.util.Map");
    public static final Fqcn OPTIONAL = Fqcn.valueOf("java.util.Optional");
    public static final Fqcn OPTIONAL_DOUBLE = Fqcn.valueOf("java.util.OptionalDouble");
    public static final Fqcn OPTIONAL_INT = Fqcn.valueOf("java.util.OptionalInt");
    public static final Fqcn OPTIONAL_LONG = Fqcn.valueOf("java.util.OptionalLong");
    public static final Fqcn PERIOD = Fqcn.valueOf("java.time.Period");
    public static final Fqcn STRING = Fqcn.valueOf("java.lang.String");

    private final String rawFqcn;
    private final PackageName packageName;
    private final Uqcn uqcn;


    public static Fqcn valueOf(final Class<?> clazz) {

        return new Fqcn(clazz.getCanonicalName());

    }


    public static Fqcn valueOf(final String rawFqcn) {

        return new Fqcn(rawFqcn);

    }


    public static Fqcn valueOf(final PackageName packageName, final Uqcn uqcn) {

        return Fqcn.valueOf(packageName.getValue() + "." + uqcn.getValue());

    }


    private Fqcn(final String rawFqcn) {

        BlankStringException.throwIfBlank(rawFqcn, "rawFqcn");

        this.rawFqcn = rawFqcn;
        this.uqcn = initUqcn(rawFqcn);
        this.packageName = initPackageNameFrom(rawFqcn);

    }


    private Uqcn initUqcn(final String rawFqcn) {

        final int lastIndexOfDot = rawFqcn.lastIndexOf(".");

        if (lastIndexOfDot == -1) {
            throw new RuntimeException("Please don't put classes in the default package. Provided FQCN = [" + rawFqcn + "]");
        }

        final String rawUqcn = rawFqcn.substring(lastIndexOfDot + 1);
        return new Uqcn(rawUqcn);

    }


    private PackageName initPackageNameFrom(final String rawFqcn) {

        final int lastIndexOfDot = rawFqcn.lastIndexOf(".");

        if (lastIndexOfDot == -1) {
            throw new RuntimeException("Please don't put classes in the default package. Provided FQCN = [" + rawFqcn + "]");
        }

        final String rawPackageName = rawFqcn.substring(0, lastIndexOfDot);
        return new PackageName(rawPackageName);

    }


    public boolean isInLangPackage() {

        return this.rawFqcn.startsWith("java.lang");

    }


    /**
     * @return Never null.
     */
    public PackageName getPackageName() {

        return this.packageName;

    }


    /**
     * @return Never null.
     */
    public Uqcn getUqcn() {

        return this.uqcn;

    }


    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final Fqcn fqcn = (Fqcn) other;

        return Objects.equals(this.rawFqcn, fqcn.rawFqcn);

    }


    @Override
    public int hashCode() {

        return Objects.hash(this.rawFqcn);

    }

    @Override
    public String toString() {

        return this.rawFqcn;

    }


    public boolean notInSamePackageAs(final Fqcn otherFqcn) {

        return this.packageName.equals(otherFqcn.getPackageName()) == false;

    }


    public Fqcn withSuffix(final String suffix) {

        return Fqcn.valueOf(this.rawFqcn + suffix);

    }


    public Fqcn withPrefix(final String prefix) {

        return Fqcn.valueOf(this.packageName, this.uqcn.withPrefix(prefix));

    }


}
