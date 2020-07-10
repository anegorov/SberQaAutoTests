package com.sber.qa.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.qa.converter.ValueConverter;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.datatable.TableEntryByTypeTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
public class Transformer implements ParameterByTypeTransformer, TableEntryByTypeTransformer, TableCellByTypeTransformer {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final ValueConverter vc;

    @Override
    public Object transform(String s, Type type) {
        s = vc.value(s);
        return objectMapper.convertValue(s, objectMapper.constructType(type));
    }

    @Override
    public <T> T transform(Map<String, String> map, Class<T> aClass, TableCellByTypeTransformer tableCellByTypeTransformer) {
        Map<String, String> convertedMap = convertMap(map);
        return objectMapper.convertValue(convertedMap, aClass);
    }

    @Override
    public <T> T transform(String s, Class<T> aClass) {
        s = vc.value(s);
        return objectMapper.convertValue(s, aClass);
    }

    private Map<String, String> convertMap(Map<String, String> map) {
        Set<String> keys = map.keySet();
        Map<String, String> outputMap = new Hashtable<>();
        for (String key : keys) {
            String objectValue = vc.value(map.get(key));
            outputMap.put(key, objectValue);
        }
        return outputMap;
    }

}
