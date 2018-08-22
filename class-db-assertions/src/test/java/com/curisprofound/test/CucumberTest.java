package com.curisprofound.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        glue = "com.curisprofound.test.cucumber",
        features = "src/test/resources")
public class CucumberTest {
}
