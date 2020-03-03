package com.gfs.cpp.data.markup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;

public class ItemTypeMarkupRowMapper implements RowMapper<ItemLevelMarkupDTO> {

    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    private static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";

    // @formatter:off
    public static final String FETCH_ITEM_CUSTOMER_ITEM_PRICE = "select CUSTOMER_ITEM_PRICE.ITEM_PRICE_ID,"
            + "CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID, "
            + "CUSTOMER_ITEM_PRICE.EFFECTIVE_DATE, "
            + "CUSTOMER_ITEM_PRICE.EXPIRATION_DATE, "
            + "CUSTOMER_ITEM_PRICE.COST_MARKUP_AMT, "
            + "CUSTOMER_ITEM_PRICE.MARKUP_AMOUNT_TYPE_CODE, "
            + "CUSTOMER_ITEM_PRICE.MARKUP_UNIT_TYPE_CODE " 
            + "from CUSTOMER_ITEM_PRICE "
            + "inner join CONTRACT_PRICE_PROF_CUSTOMER "
            + "on CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ = CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ "
            + "and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID "
            + "and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_TYPE_CODE = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE "
            + "where CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and CUSTOMER_ITEM_PRICE.ITEM_PRICE_LEVEL_CODE = :ITEM_PRICE_LEVEL_CODE " 
            + "and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID =:GFS_CUSTOMER_ID "
            + "and CUSTOMER_ITEM_PRICE.EXPIRATION_DATE>=SYSDATE ";
    
    // @formatter:on

    @Override
    public ItemLevelMarkupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setItemId(rs.getString(ITEM_PRICE_ID));
        itemLevelMarkupDTO.setEffectiveDate(rs.getDate(EFFECTIVE_DATE));
        itemLevelMarkupDTO.setExpirationDate(rs.getDate(EXPIRATION_DATE));
        itemLevelMarkupDTO.setMarkup(rs.getString(COST_MARKUP_AMT));
        itemLevelMarkupDTO.setMarkupType(rs.getInt(MARKUP_UNIT_TYPE_CODE));
        itemLevelMarkupDTO.setUnit(rs.getString(MARKUP_AMOUNT_TYPE_CODE));
        return itemLevelMarkupDTO;
    }

}
