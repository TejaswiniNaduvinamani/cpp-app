package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class FutureItemDeleteHandler {

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CppItemMappingRepository cppItemMappingRepository;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private FurtheranceChangeTracker furtheranceChangeTracker;

    public void deleteFutureItemWithAssignedItems(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerType, String itemDesc) {
        ItemLevelMarkupDTO itemLevelMarkupDTO = customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerType, itemDesc);

        if (itemLevelMarkupDTO != null) {

            List<Integer> customerItemDescSeqList = new ArrayList<>();
            customerItemDescSeqList.add(itemLevelMarkupDTO.getCustomerItemDescSeq());
            List<ItemAssignmentDTO> itemAssignmentDTOList = cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList);
            customerItemDescPriceRepository.deleteFutureItem(contractPriceProfileSeq, gfsCustomerId, itemDesc);

            if (CollectionUtils.isNotEmpty(itemAssignmentDTOList)) {
                List<String> itemIdList = new ArrayList<>();
                for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentDTOList) {
                    itemIdList.add(itemAssignmentDTO.getItemId());
                }
                customerItemPriceRepository.deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemIdList,
                        ItemPriceLevel.ITEM.getCode());
            }
        }
    }

    public void deleteFutureItemWithAssignedItemsForFurtherance(int contractPriceProfileSeq, int cppFurtheranceSeq, String cmgCustomerId,
            int cmgCustomerTypeCode, String itemDesc, String userName) {
        ItemLevelMarkupDTO itemLevelMarkupDTO = customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerTypeCode, itemDesc);

        if (itemLevelMarkupDTO != null) {
            List<String> expireItemIdList = new ArrayList<>();
            List<Integer> customerItemDescSeqList = new ArrayList<>();
            customerItemDescSeqList.add(itemLevelMarkupDTO.getCustomerItemDescSeq());
            List<ItemAssignmentDTO> itemAssignmentDTOList = cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList);
            customerItemDescPriceRepository.deleteFutureItem(contractPriceProfileSeq, cmgCustomerId, itemDesc);

            if (CollectionUtils.isNotEmpty(itemAssignmentDTOList)) {
                for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentDTOList) {
                    expireItemIdList.add(itemAssignmentDTO.getItemId());
                }
            }

            if (CollectionUtils.isNotEmpty(expireItemIdList)) {
                furtheranceChangeTracker.addTrackingForMarkupDelete(cppFurtheranceSeq, cmgCustomerId, cmgCustomerTypeCode, expireItemIdList,
                        userName);
                customerItemPriceRepository.expireItemPricing(contractPriceProfileSeq, cmgCustomerId, cmgCustomerTypeCode, expireItemIdList,
                        ItemPriceLevel.ITEM.getCode(), cppDateUtils.oneDayPreviousCurrentDate(), userName);
            }
        }
    }
}
