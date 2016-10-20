package com.mahanaroad.mongogen.generator.entities;

import com.mahanaroad.mongogen.BlankStringException;
import com.mahanaroad.mongogen.generator.AbstractJavaRenderer;
import com.mahanaroad.mongogen.spec.MongoGenFqcns;
import com.mahanaroad.mongogen.spec.definition.EntityDef;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public final class EntityFiltersRenderer extends AbstractJavaRenderer {


    private final EntityDef entityDef;


    public EntityFiltersRenderer(final EntityDef entityDef) {

        super(entityDef.getEntityFiltersClassDef());

        this.entityDef = entityDef;

    }


    @Override
    protected void renderStaticMethods() {

        addImportFor(Arrays.class);
        addImportFor(MongoGenFqcns.MONGO_FILTERS);

        blankLine();
        blankLine();
        appendLine("    public static %s and(final %s... filters) {", this.entityDef.getEntityFilterClassDef().getUqcn(), this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        return new IterableFunctionFilter(Arrays.asList(filters), Filters::and);");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    public static %s or(final %s... filters) {", this.entityDef.getEntityFilterClassDef().getUqcn(), this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        return new IterableFunctionFilter(Arrays.asList(filters), Filters::or);");
        blankLine();
        appendLine("    }");
        blankLine();
        blankLine();
        appendLine("    public static %s nor(final %s... filters) {", this.entityDef.getEntityFilterClassDef().getUqcn(), this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        return new IterableFunctionFilter(Arrays.asList(filters), Filters::nor);");
        blankLine();
        appendLine("    }");

        this.entityDef.getAllFieldsStream().forEach(fieldDef -> {

            addImportFor(fieldDef.getFieldType());

            blankLine();
            blankLine();
            appendLine("    public static FieldFilter<%s> %s() {", fieldDef.getFieldType().boxIfPrimitive().getUnqualifiedToString(), fieldDef.getClassFieldName());
            blankLine();
            appendLine("        return new FieldFilter<>(\"%s\");", fieldDef.getCollectionFieldName());
            blankLine();
            appendLine("    }");

        });

    }


    @Override
    protected void renderConstructor() {

        blankLine();
        blankLine();
        appendLine("    private %s() {", this.entityDef.getEntityFiltersClassDef().getUqcn());
        blankLine();
        appendLine("    }");

    }


    @Override
    protected void renderInnerStaticClasses() {

        renderFieldFilterStaticClass();
        renderSimpleSupplierFilterStaticClass();
        renderSimpleFunctionFilterStaticClass();
        renderIterableFunctionFilterStaticClass();

    }


    private void renderFieldFilterStaticClass() {

        addImportFor(MongoGenFqcns.MONGO_FILTERS);
        addImportFor(BlankStringException.class);

        blankLine();
        blankLine();
        appendLine("    public static class FieldFilter<T> {");
        blankLine();
        blankLine();
        appendLine("        private final String collectionFieldName;");
        blankLine();
        blankLine();
        appendLine("        private FieldFilter(final String collectionFieldName) {");
        blankLine();
        appendLine("            this.collectionFieldName = BlankStringException.throwIfBlank(collectionFieldName, \"collectionFieldName\");");
        blankLine();
        appendLine("        }");
        renderOperatorFilterMethod("eq");
        renderOperatorFilterMethod("gt");
        renderOperatorFilterMethod("gte");
        renderOperatorFilterMethod("lt");
        renderOperatorFilterMethod("lte");
        renderOperatorFilterMethod("ne");
        blankLine();
        blankLine();
        appendLine("        public %s exists() {", this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("            return new SimpleSupplierFilter(() -> Filters.exists(this.collectionFieldName));");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("    }");

    }


    private void renderOperatorFilterMethod(final String operator) {

        blankLine();
        blankLine();
        appendLine("        public %s %s(final T value) {", this.entityDef.getEntityFilterClassDef().getUqcn(), operator);
        blankLine();
        appendLine("            return new SimpleFunctionFilter<>(this.collectionFieldName, value, convertedValue -> Filters.%s(this.collectionFieldName, convertedValue));", operator);
        blankLine();
        appendLine("        }");

    }


    private void renderSimpleSupplierFilterStaticClass() {

        addImportFor(Supplier.class);

        blankLine();
        blankLine();
        appendLine("    private static class SimpleSupplierFilter implements %s {", this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        private final Supplier<Bson> supplier;");
        blankLine();
        blankLine();
        appendLine("        private SimpleSupplierFilter(final Supplier<Bson> supplier) {");
        blankLine();
        appendLine("            this.supplier = supplier;");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("        @Override");
        appendLine("        public Bson toBson(final %s fieldConverter) {", this.entityDef.getEntityFieldConverterClassDef().getUqcn());
        blankLine();
        appendLine("            return this.supplier.get();");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("    }");

    }


    private void renderSimpleFunctionFilterStaticClass() {

        addImportFor(MongoGenFqcns.BSON);
        addImportFor(Function.class);

        blankLine();
        blankLine();
        appendLine("    private static class SimpleFunctionFilter<VALUE> implements %s {", this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        private final String fieldName;");
        appendLine("        private final VALUE value;");
        appendLine("        private final Function<Object, Bson> bsonFunction;");
        blankLine();
        blankLine();
        appendLine("        private SimpleFunctionFilter(final String fieldName, final VALUE value, final Function<Object, Bson> bsonFunction) {");
        blankLine();
        appendLine("            this.fieldName = BlankStringException.throwIfBlank(fieldName, \"fieldName\");");
        appendLine("            this.value = value;");
        appendLine("            this.bsonFunction = bsonFunction;");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("        @Override");
        appendLine("        public Bson toBson(final %s fieldConverter) {", this.entityDef.getEntityFieldConverterClassDef().getUqcn());
        blankLine();
        appendLine("            final Object convertedValue = fieldConverter.convert(this.fieldName, this.value);");
        appendLine("            return this.bsonFunction.apply(convertedValue);");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("    }");

    }


    private void renderIterableFunctionFilterStaticClass() {

        addImportFor(List.class);
        addImportFor(Collectors.class);

        blankLine();
        blankLine();
        appendLine("    private static class IterableFunctionFilter implements %s {", this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("        private final Function<List<Bson>, Bson> bsonFunction;");
        appendLine("        private final List<%s> filters;", this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        blankLine();
        appendLine("        private IterableFunctionFilter(final List<%s> filters, final Function<List<Bson>, Bson> bsonFunction) {", this.entityDef.getEntityFilterClassDef().getUqcn());
        blankLine();
        appendLine("            this.filters = filters;");
        appendLine("            this.bsonFunction = bsonFunction;");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("        @Override");
        appendLine("        public Bson toBson(final %s fieldConverter) {", this.entityDef.getEntityFieldConverterClassDef().getUqcn());
        blankLine();
        appendLine("            final List<Bson> bsons = this.filters.stream().map(filter -> filter.toBson(fieldConverter)).collect(Collectors.toList());");
        appendLine("            return this.bsonFunction.apply(bsons);");
        blankLine();
        appendLine("        }");
        blankLine();
        blankLine();
        appendLine("    }");

    }


}
