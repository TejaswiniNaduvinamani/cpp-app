package com.gfs.cpp.data.markup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;

public class ItemFutureTypeMarkupRowMapper implements RowMapper<ItemLevelMarkupDTO> {

    public static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    public static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";
    public static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    public static final String ITEM_DESC = "ITEM_DESC";
    public static final String CUSTOMER_ITEM_DESC_PRICE_SEQ = "CUSTOMER_ITEM_DESC_PRICE_SEQ";

    //@formatter:off
    public static final String FETCH_CUSTOMER_ITEM_DESC_PRICE = "select CUSTOMER_ITEM_DESC_PRICE.CUSTOMER_ITEM_DESC_PRICE_SEQ,"
            + "CUSTOMER_ITEM_DESC_PRICE.ITEM_DESC, "
            + "CUSTOMER_ITEM_DESC_PRICE.COST_MARKUP_AMT, "
            + "CUSTOMER_ITEM_DESC_PRICE.MARKUP_AMOUNT_TYPE_CODE, "
            + "CUSTOMER_ITEM_DESC_PRICE.MARKUP_UNIT_TYPE_CODE "
            + "from CUSTOMER_ITEM_DESC_PRICE " 
            + "inner join CONTRACT_PRICE_PROF_CUSTOMER " 
            + "on CUSTOMER_ITEM_DESC_PRICE.CONTRACT_PRICE_PROFILE_SEQ = CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_ID = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_TYPE_CODE = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE "
            + "where CUSTOMER_ITEM_DESC_PRICE.CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE ";
    
    public static final String FETCH_CUSTOMER_ITEM_DESC_PRICE_FOR_ASSIGNMENT = "select CUSTOMER_ITEM_DESC_PRICE.CUSTOMER_ITEM_DESC_PRICE_SEQ, "
            + "CUSTOMER_ITEM_DESC_PRICE.ITEM_DESC, "
            + "CUSTOMER_ITEM_DESC_PRICE.COST_MARKUP_AMT, "
            + "CUSTOMER_ITEM_DESC_PRICE.MARKUP_AMOUNT_TYPE_CODE, "
            + "CUSTOMER_ITEM_DESC_PRICE.MARKUP_UNIT_TYPE_CODE "
            + "from CUSTOMER_ITEM_DESC_PRICE " 
            + "inner join CONTRACT_PRICE_PROF_CUSTOMER " 
            + "on CUSTOMER_ITEM_DESC_PRICE.CONTRACT_PRICE_PROFILE_SEQ = CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_ID = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_TYPE_CODE = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE "
            + "where CUSTOMER_ITEM_DESC_PRICE.CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "and CUSTOMER_ITEM_DESC_PRICE.ITEM_DESC = :ITEM_DESC "
            + "and CUSTOMER_ITEM_DESC_PRICE.GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE ";
    //@formatter:on

    @Override
    public ItemLevelMarkupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setCustomerItemDescSeq(rs.getInt(CUSTOMER_ITEM_DESC_PRICE_SEQ));
        itemLevelMarkupDTO.setItemDesc(rs.getString(ITEM_DESC));
        itemLevelMarkupDTO.setMarkup(rs.getString(COST_MARKUP_AMT));
        itemLevelMarkupDTO.setMarkupType(rs.getInt(MARKUP_UNIT_TYPE_CODE));
        itemLevelMarkupDTO.setUnit(rs.getString(MARKUP_AMOUNT_TYPE_CODE));
        return itemLevelMarkupDTO;
    }

}
