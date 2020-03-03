package com.gfs.cpp.data.markup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.furtherance.FurtheranceAction;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.util.CPPDateUtils;

@Component
public class MarkupResultSetExtractor implements ResultSetExtractor<Map<Integer, List<ProductTypeMarkupDTO>>> {

    @Autowired
    private CPPDateUtils cppDateUtils;

    private static final String FURTHERANCE_ACTION_CODE = "FURTHERANCE_ACTION_CODE";
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
    public static final String FETCH_FURTHERANCE_UPDATES_FOR_REAL_CUSTOMER="SELECT "
            + "CIP.CUSTOMER_ITEM_PRICE_SEQ, "
            + "MAP.GFS_CUSTOMER_TYPE_CODE, "
            + "MAP.GFS_CUSTOMER_ID, "
            + "CIP.EXPIRATION_DATE, "
            + "CIP.MARKUP_AMOUNT_TYPE_CODE, "
            + "CIP.COST_MARKUP_AMT, "
            + "CIP. ITEM_PRICE_ID, "
            + "CIP.MARKUP_UNIT_TYPE_CODE, "
            + "CIP.CONTRACT_PRICE_PROFILE_SEQ, "
            + "CIP.ITEM_PRICE_LEVEL_CODE, "
            + "CIP.PRICE_LOCKED_IN_TYPE_CODE, "
            + "CIP.HOLD_COST_FIRM_IND, "
            + "CIP.PRICE_LOCKIN_REASON_CODE, "
            + "CIP.PRICE_MAINTENANCE_SOURCE_CODE, "
            + "CFT.FURTHERANCE_ACTION_CODE , "
            + "CF.FURTHERANCE_EFFECTIVE_DATE AS EFFECTIVE_DATE "
        + " FROM "
            + " CPP_CONCEPT_MAPPING MAP "
            + " INNER JOIN CONTRACT_PRICE_PROF_CUSTOMER CPPC "
                + " ON CPPC.CPP_CUSTOMER_SEQ = MAP.CPP_CUSTOMER_SEQ "
            + " INNER JOIN CPP_FURTHERANCE_TRACKING CFT "
                + " ON CFT.GFS_CUSTOMER_ID = CPPC.GFS_CUSTOMER_ID "
                + " AND CFT.GFS_CUSTOMER_TYPE_CODE = CPPC.GFS_CUSTOMER_TYPE_CODE "
            + " INNER JOIN CPP_FURTHERANCE CF "
                + "ON CF.CPP_FURTHERANCE_SEQ = CFT.CPP_FURTHERANCE_SEQ "
                + "AND CPPC.CONTRACT_PRICE_PROFILE_SEQ = CF.CONTRACT_PRICE_PROFILE_SEQ "
            + " INNER JOIN CUSTOMER_ITEM_PRICE CIP "
                + " ON CIP.CONTRACT_PRICE_PROFILE_SEQ = CPPC.CONTRACT_PRICE_PROFILE_SEQ "
                + " AND CIP.GFS_CUSTOMER_ID = CPPC.GFS_CUSTOMER_ID "
                + " AND CIP.GFS_CUSTOMER_TYPE_CODE = CPPC.GFS_CUSTOMER_TYPE_CODE "
                + " AND CIP.ITEM_PRICE_ID = CFT.ITEM_PRICE_ID " 
                + " AND CIP.ITEM_PRICE_LEVEL_CODE =  CFT.ITEM_PRICE_LEVEL_CODE "
        + " WHERE "
            + " CF.CPP_FURTHERANCE_SEQ = :CPP_FURTHERANCE_SEQ "
            + " AND CF.FURTHERANCE_STATUS_CODE="+FurtheranceStatus.FURTHERANCE_SAVED.getCode()
            + " AND CFT.CHANGE_TABLE_NAME='"+CPPConstants.CIP_TABLE_NAMES+"'" ;
    
    //@formatter:on

    @Override
    public Map<Integer, List<ProductTypeMarkupDTO>> extractData(ResultSet rs) throws SQLException {
        HashMap<Integer, List<ProductTypeMarkupDTO>> returnMap = new HashMap<>();

        Date currentDate = cppDateUtils.getCurrentDate();
        while (rs.next()) {

            int furtheranceActionCode = rs.getInt(FURTHERANCE_ACTION_CODE);
            List<ProductTypeMarkupDTO> productTypeMarkupDTOList = returnMap.get(furtheranceActionCode);

            if (productTypeMarkupDTOList == null) {
                productTypeMarkupDTOList = new ArrayList<>();
                returnMap.put(furtheranceActionCode, productTypeMarkupDTOList);
            }
            Date recordCIPExpirationDate = rs.getDate(EXPIRATION_DATE);
            if (!skipRecord(recordCIPExpirationDate, furtheranceActionCode, currentDate)) {
                productTypeMarkupDTOList.add(buildProductTypeMarkupDTO(rs));
            }

        }
        return returnMap;
    }

    private boolean skipRecord(Date recordCIPExpirationDate, int furtheranceActionCode, Date currentDate) {
        boolean isRecordToBeSkipped = false;
        if ((FurtheranceAction.UPDATED.getCode() == furtheranceActionCode || ( FurtheranceAction.ADDED.getCode()==furtheranceActionCode) ) && currentDate.compareTo(recordCIPExpirationDate) > 0) {
            isRecordToBeSkipped = true;
        }
        return isRecordToBeSkipped;
    }

    private ProductTypeMarkupDTO buildProductTypeMarkupDTO(ResultSet rs) throws SQLException {
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