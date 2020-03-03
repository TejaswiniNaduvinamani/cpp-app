package com.gfs.cpp.acceptanceTests.common.data;

import static com.gfs.cpp.acceptanceTests.config.CukesConstants.ITEM_LEVEL_CODE;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.ITEM_PRICE_ID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.NEW_ITEM_ID_FOR_FURTHERANCE;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.NEW_ITEM_ID_TO_ASSIGN;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.PRODUCT_LEVEL_CODE;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.PRODUCT_PRICE_ID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_ID_VALID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_LEVEL_CODE;

public enum AcceptableItems {

    // @formatter:off
    PRODUCT_TYPE(String.valueOf(PRODUCT_PRICE_ID), PRODUCT_LEVEL_CODE), 
    ITEM_TYPE(ITEM_PRICE_ID, ITEM_LEVEL_CODE), 
    ITEM_TYPE_FOR_ASSIGNMENT(NEW_ITEM_ID_TO_ASSIGN, ITEM_LEVEL_CODE),
    ITEM_TYPE_FOR_FUTHERANCE(NEW_ITEM_ID_FOR_FURTHERANCE, ITEM_LEVEL_CODE),
    ITEM_TYPE_FOR_SUBGROUP(SUBGROUP_ID_VALID, SUBGROUP_LEVEL_CODE);
    
    // @formatter:on

    String itemId;
    int itemPriceLevelCode;

    private AcceptableItems(String itemId, int itemPriceLevelCode) {
        this.itemId = itemId;
        this.itemPriceLevelCode = itemPriceLevelCode;
    }

    public String getItemId() {
        return itemId;
    }

    public int getItemPriceLevelCode() {
        return itemPriceLevelCode;
    }
}
