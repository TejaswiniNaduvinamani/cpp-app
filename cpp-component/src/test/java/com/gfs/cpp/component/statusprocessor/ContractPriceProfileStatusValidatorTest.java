package com.gfs.cpp.component.statusprocessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractPriceProfileStatusValidatorTest {

    @InjectMocks
    private ContractPriceProfileStatusValidator target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldValidatePriceProfileStatusThrowCppExceptionWhenContractNotInDraftStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        try {
            target.validateIfContractPricingEditableStatus(contractPriceProfileSeq);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidatePriceProfileStatusWhenContractInDraftStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.DRAFT.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        target.validateIfContractPricingEditableStatus(contractPriceProfileSeq);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidateCustomerEditableThrowCppExceptionWhenContractNotInApprovedStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        try {
            target.validateIfCustomerAssignmentEditableStatus(contractPriceProfileSeq);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidateCustomerAssignmentStatusWhenContractInApprovedStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        target.validateIfCustomerAssignmentEditableStatus(contractPriceProfileSeq);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidateCostModelThrowCppExceptionWhenContractNotInApprovedStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        try {
            target.validateIfCostModelEditableStatus(contractPriceProfileSeq);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidateCostModelStatusWhenContractInApprovedStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        target.validateIfCostModelEditableStatus(contractPriceProfileSeq);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidateItemEditableThrowCppExceptionWhenContractNotInApprovedStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.DRAFT.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        try {
            target.validateIfItemAssignmentEditableStatus(contractPriceProfileSeq);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldValidateItemAssignmentStatusWhenContractInApprovedStatus() throws Exception {

        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricing = new ContractPricingResponseDTO();
        contractPricing.setContractPriceProfStatusCode(ContractPriceProfileStatus.CONTRACT_APPROVED.code);

        doReturn(contractPricing).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        target.validateIfItemAssignmentEditableStatus(contractPriceProfileSeq);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldThrowCppExceptionWhenContractNotFound() throws Exception {

        int contractPriceProfileSeq = -1001;

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        try {
            target.validateIfContractPricingEditableStatus(contractPriceProfileSeq);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NO_CONTRACT_FOUND));
        }

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

}
