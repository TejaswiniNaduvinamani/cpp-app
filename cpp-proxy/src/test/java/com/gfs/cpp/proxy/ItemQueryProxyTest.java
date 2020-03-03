package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.item.common.constant.ItemSystemConstants;
import com.gfs.corp.item.common.dataObjects.ItemPK;
import com.gfs.corp.item.common.dto.LeanBasicItemInfoDTO;
import com.gfs.corp.item.common.exception.MissingRequiredParameterException;
import com.gfs.corp.item.common.service.ItemQueryService;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.proxy.ItemInformationDTOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ItemQueryProxyTest {

    @InjectMocks
    private ItemQueryProxy target;

    @Mock
    private ItemQueryService itemQueryService;

    @Mock
    private ItemInformationDTOBuilder itemInformationDTOBuilder;

    @Test
    public void shouldBuildItemInformation() throws MissingRequiredParameterException {
        List<String> offeringIds = new ArrayList<>();
        offeringIds.add("681204");
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        String languageTypeCode = ItemSystemConstants.LANGUAGE_TYPE_EN;
        String descriptionTypeCode = ItemSystemConstants.ITEM_DESC_TYPE_SALES_LINE_ONE;
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("AC");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("3");
        itemInfo.put(itemPK, basicItem);

        when(itemQueryService.findLeanBasicItemInfo(offeringIds, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null))
                .thenReturn(itemInfo);

        Map<ItemPK, LeanBasicItemInfoDTO> itemInformation = target.buildItemInformation(offeringIds);
        LeanBasicItemInfoDTO result = itemInformation.get(itemPK);

        assertThat(result.getItemDescription(), equalTo("Test"));
        assertThat(result.getItemStatusCode(), equalTo("AC"));
        assertThat(result.getStockingCode(), equalTo("3"));

        verify(itemQueryService).findLeanBasicItemInfo(offeringIds, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null);
    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowExceptionWhenBuildItemInformation() throws MissingRequiredParameterException {
        List<String> offeringIds = new ArrayList<>();
        offeringIds.add("681204");
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        String languageTypeCode = ItemSystemConstants.LANGUAGE_TYPE_EN;
        String descriptionTypeCode = ItemSystemConstants.ITEM_DESC_TYPE_SALES_LINE_ONE;

        Mockito.doThrow(new MissingRequiredParameterException()).when(itemQueryService).findLeanBasicItemInfo(offeringIds, offeringGroupId,
                languageTypeCode, descriptionTypeCode, null, null, null, null);

        Map<ItemPK, LeanBasicItemInfoDTO> itemInformation = target.buildItemInformation(offeringIds);

        assertThat(itemInformation.size(), equalTo(0));

    }

    @Test
    public void shouldFindItemInformationList() throws MissingRequiredParameterException {
        List<String> offeringIds = new ArrayList<>();
        offeringIds.add("681204");
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        String languageTypeCode = ItemSystemConstants.LANGUAGE_TYPE_EN;
        String descriptionTypeCode = ItemSystemConstants.ITEM_DESC_TYPE_SALES_LINE_ONE;
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("AC");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("3");
        itemInfo.put(itemPK, basicItem);
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setIsActive(true);
        itemInformationDTO.setIsValid(true);
        itemInformationDTO.setItemDescription("Test");
        itemInformationDTO.setItemNo("681204");
        itemInformationDTO.setStockingCode("3");
        itemInformationDTO.setItemStatusCode("AC");
        List<ItemInformationDTO> itemInformationDTOList = new ArrayList<>();
        itemInformationDTOList.add(itemInformationDTO);

        when(itemQueryService.findLeanBasicItemInfo(offeringIds, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null))
                .thenReturn(itemInfo);
        when(itemInformationDTOBuilder.createItemInfoResponseList(itemInfo, offeringIds)).thenReturn(itemInformationDTOList);
        List<ItemInformationDTO> itemInformationList = target.findItemInformationList(offeringIds);

        assertThat(itemInformationList.get(0).getIsActive(), equalTo(true));
        assertThat(itemInformationList.get(0).getIsValid(), equalTo(true));
        assertThat(itemInformationList.get(0).getStockingCode(), equalTo("3"));
        assertThat(itemInformationList.get(0).getItemNo(), equalTo("681204"));
        assertThat(itemInformationList.get(0).getItemStatusCode(), equalTo("AC"));

        verify(itemInformationDTOBuilder).createItemInfoResponseList(itemInfo, offeringIds);
        verify(itemQueryService).findLeanBasicItemInfo(offeringIds, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null);
    }

    @Test
    public void shouldGetItemInformationByItemId() throws MissingRequiredParameterException {
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add("681204");

        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        String languageTypeCode = ItemSystemConstants.LANGUAGE_TYPE_EN;
        String descriptionTypeCode = ItemSystemConstants.ITEM_DESC_TYPE_SALES_LINE_ONE;

        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("AC");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("3");
        itemInfo.put(itemPK, basicItem);

        Map<String, ItemInformationDTO> itemInformationById = new HashMap<>();
        when(itemQueryService.findLeanBasicItemInfo(itemIdList, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null))
                .thenReturn(itemInfo);
        when(itemInformationDTOBuilder.buildItemInformationMapById(itemInfo, itemIdList)).thenReturn(itemInformationById);

        Map<String, ItemInformationDTO> result = target.getItemInformationByItemId(itemIdList);

        assertThat(result, equalTo(itemInformationById));

        verify(itemInformationDTOBuilder).buildItemInformationMapById(itemInfo, itemIdList);
        verify(itemQueryService).findLeanBasicItemInfo(itemIdList, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null);

    }

    @Test
    public void shouldFindItemInformation() throws MissingRequiredParameterException {
        List<String> itemIdList = new ArrayList<>();
        String itemId = "681204";
        itemIdList.add(itemId);

        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        String languageTypeCode = ItemSystemConstants.LANGUAGE_TYPE_EN;
        String descriptionTypeCode = ItemSystemConstants.ITEM_DESC_TYPE_SALES_LINE_ONE;

        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("AC");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("3");
        itemInfo.put(itemPK, basicItem);

        ItemInformationDTO itemInformation = new ItemInformationDTO();
        when(itemQueryService.findLeanBasicItemInfo(itemIdList, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null))
                .thenReturn(itemInfo);
        when(itemInformationDTOBuilder.createItemInfoResponse(itemInfo, itemId)).thenReturn(itemInformation);

        ItemInformationDTO result = target.findItemInformation(itemId);

        assertThat(result, equalTo(itemInformation));

        verify(itemInformationDTOBuilder).createItemInfoResponse(itemInfo, itemId);
        verify(itemQueryService).findLeanBasicItemInfo(itemIdList, offeringGroupId, languageTypeCode, descriptionTypeCode, null, null, null, null);

    }
}
