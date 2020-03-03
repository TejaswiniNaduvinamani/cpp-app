package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;

@Component
public class ItemLevelMarkupExtractor {

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private CppItemMappingRepository cppItemMappingRepository;

    @Autowired
    private MarkupDTOBuilder markupDTOBuilder;

    public List<ItemLevelMarkupDTO> extractItemTypeMarkups(List<ProductTypeMarkupDTO> allMarkupsForCmg,
            Map<String, ItemInformationDTO> allItemsByItemId, int contractPriceProfileSeq, String cmgCustomerId, Date pricingEffectiveDate,
            Date pricingExpirationDate) {

        List<ItemLevelMarkupDTO> allItemLevelMarkups = new ArrayList<>();
        List<String> assignedItemIdList = new ArrayList<>();
        List<Integer> futureItemSeqList = new ArrayList<>();
        List<ItemLevelMarkupDTO> futureItems = customerItemDescPriceRepository.fetchFutureItemTypeMarkups(contractPriceProfileSeq, cmgCustomerId,
                CPPConstants.CMG_CUSTOMER_TYPE_CODE);

        for (ItemLevelMarkupDTO itemLevelMarkupDTO : futureItems) {
            itemLevelMarkupDTO = markupDTOBuilder.buildItemLevelMarkupDTO(itemLevelMarkupDTO, StringUtils.EMPTY, true, pricingEffectiveDate,
                    pricingExpirationDate);
            futureItemSeqList.add(itemLevelMarkupDTO.getCustomerItemDescSeq());
            allItemLevelMarkups.add(itemLevelMarkupDTO);
        }

        if (CollectionUtils.isNotEmpty(futureItemSeqList)) {
            List<ItemAssignmentDTO> itemAssignmentDTOList = cppItemMappingRepository.fetchAssignedItemsForAConcept(futureItemSeqList);
            for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentDTOList) {
                assignedItemIdList.add(itemAssignmentDTO.getItemId());
            }
        }

        for (ProductTypeMarkupDTO markupForCustomer : allMarkupsForCmg) {
            if (!assignedItemIdList.contains(String.valueOf(markupForCustomer.getItemPriceId()))
                    && ItemPriceLevel.ITEM.getCode() == Integer.parseInt(markupForCustomer.getProductType())) {
                ItemInformationDTO itemInformationDTO = allItemsByItemId.get(String.valueOf(markupForCustomer.getItemPriceId()));
                if (itemInformationDTO != null) {
                    ItemLevelMarkupDTO itemLevelMarkupDTO = markupDTOBuilder.buildItemLevelMarkupDTO(itemInformationDTO, false, markupForCustomer);
                    allItemLevelMarkups.add(itemLevelMarkupDTO);
                }
            }
        }

        return allItemLevelMarkups;
    }

}