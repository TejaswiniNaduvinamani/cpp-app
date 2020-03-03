package com.gfs.cpp.data.markup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.corp.component.price.common.types.PriceLockinReason;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;

public class ProductTypeMarkupRowMapper implements RowMapper<ProductTypeMarkupDTO> {

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    private static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";
    private static final String CUSTOMER_ITEM_PRICE_SEQ = "CUSTOMER_ITEM_PRICE_SEQ";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String PRICE_LOCKED_IN_TYPE_CODE = "PRICE_LOCKED_IN_TYPE_CODE";
    private static final String HOLD_COST_FIRM_IND = "HOLD_COST_FIRM_IND";
    private static final String PRICE_LOCKIN_REASON_CODE = "PRICE_LOCKIN_REASON_CODE";
    private static final String PRICE_MAINTENANCE_SOURCE_CODE = "PRICE_MAINTENANCE_SOURCE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";

    //@formatter:off
    public static final String FETCH_CUSTOMER_ITEM_PRICE = "select CUSTOMER_ITEM_PRICE.ITEM_PRICE_ID,"
            + " CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID, "
            + " CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.EFFECTIVE_DATE, "
            + " CUSTOMER_ITEM_PRICE.EXPIRATION_DATE, "
            + " CUSTOMER_ITEM_PRICE.COST_MARKUP_AMT, "
            + " CUSTOMER_ITEM_PRICE.MARKUP_AMOUNT_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.MARKUP_UNIT_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ, "
            + " CUSTOMER_ITEM_PRICE.ITEM_PRICE_LEVEL_CODE, "
            + " CUSTOMER_ITEM_PRICE.PRICE_LOCKED_IN_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.CUSTOMER_ITEM_PRICE_SEQ, "
            + " CUSTOMER_ITEM_PRICE.HOLD_COST_FIRM_IND, "
            + " CUSTOMER_ITEM_PRICE.PRICE_LOCKIN_REASON_CODE, "
            + " CUSTOMER_ITEM_PRICE.PRICE_MAINTENANCE_SOURCE_CODE" 
            + " from CUSTOMER_ITEM_PRICE inner join CONTRACT_PRICE_PROF_CUSTOMER "
            + " on CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ= CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ"
            + " and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID= CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID"
            + " and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_TYPE_CODE= CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE"
            + " where CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ= :CONTRACT_PRICE_PROFILE_SEQ "
            + " and CUSTOMER_ITEM_PRICE.ITEM_PRICE_LEVEL_CODE= :ITEM_PRICE_LEVEL_CODE" 
            + " and CUSTOMER_ITEM_PRICE.EXPIRATION_DATE>=SYSDATE ";
    
    
    public static final String FETCH_CUSTOMER_ITEM_PRICE_FOR_ALL_CMG_BY_CPP_SEQ = "select CUSTOMER_ITEM_PRICE.ITEM_PRICE_ID, "
            + " CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID, "
            + " CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.EFFECTIVE_DATE, "
            + " CUSTOMER_ITEM_PRICE.EXPIRATION_DATE, "
            + " CUSTOMER_ITEM_PRICE.COST_MARKUP_AMT, "
            + " CUSTOMER_ITEM_PRICE.MARKUP_AMOUNT_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.MARKUP_UNIT_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ, "
            + " CUSTOMER_ITEM_PRICE.ITEM_PRICE_LEVEL_CODE, "
            + " CUSTOMER_ITEM_PRICE.PRICE_LOCKED_IN_TYPE_CODE, "
            + " CUSTOMER_ITEM_PRICE.CUSTOMER_ITEM_PRICE_SEQ, "
            + " CUSTOMER_ITEM_PRICE.HOLD_COST_FIRM_IND,"
            + " CUSTOMER_ITEM_PRICE.PRICE_LOCKIN_REASON_CODE, "
            + " CUSTOMER_ITEM_PRICE.PRICE_MAINTENANCE_SOURCE_CODE " 
            + " from CUSTOMER_ITEM_PRICE "
            + " inner join CONTRACT_PRICE_PROF_CUSTOMER "
            + " on CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ = CONTRACT_PRICE_PROF_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ "
            + " and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_ID "
            + " and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_TYPE_CODE = CONTRACT_PRICE_PROF_CUSTOMER.GFS_CUSTOMER_TYPE_CODE "
            + " where CUSTOMER_ITEM_PRICE.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " and CUSTOMER_ITEM_PRICE.EXPIRATION_DATE >= SYSDATE" ;
          
    
    public static final String FETCH_CUSTOMER_ITEM_PRICE_ALL = FETCH_CUSTOMER_ITEM_PRICE_FOR_ALL_CMG_BY_CPP_SEQ
            + " and CUSTOMER_ITEM_PRICE.GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " and CUSTOMER_ITEM_PRICE.EXPIRATION_DATE>=SYSDATE ";
    
    public static final String FETCH_PRICING_FOR_REAL_CUSTOMER = " select CIP.CUSTOMER_ITEM_PRICE_SEQ, "
            + " CPP_CONCEPT_MAPPING.GFS_CUSTOMER_TYPE_CODE ,"
            + " CPP_CONCEPT_MAPPING.GFS_CUSTOMER_ID, "
            + " CIP.EXPIRATION_DATE, "
            + " CIP.EFFECTIVE_DATE, "
            + " CIP.MARKUP_AMOUNT_TYPE_CODE, "
            + " CIP.COST_MARKUP_AMT,"
            + " CIP. ITEM_PRICE_ID,"
            + " CIP.MARKUP_UNIT_TYPE_CODE, "
            + " CIP.CONTRACT_PRICE_PROFILE_SEQ, "
            + " CIP.ITEM_PRICE_LEVEL_CODE, "
            + " CIP.PRICE_LOCKED_IN_TYPE_CODE, "
            + " CIP.HOLD_COST_FIRM_IND,"
            + " CIP.PRICE_LOCKIN_REASON_CODE, "
            + " CIP.PRICE_MAINTENANCE_SOURCE_CODE "
            + " from CONTRACT_PRICE_PROF_CUSTOMER CPP_CUSTOMER "
            + " INNER JOIN CPP_CONCEPT_MAPPING ON CPP_CUSTOMER.CPP_CUSTOMER_SEQ = CPP_CONCEPT_MAPPING.CPP_CUSTOMER_SEQ "
            + " INNER JOIN CUSTOMER_ITEM_PRICE CIP ON CIP.CONTRACT_PRICE_PROFILE_SEQ = CPP_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ "  
            + " AND CIP.GFS_CUSTOMER_ID = CPP_CUSTOMER.GFS_CUSTOMER_ID "
            + " AND CIP.GFS_CUSTOMER_TYPE_CODE = CPP_CUSTOMER.GFS_CUSTOMER_TYPE_CODE "
            + " WHERE CPP_CUSTOMER.CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " AND CIP.EXPIRATION_DATE>=SYSDATE";
     
    
    public static final String FETCH_EXISTING_BID_PRICE_LOCKIN_ENTRIES = " SELECT CIP.CUSTOMER_ITEM_PRICE_SEQ, cip.GFS_CUSTOMER_ID, cip.GFS_CUSTOMER_TYPE_CODE, "
            + " CIP.EXPIRATION_DATE, "
            + " CIP.EFFECTIVE_DATE, "
            + " CIP.MARKUP_AMOUNT_TYPE_CODE, "
            + " CIP.COST_MARKUP_AMT,"
            + " CIP. ITEM_PRICE_ID,"
            + " CIP.MARKUP_UNIT_TYPE_CODE, "
            + " CIP.CONTRACT_PRICE_PROFILE_SEQ, "
            + " CIP.ITEM_PRICE_LEVEL_CODE, "
            + " CIP.PRICE_LOCKED_IN_TYPE_CODE, "
            + " CIP.HOLD_COST_FIRM_IND,"
            + " CIP.PRICE_LOCKIN_REASON_CODE, "
            + " CIP.PRICE_MAINTENANCE_SOURCE_CODE "
            + " FROM CUSTOMER_ITEM_PRICE cip "
            + " WHERE (cip.GFS_CUSTOMER_ID, cip.GFS_CUSTOMER_TYPE_CODE) IN (:CUSTOMER_DETAIL) "
            + " AND cip.ITEM_PRICE_ID IN (:ITEM_PRICE_ID) "
            + " AND cip.ITEM_PRICE_LEVEL_CODE = :ITEM_PRICE_LEVEL_CODE "
            + " AND cip.EXPIRATION_DATE >= :EFFECTIVE_DATE "      
            + " AND cip.EFFECTIVE_DATE <= :EXPIRATION_DATE"        
            + " AND cip.EFFECTIVE_DATE <= cip.EXPIRATION_DATE"  
            + " AND cip.PRICE_LOCKIN_REASON_CODE  IN ( " + PriceLockinReason.AWARDED_BID.getCode() + ", " + PriceLockinReason.NON_AWARDED_BID.getCode()  + " ) ";
    

    //@formatter:on

    @Override
    public ProductTypeMarkupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductTypeMarkupDTO productTypeMarkup = new ProductTypeMarkupDTO();
        productTypeMarkup.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        productTypeMarkup.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        productTypeMarkup.setItemPriceId(rs.getInt(ITEM_PRICE_ID));
        productTypeMarkup.setEffectiveDate(rs.getDate(EFFECTIVE_DATE));
        productTypeMarkup.setExpirationDate(rs.getDate(EXPIRATION_DATE));
        productTypeMarkup.setMarkup(rs.getString(COST_MARKUP_AMT));
        productTypeMarkup.setMarkupType(rs.getInt(MARKUP_UNIT_TYPE_CODE));
        productTypeMarkup.setUnit(rs.getString(MARKUP_AMOUNT_TYPE_CODE));
        productTypeMarkup.setProductType(String.valueOf(rs.getInt(ITEM_PRICE_LEVEL_CODE)));
        productTypeMarkup.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        productTypeMarkup.setPriceLockedInTypeCode(rs.getInt(PRICE_LOCKED_IN_TYPE_CODE));
        productTypeMarkup.setHoldCostFirmInd(rs.getInt(HOLD_COST_FIRM_IND));
        productTypeMarkup.setPriceLockinReasonCode(rs.getInt(PRICE_LOCKIN_REASON_CODE));
        productTypeMarkup.setPriceMaintenanceSourceCode(rs.getInt(PRICE_MAINTENANCE_SOURCE_CODE));
        productTypeMarkup.setCustomerItemPriceSeq(rs.getInt(CUSTOMER_ITEM_PRICE_SEQ));

        return productTypeMarkup;
    }
}