package com.mahanaroad.mongogen.sample;

import com.mahanaroad.mongogen.spec.AbstractSpec;
import com.mahanaroad.mongogen.spec.definition.BooleanTypeDef;
import com.mahanaroad.mongogen.spec.definition.EntityDef;
import com.mahanaroad.mongogen.spec.definition.EnumDef;
import com.mahanaroad.mongogen.spec.definition.HtmlInputType;
import com.mahanaroad.mongogen.spec.definition.IntTypeDef;
import com.mahanaroad.mongogen.spec.definition.LongTypeDef;
import com.mahanaroad.mongogen.spec.definition.StringTypeDef;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;

public class TestingSpec extends AbstractSpec {


    public TestingSpec() {

        super(new PackageName("com.mahanaroad.mongogen.sample"));

        final EnumDef someStatusEnumDef = enumDef("simple", "SomeStatus")
                .values(
                        enumValue("OK"),
                        enumValue("NOT_OK"))
                .build();

        final StringTypeDef someStringTypeDef = stringType("types", "SomeStringType").alwaysLowerCase().build();
        final StringTypeDef firstNameStringTypeDef = stringType("types", "FirstName").build();
        final StringTypeDef lastNameStringTypeDef = stringType("types", "LastName").build();
        final StringTypeDef someProvidedStringTypeDef = stringType("com.mahanaroad.mongogen.sample.types.SomeProvidedStringType").provided().build();
        final StringTypeDef emailAddressStringType = stringType("contact", "EmailAddress").alwaysLowerCase().build();

        final IntTypeDef someIntTypeDef = intType("types", "SomeIntType").build();
        final IntTypeDef someProvidedIntTypeDef = intType("com.mahanaroad.mongogen.sample.types.SomeProvidedIntType").provided().build();

        final LongTypeDef someLongTypeDef = longType("types", "SomeLongType").build();
        final LongTypeDef someProvidedLongTypeDef = longType("com.mahanaroad.mongogen.sample.types.SomeProvidedLongType").provided().build();

        final BooleanTypeDef someBooleanTypeDef = booleanType("types", "SomeBooleanType").build();
        final BooleanTypeDef someProvidedBooleanTypeDef = booleanType("com.mahanaroad.mongogen.sample.types.SomeProvidedBooleanType").provided().build();

        entity("simple", "Simple")
                .fields(
                        field("someNonNullableString", FieldType.STRING).collectionFieldName("snns").unique(),
                        field("someOptionalString", FieldType.STRING).collectionFieldName("sos").optional().unique(),
                        field("someBoolean", FieldType.BOOLEAN).collectionFieldName("sb"),
                        field("someModifiableString", FieldType.STRING).collectionFieldName("sms").modifiable(),
                        field("someModifiableInstant", FieldType.INSTANT).collectionFieldName("smi").modifiable(),
                        field("someModifiableLocalDate", FieldType.LOCAL_DATE).collectionFieldName("smld").modifiable(),
                        field("someOptionalInstant", FieldType.INSTANT).collectionFieldName("soi").optional(),
                        field("someOptionalModifiableInstant", FieldType.INSTANT).collectionFieldName("somi").optional().modifiable(),
                        field("someModifiablePeriod", FieldType.PERIOD).collectionFieldName("smp").modifiable(),
                        field("someOptionalPeriod", FieldType.PERIOD).collectionFieldName("sop").optional(),
                        field("someModifiableInt", FieldType.INT).collectionFieldName("smint").modifiable(),
                        field("someOptionalInt", FieldType.INT).collectionFieldName("soint").modifiable().optional(),
                        field("someStatus", someStatusEnumDef).collectionFieldName("ss"),
                        field("someOptionalStatus", someStatusEnumDef).collectionFieldName("sost").optional(),
                        field("someOptionalBoolean", FieldType.BOOLEAN).collectionFieldName("sob").optional(),
                        field("someStringType", someStringTypeDef).collectionFieldName("sst").unique(),
                        field("someProvidedStringType", someProvidedStringTypeDef).collectionFieldName("spst"),
                        field("someOptionalStringType", someStringTypeDef).collectionFieldName("sostyp").optional(),
                        field("someOptionalProvidedStringType", someProvidedStringTypeDef).collectionFieldName("sopstyp").optional(),
                        field("someIntType", someIntTypeDef).collectionFieldName("sit").unique(),
                        field("someProvidedIntType", someProvidedIntTypeDef).collectionFieldName("spit"),
                        field("someOptionalIntType", someIntTypeDef).collectionFieldName("soityp").optional(),
                        field("someOptionalProvidedIntType", someProvidedIntTypeDef).collectionFieldName("sopityp").optional(),
                        field("someLongType", someLongTypeDef).collectionFieldName("slt").unique(),
                        field("someProvidedLongType", someProvidedLongTypeDef).collectionFieldName("splt"),
                        field("someOptionalLongType", someLongTypeDef).collectionFieldName("soltyp").optional(),
                        field("someOptionalProvidedLongType", someProvidedLongTypeDef).collectionFieldName("sopltyp").optional(),
                        field("someBooleanType", someBooleanTypeDef).collectionFieldName("sbt").unique(),
                        field("someProvidedBooleanType", someProvidedBooleanTypeDef).collectionFieldName("spbt"),
                        field("someOptionalBooleanType", someBooleanTypeDef).collectionFieldName("sobtyp").optional(),
                        field("someOptionalProvidedBooleanType", someProvidedBooleanTypeDef).collectionFieldName("sopbtyp").optional(),
                        field("someListOfStrings", listOf(FieldType.STRING)).collectionFieldName("los"),
                        field("someListOfStringTypes", listOf(someStringTypeDef)).collectionFieldName("lost"),
                        field("someListOfEnums", listOf(someStatusEnumDef)).collectionFieldName("lostat"),
                        field("someListOfInstants", listOf(FieldType.INSTANT)).collectionFieldName("loi"),
                        field("someListOfLocalDates", listOf(FieldType.LOCAL_DATE)).collectionFieldName("lold"),
                        field("someListOfPeriods", listOf(FieldType.PERIOD)).collectionFieldName("lop"),
                        field("someMapOfStringToInteger", mapOfString().to(Fqcn.INTEGER)).collectionFieldName("mosti"),
                        field("someMapOfStringTypeToStringType", mapOf(someStringTypeDef).to(someStringTypeDef)).collectionFieldName("mosttst")
                )
                .index()
                    .withField("someNonNullableString")
                    .withField("someBoolean")
                .and()
                .build();


        final EntityDef partyEntityDef = entity("party", "Party")
                .isAbstract()
                .typeDiscriminator("PA")
                .fields(

                ).build();


        entity("org", "Organization")
                .superclass(partyEntityDef)
                .typeDiscriminator("ORG")
                .fields(
                        field("name", FieldType.STRING).collectionFieldName("n").modifiable()
                ).build();


        final EntityDef personEntityDef = entity("person", "Person")
                .superclass(partyEntityDef)
                .typeDiscriminator("PE")
                .fields(
                        field("firstName", firstNameStringTypeDef).collectionFieldName("fn").modifiable(),
                        field("lastName", lastNameStringTypeDef).collectionFieldName("ln").modifiable()
                ).build();


        entity("user", "User")
                .superclass(personEntityDef)
                .typeDiscriminator("USR")
                .fields(
                        field("encryptedPassword", FieldType.STRING).collectionFieldName("ep").modifiable().masked()
                ).build();


        htmlForm("signup", "Signup")
                .fields(
                        htmlFormField("firstName", "First Name", firstNameStringTypeDef).placeholder("First name..."),
                        htmlFormField("lastName", "Last Name", lastNameStringTypeDef).placeholder("Last name..."),
                        htmlFormField("emailAddress", "Email", emailAddressStringType).placeholder("Email address...").withEmailConstraint(),
                        htmlFormField("password", "Password", FieldType.STRING).inputType(HtmlInputType.password).masked().placeholder("Password...")
                ).onSuccessUrl("/signup/success")
                .build();

    }


}
