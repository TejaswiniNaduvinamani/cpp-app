package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.CustomerStatus;
import com.gfs.corp.customer.common.controller.CustomerNotFoundException;
import com.gfs.corp.customer.common.controller.CustomerQuery;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.CustomerStatusDTO;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;

@Component
public class CustomerQueryServiceMocker implements Resettable {

    @Autowired
    private CustomerQuery customerQuery;

    @Override
    public void reset() {
        try {
            mockFindCustomerName();
        } catch (CustomerNotFoundException e) {
        }

    }

    @SuppressWarnings("unchecked")
    private void mockFindCustomerName() throws CustomerNotFoundException {
        when(customerQuery.getCustomerStatusInfoByIds(any(List.class))).then(new Answer<Map<CustomerPK, CustomerStatusDTO>>() {

            @Override
            public Map<CustomerPK, CustomerStatusDTO> answer(InvocationOnMock invocation) throws Throwable {
                List<CustomerPK> customer = invocation.getArgumentAt(0, List.class);
                String customerId = (String) customer.get(0).getId();

                if (customerId.equals(CukesConstants.GFS_CUSTOMER_ID_INVALID)) {
                    throw new CustomerNotFoundException("no customer found");
                }

                Map<CustomerPK, CustomerStatusDTO> customerMap = new HashMap<CustomerPK, CustomerStatusDTO>();

                CustomerPK customerPK = new CustomerPK();
                customerPK.setId(customerId);
                customerPK.setTypeCode(customer.get(0).getTypeCode());

                CustomerStatusDTO customerStatusDTO = new CustomerStatusDTO();
                customerStatusDTO.setCustomerId(customerId);
                customerStatusDTO.setCustomerTypeCode(customer.get(0).getTypeCode());
                customerStatusDTO.setCustomerName(customerId + "-" + CukesConstants.CONTRACT_NAME);

                if (customerId.equals(CukesConstants.GFS_CUSTOMER_ID_INACTIVE)) {
                    customerStatusDTO.setCustomerStatusCode(CustomerStatus.INACTIVE.code);
                } else if (customerId.equals(CukesConstants.MEMBER_ID_INACTIVE)) {
                    customerStatusDTO.setCustomerStatusCode(CustomerStatus.INACTIVE.code);
                } else {
                    customerStatusDTO.setCustomerStatusCode(CustomerStatus.ACTIVE.code);
                }

                customerMap.put(customerPK, customerStatusDTO);
                return customerMap;
            }
        });
    }

}
