package com.gfs.cpp.data.activatepricing;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;

public class ContractCustomerRowMapper implements RowMapper<ContractCustomerMappingDTO> {

    private static final String CPP_CUSTOMER_SEQ = "CPP_CUSTOMER_SEQ";
    private static final String CPP_CONCEPT_MAPPING_SEQ = "CPP_CONCEPT_MAPPING_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String DEFAULT_CUSTOMER_IND = "DEFAULT_CUSTOMER_IND";

    // @formatter:off
        
    public static final String FETCH_CUSTOMERS_FOR_ALL_MARKUP = "select contractPriceProfCust.CPP_CUSTOMER_SEQ as CPP_CUSTOMER_SEQ, "
           + " cppConceptMapping.CPP_CONCEPT_MAPPING_SEQ as CPP_CONCEPT_MAPPING_SEQ, "
           + " cppConceptMapping.GFS_CUSTOMER_TYPE_CODE as GFS_CUSTOMER_TYPE_CODE, "
           + " cppConceptMapping.GFS_CUSTOMER_ID as GFS_CUSTOMER_ID, "
           + " contractPriceProfCust.DEFAULT_CUSTOMER_IND"
           + " from CONTRACT_PRICE_PROF_CUSTOMER contractPriceProfCust "  
           + " INNER JOIN CPP_CONCEPT_MAPPING cppConceptMapping ON "
           + " contractPriceProfCust.CPP_CUSTOMER_SEQ = cppConceptMapping.CPP_CUSTOMER_SEQ "  
           + " where contractPriceProfCust.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
           + " order by contractPriceProfCust.DEFAULT_CUSTOMER_IND  desc ";
       
     // @formatter:on

    @Override
    public ContractCustomerMappingDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(rs.getInt(CPP_CONCEPT_MAPPING_SEQ));
        contractCustomerMappingDTO.setCppCustomerSeq(rs.getInt(CPP_CUSTOMER_SEQ));
        contractCustomerMappingDTO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        contractCustomerMappingDTO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        contractCustomerMappingDTO.setDefaultCustomerInd(rs.getInt(DEFAULT_CUSTOMER_IND));
        
        return contractCustomerMappingDTO;
    }

}
