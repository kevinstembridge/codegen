package com.mahanaroad.mongogen.persist;


import com.mahanaroad.mongogen.InvalidFieldException;
import com.mahanaroad.mongogen.MissingFieldException;
import com.mahanaroad.mongogen.domain.DocumentNotFoundException;
import com.mahanaroad.mongogen.types.CollectionName;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;


public final class MongoCollectionFacade {


    private final CollectionName collectionName;
    private final MongoClientFacade mongoClientFacade;


    public MongoCollectionFacade(final CollectionName collectionName, final MongoClientFacade mongoClientFacade) {

        Objects.requireNonNull(collectionName, "collectionName");
        Objects.requireNonNull(mongoClientFacade, "mongoClientFacade");

        this.collectionName = collectionName;
        this.mongoClientFacade = mongoClientFacade;

    }


    public void insert(final Document document) {

        Objects.requireNonNull(document, "document");
        this.mongoClientFacade.insert(this.collectionName, document);

    }


    public <T> Optional<T> findByIdOptional(final ObjectId id, final Function<Document, T> documentMapper) {

        Objects.requireNonNull(id, "id");

        final Bson query = new Document("_id", id);
        return findOneOptional(query, documentMapper);

    }


    public <T> T findById(final ObjectId id, final Function<Document, T> documentMapper) {

        Objects.requireNonNull(id, "id");

        final Bson query = new Document("_id", id);
        return findOneOptional(query, documentMapper).orElseThrow(() -> new DocumentNotFoundException(this.collectionName, query));

    }


    public Document findOne(final Bson query) {

        return findOneOptional(query, document -> document).orElseThrow(() -> new DocumentNotFoundException(this.collectionName, query));

    }


    public <T> T findOne(final Bson query, final Function<Document, T> documentMapper) {

        return findOneOptional(query, documentMapper).orElseThrow(() -> new DocumentNotFoundException(this.collectionName, query));

    }


    public Optional<Document> findOneOptional(final Bson query) {

        return findOneOptional(query, null, null, document -> document);

    }


    public <T> Optional<T> findOneOptional(final Bson query, final Function<Document, T> documentMapper) {

        return findOneOptional(query, null, null, documentMapper);

    }


    public <T> Optional<T> findOneOptional(final Bson query, final Bson projection, final Bson orderBy, final Function<Document, T> documentMapper) {

        final Document foundDbObject = getCollection()
                .find(query)
                .projection(projection)
                .sort(orderBy)
                .first();

        return Optional.ofNullable(foundDbObject).map(documentMapper );

    }


    public <T> T findOneAndUpdate(final Bson filter, final Bson update, final FindOneAndUpdateOptions findOneAndUpdateOptions, final Function<Document, T> documentMapper) {

        return findOneAndUpdateOptional(filter, update, findOneAndUpdateOptions, documentMapper).orElseThrow(() -> new DocumentNotFoundException(this.collectionName, filter));

    }


    public <T> Optional<T> findOneAndUpdateOptional(final Bson filter, final Bson update, final FindOneAndUpdateOptions findOneAndUpdateOptions, final Function<Document, T> documentMapper) {

        final Document returnDocument = getCollection().findOneAndUpdate(filter, update, findOneAndUpdateOptions);
        return Optional.ofNullable(returnDocument).map(documentMapper);

    }


    public <T> List<T> find(final Bson query, final Function<Document, T> documentMapper) {

        final Iterable<Document> iterable = () -> getCollection().find(query).iterator();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(documentMapper)
                .collect(toList());

    }


    public <T> Page<T> find(final Bson query, final Pageable pageable, final Function<Document, T> documentMapper, final Function<String, String> propertyNameToCollectionFieldNameMapper) {

        final int offset = pageable.getOffset();
        final int pageSize = pageable.getPageSize();
        final Document sortDocument = createSortDocumentFrom(pageable, propertyNameToCollectionFieldNameMapper);

        final FindIterable<Document> cursor = getCollection()
                .find(query)
                .sort(sortDocument)
                .skip(offset)
                .limit(pageSize);

        final Iterable<Document> iterable = cursor::iterator;

        final List<T> documentList = StreamSupport
                .stream(iterable.spliterator(), false)
                .map(documentMapper)
                .collect(toList());

        final long total = count(query);

        return new PageImpl<>(documentList, pageable, total);

    }


    private Document createSortDocumentFrom(final Pageable pageable, final Function<String, String> propertyNameToCollectionFieldNameMapper) {

        final Document sortDocument = new Document();
        final Sort sort = pageable.getSort();

        if (sort != null) {

            sort.forEach(order -> {
                final String collectionFieldName = getCollectionFieldNameFrom(order, propertyNameToCollectionFieldNameMapper);
                final int directionInt = getDirectionFrom(order);
                sortDocument.put(collectionFieldName, directionInt);
            });

        }

        return sortDocument;

    }


    private int getDirectionFrom(final Sort.Order order) {

        final Sort.Direction direction = order.getDirection();
        return directionFrom(direction);

    }


    private String getCollectionFieldNameFrom(final Sort.Order order, final Function<String, String> propertyNameToCollectionFieldNameMapper) {

        final String classFieldName = order.getProperty();
        return propertyNameToCollectionFieldNameMapper.apply(classFieldName);

    }


    private int directionFrom(final Sort.Direction direction) {

        switch (direction) {
            case ASC:
                return 1;
            case DESC:
                return -1;
            default:
                throw new RuntimeException("Unknown direction [" + direction + "]");
        }

    }


    public long count() {

        return getCollection().count();

    }


    public long count(final Bson filter) {

        return getCollection().count(filter);

    }


    public boolean exists(final Bson filter) {

        return getCollection().count(filter) != 0;

    }


    public MongoCollection<Document> getCollection() {

        return this.mongoClientFacade.getCollection(this.collectionName);

    }


    public CollectionName getCollectionName() {

        return this.collectionName;

    }


    public void updateOneById(final ObjectId objectId, final Bson update) {

        final Document filter = new Document("_id", objectId);
        getCollection().updateOne(filter, update);

    }


    public void deleteById(final ObjectId id) {

        delete(new Document("_id", id));

    }


    public void delete(final Bson filter) {

        getCollection().deleteOne(filter);

    }


    public final <SOURCE, T> List<T> readList(
            final Function<SOURCE, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        @SuppressWarnings("unchecked")
        final List<SOURCE> value = (List<SOURCE>) document.get(collectionFieldName);

        if (value == null) {

            return Collections.emptyList();

        } else {

            try {
                return value.stream().map(mapper).collect(Collectors.toList());
            } catch (RuntimeException e) {
                final ObjectId id = document.getObjectId("_id");
                throw new InvalidFieldException(id, this.collectionName, collectionFieldName, classFieldName, e);
            }

        }

    }


    public final <T extends Enum<T>> Optional<T> readEnumOptional(
            final Class<T> enumClass,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final String value = document.getString(collectionFieldName);

        return Optional.ofNullable(value).map(v -> {

            try {
                return Enum.valueOf(enumClass, v);
            } catch (IllegalArgumentException e) {
                throw new InvalidFieldException(document.getObjectId("_id"), collectionName, collectionFieldName, classFieldName, e);
            }

        });

    }


    public final <T extends Enum<T>> T readEnum(
            final Class<T> enumClass,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final String value = document.getString(collectionFieldName);
        MissingFieldException.throwIfBlank(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);

        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new InvalidFieldException(document.getObjectId("_id"), this.collectionName, collectionFieldName, classFieldName, e);
        }

    }


    public final <T extends Enum<T>> List<T> readEnumList(
            final Class<T> enumClass,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        @SuppressWarnings("unchecked")
        final List<String> value = (List<String>) document.get(collectionFieldName);

        if (value == null) {

            return Collections.emptyList();

        } else {

            try {
                return value.stream().map(rawValue -> Enum.valueOf(enumClass, rawValue)).collect(Collectors.toList());
            } catch (RuntimeException e) {
                final ObjectId id = document.getObjectId("_id");
                throw new InvalidFieldException(id, this.collectionName, collectionFieldName, classFieldName, e);
            }

        }

    }


    public final Boolean readBoolean(final Object object) {

        return (Boolean) object;

    }


    public final Integer readInteger(final Object object) {

        return (Integer) object;

    }


    public final <T> T readBoolean(
            final Function<Boolean, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final boolean value = readBoolean(collectionFieldName, classFieldName, document);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readBooleanOptional(
            final Function<Boolean, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return readBooleanOptional(collectionFieldName, document).map(mapper);

    }


    public final boolean readBoolean(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Boolean value = document.getBoolean(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return value;

    }


    public final Optional<Boolean> readBooleanOptional(final String collectionFieldName, final Document document) {

        final Boolean value = document.getBoolean(collectionFieldName);
        return Optional.ofNullable(value);

    }


    public final <T> T readObjectId(
            final Function<ObjectId, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final ObjectId value = readObjectId(collectionFieldName, classFieldName, document);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readObjectIdOptional(
            final Function<ObjectId, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return readObjectIdOptional(collectionFieldName, document).map(mapper);

    }


    public final ObjectId readObjectId(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final ObjectId value = document.getObjectId(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return value;

    }


    public final Optional<ObjectId> readObjectIdOptional(final String collectionFieldName, final Document document) {

        final ObjectId value = document.getObjectId(collectionFieldName);
        return Optional.ofNullable(value);

    }


    public final <T> T readInt(
            final Function<Integer, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Integer value = document.getInteger(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readIntOptional(
            final Function<Integer, T> mapper,
            final String collectionFieldName,
            final Document document) {

        final OptionalInt t = readIntOptional(collectionFieldName, document);

        if (t.isPresent()) {
            return Optional.ofNullable(mapper.apply(t.getAsInt()));
        } else {
            return Optional.empty();
        }


    }


    public final int readInt(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Integer value = document.getInteger(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return value;

    }


    public final OptionalInt readIntOptional(final String collectionFieldName, final Document document) {

        final Integer value = document.getInteger(collectionFieldName);

        if (value == null) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(value);
        }

    }


    public final <T> T readLong(
            final Function<Long, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Long value = document.getLong(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readLongOptional(
            final Function<Long, T> mapper,
            final String collectionFieldName,
            final Document document) {

        final OptionalLong opt = readLongOptional(collectionFieldName, document);

        if (opt.isPresent()) {
            return Optional.ofNullable(mapper.apply(opt.getAsLong()));
        } else {
            return Optional.empty();
        }


    }


    public final long readLong(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Long value = document.getLong(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return value;

    }


    public final OptionalLong readLongOptional(final String collectionFieldName, final Document document) {

        final Long value = document.getLong(collectionFieldName);

        if (value == null) {
            return OptionalLong.empty();
        } else {
            return OptionalLong.of(value);
        }

    }


    public final <T> T readInstant(
            final Function<Instant, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Instant value = readInstant(collectionFieldName, classFieldName, document);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readInstantOptional(
            final Function<Instant, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return readInstantOptional(collectionFieldName, document).map(mapper);

    }


    public final Instant readInstant(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Date value = document.getDate(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return value.toInstant();

    }


    public final Optional<Instant> readInstantOptional(final String collectionFieldName, final Document document) {

        final Date value = document.getDate(collectionFieldName);
        return Optional.ofNullable(value).map(Date::toInstant);

    }


    public final <T> T readLocalDate(
            final Function<LocalDate, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final LocalDate value = readLocalDate(collectionFieldName, classFieldName, document);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readLocalDateOptional(
            final Function<LocalDate, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return readLocalDateOptional(collectionFieldName, document).map(mapper);

    }


    public final LocalDate readLocalDate(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Date value = document.getDate(collectionFieldName);
        MissingFieldException.throwIfNull(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return value.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();

    }


    public final Optional<LocalDate> readLocalDateOptional(final String collectionFieldName, final Document document) {

        final Date value = document.getDate(collectionFieldName);
        return Optional.ofNullable(value).map(date -> date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate());

    }


    public final <T> T readString(
            final Function<String, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final String value = readString(collectionFieldName, classFieldName, document);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readStringOptional(
            final Function<String, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return readStringOptional(collectionFieldName, document).map(mapper);

    }


    public final String readString(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final String value = document.getString(collectionFieldName);
        MissingFieldException.throwIfBlank(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return value;

    }


    public  final List<String> readStringList(final String collectionFieldName, final String classFieldName, final Document document) {

        @SuppressWarnings("unchecked")
        final List<String> value = (List<String>) document.get(collectionFieldName);

        if (value == null) {
            return Collections.emptyList();
        } else {
            return value;
        }

    }


    public  final List<Instant> readInstantList(final String collectionFieldName, final String classFieldName, final Document document) {

        @SuppressWarnings("unchecked")
        final List<Date> value = (List<Date>) document.get(collectionFieldName);

        if (value == null) {
            return Collections.emptyList();
        } else {

            try {
                return value.stream().map(Date::toInstant).collect(Collectors.toList());
            } catch (ClassCastException e) {
                throw new InvalidFieldException(document.getObjectId("_id"), collectionName, collectionFieldName, classFieldName, e);
            }

        }

    }


    public  final List<LocalDate> readLocalDateList(final String collectionFieldName, final String classFieldName, final Document document) {

        @SuppressWarnings("unchecked")
        final List<Date> value = (List<Date>) document.get(collectionFieldName);

        if (value == null) {
            return Collections.emptyList();
        } else {

            try {
                return value.stream().map(d -> d.toInstant().atZone(ZoneId.of("UTC")).toLocalDate()).collect(Collectors.toList());
            } catch (ClassCastException e) {
                throw new InvalidFieldException(document.getObjectId("_id"), collectionName, collectionFieldName, classFieldName, e);
            }

        }

    }


    public  final List<Period> readPeriodList(final String collectionFieldName, final String classFieldName, final Document document) {

        @SuppressWarnings("unchecked")
        final List<String> value = (List<String>) document.get(collectionFieldName);

        if (value == null) {
            return Collections.emptyList();
        } else {

            try {
                return value.stream().map(Period::parse).collect(Collectors.toList());
            } catch (ClassCastException e) {
                throw new InvalidFieldException(document.getObjectId("_id"), collectionName, collectionFieldName, classFieldName, e);
            }

        }

    }


    public final Optional<String> readStringOptional(final String collectionFieldName, final Document document) {

        final String value = document.getString(collectionFieldName);
        return Optional.ofNullable(value);

    }


    public final <T> T readPeriod(
            final Function<Period, T> mapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final Period value = readPeriod(collectionFieldName, classFieldName, document);
        return mapper.apply(value);

    }


    public final <T> Optional<T> readPeriodOptional(
            final Function<Period, T> mapper,
            final String collectionFieldName,
            final Document document) {

        return readPeriodOptional(collectionFieldName, document).map(mapper);

    }


    public final Period readPeriod(
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        final String value = document.getString(collectionFieldName);
        MissingFieldException.throwIfBlank(value, document.getObjectId("_id"), collectionFieldName, classFieldName, this.collectionName);
        return Period.parse(value);

    }


    public final Optional<Period> readPeriodOptional(final String collectionFieldName, final Document document) {

        final String value = document.getString(collectionFieldName);
        return Optional.ofNullable(value).map(Period::parse);

    }


    public final <KEY, TARGET_VALUE> Map<KEY, TARGET_VALUE> readMap(
            final Function<String, KEY> keyMapper,
            final Function<Object, TARGET_VALUE> valueMapper,
            final String collectionFieldName,
            final String classFieldName,
            final Document document) {

        @SuppressWarnings("unchecked")
        final Map<String, Object> rawMap = (Map<String, Object>) document.get(collectionFieldName);

        if (rawMap == null) {

            return Collections.emptyMap();

        } else {

            // TODO catch exception and wrap it using collectionFieldName, classfieldName, collectionName.
            return rawMap
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(o -> keyMapper.apply(o.getKey()), o -> valueMapper.apply(o.getValue())));

        }

    }


    public final void createIndex(final Document indexKeys, final IndexOptions indexOptions) {

        getCollection().createIndex(indexKeys, indexOptions);

    }


}
