package com.gfs.cpp.component.furtherance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.markup.FutureItemUpdater;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class FurtheranceMarkupSaver {

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;
    @Autowired
    private MarkupDOBuilder markupDOBuilder;
    @Autowired
    private FutureItemUpdater futureItemUpdater;
    @Autowired
    private FurtheranceChangeTracker furtheranceChangeTracker;
    @Autowired
    private CPPDateUtils cppDateUtils;

    public void saveMarkupForFurtherance(MarkupWrapperDTO markupWrapper, String userName) {

        Map<Integer, ProductTypeMarkupDTO> existingMarkupMapByItemPriceId = new HashMap<>();
        List<ProductTypeMarkupDTO> existingMarkupList = customerItemPriceRepository.fetchAllMarkup(markupWrapper.getContractPriceProfileSeq(),
                markupWrapper.getGfsCustomerId());

        for (ProductTypeMarkupDTO markup : existingMarkupList) {
            existingMarkupMapByItemPriceId.put(markup.getItemPriceId(), markup);
        }

        MarkupWrapperDO markupWrapperDO = markupDOBuilder.buildMarkupWrapperDO(markupWrapper, markupWrapper.getContractPriceProfileSeq(), userName);

        saveProductsForFurtherance(markupWrapper, existingMarkupMapByItemPriceId, userName);
        saveItemsForFurtherance(markupWrapper, existingMarkupMapByItemPriceId, userName);
        saveSubGroupsForFurtherance(markupWrapper, existingMarkupMapByItemPriceId, userName);

        if (CollectionUtils.isNotEmpty(markupWrapperDO.getFutureItemList())) {
            futureItemUpdater.saveFutureItemsForFurtherance(markupWrapper, markupWrapperDO.getFutureItemList(), userName);
        }

    }

    private void saveProductsForFurtherance(MarkupWrapperDTO markupWrapper, Map<Integer, ProductTypeMarkupDTO> existingMarkupMapByItemPriceId,
            String userName) {

        List<ProductTypeMarkupDO> updatedProductDOList = new ArrayList<>();
        List<String> updateProductListForTracking = new ArrayList<>();

        for (ProductTypeMarkupDTO productTypeMarkup : markupWrapper.getProductMarkupList()) {
            ProductTypeMarkupDTO existingProductTypeMarkupDTO = existingMarkupMapByItemPriceId.get(productTypeMarkup.getItemPriceId());
            if (existingProductTypeMarkupDTO != null
                    && (Double.parseDouble(productTypeMarkup.getMarkup()) != Double.parseDouble(existingProductTypeMarkupDTO.getMarkup()))) {
                updateProductListForTracking.add(String.valueOf(productTypeMarkup.getItemPriceId()));
                updatedProductDOList.add(markupDOBuilder.buildProductTypeMarkupDO(markupWrapper, productTypeMarkup));
            }
        }
        if (CollectionUtils.isNotEmpty(updateProductListForTracking)) {
            updateAndTrackMarkupForFurtherance(markupWrapper, userName, updatedProductDOList, updateProductListForTracking,
                    ItemPriceLevel.PRODUCT_TYPE.getCode());
        }
    }

    private void saveItemsForFurtherance(MarkupWrapperDTO markupWrapper, Map<Integer, ProductTypeMarkupDTO> existingMarkupMapByItemPriceId,
            String userName) {

        List<ProductTypeMarkupDO> addedItemDOList = new ArrayList<>();
        List<String> addedItemIdListForTracking = new ArrayList<>();
        List<String> updatedItemIdListForTracking = new ArrayList<>();
        List<ProductTypeMarkupDO> updatedItemDOList = new ArrayList<>();

        for (ItemLevelMarkupDTO itemLevelMarkup : markupWrapper.getItemLevelMarkupList()) {
            if (StringUtils.isNotBlank(itemLevelMarkup.getItemId())) {
                ProductTypeMarkupDTO existingProductTypeMarkupDTO = existingMarkupMapByItemPriceId.get(Integer.parseInt(itemLevelMarkup.getItemId()));
                if (existingProductTypeMarkupDTO != null) {
                    if ((Double.parseDouble(itemLevelMarkup.getMarkup()) != Double.parseDouble(existingProductTypeMarkupDTO.getMarkup()))) {
                        updatedItemDOList.add(markupDOBuilder.buildItemLevelMarkupDO(markupWrapper, itemLevelMarkup));
                        updatedItemIdListForTracking.add(itemLevelMarkup.getItemId());
                    }
                } else {
                    addedItemDOList.add(markupDOBuilder.buildItemLevelMarkupDO(markupWrapper, itemLevelMarkup));
                    addedItemIdListForTracking.add(itemLevelMarkup.getItemId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(addedItemIdListForTracking)) {
            addAndTrackMarkupForFurtherance(markupWrapper, userName, addedItemDOList, addedItemIdListForTracking);
        }
        if (CollectionUtils.isNotEmpty(updatedItemIdListForTracking)) {
            updateAndTrackMarkupForFurtherance(markupWrapper, userName, updatedItemDOList, updatedItemIdListForTracking,
                    ItemPriceLevel.ITEM.getCode());
        }
    }

    private void saveSubGroupsForFurtherance(MarkupWrapperDTO markupWrapper, Map<Integer, ProductTypeMarkupDTO> existingMarkupMapByItemPriceId,
            String userName) {
        List<String> updatedSubGroupIdList = new ArrayList<>();
        List<ProductTypeMarkupDO> updatedSubGroupDOList = new ArrayList<>();
        for (SubgroupMarkupDTO subgroupMarkupDTO : markupWrapper.getSubgroupMarkupList()) {
            ProductTypeMarkupDTO existingProductTypeMarkupDTO = existingMarkupMapByItemPriceId
                    .get(Integer.parseInt(subgroupMarkupDTO.getSubgroupId()));
            if (existingProductTypeMarkupDTO != null
                    && (Double.parseDouble(subgroupMarkupDTO.getMarkup()) != Double.parseDouble(existingProductTypeMarkupDTO.getMarkup()))) {
                updatedSubGroupIdList.add(subgroupMarkupDTO.getSubgroupId());
                updatedSubGroupDOList.add(markupDOBuilder.buildSubgroupMarkupDO(markupWrapper.getGfsCustomerId(), markupWrapper.getGfsCustomerType(),
                        subgroupMarkupDTO));
            }
        }

        if (CollectionUtils.isNotEmpty(updatedSubGroupIdList)) {
            updateAndTrackMarkupForFurtherance(markupWrapper, userName, updatedSubGroupDOList, updatedSubGroupIdList,
                    ItemPriceLevel.SUBGROUP.getCode());
        }
    }

    private void addAndTrackMarkupForFurtherance(MarkupWrapperDTO markupWrapper, String userName, List<ProductTypeMarkupDO> addeditemDOList,
            List<String> addedItemIdList) {
        customerItemPriceRepository.saveMarkup(addeditemDOList, userName, markupWrapper.getContractPriceProfileSeq());
        furtheranceChangeTracker.addTrackingForMarkupAdd(markupWrapper.getCppFurtheranceSeq(), markupWrapper.getGfsCustomerId(),
                markupWrapper.getGfsCustomerType(), addedItemIdList, userName);
    }

    private void updateAndTrackMarkupForFurtherance(MarkupWrapperDTO markupWrapper, String userName, List<ProductTypeMarkupDO> updatedDOList,
            List<String> updateIdList, int itemCode) {
        customerItemPriceRepository.expireItemPricing(markupWrapper.getContractPriceProfileSeq(), markupWrapper.getGfsCustomerId(),
                markupWrapper.getGfsCustomerType(), updateIdList, itemCode, cppDateUtils.oneDayPreviousCurrentDate(), userName);
        customerItemPriceRepository.saveMarkup(updatedDOList, userName, markupWrapper.getContractPriceProfileSeq());
        furtheranceChangeTracker.addTrackingForMarkupUpdate(markupWrapper.getCppFurtheranceSeq(), markupWrapper.getGfsCustomerId(),
                markupWrapper.getGfsCustomerType(), updateIdList, userName, itemCode);
    }

}
