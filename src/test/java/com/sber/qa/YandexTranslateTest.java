package com.sber.qa;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.qameta.allure.Epic;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        plugin = {"pretty","json:target/cucumber-report.json"},
        tags = "@translate")
@Epic(value = "yandex-translate")
public class YandexTranslateTest {

}

