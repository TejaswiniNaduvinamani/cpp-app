package com.gfs.cpp.component.contractpricing;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class ContractPricingCreator {

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

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
    
    @Autowired
    private CostModelService costModelService;

    static final Logger logger = LoggerFactory.getLogger(ContractPricingService.class);

    public void saveContractPricingInformation(ContractPricingDTO contractPricingDTO, String userName, int contractTypeSeq) {
        ContractPricingDO contractPricingDO = contractPricingDOBuilder.buildContractPricingDO(contractPricingDTO,
                createCMGCustomerGroup(contractPricingDTO));
        saveContractPricing(userName, contractTypeSeq, contractPricingDO);
        saveAuditPrivileges(userName, contractPricingDO, contractPricingDO.getContractPriceProfileSeq());
        savePriceVerifyPrivileges(userName, contractPricingDO);
        saveSchedule(userName, contractPricingDO);
        saveCostModel(userName, contractPricingDO);
    }

    void saveContractPricing(String userName, int contractTypeSeq, ContractPricingDO contractPricingDO) {
        if (contractPricingDO.getParentAgreementId() == null) {
            contractPricingDO.setParentAgreementId(contractPricingDO.getAgreementId());
        }
        contractPriceProfileRepository.saveContractPricingDetails(contractPricingDO, userName, contractTypeSeq,
                contractPricingDO.getContractPriceProfileSeq());
        int cppCustomerSeq = contractPriceProfCustomerRepository.fetchCPPCustomerNextSequence();

        contractPriceProfCustomerRepository.saveContractPriceProfCustomer(contractPricingDO.getContractPriceProfileSeq(),
                contractPricingDO.getGfsCustomerId(), contractPricingDO.getCustomerTypeCode(), userName, CPPConstants.INDICATOR_ONE, cppCustomerSeq);
    }

    private void saveAuditPrivileges(String userName, ContractPricingDO contractPricingDO, int contractPriceProfileSeq) {
        if (contractPricingDO.getPriceAuditPrivileges()) {
            prcProfAuditAuthorityRepository.savePriceProfileAuditIndicator(userName, contractPriceProfileSeq, contractPricingDO, CPPConstants.INDICATOR_ONE);
        } else {
            prcProfAuditAuthorityRepository.savePriceProfileAuditIndicator(userName, contractPriceProfileSeq, contractPricingDO,
                    CPPConstants.INDICATOR_ZERO);
        }
    }

    private void savePriceVerifyPrivileges(String userName, ContractPricingDO contractPricingDO) {
        if (contractPricingDO.getPriceVerifyPrivileges()) {
            prcProfVerificationPrivlgRepository.savePriceVerifyPrivileges(contractPricingDO.getContractPriceProfileSeq(), userName,
                    contractPricingDO.getPricingEffectiveDate(), contractPricingDO.getPricingExpirationDate(), CPPConstants.INDICATOR_ONE);
        } else {
            prcProfVerificationPrivlgRepository.savePriceVerifyPrivileges(contractPricingDO.getContractPriceProfileSeq(), userName,
                    contractPricingDO.getPricingEffectiveDate(), contractPricingDO.getPricingExpirationDate(), CPPConstants.INDICATOR_ZERO);
        }
    }

    private void saveSchedule(String userName, ContractPricingDO contractPricingDO) {
        int costSchedulePkgSeq = prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq();
        prcProfCostSchedulePkgRepository.saveScheduleForCostChange(userName, contractPricingDO.getContractPriceProfileSeq(), contractPricingDO,
                costSchedulePkgSeq);
        if (CPPConstants.SCHEDULE_FOR_COST_GREGORIAN.equalsIgnoreCase(contractPricingDO.getScheduleForCostChange())) {
            prcProfCostRunSchedGroupRepository.insertSchedule(contractPricingDO.getContractPriceProfileSeq(), costSchedulePkgSeq,
                    CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_W, CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M);
        } else if (CPPConstants.SCHEDULE_FOR_COST_GFS_FISCAL.equalsIgnoreCase(contractPricingDO.getScheduleForCostChange())) {
            prcProfCostRunSchedGroupRepository.insertSchedule(contractPricingDO.getContractPriceProfileSeq(), costSchedulePkgSeq,
                    CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_W, CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M);
        }
    }

    private void saveCostModel(String userName, ContractPricingDO contractPricingDO) {
        
        Set<Integer> productIDs = costModelService.fetchIncludedListOfProductTypeIDs();
        Integer costModelId = costModelMapRepository.fetchCostModelId(contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd(),
                contractPricingDO.getPriceAuditInd(), contractPricingDO.getPriceVerifInd());
        if (costModelId != null) {
            saveCostModelData(userName, contractPricingDO, costModelId, productIDs);
        } else {
            saveDefaultCostModelData(userName, contractPricingDO, productIDs);
        }
    }

    private void saveDefaultCostModelData(String userName, ContractPricingDO contractPricingDO, Set<Integer> productIDs) {
        prcProfNonBrktCstMdlRepository.saveCostModelData(userName, contractPricingDO, CPPConstants.DEFAULT_ITEM_COST_MODEL,
                CPPConstants.DEFAULT_SUBGROUP_COST_MODEL, CPPConstants.DEFAULT_PRODUCT_COST_MODEL, productIDs);
    }

    private void saveCostModelData(String userName, ContractPricingDO contractPricingDO, int costModelId, Set<Integer> productIDs) {
        prcProfNonBrktCstMdlRepository.saveCostModelData(userName, contractPricingDO, costModelId, costModelId, costModelId, productIDs);
    }

    private CMGCustomerResponseDTO createCMGCustomerGroup(ContractPricingDTO contractPricingDTO) {
        String contractName = contractPricingDTO.getContractName();
        contractName = contractName.substring(0, Math.min(contractName.length(), CPPConstants.MAX_CONTRACT_NAME_LENGTH_FOR_CMG));
        return customerServiceProxy.createCMGCustomerGroup(contractPricingDTO.getContractPriceProfileId(), contractName);
    }

}
