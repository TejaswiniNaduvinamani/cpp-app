package com.gfs.cpp.data.splitcase;

import static com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRowMapper.FETCH_FURTHERANCE_UPDATES_FOR_REAL_CUSTOMER;
import static com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRowMapper.FETCH_PRC_PROF_LESSCASE_RULE_FOR_CPP_SEQ;
import static com.gfs.cpp.data.splitcase.SplitCaseGridDORowMapper.FETCH_PRC_PROF_LESSCASE_RULE_FOR_ALL_CMG_BY_CPP_SEQ;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.data.contractpricing.PrcProfLessCaseRuleParamListBuilder;

@Repository(value = "prcProfLessCaseRuleRepository")
public class PrcProfLessCaseRuleRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private PrcProfLessCaseRuleParamListBuilder prcProfLessCaseRuleParamListBuilder;

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String CW_MARKUP_AMT = "CW_MARKUP_AMT";
    private static final String NON_CW_MARKUP_AMT = "NON_CW_MARKUP_AMT";
    private static final String CW_MARKUP_AMOUNT_TYPE_CODE = "CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String NON_CW_MARKUP_AMOUNT_TYPE_CODE = "NON_CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String LESSCASE_PRICE_RULE_ID = "LESSCASE_PRICE_RULE_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String NEW_EFFECTIVE_DATE = "NEW_EFFECTIVE_DATE";
    private static final String NEW_EXPIRATION_DATE = "NEW_EXPIRATION_DATE";
    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";

    // @formatter:off

    private static final String INSERT_PRC_PROF_LESSCASE_RULE = "INSERT INTO PRC_PROF_LESSCASE_RULE"
            + " (PRC_PROF_LESSCASE_RULE_SEQ, "
            + "GFS_CUSTOMER_ID,"
            + "GFS_CUSTOMER_TYPE_CODE,"
            + "ITEM_PRICE_LEVEL_CODE,"
            + "ITEM_PRICE_ID,"
            + "EFFECTIVE_DATE,"
            + "EXPIRATION_DATE, "
            + "LAST_UPDATE_USER_ID,"
            + "LESSCASE_PRICE_RULE_ID,"
            + "CW_MARKUP_AMT,"
            + "CW_MARKUP_AMOUNT_TYPE_CODE,"
            + "NON_CW_MARKUP_AMT,"
            + "NON_CW_MARKUP_AMOUNT_TYPE_CODE,"
            + "CONTRACT_PRICE_PROFILE_SEQ,"
            + "CREATE_USER_ID)  "
            + "VALUES (PRC_PROF_LESSCASE_RULE_SEQ.nextval, "
            + ":GFS_CUSTOMER_ID, "
            + ":GFS_CUSTOMER_TYPE_CODE,"
            + " :ITEM_PRICE_LEVEL_CODE, "
            + ":ITEM_PRICE_ID, "
            + ":EFFECTIVE_DATE, "
            + ":EXPIRATION_DATE, "
            + ":LAST_UPDATE_USER_ID,"
            + " :LESSCASE_PRICE_RULE_ID, "
            + ":CW_MARKUP_AMT, "
            + ":CW_MARKUP_AMOUNT_TYPE_CODE, "
            + ":NON_CW_MARKUP_AMT,"
            + " :NON_CW_MARKUP_AMOUNT_TYPE_CODE, "
            + ":CONTRACT_PRICE_PROFILE_SEQ,"
            + ":CREATE_USER_ID)";

    private static final String EXPIRE_PRC_PROF_LESSCASE_RULE_FOR_NON_CMG_BY_CPP_SEQ = "UPDATE PRC_PROF_LESSCASE_RULE " 
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND EFFECTIVE_DATE <= EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_TYPE_CODE != "+CPPConstants.CMG_CUSTOMER_TYPE_CODE+"";
    
    private static final String EXPIRE_PRC_PROF_LESSCASE_RULE_FOR_REAL_CUST = "UPDATE PRC_PROF_LESSCASE_RULE " 
            + " SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + " WHERE EXPIRATION_DATE             > :EXPIRATION_DATE"
            + " AND GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE"
            + " AND EXPIRATION_DATE             >= :NEW_EFFECTIVE_DATE"
            + " AND EFFECTIVE_DATE              <= :NEW_EXPIRATION_DATE"
            + " AND EFFECTIVE_DATE <= EXPIRATION_DATE";

    private static final String UPDATE_PRC_PROF_LESSCASE_RULE = "UPDATE PRC_PROF_LESSCASE_RULE" 
            + " SET CW_MARKUP_AMT              = :CW_MARKUP_AMT,"
            + " CW_MARKUP_AMOUNT_TYPE_CODE     = :CW_MARKUP_AMOUNT_TYPE_CODE,"
            + " NON_CW_MARKUP_AMT              = :NON_CW_MARKUP_AMT,"
            + " NON_CW_MARKUP_AMOUNT_TYPE_CODE = :NON_CW_MARKUP_AMOUNT_TYPE_CODE, "
            + " LESSCASE_PRICE_RULE_ID         = :LESSCASE_PRICE_RULE_ID, "
            + " LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + " LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ"
            + " AND ITEM_PRICE_ID              = :ITEM_PRICE_ID "
            + " AND GFS_CUSTOMER_ID            = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE     = :GFS_CUSTOMER_TYPE_CODE"
            + " AND EXPIRATION_DATE            >= SYSDATE";
    
    private static final String EXPIRE_PRC_PROF_LESSCASE_RULE_FOR_FURTHERANCE = "UPDATE "
            + " PRC_PROF_LESSCASE_RULE " 
            + " SET EXPIRATION_DATE = :EXPIRATION_DATE, " 
            + " LAST_UPDATE_TMSTMP   = SYSTIMESTAMP, " 
            + " LAST_UPDATE_USER_ID  = :LAST_UPDATE_USER_ID " 
            + " WHERE "
            + " ITEM_PRICE_ID =:ITEM_PRICE_ID AND"
            + " ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE AND"
            + " GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID AND"
            + " GFS_CUSTOMER_TYPE_CODE=:GFS_CUSTOMER_TYPE_CODE AND"
            + " CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ";
    
    // @formatter:on

    public void savePrcProfLessCaseRuleForCustomer(List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList) {
        savePrcProfLessCaseRule(prcProfLessCaseRuleDOList, null);
    }

    public void savePrcProfLessCaseRuleForFurtheranceUpdates(List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList, String userName) {
        savePrcProfLessCaseRule(prcProfLessCaseRuleDOList, userName);
    }

    private void savePrcProfLessCaseRule(List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList, String userName) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfLessCaseRuleDO prcProfLessCaseRuleDO : prcProfLessCaseRuleDOList) {
            MapSqlParameterSource paramMap = prcProfLessCaseRuleParamListBuilder.createParamMap(prcProfLessCaseRuleDO);
            if (userName != null) {
                paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
                paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
            }
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_LESSCASE_RULE, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public List<PrcProfLessCaseRuleDO> fetchPrcProfLessCaseForRealCustomersForFurtherance(int cppFurtheranceSeq, Date effectiveDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_FURTHERANCE_SEQ, cppFurtheranceSeq, Types.NUMERIC);
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        return cppJdbcTemplate.query(FETCH_FURTHERANCE_UPDATES_FOR_REAL_CUSTOMER, paramMap, new PrcProfLessCaseRuleRowMapper());
    }

    public void expirePrcProfLessCaseRuleForFurtherance(List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList,
            Date expirationDateToSetForExistingPricing, String userName) {

        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfLessCaseRuleDO prcProfLessCaseRuleDO : prcProfLessCaseRuleDOList) {
            MapSqlParameterSource paramMap = prcProfLessCaseRuleParamListBuilder.createParamMap(prcProfLessCaseRuleDO);
            paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
            paramMap.addValue(EXPIRATION_DATE, expirationDateToSetForExistingPricing, Types.DATE);
            paramList.add(paramMap);
        }

        if (!CollectionUtils.isEmpty(paramList)) {
            cppJdbcTemplate.batchUpdate(EXPIRE_PRC_PROF_LESSCASE_RULE_FOR_FURTHERANCE, paramList.toArray(new SqlParameterSource[paramList.size()]));
        }
    }

    public List<PrcProfLessCaseRuleDO> fetchPrcProfLessCaseRuleForCPPSeq(int contractPriceProfileSeq, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, cmgCustomerResponseDTO.getId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, cmgCustomerResponseDTO.getTypeCode(), Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_LESSCASE_RULE_FOR_CPP_SEQ, paramMap, new PrcProfLessCaseRuleRowMapper());
    }

    public List<SplitCaseDO> fetchSplitCaseGridForCMG(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_LESSCASE_RULE_FOR_ALL_CMG_BY_CPP_SEQ, paramMap, new SplitCaseGridDORowMapper());
    }

    public void saveSplitCase(List<SplitCaseDO> splitCaseList, String gfsCustomerId, String userName) {
        List<SqlParameterSource> paramList = generateInsertParam(splitCaseList, gfsCustomerId, userName);
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_LESSCASE_RULE, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    private List<SqlParameterSource> generateInsertParam(List<SplitCaseDO> splitCaseList, String gfsCustomerId, String userName) {
        MapSqlParameterSource paramMap = null;
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (SplitCaseDO splitCaseDO : splitCaseList) {
            paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, splitCaseDO.getContractPriceProfileSeq(), Types.NUMERIC);
            paramMap.addValue(ITEM_PRICE_ID, splitCaseDO.getItemPriceId(), Types.VARCHAR);
            paramMap.addValue(EFFECTIVE_DATE, splitCaseDO.getEffectiveDate(), Types.DATE);
            paramMap.addValue(EXPIRATION_DATE, splitCaseDO.getExpirationDate(), Types.DATE);
            paramMap.addValue(CW_MARKUP_AMOUNT_TYPE_CODE, splitCaseDO.getUnit(), Types.VARCHAR);
            paramMap.addValue(NON_CW_MARKUP_AMOUNT_TYPE_CODE, splitCaseDO.getUnit(), Types.VARCHAR);
            paramMap.addValue(CW_MARKUP_AMT, splitCaseDO.getSplitCaseFee(), Types.NUMERIC);
            paramMap.addValue(NON_CW_MARKUP_AMT, splitCaseDO.getSplitCaseFee(), Types.NUMERIC);
            paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.NUMERIC);
            paramMap.addValue(GFS_CUSTOMER_TYPE_CODE,
                    (splitCaseDO.getGfsCustomerTypeCode() > 0) ? splitCaseDO.getGfsCustomerTypeCode() : CPPConstants.CMG_CUSTOMER_TYPE_CODE,
                    Types.NUMERIC);
            paramMap.addValue(ITEM_PRICE_LEVEL_CODE, CPPConstants.SPLIT_CASE_ITEM_PRICE_LEVEL_CODE, Types.NUMERIC);
            paramMap.addValue(LESSCASE_PRICE_RULE_ID, splitCaseDO.getLessCaseRuleId(), Types.NUMERIC);
            paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
            paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
            paramList.add(paramMap);
        }
        return paramList;
    }

    public void expireNonCmgLessCaseRuleForContract(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);

        cppJdbcTemplate.update(EXPIRE_PRC_PROF_LESSCASE_RULE_FOR_NON_CMG_BY_CPP_SEQ, paramMap);
    }

    public void expireLessCaseRuleForRealCust(Date expirationDate, String updatedUserId, String customerId, int customerTypeCode,
            Date newPricingEffectiveDate, Date newPricingExpiryDate) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, customerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerTypeCode, Types.INTEGER);
        paramMap.addValue(NEW_EFFECTIVE_DATE, newPricingEffectiveDate, Types.DATE);
        paramMap.addValue(NEW_EXPIRATION_DATE, newPricingExpiryDate, Types.DATE);
        cppJdbcTemplate.update(EXPIRE_PRC_PROF_LESSCASE_RULE_FOR_REAL_CUST, paramMap);
    }

    public void updateSplitCase(Collection<SplitCaseDO> updatedSplitcaseList, String userName, String gfsCustomerId, int gfsCustomerTypeCode) {
        List<SqlParameterSource> paramList = createParamList(updatedSplitcaseList, userName, gfsCustomerId, gfsCustomerTypeCode);
        if (!CollectionUtils.isEmpty(paramList)) {
            cppJdbcTemplate.batchUpdate(UPDATE_PRC_PROF_LESSCASE_RULE, paramList.toArray(new SqlParameterSource[paramList.size()]));
        }

    }

    public void expirePrcProfLessCaseRuleForFurtherance(List<SplitCaseDO> updatedSplitcaseList, String userName, String gfsCustomerId,
            int gfsCustomerTypeCode, Date expirationDate) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (SplitCaseDO splitCaseDO : updatedSplitcaseList) {
            MapSqlParameterSource paramMap = createParamMap(userName, gfsCustomerId, gfsCustomerTypeCode, splitCaseDO);
            paramMap.addValue(ITEM_PRICE_LEVEL_CODE, ItemPriceLevel.PRODUCT_TYPE.getCode(), Types.INTEGER);
            paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
            paramList.add(paramMap);
        }

        if (!CollectionUtils.isEmpty(paramList)) {
            cppJdbcTemplate.batchUpdate(EXPIRE_PRC_PROF_LESSCASE_RULE_FOR_FURTHERANCE, paramList.toArray(new SqlParameterSource[paramList.size()]));
        }

    }

    private List<SqlParameterSource> createParamList(Collection<SplitCaseDO> updatedSplitcaseList, String userName, String gfsCustomerId,
            int gfsCustomerTypeCode) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (SplitCaseDO splitCaseDO : updatedSplitcaseList) {
            MapSqlParameterSource paramMap = createParamMap(userName, gfsCustomerId, gfsCustomerTypeCode, splitCaseDO);
            paramList.add(paramMap);
        }
        return paramList;
    }

    private MapSqlParameterSource createParamMap(String userName, String gfsCustomerId, int gfsCustomerTypeCode, SplitCaseDO splitCaseDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(ITEM_PRICE_ID, splitCaseDO.getItemPriceId(), Types.VARCHAR);
        paramMap.addValue(CW_MARKUP_AMOUNT_TYPE_CODE, splitCaseDO.getUnit(), Types.VARCHAR);
        paramMap.addValue(NON_CW_MARKUP_AMOUNT_TYPE_CODE, splitCaseDO.getUnit(), Types.VARCHAR);
        paramMap.addValue(CW_MARKUP_AMT, splitCaseDO.getSplitCaseFee(), Types.NUMERIC);
        paramMap.addValue(NON_CW_MARKUP_AMT, splitCaseDO.getSplitCaseFee(), Types.NUMERIC);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(LESSCASE_PRICE_RULE_ID, splitCaseDO.getLessCaseRuleId(), Types.NUMERIC);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, splitCaseDO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        return paramMap;
    }
}
