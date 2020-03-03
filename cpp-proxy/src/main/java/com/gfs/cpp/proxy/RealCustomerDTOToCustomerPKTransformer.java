package com.gfs.cpp.proxy;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;

@Component
public class RealCustomerDTOToCustomerPKTransformer implements Transformer<RealCustomerDTO, CustomerPK>{

    @Override
    public CustomerPK transform(RealCustomerDTO realCustomerDTO) {
        final CustomerPK customerPK = new CustomerPK();
        customerPK.setId(realCustomerDTO.getRealCustomerId());
        customerPK.setTypeCode(realCustomerDTO.getRealCustomerType());

        return customerPK;
    }

}
