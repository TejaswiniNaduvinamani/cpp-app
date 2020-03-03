package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.CustomerStatus;
import com.gfs.corp.customer.common.CustomerType;
import com.gfs.corp.customer.common.controller.CustomerNotFoundException;
import com.gfs.corp.customer.common.controller.CustomerQuery;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.CustomerStatusDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;

@RunWith(MockitoJUnitRunner.class)
public class CustomerQueryProxyTest {

    private final Integer customerFamilyTypeCode = CustomerType.CUSTOMER_FAMILY.getCode();

    @InjectMocks
    private CustomerQueryProxy target;

    @Mock
    private CustomerQuery customerQuery;

    @Test
    public void findCustomerWithActiveFamilyMemberStatusReturnsCustomer() throws CustomerNotFoundException {
        final String customerId = "768857655";
        final int statusCode = CustomerStatus.ACTIVE.code;
        final String customerName = "john jo";

        final List<CustomerPK> customerPks = Collections.singletonList(new CustomerPK(customerId, customerFamilyTypeCode));

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoByCustomerPk = buildCustomerStatusDtoByCustomerPk(customerPks.get(0), statusCode,
                customerName);

        when(customerQuery.getCustomerStatusInfoByIds(customerPks)).thenReturn(customerStatusDtoByCustomerPk);

        final Map<CustomerPK, CustomerStatusDTO> result = target.getCustomerStatusDTOByCustomerPk(customerPks);

        assertThat(result, CoreMatchers.notNullValue());

        CustomerStatusDTO customerStatusDTO = result.get(customerPks.get(0));

        assertThat(customerStatusDTO.getCustomerId(), equalTo(customerId));
        assertThat(customerStatusDTO.getCustomerName(), equalTo(customerName));
        assertThat(customerStatusDTO.getCustomerStatusCode(), equalTo(CustomerStatus.ACTIVE.code));

        verify(customerQuery).getCustomerStatusInfoByIds(customerPks);
    }

    @Test
    public void shouldReturnInvalidCustomerWhenGetCustomerStatusInfoByIds() throws CustomerNotFoundException {
        final String customerId = "96575";

        final List<CustomerPK> customerPks = Collections.singletonList(new CustomerPK(customerId, customerFamilyTypeCode));

        when(customerQuery.getCustomerStatusInfoByIds(customerPks)).thenThrow(new CustomerNotFoundException("Customer Not Found"));

        try {
            target.getCustomerStatusDTOByCustomerPk(customerPks);
            fail("expected exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.INVALID_CUSTOMER_FOUND));
        }
    }

    private Map<CustomerPK, CustomerStatusDTO> buildCustomerStatusDtoByCustomerPk(final CustomerPK customerPk, final int statusCode,
            final String customerName) {

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoByCustomerPk = new HashMap<>();
        final CustomerStatusDTO customerStatusDto = new CustomerStatusDTO();
        customerStatusDto.setCustomerId(customerPk.getId());
        customerStatusDto.setCustomerTypeCode(customerPk.getTypeCode());
        customerStatusDto.setCustomerName(customerName);
        customerStatusDto.setCustomerStatusCode(statusCode);

        customerStatusDtoByCustomerPk.put(customerPk, customerStatusDto);
        return customerStatusDtoByCustomerPk;
    }
}
