package com.gfs.cpp.data.contractpricing;

import java.sql.Types;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;

@Component(value = "prcProfNonBrktCstMdlParamListBuilder")
public class PrcProfNonBrktCstMdlParamListBuilder {

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String COST_MODEL_ID = "COST_MODEL_ID";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
   
    public MapSqlParameterSource createParamMap(PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, prcProfNonBrktCstMdlDO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(EFFECTIVE_DATE, prcProfNonBrktCstMdlDO.getEffectiveDate(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, prcProfNonBrktCstMdlDO.getExpirationDate(), Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, prcProfNonBrktCstMdlDO.getLastUpdateUserId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, prcProfNonBrktCstMdlDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, prcProfNonBrktCstMdlDO.getGfsCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, prcProfNonBrktCstMdlDO.getItemPriceLevelCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_ID, prcProfNonBrktCstMdlDO.getItemPriceId(), Types.VARCHAR);
        paramMap.addValue(COST_MODEL_ID, prcProfNonBrktCstMdlDO.getCostModelId(), Types.INTEGER);
        paramMap.addValue(CREATE_USER_ID, prcProfNonBrktCstMdlDO.getCreateUserId(), Types.VARCHAR);
        return paramMap;

    }
}
