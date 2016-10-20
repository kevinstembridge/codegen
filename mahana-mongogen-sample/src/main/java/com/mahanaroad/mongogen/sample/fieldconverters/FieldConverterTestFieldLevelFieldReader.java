package com.mahanaroad.mongogen.sample.fieldconverters;

import com.mahanaroad.mongogen.types.CollectionName;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class FieldConverterTestFieldLevelFieldReader {


    private String value = UUID.randomUUID().toString();


    public void setNextValue(final String value) {

        this.value = value;

    }


    public String readField(final String collectionFieldName, final String classFieldName, final Document document, final CollectionName collectionName) {

        final String string = document.getString(collectionFieldName);
        return string + this.value;

    }



}
