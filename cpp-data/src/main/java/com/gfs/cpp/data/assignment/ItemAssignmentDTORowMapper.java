package com.gfs.cpp.data.assignment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;

public class ItemAssignmentDTORowMapper implements RowMapper<ItemAssignmentDTO> {

    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String CUSTOMER_ITEM_DESC_PRICE_SEQ = "CUSTOMER_ITEM_DESC_PRICE_SEQ";

    // @formatter:off
    
    public static final String FETCH_ASSIGNED_ITEMS_IN_A_CONCEPT = "Select CUSTOMER_ITEM_DESC_PRICE_SEQ,ITEM_PRICE_ID, "
            + " ITEM_PRICE_LEVEL_CODE, "
            + " EFFECTIVE_DATE, "
            + " EXPIRATION_DATE "
            + " FROM CPP_ITEM_MAPPING  "
            + " WHERE CUSTOMER_ITEM_DESC_PRICE_SEQ IN (:LIST_OF_ITEM_DESC_SEQ) "
            + " AND EXPIRATION_DATE>=SYSDATE";
    
   // @formatter:on

    @Override
    public ItemAssignmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemPriceLevelCode(rs.getInt(ITEM_PRICE_LEVEL_CODE));
        itemAssignmentDTO.setItemId(rs.getString(ITEM_PRICE_ID));
        itemAssignmentDTO.setEffectiveDate(rs.getDate(EFFECTIVE_DATE));
        itemAssignmentDTO.setExpirationDate(rs.getDate(EXPIRATION_DATE));
        itemAssignmentDTO.setCustomerItemDescSeq(rs.getInt(CUSTOMER_ITEM_DESC_PRICE_SEQ));
        return itemAssignmentDTO;
    }
}
