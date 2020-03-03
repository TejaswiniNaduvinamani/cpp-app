package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class FutureItemUpdater {

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CppItemMappingRepository cppItemMappingRepository;

    @Autowired
    private MarkupDOBuilder markupDOBuilder;

    @Autowired
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Autowired
    private CPPDateUtils cppDateUtils;

    private static final Logger logger = LoggerFactory.getLogger(FutureItemUpdater.class);

    public void saveFutureItems(String gfsCustomerId, int gfsCustomerTypeCode, int contractPriceProfileSeq, List<FutureItemDO> futureItemList,
            String userName) {

        List<ItemLevelMarkupDTO> savedfutureItemList = customerItemDescPriceRepository.fetchFutureItemTypeMarkups(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode);
        Map<String, ItemLevelMarkupDTO> savedFutureItemMap = new HashMap<>();

        for (ItemLevelMarkupDTO futureItem : savedfutureItemList) {
            savedFutureItemMap.put(futureItem.getItemDesc(), futureItem);
        }

        List<FutureItemDO> newFutureItemList = new ArrayList<>();
        Map<Integer, FutureItemDO> updateFutureItemMap = new HashMap<>();

        for (FutureItemDO futureItemDO : futureItemList) {
            if (savedFutureItemMap.containsKey(futureItemDO.getItemDesc())) {
                int customerItemDescSeq = savedFutureItemMap.get(futureItemDO.getItemDesc()).getCustomerItemDescSeq();
                updateFutureItemMap.put(customerItemDescSeq, futureItemDO);
            } else {
                newFutureItemList.add(futureItemDO);
            }
        }

        if (MapUtils.isNotEmpty(updateFutureItemMap)) {
            List<FutureItemDO> updateFutureItemList = new ArrayList<>(updateFutureItemMap.values());
            customerItemDescPriceRepository.updateFutureItems(updateFutureItemList, userName, contractPriceProfileSeq);
            updateAssignedItemswithFutureItems(gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq, userName, updateFutureItemMap);
        }

        if (CollectionUtils.isNotEmpty(newFutureItemList))
            customerItemDescPriceRepository.saveFutureItems(newFutureItemList, userName, contractPriceProfileSeq);
    }

    public void saveFutureItemsForFurtherance(MarkupWrapperDTO markupWrapper, List<FutureItemDO> futureItemList, String userName) {

        Map<String, ItemLevelMarkupDTO> savedFutureItemMap = new HashMap<>();
        Map<Integer, FutureItemDO> updatedFutureItemMap = new HashMap<>();

        List<ItemLevelMarkupDTO> savedfutureItemList = customerItemDescPriceRepository.fetchFutureItemTypeMarkups(markupWrapper.getContractPriceProfileSeq(),
                markupWrapper.getGfsCustomerId(), markupWrapper.getGfsCustomerType());

        for (ItemLevelMarkupDTO futureItem : savedfutureItemList) {
            savedFutureItemMap.put(futureItem.getItemDesc(), futureItem);
        }

        for (FutureItemDO futureItemDO : futureItemList) {
            ItemLevelMarkupDTO existingFutureItem = savedFutureItemMap.get(futureItemDO.getItemDesc());
            if (existingFutureItem != null) {
                if (Double.parseDouble(existingFutureItem.getMarkup()) != Double.parseDouble(futureItemDO.getMarkup().toString())) {
                    updatedFutureItemMap.put(existingFutureItem.getCustomerItemDescSeq(), futureItemDO);
                }
            } else {
                logger.error("New future item {} found in Furtherance service failed for cppFurtheranceSeq {} ", futureItemDO.getItemDesc(),
                        markupWrapper.getCppFurtheranceSeq());
                throw new CPPRuntimeException(CPPExceptionType.FUTURE_ITEM_ADDED_IN_FURTHERANCE, "New future item found in furtherance");
            }
        }

        if (MapUtils.isNotEmpty(updatedFutureItemMap)) {

            List<FutureItemDO> updateFutureItemList = new ArrayList<>(updatedFutureItemMap.values());
            customerItemDescPriceRepository.updateFutureItems(updateFutureItemList, userName, markupWrapper.getContractPriceProfileSeq());
            updateAssignedItemswithFutureItemsForFurtherance(markupWrapper.getCppFurtheranceSeq(), markupWrapper.getGfsCustomerId(),
                    markupWrapper.getGfsCustomerType(), markupWrapper.getContractPriceProfileSeq(), userName, updatedFutureItemMap);
        }
    }

    private void updateAssignedItemswithFutureItems(String gfsCustomerId, int gfsCustomerTypeCode, int contractPriceProfileSeq, String userName,
            Map<Integer, FutureItemDO> updateFutureItemMap) {

        List<Integer> customerItemDescSeqList = new ArrayList<>(updateFutureItemMap.keySet());
        List<ItemAssignmentDTO> itemAssignmentDTOList = cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList);
        List<ProductTypeMarkupDO> markupDOList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(itemAssignmentDTOList)) {
            for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentDTOList) {
                if (updateFutureItemMap.containsKey(itemAssignmentDTO.getCustomerItemDescSeq())) {
                    FutureItemDO futureItemDO = updateFutureItemMap.get(itemAssignmentDTO.getCustomerItemDescSeq());
                    ItemLevelMarkupDTO futureItemDTO = customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId,
                            gfsCustomerTypeCode, futureItemDO.getItemDesc());
                    ProductTypeMarkupDO markupDO = markupDOBuilder.buildMarkupDO(gfsCustomerId, gfsCustomerTypeCode, futureItemDTO,
                            itemAssignmentDTO.getItemId());
                    markupDOList.add(markupDO);
                }
            }
            customerItemPriceRepository.updateMarkup(markupDOList, userName, contractPriceProfileSeq);
        }

    }

    private void updateAssignedItemswithFutureItemsForFurtherance(int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            int contractPriceProfileSeq, String userName, Map<Integer, FutureItemDO> updatedFutureItemMap) {

        List<String> updatedAssignedItemIdList = new ArrayList<>();
        List<Integer> updatedItemDescSeqList = new ArrayList<>(updatedFutureItemMap.keySet());
        List<ItemAssignmentDTO> allItemsAssignedInAConcept = cppItemMappingRepository.fetchAssignedItemsForAConcept(updatedItemDescSeqList);
        List<ProductTypeMarkupDO> updatedFutureItemDOList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(allItemsAssignedInAConcept)) {
            for (ItemAssignmentDTO itemAssignmentDTO : allItemsAssignedInAConcept) {
                updatedAssignedItemIdList.add(itemAssignmentDTO.getItemId());
                FutureItemDO updatedFutureItemDO = updatedFutureItemMap.get(itemAssignmentDTO.getCustomerItemDescSeq());
                updatedFutureItemDOList
                        .add(markupDOBuilder.buildProductTypeMarkupDO(gfsCustomerId, gfsCustomerTypeCode, itemAssignmentDTO, updatedFutureItemDO));
            }
        }

        if (CollectionUtils.isNotEmpty(updatedAssignedItemIdList)) {
            customerItemPriceRepository.expireItemPricing(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, updatedAssignedItemIdList,
                    ItemPriceLevel.ITEM.getCode(), cppDateUtils.oneDayPreviousCurrentDate(), userName);
            customerItemPriceRepository.saveMarkup(updatedFutureItemDOList, userName, contractPriceProfileSeq);
            furtheranceChangeTracker.addTrackingForMarkupUpdate(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, updatedAssignedItemIdList,
                    userName, ItemPriceLevel.ITEM.getCode());
        }
    }
}
