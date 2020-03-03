package com.gfs.cpp.component.assignment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;
import com.gfs.cpp.component.assignment.CustomerAssignmentValidator;
import com.gfs.cpp.component.assignment.CustomerHierarchyValidator;
import com.gfs.cpp.component.assignment.DuplicateCustomerFinder;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAssignmentValidatorTest {

    protected static final String ERROR_MESSAGE = "error message";

    @InjectMocks
    @Spy
    private CustomerAssignmentValidator target = new CustomerAssignmentValidator() {

        @Override
        String buildErrorMessage(List<DuplicateCustomerDTO> duplicateCustomerAcrossConcept) {
            return ERROR_MESSAGE;
        }
    };

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private DuplicateCustomerFinder duplicateCustomerFinder;
    @Mock
    private CustomerHierarchyValidator customerHierarchyValidator;

    @Test
    public void shouldValidateCustomerHierarchyWhenAssignmentDefaultCustomer() throws Exception {

        int contractPriceProfileSeq = -101;
        Date pricingEffectiveDate = new LocalDate(2018, 01, 05).toDate();

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);

        customerAssignmentDTO.setClmContractStartDate(pricingEffectiveDate);

        doReturn(false).when(target).isDefaultConceptCustomer(customerAssignmentDTO);
        doNothing().when(target).validateExistingMappingForDefaultConcept(customerAssignmentDTO, false);
        doNothing().when(target).validateIfDuplicateAcrossConcept(customerAssignmentDTO);
        doNothing().when(target).validateIfDuplicateAcrossContracts(customerAssignmentDTO);

        target.validateCustomer(customerAssignmentDTO);

        verify(target).validateExistingMappingForDefaultConcept(customerAssignmentDTO, false);
        verify(customerHierarchyValidator).validateCustomerHierarchy(contractPriceProfileSeq, realCustomerDTOList);
        verify(target).validateIfDuplicateAcrossConcept(customerAssignmentDTO);
        verify(target).validateIfDuplicateAcrossContracts(customerAssignmentDTO);

    }

    @Test
    public void shouldNotValidateCustomerHierarchyWhenNotForDefaultCustomer() throws Exception {

        int contractPriceProfileSeq = -101;
        Date pricingEffectiveDate = new LocalDate(2018, 01, 05).toDate();

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);

        customerAssignmentDTO.setClmContractStartDate(pricingEffectiveDate);

        doReturn(true).when(target).isDefaultConceptCustomer(customerAssignmentDTO);
        doNothing().when(target).validateExistingMappingForDefaultConcept(customerAssignmentDTO, true);
        doNothing().when(target).validateIfDuplicateAcrossConcept(customerAssignmentDTO);
        doNothing().when(target).validateIfDuplicateAcrossContracts(customerAssignmentDTO);

        target.validateCustomer(customerAssignmentDTO);

        verify(target).validateExistingMappingForDefaultConcept(customerAssignmentDTO, true);
        verify(customerHierarchyValidator, never()).validateCustomerHierarchy(anyInt(), anyListOf(RealCustomerDTO.class));

    }

    @Test
    public void shouldThrowMultipleCustomerExcpetionWhenAssignedToDefaultAndFoundCustoemerMappedAlready() throws Exception {

        int contractPriceProfileSeq = 101;

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        RealCustomerDTO realCustomerMappedToDefault = new RealCustomerDTO();

        doReturn(realCustomerMappedToDefault).when(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);

        try {
            target.validateExistingMappingForDefaultConcept(customerAssignmentDTO, true);
            fail("expected exception");
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.HAS_MULTIPLE_CUSTOMER_MAPPING));
        }

    }

    @Test
    public void shouldNotThrowMultipleCustomerExcpetionWhenAssignedToDefaultAndNotFoundCustoemerMappedAlready() throws Exception {

        int contractPriceProfileSeq = 101;

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);

        doReturn(null).when(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);

        target.validateExistingMappingForDefaultConcept(customerAssignmentDTO, true);

        verify(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);
        verify(target, never()).validateNewCustomerMappingForExceptionConcept(any(RealCustomerDTO.class));
    }

    @Test
    public void shouldValidateExceptionCustoemrMappingWhenAssignedIsNotDefault() throws Exception {

        int contractPriceProfileSeq = 101;

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        RealCustomerDTO realCustomerMappedToDefault = new RealCustomerDTO();

        doReturn(realCustomerMappedToDefault).when(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);
        doNothing().when(target).validateNewCustomerMappingForExceptionConcept(realCustomerMappedToDefault);

        target.validateExistingMappingForDefaultConcept(customerAssignmentDTO, false);

        verify(cppConceptMappingRepository).fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);
        verify(target).validateNewCustomerMappingForExceptionConcept(realCustomerMappedToDefault);
    }

    @Test
    public void shouldThrowNoCustomerMappedToDefaultCustomer() throws Exception {

        try {
            target.validateNewCustomerMappingForExceptionConcept(null);
            fail("expected exception");
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT));
        }
    }

    @Test
    public void shouldThrowCustomerMappedToDefaultCustomerIsUnit() throws Exception {

        RealCustomerDTO realCustomerMappedToDefault = new RealCustomerDTO();
        realCustomerMappedToDefault.setRealCustomerType(CustomerTypeCodeEnum.CUSTOMER_UNIT.getGfsCustomerTypeCode());

        try {
            target.validateNewCustomerMappingForExceptionConcept(realCustomerMappedToDefault);
            fail("expected exception");
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE));
        }
    }

    @Test
    public void shouldNotThrowAnyExceptionWhenFoundRealCustomerMappedAndIsNotUnit() throws Exception {

        RealCustomerDTO realCustomerMappedToDefault = new RealCustomerDTO();
        realCustomerMappedToDefault.setRealCustomerType(CustomerTypeCodeEnum.CUSTOMER_FAMILY.getGfsCustomerTypeCode());

        target.validateNewCustomerMappingForExceptionConcept(realCustomerMappedToDefault);
    }

    @Test
    public void shouldThrowCustomerAlreadyMappedToConceptErrorWHenDuplicateFound() throws Exception {

        int contractPriceProfileSeq = 101;
        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        DuplicateCustomerDTO duplicateCustoemerDTO = new DuplicateCustomerDTO();
        List<DuplicateCustomerDTO> duplicateCustomerAcrossConcept = Collections.singletonList(duplicateCustoemerDTO);

        doReturn(duplicateCustomerAcrossConcept).when(duplicateCustomerFinder).findDuplicateCustomerAcrossConcepts(realCustomerDTOList,
                contractPriceProfileSeq);

        try {
            target.validateIfDuplicateAcrossConcept(customerAssignmentDTO);
            fail("expected exception");
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_CONCEPTS));
            assertThat(ce.getMessage(), equalTo(ERROR_MESSAGE));
        }

        verify(duplicateCustomerFinder).findDuplicateCustomerAcrossConcepts(realCustomerDTOList, contractPriceProfileSeq);
    }

    @Test
    public void shouldNotThrowCustomerAlreadyMappedToConceptErrorWhenNoDuplicateFound() throws Exception {

        int contractPriceProfileSeq = 101;
        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);

        doReturn(Collections.EMPTY_LIST).when(duplicateCustomerFinder).findDuplicateCustomerAcrossConcepts(realCustomerDTOList,
                contractPriceProfileSeq);

        target.validateIfDuplicateAcrossConcept(customerAssignmentDTO);

        verify(duplicateCustomerFinder).findDuplicateCustomerAcrossConcepts(realCustomerDTOList, contractPriceProfileSeq);
    }

    @Test
    public void shouldThrowDuplicateAcrossContractErrorWhenDuplicateFound() throws Exception {

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        DuplicateCustomerDTO duplicateCustoemerDTO = new DuplicateCustomerDTO();
        List<DuplicateCustomerDTO> duplicateCustomerAcrossConcept = Collections.singletonList(duplicateCustoemerDTO);

        doReturn(duplicateCustomerAcrossConcept).when(duplicateCustomerFinder).findDuplicateCustomerInOtherContract(customerAssignmentDTO);

        try {
            target.validateIfDuplicateAcrossContracts(customerAssignmentDTO);
            fail("expected exception");
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT));
            assertThat(ce.getMessage(), equalTo(ERROR_MESSAGE));
        }

        verify(duplicateCustomerFinder).findDuplicateCustomerInOtherContract(customerAssignmentDTO);

    }

    @Test
    public void shouldNotThrowDuplicateAcrossContractErrorWhenNoDuplicateFound() throws Exception {

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();

        doReturn(null).when(duplicateCustomerFinder).findDuplicateCustomerInOtherContract(customerAssignmentDTO);

        target.validateIfDuplicateAcrossContracts(customerAssignmentDTO);

        verify(duplicateCustomerFinder).findDuplicateCustomerInOtherContract(customerAssignmentDTO);

    }

    @Test
    public void shouldReturnTrueIfDefaultCustomer() throws Exception {

        int contractPriceProfileSeq = 1010;
        String cmgCustomerId = "1000124";
        int cmgCustomerType = 31;

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        customerAssignmentDTO.setCmgCustomerId(cmgCustomerId);
        customerAssignmentDTO.setCmgCustomerType(cmgCustomerType);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setDefaultCustomerInd(1);

        doReturn(cmgCustomerResponseDTO).when(contractPriceProfCustomerRepository).fetchGFSCustomerDetailForCustomer(contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(target.isDefaultConceptCustomer(customerAssignmentDTO), equalTo(true));

        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailForCustomer(contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
    }

    @Test
    public void shouldReturnFalseIfNotDefaultCustomer() throws Exception {

        int contractPriceProfileSeq = 1010;
        String cmgCustomerId = "1000124";
        int cmgCustomerType = 31;

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        customerAssignmentDTO.setCmgCustomerId(cmgCustomerId);
        customerAssignmentDTO.setCmgCustomerType(cmgCustomerType);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setDefaultCustomerInd(0);

        doReturn(cmgCustomerResponseDTO).when(contractPriceProfCustomerRepository).fetchGFSCustomerDetailForCustomer(contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(target.isDefaultConceptCustomer(customerAssignmentDTO), equalTo(false));

        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailForCustomer(contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
    }

}
