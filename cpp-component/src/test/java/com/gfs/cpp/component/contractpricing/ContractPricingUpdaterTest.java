package com.gfs.cpp.component.contractpricing;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.component.contractpricing.ContractPricingDOBuilder;
import com.gfs.cpp.component.contractpricing.ContractPricingUpdater;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.CostModelMapRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingUpdaterTest {

    @InjectMocks
    private ContractPricingUpdater target;
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
    private ContractPricingDOBuilder contractPricingDOBuilder;

    @Test
    public void shouldUpdateContractPricingInfo() throws Exception {

        String userName = "vc71u";
        int contractPriceProfileSeq = 0;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(31);
        ContractPricingDTO contractPricingDTO = buildContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(true);
        contractPricingDTO.setPriceAuditFlag(true);
        contractPricingDTO.setPriceVerificationFlag(true);
        contractPricingDTO.setScheduleForCostChange("fiscal");
        contractPricingDTO.setTransferFeeFlag(true);
        ContractPricingDO contractPricingDO = buildContractPricingDO(contractPricingDTO);

        when(costModelMapRepository.fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd())).thenReturn(null);
        when(contractPricingDOBuilder.buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO)).thenReturn(contractPricingDO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(prcProfNonBrktCstMdlRepository.fetchCostModelId(contractPriceProfileSeq)).thenReturn(1);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, "1", 31)).thenReturn(0);

        target.updateContractPricingInformation(contractPricingDTO, userName);

        verify(contractPricingDOBuilder).buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);
        verify(contractPriceProfileRepository).updateContractPriceProfile(eq(contractPriceProfileSeq), eq(contractPricingDO.getPricingEffectiveDate()),
                eq(userName), eq(contractPricingDO.getTransferFeeInd()), eq(contractPricingDO.getLabelAssesmentInd()));
        verify(prcProfAuditAuthorityRepository).updatePriceProfileAuditIndicator(userName, contractPriceProfileSeq, CPPConstants.INDICATOR_ONE);
        verify(prcProfNonBrktCstMdlRepository).fetchCostModelId(contractPricingDO.getContractPriceProfileSeq());
        verify(prcProfNonBrktCstMdlRepository).updateCostModelData(userName, CPPConstants.DEFAULT_ITEM_COST_MODEL, CPPConstants.DEFAULT_SUBGROUP_COST_MODEL,
                CPPConstants.DEFAULT_PRODUCT_COST_MODEL, contractPriceProfileSeq);
        verify(costModelMapRepository).fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd());
        verify(prcProfCostRunSchedGroupRepository).updateScheduleGroupSeqMonthly(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M,
                contractPricingDO.getContractPriceProfileSeq());
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, "1", 31);
    }

    @Test
    public void shouldUpdateContractPricingInfoWhenAllNoFlags() throws Exception {
        String userName = "vc71u";
        int contractPriceProfileSeq = 0;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(31);
        ContractPricingDTO contractPricingDTO = buildContractPricingDTO();
        ContractPricingDO contractPricingDO = buildContractPricingDO(contractPricingDTO);

        when(contractPricingDOBuilder.buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO)).thenReturn(contractPricingDO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);

        when(costModelMapRepository.fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd())).thenReturn(2);
        when(prcProfNonBrktCstMdlRepository.fetchCostModelId(contractPriceProfileSeq)).thenReturn(1);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, "1", 31)).thenReturn(1);

        target.updateContractPricingInformation(contractPricingDTO, userName);

        verify(contractPricingDOBuilder).buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);
        verify(contractPriceProfileRepository).updateContractPriceProfile(eq(contractPriceProfileSeq), eq(contractPricingDO.getPricingEffectiveDate()),
                eq(userName), eq(contractPricingDO.getTransferFeeInd()), eq(contractPricingDO.getLabelAssesmentInd()));
        verify(prcProfAuditAuthorityRepository).updatePriceProfileAuditIndicator(userName, contractPriceProfileSeq, CPPConstants.INDICATOR_ZERO);
        verify(prcProfNonBrktCstMdlRepository).fetchCostModelId(contractPricingDO.getContractPriceProfileSeq());
        verify(prcProfNonBrktCstMdlRepository).updateCostModelData(userName, 2, 2, 2, contractPriceProfileSeq);
        verify(costModelMapRepository).fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd());
        verify(prcProfCostRunSchedGroupRepository).updateScheduleGroupSeqMonthly(CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M,
                contractPricingDO.getContractPriceProfileSeq());
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, "1", 31);
    }

    private ContractPricingDTO buildContractPricingDTO() {
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(false);
        contractPricingDTO.setContractName("Test");
        contractPricingDTO.setContractPriceProfileId(1);
        contractPricingDTO.setContractPriceProfileSeq(0);
        contractPricingDTO.setContractType("DAN");
        contractPricingDTO.setPriceAuditFlag(false);
        contractPricingDTO.setPriceVerificationFlag(false);
        contractPricingDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingDTO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingDTO.setScheduleForCostChange("gregorian");
        contractPricingDTO.setTransferFeeFlag(false);

        return contractPricingDTO;
    }

    private ContractPricingDO buildContractPricingDO(ContractPricingDTO contractPricingDTO) {
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setPriceAuditPrivileges(contractPricingDTO.getPriceAuditFlag());
        contractPricingDO.setPriceAuditInd(contractPricingDTO.getPriceAuditFlag() ? 1 : 0);
        contractPricingDO.setPriceVerifyPrivileges(contractPricingDTO.getPriceVerificationFlag());
        contractPricingDO.setPriceVerifInd(contractPricingDTO.getPriceVerificationFlag() ? 1 : 0);
        contractPricingDO.setScheduleForCostChange(contractPricingDTO.getScheduleForCostChange());
        contractPricingDO.setCostModelGFSAssesFee(contractPricingDTO.getAssessmentFeeFlag());
        contractPricingDO.setLabelAssesmentInd(contractPricingDTO.getAssessmentFeeFlag() ? 1 : 0);
        contractPricingDO.setCostModelTransferFee(contractPricingDTO.getTransferFeeFlag());
        contractPricingDO.setTransferFeeInd(contractPricingDTO.getTransferFeeFlag() ? 1 : 0);
        contractPricingDO.setContractTypeCode(contractPricingDTO.getContractType());
        contractPricingDO.setContractName(contractPricingDTO.getContractName());
        contractPricingDO.setAgreementId(contractPricingDTO.getAgreementId());
        contractPricingDO.setGfsCustomerId("1");
        contractPricingDO.setCustomerTypeCode(31);
        contractPricingDO.setContractPriceProfileSeq(contractPricingDTO.getContractPriceProfileSeq());
        contractPricingDO.setContractPriceProfileId(contractPricingDTO.getContractPriceProfileId());

        return contractPricingDO;

    }
}
