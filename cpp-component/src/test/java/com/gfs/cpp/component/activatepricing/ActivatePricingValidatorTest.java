package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.activatepricing.ActivatePricingValidator;
import com.gfs.cpp.component.activatepricing.ContractCustomerMappingService;
import com.gfs.cpp.component.assignment.CustomerHierarchyValidator;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class ActivatePricingValidatorTest {

    @InjectMocks
    private ActivatePricingValidator target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private CustomerHierarchyValidator customerHierarchyValidator;

    @Captor
    private ArgumentCaptor<List<RealCustomerDTO>> realCustomersCaptor;

    @Mock
    ContractCustomerMappingService contractCustomerMappingService;

    @Mock
    private ClmContractTypeRepository clmContractTypeRepository;

    @Mock
    private ClmApiProxy clmApiProxy;

    @Test
    public void shouldValidateContractExceptionTest() {
        int contractPriceProfileSeq = 1;
        boolean isAmendment = true;
        String clmContractStatusForAmendment = ClmContractStatus.EXECUTED.value;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDetails.setContractPriceProfStatusCode(40);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 01).toDate());

        when(contractCustomerMappingService.fetchUnmappedConceptCount(contractPriceProfileSeq)).thenReturn(1);
        Date currentDate = new Date();
        try {
            target.validateContract(contractPriceProfileSeq, contractDetails, currentDate, isAmendment, clmContractStatusForAmendment);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
            assertThat(ex.getMessage(),
                    equalTo("The contract is not ready for price activation. There are concepts on the contract without valid mapping(s)."));
        }

        verify(contractCustomerMappingService).fetchUnmappedConceptCount(contractPriceProfileSeq);
    }

    @Test
    public void shouldValidateContractForPriceActivation() {
        int contractPriceProfileSeq = 1;
        boolean isAmendment = true;
        String clmContractStatusForAmendment = ClmContractStatus.EXECUTED.value;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setClmContractEndDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setContractPriceProfStatusCode(40);

        when(contractCustomerMappingService.fetchUnmappedConceptCount(contractPriceProfileSeq)).thenReturn(0);
        Date currentDate = new Date();

        target.validateContract(contractPriceProfileSeq, contractDetails, currentDate, isAmendment, clmContractStatusForAmendment);

        verify(contractCustomerMappingService).fetchUnmappedConceptCount(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidateContract() {
        int contractPriceProfileSeq = 1;
        boolean isAmendment = true;
        String clmContractStatusForAmendment = ClmContractStatus.EXECUTED.value;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDetails.setContractPriceProfStatusCode(40);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setClmContractEndDate(new LocalDate(2018, 01, 01).toDate());

        when(contractCustomerMappingService.fetchUnmappedConceptCount(contractPriceProfileSeq)).thenReturn(0);
        Date currentDate = new Date();
        try {
            target.validateContract(contractPriceProfileSeq, contractDetails, currentDate, isAmendment, clmContractStatusForAmendment);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
            assertThat(ex.getMessage(), equalTo("The expiration date of the contract is in past. It cannot be activated."));
        }
        verify(contractCustomerMappingService).fetchUnmappedConceptCount(contractPriceProfileSeq);
    }

    @Test
    public void shouldValidateContractExceptionForNotApprovedContract() {
        int contractPriceProfileSeq = 1;
        boolean isAmendment = true;
        String clmContractStatusForAmendment = ClmContractStatus.EXECUTED.value;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfStatusCode(ContractPriceProfileStatus.DRAFT.getCode());
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setClmContractEndDate(new LocalDate(2018, 01, 01).toDate());

        Date currentDate = new Date();
        try {
            target.validateContract(contractPriceProfileSeq, contractDetails, currentDate, isAmendment, clmContractStatusForAmendment);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
            assertThat(ex.getMessage(),
                    equalTo("The contract is not ready for activating price. It is in " + ContractPriceProfileStatus.DRAFT.getDesc() + " status."));
        }
    }

    @Test
    public void shouldValidateActivatePricingButton() {
        int contractPriceProfileSeq = 1;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setClmContractEndDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setContractPriceProfStatusCode(40);

        when(contractCustomerMappingService.fetchUnmappedConceptCount(contractPriceProfileSeq)).thenReturn(0);
        when(cppDateUtils.getCurrentDate()).thenReturn(new LocalDate(2018, 01, 01).toDate());
     
        target.validateIfActivatePricingCanBeEnabled(contractPriceProfileSeq, contractDetails);

        verify(contractCustomerMappingService).fetchUnmappedConceptCount(contractPriceProfileSeq);
    }

    @Test
    public void validateIfActivatePricingCanBeEnabledMappedException() {
        int contractPriceProfileSeq = 1;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setContractPriceProfStatusCode(40);

        when(contractCustomerMappingService.fetchUnmappedConceptCount(contractPriceProfileSeq)).thenReturn(1);

        try {
            target.validateIfActivatePricingCanBeEnabled(contractPriceProfileSeq, contractDetails);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException cpp) {
            assertThat(cpp.getErrorCode(), equalTo(CPPExceptionType.INVALID_CONTRACT.getErrorCode()));
            assertThat(cpp.getMessage(),
                    equalTo("The contract is not ready for price activation. There are concepts on the contract without valid mapping(s)."));
        }
        verify(contractCustomerMappingService).fetchUnmappedConceptCount(contractPriceProfileSeq);
    }

    @Test
    public void validateIfActivatePricingCanBeEnabledThrowExpiredContractException() {
        int contractPriceProfileSeq = 1;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setContractPriceProfStatusCode(40);

        when(contractCustomerMappingService.fetchUnmappedConceptCount(contractPriceProfileSeq)).thenReturn(1);
        try {
            target.validateIfActivatePricingCanBeEnabled(contractPriceProfileSeq, contractDetails);
        } catch (CPPRuntimeException cpp) {
            assertThat(cpp.getErrorCode(), equalTo(CPPExceptionType.INVALID_CONTRACT.getErrorCode()));
            assertThat(cpp.getMessage(),
                    equalTo("The contract is not ready for price activation. There are concepts on the contract without valid mapping(s)."));
        }
        verify(contractCustomerMappingService).fetchUnmappedConceptCount(contractPriceProfileSeq);

    }

    @Test
    public void shouldNotThrowExceptionWhenAllCustomersAreMemberOfHierarchy() throws Exception {

        int contractPriceProfileSeq = 101;
        String realCustomerException = "real customer exception";
        int realCustomerExpType = 3;
        String realCustomerDefault = "real customer default";
        int realCustomerExpDefault = 18;

        ContractCustomerMappingDTO realCustomerExceptionMapping = new ContractCustomerMappingDTO();
        realCustomerExceptionMapping.setGfsCustomerId(realCustomerException);
        realCustomerExceptionMapping.setGfsCustomerTypeCode(realCustomerExpType);

        ContractCustomerMappingDTO realCustomerDefaultMapping = new ContractCustomerMappingDTO();
        realCustomerDefaultMapping.setGfsCustomerId(realCustomerDefault);
        realCustomerDefaultMapping.setGfsCustomerTypeCode(realCustomerExpDefault);
        realCustomerDefaultMapping.setDefaultCustomerInd(1);

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = new ArrayList<>();
        contractCustomerMappingDTOList.add(realCustomerDefaultMapping);
        contractCustomerMappingDTOList.add(realCustomerExceptionMapping);
        doReturn(Collections.EMPTY_LIST).when(customerHierarchyValidator).findCustomersNotAMember(eq(contractPriceProfileSeq),
                realCustomersCaptor.capture());

        target.validateCustomerMembershipWithDefaultCustomerMapping(contractPriceProfileSeq, contractCustomerMappingDTOList);

        verify(customerHierarchyValidator).findCustomersNotAMember(eq(contractPriceProfileSeq), realCustomersCaptor.capture());

        List<RealCustomerDTO> actualCustomers = realCustomersCaptor.getValue();

        assertThat(actualCustomers.size(), equalTo(1));
        assertThat(actualCustomers.get(0).getRealCustomerId(), equalTo(realCustomerException));
        assertThat(actualCustomers.get(0).getRealCustomerType(), equalTo(realCustomerExpType));
    }

    @Test
    public void shouldThrowExceptionForCLMStatusIsNotExecuted() {
        boolean isAmendment = true;
        String contractStatus = ClmContractStatus.DRAFT.value;
        try {
            target.validateCLMStatusForAmendment(isAmendment, contractStatus);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
            assertThat(ex.getMessage(), equalTo("The contract is not ready for activating price. Its CLM status " + contractStatus
                    + ". Pricing can be activated only once the CLM status is EXECUTED."));
        }
    }

    @Test
    public void shouldNotThrowExceptionForCLMStatusExecuted() {
        boolean isAmendment = true;
        target.validateCLMStatusForAmendment(isAmendment, ClmContractStatus.EXECUTED.value);
    }

    @Test
    public void shouldNotThrowExceptionForCLMStatusSuperseded() {
        boolean isAmendment = true;
        target.validateCLMStatusForAmendment(isAmendment, ClmContractStatus.SUPERSEDED.value);
    }

    @Test
    public void shouldNotThrowExceptionForNotAnAmendment() {
        boolean isAmendment = false;
        target.validateCLMStatusForAmendment(isAmendment, ClmContractStatus.EXECUTED.value);
    }

    @Test
    public void shouldThrowCustomerNotAMemberExceptionWhenNoMemberCustomerFound() throws Exception {

        int contractPriceProfileSeq = 101;
        String realCustomerException = "real customer exception";
        int realCustomerExpType = 3;
        String realCustomerDefault = "real customer default";
        int realCustomerExpDefault = 18;

        ContractCustomerMappingDTO realCustomerExceptionMapping = new ContractCustomerMappingDTO();
        realCustomerExceptionMapping.setGfsCustomerId(realCustomerException);
        realCustomerExceptionMapping.setGfsCustomerTypeCode(realCustomerExpType);

        ContractCustomerMappingDTO realCustomerDefaultMapping = new ContractCustomerMappingDTO();
        realCustomerDefaultMapping.setGfsCustomerId(realCustomerDefault);
        realCustomerDefaultMapping.setGfsCustomerTypeCode(realCustomerExpDefault);
        realCustomerDefaultMapping.setDefaultCustomerInd(1);

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = new ArrayList<>();
        contractCustomerMappingDTOList.add(realCustomerDefaultMapping);
        contractCustomerMappingDTOList.add(realCustomerExceptionMapping);
        doReturn(Collections.singletonList(new RealCustomerDTO())).when(customerHierarchyValidator)
                .findCustomersNotAMember(eq(contractPriceProfileSeq), realCustomersCaptor.capture());

        try {
            target.validateCustomerMembershipWithDefaultCustomerMapping(contractPriceProfileSeq, contractCustomerMappingDTOList);
            fail("expected exception");
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.NOT_A_MEMBER_OF_DEFAULT_CUSTOMER));
            assertThat(ce.getMessage(), equalTo("Please ensure all customers assigned to the concepts belong to the same customer hierarchy."));
        }

        verify(customerHierarchyValidator).findCustomersNotAMember(eq(contractPriceProfileSeq), realCustomersCaptor.capture());

        List<RealCustomerDTO> actualCustomers = realCustomersCaptor.getValue();

        assertThat(actualCustomers.size(), equalTo(1));
        assertThat(actualCustomers.get(0).getRealCustomerId(), equalTo(realCustomerException));
        assertThat(actualCustomers.get(0).getRealCustomerType(), equalTo(realCustomerExpType));
    }

}
