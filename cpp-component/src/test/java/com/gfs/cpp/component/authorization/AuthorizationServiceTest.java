package com.gfs.cpp.component.authorization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.service.authorization.AuthorizationDetailsDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.authorization.AuthorizationService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

    private static final int CONTRACT_PRICE_PROFILE_SEQ = -101;

    @InjectMocks
    private AuthorizationService target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Mock
    private CppUserDetailsService cppUserDetailsService;
    @Mock
    private CPPDateUtils cppDateUtils;
    @Mock
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Test
    public void shouldSetCppStatus() throws Exception {

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.code);

        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false,
                ClmContractStatus.WAITING_FOR_APPROVAL.value);

        assertThat(actual.getCppStatus(), equalTo(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.desc));
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(false));
        assertThat(actual.isPriceProfileEditable(), equalTo(false));
        assertThat(actual.getIsPowerUser(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppDateUtils).getCurrentDateAsLocalDate();

    }

    @Test
    public void shouldSetCppStatusToDraftWhenNoContractPricingInfoFound() throws Exception {

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false, ClmContractStatus.DRAFT.value);

        assertThat(actual.getCppStatus(), equalTo(ContractPriceProfileStatus.DRAFT.desc));
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(false));
        assertThat(actual.isPriceProfileEditable(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
    }

    @Test
    public void shouldSetProfileEditableTrueWhenUserHasCreateContractAccessAndStatusIsDraft() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.DRAFT.code);

        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false, ClmContractStatus.DRAFT.value);
        assertThat(actual.isPriceProfileEditable(), equalTo(true));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).hasContractEditAccess();
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }

    @Test
    public void shouldSetProfileEditableFalseWhenUserHasContractCreateAccessAndStatusIsWaitingForApproval() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false,
                ClmContractStatus.WAITING_FOR_APPROVAL.value);
        assertThat(actual.isPriceProfileEditable(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).hasContractEditAccess();
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }

    @Test
    public void shouldSetProfileEditableFalseWhenUserHasNoContractCreateAccessAndStatusIsDraft() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.DRAFT.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        doReturn(false).when(cppUserDetailsService).hasContractEditAccess();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false, ClmContractStatus.DRAFT.value);
        assertThat(actual.isPriceProfileEditable(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).hasContractEditAccess();
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }

    @Test
    public void shouldSetProfileEditableFalseWhenUserHasContractCreateAccesAndStatusIsAproved() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false, ClmContractStatus.APPROVED.value);
        assertThat(actual.isPriceProfileEditable(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).hasContractEditAccess();
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }

    @Test
    public void shouldSetAssignmentEditableTrueWhenUserIsPowerUserAndStatusIsContractApproved() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(false).when(cppUserDetailsService).isAccountManagerUser();
        doReturn(true).when(cppUserDetailsService).isPowerUser();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false, ClmContractStatus.APPROVED.value);
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(true));
        assertThat(actual.isItemAssignmentEditable(), equalTo(true));
        assertThat(actual.isCostModelEditable(), equalTo(true));

        assertThat(actual.getIsPowerUser(), equalTo(true));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).isPowerUser();
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }

    @Test
    public void shouldSetAssignmentEditableFalseWhenUserIsPowerUserAndStatusIsWaitingForApproval() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(false).when(cppUserDetailsService).isAccountManagerUser();
        doReturn(true).when(cppUserDetailsService).isPowerUser();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false,
                ClmContractStatus.WAITING_FOR_APPROVAL.value);
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(false));
        assertThat(actual.isItemAssignmentEditable(), equalTo(false));
        assertThat(actual.isCostModelEditable(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).isPowerUser();
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }

    @Test
    public void shouldSetAssignmentEditableFalseWhenUserIsNotPowerUserAndStatusIsContractApproved() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(true).when(cppUserDetailsService).isAccountManagerUser();
        doReturn(false).when(cppUserDetailsService).isPowerUser();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false, ClmContractStatus.APPROVED.value);
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(false));
        assertThat(actual.isItemAssignmentEditable(), equalTo(false));
        assertThat(actual.isCostModelEditable(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).isPowerUser();
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }

    @Test
    public void shouldSetAssignmentEditableTrueWhenContractIsExecutedForAmendment() throws Exception {

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(false).when(cppUserDetailsService).isAccountManagerUser();
        doReturn(true).when(cppUserDetailsService).isPowerUser();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, true, ClmContractStatus.EXECUTED.value);
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(true));
        assertThat(actual.isItemAssignmentEditable(), equalTo(true));
        assertThat(actual.isCostModelEditable(), equalTo(true));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).isPowerUser();
        verify(cppDateUtils).getCurrentDateAsLocalDate();

    }

    @Test
    public void shouldSetAssignmentEditableTrueWhenContractIsSupersededForAmendment() throws Exception {

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(false).when(cppUserDetailsService).isAccountManagerUser();
        doReturn(true).when(cppUserDetailsService).isPowerUser();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, true, ClmContractStatus.SUPERSEDED.value);
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(true));
        assertThat(actual.isItemAssignmentEditable(), equalTo(true));
        assertThat(actual.isCostModelEditable(), equalTo(true));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).isPowerUser();
        verify(cppDateUtils).getCurrentDateAsLocalDate();

    }

    @Test
    public void shouldSetAssignmentEditableFalseWhenContractInClmNotExecutedForAmendment() throws Exception {

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);
        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(false).when(cppUserDetailsService).isAccountManagerUser();
        doReturn(true).when(cppUserDetailsService).isPowerUser();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        AuthorizationDetailsDTO actual = target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, true, ClmContractStatus.APPROVED.value);
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(false));
        assertThat(actual.isItemAssignmentEditable(), equalTo(false));
        assertThat(actual.isCostModelEditable(), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).isPowerUser();
        verify(cppDateUtils).getCurrentDateAsLocalDate();

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowCPPRuntimeExceptionWhenContractIsExpired() throws Exception {

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate().minusDays(1).toDate());
        contractPricingResponseDTO.setContractPriceProfStatusCode(ContractPriceProfileStatus.DRAFT.code);

        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(new LocalDate()).when(cppDateUtils).getCurrentDateAsLocalDate();

        target.buildAuthorizationDetails(CONTRACT_PRICE_PROFILE_SEQ, false, ClmContractStatus.DRAFT.value);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(CONTRACT_PRICE_PROFILE_SEQ);
        verify(cppUserDetailsService).hasContractEditAccess();
        verify(cppDateUtils).getCurrentDateAsLocalDate();

    }

    @Test
    public void canEditFurtheranceShouldReturnTrueWhenNoFurthernaceFoundButUserHasAccess() throws Exception {

        int furtheranceSeq = -301;

        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();

        assertThat(target.canEditFurtherance(furtheranceSeq), equalTo(true));

    }

    @Test
    public void canEditFurtheranceShouldReturnTrueWhenFurtheranceInInitatedState() throws Exception {

        int furtheranceSeq = -301;
        FurtheranceInformationDTO furtheranceInformationDto = new FurtheranceInformationDTO();
        furtheranceInformationDto.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.code);

        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(furtheranceInformationDto).when(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(furtheranceSeq);

        assertThat(target.canEditFurtherance(furtheranceSeq), equalTo(true));

    }

    @Test
    public void canEditFurtheranceShouldReturnTrueWhenFurtheranceSavedState() throws Exception {

        int furtheranceSeq = -301;
        FurtheranceInformationDTO furtheranceInformationDto = new FurtheranceInformationDTO();
        furtheranceInformationDto.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_SAVED.code);

        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(furtheranceInformationDto).when(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(furtheranceSeq);

        assertThat(target.canEditFurtherance(furtheranceSeq), equalTo(true));

    }

    @Test
    public void canEditFurtheranceShouldReturnFalseWhenFurtheranceActivatedState() throws Exception {

        int furtheranceSeq = -301;
        FurtheranceInformationDTO furtheranceInformationDto = new FurtheranceInformationDTO();
        furtheranceInformationDto.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_ACTIVATED.code);

        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(furtheranceInformationDto).when(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(furtheranceSeq);

        assertThat(target.canEditFurtherance(furtheranceSeq), equalTo(false));

    }

    @Test
    public void canEditFurtheranceShouldReturnFalseWhenFurtheranceInInitatedStateButUserHasNoEdit() throws Exception {

        int furtheranceSeq = -301;
        FurtheranceInformationDTO furtheranceInformationDto = new FurtheranceInformationDTO();
        furtheranceInformationDto.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.code);

        doReturn(false).when(cppUserDetailsService).hasContractEditAccess();
        doReturn(furtheranceInformationDto).when(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(furtheranceSeq);

        assertThat(target.canEditFurtherance(furtheranceSeq), equalTo(false));

    }

    @Test
    public void isUserAuthroizeToViewOnlyReturnTrueWhenUserHasNoAccessToEdit() throws Exception {

        doReturn(false).when(cppUserDetailsService).hasContractEditAccess();

        assertThat(target.isUserAuthorizedToViewOnly(), equalTo(true));

    }
    
    @Test
    public void isUserAuthroizeToViewOnlyReturnFalseWhenUserHasNoAccessToEdit() throws Exception {

        doReturn(true).when(cppUserDetailsService).hasContractEditAccess();

        assertThat(target.isUserAuthorizedToViewOnly(), equalTo(false));

    }

}
