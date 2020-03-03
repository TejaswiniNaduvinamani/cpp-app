package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;

@RunWith(MockitoJUnitRunner.class)
public class RealCustomerDTOToCustomerPKTransformerTest {

    @InjectMocks
    private RealCustomerDTOToCustomerPKTransformer target;

    @Test
    public void shouldTransformToCustomerPk() throws Exception {

        String realCustomerId = "1000001234";
        int customerTypeId = 3;

        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerId(realCustomerId);
        realCustomerDTO.setRealCustomerType(customerTypeId);

        CustomerPK actual = target.transform(realCustomerDTO);

        assertThat(actual.getId(), equalTo(realCustomerId));
        assertThat(actual.getTypeCode(), equalTo(customerTypeId));

    }

}
