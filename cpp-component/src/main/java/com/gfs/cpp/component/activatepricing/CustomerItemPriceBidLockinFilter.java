package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component("customerItemPriceBidLockinFilter")
public class CustomerItemPriceBidLockinFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomerItemPriceBidLockinFilter.class);

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    public List<ProductTypeMarkupDTO> filterMarkupsHavingBidLockinsAtHierarchy(List<ProductTypeMarkupDTO> productTypeMarkupList,
            Map<CustomerPK, List<CustomerPK>> customersHierarchyByCustomers, Date effectiveDate, Date expirationDate) {

        List<String> allItems = extractAllItemids(productTypeMarkupList);

        logger.info("Filter list of CIP entries to be created for the items - {} for effective and expiration date as {} - {}", allItems,
                effectiveDate, expirationDate);

        List<CustomerPK> allCustomersInHierarchy = extractAllCustomersInHierarcy(customersHierarchyByCustomers);

        List<ProductTypeMarkupDTO> existingBidLockingProductTypeDTO = customerItemPriceRepository
                .fetchExistingBidLockinEntriesForCustomer(allCustomersInHierarchy, allItems, effectiveDate, expirationDate);

        if (CollectionUtils.isEmpty(existingBidLockingProductTypeDTO)) {
            return productTypeMarkupList;
        }

        return filterMarkups(productTypeMarkupList, customersHierarchyByCustomers, existingBidLockingProductTypeDTO);

    }

    @SuppressWarnings({ "rawtypes" })
    private List<ProductTypeMarkupDTO> filterMarkups(List<ProductTypeMarkupDTO> productTypeMarkupList,
            Map<CustomerPK, List<CustomerPK>> customersHierarchyByCustomers, List<ProductTypeMarkupDTO> existingBidLockingProductTypeDTO) {
        List<ProductTypeMarkupDTO> productTypeMarkupListTobeSaved = new ArrayList<>();

        Map<MultiKey, ProductTypeMarkupDTO> mapOfItemAndCustomerWithBidEntries = groupExistingBidLockinsByCustomerItem(
                existingBidLockingProductTypeDTO);

        for (ProductTypeMarkupDTO markupDTO : productTypeMarkupList) {

            boolean isExistigEntryPresent = isCustomerHavingExistingBidLockinForItem(markupDTO, customersHierarchyByCustomers,
                    mapOfItemAndCustomerWithBidEntries);

            if (!isExistigEntryPresent) {
                productTypeMarkupListTobeSaved.add(markupDTO);
            } else {
                logger.info("Existing bid entry exists for the Customer ID-{}, Type-{}, itemPriceId - {}, not creating lockin",
                        markupDTO.getGfsCustomerId(), markupDTO.getGfsCustomerTypeCode(), markupDTO.getItemPriceId());
            }
        }
        return productTypeMarkupListTobeSaved;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map<MultiKey, ProductTypeMarkupDTO> groupExistingBidLockinsByCustomerItem(List<ProductTypeMarkupDTO> existingBidLockingProductTypeDTO) {
        Map<MultiKey, ProductTypeMarkupDTO> mapOfItemAndCustomerWithBidEntries = new HashMap<>();

        for (ProductTypeMarkupDTO existingMarkup : existingBidLockingProductTypeDTO) {
            MultiKey multikey = new MultiKey(existingMarkup.getItemPriceId(), existingMarkup.getGfsCustomerId(),
                    existingMarkup.getGfsCustomerTypeCode());
            mapOfItemAndCustomerWithBidEntries.put(multikey, existingMarkup);
        }
        return mapOfItemAndCustomerWithBidEntries;
    }

    private List<String> extractAllItemids(List<ProductTypeMarkupDTO> productTypeMarkupList) {
        List<String> itemIdList = new ArrayList<>();

        for (ProductTypeMarkupDTO prodTypeMarkup : productTypeMarkupList) {
            itemIdList.add(String.valueOf(prodTypeMarkup.getItemPriceId()));
        }
        return itemIdList;
    }

    private List<CustomerPK> extractAllCustomersInHierarcy(Map<CustomerPK, List<CustomerPK>> customerMapWithHierarchyDetails) {
        List<CustomerPK> uniqueCustomerList = new ArrayList<>();
        for (Entry<CustomerPK, List<CustomerPK>> custHierarcyEntry : customerMapWithHierarchyDetails.entrySet()) {
            uniqueCustomerList.addAll(custHierarcyEntry.getValue());
        }
        return uniqueCustomerList;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean isCustomerHavingExistingBidLockinForItem(ProductTypeMarkupDTO markupDTO,
            Map<CustomerPK, List<CustomerPK>> customerMapWithHierarchyDetails,
            Map<MultiKey, ProductTypeMarkupDTO> mapOfItemAndCustomerWithBidEntries) {

        boolean isExisting = false;

        CustomerPK customerMappedToItem = new CustomerPK();
        customerMappedToItem.setId(markupDTO.getGfsCustomerId());
        customerMappedToItem.setTypeCode(markupDTO.getGfsCustomerTypeCode());

        List<CustomerPK> ancestors = customerMapWithHierarchyDetails.get(customerMappedToItem);
        for (CustomerPK customerPk : ancestors) {
            MultiKey multiKeyAncestors = new MultiKey(markupDTO.getItemPriceId(), customerPk.getId(), customerPk.getTypeCode());
            if (mapOfItemAndCustomerWithBidEntries.get(multiKeyAncestors) != null) {
                isExisting = true;
                break;
            }
        }
        return isExisting;
    }
}
