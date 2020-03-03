package com.gfs.cpp.acceptanceTests.common.data;

import static com.gfs.cpp.acceptanceTests.config.CukesConstants.GFS_CUSTOMER_ID_INACTIVE;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.GFS_CUSTOMER_ID_INVALID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.REAL_CUSTOMER_ID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.REAL_CUSTOMER_ID_EXCEPTION;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.REAL_CUSTOMER_ID_WITH_INACTIVE_MEMBER;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.REAL_CUSTOMER_TYPE_ID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.REAL_CUSTOMER_TYPE_ID_EXCEPTION;

public enum AcceptableRealCustomer {

    REAL_CUSTOMER(REAL_CUSTOMER_ID, REAL_CUSTOMER_TYPE_ID), REAL_CUSTOMER_EXCEPTION(REAL_CUSTOMER_ID_EXCEPTION,
            REAL_CUSTOMER_TYPE_ID_EXCEPTION), REAL_CUSTOMER_INACTIVE_MEMBER(REAL_CUSTOMER_ID_WITH_INACTIVE_MEMBER,
                    REAL_CUSTOMER_TYPE_ID), REAL_CUSTOMER_UNIT("10000124",
                            0), REAL_CUSTOMER_INACTIVE(GFS_CUSTOMER_ID_INACTIVE, 0), REAL_CUSTOMER_INVALID(GFS_CUSTOMER_ID_INVALID, 0);

    String customerId;
    int customerTypeCode;

    private AcceptableRealCustomer(String customerId, int customerTypeCode) {
        this.customerId = customerId;
        this.customerTypeCode = customerTypeCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getCustomerTypeCode() {
        return customerTypeCode;
    }

}
