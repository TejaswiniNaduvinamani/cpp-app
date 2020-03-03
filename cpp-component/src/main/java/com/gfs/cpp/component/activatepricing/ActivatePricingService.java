package com.gfs.cpp.component.activatepricing;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Service("activatePricingService")
public class ActivatePricingService {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private ActivatePricingValidator activatePricingValidator;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private ContractCustomerMappingService contractCustomerMappingService;

    @Autowired
    private CustomerItemPriceProcessor customerItemPriceProcessor;

    @Autowired
    private CustomerPriceProfileProcessor customerPriceProfileProcessor;

    static final Logger logger = LoggerFactory.getLogger(ActivatePricingService.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void activatePricing(int contractPriceProfileSeq, String userId, boolean isAmendment, String clmContractStatus) {

        logger.info("Started price activation processing for contract  {}", contractPriceProfileSeq);

        ContractPricingResponseDTO contractDetails = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        Date currentDate = cppDateUtils.getCurrentDate();

        activatePricingValidator.validateContract(contractPriceProfileSeq, contractDetails, currentDate, isAmendment, clmContractStatus);
        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = contractCustomerMappingService
                .fetchAllConceptCustomerMapping(contractPriceProfileSeq);

        activatePricingValidator.validateCustomerMembershipWithDefaultCustomerMapping(contractPriceProfileSeq, contractCustomerMappingDTOList);

        updatePricingEffectiveDate(contractDetails, currentDate);
        Date expirationDateForExistingPricing = cppDateUtils.getPreviousDate(contractDetails.getPricingEffectiveDate());

        int cppSeqForLatestPricingActivated = fetchLatestCPPSeqForPricingActivated(contractDetails, isAmendment);

        customerPriceProfileProcessor.expireAndSavePriceProfile(cppSeqForLatestPricingActivated, userId, expirationDateForExistingPricing,
                contractDetails, contractCustomerMappingDTOList);

        customerItemPriceProcessor.expireAndSaveCustomerItemPriceProfile(contractPriceProfileSeq, cppSeqForLatestPricingActivated, userId,
                expirationDateForExistingPricing, contractDetails.getPricingEffectiveDate(), contractDetails.getPricingExpirationDate());

        contractPriceProfileRepository.updateToPriceActivateStatus(contractPriceProfileSeq, userId);
        logger.info("Completed processing for Price activation for contract {}", contractPriceProfileSeq);
    }

    private int fetchLatestCPPSeqForPricingActivated(ContractPricingResponseDTO contractDetails, boolean isAmendment) {
        int cppSeqForLatestActivateVersion = -1;
        if (isAmendment && contractDetails.getClmAgreementId() != contractDetails.getClmParentAgreementId()) {
            cppSeqForLatestActivateVersion = fetchCPPSequenceForLatestActivatedVersion(contractDetails.getClmParentAgreementId());
        }
        return cppSeqForLatestActivateVersion;
    }

    public Map<String, Boolean> validateActivatePricingEnabler(int contractPriceProfileSeq) {

        Map<String, Boolean> validateActivatePricingEnablerMap = new HashMap<>();
        boolean enableActivatePriceProfileButton = true;

        ContractPricingResponseDTO contractDetails = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        try {
            activatePricingValidator.validateIfActivatePricingCanBeEnabled(contractPriceProfileSeq, contractDetails);

        } catch (CPPRuntimeException cppre) {
            enableActivatePriceProfileButton = false;
            logger.info("Validation to enable activate price profile button failed for contract  {}", contractPriceProfileSeq);
        }
        validateActivatePricingEnablerMap.put(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON, enableActivatePriceProfileButton);
        return validateActivatePricingEnablerMap;
    }

    private void updatePricingEffectiveDate(ContractPricingResponseDTO contractDetails, Date currentDate) {
        if (contractDetails.getPricingEffectiveDate().compareTo(currentDate) < 0) {
            contractDetails.setPricingEffectiveDate(cppDateUtils.getCurrentDateAsLocalDate().toDate());
        }
    }

    int fetchCPPSequenceForLatestActivatedVersion(String parentAgreementId) {
        ContractPricingResponseDTO contractDetailsOfLatestVersion = contractPriceProfileRepository
                .fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);

        return contractDetailsOfLatestVersion.getContractPriceProfileSeq();
    }
}
