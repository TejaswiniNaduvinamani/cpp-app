package com.gfs.cpp.acceptanceTests.mocks;

import static com.gfs.corp.item.common.constant.ItemSystemConstants.ITEM_GROUP_ID_US;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.item.common.constant.ItemSystemConstants;
import com.gfs.corp.item.common.dataObjects.ItemPK;
import com.gfs.corp.item.common.dto.LeanBasicItemInfoDTO;
import com.gfs.corp.item.common.exception.MissingRequiredParameterException;
import com.gfs.corp.item.common.service.ItemQueryService;

@Component
public class ItemQueryServiceMock implements Resettable {

    @Autowired
    private ItemQueryService itemQueryService;

    public ItemQueryServiceMock() {
    }

    Map<ItemPK, LeanBasicItemInfoDTO> itemInfo = new HashMap<>();

    @Override
    public void reset() {

        try {
            List<String> offeringIds = new ArrayList<>();
            offeringIds.add("681204");
            when(itemQueryService.findLeanBasicItemInfo(any(List.class), eq(ITEM_GROUP_ID_US), eq("en"),
                    eq(ItemSystemConstants.ITEM_DESC_TYPE_SALES_LINE_ONE), (String) any(), (String) any(), (Integer) any(), (Integer) any()))
                            .then(new Answer<Map<ItemPK, LeanBasicItemInfoDTO>>() {
                                @Override
                                public Map<ItemPK, LeanBasicItemInfoDTO> answer(InvocationOnMock invocation) throws Throwable {
                                    @SuppressWarnings("unchecked")
                                    List<String> offeringIds = invocation.getArgumentAt(0, List.class);
                                    for (String offeringId : offeringIds) {
                                        ItemPK itemPK = new ItemPK(offeringId, ItemSystemConstants.ITEM_GROUP_ID_US);
                                        LeanBasicItemInfoDTO basicItem = new LeanBasicItemInfoDTO();
                                        basicItem.setItemStatusCode("AC");
                                        basicItem.setItemDescription("Test");
                                        basicItem.setStockingCode("3");
                                        itemInfo.put(itemPK, basicItem);
                                    }
                                    return itemInfo;

                                }
                            });
        } catch (MissingRequiredParameterException e) {
            throw new RuntimeException(e);
        }
    }

}