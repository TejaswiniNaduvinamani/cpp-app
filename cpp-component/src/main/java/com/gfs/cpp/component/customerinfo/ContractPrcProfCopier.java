package com.gfs.cpp.component.customerinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Component
public class ContractPrcProfCopier {

    @Autowired
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Autowired
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Autowired
    private PrcProfCostSchedulePkgScheduledGroupDOBuilder prcProfCostSchedulePkgScheduledGroupDOBuilder;

    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Autowired
    private PrcProfNonBrktCstMdlDOBuilder prcProfNonBrktCstMdlDOBuilder;

    @Autowired
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    @Autowired
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Autowired
    private PrcProfPricingRuleOvrdDOBuilder prcProfPricingRuleOvrdDOBuilder;

    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Autowired
    private PrcProfLessCaseRuleDOBuilder prcProfLessCaseRuleDOBuilder;

    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Autowired
    private PrcProfAuditAuthorityDOBuilder prcProfAuditAuthorityDOBuilder;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void copyPrcProfEntries(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date effectiveDate,
            Date expirationDate, CMGCustomerResponseDTO defaultCMGForLatestContractVer, Date farOutDate) {

        logger.info("Copying prc prof entries for contract {}", contractPriceProfileSequence);

        copyPrcProfCostSchedule(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate, defaultCMGForLatestContractVer);
        copyPrcProfNonBrktCostModel(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate, defaultCMGForLatestContractVer);

        copyPrcProfPricePrivilige(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, effectiveDate, expirationDate);

        copyPrcProfPricingRuleOvrd(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate, defaultCMGForLatestContractVer);

        copyPrcProfLessCaseRule(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate, defaultCMGForLatestContractVer);

        copyPrcProfAuditAuth(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate, defaultCMGForLatestContractVer);
    }

    void copyPrcProfCostSchedule(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date farOutDate,
            CMGCustomerResponseDTO defaultCMGForLatestContractVer) {

        logger.info("Copying prc prof cost schedule for contract {} from an existing contract with cpp seq {}", contractPriceProfileSequence,
                cppSeqForLatestContractVersion);

        List<PrcProfCostSchedulePkgDO> costSchedulePkgSeqListForLatestContactVersion = prcProfCostSchedulePkgRepository
                .fetchPrcProfCostScheduleForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);
        List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOForAmendment = new ArrayList<>();

        if (costSchedulePkgSeqListForLatestContactVersion != null && !costSchedulePkgSeqListForLatestContactVersion.isEmpty()) {
            PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDOForLatestVersion = costSchedulePkgSeqListForLatestContactVersion.get(0);
            int prcProfileCostSchedulePkgSeq = prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq();
            PrcProfCostSchedulePkgDO costSchedulePkg = populatePrcProfCostSchedulePkgDO(contractPriceProfileSequence, userId, farOutDate,
                    prcProfCostSchedulePkgDOForLatestVersion, prcProfileCostSchedulePkgSeq);
            prcProfCostSchedulePkgDOForAmendment.add(costSchedulePkg);
            prcProfCostSchedulePkgRepository.savePrcProfCostScheduleForCustomer(prcProfCostSchedulePkgDOForAmendment);

            logger.info("Post saving cost schedule Pkg. Invoking process to save cost run schedule group for contract {} ",
                    contractPriceProfileSequence);

            List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchPkgSGrpDTOListForLatestContactVersion = prcProfCostRunSchedGroupRepository
                    .fetchPrcProfCostRunSchedGroupForCPPSeq(prcProfCostSchedulePkgDOForLatestVersion.getContractPriceProfileSeq(),
                            prcProfCostSchedulePkgDOForLatestVersion.getPrcProfCostSchedulePkgSeq());

            List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchPkgSGrpDTOListForAmendment = new ArrayList<>();
            for (PrcProfCostSchedulePkgScheduledGroupDTO inputDTO : ListUtils.emptyIfNull(prcProfCostSchPkgSGrpDTOListForLatestContactVersion)) {
                PrcProfCostSchedulePkgScheduledGroupDTO amendmentDTO = new PrcProfCostSchedulePkgScheduledGroupDTO();
                amendmentDTO.setContractPriceSeq(contractPriceProfileSequence);
                amendmentDTO.setCostRunFrequencyCode(inputDTO.getCostRunFrequencyCode());
                amendmentDTO.setPrcProfCostSchedulePkgSeq(prcProfileCostSchedulePkgSeq);
                amendmentDTO.setScheduleGroup(inputDTO.getScheduleGroup());
                prcProfCostSchPkgSGrpDTOListForAmendment.add(amendmentDTO);

            }
            if (!prcProfCostSchPkgSGrpDTOListForAmendment.isEmpty()) {
                List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchPkgSGrpDOOListForAmendment = prcProfCostSchedulePkgScheduledGroupDOBuilder
                        .buildPrcProfCostSchedulePkgScheduledGroupDOList(prcProfCostSchPkgSGrpDTOListForAmendment, prcProfileCostSchedulePkgSeq);
                prcProfCostRunSchedGroupRepository.savePrcProfCostRunSchedGroupForCustomer(prcProfCostSchPkgSGrpDOOListForAmendment);
            }
        }
    }

    private PrcProfCostSchedulePkgDO populatePrcProfCostSchedulePkgDO(int contractPriceProfileSequence, String userId, Date farOutDate,
            PrcProfCostSchedulePkgDO costSchedulePkgSeqForLatestContactVersion, int prcProfileCostSchedulePkgSeq) {
        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO.setContractPriceProfileSeq(contractPriceProfileSequence);
        prcProfCostSchedulePkgDO.setCreateUserId(userId);
        prcProfCostSchedulePkgDO.setLastUpdateUserId(userId);
        prcProfCostSchedulePkgDO.setEffectiveDate(farOutDate);
        prcProfCostSchedulePkgDO.setExpirationDate(farOutDate);
        prcProfCostSchedulePkgDO.setGfsCustomerId(costSchedulePkgSeqForLatestContactVersion.getGfsCustomerId());
        prcProfCostSchedulePkgDO.setGfsCustomerTypeCode(costSchedulePkgSeqForLatestContactVersion.getGfsCustomerTypeCode());
        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(prcProfileCostSchedulePkgSeq);
        return prcProfCostSchedulePkgDO;
    }

    void copyPrcProfPricePrivilige(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date effectiveDate,
            Date expirationDate) {
        logger.info("Copying prc prof price privilage for contract {} from an existing contract with cpp seq {}", contractPriceProfileSequence,
                cppSeqForLatestContractVersion);

        int pricePriviligeIndicator = prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(cppSeqForLatestContractVersion);
        prcProfVerificationPrivlgRepository.savePriceVerifyPrivileges(contractPriceProfileSequence, userId, effectiveDate, expirationDate,
                pricePriviligeIndicator);
    }

    void copyPrcProfNonBrktCostModel(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date farOutDate,
            CMGCustomerResponseDTO defaultCMGForLatestContractVer) {

        logger.info("Copying prc prof non brkt cost model for contract {} from an existing contract with cpp seq {}", contractPriceProfileSequence,
                cppSeqForLatestContractVersion);

        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);

        if (prcProfNonBrktCstMdlDTOList != null && !prcProfNonBrktCstMdlDTOList.isEmpty()) {

            List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = prcProfNonBrktCstMdlDOBuilder
                    .buildPrcProfNonBrktCstMdlDOListForAmendment(contractPriceProfileSequence, userId, farOutDate, prcProfNonBrktCstMdlDTOList);
            prcProfNonBrktCstMdlRepository.savePrcProfNonBrktCstMdlForCustomer(prcProfNonBrktCstMdlDOList);
        }

    }

    void copyPrcProfPricingRuleOvrd(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date farOutDate,
            CMGCustomerResponseDTO defaultCMGForLatestContractVer) {

        logger.info("Copying prc prof pricing rule override for contract {} from an existing contract with cpp seq {}", contractPriceProfileSequence,
                cppSeqForLatestContractVersion);

        List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList = prcProfPricingRuleOvrdRepository
                .fetchPrcProfPricingRuleOvrdForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);
        if (CollectionUtils.isNotEmpty(prcProfPricingRuleOvrdDTOList)) {
            List<PrcProfPricingRuleOvrdDO> prcProfPricingRuleOvrdDOList = prcProfPricingRuleOvrdDOBuilder
                    .buildPrcProfPricingRuleOvrdDOListForAmendment(contractPriceProfileSequence, userId, farOutDate, prcProfPricingRuleOvrdDTOList);
            prcProfPricingRuleOvrdRepository.savePrcProfPricingRuleOvrdForCustomer(prcProfPricingRuleOvrdDOList);
        }
    }

    void copyPrcProfLessCaseRule(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date farOutDate,
            CMGCustomerResponseDTO defaultCMGForLatestContractVer) {

        logger.info("Copying prc prof less case rule indicator for contract {} from an existing contract with cpp seq {}",
                contractPriceProfileSequence, cppSeqForLatestContractVersion);

        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG = prcProfLessCaseRuleRepository
                .fetchPrcProfLessCaseRuleForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);

        if (prcProfLessCaseRuleDOListForCMG != null && !prcProfLessCaseRuleDOListForCMG.isEmpty()) {
            List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList = prcProfLessCaseRuleDOBuilder
                    .buildPrcProfLessCaseRuleDOListForAmendment(contractPriceProfileSequence, userId, farOutDate, prcProfLessCaseRuleDOListForCMG);
            prcProfLessCaseRuleRepository.savePrcProfLessCaseRuleForCustomer(prcProfLessCaseRuleDOList);
        }
    }

    void copyPrcProfAuditAuth(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date farOutDate,
            CMGCustomerResponseDTO defaultCMGForLatestContractVer) {

        logger.info("Copying prc prof audit authority for contract {} from an existing contract with cpp seq {}", contractPriceProfileSequence,
                cppSeqForLatestContractVersion);

        List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList = prcProfAuditAuthorityRepository
                .fetchPrcProfAuditAuthorityForCPPSeq(cppSeqForLatestContractVersion, defaultCMGForLatestContractVer);
        if (prcProfAuditAuthorityDTOList != null && !prcProfAuditAuthorityDTOList.isEmpty()) {
            List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = prcProfAuditAuthorityDOBuilder
                    .buildPrcProfAuditAuthorityDTOForAmendment(contractPriceProfileSequence, userId, farOutDate, prcProfAuditAuthorityDTOList);
            prcProfAuditAuthorityRepository.savePriceProfileAuditForCustomer(prcProfAuditAuthorityDOList);
        }
    }

}
