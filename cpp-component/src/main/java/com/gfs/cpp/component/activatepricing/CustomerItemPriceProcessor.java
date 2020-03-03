package com.gfs.cpp.component.activatepricing;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.component.contractpricing.CustomerItemPriceExpirer;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class CustomerItemPriceProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CustomerItemPriceProcessor.class);

    @Autowired
    private CustomerItemPriceExpirer customerItemPriceExpirer;

    @Autowired
    private CustomerItemPricingCopier customerItemPricingCopier;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private ContractPriceExpirer contractPriceExpirer;

    public void expireAndSaveCustomerItemPriceProfile(int contractPriceProfileSeq, int latestActivatedPricingCPPSeq, String userId,
            Date expirationDateForExistingPricing, Date newPricingEffectiveDate, Date newPricingExpiryDate) {

        logger.info("Expiring Price profile entries for cpp seq {}", contractPriceProfileSeq);

        expireCustomerItemPricingForCPPSequence(latestActivatedPricingCPPSeq, userId, expirationDateForExistingPricing);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = customerItemPriceRepository.fetchPricingForRealCustomer(contractPriceProfileSeq);

        expireAndSaveForMarkupToBeAdded(contractPriceProfileSeq, userId, expirationDateForExistingPricing, newPricingEffectiveDate,
                newPricingExpiryDate, productTypeMarkupDTOList);
    }

    public void expireAndSaveForMarkupToBeAdded(int contractPriceProfileSeq, String userId, Date expirationDateForExistingPricing,
            Date newPricingEffectiveDate, Date newPricingExpiryDate, List<ProductTypeMarkupDTO> productTypeMarkupDTOList) {
        
        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = customerItemPricingCopier
                .extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId, newPricingEffectiveDate, newPricingExpiryDate,
                        productTypeMarkupDTOList);

        expireItemPricingForRealCustomer(expirationDateForExistingPricing, userId, customerItemMappingWithNoExistingBidEntries,
                newPricingEffectiveDate, newPricingExpiryDate);

        savePricingForRealCustomers(contractPriceProfileSeq, userId, customerItemMappingWithNoExistingBidEntries);
    }
    
    protected void expireCustomerItemPricingForCPPSequence(int latestActivatedPricingCPPSeq, String lastUpdateUserId, Date expirationDate) {
        if (latestActivatedPricingCPPSeq > 0) {
            logger.info("Expiring existing price profile entries for latest activated cpp seq {}", latestActivatedPricingCPPSeq);
            contractPriceExpirer.expireCustomerItemPricingForCPPSequence(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
        }
    }

    protected void expireItemPricingForRealCustomer(Date expirationDateForExistingPricing, String userId,
            List<ProductTypeMarkupDO> productTypeMarkupDOList, Date newPricingEffectiveDate, Date newPricingExpiryDate) {
        logger.info("Expiring existing pricing entries in the CIP table overlapping the pricing dates {} - {} ", newPricingEffectiveDate,
                newPricingExpiryDate);
        customerItemPriceExpirer.expireItemPricingForRealCustomer(expirationDateForExistingPricing, userId, productTypeMarkupDOList,
                newPricingEffectiveDate, newPricingExpiryDate);
    }

    protected void savePricingForRealCustomers(int contractPriceProfileSeq, String userId, List<ProductTypeMarkupDO> productTypeMarkupDOList) {

        logger.info("Saving pricing entries for the real customer in the CIP table for sequence number {}", contractPriceProfileSeq);

        customerItemPricingCopier.saveNonBidItemAndCustomerMapping(contractPriceProfileSeq, userId, productTypeMarkupDOList);
    }
}
