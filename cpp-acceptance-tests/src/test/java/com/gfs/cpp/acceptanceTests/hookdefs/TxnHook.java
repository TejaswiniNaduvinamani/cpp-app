package com.gfs.cpp.acceptanceTests.hookdefs;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.spring.SpringTransactionHooks;

public class TxnHook {

    @Autowired
    private SpringTransactionHooks springTransactionHooks;

    @Before(value = "not @txn", order = 100)
    public void startTransaction() {
        springTransactionHooks.startTransaction();
    }

    @After(value = "not @txn", order = 100)
    public void rollBackTransaction() {
        springTransactionHooks.rollBackTransaction();
    }
}
