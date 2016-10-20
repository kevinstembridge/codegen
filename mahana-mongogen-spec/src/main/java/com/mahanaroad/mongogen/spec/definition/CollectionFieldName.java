package com.mahanaroad.mongogen.spec.definition;


import com.mahanaroad.mongogen.types.StringType;

public final class CollectionFieldName extends StringType<CollectionFieldName> {


    public CollectionFieldName(final String value) {

        super(value);

    }


    public String toValidJavaIdentifier() {

        final StringBuilder sb = new StringBuilder();

        char[] chars = getValue().toCharArray();

        for (char ch : chars) {

            if (Character.isJavaIdentifierPart(ch)) {
                sb.append(ch);
            } else {
                sb.append("_");
                sb.append(Character.hashCode(ch));
                sb.append("_");
            }

        }

        return sb.toString();

    }


}
