package com.gfs.cpp.acceptanceTests.common.data;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;

public enum AcceptableCmgCustomer {
    DEFAULT_CMG_CUSTOMER("2014"), EXCEPTION_CMG_CUSTOMER("2096");

    String customerId;

    private AcceptableCmgCustomer(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getCustomerTypeCode() {
        return CukesConstants.CMG_CUSTOMER_TYPE_ID;
    }
}
