package com.curisprofound.spring.reactivestackdemo;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        glue = "com.curisprofound.spring.reactivestackdemo.cucumber",
        features = "src/test/resources")
@ContextConfiguration(classes=ReactiveStackDemoApplication.class)
@ComponentScan(basePackages = "com.curisprofound.spring.reactivestackdemo")
@EnableWebFlux
public class CucumberTest {
}
