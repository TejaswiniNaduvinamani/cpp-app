package com.gfs.cpp.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.item.common.constant.ItemSystemConstants;
import com.gfs.corp.item.common.dataObjects.ItemPK;
import com.gfs.corp.item.common.dto.LeanBasicItemInfoDTO;
import com.gfs.corp.item.common.exception.MissingRequiredParameterException;
import com.gfs.corp.item.common.service.ItemQueryService;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.proxy.ItemInformationDTOBuilder;

@Component
public class ItemQueryProxy {

    @Autowired
    private ItemQueryService itemQueryService;

    @Autowired
    private ItemInformationDTOBuilder itemInformationDTOBuilder;

    public Map<ItemPK, LeanBasicItemInfoDTO> buildItemInformation(List<String> itemIdList) {
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        String languageTypeCode = ItemSystemConstants.LANGUAGE_TYPE_EN;
        String descriptionTypeCode = ItemSystemConstants.ITEM_DESC_TYPE_SALES_LINE_ONE;
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        try {
            itemInfo = itemQueryService.findLeanBasicItemInfo(itemIdList, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null,
                    null);
        } catch (MissingRequiredParameterException e) {
            throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, "Service to find item information failed.");
        }
        return itemInfo;
    }

    public Map<String, ItemInformationDTO> getItemInformationByItemId(List<String> itemIdList) {
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = buildItemInformation(itemIdList);
        return itemInformationDTOBuilder.buildItemInformationMapById(itemInfo, itemIdList);
    }

    public List<ItemInformationDTO> findItemInformationList(List<String> itemIdList) {
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = buildItemInformation(itemIdList);
        return itemInformationDTOBuilder.createItemInfoResponseList(itemInfo, itemIdList);
    }

    public ItemInformationDTO findItemInformation(String itemId) {
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = buildItemInformation(itemIdList);
        return itemInformationDTOBuilder.createItemInfoResponse(itemInfo, itemId);
    }

}
