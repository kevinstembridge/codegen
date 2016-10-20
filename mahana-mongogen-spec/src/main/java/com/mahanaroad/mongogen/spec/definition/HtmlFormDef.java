package com.mahanaroad.mongogen.spec.definition;


import com.mahanaroad.mongogen.spec.MongoGenConstants;
import com.mahanaroad.mongogen.spec.definition.java.ClassDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.Fqcn;
import com.mahanaroad.mongogen.spec.definition.java.HtmlFormFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.PackageName;
import com.mahanaroad.mongogen.spec.definition.java.Uqcn;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.mahanaroad.mongogen.spec.definition.builders.ClassDefBuilder.aClassDef;


public final class HtmlFormDef {


    public static HtmlFormDef newInstance(
            final HtmlFormKey htmlFormKey,
            final HtmlFormName htmlFormName,
            final PackageName basePackage,
            final PackageName subPackage,
            final List<HtmlFormFieldDef> fieldDefs,
            final Optional<String> onSuccessUrl) {

        return new HtmlFormDef(
                htmlFormKey,
                htmlFormName,
                basePackage,
                subPackage,
                fieldDefs,
                onSuccessUrl);

    }

    private final HtmlFormKey htmlFormKey;
    private final HtmlFormName htmlFormName;
    private final ClassDef dtoClassDef;
    private final Optional<String> onSuccessUrl;
    private final List<HtmlFormFieldDef> htmlFormFieldDefsNotInherited = new LinkedList<>();

    private HtmlFormDef(
            final HtmlFormKey htmlFormKey,
            final HtmlFormName htmlFormName,
            final PackageName basePackage,
            final PackageName subPackage,
            final List<HtmlFormFieldDef> htmlFormFieldDefsNotInherited,
            final Optional<String> onSuccessUrl) {

        Objects.requireNonNull(htmlFormKey, "htmlFormKey");
        Objects.requireNonNull(htmlFormName, "htmlFormName");
        Objects.requireNonNull(basePackage, "basePackage");
        Objects.requireNonNull(subPackage, "subPackage");
        Objects.requireNonNull(htmlFormFieldDefsNotInherited, "htmlFormFieldDefsNotInherited");
        Objects.requireNonNull(onSuccessUrl, "onSuccessUrl");

        this.htmlFormKey = htmlFormKey;
        this.htmlFormName = htmlFormName;
        this.onSuccessUrl = onSuccessUrl;
        this.htmlFormFieldDefsNotInherited.addAll(htmlFormFieldDefsNotInherited);

        final Uqcn dtoUqcn = new Uqcn(this.htmlFormName + "FormDto");

        final PackageName packageName = basePackage.plusSubPackage(subPackage);
        final Fqcn dtoFqcn = packageName.uqcn(dtoUqcn);

        this.dtoClassDef = aClassDef(dtoFqcn)
                .withConstructorAnnotation(MongoGenConstants.JSON_CREATOR_ANNOTATION)
                .withFieldDefsNotInherited(htmlFormFieldDefsNotInherited)
                .build();

    }


    public ClassDef getDtoClassDef() {

        return this.dtoClassDef;

    }


    public Stream<HtmlFormFieldDef> getAllHtmlFormFields() {

        return this.htmlFormFieldDefsNotInherited.stream();

    }


    public Stream<ClassFieldDef> getFieldsNotInheritedStream() {

        return this.dtoClassDef.getFieldsNotInheritedStream();

    }


    public Uqcn getUqcn() {

        return this.dtoClassDef.getUqcn();

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final HtmlFormDef entityDef = (HtmlFormDef) o;
        return Objects.equals(htmlFormKey, entityDef.htmlFormKey);

    }


    @Override
    public int hashCode() {

        return Objects.hash(htmlFormKey);

    }


    @Override
    public String toString() {

        return "HtmlFormDef{" + this.htmlFormKey + "}";

    }


    public HtmlFormKey getHtmlFormKey() {

        return this.htmlFormKey;

    }


    public HtmlFormName getHtmlFormName() {

        return this.htmlFormName;

    }


    public String getFormSubmissionPath() {

        return this.htmlFormName.getValue().toLowerCase();

    }


    public Optional<String> getOnSuccessUrl() {

        return this.onSuccessUrl;

    }


}