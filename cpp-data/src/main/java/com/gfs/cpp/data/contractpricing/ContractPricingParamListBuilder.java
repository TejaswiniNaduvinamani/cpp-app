package com.gfs.cpp.data.contractpricing;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;

@Component(value = "contractPricingParamListBuilder")
public class ContractPricingParamListBuilder {

    public static final Logger logger = LoggerFactory.getLogger(ContractPricingParamListBuilder.class);

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String PRC_PROF_AUDIT_AUTHORITY_IND = "PRC_PROF_AUDIT_AUTHORITY_IND";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";

    public MapSqlParameterSource createParamMap(String userName, int contractPriceProfileSeq, int contractPriceProfileId,
            ContractPricingDO contractPricingDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(PRC_PROF_AUDIT_AUTHORITY_IND, CPPConstants.INDICATOR_ONE, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_ID, contractPriceProfileId, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, contractPricingDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(EFFECTIVE_DATE, contractPricingDO.getEffectiveDateFuture(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, contractPricingDO.getExpirationDateFuture(), Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, contractPricingDO.getCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return paramMap;

    }

}
