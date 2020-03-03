package com.gfs.cpp.component.statusprocessor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Component
public class FurtheranceStatusValidator {

    @Autowired
    private ClmApiProxy clmApiProxy;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private CppUserDetailsService cppUserDetailsService;

    @Autowired
    private CppFurtheranceRepository cppFurtheranceRepository;

    static final Logger logger = LoggerFactory.getLogger(FurtheranceStatusValidator.class);

    public void validateIfFurtheranceEditableStatus(int contractPriceProfileSeq, int furtheranceSeq) {
        Integer status = cppFurtheranceRepository.fetchCPPFurtheranceStatus(contractPriceProfileSeq, furtheranceSeq);
        if (status == null || status == FurtheranceStatus.FURTHERANCE_ACTIVATED.getCode()) {
            logger.error("Furtherance not editable {}", furtheranceSeq);
            throw new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS, "Furtherance not in editable status");
        }
    }

    public void validateNewFurtheranceCreation(String parentAgreementId, String contractType) {
        validateUserHasEditAccessToFurtherance(parentAgreementId);
        validateAnyContractVersionInProgress(parentAgreementId);
        validCLMContractStatusAsExecuted(parentAgreementId, contractType);
    }

    private void validateAnyContractVersionInProgress(String parentAgreementId) {
        logger.info("Validating for any existing contract version in In-Progress state for the parentAgreemntId {}", parentAgreementId);

        int count = contractPriceProfileRepository.fetchInProgressContractVersionCount(parentAgreementId);
        if (count > 0) {
            logger.error("contract versions are inprogress state for parentAgreementId {}", parentAgreementId);
            throw new CPPRuntimeException(CPPExceptionType.CANNOT_CREATE_FURTHERANCE, "No contract versions are inprogress state.");
        }
    }

    private void validCLMContractStatusAsExecuted(String parentAgreementId, String contractType) {
        logger.info("Validating for CLM status to be executed for parentAgreementId {}", parentAgreementId);

        ClmContractResponseDTO clmContractResponseDTO = clmApiProxy.getAgreementData(parentAgreementId, contractType);
        if (!(ClmContractStatus.EXECUTED.value.equals(clmContractResponseDTO.getContractStatus()))) {
            logger.error("CLM status is not executed for parentAgreementId {}", parentAgreementId);
            throw new CPPRuntimeException(CPPExceptionType.CANNOT_CREATE_FURTHERANCE, "CLM status is not executed.");
        }
    }

    private void validateUserHasEditAccessToFurtherance(String parentAgreementId) {
        if (!cppUserDetailsService.hasContractEditAccess()) {
            logger.error("User doesn't have edit access for parentAgreementId {}", parentAgreementId);
            throw new CPPRuntimeException(CPPExceptionType.CANNOT_CREATE_FURTHERANCE, "User doesn't have edit access.");
        }
    }

    public void validateIfPricingCanBeActivated(Date pricingExpirationDate, String clmContractStatus, Date currentDate,
            FurtheranceInformationDTO furtheranceInformationDTO) {
        validateCLMStatus(clmContractStatus);
        validateIfContractExpired(pricingExpirationDate, currentDate);
        validateFurtheranceStatus(furtheranceInformationDTO);
    }

    public void validateIfActivatePricingCanBeEnabledForFurtherance(FurtheranceInformationDTO furtheranceInformationDTO, String clmContractStatus) {
        validateCLMStatus(clmContractStatus);
        validateFurtheranceStatus(furtheranceInformationDTO);
    }

    void validateFurtheranceStatus(FurtheranceInformationDTO furtheranceInformationDTO) {
        if (FurtheranceStatus.FURTHERANCE_SAVED.getCode() != furtheranceInformationDTO.getFurtheranceStatusCode()) {
            throw new CPPRuntimeException(CPPExceptionType.INVALID_FURTHERANCE_STATUS,
                    "Furtherance is in In-Progress. It has to be saved before pricing can be activated");
        }
    }

    void validateCLMStatus(String clmContractStatus) {
        if (!(ClmContractStatus.EXECUTED.value.equals(clmContractStatus) || ClmContractStatus.SUPERSEDED.value.equals(clmContractStatus))) {
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT,
                    String.format(
                            "The contract is not ready for activating price. Its CLM status is %s. Pricing can be activated only once the CLM status is EXECUTED.",
                            clmContractStatus));
        }
    }

    void validateIfContractExpired(Date pricingExpirationDate, Date currentDate) {
        if (pricingExpirationDate.compareTo(currentDate) < 0) {
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT,
                    "The expiration date of the contract is in past. It cannot be activated.");
        }
    }

}
