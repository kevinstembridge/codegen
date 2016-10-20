package com.mahanaroad.mongogen.migration;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.mahanaroad.mongogen.migration.MongogenMigrationId.of;
import static org.testng.Assert.assertEquals;

public class MongogenMigrationIdTest {


    @Test(dataProvider = "provideSamplesForCompareTo")
    public void testCompareTo(final MongogenMigrationId id1, final MongogenMigrationId id2, final int expectedValue) {

        int actual = id1.compareTo(id2);
        assertEquals(actual, expectedValue);

    }


    @DataProvider(name = "provideSamplesForCompareTo")
    public Object[][] provideSamplesForCompareTo() {

        return new Object[][] {
                {of(1), of(2), -1},
                {of(1), of(2, 1), -1},
                {of(2), of(1), 1},
                {of(1, 2, 3), of(1, 2, 3), 0},
                {of(1, 2), of(2), -1},
                {of(1), of(7), -1},
        };

    }


}