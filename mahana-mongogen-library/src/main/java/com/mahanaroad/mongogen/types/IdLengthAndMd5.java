package com.mahanaroad.mongogen.types;

import org.bson.types.ObjectId;

import java.util.Objects;


public final class IdLengthAndMd5 {


    private final ObjectId id;
    private final long length;
    private final Md5Checksum md5;


    public IdLengthAndMd5(final ObjectId id, final long length, final Md5Checksum md5) {

        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(md5, "md5");

        this.id = id;
        this.length = length;
        this.md5 = md5;

    }


    public ObjectId getId() {

        return id;

    }


    public long getLength() {

        return length;

    }


    public Md5Checksum getMd5() {

        return md5;

    }


}
