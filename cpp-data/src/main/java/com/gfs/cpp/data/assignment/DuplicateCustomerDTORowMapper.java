package com.gfs.cpp.data.assignment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;

public class DuplicateCustomerDTORowMapper implements RowMapper<DuplicateCustomerDTO> {

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";

    // @formatter:off
    public static final String FETCH_DUPLICATE_CUSTOMER_ACROSS_CONCEPT = "select "
            + "c.GFS_CUSTOMER_ID, "
            + "c.GFS_CUSTOMER_TYPE_CODE, "
            + "c.CONTRACT_PRICE_PROFILE_SEQ "
            + "from CONTRACT_PRICE_PROF_CUSTOMER c "
            + "inner join CPP_CONCEPT_MAPPING m "
            + "on c.cpp_customer_seq = m.cpp_customer_seq "
            + "where c.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and m.GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "and m.GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE";
    // @formatter:on

    @Override
    public DuplicateCustomerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        DuplicateCustomerDTO realCustomerValidationDTO = new DuplicateCustomerDTO();
        realCustomerValidationDTO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        realCustomerValidationDTO.setCustomerId(rs.getString(GFS_CUSTOMER_ID));
        realCustomerValidationDTO.setCustomerType(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        return realCustomerValidationDTO;
    }

}
