package com.gfs.cpp.data.contractpricing;

import java.sql.Types;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;

@Component(value = "prcProfLessCaseRuleParamListBuilder")
public class PrcProfLessCaseRuleParamListBuilder {

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String CW_MARKUP_AMT = "CW_MARKUP_AMT";
    private static final String NON_CW_MARKUP_AMT = "NON_CW_MARKUP_AMT";
    private static final String CW_MARKUP_AMOUNT_TYPE_CODE = "CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String NON_CW_MARKUP_AMOUNT_TYPE_CODE = "NON_CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String LESSCASE_PRICE_RULE_ID = "LESSCASE_PRICE_RULE_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";


    public MapSqlParameterSource createParamMap(PrcProfLessCaseRuleDO prcProfLessCaseRuleDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, prcProfLessCaseRuleDO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, prcProfLessCaseRuleDO.getLastUpdateUserId(), Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, prcProfLessCaseRuleDO.getCreateUserId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, prcProfLessCaseRuleDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(EFFECTIVE_DATE, prcProfLessCaseRuleDO.getEffectiveDate(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, prcProfLessCaseRuleDO.getExpirationDate(), Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, prcProfLessCaseRuleDO.getGfsCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, prcProfLessCaseRuleDO.getItemPriceLevelCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_ID, prcProfLessCaseRuleDO.getItemPriceId(), Types.VARCHAR);
        paramMap.addValue(LESSCASE_PRICE_RULE_ID, prcProfLessCaseRuleDO.getLesscaseRuleId(), Types.INTEGER);
        paramMap.addValue(CW_MARKUP_AMT, prcProfLessCaseRuleDO.getCwMarkupAmnt(), Types.INTEGER);
        paramMap.addValue(CW_MARKUP_AMOUNT_TYPE_CODE, prcProfLessCaseRuleDO.getCwMarkupAmountTypeCode(), Types.VARCHAR);
        paramMap.addValue(NON_CW_MARKUP_AMT, prcProfLessCaseRuleDO.getNonCwMarkupAmnt(), Types.INTEGER);
        paramMap.addValue(NON_CW_MARKUP_AMOUNT_TYPE_CODE, prcProfLessCaseRuleDO.getNonCwMarkupAmntTypeCode(), Types.VARCHAR);

        return paramMap;

    }

}
