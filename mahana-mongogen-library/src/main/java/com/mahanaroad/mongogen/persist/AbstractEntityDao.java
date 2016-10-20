package com.mahanaroad.mongogen.persist;


import com.mahanaroad.mongogen.domain.AbstractEntity;
import com.mahanaroad.mongogen.types.CollectionName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;


public abstract class AbstractEntityDao<ENTITY extends AbstractEntity> {


    private final MongoCollectionFacade mongoCollectionFacade;


    protected AbstractEntityDao(final CollectionName collectionName, final MongoClientFacade mongoClientFacade) {

        this.mongoCollectionFacade = new MongoCollectionFacade(collectionName, mongoClientFacade);

    }


    public CollectionName getCollectionName() {

        return this.mongoCollectionFacade.getCollectionName();

    }


    public MongoCollection<Document> getCollection() {

        return this.mongoCollectionFacade.getCollection();

    }


    public void insert(final ENTITY entity) {

        Objects.requireNonNull(entity, "entity");

        final Document entityDocument = toDocumentFrom(entity);
        entityDocument.remove("lm-ts");
        this.mongoCollectionFacade.insert(entityDocument);

    }


    protected abstract Document toDocumentFrom(final ENTITY entity);


    protected abstract Document toUpsertDocumentFrom(final ENTITY entity);


    protected abstract ENTITY toEntityFrom(final Document document);


    public Optional<ENTITY> findByIdOptional(final ObjectId id) {

        return this.mongoCollectionFacade.findByIdOptional(id, this::toEntityFrom);

    }


    public ENTITY findById(final ObjectId id) {

        return this.mongoCollectionFacade.findById(id, this::toEntityFrom);

    }


    protected ENTITY findOne(final Bson query) {

        return this.mongoCollectionFacade.findOne(query, this::toEntityFrom);

    }


    protected Optional<ENTITY> findOneOptional(final Bson query) {

        return this.mongoCollectionFacade.findOneOptional(query, this::toEntityFrom);

    }


    protected Optional<ENTITY> findOneOptional(final Bson query, final Bson projection, final Bson orderBy) {

        return this.mongoCollectionFacade.findOneOptional(query, projection, orderBy, this::toEntityFrom);

    }


    protected ENTITY findOneAndUpdate(final Bson filter, final Bson update, final FindOneAndUpdateOptions findOneAndUpdateOptions) {

        return this.mongoCollectionFacade.findOneAndUpdate(filter, update, findOneAndUpdateOptions, this::toEntityFrom);

    }


    protected Optional<ENTITY> findOneAndUpdateOptional(final Bson filter, final Bson update, final FindOneAndUpdateOptions findOneAndUpdateOptions) {

        return this.mongoCollectionFacade.findOneAndUpdateOptional(filter, update, findOneAndUpdateOptions, this::toEntityFrom);

    }


    protected List<ENTITY> find(final Bson query) {

        return this.mongoCollectionFacade.find(query, this::toEntityFrom);

    }


    protected Page<ENTITY> find(final Pageable pageable) {

        final Document filter = new Document();
        getTypeDiscriminator().ifPresent(typeDiscriminator -> filter.put("TYP", typeDiscriminator));
        return find(filter, pageable);

    }


    protected abstract Optional<String> getTypeDiscriminator();


    protected Page<ENTITY> find(final Bson query, final Pageable pageable) {

        return this.mongoCollectionFacade.find(query, pageable, this::toEntityFrom, this::convertClassFieldNameToCollectionFieldName);

    }


    protected abstract String convertClassFieldNameToCollectionFieldName(String classFieldName);


    public long count() {

        return this.mongoCollectionFacade.count();

    }


    protected long count(final Bson filter) {

        return this.mongoCollectionFacade.count(filter);

    }


    protected boolean exists(final Bson filter) {

        return this.mongoCollectionFacade.exists(filter);

    }


    protected void updateOneById(final ObjectId objectId, final Bson update) {

        this.mongoCollectionFacade.updateOneById(objectId, update);

    }


    public void deleteById(final ObjectId id) {

        this.mongoCollectionFacade.deleteById(id);

    }


    protected void delete(final Bson filter) {

        this.mongoCollectionFacade.delete(filter);

    }

    protected final <SOURCE, T> List<T> readList(
            final Function<SOURCE, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readList(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T extends Enum<T>> Optional<T> readEnumOptional(
            final Class<T> enumClass,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readEnumOptional(enumClass, collectionFieldName, classFieldName, document);

    }


    protected final <T extends Enum<T>> T readEnum(
            final Class<T> enumClass,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readEnum(enumClass, collectionFieldName, classFieldName, document);

    }


    protected final <T extends Enum<T>> List<T> readEnumList(
            final Class<T> enumClass,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readEnumList(enumClass, collectionFieldName, classFieldName, document);

    }


    protected final Boolean readBoolean(final Object object) {

        return this.mongoCollectionFacade.readBoolean(object);

    }


    protected final Integer readInteger(final Object object) {

        return this.mongoCollectionFacade.readInteger(object);

    }


    protected final <T> T readBoolean(
            final Function<Boolean, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readBoolean(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readBooleanOptional(
            final Function<Boolean, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readBooleanOptional(mapper, collectionFieldName, document);

    }


    protected final boolean readBoolean(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readBoolean(collectionFieldName, classFieldName, document);

    }


    protected final Optional<Boolean> readBooleanOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readBooleanOptional(collectionFieldName, document);

    }


    protected final <T> T readObjectId(
            final Function<ObjectId, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readObjectId(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readObjectIdOptional(
            final Function<ObjectId, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readObjectIdOptional(mapper, collectionFieldName, document);

    }


    protected final ObjectId readObjectId(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readObjectId(collectionFieldName, classFieldName, document);

    }


    protected final Optional<ObjectId> readObjectIdOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readObjectIdOptional(collectionFieldName, document);

    }


    protected final <T> T readInt(
            final Function<Integer, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readInt(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readIntOptional(
            final Function<Integer, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readIntOptional(mapper, collectionFieldName, document);

    }


    protected final int readInt(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readInt(collectionFieldName, classFieldName, document);

    }


    protected final OptionalInt readIntOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readIntOptional(collectionFieldName, document);

    }


    protected final <T> T readLong(
            final Function<Long, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readLong(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readLongOptional(
            final Function<Long, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readLongOptional(mapper, collectionFieldName, document);

    }


    protected final long readLong(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readLong(collectionFieldName, classFieldName, document);

    }


    protected final OptionalLong readLongOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readLongOptional(collectionFieldName, document);

    }


    protected final <T> T readInstant(
            final Function<Instant, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readInstant(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readInstantOptional(
            final Function<Instant, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readInstantOptional(mapper, collectionFieldName, document);

    }


    protected final Instant readInstant(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readInstant(collectionFieldName, classFieldName, document);

    }


    protected final Optional<Instant> readInstantOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readInstantOptional(collectionFieldName, document);

    }


    protected final <T> T readLocalDate(
            final Function<LocalDate, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readLocalDate(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readLocalDateOptional(
            final Function<LocalDate, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readLocalDateOptional(mapper, collectionFieldName, document);

    }


    protected final LocalDate readLocalDate(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readLocalDate(collectionFieldName, classFieldName, document);

    }


    protected final Optional<LocalDate> readLocalDateOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readLocalDateOptional(collectionFieldName, document);

    }


    protected final <T> T readString(
            final Function<String, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readString(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readStringOptional(
            final Function<String, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readStringOptional(mapper, collectionFieldName, document);

    }


    protected final String readString(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readString(collectionFieldName, classFieldName, document);

    }


    protected  final List<String> readStringList(final String collectionFieldName, final String classFieldName, final Document document) {

        return this.mongoCollectionFacade.readStringList(collectionFieldName, classFieldName, document);

    }


    protected  final List<Instant> readInstantList(final String collectionFieldName, final String classFieldName, final Document document) {

        return this.mongoCollectionFacade.readInstantList(collectionFieldName, classFieldName, document);

    }


    protected  final List<LocalDate> readLocalDateList(final String collectionFieldName, final String classFieldName, final Document document) {

        return this.mongoCollectionFacade.readLocalDateList(collectionFieldName, classFieldName, document);

    }


    protected  final List<Period> readPeriodList(final String collectionFieldName, final String classFieldName, final Document document) {

        return this.mongoCollectionFacade.readPeriodList(collectionFieldName, classFieldName, document);

    }


    protected final Optional<String> readStringOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readStringOptional(collectionFieldName, document);

    }


    protected final <T> T readPeriod(
            final Function<Period, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readPeriod(mapper, collectionFieldName, classFieldName, document);

    }


    protected final <T> Optional<T> readPeriodOptional(
            final Function<Period, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readPeriodOptional(mapper, collectionFieldName, document);

    }


    protected final Period readPeriod(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readPeriod(collectionFieldName, classFieldName, document);

    }


    protected final Optional<Period> readPeriodOptional(final String collectionFieldName, final Document document) {

        return this.mongoCollectionFacade.readPeriodOptional(collectionFieldName, document);

    }


    protected final <KEY, TARGET_VALUE> Map<KEY, TARGET_VALUE> readMap(
            final Function<String, KEY> keyMapper,
            final Function<Object, TARGET_VALUE> valueMapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        return this.mongoCollectionFacade.readMap(keyMapper, valueMapper, collectionFieldName, classFieldName, document);

    }


}
