package com.gfs.cpp.component.statusprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Component
public class ContractPriceProfileStatusMaintainer {

    private static final Logger logger = LoggerFactory.getLogger(ContractPriceProfileStatusMaintainer.class);

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Autowired
    private CppStatusProcessorPicker cppStatusProcessorPicker;
    @Autowired
    private ContractPriceExpirer contractPriceExpirer;
    @Autowired
    private ClmApiProxy clmApiProxy;
    @Autowired
    private ClmContractTypeRepository clmContractTypeRepository;

    public void syncWithClmStatus(ContractPricingResponseDTO contractPricingDetails, String clmStatus) {

        ContractPriceProfileStatus currentCppStatus = ContractPriceProfileStatus
                .getStatusByCode(contractPricingDetails.getContractPriceProfStatusCode());

        CppStatusChangeProcessor statusChangeProcessor = cppStatusProcessorPicker.pickStatusChangeProcessor(clmStatus);

        if (statusChangeProcessor != null && statusChangeProcessor.shouldUpdateStatus(currentCppStatus)) {

            logger.info("synching up the status for contract {} with clm status {} and cpp status {}",
                    contractPricingDetails.getContractPriceProfileSeq(), clmStatus, currentCppStatus);

            String updateUserId = statusChangeProcessor.getUpdateUserId();

            if (statusChangeProcessor.expireRequired(currentCppStatus)) {
                expireLatestPricing(contractPricingDetails, clmStatus, statusChangeProcessor.getUpdateToStatus(), updateUserId);
            }

            if (currentCppStatus == ContractPriceProfileStatus.CANCELLED
                    && hasOtherInProgressVersion(contractPricingDetails.getClmParentAgreementId())) {
                logger.info("Found another version for the same contract {} in progress, hence not processing status change for cancelled version",
                        contractPricingDetails.getContractPriceProfileSeq());
            } else {
                updateStatus(contractPricingDetails.getContractPriceProfileSeq(), statusChangeProcessor.getUpdateToStatus(), updateUserId);
            }
        }

    }

    private void expireLatestPricing(ContractPricingResponseDTO contractPricingDetails, String clmStatus,
            ContractPriceProfileStatus contractPriceProfileStatus, String updateUserId) {
        if (ClmContractStatus.TERMINATED.value.equals(clmStatus)) {
            expirePriceForTerminatedContract(contractPricingDetails, contractPriceProfileStatus, updateUserId);
        } else if (ClmContractStatus.EXPIRED.value.equals(clmStatus)) {
            expirePriceForExpiredContract(contractPricingDetails, contractPriceProfileStatus, updateUserId);
        } else {
            contractPriceExpirer.expirePriceForContract(contractPricingDetails.getContractPriceProfileSeq(), updateUserId);
        }
    }

    void expirePriceForTerminatedContract(ContractPricingResponseDTO contractPricingDetails, ContractPriceProfileStatus contractPriceProfileStatus,
            String updateUserId) {

        ContractPricingResponseDTO latestActivatedPricing = contractPriceProfileRepository
                .fetchContractDetailsForLatestActivatedContractVersion(contractPricingDetails.getClmAgreementId());
        if (latestActivatedPricing != null) {
            contractPriceExpirer.expirePriceForContract(latestActivatedPricing.getContractPriceProfileSeq(), updateUserId);

            if (latestActivatedPricing.getContractPriceProfileSeq() != contractPricingDetails.getContractPriceProfileSeq()) {
                updateStatus(latestActivatedPricing.getContractPriceProfileSeq(), contractPriceProfileStatus, updateUserId);
            }
        }
    }

    void expirePriceForExpiredContract(ContractPricingResponseDTO contractPricingDetails, ContractPriceProfileStatus contractPriceProfileStatus,
            String updateUserId) {
        ContractPricingResponseDTO latestActivatedPricing = contractPriceProfileRepository
                .fetchContractDetailsForLatestActivatedContractVersion(contractPricingDetails.getClmAgreementId());
        if (latestActivatedPricing != null) {
            processExpirationForExpiredContract(contractPricingDetails, contractPriceProfileStatus, updateUserId, latestActivatedPricing);
        }

    }

    private void processExpirationForExpiredContract(ContractPricingResponseDTO contractPricingDetails,
            ContractPriceProfileStatus contractPriceProfileStatus, String updateUserId, ContractPricingResponseDTO latestActivatedPricing) {
        if (latestActivatedPricing.getContractPriceProfileSeq() == contractPricingDetails.getContractPriceProfileSeq()) {
            contractPriceExpirer.expirePriceForContract(latestActivatedPricing.getContractPriceProfileSeq(), updateUserId);
        } else {
            String contractTypeName = clmContractTypeRepository.getContractTypeName(latestActivatedPricing.getClmContractTypeSeq());
            ClmContractResponseDTO latestAgreementDataFromClm = clmApiProxy.getAgreementData(latestActivatedPricing.getClmAgreementId(),
                    contractTypeName);

            if (ClmContractStatus.EXPIRED.value.equals(latestAgreementDataFromClm.getContractStatus())) {
                contractPriceExpirer.expirePriceForContract(latestActivatedPricing.getContractPriceProfileSeq(), updateUserId);
                updateStatus(latestActivatedPricing.getContractPriceProfileSeq(), contractPriceProfileStatus, updateUserId);
            }
        }
    }

    private boolean hasOtherInProgressVersion(String clmParentAgreementId) {
        return contractPriceProfileRepository.fetchInProgressContractVersionCount(clmParentAgreementId) > 0;
    }

    void updateStatus(int contractPriceProfileSeq, ContractPriceProfileStatus statusToUpdate, String updateUserId) {

        logger.info("updating the contract status to {} for contract {}", statusToUpdate, contractPriceProfileSeq);
        contractPriceProfileRepository.updateContractStatusWithLastUpdateUserIdByCppSeq(contractPriceProfileSeq, statusToUpdate.code, updateUserId);
    }

}
