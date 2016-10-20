package com.mahanaroad.mongogen.sample.fieldconverters;

import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class FieldConverterTestFieldTypeLevelFieldWriter {


    private String value = UUID.randomUUID().toString();


    public void setNextValue(final String value) {

        this.value = value;

    }


    public String writeField(final Object inputValue) {

        return inputValue + this.value;

    }



}
