package com.gfs.cpp.data.contractpricing;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;

public class CMGCustomerResponseRowMapper implements RowMapper<CMGCustomerResponseDTO> {

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String DEFAULT_CUSTOMER_IND = "DEFAULT_CUSTOMER_IND";
    private static final String CPP_CUSTOMER_SEQ = "CPP_CUSTOMER_SEQ";

    // @formatter:off
    public static final String SELECT_CUSTOMER_FIELDS = "Select GFS_CUSTOMER_ID,"
            + " DEFAULT_CUSTOMER_IND, "
            + " GFS_CUSTOMER_TYPE_CODE, "
            + " CPP_CUSTOMER_SEQ "
            + " FROM CONTRACT_PRICE_PROF_CUSTOMER";
    
    public static final String FETCH_GFS_CUSTOMER_DETAIL = SELECT_CUSTOMER_FIELDS 
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " AND DEFAULT_CUSTOMER_IND = :DEFAULT_CUSTOMER_IND "
            + " ORDER BY GFS_CUSTOMER_ID desc";
    
    public static final String FETCH_ALL_GFS_CUSTOMER = SELECT_CUSTOMER_FIELDS
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ ";   
    
    public static final String FETCH_GFS_CUSTOMER = SELECT_CUSTOMER_FIELDS
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ"
            + " AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID"
            + " AND GFS_CUSTOMER_TYPE_CODE= :GFS_CUSTOMER_TYPE_CODE";
    
   
    // @formatter:on

    @Override
    public CMGCustomerResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setCppCustomerSeq(rs.getInt(CPP_CUSTOMER_SEQ));
        cmgCustomerResponseDTO.setId(rs.getString(GFS_CUSTOMER_ID));
        cmgCustomerResponseDTO.setTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        cmgCustomerResponseDTO.setDefaultCustomerInd(rs.getInt(DEFAULT_CUSTOMER_IND));
        return cmgCustomerResponseDTO;
    }
}