package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.MemberHierarchyQueryProxy;

@Component("customerItemPricingCopier")
public class CustomerItemPricingCopier {

    private static final Logger logger = LoggerFactory.getLogger(CustomerItemPricingCopier.class);

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private MemberHierarchyQueryProxy memberHierarchyQueryProxy;

    @Autowired
    private MarkupDOBuilder markupDOBuilder;

    @Autowired
    private CustomerItemPriceBidLockinFilter customerItemPriceBidLockinFilter;

    public List<ProductTypeMarkupDO> extractItemAndCustomerMappingWithNoExistingBidEntries(int contractPriceProfileSeq, String userId,
            Date pricingEffectiveDate, Date pricingExpirationDate, List<ProductTypeMarkupDTO> markupDTOList) {

        logger.info("Fetch customer and its associated product/items for the contract {}", contractPriceProfileSeq);

        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(markupDTOList)) {

            List<ProductTypeMarkupDTO> itemMarkupTypeList = filterMarkupType(markupDTOList, ItemPriceLevel.ITEM.getCode());

            if (CollectionUtils.isNotEmpty(itemMarkupTypeList)) {

                List<ProductTypeMarkupDTO> markupDTOListWithNonBidPricing = filterMarkupsWithBidOverlappingLockins(itemMarkupTypeList, pricingEffectiveDate,
                        pricingExpirationDate);
                markupDTOList.removeAll(itemMarkupTypeList);
                markupDTOList.addAll(markupDTOListWithNonBidPricing);
            }

            if (!markupDTOList.isEmpty()) {
                logger.info("There exists some customer and its associated product/items for the contract {} which needs to be expired/added",
                        contractPriceProfileSeq);
                productTypeMarkupDOList = markupDOBuilder.buildProductMarkupDOList(userId, pricingEffectiveDate, pricingExpirationDate, markupDTOList);
            }
        }
        return productTypeMarkupDOList;
    }

    private List<ProductTypeMarkupDTO> filterMarkupType(List<ProductTypeMarkupDTO> markupDTOList, int markupType) {
        List<ProductTypeMarkupDTO> returnMarkupDTOList = new ArrayList<>();
        for (ProductTypeMarkupDTO markupDTO : markupDTOList) {
            if (markupType == Integer.parseInt(markupDTO.getProductType())) {
                returnMarkupDTOList.add(markupDTO);
            }
        }
        return returnMarkupDTOList;

    }

    public void saveNonBidItemAndCustomerMapping(int contractPriceProfileSeq, String userId, List<ProductTypeMarkupDO> productTypeMarkupDOList) {
        logger.info("saving entries into cip table for the contract price profile sequence {} ", contractPriceProfileSeq);

        if (CollectionUtils.isNotEmpty(productTypeMarkupDOList)) {
            customerItemPriceRepository.saveMarkup(productTypeMarkupDOList, userId, contractPriceProfileSeq);
        }
    }

    private List<ProductTypeMarkupDTO> filterMarkupsWithBidOverlappingLockins(List<ProductTypeMarkupDTO> productTypeMarkupList, Date effectiveDate,
            Date expirationDate) {

        Map<CustomerPK, List<CustomerPK>> customerMapWithHierarchyDetails = memberHierarchyQueryProxy.findCustomerHierarchy(productTypeMarkupList,
                effectiveDate);

        return customerItemPriceBidLockinFilter.filterMarkupsHavingBidLockinsAtHierarchy(productTypeMarkupList, customerMapWithHierarchyDetails,
                effectiveDate, expirationDate);
    }

}
