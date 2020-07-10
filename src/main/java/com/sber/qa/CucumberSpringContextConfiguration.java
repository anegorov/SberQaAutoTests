package com.sber.qa;

import cucumber.api.java.Before;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@Log4j2
@SpringBootTest
public class CucumberSpringContextConfiguration {

  @Before
  public void setUp() {
    log.info("-------------- Spring Context Initialized For Executing Cucumber Tests --------------");
  }

}