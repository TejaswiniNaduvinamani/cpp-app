package com.gfs.cpp.data.splitcase;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;

public class PrcProfLessCaseRuleRowMapper implements RowMapper<PrcProfLessCaseRuleDO> {

    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String NON_CW_MARKUP_AMT = "NON_CW_MARKUP_AMT";
    private static final String NON_CW_MARKUP_AMOUNT_TYPE_CODE = "NON_CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String LESSCASE_PRICE_RULE_ID = "LESSCASE_PRICE_RULE_ID";
    private static final String CW_MARKUP_AMT = "CW_MARKUP_AMT";
    private static final String CW_MARKUP_AMOUNT_TYPE_CODE = "CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_APPLIED_BEFORE_DIV_IND = "MARKUP_APPLIED_BEFORE_DIV_IND";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE"; 

 // @formatter:off 
    public static final String FETCH_PRC_PROF_LESSCASE_RULE_FOR_CPP_SEQ = "SELECT ITEM_PRICE_LEVEL_CODE, "
            + "ITEM_PRICE_ID, "
            + "LESSCASE_PRICE_RULE_ID, "
            + "CW_MARKUP_AMT, "
            + "CW_MARKUP_AMOUNT_TYPE_CODE, "
            + "NON_CW_MARKUP_AMT, "
            + "NON_CW_MARKUP_AMOUNT_TYPE_CODE, "
            + "MARKUP_APPLIED_BEFORE_DIV_IND, "
            + "CONTRACT_PRICE_PROFILE_SEQ, "
            + "GFS_CUSTOMER_ID,  "
            + "GFS_CUSTOMER_TYPE_CODE,"
            + "EFFECTIVE_DATE,"
            + "EXPIRATION_DATE "
            + "FROM PRC_PROF_LESSCASE_RULE "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID "
            + "AND GFS_CUSTOMER_TYPE_CODE=:GFS_CUSTOMER_TYPE_CODE "
            + "AND EXPIRATION_DATE >= SYSDATE"; 
    
    public static final String FETCH_FURTHERANCE_UPDATES_FOR_REAL_CUSTOMER="SELECT "
            + " MAP.GFS_CUSTOMER_TYPE_CODE , "
            + " MAP.GFS_CUSTOMER_ID, "
            + " PPLR.ITEM_PRICE_ID, "
            + " PPLR.CONTRACT_PRICE_PROFILE_SEQ, " 
            + " PPLR.ITEM_PRICE_LEVEL_CODE, "
            + " PPLR.LESSCASE_PRICE_RULE_ID, "
            + " PPLR.CW_MARKUP_AMT, "
            + " PPLR.CW_MARKUP_AMOUNT_TYPE_CODE, "
            + " PPLR.NON_CW_MARKUP_AMT, "
            + " PPLR.NON_CW_MARKUP_AMOUNT_TYPE_CODE, "
            + " PPLR.MARKUP_APPLIED_BEFORE_DIV_IND, "
            + " PPLR.EXPIRATION_DATE, " 
            + " :EFFECTIVE_DATE AS EFFECTIVE_DATE"
        + " FROM  "
            + " CPP_CONCEPT_MAPPING MAP "
            + " INNER JOIN CONTRACT_PRICE_PROF_CUSTOMER CPPC "
                + "ON CPPC.CPP_CUSTOMER_SEQ = MAP.CPP_CUSTOMER_SEQ "
            + " INNER JOIN CPP_FURTHERANCE_TRACKING CFT "
                + "ON CFT.GFS_CUSTOMER_ID = CPPC.GFS_CUSTOMER_ID AND "
                + " CFT.GFS_CUSTOMER_TYPE_CODE = CPPC.GFS_CUSTOMER_TYPE_CODE "
            + " INNER JOIN CPP_FURTHERANCE CF "
                + "ON CF.CPP_FURTHERANCE_SEQ = CFT.CPP_FURTHERANCE_SEQ AND " 
                + " CPPC.CONTRACT_PRICE_PROFILE_SEQ=CF.CONTRACT_PRICE_PROFILE_SEQ "
            + " INNER JOIN PRC_PROF_LESSCASE_RULE PPLR "
                + "ON PPLR.CONTRACT_PRICE_PROFILE_SEQ = CPPC.CONTRACT_PRICE_PROFILE_SEQ  "
                + " AND PPLR.GFS_CUSTOMER_ID = CPPC.GFS_CUSTOMER_ID "
                + " AND PPLR.GFS_CUSTOMER_TYPE_CODE = CPPC.GFS_CUSTOMER_TYPE_CODE "
                + " AND PPLR.ITEM_PRICE_ID = CFT.ITEM_PRICE_ID " 
                + " AND PPLR.ITEM_PRICE_LEVEL_CODE =  CFT.ITEM_PRICE_LEVEL_CODE "
        + " WHERE CF.CPP_FURTHERANCE_SEQ =:CPP_FURTHERANCE_SEQ "
            + " AND CF.FURTHERANCE_STATUS_CODE="+FurtheranceStatus.FURTHERANCE_SAVED.getCode()
            + " AND CFT.CHANGE_TABLE_NAME='"+CPPConstants.SPLIT_CASE_TABLE_NAMES+"'"
            + " AND PPLR.EXPIRATION_DATE>=SYSDATE ";
    
// @formatter:on 

    @Override
    public PrcProfLessCaseRuleDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = new PrcProfLessCaseRuleDO();
        prcProfLessCaseRuleDO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        prcProfLessCaseRuleDO.setCwMarkupAmnt(rs.getInt(CW_MARKUP_AMT));
        prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode(rs.getString(CW_MARKUP_AMOUNT_TYPE_CODE));
        prcProfLessCaseRuleDO.setLesscaseRuleId(rs.getInt(LESSCASE_PRICE_RULE_ID));
        prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(rs.getInt(MARKUP_APPLIED_BEFORE_DIV_IND));
        prcProfLessCaseRuleDO.setNonCwMarkupAmnt(rs.getInt(NON_CW_MARKUP_AMT));
        prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode(rs.getString(NON_CW_MARKUP_AMOUNT_TYPE_CODE));
        prcProfLessCaseRuleDO.setItemPriceId(rs.getString(ITEM_PRICE_ID));
        prcProfLessCaseRuleDO.setItemPriceLevelCode(rs.getInt(ITEM_PRICE_LEVEL_CODE));
        prcProfLessCaseRuleDO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        prcProfLessCaseRuleDO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        prcProfLessCaseRuleDO.setEffectiveDate(rs.getDate(EFFECTIVE_DATE));
        prcProfLessCaseRuleDO.setExpirationDate(rs.getDate(EXPIRATION_DATE));
        return prcProfLessCaseRuleDO;
    }

}
