package com.gfs.cpp.component.activatepricing;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.component.contractpricing.ContractPriceProfileExpirer;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;

@Component
public class CustomerPriceProfileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CustomerPriceProfileProcessor.class);

    @Autowired
    private ContractPriceProfileExpirer contractPriceProfileExpirer;

    @Autowired
    private CustomerPricingCopier customerPricingCopier;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private ContractPriceExpirer contractPriceExpirer;

    public void expireAndSavePriceProfile(int latestActivatedPricingCPPSeq, String userId, Date expirationDateForExistingPricing,
            ContractPricingResponseDTO contractDetails, List<ContractCustomerMappingDTO> contractCustomerMappingDTOList) {

        logger.info("Expiring Price profile entries for cpp seq {}", contractDetails.getContractPriceProfileSeq());

        Date newPricingEffectiveDate = contractDetails.getPricingEffectiveDate();
        Date newPricingExpiryDate = contractDetails.getPricingExpirationDate();

        expirePriceProfileForCPPSequence(latestActivatedPricingCPPSeq, userId, expirationDateForExistingPricing);

        ContractCustomerMappingDTO defaultContractCustMapping = findDefaultMarkupCustomerMappingDTO(contractCustomerMappingDTOList);

        expirePriceProfileDetails(expirationDateForExistingPricing, userId, newPricingEffectiveDate, newPricingExpiryDate,
                defaultContractCustMapping);

        savePriceProfileForRealCustomers(contractDetails.getContractPriceProfileSeq(), userId, contractDetails, defaultContractCustMapping);
    }

    protected void expirePriceProfileForCPPSequence(int latestActivatedPricingCPPSeq, String lastUpdateUserId, Date expirationDate) {
        if (latestActivatedPricingCPPSeq > 0) {
            logger.info("Expiring existing price profile entries for latest activated cpp seq {}", latestActivatedPricingCPPSeq);

            contractPriceExpirer.expirePriceProfileForCPPSequence(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
        }
    }

    private ContractCustomerMappingDTO findDefaultMarkupCustomerMappingDTO(List<ContractCustomerMappingDTO> contractCustomerMappingDTOList) {
        ContractCustomerMappingDTO retrunDTO = new ContractCustomerMappingDTO();
        for (ContractCustomerMappingDTO contractCustomerMappingDTO : contractCustomerMappingDTOList) {
            if (contractCustomerMappingDTO.getDefaultCustomerInd() == 1) {
                retrunDTO = contractCustomerMappingDTO;
                break;
            }
        }
        return retrunDTO;
    }

    protected void expirePriceProfileDetails(Date expirationDateForExistingPricing, String userId, Date newPricingEffectiveDate,
            Date newPricingExpiryDate, ContractCustomerMappingDTO defaultContractCustMapping) {
        logger.info("Expiring existing pricing entries in the PRC table overlapping the pricing dates {} - {} ", newPricingEffectiveDate,
                newPricingExpiryDate);
        contractPriceProfileExpirer.expireAllPriceProfileDataForRealCust(expirationDateForExistingPricing, userId,
                defaultContractCustMapping.getGfsCustomerId(), defaultContractCustMapping.getGfsCustomerTypeCode(), newPricingEffectiveDate,
                newPricingExpiryDate);
    }

    protected void savePriceProfileForRealCustomers(int contractPriceProfileSeq, String userId, ContractPricingResponseDTO contractDetails,
            ContractCustomerMappingDTO defaultContractCustMapping) {
        logger.info("Saving pricing profile entries for the real customer in the contract price profile sequence number {}", contractPriceProfileSeq);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq);

        customerPricingCopier.savePrcProfAuditAuthority(contractPriceProfileSeq, userId, defaultContractCustMapping, contractDetails,
                cmgCustomerResponseDTO);
        customerPricingCopier.saveCostSchedulePkgAndPkgGroup(contractPriceProfileSeq, userId, defaultContractCustMapping, contractDetails,
                cmgCustomerResponseDTO);
        customerPricingCopier.savePrcProfNonBrktCstMdl(contractPriceProfileSeq, userId, defaultContractCustMapping, contractDetails,
                cmgCustomerResponseDTO);
        customerPricingCopier.savePrcProfPricingRuleOvrd(contractPriceProfileSeq, userId, defaultContractCustMapping, contractDetails,
                cmgCustomerResponseDTO);
        customerPricingCopier.savePrcProfLessCaseRule(contractPriceProfileSeq, userId, defaultContractCustMapping, contractDetails,
                cmgCustomerResponseDTO);
    }
}
