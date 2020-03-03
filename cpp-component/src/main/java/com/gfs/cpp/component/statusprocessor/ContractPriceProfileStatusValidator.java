package com.gfs.cpp.component.statusprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class ContractPriceProfileStatusValidator {

    private static final Logger logger = LoggerFactory.getLogger(ContractPriceProfileStatusValidator.class);

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    public void validateIfContractPricingEditableStatus(int contractPriceProfileSeq) {

        ContractPriceProfileStatus contractStatus = fetchContractStatus(contractPriceProfileSeq);
        if (ContractPriceProfileStatus.DRAFT != contractStatus) {
            logger.error("contract {} in status {}, cannot edit price profile ", contractPriceProfileSeq, contractStatus);
            throw new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS,
                    String.format("Contract pricing cannot be editable in %s status.", contractStatus));
        }
    }

    private ContractPriceProfileStatus fetchContractStatus(int contractPriceProfileSeq) {

        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        if (contractPricingResponseDTO == null) {
            throw new CPPRuntimeException(CPPExceptionType.NO_CONTRACT_FOUND,
                    String.format("No contract information found for contract price profile seq %s.", contractPriceProfileSeq));
        }
        return ContractPriceProfileStatus.getStatusByCode(contractPricingResponseDTO.getContractPriceProfStatusCode());
    }

    public void validateIfCustomerAssignmentEditableStatus(int contractPriceProfileSeq) {

        ContractPriceProfileStatus contractStatus = fetchContractStatus(contractPriceProfileSeq);
        if (ContractPriceProfileStatus.CONTRACT_APPROVED != contractStatus) {
            logger.error("contract {} in status {}, cannot edit customer assignment ", contractPriceProfileSeq, contractStatus);
            throw new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS,
                    String.format("Customer Mapping cannot be editable in %s status.", contractStatus));
        }
    }

    public void validateIfCostModelEditableStatus(int contractPriceProfileSeq) {

        ContractPriceProfileStatus contractStatus = fetchContractStatus(contractPriceProfileSeq);
        if (ContractPriceProfileStatus.CONTRACT_APPROVED != contractStatus) {
            logger.error("contract {} in status {}, cannot edit cost model ", contractPriceProfileSeq, contractStatus);
            throw new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS,
                    String.format("Cost Model cannot be editable in %s status.", contractStatus));
        }
    }

    public void validateIfItemAssignmentEditableStatus(int contractPriceProfileSeq) {

        ContractPriceProfileStatus contractStatus = fetchContractStatus(contractPriceProfileSeq);
        if (ContractPriceProfileStatus.CONTRACT_APPROVED != contractStatus) {
            logger.error("contract {} in status {}, cannot edit item assignment ", contractPriceProfileSeq, contractStatus);
            throw new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS,
                    String.format("Item Mapping cannot be editable in %s status.", contractStatus));
        }
    }

}
