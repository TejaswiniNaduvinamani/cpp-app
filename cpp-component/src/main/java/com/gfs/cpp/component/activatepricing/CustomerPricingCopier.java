package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Component("customerPricingCopier")
public class CustomerPricingCopier {

    private static final Logger logger = LoggerFactory.getLogger(CustomerPricingCopier.class);

    @Autowired
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Autowired
    private PrcProfAuditAuthorityDOBuilder prcProfAuditAuthorityDOBuilder;

    @Autowired
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Autowired
    private PrcProfCostSchedulePkgDOBuilder prcProfCostSchedulePkgDOBuilder;

    @Autowired
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Autowired
    private PrcProfCostSchedulePkgScheduledGroupDOBuilder prcProfCostSchedulePkgScheduledGroupDOBuilder;

    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Autowired
    private PrcProfNonBrktCstMdlDOBuilder prcProfNonBrktCstMdlDOBuilder;

    @Autowired
    private PrcProfPricingRuleOvrdDOBuilder prcProfPricingRuleOvrdDOBuilder;

    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Autowired
    private PrcProfLessCaseRuleDOBuilder prcProfLessCaseRuleDOBuilder;

    public void savePrcProfAuditAuthority(int contractPriceProfileSeq, String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, CMGCustomerResponseDTO cmgCustomerResponseDTO) {

        logger.info("Saving Pricing entries to PRC_PROF_AUDIT_AUTHORIT table for customer {} for contract {}",
                defaultContractCustMapping.getGfsCustomerId(), contractPriceProfileSeq);

        List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList = prcProfAuditAuthorityRepository
                .fetchPrcProfAuditAuthorityForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        if (prcProfAuditAuthorityDTOList != null && !prcProfAuditAuthorityDTOList.isEmpty()) {
            List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = prcProfAuditAuthorityDOBuilder.buildPrcProfAuditAuthorityDTOList(userId,
                    defaultContractCustMapping, contractDetails, prcProfAuditAuthorityDTOList);
            prcProfAuditAuthorityRepository.savePriceProfileAuditForCustomer(prcProfAuditAuthorityDOList);
        }

    }

    public void saveCostSchedulePkgAndPkgGroup(int contractPriceProfileSeq, String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        logger.info(
                "Saving Pricing entries to PRC_PROF_COST_SCHEDULE_PKG & PRC_PROF_COST_SCHEDULE_PKG_GROUP tables for default customer {} for contract {}",
                defaultContractCustMapping.getGfsCustomerId(), contractPriceProfileSeq);

        List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOListForCMG = prcProfCostSchedulePkgRepository
                .fetchPrcProfCostScheduleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        if (prcProfCostSchedulePkgDOListForCMG != null && !prcProfCostSchedulePkgDOListForCMG.isEmpty()) {
            int pkgCostScheduleSeqNumerForCMG = prcProfCostSchedulePkgDOListForCMG.get(0).getPrcProfCostSchedulePkgSeq();

            int pkgcostScehdeleSeqNumerForCustomer = prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq();

            List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOList = new ArrayList<>();
            PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = prcProfCostSchedulePkgDOBuilder.buildPrcProfCostSchedulePkgDOList(userId,
                    defaultContractCustMapping, contractDetails, pkgcostScehdeleSeqNumerForCustomer);
            prcProfCostSchedulePkgDOList.add(prcProfCostSchedulePkgDO);
            prcProfCostSchedulePkgRepository.savePrcProfCostScheduleForCustomer(prcProfCostSchedulePkgDOList);

            List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchedulePkgScheduledGroupDTOListForCMG = prcProfCostRunSchedGroupRepository
                    .fetchPrcProfCostRunSchedGroupForCPPSeq(contractPriceProfileSeq, pkgCostScheduleSeqNumerForCMG);

            List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchedulePkgScheduledGroupDOList = prcProfCostSchedulePkgScheduledGroupDOBuilder
                    .buildPrcProfCostSchedulePkgScheduledGroupDOList(prcProfCostSchedulePkgScheduledGroupDTOListForCMG,
                            pkgcostScehdeleSeqNumerForCustomer);
            prcProfCostRunSchedGroupRepository.savePrcProfCostRunSchedGroupForCustomer(prcProfCostSchedulePkgScheduledGroupDOList);

        }
    }

    public void savePrcProfNonBrktCstMdl(int contractPriceProfileSeq, String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, CMGCustomerResponseDTO cmgCustomerResponseDTO) {

        logger.info("Saving Pricing entries to PRC_PROF_NON_BRKT_CST_MDL tables for default customer {} for contract {}",
                defaultContractCustMapping.getGfsCustomerId(), contractPriceProfileSeq);

        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);

        if (prcProfNonBrktCstMdlDTOList != null && !prcProfNonBrktCstMdlDTOList.isEmpty()) {
            List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = prcProfNonBrktCstMdlDOBuilder.buildPrcProfNonBrktCstMdlDOList(userId,
                    defaultContractCustMapping, contractDetails, prcProfNonBrktCstMdlDTOList);
            prcProfNonBrktCstMdlRepository.savePrcProfNonBrktCstMdlForCustomer(prcProfNonBrktCstMdlDOList);
        }

    }

    public void savePrcProfPricingRuleOvrd(int contractPriceProfileSeq, String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, CMGCustomerResponseDTO cmgCustomerResponseDTO) {

        logger.info("Saving Pricing entries to PRC_PROF_PRICING_RULE_OVRD tables for default customer {} for contract {}",
                defaultContractCustMapping.getGfsCustomerId(), contractPriceProfileSeq);

        List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList = prcProfPricingRuleOvrdRepository
                .fetchPrcProfPricingRuleOvrdForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);

        if (prcProfPricingRuleOvrdDTOList != null && !prcProfPricingRuleOvrdDTOList.isEmpty()) {
            List<PrcProfPricingRuleOvrdDO> prcProfNonBrktCstMdlDOList = prcProfPricingRuleOvrdDOBuilder.buildPrcProfPricingRuleOvrdDOList(userId,
                    defaultContractCustMapping, contractDetails, prcProfPricingRuleOvrdDTOList);
            prcProfPricingRuleOvrdRepository.savePrcProfPricingRuleOvrdForCustomer(prcProfNonBrktCstMdlDOList);
        }

    }

    public void savePrcProfLessCaseRule(int contractPriceProfileSeq, String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, CMGCustomerResponseDTO cmgCustomerResponseDTO) {

        logger.info("Saving Pricing entries to PRC_PROF_LESSCASE_RULE tables for default customer {} for contract {}",
                defaultContractCustMapping.getGfsCustomerId(), contractPriceProfileSeq);

        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG = prcProfLessCaseRuleRepository
                .fetchPrcProfLessCaseRuleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);

        if (prcProfLessCaseRuleDOListForCMG != null && !prcProfLessCaseRuleDOListForCMG.isEmpty()) {
            List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList = prcProfLessCaseRuleDOBuilder.buildPrcProfLessCaseRuleDOList(userId,
                    defaultContractCustMapping, contractDetails, prcProfLessCaseRuleDOListForCMG);
            prcProfLessCaseRuleRepository.savePrcProfLessCaseRuleForCustomer(prcProfLessCaseRuleDOList);
        }

    }

}
