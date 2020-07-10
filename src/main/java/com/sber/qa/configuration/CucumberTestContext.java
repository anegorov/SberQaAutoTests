package com.sber.qa.configuration;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.lang.ThreadLocal.withInitial;

public enum CucumberTestContext {
  CONTEXT;

  public static final String PAYLOAD = "PAYLOAD";
  public static final String REQUEST = "REQUEST";
  public static final String RESPONSE = "RESPONSE";
  public static final String API_PATH = "api-path";
  public static final String BASE_URL = "base-url";
  public static final String REQUEST_TYPE = "request-type";
  public static final String SESSION_ID = "JSESSIONID";
  public static final String PATH_PARAMS = "pathParams";
  public static final String QUERY_PARAMS = "queryParams";
  public static final String COOKIES = "cookies";

  private final ThreadLocal<Map<String, Object>> threadLocal = withInitial(HashMap::new);

  private Map<String, Object> testContextMap() {
    return threadLocal.get();
  }

  public String getRequestType() {
    return testContextMap().get(REQUEST_TYPE).toString();
  }

  public void setRequestType(String value) {
    testContextMap().put(REQUEST_TYPE, value);
  }

  public void set(String key, Object value) { testContextMap().put(key, value); }

  public Object get(String key) { return testContextMap().get(key); }

  public boolean isExist(String key) {
    return testContextMap().containsKey(key);
  }

  public <T> T get(String key, Class<T> clazz) {
    return clazz.cast(testContextMap().get(key));
  }

  public void setPayload(Object value) {
    set(PAYLOAD, value);
  }

  public Object getPayload() {
    return testContextMap().get(PAYLOAD);
  }

  public <T> T getPayload(Class<T> clazz) {
    return get(PAYLOAD, clazz);
  }

  public RequestSpecification getRequest() {
    RequestSpecification req = get(REQUEST, RequestSpecification.class);
    return (null == req) ? given() : req;
  }

  public void resetRequest() {
    RequestSpecification req = get(REQUEST, RequestSpecification.class);
  }

  public void setRequestSpecification(RequestSpecification requestSpecification){
    set(REQUEST, requestSpecification);
  }

  public void setResponse(Response response) {
    set(RESPONSE, response);
  }

  public Response getResponse() {
    return get(RESPONSE, Response.class);
  }

  public void reset() {
    testContextMap().clear();
  }

  public void reset(String key) {
    testContextMap().remove(key);
  }

  public void setSessionId(Response response) { set(SESSION_ID, response.getSessionId()); }

  public String getSessionId() { return get(SESSION_ID, String.class); }
}
