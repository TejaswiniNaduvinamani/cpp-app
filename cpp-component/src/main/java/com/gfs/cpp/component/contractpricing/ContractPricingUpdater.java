package com.gfs.cpp.component.contractpricing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.CostModelMapRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;

@Component
public class ContractPricingUpdater {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Autowired
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Autowired
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Autowired
    private CostModelMapRepository costModelMapRepository;

    @Autowired
    private ContractPricingDOBuilder contractPricingDOBuilder;

    static final Logger logger = LoggerFactory.getLogger(ContractPricingService.class);

    public void updateContractPricingInformation(ContractPricingDTO contractPricingDTO, String userName) {
        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchDefaultCmg(contractPricingDTO.getContractPriceProfileSeq());
        ContractPricingDO contractPricingDO = contractPricingDOBuilder.buildContractPricingDO(contractPricingDTO, cmgCustomerResponseDTO);
        if (cmgCustomerResponseDTO.getTypeCode() == CPPConstants.CMG_CUSTOMER_TYPE_CODE) {
            updateContractPricing(userName, contractPricingDO);
            updateAuditPrivileges(userName, contractPricingDO);
            updatePriceVerifyPrivileges(userName, contractPricingDO);
            updateSchedule(userName, contractPricingDO);
            updateCostModel(userName, contractPricingDO);
        }
    }

    private void updateContractPricing(String userName, ContractPricingDO contractPricingDO) {
        contractPriceProfileRepository.updateContractPriceProfile(contractPricingDO.getContractPriceProfileSeq(),
                contractPricingDO.getPricingEffectiveDate(), userName, contractPricingDO.getTransferFeeInd(),
                contractPricingDO.getLabelAssesmentInd());

    }

    private void updateAuditPrivileges(String userName, ContractPricingDO contractPricingDO) {
        int prcProfAuditAuthorityInd = prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPricingDO.getContractPriceProfileSeq(),
                contractPricingDO.getGfsCustomerId(), contractPricingDO.getCustomerTypeCode());
        if (contractPricingDO.getPriceAuditPrivileges() && prcProfAuditAuthorityInd == 0) {
            prcProfAuditAuthorityRepository.updatePriceProfileAuditIndicator(userName, contractPricingDO.getContractPriceProfileSeq(),
                    CPPConstants.INDICATOR_ONE);
        } else if (!contractPricingDO.getPriceAuditPrivileges() && prcProfAuditAuthorityInd == 1) {
            prcProfAuditAuthorityRepository.updatePriceProfileAuditIndicator(userName, contractPricingDO.getContractPriceProfileSeq(),
                    CPPConstants.INDICATOR_ZERO);
        }
    }

    private void updatePriceVerifyPrivileges(String userName, ContractPricingDO contractPricingDO) {
        prcProfVerificationPrivlgRepository.updatePriceProfileVerficationPrivlgInd(contractPricingDO, contractPricingDO.getPriceVerifInd(), userName);
    }

    private void updateSchedule(String userName, ContractPricingDO contractPricingDO) {
        int scheduleGroupSeq = prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPricingDO.getContractPriceProfileSeq(),
                contractPricingDO.getGfsCustomerId(), contractPricingDO.getCustomerTypeCode());
        if (CPPConstants.SCHEDULE_FOR_COST_GREGORIAN.equalsIgnoreCase(contractPricingDO.getScheduleForCostChange())
                && scheduleGroupSeq != CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M) {
            prcProfCostSchedulePkgRepository.updateLastUpdateUserId(userName, contractPricingDO.getContractPriceProfileSeq());
            prcProfCostRunSchedGroupRepository.updateScheduleGroupSeqMonthly(CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M,
                    contractPricingDO.getContractPriceProfileSeq());
        } else if (CPPConstants.SCHEDULE_FOR_COST_GFS_FISCAL.equalsIgnoreCase(contractPricingDO.getScheduleForCostChange())
                && scheduleGroupSeq != CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M) {
            prcProfCostSchedulePkgRepository.updateLastUpdateUserId(userName, contractPricingDO.getContractPriceProfileSeq());
            prcProfCostRunSchedGroupRepository.updateScheduleGroupSeqMonthly(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M,
                    contractPricingDO.getContractPriceProfileSeq());
        }
    }

    private void updateCostModel(String userName, ContractPricingDO contractPricingDO) {
        Integer newCostModelId = costModelMapRepository.fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd());
        Integer oldCostModelId = prcProfNonBrktCstMdlRepository.fetchCostModelId(contractPricingDO.getContractPriceProfileSeq());
        if (oldCostModelId != newCostModelId) {
            if (newCostModelId != null) {
                updateCostModelData(userName, newCostModelId, contractPricingDO.getContractPriceProfileSeq());
            } else {
                updateToDefaultCostModelData(userName, contractPricingDO.getContractPriceProfileSeq());
            }
        }
    }

    private void updateToDefaultCostModelData(String userName, int contractPriceProfileSeq) {
        prcProfNonBrktCstMdlRepository.updateCostModelData(userName, CPPConstants.DEFAULT_ITEM_COST_MODEL, CPPConstants.DEFAULT_SUBGROUP_COST_MODEL,
                CPPConstants.DEFAULT_PRODUCT_COST_MODEL, contractPriceProfileSeq);
    }

    private void updateCostModelData(String userName, int costModelId, int contractPriceProfileSeq) {
        prcProfNonBrktCstMdlRepository.updateCostModelData(userName, costModelId, costModelId, costModelId, contractPriceProfileSeq);
    }
}
