package com.sber.qa.steps;

import com.sber.qa.configuration.GlobalContext;
import com.sber.qa.converter.ValueConverter;
import com.sber.qa.generators.FreemarkerEngine;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.restassured.http.Method;
import io.restassured.response.ResponseBody;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Native;
import java.nio.charset.Charset;
import java.util.*;

import java.util.stream.Collectors;

import static com.sber.qa.configuration.CucumberTestContext.*;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@RequiredArgsConstructor
public class Steps extends AbstractSteps implements En {

    private final FreemarkerEngine templates;
    private final GlobalContext store;
    private final ValueConverter vc;

    @Given("save sessionId to variable {}")
    public void saveSessionIdToVariable(String variableName) {
        testContext().set(variableName, testContext().getSessionId());
        log.info("save sessionId [{}] to variable [{}]", testContext().getSessionId(), variableName);
    }

    @Given("set base url {}")
    public void setValue(String url) {
        testContext().set(BASE_URL, url);
        log.info("set base url [{}]", url);
    }

    @Given("set {} to value {}")
    public void setValue(String valueName, String value) {
        testContext().set(valueName, value);
        store.put(valueName, value);
        log.info("set [{}] to value [{}]", valueName, value);
    }

    @Given("extend {string} with {string} and save to {string}")
    public void extendValue(String base, String plus, String result) {
        String tempBase = testContext().get(base, String.class);
        String tempPlus = testContext().get(plus, String.class);
        store.put(result, tempBase + tempPlus);
        testContext().set(result, tempBase + tempPlus);
        log.info("extend [{}] with [{}] and saved to [{}]", tempBase, tempPlus, result);
    }

    @Given("resource is {}")
    public void setSource(String url) {
        testContext().set(API_PATH, url);
    }

    @Given("headers are")
    public void setHeaders(Map<Object, Object> headers) {
        setRequestSpecification(mapObjectToString(headers));
    }

    @Given("save cookies")
    public void saveCookie() {
        saveRequestCookie();
    }

    @Given("use cookies")
    public void useCookie() {
        useRequestCookie();
    }

    @Given("create response body from template {string}")
    public void createResponseBody(String templateFileName, Map<Object, Object> mapBody) {
        mapBody.forEach((k,v)-> System.out.println(k.toString() +":"+v.toString()));
        testContext().setPayload(templates.createBody(templateFileName, mapObjectToString(mapBody)));
    }

    @Given("create request body for form-data type")
    public void createRequestBodyForData(Map<Object, Object> mapBody) {
        mapBody.forEach((k,v)-> System.out.println(k.toString() +":"+v.toString()));
        testContext().setPayload(mapObjectToString(mapBody));
        testContext().setRequestType("multipart/form-data");
    }

    ///////////////////////////////////////
    //////////////// WHEN ////////////////

    @When("execute {method}")
    public void execute(Method httpMethod) {
        sendRequest(httpMethod, testContext().get(API_PATH).toString());
    }

    @When("execute with parameters {method}")
    public void execute(Method httpMethod, Map<Object, Object>  params) {
        sendRequest(httpMethod, testContext().get(API_PATH).toString(), mapObjectToString(params));
    }

    @When("perform {method}")
    public void perform(Method httpMethod) {
        Map pathParams = testContext().isExist(PATH_PARAMS) ? testContext().get(PATH_PARAMS, Map.class) : null;
        Map queryParams = testContext().isExist(QUERY_PARAMS) ? testContext().get(QUERY_PARAMS, Map.class) : null;
        sendRequest(httpMethod, testContext().get(API_PATH).toString(), pathParams, queryParams);
    }

    @When("set path params")
    public void setPathParams(Map<Object, Object>  paramsPath) {
        testContext().set(PATH_PARAMS, mapObjectToString(paramsPath));
    }

    @When("set query params")
    public void setQueryParams(Map<Object, Object>  paramsQuery) {
        testContext().set(QUERY_PARAMS, mapObjectToString(paramsQuery));
    }

    @When("print response body")
    public void printResponseBody() {
        System.out.println("PRINT: " + testContext().getResponse().getBody().asString());
    }

    @When("print variable {string}")
    public void printVariable(String variable) {
        try {
            System.out.println("PRINT: " + testContext().get(variable));
        } catch (NullPointerException e) {
            log.error("There is no variable [" + variable + "] in test context");
        }
    }

    @When("save response to {string}")
    public void saveResponse(String variableName) {
        testContext().set(variableName, testContext().getResponse());
    }

    @And("save response parameter {string} to {string}")
    public void saveParameterOfResponse(String parameterPath, String variableName) {
        String responseParametr = getValueByPath(parameterPath);
        testContext().set(variableName, responseParametr);
        store.put(variableName, responseParametr);
    }

    @And("save HTML response parameter {string} to {string}")
    public void saveXmlParameterOfResponse(String parameterPath, String variableName) {
        String responseParametr = testContext().getResponse().getBody().htmlPath().get(parameterPath).toString();
        testContext().set(variableName, responseParametr);
        store.put(variableName, responseParametr);
    }

    @When("response code is {int}")
    public void checkResponseCode(@NonNull Integer expectedCode) {
        int responseCode = testContext().getResponse().getStatusCode();
        assertThat(expectedCode).isNotZero().isEqualTo(responseCode);
    }

    @When("check response")
    public void checkResponse(@NonNull Map<Object, Object> mapBody) {
        mapObjectToString(mapBody).forEach((path, value) -> {
            assertThat(value).isNotNull()
                    .isEqualTo(getValueByPath(path));
        });
    }

    private String getValueByPath(String path) {
        ResponseBody body = testContext().getResponse().getBody();
        if(body.asString().toLowerCase().contains("<xml")){
            return body.xmlPath().get(path).toString();
        }
        return body.jsonPath().get(path).toString();
    }

    @When("check response match with length {int}")
    public void checkResponseTest(@NonNull Integer entriesNum, @NonNull Map<Object, Object> mapBody) {
        mapObjectToString(mapBody).forEach((path, value) -> {
            String[] namesDict = value.split(" ");
            for (int i = 0; i < entriesNum; i++){
                char c = (char) (i + '0');
                String valueFromJson = testContext().getResponse().getBody().jsonPath().get(path.replace('#', c)).toString();
                assertThat(Arrays.stream(namesDict).parallel().anyMatch(valueFromJson::contains));
            }
        });
    }

    private Map<String, String> mapObjectToString(Map<Object, Object> map) {
        return map.entrySet().stream().
                collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue().toString()
                ));

    }

    @And("wait {int} seconds")
    public void wait(int sec) throws InterruptedException {
        Thread.sleep(sec * 1000);
    }

    @And("reset context")
    public void reset() {
        testContext().reset();
    }

    @And("reset payload")
    public void resetPayload() {
        testContext().setPayload(null);
    }

}