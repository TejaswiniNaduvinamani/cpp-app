package com.gfs.cpp.acceptanceTests.common.data;

import static com.gfs.cpp.acceptanceTests.config.CukesConstants.DEFAULT_UNIT;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.FUTURE_ITEM_DESC;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.ITEM_DESC;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.ITEM_DESC_TWO;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.ITEM_ID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.ITEM_ID_TWO;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.PERCENT_UNIT;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.PER_CASE_TYPE;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.PRODUCT_PRICE_ID_ONE;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.PRODUCT_PRICE_ID_TWO;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SELL_UNIT_TYPE;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_DESCRIPTION;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_DESCRIPTION_TWO;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_ID_TWO;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_ID_VALID;

import org.apache.commons.lang3.StringUtils;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;

public enum AcceptableMarkupType {

    // @formatter:off
    PRODUCT_TYPE_CASE(PRODUCT_PRICE_ID_ONE, PER_CASE_TYPE, DEFAULT_UNIT, ItemPriceLevel.PRODUCT_TYPE.getCode(), null), 
    PRODUCT_TYPE_SELL(PRODUCT_PRICE_ID_TWO, SELL_UNIT_TYPE, PERCENT_UNIT, ItemPriceLevel.PRODUCT_TYPE.getCode(), null), 
    ITEM_CASE(ITEM_ID, PER_CASE_TYPE, DEFAULT_UNIT, ItemPriceLevel.ITEM.getCode(), ITEM_DESC),
    ITEM_SELL(ITEM_ID_TWO, SELL_UNIT_TYPE, PERCENT_UNIT, ItemPriceLevel.ITEM.getCode(), ITEM_DESC_TWO),
    SUBGROUP_CASE(SUBGROUP_ID_VALID, PER_CASE_TYPE, DEFAULT_UNIT, ItemPriceLevel.SUBGROUP.getCode(), SUBGROUP_DESCRIPTION),
    SUBGROUP_SELL(SUBGROUP_ID_TWO, SELL_UNIT_TYPE, PERCENT_UNIT, ItemPriceLevel.SUBGROUP.getCode(), SUBGROUP_DESCRIPTION_TWO),
    FUTURE_ITEM(StringUtils.EMPTY, PER_CASE_TYPE, DEFAULT_UNIT, ItemPriceLevel.ITEM.getCode(), FUTURE_ITEM_DESC);
    
 // @formatter:on

    private String itemPriceId;
    private int markupType;
    private String unit;
    private int itemPriceCode;
    private String itemDesc;

    private AcceptableMarkupType(String itemPriceId, int markupType, String unit, int itemPriceCode, String itemDesc) {
        this.itemPriceId = itemPriceId;
        this.markupType = markupType;
        this.unit = unit;
        this.itemPriceCode = itemPriceCode;
        this.itemDesc = itemDesc;
    }

    public String getItemPriceId() {
        return itemPriceId;
    }

    public int getMarkupType() {
        return markupType;
    }

    public String getUnit() {
        return unit;
    }

    public int getItemPriceCode() {
        return itemPriceCode;
    }

    public String getItemDesc() {
        return itemDesc;
    }

}
