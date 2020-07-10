package com.sber.qa.steps;

import com.sber.qa.configuration.ConfigLoader;
import com.sber.qa.configuration.CucumberTestContext;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.sber.qa.configuration.CucumberTestContext.*;
import static io.restassured.config.RedirectConfig.redirectConfig;


public class AbstractSteps {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractSteps.class);

  private CucumberTestContext CONTEXT = CucumberTestContext.CONTEXT;

  protected CucumberTestContext testContext() {
    return CONTEXT;
  }

  protected void sendRequest(Method method, String address) {
    sendRequest(method, address, null, null);
  }

  protected void sendRequest(Method method, String apiPath, Map<String, String> pathParams) {
    sendRequest(method, apiPath, pathParams, null);
  }

  protected void sendRequest(Method method, String address, Map<String, String> pathParams, Map<String, String> queryParams) {
    final RequestSpecification request = CONTEXT.getRequest();
    final Object payload = CONTEXT.getPayload();
    final String url = baseUrl() + address;

    setPayload(request, payload);
    setQueryParams(queryParams, request);
    setPathParams(pathParams, request);

    Response response = request
            .config(RestAssured.config().redirect(redirectConfig().followRedirects(true)))
            .accept(ContentType.JSON)
            .log()
            .all()
            .request(method.name(), url);

    logResponse(response);
    CONTEXT.setResponse(response);
    CONTEXT.reset(PATH_PARAMS);
    CONTEXT.reset(QUERY_PARAMS);
    CONTEXT.reset(REQUEST);
  }

  protected void getMarsSessionId(String login, String password) {
    final RequestSpecification request = CONTEXT.getRequest();
    Response response = request
            .config(RestAssured.config().redirect(redirectConfig().followRedirects(true)))
            .contentType("multipart/form-data")
            .header("Connection","keep-alive")
            .multiPart("userId", login)
            .multiPart("password", password)
            .log()
            .all()
            .get(baseUrl() + "/mars/security/login_submit.html");
    logResponse(response);
    CONTEXT.setSessionId(response);
    CONTEXT.setResponse(response);
    Cookies coo = response.getDetailedCookies();
    response = request
            .cookies(coo)
            .log()
            .all()
            .get(baseUrl() + "/mars/security/login_submit.html");
  }


  protected void setRequestSpecification(Map<String,String> headers){
    final RequestSpecification request = CONTEXT.getRequest();
    CONTEXT.setRequestSpecification(request.headers(headers));
  }

  protected void setRequestQueryParams(Map<String,String> headers){
    final RequestSpecification request = CONTEXT.getRequest();
    CONTEXT.setRequestSpecification(request.headers(headers));
  }

  protected void saveRequestCookie(){
    Cookies coo = CONTEXT.getResponse().getDetailedCookies();
    testContext().set(COOKIES, coo);
  }

  protected void useRequestCookie(){
    final RequestSpecification request = CONTEXT.getRequest();
    Cookies coo = testContext().get(COOKIES, Cookies.class);
    CONTEXT.setRequestSpecification(request.cookies(coo));
  }

  private String baseUrl() {
    if(CONTEXT.isExist(CucumberTestContext.BASE_URL))
      return testContext().get(CucumberTestContext.BASE_URL).toString();
    return ConfigLoader.getEnvProperty("baseUrl");
  }

  private void logResponse(Response response) {
    response.then()
            .log()
            .all();
  }

  private void setQueryParams(Map<String, String> queryParamas, RequestSpecification request) {
    if (null != queryParamas) {
      request.queryParams(queryParamas);
    }
  }

  private void setPathParams(Map<String, String> pathParams, RequestSpecification request) {
    if (null != pathParams) {
      request.pathParams(pathParams);
    }
  }

  private void setPayload(RequestSpecification request, Object payload) {
    if (null != payload) {
      request.contentType(ContentType.JSON)
              .body(payload);
    }
  }

}
