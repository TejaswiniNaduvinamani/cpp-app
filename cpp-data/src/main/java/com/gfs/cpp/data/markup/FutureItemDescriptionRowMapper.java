package com.gfs.cpp.data.markup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;

public class FutureItemDescriptionRowMapper implements RowMapper<FutureItemDescriptionDTO> {

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_DESC = "ITEM_DESC";
    private static final String CUSTOMER_ITEM_DESC_PRICE_SEQ = "CUSTOMER_ITEM_DESC_PRICE_SEQ";
    private static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    private static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";

    //@formatter:off
    public static final String FETCH_ALL_CUSTOMER_ITEM_DESC_PRICE ="select CUSTOMER_ITEM_DESC_PRICE_SEQ,"
            + "ITEM_DESC, "
            + "GFS_CUSTOMER_ID, "
            + "GFS_CUSTOMER_TYPE_CODE, "
            + "COST_MARKUP_AMT,"
            + "MARKUP_AMOUNT_TYPE_CODE,"
            + "MARKUP_UNIT_TYPE_CODE  "
            + "from CUSTOMER_ITEM_DESC_PRICE where "
            + "CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    public static final String FETCH_CUSTOMER_ITEM_DESC_PRICE_FOR_FURTHERANCE ="select CUSTOMER_ITEM_DESC_PRICE_SEQ,"
            + "ITEM_DESC, "
            + "GFS_CUSTOMER_ID, "
            + "GFS_CUSTOMER_TYPE_CODE, "
            + "COST_MARKUP_AMT,"
            + "MARKUP_AMOUNT_TYPE_CODE,"
            + "MARKUP_UNIT_TYPE_CODE  "
            + "from CUSTOMER_ITEM_DESC_PRICE where "
            + "CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "and ITEM_DESC = :ITEM_DESC "
            + "and GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE ";
    //@formatter:on

    @Override
    public FutureItemDescriptionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        FutureItemDescriptionDTO futureItemDescriptionDTO = new FutureItemDescriptionDTO();
        futureItemDescriptionDTO.setCustomerItemDescSeq(rs.getInt(CUSTOMER_ITEM_DESC_PRICE_SEQ));
        futureItemDescriptionDTO.setFutureItemDesc(rs.getString(ITEM_DESC));
        futureItemDescriptionDTO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        futureItemDescriptionDTO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        futureItemDescriptionDTO.setCostMarkupAmt(rs.getInt(COST_MARKUP_AMT));
        futureItemDescriptionDTO.setMarkupAmountTypeCode(rs.getString(MARKUP_AMOUNT_TYPE_CODE));
        futureItemDescriptionDTO.setMarkupUnitTypeCode(rs.getInt(MARKUP_UNIT_TYPE_CODE));

        return futureItemDescriptionDTO;
    }

}
