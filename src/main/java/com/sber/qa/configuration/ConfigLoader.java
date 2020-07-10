package com.sber.qa.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class ConfigLoader {

    private static final Config defaultConfig = ConfigFactory.parseResources(Constants.CONFIG_PATH);
    private static final Config systemProperties = ConfigFactory.systemProperties();


    public static String getProperty(String propertyName){
        return defaultConfig.getString(propertyName);
    }

    public static String getEnvProperty(String propertyName){
        String propPath = getEnvBasePath() + "." + propertyName;
        return defaultConfig.getString(propPath);
    }

    private String getEnvBasePath(){
        if(systemProperties.hasPath(Constants.ENV_SYSTEM_PROPERTY)){
            return Constants.ENV_ROOT + "." + systemProperties.getString(Constants.ENV_SYSTEM_PROPERTY);
        }
        return Constants.ENV_ROOT + "." + Constants.DEFAULT_ENV;
    }
}
