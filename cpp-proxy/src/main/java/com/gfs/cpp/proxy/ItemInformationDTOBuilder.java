package com.gfs.cpp.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.gfs.corp.item.common.constant.ItemSystemConstants;
import com.gfs.corp.item.common.dataObjects.ItemPK;
import com.gfs.corp.item.common.dto.LeanBasicItemInfoDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;

@Component
public class ItemInformationDTOBuilder {

    public ItemInformationDTO createItemInfoResponse(Map<ItemPK, LeanBasicItemInfoDTO> itemInfo, String itemId) {
        return buildItemInformation(itemInfo, itemId);
    }

    public List<ItemInformationDTO> createItemInfoResponseList(Map<ItemPK, LeanBasicItemInfoDTO> itemInfo, List<String> itemIdList) {
        List<ItemInformationDTO> itemInformationDTOList = new ArrayList<>();
        for (String itemId : itemIdList) {
            ItemInformationDTO itemInformationDTO = buildItemInformation(itemInfo, itemId);
            itemInformationDTOList.add(itemInformationDTO);
        }
        return itemInformationDTOList;
    }

    public Map<String, ItemInformationDTO> buildItemInformationMapById(Map<ItemPK, LeanBasicItemInfoDTO> itemInfo, List<String> itemIdList) {
        Map<String, ItemInformationDTO> itemInformationByItemId = new HashMap<>();

        for (String itemId : itemIdList) {
            ItemInformationDTO itemInformationDTO = buildItemInformation(itemInfo, itemId);
            itemInformationByItemId.put(itemId, itemInformationDTO);
        }

        return itemInformationByItemId;
    }

    private ItemInformationDTO buildItemInformation(Map<ItemPK, LeanBasicItemInfoDTO> itemInfo, String itemId) {
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        if (MapUtils.isNotEmpty(itemInfo)) {
            final ItemPK itemPK = new ItemPK(itemId, ItemSystemConstants.ITEM_GROUP_ID_US);
            final LeanBasicItemInfoDTO basicItemInfoDTO = itemInfo.get(itemPK);
            if (null != basicItemInfoDTO) {
                if (StringUtils.isBlank(basicItemInfoDTO.getStockingCode())
                        || ItemSystemConstants.ITEM_STOCKING_CODE_JUST_IN_TIME.equals(basicItemInfoDTO.getStockingCode())) {
                    itemInformationDTO.setIsValid(false);
                } else {
                    itemInformationDTO.setItemNo(itemId);
                    itemInformationDTO.setItemDescription(basicItemInfoDTO.getItemDescription());
                    itemInformationDTO.setIsValid(
                            ItemSystemConstants.ITEM_STATUS_CODES_CONSIDERED_ACTIVE_FOR_CUSTOMER_ORDERS.contains(basicItemInfoDTO.getItemStatusCode())
                                    ? true
                                    : false);
                    itemInformationDTO.setItemStatusCode(basicItemInfoDTO.getItemStatusCode());
                    itemInformationDTO.setIsActive(true);
                    itemInformationDTO.setStockingCode(basicItemInfoDTO.getStockingCode());
                }
                return itemInformationDTO;
            }
        }
        itemInformationDTO.setIsValid(false);
        return itemInformationDTO;
    }
}
