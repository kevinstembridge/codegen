package com.mahanaroad.mongogen.spec.definition.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class AnnotationDef {


    private final Fqcn fqcn;
    private final Optional<String> value;
    private final Map<String, String> attributes = new HashMap<>();


    public AnnotationDef(final Fqcn fqcn) {

        this(fqcn, Optional.empty(), Collections.emptyMap());

    }


    public AnnotationDef(final Fqcn fqcn, final String value) {

        this(fqcn, Optional.of(value), Collections.emptyMap());

    }


    public AnnotationDef(final Fqcn fqcn, final Optional<String> value, final Map<String, String> attributes) {

        Objects.requireNonNull(fqcn, "fqcn");
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(attributes, "attributes");
        this.fqcn = fqcn;
        this.value = value;
        this.attributes.putAll(attributes);

    }


    public String getUnqualifiedToString() {

        return this.fqcn.getUqcn().toString();

    }


    public Fqcn getFqcn() {

        return this.fqcn;

    }


    @Override
    public String toString() {

        return "@" + this.fqcn.getUqcn().toString() + formattedAttributes();

    }


    private String formattedAttributes() {

        if (parenthesesAreRequired() == false) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("(");

        this.value.ifPresent(v -> sb.append("\"").append(v).append("\""));

        sb.append(this.attributes.entrySet().stream().map(entry -> entry.getKey() + " = " + entry.getValue()).collect(Collectors.joining(", ")));

        sb.append(")");

        return sb.toString();

    }


    private boolean parenthesesAreRequired() {

        return this.value.isPresent() || this.attributes.isEmpty() == false;

    }


    public Optional<String> getValue() {
        return this.value;
    }


}
