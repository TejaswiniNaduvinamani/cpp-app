package com.gfs.cpp.acceptanceTests.hookdefs;

import cucumber.api.java.After;
import cucumber.api.java.Before;

/**
 * ScenarioContext Hook class. This hook binds a new ScenarioContext instance to the current thread when a scenario starts, and it unbinds/removes that
 * instance from the current thread when the scenario ends. In other words each scenario will get a new instance of ScenarioContext.
 * 
 * 
 */
public class ScenarioContextHook {

    @Before
    public void bindContext() {

        // triggers initialValue(). Not really required as the first
        // getScenarioContext call from a stepdef will take care of this.
        ScenarioContextUtil.getScenarioContext();
    }

    @After
    public void removeContext() {

        ScenarioContextUtil.removeScenarioContext();
    }
}
