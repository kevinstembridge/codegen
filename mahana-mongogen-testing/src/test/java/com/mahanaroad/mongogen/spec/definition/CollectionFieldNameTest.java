package com.mahanaroad.mongogen.spec.definition;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class CollectionFieldNameTest {


    @Test
    public void testToValidJavaIdentifier() {

        assertEquals(new CollectionFieldName("abc").toValidJavaIdentifier(), "abc");
        assertEquals(new CollectionFieldName("a-bc").toValidJavaIdentifier(), "a_45_bc");
        assertEquals(new CollectionFieldName("a*bc").toValidJavaIdentifier(), "a_42_bc");
        assertEquals(new CollectionFieldName("a|bc").toValidJavaIdentifier(), "a_124_bc");
        assertEquals(new CollectionFieldName("a|bc").toValidJavaIdentifier(), "a_124_bc");

    }


}