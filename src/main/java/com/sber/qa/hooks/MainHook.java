package com.sber.qa.hooks;

import com.sber.qa.steps.AbstractSteps;
import cucumber.api.Scenario;
import cucumber.api.java.Before;

public class MainHook extends AbstractSteps {

    @Before("@all")
    public void doBefore(Scenario scenario){
        testContext().reset();
    }
}
