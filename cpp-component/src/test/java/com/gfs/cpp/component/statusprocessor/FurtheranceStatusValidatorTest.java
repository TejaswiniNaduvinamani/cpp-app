package com.gfs.cpp.component.statusprocessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceStatusValidatorTest {

    @InjectMocks
    private FurtheranceStatusValidator target;

    @Mock
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private ClmApiProxy clmApiProxy;

    @Mock
    private CppUserDetailsService cppUserDetailsService;

    @Test
    public void shouldValidateFurtheranceReturnTrueWhenAllValidationIsSuccessful() {
        String parentAgreementId = "parent-agreement-id";
        String contractType = "test";
        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractStatus(ClmContractStatus.EXECUTED.value);

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setContractPriceProfStatusCode(50);
        contractPricingResponseDTO.setContractPriceProfileSeq(1);
        contractPricingResponseDTO.setClmAgreementId(parentAgreementId);

        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(true);
        when(contractPriceProfileRepository.fetchInProgressContractVersionCount(parentAgreementId)).thenReturn(0);
        when(clmApiProxy.getAgreementData(parentAgreementId, contractType)).thenReturn(clmContractResponseDTO);

        target.validateNewFurtheranceCreation(parentAgreementId, contractType);

        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchInProgressContractVersionCount(parentAgreementId);
        verify(clmApiProxy).getAgreementData(parentAgreementId, contractType);

    }

    @Test
    public void shouldThrowExceptionWhenNoContractVersionInProgress() {
        String parentAgreementId = "parent-agreement-id";
        String contractType = "test";
        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractStatus(ClmContractStatus.EXECUTED.value);

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setContractPriceProfStatusCode(50);
        contractPricingResponseDTO.setContractPriceProfileSeq(1);
        contractPricingResponseDTO.setClmAgreementId(parentAgreementId);

        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(true);
        when(contractPriceProfileRepository.fetchInProgressContractVersionCount(parentAgreementId)).thenReturn(1);
        when(clmApiProxy.getAgreementData(parentAgreementId, contractType)).thenReturn(clmContractResponseDTO);

        try {
            target.validateNewFurtheranceCreation(parentAgreementId, contractType);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.CANNOT_CREATE_FURTHERANCE));
            assertThat(ex.getMessage(), equalTo("No contract versions are inprogress state."));
        }

        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchInProgressContractVersionCount(parentAgreementId);

    }

    @Test
    public void shouldThrowExceptionWhenUserHasNoEditAccessToFurtherance() {
        String parentAgreementId = "parent-agreement-id";
        String contractType = "test";
        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractStatus(ClmContractStatus.EXECUTED.value);

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setContractPriceProfStatusCode(50);
        contractPricingResponseDTO.setContractPriceProfileSeq(1);
        contractPricingResponseDTO.setClmAgreementId(parentAgreementId);

        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(false);
        when(contractPriceProfileRepository.fetchInProgressContractVersionCount(parentAgreementId)).thenReturn(0);
        when(clmApiProxy.getAgreementData(parentAgreementId, contractType)).thenReturn(clmContractResponseDTO);

        try {
            target.validateNewFurtheranceCreation(parentAgreementId, contractType);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.CANNOT_CREATE_FURTHERANCE));
            assertThat(ex.getMessage(), equalTo("User doesn't have edit access."));
        }

        verify(cppUserDetailsService).hasContractEditAccess();

    }

    @Test
    public void shouldThrowExceptionWhenCLMContractStatusIsNotExecuted() {
        String parentAgreementId = "parent-agreement-id";
        String contractType = "test";
        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractStatus(ClmContractStatus.DRAFT.value);

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setContractPriceProfStatusCode(50);
        contractPricingResponseDTO.setContractPriceProfileSeq(1);
        contractPricingResponseDTO.setClmAgreementId(parentAgreementId);

        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(true);
        when(contractPriceProfileRepository.fetchInProgressContractVersionCount(parentAgreementId)).thenReturn(0);
        when(clmApiProxy.getAgreementData(parentAgreementId, contractType)).thenReturn(clmContractResponseDTO);

        try {
            target.validateNewFurtheranceCreation(parentAgreementId, contractType);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.CANNOT_CREATE_FURTHERANCE));
            assertThat(ex.getMessage(), equalTo("CLM status is not executed."));
        }

        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchInProgressContractVersionCount(parentAgreementId);
        verify(clmApiProxy).getAgreementData(parentAgreementId, contractType);

    }

    @Test
    public void shouldValidateIfPricingCanBeActivated() {
        String clmContractStatus = ClmContractStatus.EXECUTED.value;
        Date pricingExpirationDate = new LocalDate(2018, 01, 01).toDate();
        Date currentDate = new LocalDate(2018, 01, 01).toDate();
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_SAVED.code);

        try {
            target.validateIfPricingCanBeActivated(pricingExpirationDate, clmContractStatus, currentDate, furtheranceInformationDTO);
        } catch (CPPRuntimeException ex) {
            fail("No run time exception occured");
        }

    }

    @Test
    public void shouldValidateIfActivatePricingCanBeEnabledForFurtherance() {
        String clmContractStatus = ClmContractStatus.EXECUTED.value;
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_SAVED.code);

        try {
            target.validateIfActivatePricingCanBeEnabledForFurtherance(furtheranceInformationDTO, clmContractStatus);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
        }

    }

    @Test
    public void shouldValidateIfPricingCanBeActivatedExceptionTest() {
        String clmContractStatus = ClmContractStatus.APPROVED.value;
        Date pricingExpirationDate = new LocalDate(2018, 01, 01).toDate();
        Date currentDate = new LocalDate(2018, 01, 01).toDate();
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_SAVED.code);
        try {
            target.validateIfPricingCanBeActivated(pricingExpirationDate, clmContractStatus, currentDate, furtheranceInformationDTO);
            fail("Expected expection during this call");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
        }

    }

    @Test
    public void shouldValidateCLMStatusIfExecuted() {
        String clmContractStatus = ClmContractStatus.EXECUTED.value;
        try {
            target.validateCLMStatus(clmContractStatus);
        } catch (CPPRuntimeException e) {
            fail("No run time exception occured");
        }
    }

    @Test
    public void shouldValidateCLMStatusIfSuperseded() {
        String clmContractStatus = ClmContractStatus.SUPERSEDED.value;
        try {
            target.validateCLMStatus(clmContractStatus);
        } catch (CPPRuntimeException e) {
            fail("No run time exception occured");
        }
    }

    @Test
    public void shouldValidateCLMStatusExceptionTest() {
        String clmContractStatus = ClmContractStatus.APPROVED.value;
        try {
            target.validateCLMStatus(clmContractStatus);
            fail("Expected expection during this call");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
            assertThat(ex.getMessage(), equalTo("The contract is not ready for activating price. Its CLM status is " + clmContractStatus
                    + ". Pricing can be activated only once the CLM status is EXECUTED."));
        }
    }

    @Test
    public void shouldValidateIfContractExpired() {
        try {
            Date pricingExpirationDate = new LocalDate(2999, 01, 01).toDate();
            target.validateIfContractExpired(pricingExpirationDate, new Date());
        } catch (CPPRuntimeException e) {
            fail("No run time exception occured");
        }
    }

    @Test
    public void shouldValidateIfContractExpiredExceptionTest() {
        try {
            Date pricingExpirationDate = new LocalDate(2018, 01, 01).toDate();
            Date currentDate = new LocalDate(2018, 02, 01).toDate();
            target.validateIfContractExpired(pricingExpirationDate, currentDate);
            fail("Expected expection during this call");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
            assertThat(ex.getMessage(), equalTo("The expiration date of the contract is in past. It cannot be activated."));
        }
    }

    @Test
    public void shouldValidateFurtheranceStatus() {
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_SAVED.code);
        try {
            target.validateFurtheranceStatus(furtheranceInformationDTO);
        } catch (CPPRuntimeException e) {
            fail("No run time exception occured");
        }
    }

    @Test
    public void shouldValidateFurtheranceStatusExceptionTest() {
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.code);
        try {
            target.validateFurtheranceStatus(furtheranceInformationDTO);
            fail("No run time exception occured");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_FURTHERANCE_STATUS));
            assertThat(ex.getMessage(), equalTo("Furtherance is in In-Progress. It has to be saved before pricing can be activated"));

        }
    }

    @Test
    public void shouldValidateFurtheranceEditable() {

        int contractPriceProfileSeq = 1;
        int cppFurtheranceSeq = 1;
        when(cppFurtheranceRepository.fetchCPPFurtheranceStatus(contractPriceProfileSeq, cppFurtheranceSeq)).thenReturn(1);
        try {
            target.validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);
        } catch (CPPRuntimeException e) {
            fail("No run time exception occured");
        }

        verify(cppFurtheranceRepository).fetchCPPFurtheranceStatus(contractPriceProfileSeq, cppFurtheranceSeq);
    }

    @Test
    public void shouldValidateFurtheranceEditableExceptionTest() {
        int contractPriceProfileSeq = 1;
        int cppFurtheranceSeq = 1;
        when(cppFurtheranceRepository.fetchCPPFurtheranceStatus(contractPriceProfileSeq, cppFurtheranceSeq)).thenReturn(null);
        try {
            target.validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);
            fail("No run time exception occured");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
            assertThat(ex.getMessage(), equalTo("Furtherance not in editable status"));

        }
        verify(cppFurtheranceRepository).fetchCPPFurtheranceStatus(contractPriceProfileSeq, cppFurtheranceSeq);
    }
}
