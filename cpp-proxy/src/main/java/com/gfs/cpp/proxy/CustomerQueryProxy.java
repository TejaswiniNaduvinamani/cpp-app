package com.gfs.cpp.proxy;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.controller.CustomerNotFoundException;
import com.gfs.corp.customer.common.controller.CustomerQuery;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.CustomerStatusDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;

@Component
public class CustomerQueryProxy {

    private static final Logger logger = LoggerFactory.getLogger(CustomerQueryProxy.class);

    @Autowired
    private CustomerQuery customerQuery;

    public Map<CustomerPK, CustomerStatusDTO> getCustomerStatusDTOByCustomerPk(final List<CustomerPK> customerPks) {

        try {
            return customerQuery.getCustomerStatusInfoByIds(customerPks);

        } catch (final CustomerNotFoundException exc) {
            logger.error("Customer Not Found for Customer Id {}", customerPks.get(0).getId(), exc);
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CUSTOMER_FOUND, "Invalid Customer found.");
        }
    }

}
