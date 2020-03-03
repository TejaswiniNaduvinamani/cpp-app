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
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractAction;
import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;
import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class ClmContractChangeProcessorTest {

    @InjectMocks
    private ClmContractChangeProcessor target;

    @Mock
    private PricingContractCreateProcessor pricingContractCreateProcessor;
    @Mock
    private ContractPriceProfileStatusMaintainer contractPriceProfileStatusMaintainer;
    @Mock
    private ClmApiProxy clmApiProxy;
    @Mock
    private DeletedStatusProcessor deletedStatusProcessor;
    @Mock
    private ContractPriceProfileDateSyncher contractPriceProfileDataSyncher;
    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Mock
    private ContractPriceProfileNameSyncher contractPriceProfileNameSyncher;

    @Test
    public void shouldProcessCreateActionReceived() throws Exception {

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAction(ClmContractAction.PUBLISHED.value);

        target.processChangeEvent(contractChangeEvent);

        verify(pricingContractCreateProcessor).process(contractChangeEvent);
    }

    @Test
    public void shouldProcessAmemdmentAction() throws Exception {

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAction(ClmContractAction.AMENDMENT_PUBLISHED.value);

        target.processChangeEvent(contractChangeEvent);

        verify(pricingContractCreateProcessor).process(contractChangeEvent);
    }

    @Test
    public void shouldSyncStatusWhenUpdated() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        String clmStatus = "Draft";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        int contractPriceProfileSeq = -1001;
        int contractPriceProfileId = -101;
        String contractName = "contract name";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractStatus(clmStatus);

        agreementData.setContractName(contractName);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDataInCpp.setContractPriceProfStatusCode(cppStatus.code);
        contractDataInCpp.setContractPriceProfileId(contractPriceProfileId);

        doReturn(contractDataInCpp).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        doReturn(agreementData).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.UPDATED.value);

        target.processChangeEvent(contractChangeEvent);

        verify(contractPriceProfileDataSyncher).synchClmContractDatesWithCpp(agreementData, contractDataInCpp, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);
        verify(contractPriceProfileNameSyncher).synchCLMContractNameWithCPPContractName(contractName, contractPriceProfileSeq, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);
        verify(contractPriceProfileStatusMaintainer).syncWithClmStatus(contractDataInCpp, clmStatus);
        verify(clmApiProxy).getAgreementData(agreementId, contractTypeName);
    }

    @Test
    public void shouldNotSyncStatusWhenUpdatedButPricingIsAlreadyActivated() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        String clmStatus = "Draft";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.PRICING_ACTIVATED;
        int contractPriceProfileSeq = -1001;
        int contractPriceProfileId = -101;
        String contractName = "contract name";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractStatus(clmStatus);

        agreementData.setContractName(contractName);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDataInCpp.setContractPriceProfStatusCode(cppStatus.code);
        contractDataInCpp.setContractPriceProfileId(contractPriceProfileId);

        doReturn(contractDataInCpp).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        doReturn(agreementData).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.UPDATED.value);

        target.processChangeEvent(contractChangeEvent);

        verify(contractPriceProfileDataSyncher, never()).synchClmContractDatesWithCpp(any(ClmContractResponseDTO.class),
                any(ContractPricingResponseDTO.class), eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileNameSyncher, never()).synchCLMContractNameWithCPPContractName(anyString(), anyInt(), eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileStatusMaintainer).syncWithClmStatus(contractDataInCpp, clmStatus);
        verify(clmApiProxy).getAgreementData(agreementId, contractTypeName);
    }

    @Test
    public void shouldNotSyncStatusWhenNoContractCreatedInCpp() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        String clmStatus = "Draft";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractStatus(clmStatus);

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        doReturn(agreementData).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.UPDATED.value);

        target.processChangeEvent(contractChangeEvent);

        verify(contractPriceProfileDataSyncher, never()).synchClmContractDatesWithCpp(any(ClmContractResponseDTO.class),
                any(ContractPricingResponseDTO.class),eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileNameSyncher, never()).synchCLMContractNameWithCPPContractName(anyString(), anyInt(),eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileStatusMaintainer, never()).syncWithClmStatus(any(ContractPricingResponseDTO.class), any(String.class));
        verify(clmApiProxy, never()).getAgreementData(agreementId, contractTypeName);
    }

    @Test
    public void shouldSyncStatusWhenSentForApproval() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        String clmStatus = "Sent For Approval";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        int contractPriceProfileSeq = -1001;

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractStatus(clmStatus);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDataInCpp.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(contractDataInCpp).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        doReturn(agreementData).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.SENT_FOR_APPROVAL.value);

        target.processChangeEvent(contractChangeEvent);

        verify(contractPriceProfileDataSyncher, never()).synchClmContractDatesWithCpp(any(ClmContractResponseDTO.class),
                any(ContractPricingResponseDTO.class), eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileNameSyncher, never()).synchCLMContractNameWithCPPContractName(anyString(), anyInt(), eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileStatusMaintainer).syncWithClmStatus(contractDataInCpp, clmStatus);
        verify(clmApiProxy).getAgreementData(agreementId, contractTypeName);
    }

    @Test
    public void shouldSyncStatusWhenApproved() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        String clmStatus = "Approved";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        int contractPriceProfileSeq = -1001;

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractStatus(clmStatus);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDataInCpp.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(contractDataInCpp).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        doReturn(agreementData).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.APPROVED.value);

        target.processChangeEvent(contractChangeEvent);

        verify(contractPriceProfileDataSyncher, never()).synchClmContractDatesWithCpp(any(ClmContractResponseDTO.class),
                any(ContractPricingResponseDTO.class), eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileStatusMaintainer).syncWithClmStatus(contractDataInCpp, clmStatus);
        verify(clmApiProxy).getAgreementData(agreementId, contractTypeName);
    }

    @Test
    public void shouldSyncStatusWhenTerminated() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        String clmStatus = "Approved";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        int contractPriceProfileSeq = -1001;

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractStatus(clmStatus);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDataInCpp.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(contractDataInCpp).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        doReturn(agreementData).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.TERMINATED.value);

        target.processChangeEvent(contractChangeEvent);

        verify(contractPriceProfileDataSyncher, never()).synchClmContractDatesWithCpp(any(ClmContractResponseDTO.class),
                any(ContractPricingResponseDTO.class), eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(contractPriceProfileStatusMaintainer).syncWithClmStatus(contractDataInCpp, clmStatus);
        verify(clmApiProxy).getAgreementData(agreementId, contractTypeName);
    }

    @Test
    public void shouldProcessDeletedStatusWhenDeletedActionIsReceived() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        contractChangeEvent.setAction(ClmContractAction.DELETED.value);

        doReturn(new ContractPricingResponseDTO()).when(contractPriceProfileRepository)
                .fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        target.processChangeEvent(contractChangeEvent);

        verify(deletedStatusProcessor).process(agreementId);
        verify(clmApiProxy, never()).getAgreementData(anyString(), anyString());
    }

    @Test
    public void shouldProcessAmendmentDeletedStatusWhenDeletedActionIsReceived() throws Exception {
        String agreementId = "agreementId";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);

        contractChangeEvent.setAction(ClmContractAction.AMENDMENT_DELETED.value);

        doReturn(new ContractPricingResponseDTO()).when(contractPriceProfileRepository)
                .fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        target.processChangeEvent(contractChangeEvent);

        verify(deletedStatusProcessor).process(agreementId);
        verify(clmApiProxy, never()).getAgreementData(eq(agreementId), any(String.class));
    }

    @Test
    public void shouldNotProcessDeletedStatusWhenNoContractCreatedInCpp() throws Exception {
        String agreementId = "agreementId";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);

        contractChangeEvent.setAction(ClmContractAction.DELETED.value);

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        target.processChangeEvent(contractChangeEvent);

        verify(deletedStatusProcessor, never()).process(anyString());
        verify(clmApiProxy, never()).getAgreementData(eq(agreementId), any(String.class));
    }

    @Test
    public void shouldDoNothingWhenNotMonitoredActionPerformed() throws Exception {
        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        String contractStatus = "Draft";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractStatus(contractStatus);

        doReturn(agreementData).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        contractChangeEvent.setAction("Download");

        target.processChangeEvent(contractChangeEvent);

        verify(pricingContractCreateProcessor, never()).process(any(ClmContractChangeEventDTO.class));
        verify(contractPriceProfileStatusMaintainer, never()).syncWithClmStatus(any(ContractPricingResponseDTO.class), anyString());
    }

    @Test
    public void shouldUpdateFrutheranceUrlWhenExecutedActionReceived() throws Exception {

        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        int contractPriceProfileSeq = -1001;

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.EXECUTED.value);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractDataInCpp.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(new ClmContractResponseDTO()).when(clmApiProxy).getAgreementData(agreementId, contractTypeName);
        doReturn(contractDataInCpp).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        target.processChangeEvent(contractChangeEvent);

        verify(clmApiProxy).updateFurtheranceUrlForPricingContract(agreementId, contractTypeName);

    }

    @Test
    public void shouldNotUpdateFrutheranceUrlWhenExecutedActionReceivedButContractNotInCpp() throws Exception {

        String agreementId = "agreementId";
        String contractTypeName = "ICMDistributionAgreementRegional";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractTypeName);
        contractChangeEvent.setAction(ClmContractAction.EXECUTED.value);

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());

        target.processChangeEvent(contractChangeEvent);

        verify(clmApiProxy, never()).updateFurtheranceUrlForPricingContract(any(String.class), any(String.class));

    }

}
