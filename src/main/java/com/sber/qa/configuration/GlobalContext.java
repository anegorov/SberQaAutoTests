package com.sber.qa.configuration;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Primary
@Component
@Scope(scopeName = SCOPE_SINGLETON)
@Slf4j
public class GlobalContext {

    private Map<String, String> objectMap = Maps.newHashMap();

    public void put(String key, String value) {
        String  res = objectMap.put(key, value);
        log.info("Test data '{}' - '{}' was added to context", key, value);
    }

    public String get(String key){
        return objectMap.get(key);
    }

    public Boolean isExist(String key){
        return objectMap.containsKey(key);
    }

    public void clearAll() {
        objectMap.clear();
    }
}
