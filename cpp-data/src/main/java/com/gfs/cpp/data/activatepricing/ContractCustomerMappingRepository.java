package com.gfs.cpp.data.activatepricing;

import static com.gfs.cpp.data.activatepricing.ContractCustomerRowMapper.FETCH_CUSTOMERS_FOR_ALL_MARKUP;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;

@Repository(value = "contractCustomerMappingRepository")
public class ContractCustomerMappingRepository {

    
    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    
    // @formatter:off
    
    public static final String UNMAPPED_CONTRACT_CONCEPTS = "select count(contractPriceProfCust.CPP_CUSTOMER_SEQ) " + 
            " from CONTRACT_PRICE_PROF_CUSTOMER contractPriceProfCust " + 
            " LEFT JOIN CPP_CONCEPT_MAPPING cppConceptMapping ON " + 
            " contractPriceProfCust.CPP_CUSTOMER_SEQ = cppConceptMapping.CPP_CUSTOMER_SEQ " + 
            " WHERE contractPriceProfCust.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " + 
            " AND cppConceptMapping.CPP_CUSTOMER_SEQ is null ";
    
    // @formatter:on
    
    public Integer fetchUnmappedConceptCount(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(UNMAPPED_CONTRACT_CONCEPTS, paramMap, Integer.class);
    }
    
    public List<ContractCustomerMappingDTO> fetchAllConceptCustomerMapping(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_CUSTOMERS_FOR_ALL_MARKUP, paramMap, new ContractCustomerRowMapper());
    }
    
}

