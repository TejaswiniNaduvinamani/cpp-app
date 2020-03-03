package com.gfs.cpp.component.customerinfo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.furtherance.FurtheranceService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class AmendmentContractCreator {

    @Autowired
    private ContractDataCopier contractDataCopier;

    @Autowired
    private CppVersionCreator cppVersionCreator;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private FurtheranceService furtheranceService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CPPInformationDTO createNewContractVersion(ClmContractResponseDTO agreementData) {

        validateAmendmentEffectiveDate(agreementData.getAmendmentEffectiveDate(), agreementData.getContractExpirationDate());
        validateCLMStatusInDraft(agreementData);
        validateNoInProgressContractVersion(agreementData.getParentAgreementId());
        validateNoInProgressFurtherance(agreementData.getParentAgreementId());

        Integer latestVersionNumber = contractPriceProfileRepository.fetchLatestContractVersionNumber(agreementData.getParentAgreementId());

        if (latestVersionNumber == null) {
            logger.info("No existing version found for the parent agreement id: {}", agreementData.getParentAgreementId());
            return cppVersionCreator.createInitialVersion();
        } else {

            ContractPricingResponseDTO contractDetailsOfLatestVersion = fetchContractDetailsForLatestActivatedVersion(
                    agreementData.getParentAgreementId());
            CPPInformationDTO cppInformationDTO = cppVersionCreator.createNextCppVersion(latestVersionNumber,
                    contractDetailsOfLatestVersion.getContractPriceProfileId());

            contractDataCopier.copyContractDataToNewVersion(contractDetailsOfLatestVersion, agreementData, cppInformationDTO);
            return cppInformationDTO;
        }
    }

    private void validateAmendmentEffectiveDate(Date amendmentEffectiveDate, Date contractEndDate) {

        if (amendmentEffectiveDate.compareTo(contractEndDate) > 0) {
            logger.error(
                    "Amendment effective date is found to be past the Contract Expiration date. Amendment effecive and Contract expiration dates are {} , {}",
                    amendmentEffectiveDate, contractEndDate);
            throw new CPPRuntimeException(CPPExceptionType.INVALID_AMENDMENT_EFFECTIVE_DATE,
                    "Amendment Effective Date is greater than Contract End Date. Please change Amendment Effective Date in CLM to be less than Contract End Date.");
        }
    }

    private void validateCLMStatusInDraft(ClmContractResponseDTO agreementData) {
        if (!ClmContractStatus.DRAFT.value.equals(agreementData.getContractStatus())) {
            logger.error("Contract with agreement id {} in not in Draft status. Its currently in status {} ", agreementData.getContractAgreementId(),
                    agreementData.getContractStatus());
            throw new CPPRuntimeException(CPPExceptionType.CLM_STATUS_NOT_DRAFT, "Contract status in CLM is not DRAFT mode.");
        }
    }

    private ContractPricingResponseDTO fetchContractDetailsForLatestActivatedVersion(String parentAgreementId) {
        logger.info("Finding contract details for the existing contract version with the parent agreement id: {}", parentAgreementId);
        return contractPriceProfileRepository.fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);
    }

    private void validateNoInProgressContractVersion(String parentAgreementId) {
        int count = contractPriceProfileRepository.fetchInProgressContractVersionCount(parentAgreementId);
        if (count > 0) {
            throw new CPPRuntimeException(CPPExceptionType.IN_PROGREESS_VERSION_FOUND,
                    "The previous version of this Contract is still being worked upon. ");
        }
    }

    private void validateNoInProgressFurtherance(String parentAgreementId) {
        if (furtheranceService.hasInProgressFurtherance(parentAgreementId)) {
            logger.error("Contract with agreement id {} has in progress furtherance.", parentAgreementId);
            throw new CPPRuntimeException(CPPExceptionType.IN_PROGRESS_FURTHERANCE_FOUND,
                    "A furtherance on this contract is still being worked on. Please resolve that before creating a new amendment.");
        }
    }
}
