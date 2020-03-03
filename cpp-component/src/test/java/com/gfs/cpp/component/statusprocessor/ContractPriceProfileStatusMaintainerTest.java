package com.gfs.cpp.component.statusprocessor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class ContractPriceProfileStatusMaintainerTest {

    private static final int CONTRACT_PRICE_PROFILE_SEQ = -100;

    @InjectMocks
    @Spy
    private ContractPriceProfileStatusMaintainer target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private CppStatusChangeProcessor cppStatusChangeProcessor;
    @Mock
    private CppStatusProcessorPicker cppStatusProcessorPicker;
    @Mock
    private ContractPriceExpirer contractPriceExpirer;
    @Mock
    private ClmApiProxy clmApiProxy;
    @Mock
    private ClmContractTypeRepository clmContractTypeRepository;

    ContractPricingResponseDTO contractDetails;

    @Before
    public void setup() {
        contractDetails = new ContractPricingResponseDTO();
        contractDetails.setContractPriceProfileSeq(CONTRACT_PRICE_PROFILE_SEQ);
    }

    @Test
    public void shouldUupdateStatusWhenStatusShouldBeUpdatedIsTrue() throws Exception {

        String clmStatus = "status";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.HOLD;
        String userName = "update user";

        doReturn(cppStatusChangeProcessor).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(true).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();

        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(CONTRACT_PRICE_PROFILE_SEQ, updateToStatus.code, userName);
        verify(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor).getUpdateUserId();
        verify(cppStatusChangeProcessor).getUpdateToStatus();
        verify(contractPriceExpirer, never()).expirePriceForContract(eq(CONTRACT_PRICE_PROFILE_SEQ), anyString());
        verify(cppStatusChangeProcessor).expireRequired(cppStatus);

    }

    @Test
    public void shouldUpdateStatusWhenCurrentStatusIsCancelledButNoWorkInProgressVersionFound() throws Exception {

        String clmStatus = "status";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.CANCELLED;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.HOLD;
        String userName = "update user";
        String parentAgreementId = "12#sasfasdf";

        contractDetails.setClmParentAgreementId(parentAgreementId);
        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(cppStatusChangeProcessor).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(true).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();
        doReturn(0).when(contractPriceProfileRepository).fetchInProgressContractVersionCount(parentAgreementId);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(CONTRACT_PRICE_PROFILE_SEQ, updateToStatus.code, userName);
        verify(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor).getUpdateUserId();
        verify(cppStatusChangeProcessor).getUpdateToStatus();
        verify(contractPriceExpirer, never()).expirePriceForContract(eq(CONTRACT_PRICE_PROFILE_SEQ), anyString());
        verify(cppStatusChangeProcessor).expireRequired(cppStatus);
        verify(contractPriceProfileRepository).fetchInProgressContractVersionCount(parentAgreementId);

    }

    @Test
    public void shouldNotUpdateStatusWhenCurrentStatusIsCancelledButFoundWorkInProgressVersionFound() throws Exception {

        String clmStatus = "status";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.CANCELLED;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.HOLD;
        String userName = "update user";
        String parentAgreementId = "12#sasfasdf";

        contractDetails.setClmParentAgreementId(parentAgreementId);

        doReturn(cppStatusChangeProcessor).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(true).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();
        doReturn(1).when(contractPriceProfileRepository).fetchInProgressContractVersionCount(parentAgreementId);

        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(contractPriceProfileRepository, never()).updateContractStatusWithLastUpdateUserIdByCppSeq(CONTRACT_PRICE_PROFILE_SEQ, updateToStatus.code,
                userName);
        verify(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor).getUpdateUserId();
        verify(contractPriceExpirer, never()).expirePriceForContract(eq(CONTRACT_PRICE_PROFILE_SEQ), anyString());
        verify(cppStatusChangeProcessor).expireRequired(cppStatus);
        verify(contractPriceProfileRepository).fetchInProgressContractVersionCount(parentAgreementId);

    }

    @Test
    public void shouldNotUpdateWhenStatusBeUpdatedIsNotTrue() throws Exception {

        String clmStatus = "status";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.HOLD;
        String userName = "update user";

        doReturn(cppStatusChangeProcessor).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(false).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();

        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(contractPriceProfileRepository, never()).updateContractStatusWithLastUpdateUserIdByCppSeq(eq(CONTRACT_PRICE_PROFILE_SEQ), anyInt(),
                anyString());
        verify(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor, never()).getUpdateUserId();
        verify(cppStatusChangeProcessor, never()).getUpdateToStatus();

    }

    @Test
    public void shouldNotUpdateWhenNoStatusProcessorFoundForClmStatus() throws Exception {

        String clmStatus = "status";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.HOLD;
        String userName = "update user";

        doReturn(null).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(false).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();

        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(contractPriceProfileRepository, never()).updateContractStatusWithLastUpdateUserIdByCppSeq(eq(CONTRACT_PRICE_PROFILE_SEQ), anyInt(),
                anyString());
        verify(cppStatusChangeProcessor, never()).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor, never()).getUpdateUserId();
        verify(cppStatusChangeProcessor, never()).getUpdateToStatus();

    }

    @Test
    public void shouldExpireWhenExpireRequired() throws Exception {
        String clmStatus = "status";
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.HOLD;
        String userName = "update user";

        doReturn(cppStatusChangeProcessor).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(true).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();
        doReturn(true).when(cppStatusChangeProcessor).expireRequired(cppStatus);

        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(CONTRACT_PRICE_PROFILE_SEQ, updateToStatus.code, userName);
        verify(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor).getUpdateUserId();
        verify(cppStatusChangeProcessor, times(2)).getUpdateToStatus();
        verify(contractPriceExpirer).expirePriceForContract(CONTRACT_PRICE_PROFILE_SEQ, userName);
    }

    @Test
    public void shouldExpireLatestVersionWhenExpireRequiredAndNewStatusIsTerminated() throws Exception {
        String clmStatus = ClmContractStatus.TERMINATED.value;
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.TERMINATED;
        String userName = "update user";
        String agreementId = "agreementId";

        contractDetails.setClmAgreementId(agreementId);
        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(cppStatusChangeProcessor).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(true).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();
        doReturn(true).when(cppStatusChangeProcessor).expireRequired(cppStatus);
        doNothing().when(target).expirePriceForTerminatedContract(contractDetails, updateToStatus, userName);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(target).expirePriceForTerminatedContract(contractDetails, updateToStatus, userName);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(CONTRACT_PRICE_PROFILE_SEQ, updateToStatus.code, userName);
        verify(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor).getUpdateUserId();
        verify(cppStatusChangeProcessor, times(2)).getUpdateToStatus();
    }

    @Test
    public void shouldExpirePricingForTerminatedCotract() throws Exception {

        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.TERMINATED;
        String userName = "update user";
        String agreementId = "agreementId";
        int latestActivatedCppSeq = -3012;

        contractDetails.setClmAgreementId(agreementId);

        ContractPricingResponseDTO latestActivatedVersion = new ContractPricingResponseDTO();
        latestActivatedVersion.setContractPriceProfileSeq(latestActivatedCppSeq);

        doReturn(latestActivatedVersion).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(agreementId);

        target.expirePriceForTerminatedContract(contractDetails, updateToStatus, userName);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(latestActivatedCppSeq, updateToStatus.code, userName);
        verify(contractPriceExpirer).expirePriceForContract(latestActivatedCppSeq, userName);

    }

    @Test
    public void shouldSkipExpirePricingForTerminatedCotractWhenNoLatestActivatedContractFound() throws Exception {

        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.TERMINATED;
        String userName = "update user";
        String agreementId = "agreementId";

        contractDetails.setClmAgreementId(agreementId);

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(agreementId);

        target.expirePriceForTerminatedContract(contractDetails, updateToStatus, userName);

        verify(contractPriceExpirer, never()).expirePriceForContract(anyInt(), any(String.class));

    }

    @Test
    public void shouldSkipUpdateStatusWhenTerminatedVersionIsSameAsOriginal() throws Exception {

        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.TERMINATED;
        String userName = "update user";
        String agreementId = "agreementId";

        contractDetails.setClmAgreementId(agreementId);

        ContractPricingResponseDTO latestActivatedVersion = new ContractPricingResponseDTO();
        latestActivatedVersion.setContractPriceProfileSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(latestActivatedVersion).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(agreementId);

        target.expirePriceForTerminatedContract(contractDetails, updateToStatus, userName);

        verify(contractPriceProfileRepository, never()).updateContractStatusWithLastUpdateUserIdByCppSeq(anyInt(), anyInt(), any(String.class));
        verify(contractPriceExpirer).expirePriceForContract(CONTRACT_PRICE_PROFILE_SEQ, userName);

    }

    @Test
    public void shouldExpireLatestVersionWhenExpireRequiredAndNewStatusIsExpired() throws Exception {
        String clmStatus = ClmContractStatus.EXPIRED.value;
        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.EXPIRED;
        String userName = "update user";
        String agreementId = "agreementId";

        contractDetails.setClmAgreementId(agreementId);
        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(cppStatusChangeProcessor).when(cppStatusProcessorPicker).pickStatusChangeProcessor(clmStatus);
        doReturn(updateToStatus).when(cppStatusChangeProcessor).getUpdateToStatus();
        doReturn(true).when(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        doReturn(userName).when(cppStatusChangeProcessor).getUpdateUserId();
        doReturn(true).when(cppStatusChangeProcessor).expireRequired(cppStatus);
        doNothing().when(target).expirePriceForExpiredContract(contractDetails, updateToStatus, userName);

        target.syncWithClmStatus(contractDetails, clmStatus);

        verify(target).expirePriceForExpiredContract(contractDetails, updateToStatus, userName);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(CONTRACT_PRICE_PROFILE_SEQ, updateToStatus.code, userName);
        verify(cppStatusChangeProcessor).shouldUpdateStatus(cppStatus);
        verify(cppStatusChangeProcessor).getUpdateUserId();
        verify(cppStatusChangeProcessor, times(2)).getUpdateToStatus();
    }

    @Test
    public void shouldSkipExpirePricingWhenNoLatestActivatedContractFound() throws Exception {

        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.EXPIRED;
        String userName = "update user";
        String agreementId = "agreementId";

        contractDetails.setClmAgreementId(agreementId);
        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(agreementId);

        target.expirePriceForExpiredContract(contractDetails, updateToStatus, userName);

        verify(contractPriceExpirer, never()).expirePriceForContract(anyInt(), any(String.class));

    }

    @Test
    public void shouldExpirePricingWhenLatestActivatedContractIsSameVersion() throws Exception {

        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.EXPIRED;
        String userName = "update user";
        String agreementId = "agreementId";

        contractDetails.setClmAgreementId(agreementId);
        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        ContractPricingResponseDTO latestActivatedVersion = new ContractPricingResponseDTO();
        latestActivatedVersion.setContractPriceProfileSeq(CONTRACT_PRICE_PROFILE_SEQ);

        doReturn(latestActivatedVersion).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(agreementId);

        target.expirePriceForExpiredContract(contractDetails, updateToStatus, userName);

        verify(contractPriceProfileRepository, never()).updateContractStatusWithLastUpdateUserIdByCppSeq(anyInt(), anyInt(), any(String.class));
        verify(contractPriceExpirer).expirePriceForContract(CONTRACT_PRICE_PROFILE_SEQ, userName);

    }

    @Test
    public void shouldExpirePricingWhenLatestActivatedIsAlsoExpired() throws Exception {

        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.EXPIRED;
        String userName = "update user";
        String agreementId = "agreementId";
        String latestVersionAgreementId = "latestVersionAgreementId";
        String latestVersionContractTypeName = "streetAmendment";
        int latestActivatedCppSeq = -3012;
        int latestActivatedContractTypeSeq = -11;

        contractDetails.setClmAgreementId(agreementId);
        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        ContractPricingResponseDTO latestActivatedVersion = new ContractPricingResponseDTO();
        latestActivatedVersion.setContractPriceProfileSeq(latestActivatedCppSeq);
        latestActivatedVersion.setClmAgreementId(latestVersionAgreementId);
        latestActivatedVersion.setClmContractTypeSeq(latestActivatedContractTypeSeq);

        ClmContractResponseDTO latestAgreementDataFromClm = new ClmContractResponseDTO();
        latestAgreementDataFromClm.setContractStatus(ClmContractStatus.EXPIRED.value);

        doReturn(latestVersionContractTypeName).when(clmContractTypeRepository).getContractTypeName(latestActivatedContractTypeSeq);
        doReturn(latestAgreementDataFromClm).when(clmApiProxy).getAgreementData(latestVersionAgreementId, latestVersionContractTypeName);
        doReturn(latestActivatedVersion).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(agreementId);

        target.expirePriceForExpiredContract(contractDetails, updateToStatus, userName);

        verify(contractPriceProfileRepository).updateContractStatusWithLastUpdateUserIdByCppSeq(latestActivatedCppSeq, updateToStatus.code, userName);
        verify(contractPriceExpirer).expirePriceForContract(latestActivatedCppSeq, userName);
        verify(clmApiProxy).getAgreementData(latestVersionAgreementId, latestVersionContractTypeName);
        verify(clmContractTypeRepository).getContractTypeName(latestActivatedContractTypeSeq);

    }

    @Test
    public void shouldNotExpirePricingWhenLatestActivatedIsNotExpired() throws Exception {

        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.DRAFT;
        ContractPriceProfileStatus updateToStatus = ContractPriceProfileStatus.EXPIRED;
        String userName = "update user";
        String agreementId = "agreementId";
        String latestVersionAgreementId = "latestVersionAgreementId";
        String latestVersionContractTypeName = "streetAmendment";
        int latestActivatedCppSeq = -3012;
        int latestActivatedContractTypeSeq = -11;

        contractDetails.setClmAgreementId(agreementId);
        contractDetails.setContractPriceProfStatusCode(cppStatus.code);

        ContractPricingResponseDTO latestActivatedVersion = new ContractPricingResponseDTO();
        latestActivatedVersion.setContractPriceProfileSeq(latestActivatedCppSeq);
        latestActivatedVersion.setClmAgreementId(latestVersionAgreementId);
        latestActivatedVersion.setClmContractTypeSeq(latestActivatedContractTypeSeq);

        ClmContractResponseDTO latestAgreementDataFromClm = new ClmContractResponseDTO();
        latestAgreementDataFromClm.setContractStatus(ClmContractStatus.EXECUTED.value);

        doReturn(latestVersionContractTypeName).when(clmContractTypeRepository).getContractTypeName(latestActivatedContractTypeSeq);
        doReturn(latestAgreementDataFromClm).when(clmApiProxy).getAgreementData(latestVersionAgreementId, latestVersionContractTypeName);
        doReturn(latestActivatedVersion).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(agreementId);

        target.expirePriceForExpiredContract(contractDetails, updateToStatus, userName);

        verify(contractPriceProfileRepository, never()).updateContractStatusWithLastUpdateUserIdByCppSeq(anyInt(), anyInt(), any(String.class));
        verify(contractPriceExpirer, never()).expirePriceForContract(anyInt(), any(String.class));
        verify(clmApiProxy).getAgreementData(latestVersionAgreementId, latestVersionContractTypeName);
        verify(clmContractTypeRepository).getContractTypeName(latestActivatedContractTypeSeq);

    }

}
