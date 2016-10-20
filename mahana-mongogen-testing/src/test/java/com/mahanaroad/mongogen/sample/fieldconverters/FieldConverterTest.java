package com.mahanaroad.mongogen.sample.fieldconverters;

import com.mahanaroad.mongogen.AbstractIntegrationTest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

public class FieldConverterTest extends AbstractIntegrationTest {


    @Autowired
    private FieldConversionDao fieldConversionDao;

    @Autowired
    private FieldConverterTestFieldTypeLevelFieldReader fieldConverterTestFieldTypeLevelFieldReader;

    @Autowired
    private FieldConverterTestFieldLevelFieldReader fieldConverterTestFieldLevelFieldReader;

    @Autowired
    private FieldConverterTestFieldTypeLevelFieldWriter fieldConverterTestFieldTypeLevelFieldWriter;

    @Autowired
    private FieldConverterTestFieldLevelFieldWriter fieldConverterTestFieldLevelFieldWriter;


    @Test
    public void shouldUseFieldTypeConverter() {

        final String someStringWithFieldLevelConverters = UUID.randomUUID().toString();
        final String someStringWithFieldTypeLevelConverters = UUID.randomUUID().toString();

        final String expectedFieldTypeValueAppendedByWriter = UUID.randomUUID().toString();
        final String expectedFieldTypeValueAppendedByReader = UUID.randomUUID().toString();
        final String expectedFieldValueAppendedByWriter = UUID.randomUUID().toString();
        final String expectedFieldValueAppendedByReader = UUID.randomUUID().toString();

        this.fieldConverterTestFieldTypeLevelFieldWriter.setNextValue(expectedFieldTypeValueAppendedByWriter);
        this.fieldConverterTestFieldLevelFieldWriter.setNextValue(expectedFieldValueAppendedByWriter);

        this.fieldConverterTestFieldTypeLevelFieldReader.setNextValue(expectedFieldTypeValueAppendedByReader);
        this.fieldConverterTestFieldLevelFieldReader.setNextValue(expectedFieldValueAppendedByReader);

        final FieldConversionEntity entity = new FieldConversionEntity(
                Instant.now(),
                ObjectId.get(),
                Optional.empty(),
                someStringWithFieldLevelConverters,
                someStringWithFieldTypeLevelConverters);

        this.fieldConversionDao.insert(entity);

        final FieldConversionEntity foundEntity = this.fieldConversionDao.findById(entity.getId());

        assertEquals(foundEntity.getSomeStringWithFieldTypeLevelReader(), someStringWithFieldTypeLevelConverters + expectedFieldTypeValueAppendedByWriter + expectedFieldTypeValueAppendedByReader);
        assertEquals(foundEntity.getSomeStringWithFieldLevelReader(), someStringWithFieldLevelConverters + expectedFieldValueAppendedByWriter + expectedFieldValueAppendedByReader);

    }


}
