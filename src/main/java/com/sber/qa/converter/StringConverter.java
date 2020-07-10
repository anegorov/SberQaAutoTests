package com.sber.qa.converter;

import com.sber.qa.configuration.CucumberTestContext;
import lombok.Data;

@Data
public class StringConverter {

    private CucumberTestContext CONTEXT = CucumberTestContext.CONTEXT;

    private String str;

    protected CucumberTestContext testContext() {
        return CONTEXT;
    }

    public StringConverter(String str){
        this.str = str;
    }

    public String getConverted(){
        return convert(str);
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
            return first + testContext().get(param) + last;
        }
        return str.replace("'","");
    }
}
