package com.gfs.cpp.data.contractpricing;

import static com.gfs.cpp.data.contractpricing.CMGCustomerResponseRowMapper.FETCH_ALL_GFS_CUSTOMER;
import static com.gfs.cpp.data.contractpricing.CMGCustomerResponseRowMapper.FETCH_GFS_CUSTOMER;
import static com.gfs.cpp.data.contractpricing.CMGCustomerResponseRowMapper.FETCH_GFS_CUSTOMER_DETAIL;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;

@Repository(value = "contractPriceProfCustomerRepository")
public class ContractPriceProfCustomerRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String DEFAULT_CUSTOMER_IND = "DEFAULT_CUSTOMER_IND";
    private static final String CPP_CUSTOMER_SEQ = "CPP_CUSTOMER_SEQ";

    // @formatter:off
    private static final String INSERT_CONTRACT_PRICE_PROF_CUSTOMER = "Insert into CONTRACT_PRICE_PROF_CUSTOMER "
            + "(CPP_CUSTOMER_SEQ," 
            + "CONTRACT_PRICE_PROFILE_SEQ," 
            + "GFS_CUSTOMER_ID," 
            + "GFS_CUSTOMER_TYPE_CODE,"
            + "CREATE_USER_ID," 
            + "DEFAULT_CUSTOMER_IND) "
            + "values (:CPP_CUSTOMER_SEQ , " 
            + ":CONTRACT_PRICE_PROFILE_SEQ , "
            + ":GFS_CUSTOMER_ID , " 
            + ":GFS_CUSTOMER_TYPE_CODE, " 
            + ":CREATE_USER_ID," 
            + ":DEFAULT_CUSTOMER_IND)";
    
    private static final String DELETE_CONTRACT_PRICE_PROF_CUSTOMER = "Delete from CONTRACT_PRICE_PROF_CUSTOMER where "
            + " CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    
    private static final String FETCH_GFS_CUSTOMER_ID = "Select GFS_CUSTOMER_ID "
            + "from CONTRACT_PRICE_PROF_CUSTOMER "
            + "where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and DEFAULT_CUSTOMER_IND = :DEFAULT_CUSTOMER_IND";
    
    private static final String FETCH_CPP_CUSTOMER_SEQ = "Select CPP_CUSTOMER_SEQ "
            + "from CONTRACT_PRICE_PROF_CUSTOMER "
            + "where GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "and GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE "
            + "and  CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ";
    
    private static final String FETCH_CPP_CUSTOMER_NEXT_SEQ = "select CONTRACT_PRC_PROF_CUSTOMER_SEQ.nextVal from dual";
    
    // @formatter:on

    public void saveContractPriceProfCustomer(int contractPriceProfileSeq, String gfsCustomerId, int customerTypeCode, String userName,
            int defaultCustomerInd, int cppCustomerSeq) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(DEFAULT_CUSTOMER_IND, defaultCustomerInd, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerTypeCode, Types.INTEGER);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CPP_CUSTOMER_SEQ, cppCustomerSeq, Types.INTEGER);
        cppJdbcTemplate.update(INSERT_CONTRACT_PRICE_PROF_CUSTOMER, paramMap);
    }

    public Integer deleteCPPCustomer(int contractPriceProfileSeq, String gfsCustomerId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.update(DELETE_CONTRACT_PRICE_PROF_CUSTOMER, paramMap);
    }

    public CMGCustomerResponseDTO fetchDefaultCmg(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(DEFAULT_CUSTOMER_IND, 1, Types.INTEGER);
        List<CMGCustomerResponseDTO> result = cppJdbcTemplate.query(FETCH_GFS_CUSTOMER_DETAIL, paramMap, new CMGCustomerResponseRowMapper());
        return result.get(0);
    }

    public CMGCustomerResponseDTO fetchLatestCreatedExceptionCmg(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(DEFAULT_CUSTOMER_IND, 0, Types.INTEGER);
        List<CMGCustomerResponseDTO> result = cppJdbcTemplate.query(FETCH_GFS_CUSTOMER_DETAIL, paramMap, new CMGCustomerResponseRowMapper());
        return result.get(0);
    }

    public List<CMGCustomerResponseDTO> fetchGFSCustomerDetailsList(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_ALL_GFS_CUSTOMER, paramMap, new CMGCustomerResponseRowMapper());
    }

    public CMGCustomerResponseDTO fetchGFSCustomerDetailForCustomer(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_GFS_CUSTOMER, paramMap, new CMGCustomerResponseRowMapper());
    }

    public String fetchGfsCustomerId(int contractPriceProfileSeq, boolean isDefault) {
        MapSqlParameterSource paramMapQuery = new MapSqlParameterSource();
        paramMapQuery.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq);
        if (isDefault) {
            paramMapQuery.addValue(DEFAULT_CUSTOMER_IND, CPPConstants.INDICATOR_ONE, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(FETCH_GFS_CUSTOMER_ID, paramMapQuery, String.class);
        } else {
            paramMapQuery.addValue(DEFAULT_CUSTOMER_IND, CPPConstants.INDICATOR_ZERO, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(FETCH_GFS_CUSTOMER_ID, paramMapQuery, String.class);
        }
    }

    public int fetchCPPCustomerSeq(String gfsCustomerId, int gfsCustomerTypeCode, int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_CPP_CUSTOMER_SEQ, paramMap, Integer.class);
    }

    public int fetchCPPCustomerNextSequence() {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        return cppJdbcTemplate.queryForObject(FETCH_CPP_CUSTOMER_NEXT_SEQ, paramMap, Integer.class);
    }

}
