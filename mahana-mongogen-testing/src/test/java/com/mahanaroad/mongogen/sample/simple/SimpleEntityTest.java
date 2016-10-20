package com.mahanaroad.mongogen.sample.simple;


import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.domain.AbstractEntity;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mahanaroad.mongogen.sample.simple.SimpleEntityTestBuilder.aSimpleEntity;
import static org.testng.Assert.*;


public class SimpleEntityTest {


    @Test
    public void should_extend_AbstractEntity() {

        assertEquals(SimpleEntity.class.getSuperclass(), AbstractEntity.class);

    }


    @Test
    public void should_throw_BlankStringException_if_mandatory_constructor_arg_is_null_or_blank() {

        try {
            aSimpleEntity().withSomeNonNullableString(null).build();
            fail("Should have thrown a BlankStringException");
        } catch (BlankStringException e) {
            assertEquals(e.getArgumentName(), "someNonNullableString");
        }

    }


    @Test
    public void should_throw_NullPointerException_if_mandatory_constructor_arg_is_null() {

        try {
            aSimpleEntity().withSomeModifiableInstant(null).build();
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "someModifiableInstant");
        }

    }


    @Test
    public void should_throw_NullPointerException_for_optional_constructor_arg() {

        try {
            aSimpleEntity().withSomeOptionalString(null).build();
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "someOptionalString");
        }

    }


    @Test
    public void testGetter() {

        final String expectedSomeNonNullableString = UUID.randomUUID().toString();
        final SimpleEntity simpleEntity = aSimpleEntity().withSomeNonNullableString(expectedSomeNonNullableString).build();

        assertEquals(simpleEntity.getSomeNonNullableString(), expectedSomeNonNullableString);

    }


    @Test
    public void testSetterForNonPrimitive() {

        final String expectedSomeModifiableString = UUID.randomUUID().toString();
        final SimpleEntity simpleEntity = aSimpleEntity().build();

        simpleEntity.setSomeModifiableString(expectedSomeModifiableString);

        assertEquals(simpleEntity.getSomeModifiableString(), expectedSomeModifiableString);

    }


    @Test
    public void testSetterForPrimitive() {

        final SimpleEntity simpleEntity = aSimpleEntity().build();
        final int originalInt = simpleEntity.getSomeModifiableInt();
        final int newInt = originalInt + 1;

        simpleEntity.setSomeModifiableInt(newInt);

        assertEquals(simpleEntity.getSomeModifiableInt(), newInt);

    }


    @Test
    public void setter_should_throw_BlankStringException() {

        final SimpleEntity simpleEntity = aSimpleEntity().build();

        try {
            simpleEntity.setSomeModifiableString(null);
            fail("Should have thrown a BlankStringException");
        } catch (BlankStringException e) {
            assertEquals(e.getArgumentName(), "someModifiableString");
        }

    }


    @Test
    public void setter_should_throw_NullPointerException() {

        final SimpleEntity simpleEntity = aSimpleEntity().build();

        try {
            simpleEntity.setSomeModifiableInstant(null);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "someModifiableInstant");
        }

    }


    @Test
    public void testAllFieldsExist() {

        final Set<String> expectedFieldNames = new TreeSet<>();
        expectedFieldNames.add("someNonNullableString");
        expectedFieldNames.add("someModifiableInstant");
        expectedFieldNames.add("someModifiableInt");
        expectedFieldNames.add("someModifiablePeriod");
        expectedFieldNames.add("someModifiableString");
        expectedFieldNames.add("someModifiableLocalDate");
        expectedFieldNames.add("someOptionalString");
        expectedFieldNames.add("someBoolean");
        expectedFieldNames.add("someStatus");
        expectedFieldNames.add("someListOfEnums");
        expectedFieldNames.add("someListOfInstants");
        expectedFieldNames.add("someListOfLocalDates");
        expectedFieldNames.add("someListOfPeriods");
        expectedFieldNames.add("someListOfStringTypes");
        expectedFieldNames.add("someListOfStrings");
        expectedFieldNames.add("someOptionalStatus");
        expectedFieldNames.add("someOptionalBoolean");
        expectedFieldNames.add("someOptionalInt");
        expectedFieldNames.add("someOptionalInstant");
        expectedFieldNames.add("someOptionalPeriod");
        expectedFieldNames.add("someOptionalStringType");
        expectedFieldNames.add("someOptionalProvidedStringType");
        expectedFieldNames.add("someProvidedStringType");
        expectedFieldNames.add("someStringType");
        expectedFieldNames.add("someIntType");
        expectedFieldNames.add("someBooleanType");
        expectedFieldNames.add("someLongType");
        expectedFieldNames.add("someMapOfStringToInteger");
        expectedFieldNames.add("someMapOfStringTypeToStringType");
        expectedFieldNames.add("someProvidedIntType");
        expectedFieldNames.add("someProvidedBooleanType");
        expectedFieldNames.add("someProvidedLongType");
        expectedFieldNames.add("someOptionalModifiableInstant");
        expectedFieldNames.add("someOptionalProvidedIntType");
        expectedFieldNames.add("someOptionalProvidedBooleanType");
        expectedFieldNames.add("someOptionalProvidedLongType");
        expectedFieldNames.add("someOptionalIntType");
        expectedFieldNames.add("someOptionalBooleanType");
        expectedFieldNames.add("someOptionalLongType");

        final Set<String> actualFieldNames = getActualFieldNames();

        assertEquals(actualFieldNames, expectedFieldNames);

        assertFalse(actualFieldNames.contains("id"));
        assertFalse(actualFieldNames.contains("createdTimestampUtc"));
        assertFalse(actualFieldNames.contains("lastModifiedTimestampUtc"));

    }


    //TODO should also confirm field type, not just name
    private Set<String> getActualFieldNames() {

        final Field[] declaredFields = SimpleEntity.class.getDeclaredFields();
        return Arrays.stream(declaredFields)
                .map(Field::getName)
                .collect(Collectors.toCollection(TreeSet::new));

    }


    @Test(dataProvider = "provideUnwantedSetterNames")
    public void should_not_have_setters_for_unmodifiable_fields(final String unwantedSetterName) {

        confirmMethodDoesNotExist(unwantedSetterName);

    }


    private void confirmMethodDoesNotExist(final String unwantedMethodName) {

        final Method[] declaredMethods = SimpleEntity.class.getDeclaredMethods();

        for (Method method : declaredMethods) {

            if (method.getName().equals(unwantedMethodName)) {
                fail("Class must not contain a method named " + unwantedMethodName);
            }

        }

    }


    @DataProvider(name = "provideUnwantedSetterNames")
    public Object[][] provideUnwantedSetterNames() {

        return new String[][] {
                {"setSomeNonNullableString"},
                {"setSomeOptionalString"},
                {"setSomeBoolean"},
                {"setSomeStatus"}
        };

    }


}
