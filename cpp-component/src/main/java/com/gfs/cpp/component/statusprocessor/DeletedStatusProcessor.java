package com.gfs.cpp.component.statusprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class DeletedStatusProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DeletedStatusProcessor.class);

    static final String UPDATE_USER_ID = "Deleted Status Processor";

    @Autowired
    private ContractPriceExpirer contractPriceExpirer;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    void process(int contractPriceProfileSeq, ContractPriceProfileStatus cppStatus) {

        if (cppStatus != ContractPriceProfileStatus.DELETED) {
            logger.info("processing Deleted action for contract {}", contractPriceProfileSeq);

            if (cppStatus == ContractPriceProfileStatus.PRICING_ACTIVATED) {
                contractPriceExpirer.expirePriceForContract(contractPriceProfileSeq, UPDATE_USER_ID);
            }

            logger.info("updating the contract status to Deleted for contract {}", contractPriceProfileSeq);
            contractPriceProfileRepository.updateContractStatusWithLastUpdateUserIdByCppSeq(contractPriceProfileSeq, ContractPriceProfileStatus.DELETED.code,
                    UPDATE_USER_ID);
        }
    }

    public void process(String agreementId) {

        ContractPricingResponseDTO contractPricingDetails = contractPriceProfileRepository.fetchContractDetailsByAgreementId(agreementId);

        if (contractPricingDetails != null) {

            ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus
                    .getStatusByCode(contractPricingDetails.getContractPriceProfStatusCode());

            process(contractPricingDetails.getContractPriceProfileSeq(), cppStatus);
        }
    }

}
