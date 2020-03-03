package com.gfs.cpp.data.markup;

import static com.gfs.cpp.data.markup.ItemTypeMarkupRowMapper.FETCH_ITEM_CUSTOMER_ITEM_PRICE;
import static com.gfs.cpp.data.markup.MarkupResultSetExtractor.FETCH_FURTHERANCE_UPDATES_FOR_REAL_CUSTOMER;
import static com.gfs.cpp.data.markup.ProductTypeMarkupRowMapper.FETCH_CUSTOMER_ITEM_PRICE;
import static com.gfs.cpp.data.markup.ProductTypeMarkupRowMapper.FETCH_CUSTOMER_ITEM_PRICE_ALL;
import static com.gfs.cpp.data.markup.ProductTypeMarkupRowMapper.FETCH_CUSTOMER_ITEM_PRICE_FOR_ALL_CMG_BY_CPP_SEQ;
import static com.gfs.cpp.data.markup.ProductTypeMarkupRowMapper.FETCH_EXISTING_BID_PRICE_LOCKIN_ENTRIES;
import static com.gfs.cpp.data.markup.ProductTypeMarkupRowMapper.FETCH_PRICING_FOR_REAL_CUSTOMER;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.component.price.common.types.PriceLockinReason;
import com.gfs.corp.component.price.common.types.PriceMaintenanceSource;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;

@Repository(value = "customerItemPriceRepository")
public class CustomerItemPriceRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private MarkupResultSetExtractor markupResultSetExtractor;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String PRICE_LOCKED_IN_TYPE_CODE = "PRICE_LOCKED_IN_TYPE_CODE";
    private static final String HOLD_COST_FIRM_IND = "HOLD_COST_FIRM_IND";
    private static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    private static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";
    private static final String PRICE_LOCKIN_REASON_CODE = "PRICE_LOCKIN_REASON_CODE";
    private static final String PRICE_MAINTENANCE_SOURCE_CODE = "PRICE_MAINTENANCE_SOURCE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String NEW_EFFECTIVE_DATE = "NEW_EFFECTIVE_DATE";
    private static final String NEW_EXPIRATION_DATE = "NEW_EXPIRATION_DATE";
    private static final String LIST_OF_IDS = "LIST_OF_IDS";
    private static final String CUSTOMER_DETAIL = "CUSTOMER_DETAIL";
    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";

    // @formatter:off
    private static final String INSERT_CUSTOMER_ITEM_PRICE = "Insert into CUSTOMER_ITEM_PRICE "
            + "(CUSTOMER_ITEM_PRICE_SEQ,"
            + "GFS_CUSTOMER_ID ,"
            + " GFS_CUSTOMER_TYPE_CODE,"
            + "ITEM_PRICE_LEVEL_CODE,"
            + "ITEM_PRICE_ID,"
            + " EFFECTIVE_DATE ,"
            + "EXPIRATION_DATE, "
            + "CREATE_USER_ID ,"
            + "LAST_UPDATE_USER_ID ,"
            + "PRICE_LOCKED_IN_TYPE_CODE,"
            + "HOLD_COST_FIRM_IND,"
            + "COST_MARKUP_AMT ,"
            + "MARKUP_AMOUNT_TYPE_CODE, "
            + "MARKUP_UNIT_TYPE_CODE,"
            + "PRICE_LOCKIN_REASON_CODE,"
            + "PRICE_MAINTENANCE_SOURCE_CODE,"
            + "CONTRACT_PRICE_PROFILE_SEQ)  "
            + "values (CUSTOMER_ITEM_PRICE_SEQ.nextVal,"
            + ":GFS_CUSTOMER_ID, "
            + ":GFS_CUSTOMER_TYPE_CODE, "
            + ":ITEM_PRICE_LEVEL_CODE,"
            + ":ITEM_PRICE_ID,"
            + ":EFFECTIVE_DATE,"
            + ":EXPIRATION_DATE,"
            + ":CREATE_USER_ID ,"
            + ":LAST_UPDATE_USER_ID ,"
            + ":PRICE_LOCKED_IN_TYPE_CODE,"
            + ":HOLD_COST_FIRM_IND,"
            + ":COST_MARKUP_AMT,"
            + ":MARKUP_AMOUNT_TYPE_CODE,"
            + ":MARKUP_UNIT_TYPE_CODE,"
            + ":PRICE_LOCKIN_REASON_CODE,"
            + ":PRICE_MAINTENANCE_SOURCE_CODE,"
            + ":CONTRACT_PRICE_PROFILE_SEQ )";
    
    private static final String UPDATE_CUSTOMER_ITEM_PRICE = "Update CUSTOMER_ITEM_PRICE SET "
            + "GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE, "
            + "LAST_UPDATE_TMSTMP = SYSTIMESTAMP, "
            + "ITEM_PRICE_LEVEL_CODE=:ITEM_PRICE_LEVEL_CODE,"
            + "EFFECTIVE_DATE=:EFFECTIVE_DATE,"
            + "EXPIRATION_DATE=:EXPIRATION_DATE, "
            + "LAST_UPDATE_USER_ID =:LAST_UPDATE_USER_ID , "
            + "PRICE_LOCKED_IN_TYPE_CODE=:PRICE_LOCKED_IN_TYPE_CODE, "
            + "HOLD_COST_FIRM_IND=:HOLD_COST_FIRM_IND, "
            + "COST_MARKUP_AMT =:COST_MARKUP_AMT, "
            + "MARKUP_AMOUNT_TYPE_CODE=:MARKUP_AMOUNT_TYPE_CODE, "
            + "MARKUP_UNIT_TYPE_CODE=:MARKUP_UNIT_TYPE_CODE, "
            + "PRICE_LOCKIN_REASON_CODE=:PRICE_LOCKIN_REASON_CODE, "
            + "PRICE_MAINTENANCE_SOURCE_CODE=:PRICE_MAINTENANCE_SOURCE_CODE "
            + "where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID and ITEM_PRICE_ID=:ITEM_PRICE_ID";
    
    private static final String DELETE_CUSTOMER_ITEM_PRICE = "Delete from CUSTOMER_ITEM_PRICE where "
            + " CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID ";
    
    private static final String DELETE_CUSTOMER_ITEM_PRICE_FOR_ITEM_OR_SUBGROUP = "Delete from CUSTOMER_ITEM_PRICE where "
            + "CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + " and ITEM_PRICE_ID IN (:LIST_OF_IDS) "
            + " and ITEM_PRICE_LEVEL_CODE = :ITEM_PRICE_LEVEL_CODE " 
            + " and GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " and GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE ";
    
    private static final String FETCH_COUNT_CUSTOMER_ITEM_PRICE = "Select count(*) "
            + " from CUSTOMER_ITEM_PRICE "
            + " inner join CONTRACT_PRICE_PROF_CUSTOMER "
            + " on CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ = CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ"
            + " and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID"
            + " and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_TYPE_CODE = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE"
            + " where CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " and CUSTOMER_ITEM_PRICE.EXPIRATION_DATE>=SYSDATE";
    
    private static final String EXPIRE_ITEM_PRICE_FOR_REAL_CUST = "UPDATE CUSTOMER_ITEM_PRICE"
            + " SET EXPIRATION_DATE              = :EXPIRATION_DATE, "
            + " LAST_UPDATE_TMSTMP               = SYSTIMESTAMP, "
            + " LAST_UPDATE_USER_ID              = :LAST_UPDATE_USER_ID "
            + " WHERE GFS_CUSTOMER_ID            = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE       = :GFS_CUSTOMER_TYPE_CODE"
            + " AND ITEM_PRICE_ID                = :ITEM_PRICE_ID "
            + " AND ITEM_PRICE_LEVEL_CODE        = :ITEM_PRICE_LEVEL_CODE "
            + " AND EXPIRATION_DATE              >= :NEW_EFFECTIVE_DATE"
            + " AND EFFECTIVE_DATE               <= :NEW_EXPIRATION_DATE"
            + " AND EFFECTIVE_DATE               < EXPIRATION_DATE"
            + " AND PRICE_LOCKIN_REASON_CODE NOT IN ( " + PriceLockinReason.AWARDED_BID.getCode()  + "," + PriceLockinReason.NON_AWARDED_BID.getCode() + " ) ";

    private static final String FETCH_ITEM_SUBGROUP_IN_CUSTOMER_ITEM_PRICE = "Select distinct(ITEM_PRICE_ID) "
            + " from CUSTOMER_ITEM_PRICE "
            + " where ITEM_PRICE_ID IN (:LIST_OF_IDS) "
            + " and ITEM_PRICE_LEVEL_CODE = :ITEM_PRICE_LEVEL_CODE" 
            + " and GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " and GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE "
            + " and CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " and EXPIRATION_DATE>SYSDATE ";

    private static final String EXPIRE_NON_CMG_CUSTOMER_ITEM_PRICE_FOR_CONTRACT = "UPDATE CUSTOMER_ITEM_PRICE "
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, "
            + "LAST_UPDATE_TMSTMP               = SYSTIMESTAMP, "
            + "LAST_UPDATE_USER_ID              = :LAST_UPDATE_USER_ID "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND EFFECTIVE_DATE <= EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_TYPE_CODE != "+CPPConstants.CMG_CUSTOMER_TYPE_CODE+"";
    
    private static final String FETCH_SELL_PRICE_IND_FROM_CUSTOMER_ITEM_PRICE = "Select count(*) from CUSTOMER_ITEM_PRICE "
            + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND markup_amount_type_code= '" + AmountType.PERCENT.getCode() + "'";
    
    private static final String FETCH_ALL_GFS_CUTSTOMER_ID_FROM_CUSTOMER_ITEM_PRICE = "Select distinct(GFS_CUSTOMER_ID) "
            + "from CUSTOMER_ITEM_PRICE "
            + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND EXPIRATION_DATE             >= SYSDATE";
    
    private static final String EXPIRE_ITEM_PRICE_ID = "UPDATE CUSTOMER_ITEM_PRICE "
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, "
            + "LAST_UPDATE_TMSTMP               = SYSTIMESTAMP, "
            + "LAST_UPDATE_USER_ID              = :LAST_UPDATE_USER_ID "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE "
            + "AND ITEM_PRICE_ID = :ITEM_PRICE_ID "
            + "AND ITEM_PRICE_LEVEL_CODE = :ITEM_PRICE_LEVEL_CODE";

            
    // @formatter:on

    public Map<Integer, List<ProductTypeMarkupDTO>> fetchMarkupsForRealCustomersForFurtherance(int cppFurtheranceSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_FURTHERANCE_SEQ, cppFurtheranceSeq, Types.NUMERIC);
        return cppJdbcTemplate.query(FETCH_FURTHERANCE_UPDATES_FOR_REAL_CUSTOMER, paramMap, markupResultSetExtractor);
    }

    public List<ProductTypeMarkupDTO> fetchProductTypeMarkups(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.NUMERIC);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, ItemPriceLevel.PRODUCT_TYPE.getCode(), Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_CUSTOMER_ITEM_PRICE, paramMap, new ProductTypeMarkupRowMapper());
    }

    public int fetchSellPriceIndCount(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_SELL_PRICE_IND_FROM_CUSTOMER_ITEM_PRICE, paramMap, Integer.class);
    }

    public List<ProductTypeMarkupDTO> fetchMarkupsForCMGs(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.NUMERIC);
        return cppJdbcTemplate.query(FETCH_CUSTOMER_ITEM_PRICE_FOR_ALL_CMG_BY_CPP_SEQ, paramMap, new ProductTypeMarkupRowMapper());
    }

    public List<ProductTypeMarkupDTO> fetchAllMarkup(int contractPriceProfileSeq, String gfsCustomerId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        return cppJdbcTemplate.query(FETCH_CUSTOMER_ITEM_PRICE_ALL, paramMap, new ProductTypeMarkupRowMapper());
    }

    public void saveMarkup(List<ProductTypeMarkupDO> markupList, String userName, int contractPriceProfileSeq) {
        List<SqlParameterSource> paramList = buildMarkupWrapperDO(markupList, userName, contractPriceProfileSeq);
        cppJdbcTemplate.batchUpdate(INSERT_CUSTOMER_ITEM_PRICE, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public int fetchMarkupCount(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_COUNT_CUSTOMER_ITEM_PRICE, paramMap, Integer.class);
    }

    public void updateMarkup(List<ProductTypeMarkupDO> markupList, String userName, int contractPriceProfileSeq) {
        List<SqlParameterSource> paramList = buildMarkupWrapperDO(markupList, userName, contractPriceProfileSeq);
        cppJdbcTemplate.batchUpdate(UPDATE_CUSTOMER_ITEM_PRICE, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public void deleteExceptionData(int contractPriceProfileSeq, String gfsCustomerId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_CUSTOMER_ITEM_PRICE, paramMap);
    }

    public List<ItemLevelMarkupDTO> fetchItemTypeMarkups(int contractPriceProfileSeq, String gfsCustomerId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, ItemPriceLevel.ITEM.getCode(), Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_ITEM_CUSTOMER_ITEM_PRICE, paramMap, new ItemTypeMarkupRowMapper());
    }

    public void deleteExistingItemOrSubgroupMarkup(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerType, List<String> itemIdList,
            int itemPriceLevelCode) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(LIST_OF_IDS, itemIdList, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerType, Types.VARCHAR);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, itemPriceLevelCode, Types.INTEGER);

        cppJdbcTemplate.update(DELETE_CUSTOMER_ITEM_PRICE_FOR_ITEM_OR_SUBGROUP, paramMap);
    }

    public List<String> fetchAlreadyExistingItemsOrSubgroups(List<String> itemIdList, String gfsCustomerId, int gfsCustomerTypeCode,
            int contractPriceProfileSeq, int itemLevelCode) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(LIST_OF_IDS, itemIdList, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, itemLevelCode, Types.INTEGER);
        return cppJdbcTemplate.queryForList(FETCH_ITEM_SUBGROUP_IN_CUSTOMER_ITEM_PRICE, paramMap, String.class);
    }

    public void expireNonCmgPriceForContract(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        cppJdbcTemplate.update(EXPIRE_NON_CMG_CUSTOMER_ITEM_PRICE_FOR_CONTRACT, paramMap);
    }

    public List<ProductTypeMarkupDTO> fetchPricingForRealCustomer(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRICING_FOR_REAL_CUSTOMER, paramMap, new ProductTypeMarkupRowMapper());
    }

    public List<ProductTypeMarkupDTO> fetchExistingBidLockinEntriesForCustomer(List<CustomerPK> customerHierarchy, List<String> itemPriceIdList,
            Date effectiveDate, Date expirationDate) {
        SqlParameterSource params = buildParamMapForAllItemAndCustomersHierarchy(customerHierarchy, itemPriceIdList, effectiveDate, expirationDate);
        return cppJdbcTemplate.query(FETCH_EXISTING_BID_PRICE_LOCKIN_ENTRIES, params, new ProductTypeMarkupRowMapper());
    }

    public void expireItemPricingForRealCustomer(Date expirationDate, String updatedUserId, List<ProductTypeMarkupDO> productTypeMarkupDOList,
            Date newPricingEffectiveDate, Date newPricingExpiryDate) {
        List<SqlParameterSource> paramList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(productTypeMarkupDOList)) {
            for (ProductTypeMarkupDO customerDetail : productTypeMarkupDOList) {
                MapSqlParameterSource paramMap = buildParamMapForExpiringItemPricingRealCust(expirationDate, updatedUserId, newPricingEffectiveDate,
                        newPricingExpiryDate, customerDetail);
                paramList.add(paramMap);
            }
            cppJdbcTemplate.batchUpdate(EXPIRE_ITEM_PRICE_FOR_REAL_CUST, paramList.toArray(new SqlParameterSource[paramList.size()]));
        }
    }

    public List<String> fetchAllGfsCustomerIdsInCustomerItemPrice(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForList(FETCH_ALL_GFS_CUTSTOMER_ID_FROM_CUSTOMER_ITEM_PRICE, paramMap, String.class);
    }

    private MapSqlParameterSource buildParamMapForExpiringItemPricingRealCust(Date expirationDate, String updatedUserId, Date newPricingEffectiveDate,
            Date newPricingExpiryDate, ProductTypeMarkupDO productTypeMarkupDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, productTypeMarkupDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, productTypeMarkupDO.getCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_ID, productTypeMarkupDO.getItemPriceId(), Types.VARCHAR);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, productTypeMarkupDO.getProductType(), Types.INTEGER);
        paramMap.addValue(NEW_EFFECTIVE_DATE, newPricingEffectiveDate, Types.DATE);
        paramMap.addValue(NEW_EXPIRATION_DATE, newPricingExpiryDate, Types.DATE);
        return paramMap;
    }

    public void expireItemPricing(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode, List<String> itemPriceIdList,
            int itemPriceLevelCode, Date expirationDate, String userName) {

        List<SqlParameterSource> paramList = buildParamMapForExpiringItemPricing(expirationDate, userName, gfsCustomerId, gfsCustomerTypeCode,
                itemPriceIdList, itemPriceLevelCode, contractPriceProfileSeq);
        cppJdbcTemplate.batchUpdate(EXPIRE_ITEM_PRICE_ID, paramList.toArray(new SqlParameterSource[paramList.size()]));

    }

    private List<SqlParameterSource> buildParamMapForExpiringItemPricing(Date expirationDate, String userName, String gfsCustomerId,
            int gfsCustomerTypeCode, List<String> itemPriceIdList, int itemPriceLevelCode, int contractPriceProfileSeq) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(itemPriceIdList)) {
            for (String itemPriceId : itemPriceIdList) {
                MapSqlParameterSource paramMap = new MapSqlParameterSource();
                paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
                paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
                paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
                paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
                paramMap.addValue(ITEM_PRICE_ID, itemPriceId, Types.VARCHAR);
                paramMap.addValue(ITEM_PRICE_LEVEL_CODE, itemPriceLevelCode, Types.INTEGER);
                paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
                paramList.add(paramMap);
            }
        }
        return paramList;
    }

    private List<SqlParameterSource> buildMarkupWrapperDO(List<ProductTypeMarkupDO> markupList, String userName, int contractPriceProfileSeq) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (ProductTypeMarkupDO markupToSave : markupList) {
            MapSqlParameterSource paramMapMarkupData = new MapSqlParameterSource();
            paramMapMarkupData.addValue(GFS_CUSTOMER_ID, markupToSave.getGfsCustomerId(), Types.VARCHAR);
            paramMapMarkupData.addValue(GFS_CUSTOMER_TYPE_CODE, markupToSave.getCustomerTypeCode(), Types.INTEGER);
            paramMapMarkupData.addValue(ITEM_PRICE_LEVEL_CODE, markupToSave.getProductType(), Types.INTEGER);
            paramMapMarkupData.addValue(ITEM_PRICE_ID, markupToSave.getItemPriceId(), Types.VARCHAR);
            paramMapMarkupData.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
            paramMapMarkupData.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
            paramMapMarkupData.addValue(EFFECTIVE_DATE, markupToSave.getEffectiveDate(), Types.DATE);
            paramMapMarkupData.addValue(EXPIRATION_DATE, markupToSave.getExpirationDate(), Types.DATE);
            paramMapMarkupData.addValue(PRICE_LOCKED_IN_TYPE_CODE, markupToSave.getPriceLockedInTypeCode(), Types.INTEGER);
            paramMapMarkupData.addValue(HOLD_COST_FIRM_IND, CPPConstants.INDICATOR_ZERO, Types.INTEGER);
            paramMapMarkupData.addValue(COST_MARKUP_AMT, markupToSave.getMarkup(), Types.NUMERIC);
            paramMapMarkupData.addValue(MARKUP_AMOUNT_TYPE_CODE, markupToSave.getUnit(), Types.VARCHAR);
            paramMapMarkupData.addValue(MARKUP_UNIT_TYPE_CODE, markupToSave.getMarkupType(), Types.INTEGER);
            paramMapMarkupData.addValue(PRICE_LOCKIN_REASON_CODE, markupToSave.getPriceLockinReasonCode(), Types.INTEGER);
            paramMapMarkupData.addValue(PRICE_MAINTENANCE_SOURCE_CODE, PriceMaintenanceSource.CPP.getCode(), Types.INTEGER);
            paramMapMarkupData.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
            paramList.add(paramMapMarkupData);
        }
        return paramList;
    }

    private SqlParameterSource buildParamMapForAllItemAndCustomersHierarchy(List<CustomerPK> customerHierarchy, List<String> itemPriceIdList,
            Date effectiveDate, Date expirationDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(ITEM_PRICE_ID, itemPriceIdList, Types.VARCHAR);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, ItemPriceLevel.ITEM.getCode(), Types.INTEGER);
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        List<String[]> customerList = new ArrayList<>();
        if (customerHierarchy != null && !customerHierarchy.isEmpty()) {
            for (CustomerPK customerPK : customerHierarchy) {
                String[] entry = { customerPK.getId(), String.valueOf(customerPK.getTypeCode()) };
                customerList.add(entry);
            }
        }
        paramMap.addValue(CUSTOMER_DETAIL, customerList, Types.VARCHAR);
        return paramMap;
    }

    public void expireItemPricingForDeletedItemsDuringFurtherance(Date expirationDate, String updatedUserId,
            List<ProductTypeMarkupDO> productTypeMarkupDOList) {

        List<SqlParameterSource> paramList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(productTypeMarkupDOList)) {
            for (ProductTypeMarkupDO productTypeMarkupDO : productTypeMarkupDOList) {
                MapSqlParameterSource paramMap = buildParamMapForExpiringItemPricingRealCust(expirationDate, updatedUserId, null, null,
                        productTypeMarkupDO);
                paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, productTypeMarkupDO.getContractPriceProfileSeq(), Types.INTEGER);
                paramList.add(paramMap);
            }
            cppJdbcTemplate.batchUpdate(EXPIRE_ITEM_PRICE_ID, paramList.toArray(new SqlParameterSource[paramList.size()]));
        }
    }
}
