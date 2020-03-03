package com.gfs.cpp.component.assignment.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.proxy.ItemQueryProxy;

@Component
public class ItemAssignmentBuilder {

    @Autowired
    private ItemQueryProxy itemQueryProxy;

    @Autowired
    private CppItemMappingRepository cppItemMappingRepository;

    public ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(FutureItemDescriptionDTO futureItemDescriptionDTO, int contractPriceProfileSeq) {

        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(futureItemDescriptionDTO.getCustomerItemDescSeq());

        List<ItemAssignmentDTO> itemAssignmentDTOList = cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList);

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setFutureItemDesc(futureItemDescriptionDTO.getFutureItemDesc());
        itemAssignmentWrapperDTO.setGfsCustomerId(futureItemDescriptionDTO.getGfsCustomerId());
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(futureItemDescriptionDTO.getGfsCustomerTypeCode());
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);

        if (CollectionUtils.isNotEmpty(itemAssignmentDTOList)) {
            List<String> itemIdList = new ArrayList<>();
            Map<String, ItemAssignmentDTO> itemAssignmentMap = new HashMap<>();
            for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentDTOList) {
                itemIdList.add(itemAssignmentDTO.getItemId());
                itemAssignmentMap.put(itemAssignmentDTO.getItemId(), itemAssignmentDTO);
            }
            List<ItemInformationDTO> itemInformationDTOList = itemQueryProxy.findItemInformationList(itemIdList);

            for (ItemInformationDTO itemInformationDTO : itemInformationDTOList) {
                if (itemAssignmentMap.containsKey(itemInformationDTO.getItemNo())) {
                    ItemAssignmentDTO itemAssignmentDTO = itemAssignmentMap.get(itemInformationDTO.getItemNo());
                    itemAssignmentDTO.setIsItemSaved(true);
                    itemAssignmentDTO.setItemDescription(itemInformationDTO.getItemDescription());
                }
            }
            itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
            itemAssignmentWrapperDTO.setIsFutureItemSaved(true);
        } else {
            itemAssignmentWrapperDTO.setItemAssignmentList(buildItemAssignmentList());
            itemAssignmentWrapperDTO.setIsFutureItemSaved(false);
        }
        return itemAssignmentWrapperDTO;
    }

    List<ItemAssignmentDO> buildItemAssignmentDOList(List<ItemAssignmentDTO> itemAssignmentDTOList, int customerItemDescSeq) {
        List<ItemAssignmentDO> itemAssignmentDOList = new ArrayList<>();
        for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentDTOList) {
            ItemAssignmentDO itemAssignmentDO = buildItemAssignmentDO(customerItemDescSeq, itemAssignmentDTO.getItemId());
            itemAssignmentDOList.add(itemAssignmentDO);
        }
        return itemAssignmentDOList;
    }

    ItemAssignmentDO buildItemAssignmentDO(int customerItemDescSeq, String itemId) {
        ItemAssignmentDO itemAssignmentDO = new ItemAssignmentDO();
        itemAssignmentDO.setCustomerItemDescSeq(customerItemDescSeq);
        itemAssignmentDO.setItemPriceId(itemId);
        itemAssignmentDO.setItemPriceLevelCode(ItemPriceLevel.ITEM.getCode());
        return itemAssignmentDO;
    }

    private List<ItemAssignmentDTO> buildItemAssignmentList() {
        List<ItemAssignmentDTO> itemAssignmentList = new ArrayList<>();
        ItemAssignmentDTO itemAssigmentDTO = new ItemAssignmentDTO();
        itemAssigmentDTO.setItemDescription(StringUtils.EMPTY);
        itemAssigmentDTO.setItemId(StringUtils.EMPTY);
        itemAssigmentDTO.setIsItemSaved(false);
        itemAssignmentList.add(itemAssigmentDTO);
        return itemAssignmentList;
    }

}
