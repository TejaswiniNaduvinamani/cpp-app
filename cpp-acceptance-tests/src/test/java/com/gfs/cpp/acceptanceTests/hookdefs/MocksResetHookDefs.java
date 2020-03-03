package com.gfs.cpp.acceptanceTests.hookdefs;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gfs.cpp.acceptanceTests.mocks.MockerRegistry;

import cucumber.api.java.Before;

public class MocksResetHookDefs {

    @Autowired
    private MockerRegistry mockerRegistry;

    @Autowired
    @Qualifier("mockito")
    public Object[] allMocks;

    @Before(order = 101)//  keep this order low, Otherwise mocks set up in other Before's can reset just after they setup
    public void performResets() {
    	reset(allMocks);
        mockerRegistry.clear();
    }
}
