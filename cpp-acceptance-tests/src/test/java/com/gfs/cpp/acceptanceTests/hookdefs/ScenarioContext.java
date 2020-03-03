package com.gfs.cpp.acceptanceTests.hookdefs;

import java.util.HashMap;
import java.util.Map;

/**
 * A context class, each scenario will get own instance of this context class. Save any attributes, and retrieve back as long as you are in same
 * scenario. Once the cucumber runtime finishes executing current scenario steps, this context instance will be thrown away. So no need to bother
 * about resetting/clearing attributes when a scenario ends/starts up. ScenarioContextHook class takes care of binding and unbinding context instance
 * with current thread.
 * 
 * @see ScenarioContextHook
 * 
 */
public class ScenarioContext {

    private Map<String, Object> map = new HashMap<String, Object>();

    public ScenarioContext() {

    }

    public void setAttribute(String attributeName, Object attributeValue) {
        map.put(attributeName, attributeValue);
    }

    public Object getAttribute(String attributeName) {
        return map.get(attributeName);
    }
}
