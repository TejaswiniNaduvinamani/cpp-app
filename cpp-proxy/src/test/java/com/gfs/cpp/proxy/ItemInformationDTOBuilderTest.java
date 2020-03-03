package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.item.common.constant.ItemSystemConstants;
import com.gfs.corp.item.common.dataObjects.ItemPK;
import com.gfs.corp.item.common.dto.LeanBasicItemInfoDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.proxy.ItemInformationDTOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ItemInformationDTOBuilderTest {

    @InjectMocks
    private ItemInformationDTOBuilder target;

    @Test
    public void createItemInfoResponseTest() {
        List<String> offeringIds = new ArrayList<>();
        offeringIds.add("681204");
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("AC");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("3");
        itemInfo.put(itemPK, basicItem);

        ItemInformationDTO itemInformation = target.createItemInfoResponse(itemInfo, "681204");

        assertThat(itemInformation.getIsActive(), equalTo(true));
        assertThat(itemInformation.getIsValid(), equalTo(true));
        assertThat(itemInformation.getStockingCode(), equalTo("3"));
        assertThat(itemInformation.getItemNo(), equalTo("681204"));
        assertThat(itemInformation.getItemStatusCode(), equalTo("AC"));
    }

    @Test
    public void createItemInfoResponseTest_Empty() {

        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();

        ItemInformationDTO itemInformation = target.createItemInfoResponse(itemInfo, "681204");

        assertThat(itemInformation.getIsValid(), equalTo(false));
    }

    @Test
    public void createItemInfoResponseTest_Invalid() {
        List<String> offeringIds = new ArrayList<>();
        offeringIds.add("681204");
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("IN");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("3");
        itemInfo.put(itemPK, basicItem);

        ItemInformationDTO itemInformation = target.createItemInfoResponse(itemInfo, "681204");

        assertThat(itemInformation.getIsActive(), equalTo(true));
        assertThat(itemInformation.getIsValid(), equalTo(false));
        assertThat(itemInformation.getStockingCode(), equalTo("3"));
        assertThat(itemInformation.getItemNo(), equalTo("681204"));
        assertThat(itemInformation.getItemStatusCode(), equalTo("IN"));
    }

    @Test
    public void createItemInfoResponseTest_JustInTimeStock() {
        List<String> offeringIds = new ArrayList<>();
        offeringIds.add("681204");
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("AC");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("");
        itemInfo.put(itemPK, basicItem);

        ItemInformationDTO itemInformation = target.createItemInfoResponse(itemInfo, "681204");

        assertThat(itemInformation.getIsValid(), equalTo(false));
    }

    @Test
    public void createItemInfoResponseListTest() {
        List<String> offeringIds = new ArrayList<>();
        offeringIds.add("681204");
        String offeringGroupId = ItemSystemConstants.ITEM_GROUP_ID_US;
        Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();
        ItemPK itemPK = new ItemPK("681204", offeringGroupId);
        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
        basicItem.setItemStatusCode("AC");
        basicItem.setItemDescription("Test");
        basicItem.setStockingCode("3");
        itemInfo.put(itemPK, basicItem);

        List<ItemInformationDTO> itemInformationList = target.createItemInfoResponseList(itemInfo, offeringIds);

        assertThat(itemInformationList.get(0).getIsActive(), equalTo(true));
        assertThat(itemInformationList.get(0).getIsValid(), equalTo(true));
        assertThat(itemInformationList.get(0).getStockingCode(), equalTo("3"));
        assertThat(itemInformationList.get(0).getItemNo(), equalTo("681204"));
        assertThat(itemInformationList.get(0).getItemStatusCode(), equalTo("AC"));
    }

    @Test
    public void shouldBuildItemInformationMapById() {

        Map<ItemPK, LeanBasicItemInfoDTO> itemInfoMap = new HashMap<>();
        String itemId = "1";
        List<String> itemIdList = new ArrayList<>();
        ItemPK itemPK = new ItemPK();
        itemPK.setItemId(itemId);
        itemPK.setItemGroupId("00001");

        LeanBasicItemInfoDTO leanBasicItemInfoDTO = new LeanBasicItemInfoDTO();
        leanBasicItemInfoDTO.setItemDescription("item description");

        itemInfoMap.put(itemPK, leanBasicItemInfoDTO);
        itemIdList.add(itemId);
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();

        Map<String, ItemInformationDTO> result = target.buildItemInformationMapById(itemInfoMap, itemIdList);

        assertThat(result.get(itemId), equalTo(itemInformationDTO));

    }
}
