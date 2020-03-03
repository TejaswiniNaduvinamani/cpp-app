package com.gfs.cpp.data.contractpricing;

import java.sql.Types;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;

@Component(value = "prcProfPricingRuleOvrdParamListBuilder")
public class PrcProfPricingRuleOvrdParamListBuilder {
 
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String PRICING_OVERRIDE_ID = "PRICING_OVERRIDE_ID";
    private static final String PRICING_OVERRIDE_IND = "PRICING_OVERRIDE_IND";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";

    public MapSqlParameterSource createParamMap(PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, prcProfPricingRuleOvrdDO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, prcProfPricingRuleOvrdDO.getLastUpdateUserId(), Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, prcProfPricingRuleOvrdDO.getCreateUserId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, prcProfPricingRuleOvrdDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(EFFECTIVE_DATE, prcProfPricingRuleOvrdDO.getEffectiveDate(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, prcProfPricingRuleOvrdDO.getExpirationDate(), Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, prcProfPricingRuleOvrdDO.getGfsCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(PRICING_OVERRIDE_ID, prcProfPricingRuleOvrdDO.getPricingOverrideId(), Types.INTEGER);
        paramMap.addValue(PRICING_OVERRIDE_IND, prcProfPricingRuleOvrdDO.getPricingOverrideInd(), Types.INTEGER);

        return paramMap;

    }
}
