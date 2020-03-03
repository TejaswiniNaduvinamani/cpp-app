package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.CustomerStatus;
import com.gfs.corp.customer.common.CustomerType;
import com.gfs.corp.customer.common.controller.CustomerGroupMembershipQuery;
import com.gfs.corp.customer.common.controller.CustomerNotFoundException;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.CustomerStatusDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;

@RunWith(MockitoJUnitRunner.class)
public class CustomerGroupMemberValidatorTest {

    @InjectMocks
    private CustomerGroupMemberValidator target;

    @Mock
    private CustomerGroupMembershipQuery customerGroupMembershipQuery;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private CustomerQueryProxy customerQueryProxy;

    @Test
    public void findCustomerWithInactiveFamilyMemberStatusReturnsInvalidCustomer() throws CustomerNotFoundException {
        final String customerId = "768857655";
        final String memberId = "123456";
        final int memberStatusCode = CustomerStatus.INACTIVE.code;
        final String memberName = "jack sparrow";
        final Integer customerTypeCode = CustomerType.CUSTOMER_FAMILY.getCode();
        final Integer memberTypeCode = CustomerType.CUSTOMER_TYPE_UNIT.getCode();
        final List<String> memberUnitIdList = Arrays.asList(memberId);
        final Date currentDate = new LocalDate().toDate();

        final List<CustomerPK> customerPks = buildCustomerPks(customerId, customerTypeCode);

        final List<CustomerPK> customerPksForMember = buildCustomerPks(memberId, memberTypeCode);

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoForMember = buildCustomerStatusDtoByCustomerPk(customerPksForMember.get(0),
                memberStatusCode, memberName);

        when(cppDateUtils.getCurrentDate()).thenReturn(currentDate);
        when(customerGroupMembershipQuery.findMemberUnits(customerId, customerTypeCode, currentDate)).thenReturn(memberUnitIdList);
        when(customerQueryProxy.getCustomerStatusDTOByCustomerPk(customerPksForMember)).thenReturn(customerStatusDtoForMember);
        when(customerServiceProxy.isActiveCustomer(customerStatusDtoForMember.get(customerPksForMember.get(0)))).thenReturn(false);

        try {
            target.validateIfHasActiveMembers(customerPks.get(0));
            fail("expected exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.INACTIVE_CUSTOMER_FOUND));
        }

    }

    @Test
    public void findCustomerWithActiveFamilyMemberStatusValidatesCustomer() throws CustomerNotFoundException {
        final String customerId = "768857655";
        final String memberId = "123456";
        final int memberStatusCode = CustomerStatus.INACTIVE.code;
        final String memberName = "jack sparrow";
        final Integer customerTypeCode = CustomerType.CUSTOMER_FAMILY.getCode();
        final Integer memberTypeCode = CustomerType.CUSTOMER_TYPE_UNIT.getCode();
        final List<String> memberUnitIdList = Arrays.asList(memberId);
        final Date currentDate = new LocalDate().toDate();

        final List<CustomerPK> customerPks = buildCustomerPks(customerId, customerTypeCode);

        final List<CustomerPK> customerPksForMember = buildCustomerPks(memberId, memberTypeCode);

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoForMember = buildCustomerStatusDtoByCustomerPk(customerPksForMember.get(0),
                memberStatusCode, memberName);

        final CustomerStatusDTO customerStatusDTO = customerStatusDtoForMember.get(customerPksForMember.get(0));

        when(cppDateUtils.getCurrentDate()).thenReturn(currentDate);
        when(customerGroupMembershipQuery.findMemberUnits(customerId, customerTypeCode, currentDate)).thenReturn(memberUnitIdList);
        when(customerQueryProxy.getCustomerStatusDTOByCustomerPk(customerPksForMember)).thenReturn(customerStatusDtoForMember);
        when(customerServiceProxy.isActiveCustomer(customerStatusDTO)).thenReturn(true);

        target.validateIfHasActiveMembers(customerPks.get(0));

        verify(customerQueryProxy).getCustomerStatusDTOByCustomerPk(customerPksForMember);
        verify(customerServiceProxy).isActiveCustomer(customerStatusDTO);
        verify(cppDateUtils).getCurrentDate();
        verify(customerGroupMembershipQuery).findMemberUnits(customerId, customerTypeCode, currentDate);

    }

    private List<CustomerPK> buildCustomerPks(final String customerId, final Integer customerTypeCode) {

        final List<CustomerPK> customerPks = new ArrayList<>();
        final CustomerPK customerPk = new CustomerPK(customerId, customerTypeCode);
        customerPks.add(customerPk);
        return customerPks;
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
