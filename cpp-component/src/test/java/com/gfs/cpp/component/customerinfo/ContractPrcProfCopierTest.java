package com.gfs.cpp.component.customerinfo;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;
import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;
import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;
import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.component.activatepricing.PrcProfAuditAuthorityDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfCostSchedulePkgScheduledGroupDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfLessCaseRuleDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfNonBrktCstMdlDOBuilder;
import com.gfs.cpp.component.activatepricing.PrcProfPricingRuleOvrdDOBuilder;
import com.gfs.cpp.component.customerinfo.ContractPrcProfCopier;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractPrcProfCopierTest {

    @InjectMocks
    @Spy
    private ContractPrcProfCopier target;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Mock
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Mock
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    @Mock
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Mock
    private PrcProfNonBrktCstMdlDOBuilder prcProfNonBrktCstMdlDOBuilder;

    @Mock
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Mock
    private PrcProfPricingRuleOvrdDOBuilder prcProfPricingRuleOvrdDOBuilder;

    @Mock
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Mock
    private PrcProfLessCaseRuleDOBuilder prcProfLessCaseRuleDOBuilder;

    @Mock
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Mock
    private PrcProfAuditAuthorityDOBuilder prcProfAuditAuthorityDOBuilder;

    @Mock
    private PrcProfCostSchedulePkgScheduledGroupDOBuilder prcProfCostSchedulePkgScheduledGroupDOBuilder;

    private int contractPriceProfileSequence = 3;
    private int cppSeqForLatestContractVersion = 2;
    private String userId = "Test";
    private Date effectiveDate = new Date();
    private Date expirationDate = new Date();
    private int existingCustomerSeq = 22;
    private String customerId = "123456";
    private Integer customerTypeCode = 0;
    private int prcSequence = 23;
    private int pricePriviligeIndicator = 1;
    private int prcProfCostSchedulePkgExistingSeq = 31;
    private Date farOutDate = new Date();
    private CMGCustomerResponseDTO defaultCMGForLatestContractVer = new CMGCustomerResponseDTO();

    @Before
    public void buildRequiredLists() {
        defaultCMGForLatestContractVer.setCppCustomerSeq(existingCustomerSeq);
        defaultCMGForLatestContractVer.setDefaultCustomerInd(1);
        defaultCMGForLatestContractVer.setId(customerId);
        defaultCMGForLatestContractVer.setTypeCode(customerTypeCode);
    }

    @Test
    public void shouldCopyPrcProfEntries() {
        CMGCustomerResponseDTO defaultCMGForLatestContractVer = null;

        target.copyPrcProfEntries(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, effectiveDate, expirationDate,
                defaultCMGForLatestContractVer, farOutDate);

        verify(target).copyPrcProfCostSchedule(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);
        verify(target).copyPrcProfNonBrktCostModel(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);

        verify(target).copyPrcProfPricePrivilige(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, effectiveDate, expirationDate);

        verify(target).copyPrcProfPricingRuleOvrd(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);

        verify(target).copyPrcProfLessCaseRule(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);

        verify(target).copyPrcProfAuditAuth(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);
    }

    @Test
    public void shouldCopyPrcProfCostSchedule() {

        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDOForAmendment = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDOForAmendment.setContractPriceProfileSeq(contractPriceProfileSequence);
        prcProfCostSchedulePkgDOForAmendment.setEffectiveDate(effectiveDate);
        prcProfCostSchedulePkgDOForAmendment.setExpirationDate(expirationDate);
        prcProfCostSchedulePkgDOForAmendment.setGfsCustomerId(customerId);
        prcProfCostSchedulePkgDOForAmendment.setGfsCustomerTypeCode(customerTypeCode);
        prcProfCostSchedulePkgDOForAmendment.setPrcProfCostSchedulePkgSeq(prcSequence);

        PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTO = new PrcProfCostSchedulePkgScheduledGroupDTO();
        prcProfCostSchedulePkgScheduledGroupDTO.setPrcProfCostSchedulePkgSeq(prcProfCostSchedulePkgExistingSeq);
        prcProfCostSchedulePkgScheduledGroupDTO.setContractPriceSeq(cppSeqForLatestContractVersion);
        prcProfCostSchedulePkgScheduledGroupDTO.setCostRunFrequencyCode("2");
        prcProfCostSchedulePkgScheduledGroupDTO.setScheduleGroup(1);
        List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchedulePkgScheduledGroupDTOListForCMG = Collections
                .singletonList(prcProfCostSchedulePkgScheduledGroupDTO);

        PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTOForAmendment = new PrcProfCostSchedulePkgScheduledGroupDTO();
        prcProfCostSchedulePkgScheduledGroupDTOForAmendment.setPrcProfCostSchedulePkgSeq(prcSequence);
        prcProfCostSchedulePkgScheduledGroupDTOForAmendment.setContractPriceSeq(contractPriceProfileSequence);
        prcProfCostSchedulePkgScheduledGroupDTOForAmendment.setCostRunFrequencyCode("2");
        prcProfCostSchedulePkgScheduledGroupDTOForAmendment.setScheduleGroup(1);
        List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchedulePkgScheduledGroupDTOListForAmendmentCMG = Collections
                .singletonList(prcProfCostSchedulePkgScheduledGroupDTOForAmendment);

        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO.setContractPriceProfileSeq(cppSeqForLatestContractVersion);
        prcProfCostSchedulePkgDO.setEffectiveDate(effectiveDate);
        prcProfCostSchedulePkgDO.setExpirationDate(expirationDate);
        prcProfCostSchedulePkgDO.setGfsCustomerId(customerId);
        prcProfCostSchedulePkgDO.setGfsCustomerTypeCode(customerTypeCode);
        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(prcProfCostSchedulePkgExistingSeq);
        List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOList = Collections.singletonList(prcProfCostSchedulePkgDO);

        PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO = new PrcProfCostSchedulePkgScheduledGroupDO();
        prcProfCostSchedulePkgScheduledGroupDO.setPrcProfCostSchedulePkgSeq(prcSequence);
        prcProfCostSchedulePkgScheduledGroupDO.setContractPriceSeq(contractPriceProfileSequence);
        prcProfCostSchedulePkgScheduledGroupDO.setCostRunFrequencyCode("2");
        prcProfCostSchedulePkgScheduledGroupDO.setScheduleGroup(1);
        List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchedulePkgScheduledGroupDOList = Collections
                .singletonList(prcProfCostSchedulePkgScheduledGroupDO);

        when(prcProfCostSchedulePkgRepository.fetchPrcProfCostScheduleForCPPSeq(eq(cppSeqForLatestContractVersion), eq(defaultCMGForLatestContractVer)))
                .thenReturn(prcProfCostSchedulePkgDOList);
        when(prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq()).thenReturn(prcSequence);

        when(prcProfCostRunSchedGroupRepository.fetchPrcProfCostRunSchedGroupForCPPSeq(eq(cppSeqForLatestContractVersion),
                eq(prcProfCostSchedulePkgExistingSeq))).thenReturn(prcProfCostSchedulePkgScheduledGroupDTOListForCMG);
        when(prcProfCostSchedulePkgScheduledGroupDOBuilder
                .buildPrcProfCostSchedulePkgScheduledGroupDOList(eq(prcProfCostSchedulePkgScheduledGroupDTOListForAmendmentCMG), eq(prcSequence)))
                        .thenReturn(prcProfCostSchedulePkgScheduledGroupDOList);

        target.copyPrcProfCostSchedule(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);

        verify(prcProfCostSchedulePkgRepository).fetchPrcProfCostScheduleForCPPSeq(eq(cppSeqForLatestContractVersion), eq(defaultCMGForLatestContractVer));
        verify(prcProfCostSchedulePkgRepository).fetchPrcProfileCostSchedulePkgNextSeq();
        verify(prcProfCostRunSchedGroupRepository).fetchPrcProfCostRunSchedGroupForCPPSeq(eq(cppSeqForLatestContractVersion),
                eq(prcProfCostSchedulePkgExistingSeq));
        verify(prcProfCostSchedulePkgScheduledGroupDOBuilder)
                .buildPrcProfCostSchedulePkgScheduledGroupDOList(eq(prcProfCostSchedulePkgScheduledGroupDTOListForAmendmentCMG), eq(prcSequence));

        verify(prcProfCostRunSchedGroupRepository).savePrcProfCostRunSchedGroupForCustomer(eq(prcProfCostSchedulePkgScheduledGroupDOList));
    }

    @Test
    public void shouldCopyPrcProfPricePrivilige() {
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(cppSeqForLatestContractVersion)).thenReturn(pricePriviligeIndicator);
        target.copyPrcProfPricePrivilige(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, effectiveDate, expirationDate);
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(cppSeqForLatestContractVersion);
        verify(prcProfVerificationPrivlgRepository).savePriceVerifyPrivileges(eq(contractPriceProfileSequence), eq(userId), eq(effectiveDate),
                eq(expirationDate), eq(pricePriviligeIndicator));

    }

    @Test
    public void shouldCopyPrcProfNonBrktCostModel() {

        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(cppSeqForLatestContractVersion);
        prcProfNonBrktCstMdlDTO.setCostModelId(2);
        prcProfNonBrktCstMdlDTO.setItemPriceId("1");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(2);
        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = Collections.singletonList(prcProfNonBrktCstMdlDTO);

        PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO = new PrcProfNonBrktCstMdlDO();
        prcProfNonBrktCstMdlDO.setContractPriceProfileSeq(contractPriceProfileSequence);
        prcProfNonBrktCstMdlDO.setCostModelId(2);
        prcProfNonBrktCstMdlDO.setCreateUserId("test user");
        prcProfNonBrktCstMdlDO.setEffectiveDate(effectiveDate);
        prcProfNonBrktCstMdlDO.setExpirationDate(expirationDate);
        prcProfNonBrktCstMdlDO.setGfsCustomerId(customerId);
        prcProfNonBrktCstMdlDO.setGfsCustomerTypeCode(customerTypeCode);
        prcProfNonBrktCstMdlDO.setItemPriceId("1");
        prcProfNonBrktCstMdlDO.setItemPriceLevelCode(2);
        prcProfNonBrktCstMdlDO.setLastUpdateUserId("test user");
        List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = Collections.singletonList(prcProfNonBrktCstMdlDO);

        when(prcProfNonBrktCstMdlRepository.fetchPrcProfNonBrktCstMdlForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer))
                .thenReturn(prcProfNonBrktCstMdlDTOList);
        when(prcProfNonBrktCstMdlDOBuilder.buildPrcProfNonBrktCstMdlDOListForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfNonBrktCstMdlDTOList)).thenReturn(prcProfNonBrktCstMdlDOList);

        target.copyPrcProfNonBrktCostModel(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);

        verify(prcProfNonBrktCstMdlRepository).fetchPrcProfNonBrktCstMdlForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);
        verify(prcProfNonBrktCstMdlDOBuilder).buildPrcProfNonBrktCstMdlDOListForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfNonBrktCstMdlDTOList);
        verify(prcProfNonBrktCstMdlRepository).savePrcProfNonBrktCstMdlForCustomer(eq(prcProfNonBrktCstMdlDOList));

    }

    @Test
    public void shouldCopyPrcProfPricingRuleOvrd() {

        PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO = new PrcProfPricingRuleOvrdDO();
        prcProfPricingRuleOvrdDO.setContractPriceProfileSeq(contractPriceProfileSequence);
        prcProfPricingRuleOvrdDO.setCreateUserId("test user");
        prcProfPricingRuleOvrdDO.setEffectiveDate(effectiveDate);
        prcProfPricingRuleOvrdDO.setExpirationDate(expirationDate);
        prcProfPricingRuleOvrdDO.setGfsCustomerId(customerId);
        prcProfPricingRuleOvrdDO.setGfsCustomerTypeCode(customerTypeCode);
        prcProfPricingRuleOvrdDO.setLastUpdateUserId("test user");
        prcProfPricingRuleOvrdDO.setPricingOverrideId(1);
        prcProfPricingRuleOvrdDO.setPricingOverrideInd(2);
        List<PrcProfPricingRuleOvrdDO> prcProfPricingRuleOvrdDOList = Collections.singletonList(prcProfPricingRuleOvrdDO);

        List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList = new ArrayList<>();
        PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO = new PrcProfPricingRuleOvrdDTO();
        prcProfPricingRuleOvrdDTO.setContractPriceProfileSeq(cppSeqForLatestContractVersion);
        prcProfPricingRuleOvrdDTOList.add(prcProfPricingRuleOvrdDTO);

        when(prcProfPricingRuleOvrdRepository.fetchPrcProfPricingRuleOvrdForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer))
                .thenReturn(prcProfPricingRuleOvrdDTOList);
        when(prcProfPricingRuleOvrdDOBuilder.buildPrcProfPricingRuleOvrdDOListForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfPricingRuleOvrdDTOList)).thenReturn(prcProfPricingRuleOvrdDOList);

        target.copyPrcProfPricingRuleOvrd(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);

        verify(prcProfPricingRuleOvrdRepository).fetchPrcProfPricingRuleOvrdForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);
        verify(prcProfPricingRuleOvrdDOBuilder).buildPrcProfPricingRuleOvrdDOListForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfPricingRuleOvrdDTOList);
        verify(prcProfPricingRuleOvrdRepository).savePrcProfPricingRuleOvrdForCustomer(eq(prcProfPricingRuleOvrdDOList));
    }

    @Test
    public void shouldCopyPrcProfLessCaseRule() {

        PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = new PrcProfLessCaseRuleDO();
        prcProfLessCaseRuleDO.setContractPriceProfileSeq(contractPriceProfileSequence);
        prcProfLessCaseRuleDO.setGfsCustomerId(customerId);
        prcProfLessCaseRuleDO.setCreateUserId("test user");
        prcProfLessCaseRuleDO.setEffectiveDate(effectiveDate);
        prcProfLessCaseRuleDO.setExpirationDate(expirationDate);
        prcProfLessCaseRuleDO.setLastUpdateUserId("test user");
        prcProfLessCaseRuleDO.setCwMarkupAmnt(1);
        prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode("12");
        prcProfLessCaseRuleDO.setGfsCustomerTypeCode(customerTypeCode);
        prcProfLessCaseRuleDO.setItemPriceId("1");
        prcProfLessCaseRuleDO.setItemPriceLevelCode(1);
        prcProfLessCaseRuleDO.setLesscaseRuleId(1);
        prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(1);
        prcProfLessCaseRuleDO.setNonCwMarkupAmnt(3);
        prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode("12");
        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG = Collections.singletonList(prcProfLessCaseRuleDO);

        when(prcProfLessCaseRuleRepository.fetchPrcProfLessCaseRuleForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer))
                .thenReturn(prcProfLessCaseRuleDOListForCMG);
        when(prcProfLessCaseRuleDOBuilder.buildPrcProfLessCaseRuleDOListForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfLessCaseRuleDOListForCMG)).thenReturn(prcProfLessCaseRuleDOListForCMG);

        target.copyPrcProfLessCaseRule(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate,
                defaultCMGForLatestContractVer);

        verify(prcProfLessCaseRuleRepository).fetchPrcProfLessCaseRuleForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);
        verify(prcProfLessCaseRuleDOBuilder).buildPrcProfLessCaseRuleDOListForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfLessCaseRuleDOListForCMG);
        verify(prcProfLessCaseRuleRepository).savePrcProfLessCaseRuleForCustomer(eq(prcProfLessCaseRuleDOListForCMG));
    }

    @Test
    public void shouldCopyPrcProfAuditAuth() {

        PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO = new PrcProfAuditAuthorityDTO();
        prcProfAuditAuthorityDTO.setContractPriceProfileSeq(cppSeqForLatestContractVersion);
        prcProfAuditAuthorityDTO.setGfsCustomerId(customerId);
        prcProfAuditAuthorityDTO.setGfsCustomerType(customerTypeCode);
        prcProfAuditAuthorityDTO.setCreateUserId("test user");
        prcProfAuditAuthorityDTO.setEffectiveDate(effectiveDate);
        prcProfAuditAuthorityDTO.setExpirationDate(expirationDate);
        prcProfAuditAuthorityDTO.setLastUpdateUserId("test user");
        prcProfAuditAuthorityDTO.setPrcProfAuditAuthorityInd(1);
        List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList = Collections.singletonList(prcProfAuditAuthorityDTO);

        PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = new PrcProfAuditAuthorityDO();
        prcProfAuditAuthorityDO.setContractPriceProfileSeq(contractPriceProfileSequence);
        prcProfAuditAuthorityDO.setGfsCustomerId(customerId);
        prcProfAuditAuthorityDO.setGfsCustomerTypeCode(customerTypeCode);
        prcProfAuditAuthorityDO.setCreateUserId("test user");
        prcProfAuditAuthorityDO.setEffectiveDate(effectiveDate);
        prcProfAuditAuthorityDO.setExpirationDate(expirationDate);
        prcProfAuditAuthorityDO.setLastUpdateUserId("test user");
        prcProfAuditAuthorityDO.setPrcProfAuditAuthorityInd(1);
        List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = Collections.singletonList(prcProfAuditAuthorityDO);

        when(prcProfAuditAuthorityRepository.fetchPrcProfAuditAuthorityForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer))
                .thenReturn(prcProfAuditAuthorityDTOList);
        when(prcProfAuditAuthorityDOBuilder.buildPrcProfAuditAuthorityDTOForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfAuditAuthorityDTOList)).thenReturn(prcProfAuditAuthorityDOList);

        target.copyPrcProfAuditAuth(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate, defaultCMGForLatestContractVer);

        verify(prcProfAuditAuthorityRepository).fetchPrcProfAuditAuthorityForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);
        verify(prcProfAuditAuthorityDOBuilder).buildPrcProfAuditAuthorityDTOForAmendment(contractPriceProfileSequence, userId, farOutDate,
                prcProfAuditAuthorityDTOList);
        verify(prcProfAuditAuthorityRepository).savePriceProfileAuditForCustomer(eq(prcProfAuditAuthorityDOList));
    }

}
