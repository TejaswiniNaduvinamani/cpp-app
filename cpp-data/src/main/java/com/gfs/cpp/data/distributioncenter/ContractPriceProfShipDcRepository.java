package com.gfs.cpp.data.distributioncenter;

import static com.gfs.cpp.data.distributioncenter.DistributionCenterDetailRowMapper.FETCH_ALL_CONTRACT_PRICE_PROF_SHIP_DC;
import static com.gfs.cpp.data.distributioncenter.DistributionCenterDetailRowMapper.FETCH_CONTRACT_PRICE_PROF_SHIP_DC;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;

@Repository(value = "contractPriceProfShipDcRepository")
public class ContractPriceProfShipDcRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String SHIP_DC_NBR = "SHIP_DC_NBR";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String USER_NAME = "USER_NAME";

    // @formatter:off
    static final String INSERT_INTO_CONTRACT_PRICE_PROF_SHIP_DC = "INSERT INTO CONTRACT_PRICE_PROF_SHIP_DC "
            + "(CPP_SHIP_DC_SEQ, "
            + "CONTRACT_PRICE_PROFILE_SEQ,"
            + "SHIP_DC_NBR, EFFECTIVE_DATE, "
            + "EXPIRATION_DATE, "
            + "CREATE_USER_ID) "
            + "VALUES(CONTRACT_PRC_PROF_SHIP_DC_SEQ.nextval,"
            + " :CONTRACT_PRICE_PROFILE_SEQ, "
            + ":SHIP_DC_NBR, "
            + ":EFFECTIVE_DATE, "
            + ":EXPIRATION_DATE, "
            + ":USER_NAME)";

    static final String DELETE_FROM_CONTRACT_PRICE_PROF_SHIP_DC = "DELETE FROM CONTRACT_PRICE_PROF_SHIP_DC "
            + "where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and SHIP_DC_NBR=:SHIP_DC_NBR";

    static final String FETCH_SHIP_DC_NBR = "SELECT SHIP_DC_NBR "
            + "FROM CONTRACT_PRICE_PROF_SHIP_DC"
            + " where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and EXPIRATION_DATE >= SYSDATE";
    
 // @formatter:on

    public void saveDistributionCenter(DistributionCenterDO distributionCenterDOToInsert, String userName) {
        List<SqlParameterSource> paramList = createParamList(distributionCenterDOToInsert, userName);
        if (!CollectionUtils.isEmpty(paramList)) {
            cppJdbcTemplate.batchUpdate(INSERT_INTO_CONTRACT_PRICE_PROF_SHIP_DC, paramList.toArray(new SqlParameterSource[paramList.size()]));
        }
    }

    public void deleteDistributionCenter(DistributionCenterDO distributionCenterDOToUpdate, String userName) {
        List<SqlParameterSource> paramList = createParamList(distributionCenterDOToUpdate, userName);
        if (!CollectionUtils.isEmpty(paramList)) {
            cppJdbcTemplate.batchUpdate(DELETE_FROM_CONTRACT_PRICE_PROF_SHIP_DC, paramList.toArray(new SqlParameterSource[paramList.size()]));
        }
    }

    public List<DistributionCenterDetailDO> fetchSavedDistributionCenters(Integer contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_CONTRACT_PRICE_PROF_SHIP_DC, paramMap, new DistributionCenterDetailRowMapper());
    }

    public List<DistributionCenterDetailDO> fetchAllDistributionCenter(Integer contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_ALL_CONTRACT_PRICE_PROF_SHIP_DC, paramMap, new DistributionCenterDetailRowMapper());
    }

    public List<String> fetchDistributionCentersbyContractPriceProfileSeq(Integer contractPriceProfileSeq) {
        MapSqlParameterSource paramMapQuery = new MapSqlParameterSource();
        paramMapQuery.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq);
        return cppJdbcTemplate.queryForList(FETCH_SHIP_DC_NBR, paramMapQuery, String.class);
    }

    private List<SqlParameterSource> createParamList(DistributionCenterDO distributionCenterDO, String userName) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        List<String> dcCodes = distributionCenterDO.getDcCodes();
        for (String distributionCode : dcCodes) {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, distributionCenterDO.getContractPriceProfileSeq(), Types.INTEGER);
            paramMap.addValue(SHIP_DC_NBR, distributionCode, Types.VARCHAR);
            paramMap.addValue(EFFECTIVE_DATE, distributionCenterDO.getEffectiveDate(), Types.DATE);
            paramMap.addValue(EXPIRATION_DATE, distributionCenterDO.getExpirationDate(), Types.DATE);
            paramMap.addValue(USER_NAME, userName, Types.VARCHAR);
            paramList.add(paramMap);
        }
        return paramList;
    }
}
