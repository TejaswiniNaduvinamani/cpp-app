package com.gfs.cpp.data.markup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;

public class PrcProfPricingRuleOvrdRowMapper implements RowMapper<PrcProfPricingRuleOvrdDTO> {
    
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRICING_OVERRIDE_IND = "PRICING_OVERRIDE_IND";
    private static final String PRICING_OVERRIDE_ID = "PRICING_OVERRIDE_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    
    public static final String FETCH_PRC_PROF_PRICING_RULE_OVRD_FOR_CPP_SEQ="SELECT PRICING_OVERRIDE_IND, "
            + " PRICING_OVERRIDE_ID, "
            + " CONTRACT_PRICE_PROFILE_SEQ,"
            + " GFS_CUSTOMER_ID,"
            + " GFS_CUSTOMER_TYPE_CODE "
            + " FROM PRC_PROF_PRICING_RULE_OVRD "
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID "
            + "AND GFS_CUSTOMER_TYPE_CODE=:GFS_CUSTOMER_TYPE_CODE "
            + "AND EXPIRATION_DATE >= SYSDATE"; 
    
    @Override
    public PrcProfPricingRuleOvrdDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO = new PrcProfPricingRuleOvrdDTO();
        prcProfPricingRuleOvrdDTO.setPricingOverrideInd(rs.getInt(PRICING_OVERRIDE_IND));
        prcProfPricingRuleOvrdDTO.setPricingOverrideId(rs.getInt(PRICING_OVERRIDE_ID));
        prcProfPricingRuleOvrdDTO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        prcProfPricingRuleOvrdDTO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        prcProfPricingRuleOvrdDTO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        return prcProfPricingRuleOvrdDTO;
    }
}
