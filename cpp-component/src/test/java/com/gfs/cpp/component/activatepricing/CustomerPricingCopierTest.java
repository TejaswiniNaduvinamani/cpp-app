package com.gfs.cpp.component.activatepricing;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;
import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;
import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;
import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.CustomerPricingCopier;
import com.gfs.cpp.component.activatepricing.PrcProfAuditAuthorityDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfCostSchedulePkgDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfCostSchedulePkgScheduledGroupDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfLessCaseRuleDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfNonBrktCstMdlDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfPricingRuleOvrdDOBuilder;
import com.gfs.cpp.component.activatepricing.PriceProfileBuilder;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerPricingCopierTest {

    @InjectMocks
    private CustomerPricingCopier target;

    @Mock
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Mock
    private ContractPricingService contractPricingService;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private SplitCaseService splitCaseService;

    @Mock
    private PriceProfileBuilder priceProfileBuilder;

    @Mock
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Mock
    private PrcProfAuditAuthorityDOBuilder prcProfAuditAuthorityDOBuilder;

    @Mock
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Mock
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Mock
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Mock
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Mock
    private PrcProfCostSchedulePkgDOBuilder prcProfCostSchedulePkgDOBuilder;

    @Mock
    private PrcProfCostSchedulePkgScheduledGroupDOBuilder prcProfCostSchedulePkgScheduledGroupDOBuilder;

    @Mock
    private PrcProfNonBrktCstMdlDOBuilder prcProfNonBrktCstMdlDOBuilder;

    @Mock
    private PrcProfPricingRuleOvrdDOBuilder prcProfPricingRuleOvrdDOBuilder;

    @Mock
    private PrcProfLessCaseRuleDOBuilder prcProfLessCaseRuleDOBuilder;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    @Captor
    private ArgumentCaptor<List<PrcProfAuditAuthorityDO>> prcProfAuditAuthorityDOListCaptor;

    @Captor
    private ArgumentCaptor<List<PrcProfCostSchedulePkgDO>> prcProfCostSchedulePkgDOListCaptor;

    @Captor
    private ArgumentCaptor<CMGCustomerResponseDTO> cmgCustomerResponseDTOCaptor;

    @Captor
    private ArgumentCaptor<List<SplitCaseDO>> splitCaseListCaptor;

    @Test
    public void shouldSavePrcProfAuditAuthority() throws ParseException {
        int contractPriceProfileSeq = 1;
        String userId = "test user";

        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("1");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);

        List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList = new ArrayList<PrcProfAuditAuthorityDTO>();
        PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO = new PrcProfAuditAuthorityDTO();
        prcProfAuditAuthorityDTO.setContractPriceProfileSeq(1);
        prcProfAuditAuthorityDTO.setGfsCustomerId("1");
        prcProfAuditAuthorityDTO.setGfsCustomerType(22);
        prcProfAuditAuthorityDTO.setCreateUserId("test user");
        prcProfAuditAuthorityDTO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfAuditAuthorityDTO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfAuditAuthorityDTO.setLastUpdateUserId("test user");
        prcProfAuditAuthorityDTO.setPrcProfAuditAuthorityInd(1);
        prcProfAuditAuthorityDTOList.add(prcProfAuditAuthorityDTO);

        List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = new ArrayList<PrcProfAuditAuthorityDO>();
        PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = new PrcProfAuditAuthorityDO();
        prcProfAuditAuthorityDO.setContractPriceProfileSeq(1);
        prcProfAuditAuthorityDO.setGfsCustomerId("1");
        prcProfAuditAuthorityDO.setGfsCustomerTypeCode(22);
        prcProfAuditAuthorityDO.setCreateUserId("test user");
        prcProfAuditAuthorityDO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfAuditAuthorityDO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfAuditAuthorityDO.setLastUpdateUserId("test user");
        prcProfAuditAuthorityDO.setPrcProfAuditAuthorityInd(1);
        prcProfAuditAuthorityDOList.add(prcProfAuditAuthorityDO);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(prcProfAuditAuthorityRepository.fetchPrcProfAuditAuthorityForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO))
                .thenReturn(prcProfAuditAuthorityDTOList);
        when(prcProfAuditAuthorityDOBuilder.buildPrcProfAuditAuthorityDTOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfAuditAuthorityDTOList)).thenReturn(prcProfAuditAuthorityDOList);

        target.savePrcProfAuditAuthority(contractPriceProfileSeq, userId, contractCustomerMappingDTO, contractDetails, cmgCustomerResponseDTO);

        verify(prcProfAuditAuthorityRepository).fetchPrcProfAuditAuthorityForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        verify(prcProfAuditAuthorityDOBuilder).buildPrcProfAuditAuthorityDTOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfAuditAuthorityDTOList);
        verify(prcProfAuditAuthorityRepository).savePriceProfileAuditForCustomer(prcProfAuditAuthorityDOList);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldNotSavePrcProfAuditAuthorityForNotExistingRecordForCPPSeq() throws ParseException {
        int contractPriceProfileSeq = 1;
        String userId = "test user";

        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("1");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);

        List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList = null;

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(prcProfAuditAuthorityRepository.fetchPrcProfAuditAuthorityForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO))
                .thenReturn(prcProfAuditAuthorityDTOList);

        target.savePrcProfAuditAuthority(contractPriceProfileSeq, userId, contractCustomerMappingDTO, contractDetails, cmgCustomerResponseDTO);

        verify(prcProfAuditAuthorityRepository).fetchPrcProfAuditAuthorityForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        verify(prcProfAuditAuthorityRepository, never()).savePriceProfileAuditForCustomer(any(List.class));
    }

    @Test
    public void shouldSaveCostSchedulePkgAndPkgGroup() throws ParseException {
        int contractPriceProfileSeq = 1;
        String userId = "test user";
        int pkgcostScehdeleSeqNumerForCustomer = 1;

        List<PrcProfCostSchedulePkgDO> costPackageSeqList = new ArrayList<>();
        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO_1 = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO_1.setPrcProfCostSchedulePkgSeq(1);

        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO_2 = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO_2.setPrcProfCostSchedulePkgSeq(2);

        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO_3 = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO_3.setPrcProfCostSchedulePkgSeq(3);

        costPackageSeqList.add(prcProfCostSchedulePkgDO_1);
        costPackageSeqList.add(prcProfCostSchedulePkgDO_2);
        costPackageSeqList.add(prcProfCostSchedulePkgDO_3);

        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);

        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO.setContractPriceProfileSeq(1);
        prcProfCostSchedulePkgDO.setGfsCustomerTypeCode(1);
        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(2);
        prcProfCostSchedulePkgDO.setCreateUserId("test user");
        prcProfCostSchedulePkgDO.setEffectiveDate(null);
        prcProfCostSchedulePkgDO.setExpirationDate(null);
        prcProfCostSchedulePkgDO.setGfsCustomerId("1");
        prcProfCostSchedulePkgDO.setLastUpdateUserId("test user");

        List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchedulePkgScheduledGroupDTOListForCMG = new ArrayList<PrcProfCostSchedulePkgScheduledGroupDTO>();
        PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTO = new PrcProfCostSchedulePkgScheduledGroupDTO();
        prcProfCostSchedulePkgScheduledGroupDTO.setPrcProfCostSchedulePkgSeq(2);
        prcProfCostSchedulePkgScheduledGroupDTO.setContractPriceSeq(1);
        prcProfCostSchedulePkgScheduledGroupDTO.setCostRunFrequencyCode("2");
        prcProfCostSchedulePkgScheduledGroupDTO.setScheduleGroup(1);
        prcProfCostSchedulePkgScheduledGroupDTOListForCMG.add(prcProfCostSchedulePkgScheduledGroupDTO);

        List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchedulePkgScheduledGroupDOList = new ArrayList<PrcProfCostSchedulePkgScheduledGroupDO>();
        PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO = new PrcProfCostSchedulePkgScheduledGroupDO();
        prcProfCostSchedulePkgScheduledGroupDO.setPrcProfCostSchedulePkgSeq(2);
        prcProfCostSchedulePkgScheduledGroupDO.setContractPriceSeq(1);
        prcProfCostSchedulePkgScheduledGroupDO.setCostRunFrequencyCode("2");
        prcProfCostSchedulePkgScheduledGroupDO.setScheduleGroup(1);
        prcProfCostSchedulePkgScheduledGroupDOList.add(prcProfCostSchedulePkgScheduledGroupDO);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(prcProfCostSchedulePkgRepository.fetchPrcProfCostScheduleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO))
                .thenReturn(costPackageSeqList);
        when(prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq()).thenReturn(pkgcostScehdeleSeqNumerForCustomer);
        when(prcProfCostSchedulePkgDOBuilder.buildPrcProfCostSchedulePkgDOList(userId, contractCustomerMappingDTO, contractDetails,
                pkgcostScehdeleSeqNumerForCustomer)).thenReturn(prcProfCostSchedulePkgDO);
        when(prcProfCostRunSchedGroupRepository.fetchPrcProfCostRunSchedGroupForCPPSeq(contractPriceProfileSeq, pkgcostScehdeleSeqNumerForCustomer))
                .thenReturn(prcProfCostSchedulePkgScheduledGroupDTOListForCMG);
        when(prcProfCostSchedulePkgScheduledGroupDOBuilder.buildPrcProfCostSchedulePkgScheduledGroupDOList(
                prcProfCostSchedulePkgScheduledGroupDTOListForCMG, pkgcostScehdeleSeqNumerForCustomer))
                        .thenReturn(prcProfCostSchedulePkgScheduledGroupDOList);

        target.saveCostSchedulePkgAndPkgGroup(contractPriceProfileSeq, userId, contractCustomerMappingDTO, contractDetails, cmgCustomerResponseDTO);

        verify(prcProfCostSchedulePkgRepository).fetchPrcProfCostScheduleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        verify(prcProfCostSchedulePkgRepository).fetchPrcProfileCostSchedulePkgNextSeq();
        verify(prcProfCostSchedulePkgDOBuilder).buildPrcProfCostSchedulePkgDOList(userId, contractCustomerMappingDTO, contractDetails,
                pkgcostScehdeleSeqNumerForCustomer);
        verify(prcProfCostSchedulePkgRepository).savePrcProfCostScheduleForCustomer(prcProfCostSchedulePkgDOListCaptor.capture());
        verify(prcProfCostRunSchedGroupRepository).fetchPrcProfCostRunSchedGroupForCPPSeq(contractPriceProfileSeq, pkgcostScehdeleSeqNumerForCustomer);
        verify(prcProfCostSchedulePkgScheduledGroupDOBuilder).buildPrcProfCostSchedulePkgScheduledGroupDOList(
                prcProfCostSchedulePkgScheduledGroupDTOListForCMG, pkgcostScehdeleSeqNumerForCustomer);
        verify(prcProfCostRunSchedGroupRepository).savePrcProfCostRunSchedGroupForCustomer(prcProfCostSchedulePkgScheduledGroupDOList);

    }

    @Test
    public void shouldSavePrcProfNonBrktCstMdl() throws ParseException {
        int contractPriceProfileSeq = 1;
        String userId = "test user";

        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);

        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(1);
        prcProfNonBrktCstMdlDTO.setCostModelId(2);
        prcProfNonBrktCstMdlDTO.setItemPriceId("1");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(2);
        prcProfNonBrktCstMdlDTOList.add(prcProfNonBrktCstMdlDTO);

        List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO = new PrcProfNonBrktCstMdlDO();
        prcProfNonBrktCstMdlDO.setContractPriceProfileSeq(1);
        prcProfNonBrktCstMdlDO.setCostModelId(2);
        prcProfNonBrktCstMdlDO.setCreateUserId("test user");
        prcProfNonBrktCstMdlDO.setEffectiveDate(null);
        prcProfNonBrktCstMdlDO.setExpirationDate(null);
        prcProfNonBrktCstMdlDO.setGfsCustomerId("1");
        prcProfNonBrktCstMdlDO.setGfsCustomerTypeCode(1);
        prcProfNonBrktCstMdlDO.setItemPriceId("1");
        prcProfNonBrktCstMdlDO.setItemPriceLevelCode(2);
        prcProfNonBrktCstMdlDO.setLastUpdateUserId("test user");
        prcProfNonBrktCstMdlDOList.add(prcProfNonBrktCstMdlDO);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(prcProfNonBrktCstMdlRepository.fetchPrcProfNonBrktCstMdlForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO))
                .thenReturn(prcProfNonBrktCstMdlDTOList);

        when(prcProfNonBrktCstMdlDOBuilder.buildPrcProfNonBrktCstMdlDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfNonBrktCstMdlDTOList)).thenReturn(prcProfNonBrktCstMdlDOList);

        target.savePrcProfNonBrktCstMdl(contractPriceProfileSeq, userId, contractCustomerMappingDTO, contractDetails, cmgCustomerResponseDTO);

        verify(prcProfNonBrktCstMdlRepository).fetchPrcProfNonBrktCstMdlForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        verify(prcProfNonBrktCstMdlDOBuilder).buildPrcProfNonBrktCstMdlDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfNonBrktCstMdlDTOList);
        verify(prcProfNonBrktCstMdlRepository).savePrcProfNonBrktCstMdlForCustomer(prcProfNonBrktCstMdlDOList);

    }

    @Test
    public void shouldSavePrcProfPricingRuleOvrd() throws ParseException {
        int contractPriceProfileSeq = 1;
        String userId = "test user";

        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);

        List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList = new ArrayList<>();
        PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO = new PrcProfPricingRuleOvrdDTO();
        prcProfPricingRuleOvrdDTO.setContractPriceProfileSeq(1);
        prcProfPricingRuleOvrdDTO.setPricingOverrideId(1);
        prcProfPricingRuleOvrdDTO.setPricingOverrideInd(1);
        prcProfPricingRuleOvrdDTOList.add(prcProfPricingRuleOvrdDTO);

        List<PrcProfPricingRuleOvrdDO> pcProfPricingRuleOvrDOList = new ArrayList<>();
        PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO = new PrcProfPricingRuleOvrdDO();
        prcProfPricingRuleOvrdDO.setContractPriceProfileSeq(1);
        prcProfPricingRuleOvrdDO.setCreateUserId("test user");
        prcProfPricingRuleOvrdDO.setEffectiveDate(null);
        prcProfPricingRuleOvrdDO.setExpirationDate(null);
        prcProfPricingRuleOvrdDO.setGfsCustomerId("1");
        prcProfPricingRuleOvrdDO.setGfsCustomerTypeCode(1);
        prcProfPricingRuleOvrdDO.setLastUpdateUserId("test user");
        prcProfPricingRuleOvrdDO.setPricingOverrideId(1);
        prcProfPricingRuleOvrdDO.setPricingOverrideInd(2);
        pcProfPricingRuleOvrDOList.add(prcProfPricingRuleOvrdDO);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(prcProfPricingRuleOvrdRepository.fetchPrcProfPricingRuleOvrdForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO))
                .thenReturn(prcProfPricingRuleOvrdDTOList);
        when(prcProfPricingRuleOvrdDOBuilder.buildPrcProfPricingRuleOvrdDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfPricingRuleOvrdDTOList)).thenReturn(pcProfPricingRuleOvrDOList);

        target.savePrcProfPricingRuleOvrd(contractPriceProfileSeq, userId, contractCustomerMappingDTO, contractDetails, cmgCustomerResponseDTO);

        verify(prcProfPricingRuleOvrdRepository).fetchPrcProfPricingRuleOvrdForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        verify(prcProfPricingRuleOvrdDOBuilder).buildPrcProfPricingRuleOvrdDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfPricingRuleOvrdDTOList);
        verify(prcProfPricingRuleOvrdRepository).savePrcProfPricingRuleOvrdForCustomer(pcProfPricingRuleOvrDOList);

    }

    @Test
    public void shouldSavePrcProfLessCaseRule() throws ParseException {
        int contractPriceProfileSeq = 1;
        String userId = "test user";

        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);

        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG = new ArrayList<>();
        PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = new PrcProfLessCaseRuleDO();
        prcProfLessCaseRuleDO.setContractPriceProfileSeq(1);
        prcProfLessCaseRuleDO.setGfsCustomerId("1");
        prcProfLessCaseRuleDO.setCreateUserId("test user");
        prcProfLessCaseRuleDO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfLessCaseRuleDO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfLessCaseRuleDO.setLastUpdateUserId("test user");
        prcProfLessCaseRuleDO.setCwMarkupAmnt(1);
        prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode("12");
        prcProfLessCaseRuleDO.setGfsCustomerTypeCode(1);
        prcProfLessCaseRuleDO.setItemPriceId("1");
        prcProfLessCaseRuleDO.setItemPriceLevelCode(1);
        prcProfLessCaseRuleDO.setLesscaseRuleId(1);
        prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(1);
        prcProfLessCaseRuleDO.setNonCwMarkupAmnt(3);
        prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode("12");
        prcProfLessCaseRuleDOListForCMG.add(prcProfLessCaseRuleDO);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(prcProfLessCaseRuleRepository.fetchPrcProfLessCaseRuleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO))
                .thenReturn(prcProfLessCaseRuleDOListForCMG);
        when(prcProfLessCaseRuleDOBuilder.buildPrcProfLessCaseRuleDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfLessCaseRuleDOListForCMG)).thenReturn(prcProfLessCaseRuleDOListForCMG);

        target.savePrcProfLessCaseRule(contractPriceProfileSeq, userId, contractCustomerMappingDTO, contractDetails, cmgCustomerResponseDTO);

        verify(prcProfLessCaseRuleRepository).fetchPrcProfLessCaseRuleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        verify(prcProfLessCaseRuleDOBuilder).buildPrcProfLessCaseRuleDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfLessCaseRuleDOListForCMG);
        verify(prcProfLessCaseRuleRepository).savePrcProfLessCaseRuleForCustomer(prcProfLessCaseRuleDOListForCMG);

    }
}
