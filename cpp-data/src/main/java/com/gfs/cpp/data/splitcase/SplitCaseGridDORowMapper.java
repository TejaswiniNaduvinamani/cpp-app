package com.gfs.cpp.data.splitcase;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.model.splitcase.SplitCaseDO;

public class SplitCaseGridDORowMapper implements RowMapper<SplitCaseDO> {

    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String NON_CW_MARKUP_AMT = "NON_CW_MARKUP_AMT";
    private static final String NON_CW_MARKUP_AMOUNT_TYPE_CODE = "NON_CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String LESSCASE_PRICE_RULE_ID = "LESSCASE_PRICE_RULE_ID";

 // @formatter:off 
    public static final String FETCH_PRC_PROF_LESSCASE_RULE_FOR_ALL_CMG_BY_CPP_SEQ = "select ITEM_PRICE_ID, "
            + "LESSCASE_PRICE_RULE_ID, "
            + "EFFECTIVE_DATE,"
            + "EXPIRATION_DATE,"
            + "NON_CW_MARKUP_AMT,"
            + "NON_CW_MARKUP_AMOUNT_TYPE_CODE "
            + "from PRC_PROF_LESSCASE_RULE "
            + "inner join CONTRACT_PRICE_PROF_CUSTOMER "
            + "on PRC_PROF_LESSCASE_RULE.CONTRACT_PRICE_PROFILE_SEQ = CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ "
            + "and PRC_PROF_LESSCASE_RULE.GFS_CUSTOMER_ID = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID "
            + "and PRC_PROF_LESSCASE_RULE.GFS_CUSTOMER_TYPE_CODE = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE "
            + "where PRC_PROF_LESSCASE_RULE.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "and PRC_PROF_LESSCASE_RULE.EXPIRATION_DATE >= SYSDATE ";
    
    public static final String FETCH_PRC_PROF_LESSCASE_RULE_FOR_REAL_CUSTOMER = "select prcProfLessCase.ITEM_PRICE_ID, "
            + "prcProfLessCase.LESSCASE_PRICE_RULE_ID, " 
            + "prcProfLessCase.EFFECTIVE_DATE, " 
            + "prcProfLessCase.EXPIRATION_DATE, " 
            + "prcProfLessCase.NON_CW_MARKUP_AMT, "  
            + "prcProfLessCase.NON_CW_MARKUP_AMOUNT_TYPE_CODE  "  
            + "from PRC_PROF_LESSCASE_RULE prcProfLessCase INNER JOIN CONTRACT_PRICE_PROF_CUSTOMER contractProcProfileCust " 
            + "ON prcProfLessCase.CONTRACT_PRICE_PROFILE_SEQ=contractProcProfileCust.CONTRACT_PRICE_PROFILE_SEQ  "
            + "AND prcProfLessCase.GFS_CUSTOMER_TYPE_CODE=contractProcProfileCust.GFS_CUSTOMER_TYPE_CODE " 
            + "AND prcProfLessCase.GFS_CUSTOMER_ID=contractProcProfileCust.GFS_CUSTOMER_ID " 
            + "where prcProfLessCase.CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ  " 
            + "and  contractProcProfileCust.DEFAULT_CUSTOMER_IND=:DEFAULT_CUSTOMER_IND";
 // @formatter:on 

    @Override
    public SplitCaseDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setItemPriceId(rs.getString(ITEM_PRICE_ID));
        splitCaseDO.setEffectiveDate(rs.getDate(EFFECTIVE_DATE));
        splitCaseDO.setExpirationDate(rs.getDate(EXPIRATION_DATE));
        splitCaseDO.setSplitCaseFee(rs.getDouble(NON_CW_MARKUP_AMT));
        splitCaseDO.setUnit(rs.getString(NON_CW_MARKUP_AMOUNT_TYPE_CODE));
        splitCaseDO.setLessCaseRuleId(rs.getInt(LESSCASE_PRICE_RULE_ID));
        return splitCaseDO;
    }
}
