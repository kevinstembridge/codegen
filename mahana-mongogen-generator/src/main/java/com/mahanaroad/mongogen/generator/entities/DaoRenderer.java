package com.mahanaroad.mongogen.generator.entities;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.persist.BsonCompatibleType;
import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.EntityDef;
import com.mahanaroad.mongogen.spec.definition.EntityHierarchy;
import com.mahanaroad.mongogen.spec.definition.IndexDef;
import com.mahanaroad.mongogen.spec.definition.SimpleTypeDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.ClassFieldName;
import com.mahanaroad.mongogen.spec.definition.java.EntityFieldDef;
import com.mahanaroad.mongogen.spec.definition.java.EnumType;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;
import com.mahanaroad.mongogen.spec.definition.java.NonPrimitiveType;
import com.mahanaroad.mongogen.spec.definition.java.ParameterizedType;
import com.mahanaroad.mongogen.types.CollectionName;

import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mahanaroad.mongogen.spec.definition.java.ClassFieldDef.aClassField;
import static java.util.stream.Collectors.joining;

public final class DaoRenderer extends AbstractJavaRenderer {


    private final EntityHierarchy entityHierarchy;
    private final EntityDef entityDef;


    public DaoRenderer(final EntityHierarchy entityHierarchy) {

        super(entityHierarchy.getEntityDef().getDaoClassDef());

        this.entityHierarchy = entityHierarchy;
        this.entityDef = entityHierarchy.getEntityDef();

        addConstructorArg(aClassField("mongoClientFacade", MongoGenFqcns.MONGOGEN_CLIENT_FACADE).build());

        entityHierarchy.getAllFieldDefStream().forEach(fieldDef -> {
            fieldDef.getFieldReaderClassField().ifPresent(this::addConstructorArg);
            fieldDef.getFieldWriterClassField().ifPresent(this::addConstructorArg);
        });

    }


    @Override
    protected void renderPreClassFields() {

        blankLine();
        appendLine("    private final %s fieldConverter = new %s() {", this.entityDef.getEntityFieldConverterClassDef().getUqcn(), this.entityDef.getEntityFieldConverterClassDef().getUqcn());
        blankLine();
        appendLine("        @Override");
        appendLine("        public Object convert(final String collectionFieldName, final Object inputValue) {");
        blankLine();
        appendLine("            switch(collectionFieldName) {");
        blankLine();

        this.entityDef.getAllFieldsStream().forEach(fieldDef -> {
            appendLine("                case \"%s\":", fieldDef.getCollectionFieldName());

            final Optional<ClassFieldDef> fieldWriterClassField = fieldDef.getFieldWriterClassField();

            if (fieldWriterClassField.isPresent()) {

                final ClassFieldDef writerClassField = fieldWriterClassField.get();

                if (fieldDef.isList()) {
                    appendLine("                    return ((List<%s>) inputValue).stream().map(%s::writeField);", fieldDef.getFieldType().getFirstParameterType().getUnqualifiedToString(), writerClassField.getClassFieldName());
                } else {
                    appendLine("                    return %s.writeField((%s) inputValue);", writerClassField.getClassFieldName(), fieldDef.getFieldType().unwrapIfOptional().getUnqualifiedToString());
                }

            } else {

                appendLine("                    return %s;", renderWriteConversionForImplicitField(fieldDef, "inputValue", true));

            }

        });

        appendLine("                 default:");
        appendLine("                     throw new RuntimeException(\"Unknown collectionFieldName [\" + collectionFieldName + \"]\");");
        appendLine("            }");
        blankLine();
        appendLine("        }");
        blankLine();
        appendLine("    };");

    }


    @Override
    protected void renderCallToSuperConstructor(ClassDef superclassDef) {

        addImportFor(CollectionName.class);

        blankLine();
        appendLine("        super(new CollectionName(\"%s\"), mongoClientFacade);", this.entityDef.getCollectionName());

    }


    @Override
    protected void renderMethods() {

        renderMethod_toDocumentFrom();
        renderMethod_toUpsertDocumentFrom();
        renderMethod_toEntityFrom();
        renderMethodsForUniqueFields();
        renderFindersForIndexes();
        renderExistsByForIndexes();
        renderCountWithFilter();
        renderMethod_findAllBy();
        renderMethod_findAllWithPageable();
        renderMethod_findAllByWithPageable();
        renderMethod_setFields();
        renderConvertClassFieldNameToCollectionFieldName();
        renderMethod_getTypeDiscriminator();

    }


    private void renderFindersForIndexes() {

        this.entityDef.getIndexDefStream().forEach(this::renderFinderForIndex);

    }


    private void renderExistsByForIndexes() {

        this.entityDef.getIndexDefStream().forEach(indexDef -> renderExistsByForFields(indexDef::getFieldDefStream));

    }


    private void renderFinderForIndex(final IndexDef indexDef) {

        addImportFor(List.class);

        final String methodParameters = buildMethodParametersFrom(indexDef.getFieldDefStream());
        final String fieldNamesAnded = fieldNamesAnded(indexDef.getFieldDefStream());

        blankLine();
        blankLine();
        appendLine("    public List<%s> findBy%s(%s) {", this.entityDef.getUqcn(), fieldNamesAnded, methodParameters);
        blankLine();
        appendLine("        final Document query = new Document();");

        indexDef.getFieldDefStream().forEach(fieldDef -> {

            final Optional<ClassFieldDef> fieldWriterClassField = fieldDef.getFieldWriterClassField();

            if (fieldWriterClassField.isPresent()) {

                final ClassFieldName fieldWriterClassFieldName = fieldWriterClassField.get().getClassFieldName();
                appendLine("        query.append(\"%s\", this.%s.writeField(%s));",
                        fieldDef.getCollectionFieldName(),
                        fieldWriterClassFieldName,
                        fieldDef.getClassFieldName());
            } else {

                appendLine("        query.append(\"%s\", %s);",
                        fieldDef.getCollectionFieldName(),
                        renderWriteConversionForImplicitField(fieldDef));

            }

        });

        appendLine("        return find(query);");
        blankLine();
        appendLine("    }");

    }


    private void renderCountWithFilter() {

        addImportFor(MongoGenFqcns.BSON);

        blankLine();
        blankLine();
        appendLine("    public long count(final %s filter) {", this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        final Bson bsonFilter = filter.toBson(this.fieldConverter);");
        appendLine("        return super.count(bsonFilter);");
        blankLine();
        appendLine("    }");

    }


    private void renderMethod_findAllBy() {

        addImportFor(List.class);
        addImportFor(MongoGenFqcns.BSON);

        blankLine();
        blankLine();
        appendLine("    public List<%s> findAllBy(final %s filter) {", this.entityDef.getUqcn(), this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        final Bson bsonFilter = filter.toBson(this.fieldConverter);");
        appendLine("        return super.find(bsonFilter);");
        blankLine();
        appendLine("    }");

    }


    private void renderMethod_findAllByWithPageable() {

        addImportFor(MongoGenFqcns.BSON);
        addImportFor(MongoGenFqcns.SPRING_PAGE);
        addImportFor(MongoGenFqcns.SPRING_PAGEABLE);

        blankLine();
        blankLine();
        appendLine("    public Page<%s> findAllBy(final %s filter, final Pageable pageable) {", this.entityDef.getUqcn(), this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        final Bson bsonFilter = filter.toBson(this.fieldConverter);");
        appendLine("        return super.find(bsonFilter, pageable);");
        blankLine();
        appendLine("    }");

    }


    private void renderMethod_findAllWithPageable() {

        addImportFor(MongoGenFqcns.SPRING_PAGE);
        addImportFor(MongoGenFqcns.SPRING_PAGEABLE);

        blankLine();
        blankLine();
        appendLine("    public Page<%s> findAll(final Pageable pageable) {", this.entityDef.getUqcn());
        blankLine();
        appendLine("        return super.find(pageable);");
        blankLine();
        appendLine("    }");

    }


    private void renderMethod_setFields() {

        addImportFor(MongoGenFqcns.BSON);

        blankLine();
        blankLine();
        appendLine("    public void setFields(final %s updater) {", this.entityDef.getEntityUpdaterClassDef().getUqcn());
        blankLine();
        appendLine("        final Bson bson = updater.toBson(this.fieldConverter);");
        appendLine("        super.updateOneById(updater.getId(), bson);");
        blankLine();
        appendLine("    }");

    }


    private void renderMethodsForUniqueFields() {

        renderFindersForUniqueFields();
        renderUpsertsForUniqueFields();
        renderExistsByForUniqueFields();

    }


    private void renderFindersForUniqueFields() {

        this.entityDef.getAllUniqueFieldsStream().forEach(this::renderMethod_findOneByForField);

    }


    private void renderExistsByForUniqueFields() {

        this.entityDef.getAllUniqueFieldsStream().forEach(fieldDef -> renderExistsByForFields(() -> Stream.of(fieldDef)));

    }


    private void renderUpsertsForUniqueFields() {

        this.entityDef.getAllUniqueFieldsStream().forEach(this::renderUpsertForUniqueField);

    }


    private void renderMethod_findOneByForField(final EntityFieldDef entityFieldDef) {

        blankLine();
        blankLine();
        appendLine("    public Optional<%s> findOneOptionalBy%s(final %s %s) {",
                this.entityDef.getUqcn(),
                entityFieldDef.getClassFieldName().firstToUpper(),
                entityFieldDef.getFieldType().unwrapIfOptional().getUnqualifiedToString(),
                entityFieldDef.getClassFieldName());

        blankLine();
        appendLine("        final Document query = new Document();");
        appendLine("        query.put(\"%s\", %s);", entityFieldDef.getCollectionFieldName(), renderFieldWrite(entityFieldDef));
        appendLine("        return findOneOptional(query);");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    public %s findOneBy%s(final %s %s) {",
                this.entityDef.getUqcn(),
                entityFieldDef.getClassFieldName().firstToUpper(),
                entityFieldDef.getFieldType().unwrapIfOptional().getUnqualifiedToString(),
                entityFieldDef.getClassFieldName());

        blankLine();
        appendLine("        final Document query = new Document();");
        appendLine("        query.put(\"%s\", %s);", entityFieldDef.getCollectionFieldName(), renderFieldWrite(entityFieldDef));
        appendLine("        return findOne(query);");
        blankLine();
        appendLine("    }");

    }


    private String renderFieldWrite(final EntityFieldDef entityFieldDef) {

        return renderFieldWrite(entityFieldDef, entityFieldDef.getClassFieldName().getValue());

    }


    private String renderFieldWrite(final EntityFieldDef entityFieldDef, final String fieldName) {

        final Optional<ClassFieldDef> fieldWriterClassField = entityFieldDef.getFieldWriterClassField();

        if (fieldWriterClassField.isPresent()) {

            return String.format("this.%s.writeField(%s)", fieldWriterClassField.get().getClassFieldName(), fieldName);

        } else {

            return renderWriteConversionForImplicitField(entityFieldDef, fieldName);

        }

    }


    private void renderExistsByForFields(final Supplier<Stream<EntityFieldDef>> fieldDefStream) {

        addImportFor(List.class);

        final String methodParameters = buildMethodParametersWithUnwrappedOptionalsFrom(fieldDefStream.get());
        final String fieldNamesAnded = fieldNamesAnded(fieldDefStream.get());

        blankLine();
        blankLine();
        appendLine("    public boolean existsBy%s(%s) {", fieldNamesAnded, methodParameters);
        blankLine();
        appendLine("        final Document query = new Document();");

        fieldDefStream.get().forEach(fieldDef ->
                appendLine("        query.append(\"%s\", %s);",
                        fieldDef.getCollectionFieldName(),
                        renderFieldWrite(fieldDef)));

        appendLine("        return exists(query);");
        blankLine();
        appendLine("    }");

    }


    private void renderUpsertForUniqueField(final EntityFieldDef entityFieldDef) {

        addImportFor(MongoGenFqcns.MONGO_FIND_ONE_AND_UPDATE_OPTIONS);
        addImportFor(MongoGenFqcns.MONGO_RETURN_DOCUMENT);

        blankLine();
        blankLine();
        appendLine("    public %s upsertBy%s(final %s upsertEntity) {",
                this.entityDef.getUqcn(),
                entityFieldDef.getClassFieldName().firstToUpper(),
                this.entityDef.getUqcn());

        blankLine();
        appendLine("        Objects.requireNonNull(upsertEntity, \"upsertEntity\");", entityFieldDef.getClassFieldName(), entityFieldDef.getClassFieldName());
        blankLine();
        appendLine("        final Document filter = new Document();");

        if (entityFieldDef.isOptional()) {

            appendLine("        upsertEntity.%s().ifPresent(fieldValue -> filter.put(\"%s\", %s));",
                    entityFieldDef.getGetterMethodName(),
                    entityFieldDef.getCollectionFieldName(),
                    renderFieldWrite(entityFieldDef, "fieldValue"));

        } else {

            appendLine("        filter.put(\"%s\", %s);",
                    entityFieldDef.getCollectionFieldName(),
                    renderFieldWrite(entityFieldDef, "upsertEntity." + entityFieldDef.getGetterMethodName() +"()"));

        }

        appendLine("        final Document update = toUpsertDocumentFrom(upsertEntity);");
        appendLine("        final FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(true);");
        appendLine("        return findOneAndUpdate(filter, update, options);");
        blankLine();
        appendLine("    }");

    }


    private void renderMethod_toDocumentFrom() {

        addImportFor(MongoGenFqcns.BSON_DOCUMENT);

        blankLine();
        blankLine();
        appendLine("    @Override");
        appendLine("    protected Document toDocumentFrom(final %s entity) {", this.entityDef.getUqcn());
        blankLine();

        if (this.entityHierarchy.hasSubclasses()) {

            String startOfIfClause = "if";

            final List<EntityDef> concreteEntities = this.entityHierarchy.getConcreteEntityDefStream().collect(Collectors.toList());

            for (final EntityDef entity : concreteEntities) {

                addImportFor(entity.getEntityClassDef().getNonPrimitiveType());

                appendLine("        %s (entity instanceof %s) {", startOfIfClause, entity.getUqcn());
                blankLine();
                appendLine("            return toDocumentFrom%s((%s) entity);", entity.getUqcn(), entity.getUqcn());
                blankLine();

                startOfIfClause = "} else if";

            }

            appendLine("        } else {");
            blankLine();
            appendLine("            throw new IllegalStateException(\"Unknown type in class hierarchy: \" + entity.getClass());");
            blankLine();
            appendLine("        }");

        } else {

            appendLine("        final Document document = new Document();");
            blankLine();

            if (this.entityDef.isRootEntity() == false) {
                appendLine("        document.put(\"TYP\", \"%s\");", this.entityDef.getTypeDiscriminator());
            }

            this.entityDef.getAllFieldsStream().forEach(this::writeFieldToDocument);

            blankLine();
            appendLine("        return document;");

        }

        blankLine();
        appendLine("    }");

        if (this.entityHierarchy.hasSubclasses()) {
            this.entityHierarchy.getConcreteEntityDefStream().forEach(this::renderToDocumentForEntity);
        }

    }


    private void renderMethod_toUpsertDocumentFrom() {

        addImportFor(MongoGenFqcns.BSON_DOCUMENT);

        blankLine();
        blankLine();
        appendLine("    @Override");
        appendLine("    protected Document toUpsertDocumentFrom(final %s entity) {", this.entityDef.getUqcn());
        blankLine();

        if (this.entityHierarchy.hasSubclasses()) {

            String startOfIfClause = "if";

            final List<EntityDef> concreteEntities = this.entityHierarchy.getConcreteEntityDefStream().collect(Collectors.toList());

            for (final EntityDef entity : concreteEntities) {

                addImportFor(entity.getEntityClassDef().getNonPrimitiveType());

                appendLine("        %s (entity instanceof %s) {", startOfIfClause, entity.getUqcn());
                blankLine();
                appendLine("            return toUpsertDocumentFrom%s((%s) entity);", entity.getUqcn(), entity.getUqcn());
                blankLine();

                startOfIfClause = "} else if";

            }

            appendLine("        } else {");
            blankLine();
            appendLine("            throw new IllegalStateException(\"Unknown type in class hierarchy: \" + entity.getClass());");
            blankLine();
            appendLine("        }");

        } else {

            appendLine("        final Document modifiableFieldsDocument = new Document();");
            appendLine("        final Document unmodifiableFieldsDocument = new Document();");
            blankLine();

            if (this.entityDef.isRootEntity() == false) {
                appendLine("        unmodifiableFieldsDocument.put(\"TYP\", \"%s\");", this.entityDef.getTypeDiscriminator());
            }

            this.entityDef.getAllModifiableFieldDefStream().forEach(fieldDef -> writeFieldToDocument(fieldDef, "modifiableFieldsDocument"));
            this.entityDef.getAllUnmodifiableFieldDefStream().forEach(fieldDef -> writeFieldToDocument(fieldDef, "unmodifiableFieldsDocument"));

            blankLine();
            appendLine("        return new Document()");
            appendLine("                .append(\"$setOnInsert\", unmodifiableFieldsDocument)");
            appendLine("                .append(\"$set\", modifiableFieldsDocument);");

        }

        blankLine();
        appendLine("    }");

        if (this.entityHierarchy.hasSubclasses()) {
            this.entityHierarchy.getConcreteEntityDefStream().forEach(this::renderToUpsertDocumentForEntity);
        }

    }


    private void renderToDocumentForEntity(final EntityDef entity) {

        blankLine();
        blankLine();
        appendLine("    private Document toDocumentFrom%s(final %s entity) {", entity.getUqcn(), entity.getUqcn());
        blankLine();
        appendLine("        final Document document = new Document();");
        blankLine();
        appendLine("        document.put(\"TYP\", \"%s\");", entity.getTypeDiscriminator());

        entity.getAllFieldsStream().forEach(this::writeFieldToDocument);

        blankLine();
        appendLine("        return document;");
        blankLine();
        appendLine("    }");



    }


    private void renderToUpsertDocumentForEntity(final EntityDef entity) {

        blankLine();
        blankLine();
        appendLine("    private Document toUpsertDocumentFrom%s(final %s entity) {", entity.getUqcn(), entity.getUqcn());
        blankLine();
        appendLine("        final Document modifiableFieldsDocument = new Document();");
        appendLine("        final Document unmodifiableFieldsDocument = new Document();");
        blankLine();
        appendLine("        unmodifiableFieldsDocument.put(\"TYP\", \"%s\");", entity.getTypeDiscriminator());

        entity.getAllModifiableFieldDefStream().forEach(fieldDef -> writeFieldToDocument(fieldDef, "modifiableFieldsDocument"));
        entity.getAllUnmodifiableFieldDefStream().forEach(fieldDef -> writeFieldToDocument(fieldDef, "unmodifiableFieldsDocument"));

        blankLine();
        appendLine("        return new Document()");
        appendLine("                .append(\"$setOnInsert\", unmodifiableFieldsDocument)");
        appendLine("                .append(\"$set\", modifiableFieldsDocument);");
        blankLine();
        appendLine("    }");



    }


    private void writeFieldToDocument(final EntityFieldDef entityFieldDef) {

        writeFieldToDocument(entityFieldDef, "document");

    }


    private void writeFieldToDocument(final EntityFieldDef entityFieldDef, final String nameOfDocument) {

        if (entityFieldDef.isOptional()) {

            appendLine("        entity.%s().ifPresent(%s -> %s.put(\"%s\", %s));",
                    entityFieldDef.getGetterMethodName(),
                    entityFieldDef.getClassFieldName(),
                    nameOfDocument,
                    entityFieldDef.getCollectionFieldName(),
                    renderFieldWrite(entityFieldDef));

        } else {

            if (entityFieldDef.isList()) {

                addImportFor(Collectors.class);

                appendLine("        %s.put(\"%s\", %s);",
                        nameOfDocument,
                        entityFieldDef.getCollectionFieldName(),
                        renderFieldWrite(entityFieldDef, String.format("entity.%s()", entityFieldDef.getGetterMethodName())));

//                appendLine("        %s.put(\"%s\", entity.%s().stream().map(v -> %s).collect(Collectors.toList()));",
//                        nameOfDocument,
//                        entityFieldDef.getCollectionFieldName(),
//                        entityFieldDef.getGetterMethodName(),
//                        renderFieldWrite(entityFieldDef, "v"));

            } else {

                appendLine("        %s.put(\"%s\", %s);",
                        nameOfDocument,
                        entityFieldDef.getCollectionFieldName(),
                        renderFieldWrite(entityFieldDef, String.format("entity.%s()", entityFieldDef.getGetterMethodName())));

            }

        }

    }


    private void renderMethod_toEntityFrom() {

        addImportFor(MongoGenFqcns.BSON_DOCUMENT);

        blankLine();
        blankLine();
        appendLine("    @Override");
        appendLine("    protected %s toEntityFrom(final Document document) {", this.entityDef.getUqcn());
        blankLine();

        if (this.entityHierarchy.hasSubclasses()) {

            appendLine("        final String typeDiscriminator = readString(\"TYP\", \"typeDiscriminator\", document);");
            blankLine();
            appendLine("        switch (typeDiscriminator) {");

            this.entityHierarchy.getConcreteEntityDefStream().forEach(entity -> {

                addImportFor(entity.getEntityClassDef().getNonPrimitiveType());

                blankLine();
                appendLine("            case \"%s\":", entity.getTypeDiscriminatorOptional().orElseThrow(() -> new RuntimeException("Expected entity to have a type discriminator: " + entity.getEntityKey())));
                appendLine("                return %sFrom(document);", entity.getUqcn().firstToLower());

            });

            appendLine("            default:");
            appendLine("                throw new RuntimeException(\"A record exists with id \" + document.get(\"_id\") + \" but it has an unknown type discriminator: \" + typeDiscriminator);");
            appendLine("        }");
            blankLine();
            appendLine("    }");

            this.entityHierarchy.getConcreteEntityDefStream().forEach(this::renderMethod_entityFrom);

        } else {

            this.entityDef.getAllFieldsStream().forEach(this::renderReadField);

            renderCallToEntityConstructor(this.entityDef, "        ");
            blankLine();
            appendLine("    }");

        }

    }


    private void renderMethod_entityFrom(final EntityDef entity) {

        addImportFor(entity.getEntityClassDef().getNonPrimitiveType());

        blankLine();
        blankLine();
        appendLine("    private %s %sFrom(Document document) {", entity.getUqcn(), entity.getUqcn().firstToLower());
        blankLine();

        entity.getAllFieldsStream().forEach(this::renderReadField);

        renderCallToEntityConstructor(entity, "        ");
        blankLine();
        appendLine("    }");

    }


    private void renderReadField(final EntityFieldDef entityFieldDef) {

        addImportFor(entityFieldDef.getFieldType());

        final Optional<ClassFieldDef> fieldReaderClassFieldOptional = entityFieldDef.getFieldReaderClassField();

        if (fieldReaderClassFieldOptional.isPresent()) {

            final ClassFieldDef fieldReaderClassField = fieldReaderClassFieldOptional.get();

            if (entityFieldDef.isOptional()) {

                appendLine("        final %s %s = this.%s.readField(\"%s\", document);",
                        entityFieldDef.getFieldType().getUnqualifiedToString(),
                        entityFieldDef.getClassFieldName(),
                        fieldReaderClassField.getClassFieldName(),
                        entityFieldDef.getCollectionFieldName());

            } else {

                appendLine("        final %s %s = this.%s.readField(\"%s\", \"%s\", document, getCollectionName());",
                        entityFieldDef.getFieldType().getUnqualifiedToString(),
                        entityFieldDef.getClassFieldName(),
                        fieldReaderClassField.getClassFieldName(),
                        entityFieldDef.getCollectionFieldName(),
                        entityFieldDef.getClassFieldName());

            }

        } else {

            if (entityFieldDef.isOptionalEnum()) {

                appendLine("        final %s %s = readEnumOptional(%s.class, \"%s\", \"%s\", document);",
                        entityFieldDef.getFieldType().getUnqualifiedToString(),
                        entityFieldDef.getClassFieldName(),
                        entityFieldDef.getFieldType().getFirstParameterType().getUnqualifiedToString(),
                        entityFieldDef.getCollectionFieldName(),
                        entityFieldDef.getClassFieldName());

            } else if (entityFieldDef.isEnumList()) {

                appendLine("        final %s %s = readEnumList(%s.class, \"%s\", \"%s\", document);",
                        entityFieldDef.getFieldType().getUnqualifiedToString(),
                        entityFieldDef.getClassFieldName(),
                        entityFieldDef.getFieldType().getFirstParameterType().getUnqualifiedToString(),
                        entityFieldDef.getCollectionFieldName(),
                        entityFieldDef.getClassFieldName());

            } else if (entityFieldDef.isOptional()) {

                appendLine("        final %s %s = %sOptional(%s\"%s\", document);",
                        entityFieldDef.getFieldType().getUnqualifiedToString(),
                        entityFieldDef.getClassFieldName(),
                        getImplicitReadFieldMethodName(entityFieldDef),
                        getReadFieldMapper(entityFieldDef),
                        entityFieldDef.getCollectionFieldName());

            } else if (entityFieldDef.isMap()) {

                appendLine("        final %s %s = readMap(%s, %s, \"%s\", \"%s\", document);",
                        entityFieldDef.getFieldType().getUnqualifiedToString(),
                        entityFieldDef.getClassFieldName(),
                        renderMapKeyMapper(entityFieldDef),
                        renderMapValueMapper(entityFieldDef),
                        entityFieldDef.getCollectionFieldName(),
                        entityFieldDef.getClassFieldName());

            } else {

                appendLine("        final %s %s = %s(%s\"%s\", \"%s\", document);",
                        entityFieldDef.getFieldType().getUnqualifiedToString(),
                        entityFieldDef.getClassFieldName(),
                        getImplicitReadFieldMethodName(entityFieldDef),
                        getReadFieldMapper(entityFieldDef),
                        entityFieldDef.getCollectionFieldName(),
                        entityFieldDef.getClassFieldName());

            }

        }

    }


    private String getReadFieldMapper(final EntityFieldDef entityFieldDef) {

        final FieldType fieldType = entityFieldDef.getFieldType();

        if (fieldType.isList()) {

            final NonPrimitiveType firstParameterType = fieldType.getFirstParameterType();

            if (firstParameterType.isSimpleTypeWrapper()) {

                return firstParameterType.getUnqualifiedToString() + "::new, ";

            }

        }

        if (entityFieldDef.isOptional()) {

            if (fieldType.isSimpleTypeWrapper()) {
                return fieldType.getFirstParameterType().getUnqualifiedToString() + "::new, ";
            }

            if (fieldType.isEnum()) {
                return fieldType.getFirstParameterType().getUnqualifiedToString() + ".class, ";
            }

        }

        if (fieldType.isSimpleTypeWrapper()) {
            return fieldType.getUnqualifiedToString() + "::new, ";
        }

        if (fieldType.isEnum()) {
            return fieldType.getUnqualifiedToString() + ".class, ";
        }

        return "";

    }


    private String getImplicitReadFieldMethodName(final EntityFieldDef entityFieldDef) {

        final String listSuffix = entityFieldDef.isList() ? "List" : "";
        final String mapSuffix = entityFieldDef.isMap() ? "Map" : "";
        final String collectionSuffix = listSuffix + mapSuffix;
        return getImplicitReadFieldMethodNamePrefix(entityFieldDef) + collectionSuffix;

    }


    private String getImplicitReadFieldMethodNamePrefix(final EntityFieldDef entityFieldDef) {

        final FieldType fieldType = entityFieldDef.getFieldType();

        if (fieldType.isEnum()) {
            return "readEnum";
        }

        if (fieldType.isListOfSimpleTypeWrappers()) {
            return "read";
        }

        final BsonCompatibleType bsonCompatibleType = fieldType.getBsonCompatibleType().orElseThrow(() -> new IllegalArgumentException("An implicit field type is expected to have a BsonType: " + entityFieldDef));

        switch (bsonCompatibleType) {
            case BOOLEAN:
                return "readBoolean";
            case DOUBLE:
                return "readDouble";
            case INSTANT:
                return "readInstant";
            case INT:
                return "readInt";
            case INTEGER:
                return "readInteger";
            case LOCAL_DATE:
                return "readLocalDate";
            case LONG:
                return "readLong";
            case OBJECT_ID:
                return "readObjectId";
            case PERIOD:
                return "readPeriod";
            case STRING:
                return "readString";
            default:
                throw new RuntimeException("Unable to find implicit field reader method for " + entityFieldDef);
        }

    }


    private void renderCallToEntityConstructor(final EntityDef entity, final String indent) {

        blankLine();
        append(indent + "return new %s(", entity.getUqcn());

        final String constructorArgs = entity
                .getAllFieldsStream()
                .map(fieldDef -> "\n        " + indent + fieldDef.getClassFieldName())
                .collect(joining(","));

        append(constructorArgs);
        append(");");
        newLine();

    }


    private void renderConvertClassFieldNameToCollectionFieldName() {

        blankLine();
        blankLine();
        appendLine("    protected final String convertClassFieldNameToCollectionFieldName(final String classFieldName) {");
        blankLine();
        appendLine("        switch(classFieldName) {");

        this.entityDef.getAllFieldsStream().forEach(fieldDef -> {
                appendLine("            case \"%s\":", fieldDef.getClassFieldName());
                appendLine("                return \"%s\";", fieldDef.getCollectionFieldName());
            }
        );

        appendLine("            default:");
        appendLine("                throw new IllegalArgumentException(\"Unknown classFieldName [\" + classFieldName + \"]\");");
        appendLine("        }");
        blankLine();
        appendLine("    }");

    }


    private void renderMethod_getTypeDiscriminator() {

        addImportFor(Optional.class);

        blankLine();
        blankLine();
        appendLine("    protected final Optional<String> getTypeDiscriminator() {");
        blankLine();

        if (this.entityDef.getTypeDiscriminatorOptional().isPresent()) {
            appendLine("        return Optional.of(\"%s\");", this.entityDef.getTypeDiscriminator());
        } else {
            appendLine("        return Optional.empty();");
        }

        blankLine();
        appendLine("    }");

    }


    private String renderWriteConversionForImplicitField(final EntityFieldDef entityFieldDef) {

        return renderWriteConversionForImplicitField(entityFieldDef, entityFieldDef.getClassFieldName().getValue());

    }


    private String renderWriteConversionForImplicitField(final EntityFieldDef entityFieldDef, final String fieldName) {

        return renderWriteConversionForImplicitField(entityFieldDef, fieldName, false);

    }


    private String renderWriteConversionForImplicitField(final EntityFieldDef entityFieldDef, final String fieldName, final boolean requiresCast) {

        final FieldType fieldType = entityFieldDef.getFieldType().unwrapIfOptional();
        final String castPrefix = requiresCast ? String.format("(%s) %s", fieldType.getUnqualifiedToString(), fieldName) : fieldName;
        final String fullCastPrefix = requiresCast ? String.format("((%s) %s)", fieldType.getUnqualifiedToString(), fieldName) : fieldName;

        if (fieldType.isList()) {

            final NonPrimitiveType listElementNonPrimitiveType = fieldType.getFirstParameterType();

            if (listElementNonPrimitiveType instanceof ParameterizedType) {

                final ParameterizedType listElementParameterizedType = (ParameterizedType) listElementNonPrimitiveType;


                if (listElementParameterizedType.isSimpleTypeWrapper()) {
                    return String.format("%s.stream().map(%s::getValue).collect(Collectors.toList())", fullCastPrefix, listElementParameterizedType.getUnqualifiedToString());
                }

                if (listElementParameterizedType.isInstant()) {
                    return String.format("%s.stream().map(Date::from).collect(Collectors.toList())", fullCastPrefix);
                }

                if (listElementParameterizedType.isLocalDate()) {

                    addImportFor(ZoneId.class);
                    return String.format("%s.stream().map(ld -> Date.from(ld.atStartOfDay(ZoneId.of(\"UTC\")).toInstant())).collect(Collectors.toList())", fullCastPrefix);

                }

                if (listElementParameterizedType.isPeriod()) {

                    addImportFor(Period.class);
                    return String.format("%s.stream().map(Period::toString).collect(Collectors.toList())", fullCastPrefix);

                }

            } else if (listElementNonPrimitiveType instanceof EnumType) {

                final EnumType listElementEnumType = (EnumType) listElementNonPrimitiveType;
                return String.format("%s.stream().map(%s::name).collect(Collectors.toList())", fullCastPrefix, listElementEnumType.getUnqualifiedToString());

            }

            return fieldName;


        } else {

            if (fieldType.isSimpleTypeWrapper()) {

                return String.format("%s.getValue()", fullCastPrefix);

            }

            if (fieldType.isEnum()) {

                return String.format("%s.name()", fullCastPrefix);

            }

            if (fieldType.isInstant()) {

                addImportFor(Date.class);
                return "Date.from(" + castPrefix + ")";

            }

            if (fieldType.isLocalDate()) {

                addImportFor(ZoneId.class);
                return "Date.from(" + fullCastPrefix + ".atStartOfDay(ZoneId.of(\"UTC\")).toInstant())";

            }

            if (fieldType.isPeriod()) {

                return fieldName + ".toString()";

            }

            return fieldName;

        }

    }


    private String renderMapKeyMapper(final EntityFieldDef entityFieldDef) {

        final FieldType mapFieldType = entityFieldDef.getFieldType();
        final NonPrimitiveType keyType = mapFieldType.getFirstParameterType();

        if (keyType instanceof ParameterizedType) {

            final ParameterizedType keyParameterizedType = (ParameterizedType) keyType;

            if (keyParameterizedType.isSimpleTypeWrapper()) {

                return String.format("%s::new", keyType.getUnqualifiedToString());

            }

        } else if (keyType instanceof EnumType) {

            return String.format("key -> %s.valueOf(key)", keyType.getUnqualifiedToString());

        }

        return "key -> key";

    }


    private String renderMapValueMapper(final EntityFieldDef entityFieldDef) {

        final FieldType mapFieldType = entityFieldDef.getFieldType();
        final NonPrimitiveType valueType = mapFieldType.getSecondParameterType();
        final Optional<SimpleTypeDef> simpleTypeDefOptional = entityFieldDef.getSimpleTypeDef();

        if (simpleTypeDefOptional.isPresent()) {

            final SimpleTypeDef simpleTypeDef = simpleTypeDefOptional.get();
            return String.format("value -> new %s((%s) value)", valueType.getUnqualifiedToString(), simpleTypeDef.getSuperTypeFieldType().getUnqualifiedToString());

        }

        return String.format("value -> (%s) value", valueType.getUnqualifiedToString());

    }


}
