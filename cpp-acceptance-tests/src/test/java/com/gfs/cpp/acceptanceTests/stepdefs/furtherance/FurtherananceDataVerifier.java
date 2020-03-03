package com.gfs.cpp.acceptanceTests.stepdefs.furtherance;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableItems;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.common.data.FurtheranceAction;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.data.furtherance.CppFurtheranceTrackingRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class FurtherananceDataVerifier {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CppFurtheranceTrackingRepository cppFurtheranceTrackingRepository;

    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String FURTHERANCE_STATUS_CODE = "FURTHERANCE_STATUS_CODE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";

    // @formatter:off
    private static final String UPDATE_CPP_FURTHERANCE_STATUS = "UPDATE CPP_FURTHERANCE "
            + " SET FURTHERANCE_STATUS_CODE=:FURTHERANCE_STATUS_CODE "
            + " WHERE CPP_FURTHERANCE_SEQ=:CPP_FURTHERANCE_SEQ ";
   
    private static final String EXPIRED_CUSTOMER_ITEM_PRICING_COUNT="SELECT ITEM_PRICE_ID FROM CUSTOMER_ITEM_PRICE WHERE  "
            + " EXPIRATION_DATE =:EXPIRATION_DATE "
            + " and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID"
            + " AND ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE";
    

    
    private static final String SPLIT_CASE_FEE_PRODUCT_TYPE_BY_EXPIRATION_DATE="SELECT ITEM_PRICE_ID FROM PRC_PROF_LESSCASE_RULE WHERE "
            + " EXPIRATION_DATE =:EXPIRATION_DATE "
            + " AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID"
            + " AND ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE";
    
    private static final String SPLIT_CASE_FEE_PRODUCT_TYPE_BY_EFFECTIVE_DATE="SELECT ITEM_PRICE_ID FROM PRC_PROF_LESSCASE_RULE WHERE "
            + " EFFECTIVE_DATE =:EFFECTIVE_DATE "
            + " AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID"
            + " AND ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE";
    
    private static final String SUBGROUP_ITEM_ID_BY_EXPIRATION_DATE="SELECT ITEM_PRICE_ID FROM CUSTOMER_ITEM_PRICE WHERE "
            + " EXPIRATION_DATE =:EXPIRATION_DATE "
            + " AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID"
            + " AND ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE";
    
    private static final String CIP_ENRTY_BY_EFFECTIVE_DATE="SELECT ITEM_PRICE_ID FROM CUSTOMER_ITEM_PRICE WHERE "
            + " EFFECTIVE_DATE =:EFFECTIVE_DATE "
            + " AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID"
            + " AND ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE";
    
    
    
    // @formatter:on

    public void addItemLevelToMarkup(Date effectiveDate, Date expirationDate, AcceptableItems acceptableItem) {
        addItemToMarkup(effectiveDate, expirationDate, acceptableItem);
        addItemLevelEntryToCPPFurtheranceTracking(acceptableItem, FurtheranceAction.ADDED.getFurtheranceActionCode());
    }

    public String validateSplitCaseFeeIsExpiredForRealCustomer(Date expirationDate) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_ID, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, CukesConstants.PRODUCT_LEVEL_CODE, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(SPLIT_CASE_FEE_PRODUCT_TYPE_BY_EXPIRATION_DATE, paramMap, String.class);
    }

    public String validateSplitCaseFeeIsCreatedForRealCustomer(Date effectiveDate) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_ID, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, CukesConstants.PRODUCT_LEVEL_CODE, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(SPLIT_CASE_FEE_PRODUCT_TYPE_BY_EFFECTIVE_DATE, paramMap, String.class);
    }

    public String validateSubgroupIsExpiredForRealCustomer(Date expirationDate) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_ID, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, CukesConstants.SUBGROUP_LEVEL_CODE, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(SUBGROUP_ITEM_ID_BY_EXPIRATION_DATE, paramMap, String.class);
    }

    public String validateCIPEntryCreatedForRealCustomer(Date effectiveDate, int ItemPriceLevelCode) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_ID, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, ItemPriceLevelCode, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(CIP_ENRTY_BY_EFFECTIVE_DATE, paramMap, String.class);
    }

    public String validateSubgroupIsCreatedForRealCustomer(Date effectiveDate) {

        return validateCIPEntryCreatedForRealCustomer(effectiveDate, CukesConstants.SUBGROUP_LEVEL_CODE);
    }

    public String validateProductTypeIsCreatedForRealCustomer(Date effectiveDate) {

        return validateCIPEntryCreatedForRealCustomer(effectiveDate, CukesConstants.PRODUCT_LEVEL_CODE);
    }

    private void addItemLevelEntryToCPPFurtheranceTracking(AcceptableItems acceptableItem, int furtheranceActionCode) {
        List<CPPFurtheranceTrackingDTO> furtheranceWrapperDTOList = new ArrayList<>();

        CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = new CPPFurtheranceTrackingDTO();
        cppFurtheranceTrackingDTO.setChangeTableName("CUSTOMER_ITEM_PRICE");
        cppFurtheranceTrackingDTO.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        cppFurtheranceTrackingDTO.setFurtheranceActionCode(furtheranceActionCode);
        cppFurtheranceTrackingDTO.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        cppFurtheranceTrackingDTO.setGfsCustomerTypeCode(CukesConstants.CMG_CUSTOMER_TYPE_ID);
        cppFurtheranceTrackingDTO.setItemPriceId(acceptableItem.getItemId());
        cppFurtheranceTrackingDTO.setItemPriceLevelCode(acceptableItem.getItemPriceLevelCode());

        furtheranceWrapperDTOList.add(cppFurtheranceTrackingDTO);

        cppFurtheranceTrackingRepository.batchInsertRecordsInFurtheranceTracking(furtheranceWrapperDTOList, CukesConstants.CURRENT_USER_ID);
    }

    private void addItemToMarkup(Date effectiveDate, Date expirationDate, AcceptableItems acceptableItem) {
        List<ProductTypeMarkupDO> markupList = new ArrayList<>();

        ProductTypeMarkupDO markupDO = new ProductTypeMarkupDO();
        markupDO.setCustomerTypeCode(CukesConstants.CMG_CUSTOMER_TYPE_ID);
        markupDO.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        markupDO.setProductType(String.valueOf(acceptableItem.getItemPriceLevelCode()));
        markupDO.setItemPriceId(Integer.valueOf(acceptableItem.getItemId()));
        markupDO.setEffectiveDate(effectiveDate);
        markupDO.setExpirationDate(expirationDate);
        markupDO.setPriceLockedInTypeCode(3);
        markupDO.setHoldCostFirmInd(0);
        markupDO.setMarkup(new BigDecimal(CukesConstants.MARKUP_VALUE));
        markupDO.setUnit(CukesConstants.DEFAULT_UNIT);
        markupDO.setMarkupType(2);
        markupDO.setPriceLockinReasonCode(3);
        markupDO.setPriceMaintenanceSourceCode(13);
        markupDO.setContractPriceProfileSeq(getContractPriceProfileSeq());
        markupList.add(markupDO);

        customerItemPriceRepository.saveMarkup(markupList, CukesConstants.CURRENT_USER_ID, getContractPriceProfileSeq());
    }

    public void updateFurtheranceStatus(int cppFurtheranceSequence, FurtheranceStatus furtheranceStatus) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(FURTHERANCE_STATUS_CODE, furtheranceStatus.getCode(), Types.INTEGER);
        paramMap.addValue(CPP_FURTHERANCE_SEQ, cppFurtheranceSequence, Types.INTEGER);
        cppJdbcTemplate.update(UPDATE_CPP_FURTHERANCE_STATUS, paramMap);
    }

    public String validateCIPEntriesForExistingContract(AcceptableItems acceptableItem, AcceptableRealCustomer realCustomer,
            Date existingCustomerPricingExpirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, existingCustomerPricingExpirationDate, Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_ID, realCustomer.getCustomerId(), Types.VARCHAR);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, acceptableItem.getItemPriceLevelCode(), Types.INTEGER);
        return cppJdbcTemplate.queryForObject(EXPIRED_CUSTOMER_ITEM_PRICING_COUNT, paramMap, String.class);
    }

}
