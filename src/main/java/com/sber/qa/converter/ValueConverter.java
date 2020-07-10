package com.sber.qa.converter;

import com.sber.qa.configuration.ConfigLoader;
import com.sber.qa.configuration.CucumberTestContext;
import com.sber.qa.configuration.GlobalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class ValueConverter {

    private final GlobalContext store;

    private CucumberTestContext CONTEXT = CucumberTestContext.CONTEXT;

    private String str;

    public String value(String str){
        String total;
        total = convert(str);
        total = config(total);
        return total;
    }

    public String getConverted(){
        return convert(str);
    }

    protected CucumberTestContext testContext() {
        return CONTEXT;
    }

    private String config(String str){
        if(str.contains("{config{")){
            str = str.replace("{config{", "");
            str = str.replace("}}", "");
            return ConfigLoader.getEnvProperty(str);
        }
        return str;
    }

    private String convert(String str){
        if(str.contains("{{") && (str.contains("{{"))){
            str = str.replace("'","");
            int len = str.length();
            int firstIndex = str.lastIndexOf("{");
            int lastIndex = str.lastIndexOf("}");
            String first = str.substring(0, firstIndex - 1);
            String last = str.substring(lastIndex + 1, len);
            String param = str.substring(firstIndex + 1, lastIndex -1);
            String out = first + getParamFromAllContexts(param) + last;
            return out;
        }
        return str.replace("'","");
    }

    private String getParamFromAllContexts(String parameter){

        if(testContext().isExist(parameter)){
            return testContext().get(parameter).toString();
        }

        if(store.isExist(parameter)){
            return store.get(parameter);
        }

//        String result;
//        try {
//            result = testContext().get(parameter).toString();
//            System.out.println("Parameter ["+result+"] context");
//        }catch (NullPointerException e){
//            result = store.get(parameter);
//            System.out.println("Parameter ["+result+"] store");
//        }
        return parameter;
    }
}
