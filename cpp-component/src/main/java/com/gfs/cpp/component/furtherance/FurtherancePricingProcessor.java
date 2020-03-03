package com.gfs.cpp.component.furtherance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.furtherance.FurtheranceAction;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.CustomerItemPriceProcessor;
import com.gfs.cpp.component.activatepricing.CustomerItemPricingCopier;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Component
public class FurtherancePricingProcessor {

    static final Logger logger = LoggerFactory.getLogger(FurtherancePricingProcessor.class);

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Autowired
    private CustomerItemPricingCopier customerItemPricingCopier;

    @Autowired
    private CustomerItemPriceProcessor customerItemPriceProcessor;

    @Autowired
    private CPPDateUtils cppDateUtils;

    public void expireAndSaveFurtheranceUpdates(Date expirationDateToSetForExistingPricing, FurtheranceInformationDTO furtheranceInformationDTO,
            String userName) {

        Date newPricingExpiryDate = cppDateUtils.getFutureDate();

        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList = prcProfLessCaseRuleRepository.fetchPrcProfLessCaseForRealCustomersForFurtherance(
                furtheranceInformationDTO.getCppFurtheranceSeq(), furtheranceInformationDTO.getFurtheranceEffectiveDate());

        expirePrcProfLessCaseRuleForFurtherance(prcProfLessCaseRuleDOList, expirationDateToSetForExistingPricing, userName);
        savePrcProfLessCaseRule(prcProfLessCaseRuleDOList, userName);

        Map<Integer, List<ProductTypeMarkupDTO>> markupsForRealCustomer = customerItemPriceRepository
                .fetchMarkupsForRealCustomersForFurtherance(furtheranceInformationDTO.getCppFurtheranceSeq());

        List<ProductTypeMarkupDTO> productTypeMarkupDTOListToBeAddedOrUpdated = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(markupsForRealCustomer.get(FurtheranceAction.UPDATED.getCode()))) {
            productTypeMarkupDTOListToBeAddedOrUpdated.addAll(markupsForRealCustomer.get(FurtheranceAction.UPDATED.getCode()));
        }

        if (CollectionUtils.isNotEmpty(markupsForRealCustomer.get(FurtheranceAction.ADDED.getCode()))) {
            productTypeMarkupDTOListToBeAddedOrUpdated.addAll(markupsForRealCustomer.get(FurtheranceAction.ADDED.getCode()));
        }

        expireDeletedItems(expirationDateToSetForExistingPricing, furtheranceInformationDTO, userName, newPricingExpiryDate, markupsForRealCustomer);

        saveAndExpireForAddedOrUpdatedMarkups(expirationDateToSetForExistingPricing, furtheranceInformationDTO, userName, newPricingExpiryDate,
                productTypeMarkupDTOListToBeAddedOrUpdated);

    }

    private void saveAndExpireForAddedOrUpdatedMarkups(Date expirationDateToSetForExistingPricing,
            FurtheranceInformationDTO furtheranceInformationDTO, String userName, Date newPricingExpiryDate,
            List<ProductTypeMarkupDTO> productTypeMarkupDTOListToBeAddedOrUpdated) {

        if (CollectionUtils.isNotEmpty(productTypeMarkupDTOListToBeAddedOrUpdated)) {

            customerItemPriceProcessor.expireAndSaveForMarkupToBeAdded(furtheranceInformationDTO.getContractPriceProfileSeq(), userName,
                    expirationDateToSetForExistingPricing, furtheranceInformationDTO.getFurtheranceEffectiveDate(), newPricingExpiryDate,
                    productTypeMarkupDTOListToBeAddedOrUpdated);
        }
    }

    private void expireDeletedItems(Date expirationDateToSetForExistingPricing, FurtheranceInformationDTO furtheranceInformationDTO, String userName,
            Date newPricingExpiryDate, Map<Integer, List<ProductTypeMarkupDTO>> markupsForRealCustomer) {

        if (CollectionUtils.isNotEmpty(markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()))) {
            List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = customerItemPricingCopier
                    .extractItemAndCustomerMappingWithNoExistingBidEntries(furtheranceInformationDTO.getContractPriceProfileSeq(), userName,
                            furtheranceInformationDTO.getFurtheranceEffectiveDate(), newPricingExpiryDate,
                            markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()));

            customerItemPriceRepository.expireItemPricingForDeletedItemsDuringFurtherance(expirationDateToSetForExistingPricing, userName,
                    customerItemMappingWithNoExistingBidEntries);
        }
    }

    void expirePrcProfLessCaseRuleForFurtherance(List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList, Date expirationDateToSetForExistingPricing,
            String userName) {
        if (CollectionUtils.isNotEmpty(prcProfLessCaseRuleDOList)) {
            prcProfLessCaseRuleRepository.expirePrcProfLessCaseRuleForFurtherance(prcProfLessCaseRuleDOList, expirationDateToSetForExistingPricing,
                    userName);
        }
    }

    void savePrcProfLessCaseRule(List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList, String userName) {
        if (CollectionUtils.isNotEmpty(prcProfLessCaseRuleDOList)) {
            prcProfLessCaseRuleRepository.savePrcProfLessCaseRuleForFurtheranceUpdates(prcProfLessCaseRuleDOList, userName);
        }
    }

}