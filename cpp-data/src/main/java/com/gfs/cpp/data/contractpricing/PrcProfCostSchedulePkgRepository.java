package com.gfs.cpp.data.contractpricing;

import static com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgDORowMapper.FETCH_PRC_PROF_COST_SCHEDULE_BY_CPP_SEQ;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;

@Repository(value = "prcProfCostSchedulePkgRepository")
public class PrcProfCostSchedulePkgRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private ContractPricingParamListBuilder contractPricingParamListBuilder;

    @Autowired
    private PrcProfCostSchedulePkgParamListBuilder prcProfCostSchedulePkgParamListBuilder;

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRC_PROF_COST_SCHEDULE_PKG_SEQ = "PRC_PROF_COST_SCHEDULE_PKG_SEQ";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String NEW_EFFECTIVE_DATE = "NEW_EFFECTIVE_DATE";
    private static final String NEW_EXPIRATION_DATE = "NEW_EXPIRATION_DATE";

 // @formatter:off
    private static final String INSERT_PRC_PROF_COST_SCHEDULE_PKG = "Insert into PRC_PROF_COST_SCHEDULE_PKG "
            + "(PRC_PROF_COST_SCHEDULE_PKG_SEQ," 
            + "GFS_CUSTOMER_ID ," 
            + "GFS_CUSTOMER_TYPE_CODE ," 
            + "EFFECTIVE_DATE ,"
            + "EXPIRATION_DATE ," 
            + "CREATE_USER_ID ," 
            + "LAST_UPDATE_USER_ID ," 
            + "CONTRACT_PRICE_PROFILE_SEQ ) "
            + " values (:PRC_PROF_COST_SCHEDULE_PKG_SEQ," 
            + ":GFS_CUSTOMER_ID, " 
            + ":GFS_CUSTOMER_TYPE_CODE, "
            + ":EFFECTIVE_DATE, " 
            + ":EXPIRATION_DATE, " 
            + ":CREATE_USER_ID, "
            + ":LAST_UPDATE_USER_ID, " 
            + ":CONTRACT_PRICE_PROFILE_SEQ) ";
    
    private static final String DELETE_PRC_PROF_COST_SCHEDULE_PKG = "DELETE from PRC_PROF_COST_SCHEDULE_PKG "
            + " WHERE "
            + " CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ";
    
    private static final String FETCH_PRC_PROF_COST_SCHEDULE_PKG_SEQ = "SELECT PRC_PROF_COST_SCHEDULE_PKG_SEQ.nextVal from dual";

    private static final String EXPIRE_PRC_PROF_COST_SCHEDULE_PKG_FOR_NON_CMG_CUSTOMER_BY_CPP_SEQ = "UPDATE PRC_PROF_COST_SCHEDULE_PKG "
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND EFFECTIVE_DATE <= EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_TYPE_CODE != "+CPPConstants.CMG_CUSTOMER_TYPE_CODE+"";
    
    private static final String EXPIRE_PRC_PROF_COST_SCHEDULE_PKG_FOR_REAL_CUST = "UPDATE PRC_PROF_COST_SCHEDULE_PKG "
            + " SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + " LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + " LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + " WHERE GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE"
            + " AND EXPIRATION_DATE             >= :NEW_EFFECTIVE_DATE"
            + " AND EFFECTIVE_DATE              <= :NEW_EXPIRATION_DATE "
            + " AND EFFECTIVE_DATE <= EXPIRATION_DATE";

    private static final String UPDATE_PRC_PROF_COST_SCHEDULE_FOR_CPP_SEQ="UPDATE PRC_PROF_COST_SCHEDULE_PKG"
            + " SET LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID, "
            + " LAST_UPDATE_TMSTMP=SYSTIMESTAMP "
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + " AND EXPIRATION_DATE>=SYSDATE";
    
    // @formatter:on

    public List<PrcProfCostSchedulePkgDO> fetchPrcProfCostScheduleForCPPSeq(int contractPriceProfileSeq, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, cmgCustomerResponseDTO.getId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, cmgCustomerResponseDTO.getTypeCode(), Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_COST_SCHEDULE_BY_CPP_SEQ, paramMap, new PrcProfCostSchedulePkgDORowMapper());
    }

    public void savePrcProfCostScheduleForCustomer(List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOList) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO : prcProfCostSchedulePkgDOList) {
            MapSqlParameterSource paramMap = prcProfCostSchedulePkgParamListBuilder.createParamMap(prcProfCostSchedulePkgDO);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_COST_SCHEDULE_PKG, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }
    
    public void saveScheduleForCostChange(String userName, int contractPriceProfileSeq, ContractPricingDO contractPricingDO, int costSchedulePkgSeq) {
        MapSqlParameterSource paramMap = contractPricingParamListBuilder.createParamMap(userName, contractPriceProfileSeq,
                contractPricingDO.getContractPriceProfileId(), contractPricingDO);
        paramMap.addValue(PRC_PROF_COST_SCHEDULE_PKG_SEQ, costSchedulePkgSeq);
        cppJdbcTemplate.update(INSERT_PRC_PROF_COST_SCHEDULE_PKG, paramMap);

    }

    public void deleteCostSchedulePackage(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_PRC_PROF_COST_SCHEDULE_PKG, paramMap);
    }

    public int fetchPrcProfileCostSchedulePkgNextSeq() {
        return cppJdbcTemplate.queryForObject(FETCH_PRC_PROF_COST_SCHEDULE_PKG_SEQ, new MapSqlParameterSource(), Integer.class);
    }

    public void expireNonCmgCostSchedulePackageForContract(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);

        cppJdbcTemplate.update(EXPIRE_PRC_PROF_COST_SCHEDULE_PKG_FOR_NON_CMG_CUSTOMER_BY_CPP_SEQ, paramMap);
    }

    public void expireCostSchedulePackageForRealCust(Date expirationDate, String updatedUserId, String customerId, int customerTypeCode,
            Date newPricingEffectiveDate, Date newPricingExpiryDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, customerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerTypeCode, Types.INTEGER);
        paramMap.addValue(NEW_EFFECTIVE_DATE, newPricingEffectiveDate, Types.DATE);
        paramMap.addValue(NEW_EXPIRATION_DATE, newPricingExpiryDate, Types.DATE);
        cppJdbcTemplate.update(EXPIRE_PRC_PROF_COST_SCHEDULE_PKG_FOR_REAL_CUST, paramMap);
    }

    public void updateLastUpdateUserId(String userName, int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(UPDATE_PRC_PROF_COST_SCHEDULE_FOR_CPP_SEQ, paramMap);
    }

}
