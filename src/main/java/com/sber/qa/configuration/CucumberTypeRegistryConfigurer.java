package com.sber.qa.configuration;

import com.sber.qa.converter.ValueConverter;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import lombok.extern.log4j.Log4j2;
import io.restassured.http.Method;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.ENGLISH;

@Log4j2
public class CucumberTypeRegistryConfigurer implements TypeRegistryConfigurer {

  @Autowired
  private GlobalContext store;

  Transformer transformer;

  public CucumberTypeRegistryConfigurer() {
    ValueConverter vc = new ValueConverter(store);
    transformer = new Transformer(vc);
  }

  @Override
  public Locale locale() {
    return ENGLISH;
  }

  @Override
  public void configureTypeRegistry(cucumber.api.TypeRegistry typeRegistry) {
    typeRegistry.setDefaultDataTableCellTransformer(transformer);
//    typeRegistry.setDefaultDataTableEntryTransformer(transformer);
    typeRegistry.setDefaultParameterTransformer(transformer);

    typeRegistry.defineParameterType(
            new ParameterType<>("names", ".*?", List.class, (String s) -> Arrays.asList(s.split(","))));
    typeRegistry.defineParameterType(
            new ParameterType<>("method", "GET|PUT|POST|DELETE", Method.class, (io.cucumber.cucumberexpressions.Transformer<Method>) Method::valueOf));
  }
}