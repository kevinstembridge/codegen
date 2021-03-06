// This source was generated by the Mahana Mongogen generator
// Renderer class: class com.mahanaroad.mongogen.generator.entities.DaoRenderer
// Rendered at: 2016-09-30T22:30:59.763Z

package com.mahanaroad.mongogen.sample.org;

import com.mahanaroad.mongogen.persist.AbstractEntityDao;
import com.mahanaroad.mongogen.persist.MongoClientFacade;
import com.mahanaroad.mongogen.types.CollectionName;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public class OrganizationDao extends AbstractEntityDao<OrganizationEntity> {

    private final OrganizationEntityFieldConverter fieldConverter = new OrganizationEntityFieldConverter() {

        @Override
        public Object convert(final String collectionFieldName, final Object inputValue) {

            switch(collectionFieldName) {

                case "c-ts":
                    return Date.from((Instant) inputValue);
                case "_id":
                    return inputValue;
                case "lm-ts":
                    return Date.from((Instant) inputValue);
                case "n":
                    return inputValue;
                 default:
                     throw new RuntimeException("Unknown collectionFieldName [" + collectionFieldName + "]");
            }

        }

    };


    private final MongoClientFacade mongoClientFacade;


    @Autowired
    public OrganizationDao(final MongoClientFacade mongoClientFacade) {

        super(new CollectionName("party"), mongoClientFacade);

        Objects.requireNonNull(mongoClientFacade, "mongoClientFacade");

        this.mongoClientFacade = mongoClientFacade;

    }


    @Override
    protected Document toDocumentFrom(final OrganizationEntity entity) {

        final Document document = new Document();

        document.put("TYP", "ORG");
        document.put("c-ts", Date.from(entity.getCreatedTimestampUtc()));
        document.put("_id", entity.getId());
        entity.getLastModifiedTimestampUtc().ifPresent(lastModifiedTimestampUtc -> document.put("lm-ts", Date.from(lastModifiedTimestampUtc)));
        document.put("n", entity.getName());

        return document;

    }


    @Override
    protected Document toUpsertDocumentFrom(final OrganizationEntity entity) {

        final Document modifiableFieldsDocument = new Document();
        final Document unmodifiableFieldsDocument = new Document();

        unmodifiableFieldsDocument.put("TYP", "ORG");
        modifiableFieldsDocument.put("n", entity.getName());
        unmodifiableFieldsDocument.put("c-ts", Date.from(entity.getCreatedTimestampUtc()));
        unmodifiableFieldsDocument.put("_id", entity.getId());
        entity.getLastModifiedTimestampUtc().ifPresent(lastModifiedTimestampUtc -> unmodifiableFieldsDocument.put("lm-ts", Date.from(lastModifiedTimestampUtc)));

        return new Document()
                .append("$setOnInsert", unmodifiableFieldsDocument)
                .append("$set", modifiableFieldsDocument);

    }


    @Override
    protected OrganizationEntity toEntityFrom(final Document document) {

        final Instant createdTimestampUtc = readInstant("c-ts", "createdTimestampUtc", document);
        final ObjectId id = readObjectId("_id", "id", document);
        final Optional<Instant> lastModifiedTimestampUtc = readInstantOptional("lm-ts", document);
        final String name = readString("n", "name", document);

        return new OrganizationEntity(
                createdTimestampUtc,
                id,
                lastModifiedTimestampUtc,
                name);

    }


    public long count(final OrganizationEntityFilter filter) {

        final Bson bsonFilter = filter.toBson(this.fieldConverter);
        return super.count(bsonFilter);

    }


    public List<OrganizationEntity> findAllBy(final OrganizationEntityFilter filter) {

        final Bson bsonFilter = filter.toBson(this.fieldConverter);
        return super.find(bsonFilter);

    }


    public Page<OrganizationEntity> findAll(final Pageable pageable) {

        return super.find(pageable);

    }


    public Page<OrganizationEntity> findAllBy(final OrganizationEntityFilter filter, final Pageable pageable) {

        final Bson bsonFilter = filter.toBson(this.fieldConverter);
        return super.find(bsonFilter, pageable);

    }


    public void setFields(final OrganizationEntityUpdater updater) {

        final Bson bson = updater.toBson(this.fieldConverter);
        super.updateOneById(updater.getId(), bson);

    }


    protected final String convertClassFieldNameToCollectionFieldName(final String classFieldName) {

        switch(classFieldName) {
            case "createdTimestampUtc":
                return "c-ts";
            case "id":
                return "_id";
            case "lastModifiedTimestampUtc":
                return "lm-ts";
            case "name":
                return "n";
            default:
                throw new IllegalArgumentException("Unknown classFieldName [" + classFieldName + "]");
        }

    }


    protected final Optional<String> getTypeDiscriminator() {

        return Optional.of("ORG");

    }


}
