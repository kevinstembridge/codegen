package com.mahanaroad.mongogen.sample.simple;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public final class SomeStatusTest {


    @Test
    public void testEnumValues() {

        final SomeStatus[] actualValues = SomeStatus.values();
        final SomeStatus[] expectedValues = new SomeStatus[] {SomeStatus.OK, SomeStatus.NOT_OK};

        assertEquals(actualValues, expectedValues);

    }


}