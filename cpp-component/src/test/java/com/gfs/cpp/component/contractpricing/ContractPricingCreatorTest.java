package com.gfs.cpp.component.contractpricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.component.costmodel.CostModelService;
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
public class ContractPricingCreatorTest {

    private static final String PARENT_AGREEMENT_ID = "parent agreement id";
    @InjectMocks
    private ContractPricingCreator target;
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
    @Captor
    private ArgumentCaptor<ContractPricingDO> contractPricingDOCaptor;
    @Mock
    private ContractPricingDOBuilder contractPricingDOBuilder;
    
    @Mock
    private CostModelService costModelService;

    @Test
    public void shouldSaveContractPricingInfo() throws Exception {
        String userName = "vc71u";
        int contractPriceProfileSeq = 12345;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        ContractPricingDTO contractPricingDTO = buildContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(true);
        contractPricingDTO.setPriceAuditFlag(true);
        contractPricingDTO.setPriceVerificationFlag(true);
        contractPricingDTO.setTransferFeeFlag(true);
        contractPricingDTO.setScheduleForCostChange("gregorian");
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(31);
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/1999");
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/1999");
        ContractPricingDO contractPricingDO = buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);

        when(contractPricingDOBuilder.buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO)).thenReturn(contractPricingDO);
        when(prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq()).thenReturn(1);
        doReturn(cMGCustomerResponseDTO).when(customerServiceProxy).createCMGCustomerGroup(1, "Test");
        when(costModelMapRepository.fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd())).thenReturn(null);
        when(costModelService.fetchIncludedListOfProductTypeIDs()).thenReturn(setOfProductTypes());

        target.saveContractPricingInformation(contractPricingDTO, userName, 1);

        verify(prcProfAuditAuthorityRepository).savePriceProfileAuditIndicator(eq(userName), eq(contractPriceProfileSeq), contractPricingDOCaptor.capture(),
                eq(CPPConstants.INDICATOR_ONE));
        verify(prcProfCostSchedulePkgRepository).fetchPrcProfileCostSchedulePkgNextSeq();
        verify(prcProfCostSchedulePkgRepository).saveScheduleForCostChange(eq(userName), eq(contractPriceProfileSeq), contractPricingDOCaptor.capture(),
                eq(1));
        verify(prcProfCostRunSchedGroupRepository).insertSchedule(eq(contractPriceProfileSeq), eq(1), eq(CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_W),
                eq(CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M));
        verify(prcProfVerificationPrivlgRepository).savePriceVerifyPrivileges(eq(contractPriceProfileSeq), eq(userName), eq(effectiveDate),
                eq(expirationDate), eq(1));
        verify(costModelMapRepository).fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd());
        verify(customerServiceProxy).createCMGCustomerGroup(1, "Test");
        verify(contractPricingDOBuilder).buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);
        verify(prcProfNonBrktCstMdlRepository).saveCostModelData(userName, contractPricingDO, CPPConstants.DEFAULT_ITEM_COST_MODEL,
                CPPConstants.DEFAULT_SUBGROUP_COST_MODEL, CPPConstants.DEFAULT_PRODUCT_COST_MODEL,setOfProductTypes());
        verify(costModelService).fetchIncludedListOfProductTypeIDs();
        
        ContractPricingDO actualContractPricingSaved = contractPricingDOCaptor.getValue();

        assertThat(actualContractPricingSaved.getContractPriceProfileId(), equalTo(1));
        assertThat(actualContractPricingSaved.getContractPriceProfileSeq(), equalTo(12345));
        assertThat(actualContractPricingSaved.getPriceAuditPrivileges(), equalTo(true));
        assertThat(actualContractPricingSaved.getPriceAuditInd(), equalTo(1));
        assertThat(actualContractPricingSaved.getPriceVerifyPrivileges(), equalTo(true));
        assertThat(actualContractPricingSaved.getPriceVerifInd(), equalTo(1));
        assertThat(actualContractPricingSaved.getScheduleForCostChange(), equalTo("gregorian"));
        assertThat(actualContractPricingSaved.getCostModelGFSAssesFee(), equalTo(true));
        assertThat(actualContractPricingSaved.getLabelAssesmentInd(), equalTo(1));
        assertThat(actualContractPricingSaved.getCostModelTransferFee(), equalTo(true));
        assertThat(actualContractPricingSaved.getTransferFeeInd(), equalTo(1));
        assertThat(actualContractPricingSaved.getContractTypeCode(), equalTo("DAN"));
        assertThat(actualContractPricingSaved.getContractName(), equalTo("Test"));

    }

    @Test
    public void shouldSaveContractPricingInfoWhenAllNoFlags() throws Exception {
        String userName = "vc71u";
        int contractPriceProfileSeq = 12345;
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        ContractPricingDTO contractPricingDTO = buildContractPricingDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(31);
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/1999");
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/1999");
        ContractPricingDO contractPricingDO = buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);

        when(contractPricingDOBuilder.buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO))
                .thenReturn(buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO));
        when(prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq()).thenReturn(1);
        doReturn(cMGCustomerResponseDTO).when(customerServiceProxy).createCMGCustomerGroup(1, "Test");
        when(costModelMapRepository.fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd())).thenReturn(1);
        when(costModelService.fetchIncludedListOfProductTypeIDs()).thenReturn(setOfProductTypes());
        
        target.saveContractPricingInformation(contractPricingDTO, userName, 1);

        verify(prcProfAuditAuthorityRepository).savePriceProfileAuditIndicator(eq(userName), eq(contractPriceProfileSeq), contractPricingDOCaptor.capture(),
                eq(CPPConstants.INDICATOR_ZERO));
        verify(prcProfCostSchedulePkgRepository).fetchPrcProfileCostSchedulePkgNextSeq();
        verify(prcProfCostSchedulePkgRepository).saveScheduleForCostChange(eq(userName), eq(contractPriceProfileSeq), contractPricingDOCaptor.capture(),
                eq(CPPConstants.INDICATOR_ONE));
        verify(prcProfCostRunSchedGroupRepository).insertSchedule(eq(contractPriceProfileSeq), eq(1), eq(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_W),
                eq(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M));
        verify(prcProfVerificationPrivlgRepository).savePriceVerifyPrivileges(eq(contractPriceProfileSeq), eq(userName), eq(effectiveDate),
                eq(expirationDate), eq(CPPConstants.INDICATOR_ZERO));
        verify(costModelMapRepository).fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd());
        verify(customerServiceProxy).createCMGCustomerGroup(1, "Test");
        verify(contractPricingDOBuilder).buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);
        verify(prcProfNonBrktCstMdlRepository).saveCostModelData(userName, contractPricingDO, 1, 1, 1,setOfProductTypes());
        verify(costModelService).fetchIncludedListOfProductTypeIDs();
        
        ContractPricingDO actualContractPricingSaved = contractPricingDOCaptor.getValue();

        assertThat(actualContractPricingSaved.getContractPriceProfileId(), equalTo(1));
        assertThat(actualContractPricingSaved.getContractPriceProfileSeq(), equalTo(12345));
        assertThat(actualContractPricingSaved.getPriceAuditPrivileges(), equalTo(false));
        assertThat(actualContractPricingSaved.getPriceAuditInd(), equalTo(0));
        assertThat(actualContractPricingSaved.getPriceVerifyPrivileges(), equalTo(false));
        assertThat(actualContractPricingSaved.getPriceVerifInd(), equalTo(0));
        assertThat(actualContractPricingSaved.getScheduleForCostChange(), equalTo("fiscal"));
        assertThat(actualContractPricingSaved.getCostModelGFSAssesFee(), equalTo(false));
        assertThat(actualContractPricingSaved.getLabelAssesmentInd(), equalTo(0));
        assertThat(actualContractPricingSaved.getCostModelTransferFee(), equalTo(false));
        assertThat(actualContractPricingSaved.getTransferFeeInd(), equalTo(0));
        assertThat(actualContractPricingSaved.getContractTypeCode(), equalTo("DAN"));
        assertThat(actualContractPricingSaved.getContractName(), equalTo("Test"));
        assertThat(actualContractPricingSaved.getParentAgreementId(), equalTo(PARENT_AGREEMENT_ID));

    }

    private ContractPricingDTO buildContractPricingDTO() {
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(false);
        contractPricingDTO.setContractName("Test");
        contractPricingDTO.setContractPriceProfileId(1);
        contractPricingDTO.setContractPriceProfileSeq(12345);
        contractPricingDTO.setContractType("DAN");
        contractPricingDTO.setPriceAuditFlag(false);
        contractPricingDTO.setPriceVerificationFlag(false);
        contractPricingDTO.setPricingEffectiveDate(new LocalDate(1999, 01, 01).toDate());
        contractPricingDTO.setPricingExpirationDate(new LocalDate(1999, 01, 01).toDate());
        contractPricingDTO.setScheduleForCostChange("fiscal");
        contractPricingDTO.setTransferFeeFlag(false);

        return contractPricingDTO;

    }

    @Test
    public void shouldUseAgreementIdWhenParentIdIsNull() throws Exception {

        String userName = "user name";
        int contractTypeSeq = -1001;
        String agreementId = "agreementId";
        int contractPriceProfileSeq = 1001;

        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setAgreementId(agreementId);
        contractPricingDO.setContractPriceProfileSeq(contractPriceProfileSeq);

        target.saveContractPricing(userName, contractTypeSeq, contractPricingDO);

        verify(contractPriceProfileRepository).saveContractPricingDetails(contractPricingDOCaptor.capture(), eq(userName), eq(contractTypeSeq),
                eq(contractPriceProfileSeq));

        ContractPricingDO actual = contractPricingDOCaptor.getValue();
        assertThat(actual.getParentAgreementId(), equalTo(agreementId));
    }

    private ContractPricingDO buildContractPricingDO(ContractPricingDTO contractPricingDTO, CMGCustomerResponseDTO cmgCustomerResponseDTO)
            throws ParseException {
        ContractPricingDO contractPricingDO = new ContractPricingDO();

        contractPricingDO.setContractPriceProfileSeq(contractPricingDTO.getContractPriceProfileSeq());
        contractPricingDO.setGfsCustomerId(cmgCustomerResponseDTO.getId());
        contractPricingDO.setCustomerTypeCode(cmgCustomerResponseDTO.getTypeCode());
        contractPricingDO.setPricingEffectiveDate(contractPricingDTO.getPricingEffectiveDate());
        contractPricingDO.setPricingExpirationDate(contractPricingDTO.getPricingExpirationDate());
        contractPricingDO.setContractPriceProfileId(contractPricingDTO.getContractPriceProfileId());
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
        contractPricingDO.setParentAgreementId(PARENT_AGREEMENT_ID);
        contractPricingDO.setEffectiveDateFuture(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        contractPricingDO.setExpirationDateFuture(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        return contractPricingDO;
    }

    private Set<Integer> setOfProductTypes(){
        Integer arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}; 
        return new HashSet<>(Arrays.asList(arr)); 
    }
}
