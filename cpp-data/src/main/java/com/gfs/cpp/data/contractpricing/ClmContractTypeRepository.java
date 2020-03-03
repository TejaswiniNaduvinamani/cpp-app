package com.gfs.cpp.data.contractpricing;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClmContractTypeRepository {

    private static final String CLM_CONTRACT_TYPE_NAME = "CLM_CONTRACT_TYPE_NAME";
    private static final String CLM_CONTRACT_TYPE_SEQ = "CLM_CONTRACT_TYPE_SEQ";

    // @formatter:off
    
    private static final String FETCH_ALL_CONTRACT_TYPES = "select CLM_CONTRACT_TYPE_NAME from CLM_CONTRACT_TYPE";

    private static final String FETCH_CLM_CONTRACT_TYPE_SEQ_BY_TYPE_NAME = "select CLM_CONTRACT_TYPE_SEQ "
            + "from CLM_CONTRACT_TYPE " 
            + "where CLM_CONTRACT_TYPE_NAME = :CLM_CONTRACT_TYPE_NAME";
    
    private static final String FETCH_CLM_CONTRACT_TYPE_NAME_BY_TYPE_SEQ = "select CLM_CONTRACT_TYPE_NAME "
            + "from CLM_CONTRACT_TYPE " 
            + "where CLM_CONTRACT_TYPE_SEQ = :CLM_CONTRACT_TYPE_SEQ";
    
    // @formatter:on

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    public List<String> getAllContractTypes() {
        return cppJdbcTemplate.queryForList(FETCH_ALL_CONTRACT_TYPES, new EmptySqlParameterSource(), String.class);
    }

    public int fetchContractTypeSequenceByTypeName(String clmContractTypeName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CLM_CONTRACT_TYPE_NAME, clmContractTypeName, Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(FETCH_CLM_CONTRACT_TYPE_SEQ_BY_TYPE_NAME, paramMap, Integer.class);
    }

    public String getContractTypeName(int clmContractTypeSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CLM_CONTRACT_TYPE_SEQ, clmContractTypeSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_CLM_CONTRACT_TYPE_NAME_BY_TYPE_SEQ, paramMap, String.class);

    }

}
