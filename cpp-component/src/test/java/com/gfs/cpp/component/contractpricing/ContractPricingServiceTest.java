package com.gfs.cpp.component.contractpricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.contractpricing.ContractPricingCreator;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.contractpricing.ContractPricingUpdater;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusMaintainer;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.CostModelMapRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingServiceTest {

    @InjectMocks
    private ContractPricingService target;
    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Mock
    private CustomerServiceProxy customerServiceProxy;
    @Mock
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;
    @Mock
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;
    @Mock
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;
    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;
    @Mock
    private CostModelMapRepository costModelMapRepository;
    @Mock
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;
    @Mock
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;
    @Mock
    private ClmContractTypeRepository contractTypeRepository;
    @Captor
    private ArgumentCaptor<ContractPricingDO> contractPricingDOCaptor;
    @Mock
    private ContractPriceProfileStatusMaintainer contractPriceProfileStatusMaintainer;
    @Mock
    private ContractPricingCreator contractPricingCreator;
    @Mock
    private ContractPricingUpdater contractPricingUpdater;
    @Mock
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;
    @Mock
    private ClmApiProxy clmApiProxy;
    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void fetchCPPSequenceTest() throws ParseException {
        int contractPriceProfileId = -101;
        doReturn(5).when(contractPriceProfileRepository).fetchCPPSequence(contractPriceProfileId);

        assertThat(target.fetchCPPSequence(contractPriceProfileId), equalTo(5));

        verify(contractPriceProfileRepository).fetchCPPSequence(contractPriceProfileId);
    }

    @Test
    public void fetchPricingInformationTrue() throws Exception {
        int contractPriceProfileSeq = 1;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractTypeSeq(1);
        contractPricingResponseDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setAssessmentFeeFlag(1);
        contractPricingResponseDTO.setTransferFeeFlag(1);
        CMGContractDTO cmgContract = new CMGContractDTO();
        cmgContract.setContractName("Test");
        cmgContract.setContractPriceProfileId("1");
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(1);
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq)).thenReturn(1);
        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M);

        ContractPricingDTO contractPricingDTO = target.fetchPricingInformation(1, "test", "test");

        assertThat(contractPricingDTO.getAssessmentFeeFlag(), equalTo(true));
        assertThat(contractPricingDTO.getPriceAuditFlag(), equalTo(true));
        assertThat(contractPricingDTO.getPriceVerificationFlag(), equalTo(true));
        assertThat(contractPricingDTO.getTransferFeeFlag(), equalTo(true));
        assertThat(contractPricingDTO.getPricingEffectiveDate(), equalTo(new LocalDate(2018, 01, 01).toDate()));
        assertThat(contractPricingDTO.getPricingExpirationDate(), equalTo(new LocalDate(2018, 01, 01).toDate()));
        assertThat(contractPricingDTO.getScheduleForCostChange(), equalTo("gregorian"));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(contractPriceProfileSeq);
        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());

    }

    @Test
    public void fetchPricingInformationFalse() throws Exception {
        int contractPriceProfileSeq = 1;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractTypeSeq(1);
        contractPricingResponseDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setAssessmentFeeFlag(0);
        contractPricingResponseDTO.setTransferFeeFlag(0);
        CMGContractDTO cmgContract = new CMGContractDTO();
        cmgContract.setContractName("Test");
        cmgContract.setContractPriceProfileId("1");
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(0);
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq)).thenReturn(0);
        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M);

        ContractPricingDTO contractPricingDTO = target.fetchPricingInformation(1, "test", "test");

        assertThat(contractPricingDTO.getAssessmentFeeFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPriceAuditFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPriceVerificationFlag(), equalTo(false));
        assertThat(contractPricingDTO.getTransferFeeFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPricingEffectiveDate(), equalTo(new LocalDate(2018, 01, 01).toDate()));
        assertThat(contractPricingDTO.getPricingExpirationDate(), equalTo(new LocalDate(2018, 01, 01).toDate()));
        assertThat(contractPricingDTO.getScheduleForCostChange(), equalTo("fiscal"));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(1);
        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());

    }

    @Test
    public void fetchPricingInformationNull() throws Exception {
        int contractPriceProfileSeq = 1;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractTypeSeq(1);
        contractPricingResponseDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        CMGContractDTO cmgContract = new CMGContractDTO();
        cmgContract.setContractName("Test");
        cmgContract.setContractPriceProfileId("1");
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(0);
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq)).thenReturn(0);
        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M);

        ContractPricingDTO contractPricingDTO = target.fetchPricingInformation(1, "test", "test");

        assertThat(contractPricingDTO.getAssessmentFeeFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPriceAuditFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPriceVerificationFlag(), equalTo(false));
        assertThat(contractPricingDTO.getTransferFeeFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPricingEffectiveDate(), equalTo(new LocalDate(2018, 01, 01).toDate()));
        assertThat(contractPricingDTO.getPricingExpirationDate(), equalTo(new LocalDate(2018, 01, 01).toDate()));
        assertThat(contractPricingDTO.getScheduleForCostChange(), equalTo("fiscal"));

        verify(contractPriceProfileStatusMaintainer).syncWithClmStatus(contractPricingResponseDTO, "test");
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(contractPriceProfileSeq);
        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());

    }

    @Test
    public void fetchPricingInformationDefault() throws Exception {
        int contractPriceProfileSeq = 1;
        Date futureDate = new LocalDate(9999, 01, 01).toDate();

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(null);
        when(cppDateUtils.getFutureDate()).thenReturn(futureDate);

        ContractPricingDTO contractPricingDTO = target.fetchPricingInformation(contractPriceProfileSeq, "test", "test");

        assertThat(contractPricingDTO.getAssessmentFeeFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPriceAuditFlag(), equalTo(false));
        assertThat(contractPricingDTO.getPriceVerificationFlag(), equalTo(false));
        assertThat(contractPricingDTO.getTransferFeeFlag(), equalTo(false));
        assertThat(contractPricingDTO.getScheduleForCostChange(), equalTo("fiscal"));
        assertThat(contractPricingDTO.getPricingExpirationDate(), equalTo(new LocalDate(9999, 01, 01).toDate()));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(cppDateUtils, atLeast(1)).getFutureDate();

    }

    @Test
    public void shouldSaveContractPricingInfo() throws Exception {
        String userName = "vc71u";
        int contractPriceProfileSeq = 12345;
        int contractTypeSeq = 1;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(true);
        contractPricingDTO.setContractName("Test");
        contractPricingDTO.setContractPriceProfileId(1);
        contractPricingDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractPricingDTO.setContractType("DAN");
        contractPricingDTO.setPriceAuditFlag(true);
        contractPricingDTO.setPriceVerificationFlag(true);
        contractPricingDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingDTO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingDTO.setScheduleForCostChange("gregorian");
        contractPricingDTO.setTransferFeeFlag(true);
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(31);

        when(contractTypeRepository.fetchContractTypeSequenceByTypeName(contractPricingDTO.getContractType())).thenReturn(contractTypeSeq);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(null);

        target.saveContractPricing(contractPricingDTO, userName);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractTypeRepository).fetchContractTypeSequenceByTypeName("DAN");
        verify(contractPricingCreator).saveContractPricingInformation(contractPricingDTO, userName, contractTypeSeq);

    }

    @Test
    public void shouldUpdateContractPricingInfo() throws Exception {
        String userName = "vc71u";
        int contractPriceProfileSeq = 12345;
        int contractTypeSeq = 1;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(true);
        contractPricingDTO.setContractName("Test");
        contractPricingDTO.setContractPriceProfileId(1);
        contractPricingDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractPricingDTO.setContractType("DAN");
        contractPricingDTO.setPriceAuditFlag(true);
        contractPricingDTO.setPriceVerificationFlag(true);
        contractPricingDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingDTO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingDTO.setScheduleForCostChange("gregorian");
        contractPricingDTO.setTransferFeeFlag(true);
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(31);
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();

        when(contractTypeRepository.fetchContractTypeSequenceByTypeName(contractPricingDTO.getContractType())).thenReturn(contractTypeSeq);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);

        target.saveContractPricing(contractPricingDTO, userName);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractTypeRepository).fetchContractTypeSequenceByTypeName("DAN");
        verify(contractPricingUpdater).updateContractPricingInformation(contractPricingDTO, userName);

    }

    @Test
    public void shouldDeletePricingExhibit() throws Exception {
        String agreementId = "Test";
        String userName = "vc71u";
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setContractPriceProfileSeq(1);
        contractPricingResponseDTO.setPricingExhibitSysId("test");
        when(contractPriceProfileRepository.fetchContractDetailsByAgreementId(agreementId)).thenReturn(contractPricingResponseDTO);

        target.deletePricingExhibit(agreementId, userName);

        verify(contractPriceProfileRepository).fetchContractDetailsByAgreementId(agreementId);
        verify(clmApiProxy).deletePricingExhibit("test");
        verify(contractPriceProfileRepository).updatePricingExhibitGuid(contractPricingResponseDTO.getContractPriceProfileSeq(), null, userName);
    }

}