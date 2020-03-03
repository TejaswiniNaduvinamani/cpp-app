package com.gfs.cpp.component.contractpricing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusMaintainer;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Service
public class ContractPricingService {
    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Autowired
    private ClmContractTypeRepository contractTypeRepository;

    @Autowired
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Autowired
    private ContractPricingCreator contractPricingCreator;

    @Autowired
    private ContractPricingUpdater contractPricingUpdater;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Autowired
    private ContractPriceProfileStatusMaintainer contractPriceProfileStatusMaintainer;

    @Autowired
    private ClmApiProxy clmApiProxy;

    @Autowired
    private CPPDateUtils cppDateUtils;

    static final Logger logger = LoggerFactory.getLogger(ContractPricingService.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveContractPricing(ContractPricingDTO contractPricingDTO, String userName) {
        int contractPriceProfileSeq = contractPricingDTO.getContractPriceProfileSeq();
        int contractTypeSeq = contractTypeRepository.fetchContractTypeSequenceByTypeName(contractPricingDTO.getContractType());
        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        if (contractPricingResponseDTO == null) {
            contractPricingDTO.setExpireLowerLevelInd(null);
            contractPricingCreator.saveContractPricingInformation(contractPricingDTO, userName, contractTypeSeq);
        } else {
            contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPricingDTO.getContractPriceProfileSeq());
            contractPricingUpdater.updateContractPricingInformation(contractPricingDTO, userName);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deletePricingExhibit(String agreementId, String userName) {
        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByAgreementId(agreementId);
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPricingResponseDTO.getContractPriceProfileSeq());
        clmApiProxy.deletePricingExhibit(contractPricingResponseDTO.getPricingExhibitSysId());
        contractPriceProfileRepository.updatePricingExhibitGuid(contractPricingResponseDTO.getContractPriceProfileSeq(), null, userName);
    }

    public int fetchCPPSequence(int contractPriceProfileId) {
        return contractPriceProfileRepository.fetchCPPSequence(contractPriceProfileId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public ContractPricingDTO fetchPricingInformation(int contractPriceProfileSeq, String agreementId, String clmContractStatus) {
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        if (contractPricingResponseDTO == null) {
            contractPricingDTO = buildDefaultPricingDTO(contractPriceProfileSeq);
        } else {
            contractPriceProfileStatusMaintainer.syncWithClmStatus(contractPricingResponseDTO, clmContractStatus);
            fetchSavedPricingDTO(contractPricingDTO, contractPriceProfileSeq, contractPricingResponseDTO);
        }
        return contractPricingDTO;
    }

    private void fetchSavedPricingDTO(ContractPricingDTO contractPricingDTO, int contractPriceProfileSeq,
            ContractPricingResponseDTO contractPricingResponseDTO) {

        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq);

        contractPricingDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractPricingDTO.setPricingEffectiveDate(contractPricingResponseDTO.getPricingEffectiveDate());
        contractPricingDTO.setClmContractStartDate(contractPricingResponseDTO.getClmContractStartDate());
        contractPricingDTO.setClmContractEndDate(contractPricingResponseDTO.getClmContractEndDate());
        contractPricingDTO.setPricingExpirationDate(contractPricingResponseDTO.getPricingExpirationDate());
        contractPricingDTO.setPricingExhibitSysId(contractPricingResponseDTO.getPricingExhibitSysId());
        contractPricingDTO.setTransferFeeFlag(contractPricingResponseDTO.getTransferFeeFlag() == 0 ? false : true);
        contractPricingDTO.setAssessmentFeeFlag(contractPricingResponseDTO.getAssessmentFeeFlag() == 0 ? false : true);
        contractPricingDTO.setPriceAuditFlag(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq,
                cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode()) == 1 ? true : false);

        contractPricingDTO
                .setPriceVerificationFlag(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq) == 0 ? false : true);
        int costRunSchedule = prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(),
                cmgCustomerResponseDTO.getTypeCode());

        if (costRunSchedule == CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M) {
            contractPricingDTO.setScheduleForCostChange(CPPConstants.SCHEDULE_FOR_COST_GREGORIAN);
        } else if (costRunSchedule == CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M) {
            contractPricingDTO.setScheduleForCostChange(CPPConstants.SCHEDULE_FOR_COST_GFS_FISCAL);
        }
    }

    private ContractPricingDTO buildDefaultPricingDTO(int contractPriceProfileSeq) {
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(false);
        contractPricingDTO.setPriceAuditFlag(false);
        contractPricingDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractPricingDTO.setScheduleForCostChange(CPPConstants.SCHEDULE_FOR_COST_GFS_FISCAL);
        contractPricingDTO.setPriceVerificationFlag(false);
        contractPricingDTO.setTransferFeeFlag(false);
        contractPricingDTO.setPricingExhibitSysId(null);
        contractPricingDTO.setPricingExpirationDate(cppDateUtils.getFutureDate());
        return contractPricingDTO;
    }
}