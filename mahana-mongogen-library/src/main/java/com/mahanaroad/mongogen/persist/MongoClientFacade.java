package com.mahanaroad.mongogen.persist;


import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.types.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


public final class MongoClientFacade {


    private final MongoDatabase defaultDatabase;
    private final GridFS defaultGridFS;


    public MongoClientFacade(final MongoClient mongoClient, final String defaultDatabaseName) {

        Objects.requireNonNull(mongoClient, "mongoClient");
        BlankStringException.throwIfBlank(defaultDatabaseName, "defaultDatabaseName");

        this.defaultDatabase = mongoClient.getDatabase(defaultDatabaseName);
        this.defaultGridFS = new GridFS(mongoClient.getDB(defaultDatabaseName));

    }


    public MongoDatabase getDefaultDatabase() {

        return this.defaultDatabase;

    }


    public GridFS getDefaultGridFS() {

        return this.defaultGridFS;

    }


    /**
     * Creates an entry in GridFS containing the contents of the given input stream.
     *
     * @param filename    Must not be null.
     * @param contentType    Must not be null.
     * @param inputStream Must not be null.
     * @return A tuple containing the new file id and the length of the file in bytes.
     */
    public IdLengthAndMd5 saveToGridFS(final FileName filename, final ContentType contentType, final InputStream inputStream) {

        Objects.requireNonNull(filename, "filename");
        Objects.requireNonNull(contentType, "contentType");
        Objects.requireNonNull(inputStream, "inputStream");

        final MessageDigest messageDigest = getMessageDigest();
        final DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
        final ObjectId fileId = ObjectId.get();
        final GridFSInputFile gridFsInputFile = this.defaultGridFS.createFile(digestInputStream);

        gridFsInputFile.setContentType(contentType.getValue());
        gridFsInputFile.setFilename(filename.getValue());
        gridFsInputFile.setId(fileId);
        gridFsInputFile.save();

        final long length = gridFsInputFile.getLength();
        final String md5String = DatatypeConverter.printHexBinary(messageDigest.digest());

        return new IdLengthAndMd5(fileId, length, new Md5Checksum(md5String));

    }


    private MessageDigest getMessageDigest() {

        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // Should never happen
            throw new RuntimeException(e);
        }

    }


    public long count(final CollectionName collectionName) {

        return getCollection(collectionName).count();

    }



    public long count(final CollectionName collectionName, final Bson query) {

        return getCollection(collectionName).count(query);

    }


    public boolean exists(final CollectionName collectionName, final Bson query) {

        return getCollection(collectionName).count(query) > 0;

    }


    public Document findOne(final CollectionName collectionName, final Bson filter) {

        return getCollection(collectionName).find(filter).first();

    }


    public MongoCollection<Document> getCollection(final CollectionName collectionName) {

        return this.defaultDatabase.getCollection(collectionName.getValue());

    }


    public void insert(final CollectionName collectionName, final Document document) {

        getCollection(collectionName).insertOne(document);

    }


    public DeleteResult deleteMany(final CollectionName collectionName, final Bson query) {

        Objects.requireNonNull(collectionName, "collectionName");
        Objects.requireNonNull(query, "query");

        return getCollection(collectionName).deleteMany(query);

    }


}
