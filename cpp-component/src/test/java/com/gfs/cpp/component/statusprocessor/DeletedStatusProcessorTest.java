package com.gfs.cpp.component.statusprocessor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeletedStatusProcessorTest {

    @InjectMocks
    @Spy
    private DeletedStatusProcessor target;

    @Mock
    private ContractPriceExpirer contractPriceExpirer;
    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldProcessDeleteActionByAgreementId() throws Exception {

        String agreementId = "agremementId";
        int contractPriceProfileSeq = -101;

        ContractPricingResponseDTO response = new ContractPricingResponseDTO();
        response.setContractPriceProfileSeq(contractPriceProfileSeq);
        response.setContractPriceProfStatusCode(ContractPriceProfileStatus.DRAFT.code);

        doReturn(response).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(agreementId);

        target.process(agreementId);

        verify(target).process(contractPriceProfileSeq, ContractPriceProfileStatus.DRAFT);
    }

    @Test
    public void shouldNotProcessDeleteActionWhenNoContractFoundByAgreementId() throws Exception {

        String agreementId = "agremementId";
        int contractPriceProfileSeq = -101;

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(agreementId);

        target.process(agreementId);

        verify(target, never()).process(eq(contractPriceProfileSeq), any(ContractPriceProfileStatus.class));
    }

    @Test
    public void shouldUpdateStatusToDeletedWhenCppStatusIsNotDeleted() throws Exception {
        int contractPriceProfileSeq = -101;

        target.process(contractPriceProfileSeq, ContractPriceProfileStatus.DRAFT);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(contractPriceProfileSeq,
                ContractPriceProfileStatus.DELETED.code, DeletedStatusProcessor.UPDATE_USER_ID);
    }

    @Test
    public void shouldExpirePriceIfCppStatusIsPricingActivated() throws Exception {
        int contractPriceProfileSeq = -101;

        target.process(contractPriceProfileSeq, ContractPriceProfileStatus.PRICING_ACTIVATED);

        verify(contractPriceExpirer).expirePriceForContract(contractPriceProfileSeq, DeletedStatusProcessor.UPDATE_USER_ID);
    }

    @Test
    public void shouldNotExpirePriceIfCppStatusIsNotPricingActivated() throws Exception {
        int contractPriceProfileSeq = -101;

        target.process(contractPriceProfileSeq, ContractPriceProfileStatus.DRAFT);

        verify(contractPriceExpirer, never()).expirePriceForContract(contractPriceProfileSeq, DeletedStatusProcessor.UPDATE_USER_ID);
    }

    @Test
    public void shouldNotUpdateStatusAndExpirePriceWhenCppStatusIsDeleted() throws Exception {

        int contractPriceProfileSeq = -101;

        target.process(contractPriceProfileSeq, ContractPriceProfileStatus.DELETED);

        verify(contractPriceExpirer, never()).expirePriceForContract(contractPriceProfileSeq, DeletedStatusProcessor.UPDATE_USER_ID);

        verify(contractPriceProfileRepository, never()).updateContractStatusWithLastUpdateUserIdByCppSeq(eq(contractPriceProfileSeq), anyInt(), anyString());

    }

}
