package com.mahanaroad.mongogen.persist;


import com.mahanaroad.mongogen.types.CollectionName;
import org.bson.Document;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MapFieldReader<KEY, SOURCE_VALUE, TARGET_VALUE> {


    private final Function<String, KEY> keyMapper;
    private final Function<SOURCE_VALUE, TARGET_VALUE> valueMapper;


    public MapFieldReader(Function<String, KEY> keyMapper, Function<SOURCE_VALUE, TARGET_VALUE> valueMapper) {

        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;

    }


    public Map<KEY, TARGET_VALUE> readField(final String collectionFieldName, final String classFieldName, final Document document, final CollectionName collectionName) {

        @SuppressWarnings("unchecked")
        final Map<String, SOURCE_VALUE> rawMap = (Map<String, SOURCE_VALUE>) document.get(collectionFieldName);

        if (rawMap == null) {

            return Collections.emptyMap();

        } else {

            // TODO catch exception and wrap it using collectionFieldName, classfieldName, collectionName.
            return rawMap
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(o -> this.keyMapper.apply(o.getKey()), o -> this.valueMapper.apply(o.getValue())));

        }

    }


}
