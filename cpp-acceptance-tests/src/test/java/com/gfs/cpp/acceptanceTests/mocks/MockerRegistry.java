package com.gfs.cpp.acceptanceTests.mocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockerRegistry {

    @Autowired
    private Resettable[] mockers;

    public MockerRegistry() {
    }

    public void clear() {
        for (Resettable mocker : mockers) {
            mocker.reset();
        }
    }

    public void mock() throws Exception {
    }
}
