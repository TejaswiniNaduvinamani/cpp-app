package com.gfs.cpp.acceptanceTests.stepdefs.ActivatePricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.sql.Types;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;

@Component(value = "validateCustomerPricing")
public class ValidateCustomerPricing {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";

    // @formatter:off
    
    private static final String ACTIVE_CUSTOMER_ITEM_PRICING_COUNT="SELECT COUNT(CONTRACT_PRICE_PROFILE_SEQ) FROM CUSTOMER_ITEM_PRICE WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ and GFS_CUSTOMER_TYPE_CODE<> "+CukesConstants.CMG_CUSTOMER_TYPE_ID+" and EXPIRATION_DATE >= sysdate";
    private static final String ACTIVE_PRC_PROF_AUDIT_AUTHORITY_COUNT="SELECT COUNT(CONTRACT_PRICE_PROFILE_SEQ) FROM PRC_PROF_AUDIT_AUTHORITY WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ and GFS_CUSTOMER_TYPE_CODE<> "+CukesConstants.CMG_CUSTOMER_TYPE_ID+"  and EXPIRATION_DATE >= sysdate";
    private static final String ACTIVE_PRC_PROF_LESSCASE_RULE_COUNT="SELECT COUNT(CONTRACT_PRICE_PROFILE_SEQ) FROM PRC_PROF_LESSCASE_RULE WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ and GFS_CUSTOMER_TYPE_CODE<> "+CukesConstants.CMG_CUSTOMER_TYPE_ID+"  and EXPIRATION_DATE >= sysdate";
    private static final String ACTIVE_PRC_PROF_PRICING_RULE_OVRD_COUNT="SELECT COUNT(CONTRACT_PRICE_PROFILE_SEQ) FROM PRC_PROF_PRICING_RULE_OVRD WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ and GFS_CUSTOMER_TYPE_CODE<> "+CukesConstants.CMG_CUSTOMER_TYPE_ID+"  and EXPIRATION_DATE >= sysdate";
    private static final String ACTIVE_PRC_PROF_COST_SCHEDULE_PKG_COUNT="SELECT COUNT(CONTRACT_PRICE_PROFILE_SEQ) FROM PRC_PROF_COST_SCHEDULE_PKG WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ and GFS_CUSTOMER_TYPE_CODE<> "+CukesConstants.CMG_CUSTOMER_TYPE_ID+" and EXPIRATION_DATE >= sysdate";
    private static final String ACTIVE_PRC_PROF_NON_BRKT_CST_MDL_COUNT="SELECT COUNT(CONTRACT_PRICE_PROFILE_SEQ) FROM PRC_PROF_NON_BRKT_CST_MDL WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ and GFS_CUSTOMER_TYPE_CODE<> "+CukesConstants.CMG_CUSTOMER_TYPE_ID+"  and EXPIRATION_DATE >= sysdate";
    
    private static final String EXPIRED_CUSTOMER_ITEM_PRICING_COUNT="SELECT COUNT(EXPIRATION_DATE) FROM CUSTOMER_ITEM_PRICE WHERE  EXPIRATION_DATE =:EXPIRATION_DATE and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String EXPIRED_CUSTOMER_PRICING_AUDIT_AUTH_COUNT="SELECT COUNT(EXPIRATION_DATE) FROM PRC_PROF_AUDIT_AUTHORITY WHERE  EXPIRATION_DATE =:EXPIRATION_DATE and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String EXPIRED_CUSTOMER_PRICING_LESSCASE_RULE_COUNT="SELECT COUNT(EXPIRATION_DATE) FROM PRC_PROF_LESSCASE_RULE WHERE  EXPIRATION_DATE =:EXPIRATION_DATE and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String EXPIRED_CUSTOMER_PRICING_RULE_OVRD_COUNT="SELECT COUNT(EXPIRATION_DATE) FROM PRC_PROF_PRICING_RULE_OVRD WHERE  EXPIRATION_DATE =:EXPIRATION_DATE and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String EXPIRED_CUSTOMER_PRICING_COST_SCHEDULE_PKG_COUNT="SELECT COUNT(EXPIRATION_DATE) FROM PRC_PROF_COST_SCHEDULE_PKG WHERE  EXPIRATION_DATE =:EXPIRATION_DATE and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String EXPIRED_CUSTOMER_PRICING_COST_MODEL_COUNT="SELECT COUNT(EXPIRATION_DATE) FROM PRC_PROF_NON_BRKT_CST_MDL WHERE  EXPIRATION_DATE =:EXPIRATION_DATE and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
       
    private static final String CUSTOMER_ITEM_PRICING_COUNT="SELECT COUNT(EFFECTIVE_DATE) FROM CUSTOMER_ITEM_PRICE WHERE EFFECTIVE_DATE =:EFFECTIVE_DATE and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID AND ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE";
    private static final String CUSTOMER_PRICING_AUDIT_AUTH_COUNT="SELECT COUNT(EFFECTIVE_DATE) FROM PRC_PROF_AUDIT_AUTHORITY WHERE  EFFECTIVE_DATE =:EFFECTIVE_DATE  and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String CUSTOMER_PRICING_LESSCASE_RULE_COUNT="SELECT COUNT(EFFECTIVE_DATE) FROM PRC_PROF_LESSCASE_RULE WHERE  EFFECTIVE_DATE =:EFFECTIVE_DATE  and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String CUSTOMER_PRICING_RULE_OVRD_COUNT="SELECT COUNT(EFFECTIVE_DATE) FROM PRC_PROF_PRICING_RULE_OVRD WHERE  EFFECTIVE_DATE =:EFFECTIVE_DATE  and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String CUSTOMER_PRICING_COST_SCHEDULE_PKG_COUNT="SELECT COUNT(EFFECTIVE_DATE) FROM PRC_PROF_COST_SCHEDULE_PKG WHERE  EFFECTIVE_DATE =:EFFECTIVE_DATE  and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    private static final String CUSTOMER_PRICING_COST_MODEL_COUNT="SELECT COUNT(distinct(EFFECTIVE_DATE)) FROM PRC_PROF_NON_BRKT_CST_MDL WHERE  EFFECTIVE_DATE =:EFFECTIVE_DATE  and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID";
    
    // @formatter:on

    public void validatePricingEntriesForPricingActivateContract(AcceptableRealCustomer realCustomer, Date expirationDate) {
        validatePricingEntriesForContract(realCustomer, expirationDate);
        validateCustPricingCostModelForPricingActivateContract(realCustomer, expirationDate);
    }

    private void validatePricingEntriesForContract(AcceptableRealCustomer realCustomer, Date expirationDate) {
        validateCustPricingInPrcProfAuditAuthorityForExistingContract(realCustomer, expirationDate);
        validateCustPricingInCostPackageForExistingContract(realCustomer, expirationDate);
        validateCustPricingRuleOverrideForExistingContract(realCustomer, expirationDate);
        validateCustPricingLessCaseRuleForExistingContract(realCustomer, expirationDate);
    }

    public void validatePricingEntriesForExistingContract(AcceptableRealCustomer realCustomer, Date expirationDate) {
        validatePricingEntriesForContract(realCustomer, expirationDate);
        validateCustPricingCostModelForExistingContract(realCustomer, expirationDate);
    }

    public Integer fetchExpiredCustomerPricingLesscaseRuleCount(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_PRICING_LESSCASE_RULE_COUNT, paramMap, Integer.class);
    }

    public Integer fetchExpiredCustomerPricingRuleOvrdCount(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_PRICING_RULE_OVRD_COUNT, paramMap, Integer.class);
    }

    public Integer fetchExpiredCustomerPricingCostSchedulePkgCount(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_PRICING_COST_SCHEDULE_PKG_COUNT, paramMap, Integer.class);
    }

    public Integer fetchExpiredCustomerPricingCostModelCount(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_PRICING_COST_MODEL_COUNT, paramMap, Integer.class);
    }

    public Integer fetchExpiredCustomerPricingAuditAuthorityCount(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_PRICING_AUDIT_AUTH_COUNT, paramMap, Integer.class);
    }

    public Integer fetchCustomerItemPricingCountForItemMarkup(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate, int itemId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
            paramMap.addValue(ITEM_PRICE_ID, itemId, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_ITEM_PRICING_COUNT+" AND ITEM_PRICE_ID=:ITEM_PRICE_ID", paramMap, Integer.class);
            
    }
    
    public Integer fetchExpiredCustomerItemPricingCount(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_ITEM_PRICING_COUNT, paramMap, Integer.class);
    }

    public Integer fetchActiveCustomerItemPricingCount(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(ACTIVE_CUSTOMER_ITEM_PRICING_COUNT, paramMap, Integer.class);
    }

    public Integer fetchActiveAuditAuthorityCountForContract(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(ACTIVE_PRC_PROF_AUDIT_AUTHORITY_COUNT, paramMap, Integer.class);
    }

    public Integer fetchActiveCostScheduleCountForContract(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(ACTIVE_PRC_PROF_COST_SCHEDULE_PKG_COUNT, paramMap, Integer.class);
    }

    public Integer fetchActiveLessCaseRuleCountForContract(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(ACTIVE_PRC_PROF_LESSCASE_RULE_COUNT, paramMap, Integer.class);
    }

    public Integer fetchActiveNonBrktCstModelCountForContract(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(ACTIVE_PRC_PROF_NON_BRKT_CST_MDL_COUNT, paramMap, Integer.class);
    }

    public Integer fetchActivePriceRuleOvrdCountForContract(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(ACTIVE_PRC_PROF_PRICING_RULE_OVRD_COUNT, paramMap, Integer.class);
    }

    public void validateCustPricingInPrcProfAuditAuthorityForExistingContract(AcceptableRealCustomer realCustomer,
            Date existingCustomerPricingExpirationDate) {

        int result = fetchExpiredCustomerPricingAuditAuthorityCount(realCustomer, existingCustomerPricingExpirationDate);
        assertThat(result, is(1));
    }

    public void validateCustPricingInCostPackageForExistingContract(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        int result = fetchExpiredCustomerPricingCostSchedulePkgCount(realCustomer, existingCustomerPricingExpirationDate);
        assertThat(result, is(1));
    }

    public void validateCustPricingCostModelForExistingContract(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        int result = fetchExpiredCustomerPricingCostModelCount(realCustomer, existingCustomerPricingExpirationDate);
        assertThat(result, is(1));
    }

    public void validateCustPricingCostModelForPricingActivateContract(AcceptableRealCustomer realCustomer,
            Date existingCustomerPricingExpirationDate) {
        int result = fetchExpiredCustomerPricingCostModelCount(realCustomer, existingCustomerPricingExpirationDate);
        assertThat(result, is(6));
    }

    public void validateCustPricingRuleOverrideForExistingContract(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        int result = fetchExpiredCustomerPricingRuleOvrdCount(realCustomer, existingCustomerPricingExpirationDate);
        assertThat(result, is(1));
    }

    public void validateCustPricingLessCaseRuleForExistingContract(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        int result = fetchExpiredCustomerPricingLesscaseRuleCount(realCustomer, existingCustomerPricingExpirationDate);
        assertThat(result, is(1));
    }

    public void validateCIPEntriesForExistingContract(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate) {
        int result = fetchExpiredCustomerItemPricingCount(realCustomer, existingCustomerPricingExpirationDate);
        assertTrue(result>0);
    }
    
    public void validateCIPEntriesForItemMarkupForExistingContract(AcceptableRealCustomer realCustomer, Date existingCustomerPricingExpirationDate, int itemId) {
        int result = fetchCustomerItemPricingCountForItemMarkup(realCustomer, existingCustomerPricingExpirationDate,itemId);
        assertThat(result, is(1));
    }

    public void validatePricingEntries(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {

        verifyPricingEntryInAuditAuthority(realCustomer, customerPricingEffectiveDate);
        verifyPricingEntryInCostPackage(realCustomer, customerPricingEffectiveDate);
        verifyCostModel(realCustomer, customerPricingEffectiveDate);
        verifyPricingRuleOverride(realCustomer, customerPricingEffectiveDate);
        verifyLessCaseRule(realCustomer, customerPricingEffectiveDate);
    }

    public void verifyPricingEntryInAuditAuthority(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {

        int result = fetchCustomerPricingAuditAuthorityCount(realCustomer, customerPricingEffectiveDate);
        assertThat(result, is(1));
    }

    public void verifyPricingEntryInCostPackage(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        int result = fetchCustomerPricingCostSchedulePkgCount(realCustomer, customerPricingEffectiveDate);
        assertThat(result, is(1));
    }

    public void verifyCostModel(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        int result = fetchCustomerPricingCostModelCount(realCustomer, customerPricingEffectiveDate);
        assertThat(result, is(1));
    }

    public void verifyPricingRuleOverride(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        int result = fetchCustomerPricingRuleOvrdCount(realCustomer, customerPricingEffectiveDate);
        assertThat(result, is(1));
    }

    public void verifyLessCaseRule(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        int result = fetchCustomerPricingLesscaseRuleCount(realCustomer, customerPricingEffectiveDate);
        assertThat(result, is(1));
    }

    public void verifyCIPEntriesForProductTypeMarkup(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        int result = fetchCustomerItemPricingCount(realCustomer.getCustomerId(), customerPricingEffectiveDate, ItemPriceLevel.PRODUCT_TYPE.getCode(), -1);
        assertThat(result, is(1));
    }

    public void verifyCIPEntriesNotSaved(Date customerPricingEffectiveDate, int itemId) {
        int result = fetchCustomerItemPricingCount(CukesConstants.REAL_CUSTOMER_ID, customerPricingEffectiveDate, ItemPriceLevel.ITEM.getCode(), itemId);
        assertThat(result, is(0));
    }

    public void verifyCIPEntriesSaved(Date customerPricingEffectiveDate, int itemId) {
        int result = fetchCustomerItemPricingCount(CukesConstants.REAL_CUSTOMER_ID, customerPricingEffectiveDate, ItemPriceLevel.ITEM.getCode(), itemId);
        assertThat(result, is(1));
    }

    public Integer fetchCustomerPricingLesscaseRuleCount(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, customerPricingEffectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(CUSTOMER_PRICING_LESSCASE_RULE_COUNT, paramMap, Integer.class);
    }

    public Integer fetchCustomerPricingRuleOvrdCount(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, customerPricingEffectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(CUSTOMER_PRICING_RULE_OVRD_COUNT, paramMap, Integer.class);
    }

    public Integer fetchCustomerPricingCostSchedulePkgCount(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, customerPricingEffectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(CUSTOMER_PRICING_COST_SCHEDULE_PKG_COUNT, paramMap, Integer.class);
    }

    public Integer fetchCustomerPricingCostModelCount(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, customerPricingEffectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(CUSTOMER_PRICING_COST_MODEL_COUNT, paramMap, Integer.class);
    }

    public Integer fetchCustomerPricingAuditAuthorityCount(AcceptableRealCustomer realCustomer, Date customerPricingEffectiveDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, customerPricingEffectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(CUSTOMER_PRICING_AUDIT_AUTH_COUNT, paramMap, Integer.class);
    }

    public Integer fetchCustomerItemPricingCount(String customerId, Date customerPricingEffectiveDate, int itemLevelPriceCode, int itemId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, customerPricingEffectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, customerId, Types.VARCHAR);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, itemLevelPriceCode, Types.VARCHAR);
        if(itemId!=-1) {
            paramMap.addValue(ITEM_PRICE_ID, itemId, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(CUSTOMER_ITEM_PRICING_COUNT+" AND ITEM_PRICE_ID=:ITEM_PRICE_ID", paramMap, Integer.class);
            
        }else {
            return cppJdbcTemplate.queryForObject(CUSTOMER_ITEM_PRICING_COUNT, paramMap, Integer.class);
        }
        
    }

}
