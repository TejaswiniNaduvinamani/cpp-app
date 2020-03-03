package com.gfs.cpp.data.assignment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;

public class RealCustomerDTORowMapper implements RowMapper<RealCustomerDTO> {

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";

    // @formatter:off
    
    public static final String FETCH_SAVED_ASSIGNMENT_LIST = "Select GFS_CUSTOMER_ID, "
            + "GFS_CUSTOMER_TYPE_CODE "
            + "from CPP_CONCEPT_MAPPING "
            + "where CPP_CUSTOMER_SEQ = :CPP_CUSTOMER_SEQ "
            + "and EXPIRATION_DATE>SYSDATE";
    
    public static final String FETCH_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT = "select "
            + "m.GFS_CUSTOMER_ID, "
            + "m.GFS_CUSTOMER_TYPE_CODE "
            + " from CONTRACT_PRICE_PROF_CUSTOMER c "
            + "inner join  CPP_CONCEPT_MAPPING m "
            + " on c.cpp_customer_seq = m.cpp_customer_seq "
            + "where c.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "AND c.DEFAULT_CUSTOMER_IND = 1 ";
	
    public static final String FETCH_SAVED_ASSIGNMENT_LIST_FOR_CPP_SEQ = "Select GFS_CUSTOMER_ID, "
            + "GFS_CUSTOMER_TYPE_CODE "
            + "from CPP_CONCEPT_MAPPING "
            + "where CPP_CUSTOMER_SEQ = :CPP_CUSTOMER_SEQ ";
   
    // @formatter:on

    @Override
    public RealCustomerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerId(rs.getString(GFS_CUSTOMER_ID));
        realCustomerDTO.setRealCustomerType(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        return realCustomerDTO;
    }

}
