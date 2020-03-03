package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class ItemMarkupDeleteHandler {

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private ItemAssignmentHelper itemAssignmentHelper;

    @Autowired
    private CPPDateUtils cppDateUtils;

    public void deleteItemMarkupForFurtherance(int contractPriceProfileSeq, int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemId, String userName) {
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        List<String> existingItems = customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(itemIdList, gfsCustomerId, gfsCustomerTypeCode,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());

        if (CollectionUtils.isNotEmpty(existingItems)) {
            furtheranceChangeTracker.addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemIdList, userName);
            customerItemPriceRepository.expireItemPricing(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemIdList,
                    ItemPriceLevel.ITEM.getCode(), cppDateUtils.oneDayPreviousCurrentDate(), userName);
        }
    }

    public void deleteMappledItemMarkupForFurtherance(int contractPriceProfileSeq, int cppFurtheranceSeq, String gfsCustomerId,
            int gfsCustomerTypeCode, String itemId, String itemDesc, String userName) {
        FutureItemDescriptionDTO futureItemDescriptionDTO = customerItemDescPriceRepository.fetchFutureItemForFurtherance(contractPriceProfileSeq,
                gfsCustomerId, gfsCustomerTypeCode, itemDesc);

        itemAssignmentHelper.expireItemMapping(futureItemDescriptionDTO.getCustomerItemDescSeq(), itemId, userName);

        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        customerItemPriceRepository.expireItemPricing(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemIdList, ItemPriceLevel.ITEM.getCode(),
                cppDateUtils.oneDayPreviousCurrentDate(), userName);
        furtheranceChangeTracker.addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemIdList, userName);
    }

}
