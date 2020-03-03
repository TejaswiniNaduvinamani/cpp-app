package com.gfs.cpp.acceptanceTests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

//@formatter:off
@RunWith(Cucumber.class)
@CucumberOptions(
     tags = { "@selected" },
     glue = { "com.gfs.cpp.acceptanceTests.stepdefs", "com.gfs.cpp.acceptanceTests.hookdefs", "cucumber.api.spring" },
     features = { "src/test/resources/features" },
     plugin = { "html:target/cucumber-html-report" }
)
//@formatter:on
public class RunSelectedCukesTest {

}
