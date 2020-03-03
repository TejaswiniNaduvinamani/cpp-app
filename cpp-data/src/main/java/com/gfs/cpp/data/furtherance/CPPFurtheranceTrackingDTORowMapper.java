package com.gfs.cpp.data.furtherance;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;

public class CPPFurtheranceTrackingDTORowMapper implements RowMapper<CPPFurtheranceTrackingDTO> {

    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String FURTHERANCE_ACTION_CODE = "FURTHERANCE_ACTION_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CHANGE_TABLE_NAME = "CHANGE_TABLE_NAME";

    // @formatter:off
  
    public static final String FETCH_ALL_CPP_FURTHERANCE_TRACKING_RECORDS_BY_CPP_FURTHERANCE_SEQUENCE = "select "
            + " CPP_FURTHERANCE_SEQ,"
            + " FURTHERANCE_ACTION_CODE,"
            + " ITEM_PRICE_ID,"
            + " ITEM_PRICE_LEVEL_CODE,"
            + " GFS_CUSTOMER_ID,"
            + " GFS_CUSTOMER_TYPE_CODE,"
            + " CHANGE_TABLE_NAME"
            + " from CPP_FURTHERANCE_TRACKING "
            + " WHERE CPP_FURTHERANCE_SEQ=:CPP_FURTHERANCE_SEQ";

    // @formatter:on

    @Override
    public CPPFurtheranceTrackingDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = new CPPFurtheranceTrackingDTO();
        cppFurtheranceTrackingDTO.setCppFurtheranceSeq(rs.getInt(CPP_FURTHERANCE_SEQ));
        cppFurtheranceTrackingDTO.setChangeTableName(rs.getString(CHANGE_TABLE_NAME));
        cppFurtheranceTrackingDTO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        cppFurtheranceTrackingDTO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        cppFurtheranceTrackingDTO.setFurtheranceActionCode(rs.getInt(FURTHERANCE_ACTION_CODE));
        cppFurtheranceTrackingDTO.setItemPriceId(rs.getString(ITEM_PRICE_ID));
        cppFurtheranceTrackingDTO.setItemPriceLevelCode(rs.getInt(ITEM_PRICE_LEVEL_CODE));

        return cppFurtheranceTrackingDTO;
    }

}
