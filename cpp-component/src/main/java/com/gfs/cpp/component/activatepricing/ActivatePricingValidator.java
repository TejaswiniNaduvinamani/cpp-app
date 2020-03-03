package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.assignment.CustomerHierarchyValidator;

@Component("activatePricingValidator")
public class ActivatePricingValidator {

    private static final Logger logger = LoggerFactory.getLogger(ActivatePricingValidator.class);
    
    @Autowired
    ContractCustomerMappingService contractCustomerMappingService;

    @Autowired
    private CustomerHierarchyValidator customerHierarchyValidator;

    @Autowired
    private CPPDateUtils cppDateUtils;
    
    public void validateContract(int contractPriceProfileSeq, ContractPricingResponseDTO contractDetails, Date currentDate, boolean isAmendment,
            String clmContractStatus) {

        validateIsContractApproved(contractDetails);
        validateCLMStatusForAmendment(isAmendment, clmContractStatus);
        validateConceptHasCustomerMapping(contractPriceProfileSeq);
        validateIfContractExpired(contractDetails, currentDate);
    }

    public void validateIfActivatePricingCanBeEnabled(int contractPriceProfileSeq, ContractPricingResponseDTO contractDetails) {
        validateIsContractApproved(contractDetails);
        validateConceptHasCustomerMapping(contractPriceProfileSeq);
        validateIfContractExpired(contractDetails, cppDateUtils.getCurrentDate());

    }

    private void validateConceptHasCustomerMapping(int contractPriceProfileSeqs) {
        int count = contractCustomerMappingService.fetchUnmappedConceptCount(contractPriceProfileSeqs);
        if (count > 0) {
            
            logger.info("Price Activation Validation failed because there are concepts on the contract without valid mappings");
            
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT,
                    "The contract is not ready for price activation. There are concepts on the contract without valid mapping(s).");
        }
    }

    private void validateIsContractApproved(ContractPricingResponseDTO contractDetails) {
        if (ContractPriceProfileStatus.CONTRACT_APPROVED.getCode() != contractDetails.getContractPriceProfStatusCode()) {
            
            logger.info("Price Activation Validation failed because contract is not in valid status in CPP. Its status code is {}",
                    contractDetails.getContractPriceProfStatusCode());
            
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT,
                    String.format("The contract is not ready for activating price. It is in %s status.",
                            ContractPriceProfileStatus.getStatusByCode(contractDetails.getContractPriceProfStatusCode()).desc));
        }
    }

    void validateCLMStatusForAmendment(boolean isAmendment, String clmContractStatus) {
        if (isAmendment && !contractExecuted(clmContractStatus)) {
            
            logger.info("Price Activation Validation failed because clm status for the contract is not in Executed status. Its currently in {}",
                    clmContractStatus);
            
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT, String.format(
                    "The contract is not ready for activating price. Its CLM status %s. Pricing can be activated only once the CLM status is EXECUTED.",
                    clmContractStatus));
        }
    }

    private boolean contractExecuted(String clmContractStatus) {
        return ClmContractStatus.EXECUTED.value.equals(clmContractStatus) || ClmContractStatus.SUPERSEDED.value.equals(clmContractStatus);
    }

    private void validateIfContractExpired(ContractPricingResponseDTO contractDetails, Date currentDate) {
        
        if (contractDetails.getClmContractEndDate().compareTo(currentDate) < 0) {
            
            logger.info("Price Activation Validation failed because contract end date: {} is found to be less then the system date {}",
                    contractDetails.getClmContractEndDate(), currentDate);
            
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT,
                    "The expiration date of the contract is in past. It cannot be activated.");
        }
    }

    public void validateCustomerMembershipWithDefaultCustomerMapping(int contractPriceProfileSeq,
            List<ContractCustomerMappingDTO> contractCustomerMappingDTOList) {

        List<RealCustomerDTO> findCustomersNotAMember = customerHierarchyValidator.findCustomersNotAMember(contractPriceProfileSeq,
                buildRealCustomersMappedToException(contractCustomerMappingDTOList));
        if (CollectionUtils.isNotEmpty(findCustomersNotAMember)) {
            throw new CPPRuntimeException(CPPExceptionType.NOT_A_MEMBER_OF_DEFAULT_CUSTOMER,
                    "Please ensure all customers assigned to the concepts belong to the same customer hierarchy.");
        }
    }

    private List<RealCustomerDTO> buildRealCustomersMappedToException(List<ContractCustomerMappingDTO> contractCustomerMappingDTOList) {

        List<RealCustomerDTO> realCustomersMappedToException = new ArrayList<>();
        for (ContractCustomerMappingDTO contractCustomerMappingDTO : contractCustomerMappingDTOList) {
            if (contractCustomerMappingDTO.getDefaultCustomerInd() != 1) {
                realCustomersMappedToException.add(buildRealCustomerDto(contractCustomerMappingDTO));
            }

        }
        return realCustomersMappedToException;
    }

    private RealCustomerDTO buildRealCustomerDto(ContractCustomerMappingDTO contractCustomerMappingDTO) {
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerId(contractCustomerMappingDTO.getGfsCustomerId());
        realCustomerDTO.setRealCustomerType(contractCustomerMappingDTO.getGfsCustomerTypeCode());
        return realCustomerDTO;
    }
}
