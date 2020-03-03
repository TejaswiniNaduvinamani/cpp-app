package com.gfs.cpp.data.markup;

import static com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRowMapper.FETCH_PRC_PROF_PRICING_RULE_OVRD_FOR_CPP_SEQ;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;
import com.gfs.cpp.data.contractpricing.PrcProfPricingRuleOvrdParamListBuilder;

@Repository(value = "prcProfPricingRuleOvrdRepository")
public class PrcProfPricingRuleOvrdRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private PrcProfPricingRuleOvrdParamListBuilder prcProfPricingRuleOvrdParamListBuilder;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRICING_OVERRIDE_ID = "PRICING_OVERRIDE_ID";
    private static final String PRICING_OVERRIDE_IND = "PRICING_OVERRIDE_IND";
    private static final String NEW_EFFECTIVE_DATE = "NEW_EFFECTIVE_DATE";
    private static final String NEW_EXPIRATION_DATE = "NEW_EXPIRATION_DATE";

    //@formatter:off
    private static final String INSERT_PRC_PROF_PRICING_RULE_OVRD = "Insert into PRC_PROF_PRICING_RULE_OVRD "
            + "(PRC_PROF_PRICING_RULE_OVRD_SEQ,"
            + " GFS_CUSTOMER_ID ,"
            + " GFS_CUSTOMER_TYPE_CODE,"
            + "PRICING_OVERRIDE_ID,"
            + " EFFECTIVE_DATE,"
            + " EXPIRATION_DATE,"
            + "CREATE_USER_ID,"
            + "LAST_UPDATE_USER_ID,"
            + "PRICING_OVERRIDE_IND,"
            + "CONTRACT_PRICE_PROFILE_SEQ) "
            + "values (PRC_PROF_PRICING_RULE_OVRD_SEQ.nextVal, "
            + ":GFS_CUSTOMER_ID,"
            + ":GFS_CUSTOMER_TYPE_CODE,"
            + ":PRICING_OVERRIDE_ID,"
            + ":EFFECTIVE_DATE,"
            + ":EXPIRATION_DATE,"
            + ":CREATE_USER_ID,"
            + ":LAST_UPDATE_USER_ID,"
            + ":PRICING_OVERRIDE_IND,"
            + ":CONTRACT_PRICE_PROFILE_SEQ)";
    
    private static final String UPDATE_PRC_PROF_PRICING_RULE_OVRD = "Update PRC_PROF_PRICING_RULE_OVRD SET "
            + "GFS_CUSTOMER_TYPE_CODE =:GFS_CUSTOMER_TYPE_CODE, "
            + "LAST_UPDATE_TMSTMP=SYSTIMESTAMP, "
            + "PRICING_OVERRIDE_ID=:PRICING_OVERRIDE_ID,"
            + "EFFECTIVE_DATE=:EFFECTIVE_DATE,"
            + "EXPIRATION_DATE=:EXPIRATION_DATE,"
            + "LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID,"
            + "PRICING_OVERRIDE_IND=:PRICING_OVERRIDE_IND "
            + "where CONTRACT_PRICE_PROFILE_SEQ =  :CONTRACT_PRICE_PROFILE_SEQ "
            + "and GFS_CUSTOMER_ID= :GFS_CUSTOMER_ID";

    private static final String FETCH_PRC_PROF_PRICING_RULE_OVRD = "select PRC_PROF_PRICING_RULE_OVRD.PRICING_OVERRIDE_IND from PRC_PROF_PRICING_RULE_OVRD "
            + "inner join CONTRACT_PRICE_PROF_CUSTOMER "
            + "on PRC_PROF_PRICING_RULE_OVRD.CONTRACT_PRICE_PROFILE_SEQ = CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ "
            + "and PRC_PROF_PRICING_RULE_OVRD.GFS_CUSTOMER_ID = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID "
            + "and PRC_PROF_PRICING_RULE_OVRD.GFS_CUSTOMER_TYPE_CODE = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE "
            + "where PRC_PROF_PRICING_RULE_OVRD.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and PRC_PROF_PRICING_RULE_OVRD.EXPIRATION_DATE>=SYSDATE";
    
    private static final String FETCH_COUNT_PRC_PROF_PRICING_RULE_OVRD = "select count(*) from PRC_PROF_PRICING_RULE_OVRD "
            + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ"
            + " and EXPIRATION_DATE >= SYSDATE";
    
    private static final String EXPIRE_PRC_PROF_PRICING_RULE_OVRD_NON_CMG_CUSTOMER_BY_CPP_SEQ = "UPDATE PRC_PROF_PRICING_RULE_OVRD " 
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND EFFECTIVE_DATE <= EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_TYPE_CODE != "+CPPConstants.CMG_CUSTOMER_TYPE_CODE+"";  
    
    private static final String EXPIRE_PRC_PROF_PRICING_RULE_OVRD_FOR_REAL_CUST = "UPDATE PRC_PROF_PRICING_RULE_OVRD " 
            + " SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + " WHERE EXPIRATION_DATE             > :EXPIRATION_DATE"
            + " AND GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE"
            + " AND PRICING_OVERRIDE_ID ="+CPPConstants.MARKUP_BASED_ON_SELL_OVERRIDE_ID
            + " AND EXPIRATION_DATE             >= :NEW_EFFECTIVE_DATE"
            + " AND EFFECTIVE_DATE              <= :NEW_EXPIRATION_DATE"
            + " AND EFFECTIVE_DATE <= EXPIRATION_DATE";
    
    // @formatter:on

    public void savePrcProfPricingRuleOvrdForCustomer(List<PrcProfPricingRuleOvrdDO> prcProfPricingRuleOvrdDOList) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO : prcProfPricingRuleOvrdDOList) {
            MapSqlParameterSource paramMap = prcProfPricingRuleOvrdParamListBuilder.createParamMap(prcProfPricingRuleOvrdDO);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_PRICING_RULE_OVRD, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public List<PrcProfPricingRuleOvrdDTO> fetchPrcProfPricingRuleOvrdForCPPSeq(int contractPriceProfileSeq,
            CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, cmgCustomerResponseDTO.getId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, cmgCustomerResponseDTO.getTypeCode(), Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_PRICING_RULE_OVRD_FOR_CPP_SEQ, paramMap, new PrcProfPricingRuleOvrdRowMapper());
    }

    public Integer fetchPrcProfOvrdInd(int contractPriceProfileSeq) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(FETCH_PRC_PROF_PRICING_RULE_OVRD, paramMap, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void saveMarkupOnSellIndicator(MarkupRequestDTO markupRequest, String gfsCustomerId, int gfsCustomerTypeCode, String userName,
            Date effectiveDate, Date expirationDate) {
        MapSqlParameterSource paramMap = createParamMarkupBasedOnSell(markupRequest, gfsCustomerId, gfsCustomerTypeCode, userName, effectiveDate,
                expirationDate);
        cppJdbcTemplate.update(INSERT_PRC_PROF_PRICING_RULE_OVRD, paramMap);
    }

    public void updateMarkupOnSellIndicator(MarkupRequestDTO markupRequest, String gfsCustomerId, int gfsCustomerTypeCode, String userName,
            Date effectiveDate, Date expirationDate) {
        MapSqlParameterSource paramMap = createParamMarkupBasedOnSell(markupRequest, gfsCustomerId, gfsCustomerTypeCode, userName, effectiveDate,
                expirationDate);
        cppJdbcTemplate.update(UPDATE_PRC_PROF_PRICING_RULE_OVRD, paramMap);
    }

    public int fetchSellPriceCount(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_COUNT_PRC_PROF_PRICING_RULE_OVRD, paramMap, Integer.class);
    }

    public void expireNonCmgPriceProfRuleOverdForContract(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        cppJdbcTemplate.update(EXPIRE_PRC_PROF_PRICING_RULE_OVRD_NON_CMG_CUSTOMER_BY_CPP_SEQ, paramMap);
    }

    public void expirePriceProfRuleOverdForRealCust(Date expirationDate, String updatedUserId, String customerId, int customerTypeCode,
            Date newPricingEffectiveDate, Date newPricingExpirationDate) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, customerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerTypeCode, Types.INTEGER);
        paramMap.addValue(NEW_EFFECTIVE_DATE, newPricingEffectiveDate, Types.DATE);
        paramMap.addValue(NEW_EXPIRATION_DATE, newPricingExpirationDate, Types.DATE);
        cppJdbcTemplate.update(EXPIRE_PRC_PROF_PRICING_RULE_OVRD_FOR_REAL_CUST, paramMap);
    }

    private MapSqlParameterSource createParamMarkupBasedOnSell(MarkupRequestDTO markupRequest, String gfsCustomerId, int gfsCustomerTypeCode,
            String userName, Date effectiveDate, Date expirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        paramMap.addValue(PRICING_OVERRIDE_ID, CPPConstants.MARKUP_BASED_ON_SELL_OVERRIDE_ID, Types.INTEGER);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(PRICING_OVERRIDE_IND, markupRequest.isMarkupOnSell() ? 1 : 0, Types.INTEGER);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, markupRequest.getContractPriceProfileSeq(), Types.INTEGER);
        return paramMap;
    }
}
