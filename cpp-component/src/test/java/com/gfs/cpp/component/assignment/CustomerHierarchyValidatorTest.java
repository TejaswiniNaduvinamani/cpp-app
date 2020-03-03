package com.gfs.cpp.component.assignment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.assignment.CustomerHierarchyValidator;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.proxy.MemberHierarchyQueryProxy;

@RunWith(MockitoJUnitRunner.class)
public class CustomerHierarchyValidatorTest {

    @InjectMocks
    private CustomerHierarchyValidator target;

    @Mock
    private MemberHierarchyQueryProxy memberHierarchyQueryProxy;

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Test
    public void shouldThrowExceptionWhenAnyCustomerIsNotAMember() throws Exception {

        int contractPriceProfileSeq = -101;
        String realCustomerId = "real customer id";
        String defaultCustomerId = "default Customer";
        int defaultCustomerType = 15;

        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerId(realCustomerId);
        realCustomerDTO.setRealCustomerType(3);

        RealCustomerDTO defaultRealCustomer = new RealCustomerDTO();

        defaultRealCustomer.setRealCustomerId(defaultCustomerId);
        defaultRealCustomer.setRealCustomerType(defaultCustomerType);

        List<RealCustomerDTO> allRealCustomers = Collections.singletonList(realCustomerDTO);

        doReturn(defaultRealCustomer).when(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);
        doReturn(allRealCustomers).when(memberHierarchyQueryProxy).filterCustomersNotMemberOfDefaultCustomer(defaultCustomerId, defaultCustomerType,
                allRealCustomers);

        try {
            target.validateCustomerHierarchy(contractPriceProfileSeq, allRealCustomers);
            fail("expected exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_A_MEMBER_OF_DEFAULT_CUSTOMER));
            assertThat(ex.getMessage().contains(realCustomerId), equalTo(true));
        }

        verify(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);
        verify(memberHierarchyQueryProxy).filterCustomersNotMemberOfDefaultCustomer(defaultCustomerId, defaultCustomerType, allRealCustomers);

    }

    @Test
    public void shouldNotThrowExceptionWhenAnyCustomerIsNotAMember() throws Exception {

        int contractPriceProfileSeq = -101;
        String realCustomerId = "real customer id";
        String defaultCustomerId = "default Customer";
        int defaultCustomerType = 15;

        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerId(realCustomerId);
        realCustomerDTO.setRealCustomerType(3);

        RealCustomerDTO defaultRealCustomer = new RealCustomerDTO();

        defaultRealCustomer.setRealCustomerId(defaultCustomerId);
        defaultRealCustomer.setRealCustomerType(defaultCustomerType);

        List<RealCustomerDTO> allRealCustomers = Collections.singletonList(realCustomerDTO);

        doReturn(defaultRealCustomer).when(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);
        doReturn(Collections.EMPTY_LIST).when(memberHierarchyQueryProxy).filterCustomersNotMemberOfDefaultCustomer(defaultCustomerId,
                defaultCustomerType, allRealCustomers);

        target.validateCustomerHierarchy(contractPriceProfileSeq, allRealCustomers);

        verify(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);
        verify(memberHierarchyQueryProxy).filterCustomersNotMemberOfDefaultCustomer(defaultCustomerId, defaultCustomerType, allRealCustomers);

    }

}
