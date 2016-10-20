package com.mahanaroad.mongogen.generator.entities;

import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.EntityDef;
import com.mahanaroad.mongogen.spec.definition.java.FieldType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EntityUpdaterRenderer extends AbstractJavaRenderer {


    private final EntityDef entityDef;


    public EntityUpdaterRenderer(final EntityDef entityDef) {

        super(entityDef.getEntityUpdaterClassDef());
        this.entityDef = entityDef;

    }


    @Override
    protected void renderPreClassFields() {

        addImportFor(FieldType.OBJECT_ID);
        addImportFor(Map.class);
        addImportFor(HashMap.class);

        blankLine();
        blankLine();
        appendLine("    private final ObjectId id;");
        appendLine("    private final Map<String, Object> fields = new HashMap<>();");

    }


    @Override
    protected void renderConstructor() {

        addImportFor(FieldType.OBJECT_ID);
        addImportFor(Objects.class);
        addImportFor(Map.class);

        blankLine();
        blankLine();
        appendLine("    private %s(final ObjectId id, final Map<String, Object> fields) {", getClassDef().getUqcn());
        blankLine();
        appendLine("        this.id = Objects.requireNonNull(id, \"id\");");
        appendLine("        this.fields.putAll(fields);");
        blankLine();
        appendLine("    }");

    }


    @Override
    protected void renderMethods() {

        renderGetIdMethod();
        renderToBsonMethod();
        renderForIdMethod();

    }


    private void renderGetIdMethod() {

        blankLine();
        blankLine();
        appendLine("    public ObjectId getId() {");
        blankLine();
        appendLine("        return this.id;");
        blankLine();
        appendLine("    }");

    }

    /*


    public Bson toBson(final SimpleEntityFieldConverter fieldConverter) {

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

     */

    private void renderToBsonMethod() {

        addImportFor(MongoGenFqcns.BSON);
        addImportFor(MongoGenFqcns.BSON_DOCUMENT);
        addImportFor(Optional.class);

        blankLine();
        blankLine();
        appendLine("    public Bson toBson(final %s fieldConverter) {", this.entityDef.getEntityFieldConverterClassDef().getUqcn());
        blankLine();
        appendLine("        final Document setFieldsDocument = new Document();");
        appendLine("        final Document unsetFieldsDocument = new Document();");
        blankLine();
        appendLine("        this.fields.forEach((collectionFieldName, fieldValue) -> {");
        blankLine();
        appendLine("            if (fieldValue instanceof Optional) {");
        blankLine();
        appendLine("                final Optional fieldValueOptional = (Optional) fieldValue;");
        blankLine();
        appendLine("                if (fieldValueOptional.isPresent()) {");
        appendLine("                    setFieldsDocument.put(collectionFieldName, fieldConverter.convert(collectionFieldName, fieldValueOptional.get()));");
        appendLine("                } else {");
        appendLine("                    unsetFieldsDocument.put(collectionFieldName, \"\");");
        appendLine("                }");
        blankLine();
        appendLine("            } else {");
        blankLine();
        appendLine("                setFieldsDocument.put(collectionFieldName, fieldConverter.convert(collectionFieldName, fieldValue));");
        appendLine("            }");
        blankLine();
        appendLine("        });");
        blankLine();
        appendLine("        final Document document = new Document();");
        blankLine();
        appendLine("        if (setFieldsDocument.isEmpty() == false) {");
        appendLine("            document.put(\"$set\", setFieldsDocument);");
        appendLine("        }");
        blankLine();
        appendLine("        if (unsetFieldsDocument.isEmpty() == false) {");
        appendLine("            document.put(\"$unset\", unsetFieldsDocument);");
        appendLine("        }");
        blankLine();
        appendLine("        return document;");
        blankLine();
        appendLine("    }");

    }


    private void renderForIdMethod() {

        blankLine();
        blankLine();
        appendLine("    public static Builder forId(final ObjectId id) {");
        blankLine();
        appendLine("        return new Builder(id);");
        blankLine();
        appendLine("    }");

    }


    @Override
    protected void renderInnerStaticClasses() {

        blankLine();
        blankLine();
        appendLine("    public static class Builder {");
        blankLine();
        blankLine();
        appendLine("        private final ObjectId id;");
        appendLine("        private final Map<String, Object> fields = new HashMap<>();");
        blankLine();
        appendLine("        private Builder(final ObjectId id) {");
        blankLine();
        appendLine("            this.id = Objects.requireNonNull(id, \"id\");");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("        public %s build() {", getClassDef().getUqcn());
        blankLine();
        appendLine("            return new %s(this.id, this.fields);", getClassDef().getUqcn());
        blankLine();
        appendLine("        }");

        this.entityDef.getAllModifiableFieldDefStream().forEach(fieldDef -> {

            addImportFor(fieldDef.getFieldType());

            blankLine();
            blankLine();
            appendLine("        public Builder %s(final %s %s) {", fieldDef.getSetterMethodName(), fieldDef.getFieldType().getUnqualifiedToString(), fieldDef.getClassFieldName());
            blankLine();
            appendLine("            this.fields.put(\"%s\", %s);", fieldDef.getCollectionFieldName(), fieldDef.getClassFieldName());
            appendLine("            return this;");
            blankLine();
            appendLine("        }");

        });

        blankLine();
        blankLine();
        appendLine("    }");

    }


}
