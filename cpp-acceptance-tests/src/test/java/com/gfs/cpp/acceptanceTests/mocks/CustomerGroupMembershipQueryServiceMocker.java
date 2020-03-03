package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.controller.CustomerGroupMembershipQuery;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;

@Component
public class CustomerGroupMembershipQueryServiceMocker {

    @Autowired
    private CustomerGroupMembershipQuery customerGroupMembershipQuery;

    public void mockFindMemberUnits() {
        when(customerGroupMembershipQuery.findMemberUnits(any(String.class), anyInt(), any(Date.class))).then(new Answer<List<String>>() {

            @Override
            public List<String> answer(InvocationOnMock invocation) throws Throwable {
                String customerId = invocation.getArgumentAt(0, String.class);

                List<String> memberUnitIdList = new ArrayList<>();
                if (customerId.equals(CukesConstants.REAL_CUSTOMER_ID)) {
                    memberUnitIdList.add(CukesConstants.MEMBER_ID_ACTIVE);
                } else if (customerId.equals(CukesConstants.REAL_CUSTOMER_ID_EXCEPTION)) {
                    memberUnitIdList.add(CukesConstants.MEMBER_ID_ACTIVE);
                } else if (customerId.equals(CukesConstants.REAL_CUSTOMER_ID_WITH_INACTIVE_MEMBER)) {
                    memberUnitIdList.add(CukesConstants.MEMBER_ID_INACTIVE);
                }
                return memberUnitIdList;
            }
        });
    }
}
