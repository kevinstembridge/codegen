// This source was generated by the Mahana Mongogen generator
// Renderer class: class com.mahanaroad.mongogen.generator.entities.EntityUpdaterRenderer
// Rendered at: 2016-09-30T22:30:59.771Z

package com.mahanaroad.mongogen.sample.user;

import com.mahanaroad.mongogen.sample.types.FirstName;
import com.mahanaroad.mongogen.sample.types.LastName;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


public class UserEntityUpdater {


    private final ObjectId id;
    private final Map<String, Object> fields = new HashMap<>();



    private UserEntityUpdater(final ObjectId id, final Map<String, Object> fields) {

        this.id = Objects.requireNonNull(id, "id");
        this.fields.putAll(fields);

    }


    public ObjectId getId() {

        return this.id;

    }


    public Bson toBson(final UserEntityFieldConverter fieldConverter) {

        final Document setFieldsDocument = new Document();
        final Document unsetFieldsDocument = new Document();

        this.fields.forEach((collectionFieldName, fieldValue) -> {

            if (fieldValue instanceof Optional) {

                final Optional fieldValueOptional = (Optional) fieldValue;

                if (fieldValueOptional.isPresent()) {
                    setFieldsDocument.put(collectionFieldName, fieldConverter.convert(collectionFieldName, fieldValueOptional.get()));
                } else {
                    unsetFieldsDocument.put(collectionFieldName, "");
                }

            } else {

                setFieldsDocument.put(collectionFieldName, fieldConverter.convert(collectionFieldName, fieldValue));
            }

        });

        final Document document = new Document();

        if (setFieldsDocument.isEmpty() == false) {
            document.put("$set", setFieldsDocument);
        }

        if (unsetFieldsDocument.isEmpty() == false) {
            document.put("$unset", unsetFieldsDocument);
        }

        return document;

    }


    public static Builder forId(final ObjectId id) {

        return new Builder(id);

    }


    public static class Builder {


        private final ObjectId id;
        private final Map<String, Object> fields = new HashMap<>();

        private Builder(final ObjectId id) {

            this.id = Objects.requireNonNull(id, "id");

        }


        public UserEntityUpdater build() {

            return new UserEntityUpdater(this.id, this.fields);

        }


        public Builder setEncryptedPassword(final String encryptedPassword) {

            this.fields.put("ep", encryptedPassword);
            return this;

        }


        public Builder setFirstName(final FirstName firstName) {

            this.fields.put("fn", firstName);
            return this;

        }


        public Builder setLastName(final LastName lastName) {

            this.fields.put("ln", lastName);
            return this;

        }


    }


}
