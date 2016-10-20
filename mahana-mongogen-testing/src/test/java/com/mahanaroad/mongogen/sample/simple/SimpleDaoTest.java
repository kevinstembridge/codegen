package com.mahanaroad.mongogen.sample.simple;

import com.mahanaroad.mongogen.AbstractIntegrationTest;
import com.mahanaroad.mongogen.SampleTestData;
import com.mahanaroad.mongogen.domain.AbstractEntity;
import com.mahanaroad.mongogen.domain.DocumentNotFoundException;
import com.mahanaroad.mongogen.persist.MongoClientFacade;
import com.mahanaroad.mongogen.sample.types.SomeStringType;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.mahanaroad.mongogen.SampleTestData.anyRandomString;
import static com.mahanaroad.mongogen.sample.simple.SimpleEntityTestBuilder.aSimpleEntity;
import static java.util.Arrays.asList;
import static org.testng.Assert.*;


public class SimpleDaoTest extends AbstractIntegrationTest {

    @Autowired
    private SimpleDao simpleDao;

    @Autowired
    private MongoClientFacade mongoClientFacade;


    @Test
    public void testInsertAndFindById() {

        final SimpleEntity simpleEntity = aSimpleEntity().withLastModifiedTimestampUtc(Instant.now()).build();

        this.simpleDao.insert(simpleEntity);

        final SimpleEntity actual = this.simpleDao.findById(simpleEntity.getId());

        assertNotNull(actual);

        assertEquals(actual.getId(), simpleEntity.getId());
        assertEquals(actual.getCreatedTimestampUtc(), simpleEntity.getCreatedTimestampUtc());

        // lastModifiedTimestampUtc must always be set to empty for initial insert
        assertFalse(actual.getLastModifiedTimestampUtc().isPresent());

        assertEntityFields(actual, simpleEntity);

    }


    @Test
    public void testFindOneOptionalBySomeNonNullableString() {

        //GIVEN
        final String someNonNullableString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeNonNullableString(someNonNullableString).build();
        this.simpleDao.insert(simpleEntity);

        //WHEN
        final Optional<SimpleEntity> actualOptional = this.simpleDao.findOneOptionalBySomeNonNullableString(someNonNullableString);

        //THEN
        final SimpleEntity actual = actualOptional.orElseThrow(() -> new AssertionError("Expected to find a result"));
        assertEquals(actual.getId(), simpleEntity.getId());

        // AND WHEN
        final Optional<SimpleEntity> shouldBeEmptyOptional = this.simpleDao.findOneOptionalBySomeNonNullableString(UUID.randomUUID().toString());

        // THEN
        assertFalse(shouldBeEmptyOptional.isPresent());

    }


    @Test
    public void testFindOneOptionalBySomeStringType() {

        //GIVEN
        final SimpleEntity simpleEntity = aSimpleEntity().build();
        this.simpleDao.insert(simpleEntity);

        //WHEN
        final Optional<SimpleEntity> actualOptional = this.simpleDao.findOneOptionalBySomeStringType(simpleEntity.getSomeStringType());

        //THEN
        final SimpleEntity actual = actualOptional.orElseThrow(() -> new AssertionError("Expected to find a result"));
        assertEquals(actual.getId(), simpleEntity.getId());

        // AND WHEN
        final Optional<SimpleEntity> shouldBeEmptyOptional = this.simpleDao.findOneOptionalBySomeStringType(new SomeStringType(UUID.randomUUID().toString()));

        // THEN
        assertFalse(shouldBeEmptyOptional.isPresent());

    }


    @Test
    public void testFindOneBySomeNonNullableString() {

        //GIVEN
        final String someNonNullableString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeNonNullableString(someNonNullableString).build();
        this.simpleDao.insert(simpleEntity);

        //WHEN
        final SimpleEntity actual = this.simpleDao.findOneBySomeNonNullableString(someNonNullableString);

        //THEN
        assertEquals(actual.getId(), simpleEntity.getId());

        try {
            this.simpleDao.findOneBySomeNonNullableString(UUID.randomUUID().toString());
            Assert.fail("Should have thrown an DocumentNotFoundException");
        } catch (DocumentNotFoundException e) {
            assertEquals(e.getCollectionName(), this.simpleDao.getCollectionName());
        }

    }


    @Test
    public void testFindOneBySomeOptionalUniqueString() {

        //GIVEN
        final String someString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeOptionalString(Optional.of(someString)).build();
        this.simpleDao.insert(simpleEntity);

        //WHEN
        final SimpleEntity actual = this.simpleDao.findOneBySomeOptionalString(someString);

        //THEN
        assertEquals(actual.getId(), simpleEntity.getId());

        try {
            this.simpleDao.findOneBySomeOptionalString(UUID.randomUUID().toString());
            Assert.fail("Should have thrown an DocumentNotFoundException");
        } catch (DocumentNotFoundException e) {
            assertEquals(e.getCollectionName(), this.simpleDao.getCollectionName());
        }

    }


    @Test
    public void testFindOneOptionalBySomeOptionalUniqueString() {

        //GIVEN
        final String someString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeOptionalString(Optional.of(someString)).build();
        this.simpleDao.insert(simpleEntity);

        //WHEN
        final Optional<SimpleEntity> actual = this.simpleDao.findOneOptionalBySomeOptionalString(someString);

        //THEN
        assertEquals(actual.get().getId(), simpleEntity.getId());

        final Optional<SimpleEntity> shouldBeEmpty = this.simpleDao.findOneOptionalBySomeOptionalString(UUID.randomUUID().toString());
        assertEquals(shouldBeEmpty, Optional.empty());

    }


    @Test
    public void testFindOneOptionalBySomeOptionalUniqueStringThatIsEmpty() {

        //GIVEN
        final String someString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeOptionalString(Optional.empty()).build();
        this.simpleDao.insert(simpleEntity);

        //WHEN
        final Optional<SimpleEntity> shouldBeEmpty = this.simpleDao.findOneOptionalBySomeOptionalString(someString);

        //THEN
        assertEquals(shouldBeEmpty, Optional.empty());

    }


    @Test
    public void testFindOneBySomeOptionalUniqueStringThatIsEmpty() {

        //GIVEN
        final String someString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeOptionalString(Optional.empty()).build();
        this.simpleDao.insert(simpleEntity);

        try {
            this.simpleDao.findOneBySomeOptionalString(someString);
            Assert.fail("Should have thrown an DocumentNotFoundException");
        } catch (DocumentNotFoundException e) {
            assertEquals(e.getCollectionName(), this.simpleDao.getCollectionName());
        }

    }


    @Test
    public void testFindOneBySomeUniqueStringTypeField() {

        //GIVEN
        final SimpleEntity simpleEntity = aSimpleEntity().build();
        this.simpleDao.insert(simpleEntity);

        //WHEN
        final SimpleEntity actual = this.simpleDao.findOneBySomeStringType(simpleEntity.getSomeStringType());

        //THEN
        assertEquals(actual.getId(), simpleEntity.getId());

        try {
            this.simpleDao.findOneBySomeStringType(new SomeStringType(UUID.randomUUID().toString()));
            Assert.fail("Should have thrown an DocumentNotFoundException");
        } catch (DocumentNotFoundException e) {
            assertEquals(e.getCollectionName(), this.simpleDao.getCollectionName());
        }

    }


    @Test
    public void testExistsBySomeUniqueField() {

        //GIVEN
        final SimpleEntity simpleEntity = aSimpleEntity().build();
        this.simpleDao.insert(simpleEntity);

        //THEN
        assertTrue(this.simpleDao.existsBySomeStringType(simpleEntity.getSomeStringType()));
        assertFalse(this.simpleDao.existsBySomeStringType(new SomeStringType(UUID.randomUUID().toString())));

    }


    @Test
    public void testExistsBySomeOptionalUniqueField() {

        //GIVEN
        final String someString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeOptionalString(Optional.of(someString)).build();
        this.simpleDao.insert(simpleEntity);

        //THEN
        assertTrue(this.simpleDao.existsBySomeOptionalString(someString));
        assertFalse(this.simpleDao.existsBySomeOptionalString(UUID.randomUUID().toString()));

    }


    @Test
    public void testExistsBySomeOptionalUniqueFieldThatIsEmpty() {

        //GIVEN
        final String someString = anyRandomString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeOptionalString(Optional.empty()).build();
        this.simpleDao.insert(simpleEntity);

        //THEN
        assertFalse(this.simpleDao.existsBySomeOptionalString(someString));

    }


    @Test
    public void should_ignore_legacy_attributes() {

        final SimpleEntity simpleEntity = aSimpleEntity().build();

        this.simpleDao.insert(simpleEntity);

        this.simpleDao.getCollection()
                .updateOne(
                        new Document("_id", simpleEntity.getId()),
                        new Document("$set", new Document("someLegacyField", "Some value"))
                                .append("$set", new Document("someLegacyDocument", new Document("someLegacyEmbeddedField", "Some value").append("someLegacyEmbeddedArray", asList("one", "two"))))
                                .append("$set", new Document("someLegacyArrayField", asList("one", "two"))));

        this.simpleDao.findById(simpleEntity.getId());

    }


    @Test
    public void testUpsertBySomeNonNullableField() {

        final SimpleEntityTestBuilder simpleEntityTestBuilder = aSimpleEntity();
        final SimpleEntity simpleEntityOriginal = simpleEntityTestBuilder.build();

        final SimpleEntity actualOriginal = this.simpleDao.upsertBySomeNonNullableString(simpleEntityOriginal);
        assertEquals(actualOriginal.getId(), simpleEntityOriginal.getId());
        assertEntityFields(actualOriginal, simpleEntityOriginal);

        final SimpleEntity actualOriginalDirectFromDb = this.simpleDao.findById(simpleEntityOriginal.getId());
        assertEntityFields(actualOriginalDirectFromDb, actualOriginal);

        final SimpleEntity expectedUpdatedEntity = simpleEntityTestBuilder
                .withId(ObjectId.get())
                .withLaterCreatedTimestampUtc()
                .withSomeModifiableInt(actualOriginal.getSomeModifiableInt() + 1)
                .withSomeBoolean(actualOriginal.getSomeBoolean() == false)
                .build();

        final SimpleEntity actualUpdated = this.simpleDao.upsertBySomeNonNullableString(expectedUpdatedEntity);
        assertEquals(actualUpdated.getId(), simpleEntityOriginal.getId());
        assertNotEquals(actualUpdated.getId(), expectedUpdatedEntity.getId());
        assertNotEquals(actualUpdated.getCreatedTimestampUtc(), expectedUpdatedEntity.getCreatedTimestampUtc());
        assertEquals(actualUpdated.getSomeModifiableInt(), expectedUpdatedEntity.getSomeModifiableInt());
        assertEquals(actualUpdated.getSomeBoolean(), actualOriginal.getSomeBoolean());

    }


    @Test
    public void testUpsertBySomeStringType() {

        final SimpleEntityTestBuilder simpleEntityTestBuilder = aSimpleEntity();
        final SimpleEntity simpleEntityOriginal = simpleEntityTestBuilder.build();

        final SimpleEntity actualOriginal = this.simpleDao.upsertBySomeStringType(simpleEntityOriginal);
        assertEquals(actualOriginal.getId(), simpleEntityOriginal.getId());
        assertEntityFields(actualOriginal, simpleEntityOriginal);

        final SimpleEntity actualOriginalDirectFromDb = this.simpleDao.findById(simpleEntityOriginal.getId());
        assertEntityFields(actualOriginalDirectFromDb, actualOriginal);

        final SimpleEntity expectedUpdatedEntity = simpleEntityTestBuilder
                .withId(ObjectId.get())
                .withLaterCreatedTimestampUtc()
                .withSomeModifiableInt(actualOriginal.getSomeModifiableInt() + 1)
                .withSomeBoolean(actualOriginal.getSomeBoolean() == false)
                .build();

        final SimpleEntity actualUpdated = this.simpleDao.upsertBySomeStringType(expectedUpdatedEntity);
        assertEquals(actualUpdated.getId(), simpleEntityOriginal.getId());
        assertNotEquals(actualUpdated.getId(), expectedUpdatedEntity.getId());
        assertNotEquals(actualUpdated.getCreatedTimestampUtc(), expectedUpdatedEntity.getCreatedTimestampUtc());
        assertEquals(actualUpdated.getSomeModifiableInt(), expectedUpdatedEntity.getSomeModifiableInt());
        assertEquals(actualUpdated.getSomeBoolean(), actualOriginal.getSomeBoolean());

    }


    @Test
    public void testCountBySomeStringTypeField() {

        final SomeStringType someStringType = new SomeStringType(anyRandomString());

        this.simpleDao.insert(aSimpleEntity()
                .withSomeStringType(someStringType)
                .build());

        long actualCount = this.simpleDao.count(SimpleEntityFilters.someStringType().eq(someStringType));

        assertEquals(actualCount, 1);

    }


    @Test
    public void testCountBySomeNonNullableString() {

        final String someNonNullableString = SampleTestData.anyRandomString();

        this.simpleDao.insert(aSimpleEntity()
                .withSomeNonNullableString(someNonNullableString)
                .build());

        long actualCount = this.simpleDao.count(SimpleEntityFilters.someNonNullableString().eq(someNonNullableString));

        assertEquals(actualCount, 1);

    }


    @Test
    public void testFindByIndex() {

        //GIVEN
        final String someNonNullableString = SampleTestData.anyRandomString();
        final boolean someBoolean = SampleTestData.anyRandomBoolean();
        final SimpleEntity simpleEntity1 = SimpleEntityTestBuilder.aSimpleEntity().withSomeNonNullableString(someNonNullableString).withSomeBoolean(someBoolean).build();
        final SimpleEntity simpleEntity2 = SimpleEntityTestBuilder.aSimpleEntity().withSomeNonNullableString(someNonNullableString).withSomeBoolean(someBoolean).build();
        final SimpleEntity simpleEntity3 = SimpleEntityTestBuilder.aSimpleEntity().withSomeNonNullableString(someNonNullableString).withSomeBoolean(someBoolean == false).build();

        this.simpleDao.insert(simpleEntity1);
        this.simpleDao.insert(simpleEntity2);
        this.simpleDao.insert(simpleEntity3);

        final Set<ObjectId> expectedIds = new HashSet<>();
        expectedIds.add(simpleEntity1.getId());
        expectedIds.add(simpleEntity2.getId());

        //WHEN
        final List<SimpleEntity> actual = this.simpleDao.findBySomeNonNullableStringAndSomeBoolean(someNonNullableString, someBoolean);

        // THEN
        final Set<ObjectId> actualIds = actual.stream().map(AbstractEntity::getId).collect(Collectors.toSet());
        Assert.assertEquals(actualIds, expectedIds);

    }


    @Test
    public void testUpdate() {

        final SimpleEntityTestBuilder entityBuilder = SimpleEntityTestBuilder.aSimpleEntity();
        final SimpleEntity entityBeforeUpdate = entityBuilder.build();
        final ObjectId id = entityBeforeUpdate.getId();
        final Instant modifiedInstant = Instant.now().minusSeconds(10);
        final String modifiedString = UUID.randomUUID().toString();

        entityBuilder.withSomeModifiableInstant(modifiedInstant);
        entityBuilder.withSomeModifiableString(modifiedString);
        entityBuilder.withSomeOptionalModifiableInstant(Optional.empty());

        final SimpleEntity expectedEntity = entityBuilder.build();


        this.simpleDao.insert(entityBeforeUpdate);

        final SimpleEntityUpdater entityUpdater = SimpleEntityUpdater.forId(id)
                .setSomeModifiableInstant(modifiedInstant)
                .setSomeModifiableString(modifiedString)
                .setSomeOptionalModifiableInstant(Optional.empty())
                .build();
        this.simpleDao.setFields(entityUpdater);

        final SimpleEntity entityAfterUpdate = this.simpleDao.findById(id);

        assertEntityFields(entityAfterUpdate, expectedEntity);

    }


    private void assertEntityFields(final SimpleEntity actual, final SimpleEntity expected) {

        assertEquals(actual.getSomeBoolean(), expected.getSomeBoolean());
        assertEquals(actual.getSomeBooleanType(), expected.getSomeBooleanType());
        assertEquals(actual.getSomeIntType(), expected.getSomeIntType());
        assertEquals(actual.getSomeListOfEnums(), expected.getSomeListOfEnums());
        assertEquals(actual.getSomeListOfInstants(), expected.getSomeListOfInstants());
        assertEquals(actual.getSomeListOfLocalDates(), expected.getSomeListOfLocalDates());
        assertEquals(actual.getSomeListOfPeriods(), expected.getSomeListOfPeriods());
        assertEquals(actual.getSomeListOfStrings(), expected.getSomeListOfStrings());
        assertEquals(actual.getSomeListOfStringTypes(), expected.getSomeListOfStringTypes());
        assertEquals(actual.getSomeLongType(), expected.getSomeLongType());
        assertEquals(actual.getSomeMapOfStringToInteger(), expected.getSomeMapOfStringToInteger());
        assertEquals(actual.getSomeMapOfStringTypeToStringType(), expected.getSomeMapOfStringTypeToStringType());
        assertEquals(actual.getSomeModifiableInstant(), expected.getSomeModifiableInstant());
        assertEquals(actual.getSomeModifiableInt(), expected.getSomeModifiableInt());
        assertEquals(actual.getSomeModifiableLocalDate(), expected.getSomeModifiableLocalDate());
        assertEquals(actual.getSomeModifiablePeriod(), expected.getSomeModifiablePeriod());
        assertEquals(actual.getSomeModifiableString(), expected.getSomeModifiableString());
        assertEquals(actual.getSomeNonNullableString(), expected.getSomeNonNullableString());
        assertEquals(actual.getSomeOptionalBoolean(), expected.getSomeOptionalBoolean());
        assertEquals(actual.getSomeOptionalBooleanType(), expected.getSomeOptionalBooleanType());
        assertEquals(actual.getSomeOptionalModifiableInstant(), expected.getSomeOptionalModifiableInstant());
        assertEquals(actual.getSomeOptionalProvidedBooleanType(), expected.getSomeOptionalProvidedBooleanType());
        assertEquals(actual.getSomeOptionalStatus(), expected.getSomeOptionalStatus());
        assertEquals(actual.getSomeOptionalString(), expected.getSomeOptionalString());
        assertEquals(actual.getSomeOptionalInstant(), expected.getSomeOptionalInstant());
        assertEquals(actual.getSomeOptionalInt(), expected.getSomeOptionalInt());
        assertEquals(actual.getSomeOptionalIntType(), expected.getSomeOptionalIntType());
        assertEquals(actual.getSomeOptionalLongType(), expected.getSomeOptionalLongType());
        assertEquals(actual.getSomeOptionalPeriod(), expected.getSomeOptionalPeriod());
        assertEquals(actual.getSomeOptionalProvidedBooleanType(), expected.getSomeOptionalProvidedBooleanType());
        assertEquals(actual.getSomeOptionalProvidedIntType(), expected.getSomeOptionalProvidedIntType());
        assertEquals(actual.getSomeOptionalProvidedLongType(), expected.getSomeOptionalProvidedLongType());
        assertEquals(actual.getSomeOptionalProvidedStringType(), expected.getSomeOptionalProvidedStringType());
        assertEquals(actual.getSomeOptionalStringType(), expected.getSomeOptionalStringType());
        assertEquals(actual.getSomeProvidedBooleanType(), expected.getSomeProvidedBooleanType());
        assertEquals(actual.getSomeProvidedIntType(), expected.getSomeProvidedIntType());
        assertEquals(actual.getSomeProvidedStringType(), expected.getSomeProvidedStringType());
        assertEquals(actual.getSomeProvidedLongType(), expected.getSomeProvidedLongType());
        assertEquals(actual.getSomeStatus(), expected.getSomeStatus());
        assertEquals(actual.getSomeStringType(), expected.getSomeStringType());

    }


}
