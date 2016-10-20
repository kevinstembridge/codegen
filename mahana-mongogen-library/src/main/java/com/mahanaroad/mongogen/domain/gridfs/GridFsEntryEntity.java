package com.mahanaroad.mongogen.domain.gridfs;

import com.mahanaroad.mongogen.domain.AbstractEntity;
import com.mahanaroad.mongogen.types.*;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;


public abstract class GridFsEntryEntity extends AbstractEntity {

    public static final CollectionName COLLECTION_NAME = new CollectionName("gridFsEntry");

//    public static final CollectionFieldName FIELD_NAME_GRID_FS_ID = new CollectionFieldName("gfs-id");
//    public static final CollectionFieldName FIELD_NAME_FILE_NAME = new CollectionFieldName("fn");
//    public static final CollectionFieldName FIELD_NAME_LENGTH_IN_BYTES = new CollectionFieldName("l");
//    public static final CollectionFieldName FIELD_NAME_MD5_CHECKSUM = new CollectionFieldName("md5");
//    public static final CollectionFieldName FIELD_NAME_CONTENT_TYPE = new CollectionFieldName("ct");
//    public static final CollectionFieldName FIELD_NAME_DELETED = new CollectionFieldName("del");

    private final ObjectId gridFsId;
    private final FileName fileName;
    private final long lengthInBytes;
    private final Md5Checksum md5Checksum;
    private final ContentType contentType;
    private final TypeDiscriminator typeDiscriminator;
    private final boolean deleted;


    protected GridFsEntryEntity(
            final ContentType contentType,
            final Instant createdTimestampUtc,
            final boolean deleted,
            final FileName fileName,
            final ObjectId gridFsId,
            final ObjectId id,
            final Optional<Instant> lastModifiedTimestampUtc,
            final long lengthInBytes,
            final Md5Checksum md5Checksum,
            final TypeDiscriminator typeDiscriminator) {

        super(createdTimestampUtc, id, lastModifiedTimestampUtc);

        Objects.requireNonNull(gridFsId, "gridFsId");
        Objects.requireNonNull(gridFsId, "gridFsId");
        Objects.requireNonNull(fileName, "fileName");
        Objects.requireNonNull(md5Checksum, "md5Checksum");
        Objects.requireNonNull(contentType, "contentType");
        Objects.requireNonNull(typeDiscriminator, "typeDiscriminator");

        this.gridFsId = gridFsId;
        this.fileName = fileName;
        this.lengthInBytes = lengthInBytes;
        this.md5Checksum = md5Checksum;
        this.contentType = contentType;
        this.typeDiscriminator = typeDiscriminator;
        this.deleted = deleted;

    }


    public final ObjectId getGridFsId() {

        return this.gridFsId;

    }


    public final FileName getFileName() {

        return this.fileName;

    }


    public final long getLengthInBytes() {

        return this.lengthInBytes;

    }


    public final Md5Checksum getMd5Checksum() {

        return this.md5Checksum;

    }


    public final ContentType getContentType() {

        return this.contentType;

    }


    public final boolean isDeleted() {

        return this.deleted;

    }


}
